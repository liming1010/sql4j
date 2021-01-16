package com.tpy.core.service;

import com.tpy.core.proxy.AuthProxy;
import com.tpy.core.proxy.AuthProxyFilter;
import com.tpy.core.proxy.ProxyFactory;
import com.tpy.core.service.tran.ProxyHandler;
import net.sf.cglib.proxy.Enhancer;

import java.sql.Connection;

public class DbFactory<T> {

    public static DbExecuteImp getInstance(){
        AuthProxy authProxy = new AuthProxy();
        Enhancer en = new Enhancer();
        //进行代理
        en.setSuperclass(DbExecuteImp.class);
        en.setCallback(authProxy);
        en.setCallbackFilter(new AuthProxyFilter());
        //生成代理实例
        DbExecuteImp db = (DbExecuteImp)en.create();
        // db.setConn(DataSourceUtils.getConnection());
        // db.setConn(ConnectionUtil.getConnection(null));
        db.setConn(ConnectionPool.getConn(Thread.currentThread().getId()));
        return db;
    }

    public static DbExecute getProxyInstance(){
        DbExecute db = (DbExecute) new ProxyFactory(new DbExecuteImp<>()).getProxyInstance();
        // db.setConn(DataSourceUtils.getConnection());
        return db;
    }
    /*static ProxyHandler handler=new ProxyHandler();
    public static DbExecute getProxyInstance(){
        try {
            DbExecuteImp dbExecuteImp = (DbExecuteImp)handler.getCglibProxy(DbExecuteImp.class);
            return dbExecuteImp;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * 创建代理对象方法
     *
     * @param target        代理对象
     * @param args          对应的构造器参数类型
     *
     *                          例：有构造器如下
     *                          public Person(name,age){...} name为String.class age为int.class
     *                          写入name的类型与age的类型
     *
     *                          则：new Class[]{String.class,int.class}
     *
     * @param argsValue     对应的构造器参数值
     *
     *                          例:如此创建对象 new Person("name",23) 用以下方式传入：new Object[]{"name",23}
     *
     * @return              返回跟代理对象类型
     */
    public T getInstance(T target,Class[] args,Object[] argsValue){
        System.out.println("---被代理了--");
        AuthProxy authProxy = new AuthProxy();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(authProxy);
        return (T) enhancer.create(args,argsValue);
    }


}
