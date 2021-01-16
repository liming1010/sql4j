package com.tpy.core.proxy;

import com.tpy.core.manager.SqlManager;
import com.tpy.core.manager.queryimp.SqlManagerImp;
import com.tpy.core.service.DbExecute;
import com.tpy.core.service.DbExecuteImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    Logger log = LoggerFactory.getLogger(ProxyFactory.class);

    //维护一个目标对象
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    //给目标对象生成代理对象
    public Object getProxyInstance() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    String name = method.getName();
                    //执行目标对象方法
                    try{
                        Object returnValue = method.invoke(target, args);
                        return returnValue;
                    }catch (Exception e){

                    }
                    return null;
                }
        );
    }

    public static void main(String[] args) {
        // 目标对象
         SqlManager target = new SqlManagerImp();
        // 【原始的类型 class cn.itcast.b_dynamic.UserDao】

        // 给目标对象，创建代理对象
         SqlManager proxy = (SqlManager) new ProxyFactory(target).getProxyInstance();
        // class $Proxy0   内存中动态生成的代理对象

        // 执行方法   【代理对象】

        /*DbExecuteImp dbExecuteImp = new DbExecuteImp();
        Object proxyInstance = new ProxyFactory(dbExecuteImp).getProxyInstance();
        DbExecute dbExecute = (DbExecute)proxyInstance;
        System.out.println(dbExecute);*/
    }

}
