package com.tpy.core.service.tran;
import java.lang.reflect.Method;

import ch.qos.logback.classic.joran.action.LoggerAction;
import com.tpy.pojo.manager.Transaction;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * cglib动态代理方法拦截器
 * @author Administrator
 *
 */
public class TranscationInterecptor implements MethodInterceptor{

    Logger log = LoggerFactory.getLogger(TranscationInterecptor.class);

    //增强对象(事务管理器)
    private TranscationManager transcationManager;
    public TranscationInterecptor(TranscationManager transcationManager) {
        this.transcationManager=transcationManager;
    }
    /**
     * proxy:生成动态代理对象
     * method：被拦截的方法
     * args:方法参数
     * MethodProxy:生成的代理方法对象
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //获取方法上的@Transaction注解
        Transaction annotation = method.getAnnotation(Transaction.class);
        long id = Thread.currentThread().getId();
        if(annotation==null){
            Object o = methodProxy.invokeSuper(proxy, args);
            return o;
        }
        //开启事务
        log.debug("------------------开始事务-----------------");
        transcationManager.setConn(method, id);
        try {
            //开启事务后，调用父类(目标对象)中指定方法
            Object object = methodProxy.invokeSuper(proxy, args);
            //提交事务
            transcationManager.commit(method, id);
            //返回父类(目标对象)返回对象
            log.debug("------------------事务结束-----------------");
            return object;
        } catch (Exception e) {
            //抛出异常，回滚事务
            log.error("------------------事务错误, 开始回滚-----------------");
            transcationManager.rollback(method, id);
            e.printStackTrace();
        }

        return null;
    }

}
