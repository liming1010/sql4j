package com.tpy.core.service.tran;

import com.tpy.pojo.manager.Transaction;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Proxy;

import java.lang.reflect.Method;

/**
 * 生成cglib代理类
 * @author Administrator
 *
 */
public class  ProxyHandler<T> {
    /** 增强对象(事务管理器) **/
    private static TranscationManager transcationManager=new TranscationManager();
    /** 方法拦截器 **/
    private static TranscationInterecptor interecptor=new TranscationInterecptor(transcationManager);
    /**
     * 获取cglib生成的动态代理对象
     */
    public Object getCglibProxy(String className) throws ClassNotFoundException{

        //获取class对象
        Class<?> cla = Class.forName(className);
        Enhancer enhancer=new Enhancer();
        //设置父类类型，即关注目标方法所属类的类型
        enhancer.setSuperclass(cla);
        //设置类加载器为目标类的类加载器
        enhancer.setClassLoader(cla.getClassLoader());
        //设置子类的回调对象
        enhancer.setCallback(interecptor);
        //生产代理对象，并利用类加载器加载
        return enhancer.create();
    }
    /**
     * 获取cglib生成的动态代理对象，重载方法
     */
    public static<T> T getCglibProxy(Class cla) {
        //获取class对象
        Enhancer enhancer=new Enhancer();
        //设置父类类型，即关注目标方法所属类的类型
        enhancer.setSuperclass(cla);
        //设置类加载器为目标类的类加载器
        enhancer.setClassLoader(cla.getClassLoader());
        //设置子类的回调对象
        enhancer.setCallback(interecptor);
        //生产代理对象，并利用类加载器加载
        return (T)enhancer.create();
    }
    /**
     * 获取Jdk生产的动态代理对象
     */
    /*public Object getJdkProxy(Class cla) throws Exception{
        //初始化事务增强处理器
        TransactionHandler transactionHandler=new TransactionHandler(cla.newInstance(), transcationManager);
        //获取动态代理对象实例
        Object object = Proxy.newProxyInstance(cla.getClassLoader(), cla.getInterfaces(), transactionHandler);
        return object;
    }*/

}

