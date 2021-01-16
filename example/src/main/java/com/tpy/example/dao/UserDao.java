package com.tpy.example.dao;

import com.tpy.core.manager.queryimp.SqlManagerImp;
import com.tpy.example.model.User;
import com.tpy.pojo.manager.Sql4jDb;
import com.tpy.pojo.manager.Transaction;


public class UserDao extends SqlManagerImp<User> {

    @Transaction
    public void tran(){
        User u = new User();
        u.setId(1L).setName("aa").setPwd("ss").setAge("ss").setDel_flag(2);
        query(u).executeQuery();
        query(u);
    }

    public void text(){

    }

}
