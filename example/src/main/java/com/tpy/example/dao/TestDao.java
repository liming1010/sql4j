package com.tpy.example.dao;

import com.tpy.core.manager.queryimp.SqlManagerImp;
import com.tpy.example.model.TestModel;
import com.tpy.pojo.manager.Transaction;

public class TestDao  extends SqlManagerImp<TestModel>{

    @Transaction
    public void tran(){
        delByPhysics(new TestModel()).executeUpdate();
        TestModel t = new TestModel();
        t.setAge("23");
        insert(t).executeInsert();
        t.setAge("232");
        insert(t).executeInsert();
        t.setAge("232");
        insert(t).executeInsert();
        t.setAge("232");
        insert(t).executeInsert();
        t.setAge("232");
        insert(t).executeInsert();

        int i = 1 / 0;

    }



}
