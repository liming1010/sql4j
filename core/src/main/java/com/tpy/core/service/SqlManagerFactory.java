package com.tpy.core.service;

import com.tpy.core.manager.SqlManager;
import com.tpy.core.proxy.AuthProxy;
import com.tpy.core.proxy.AuthProxyFilter;
import com.tpy.core.service.tran.TranscationManager;
import com.tpy.pojo.manager.Autowired;
import com.tpy.pojo.manager.Sql4jDb;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class SqlManagerFactory<T>{

    static AuthProxy authProxy = new AuthProxy();

    public static<T> T getInstance(Class clazz){
        Enhancer en = new Enhancer();
        //进行代理
        en.setSuperclass(clazz);
        //设置类加载器为目标类的类加载器
        en.setClassLoader(clazz.getClassLoader());
        en.setCallback(authProxy);
        en.setCallbackFilter(new AuthProxyFilter());
        //生成代理实例
        T t = (T)en.create();
        return t;
    }

}
