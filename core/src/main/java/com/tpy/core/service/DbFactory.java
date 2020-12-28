package com.tpy.core.service;

import com.tpy.core.proxy.AuthProxy;
import com.tpy.core.proxy.AuthProxyFilter;
import net.sf.cglib.proxy.Enhancer;

public class DbFactory<T> {

    public static DbExecute getInstance(){
        AuthProxy authProxy = new AuthProxy();
        Enhancer en = new Enhancer();
        //进行代理
        en.setSuperclass(DbExecute.class);
        en.setCallback(authProxy);
        en.setCallbackFilter(new AuthProxyFilter());
        //生成代理实例
        DbExecute db = (DbExecute)en.create();
        db.setConn(DataSourceUtils.getConnection());
        return db;
    }


}
