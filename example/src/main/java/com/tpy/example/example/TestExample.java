package com.tpy.example.example;

import com.tpy.core.service.tran.ProxyHandler;
import com.tpy.example.dao.TestDao;

public class TestExample {

    static TestDao dao = ProxyHandler.getCglibProxy(TestDao.class);

    public static void query(){
        /*TestModel tm = new TestModel();
        QueryModel queryModel = dao.query(tm).executeQuery();
        System.out.println(queryModel.getCount());*/

        dao.tran();
    }
}
