package com.tpy.example.example;

import com.tpy.example.dao.UserDao;
import com.tpy.example.model.User;

public class QueryBexample {

    public static void query(){
        User u = new User();
        u.setId(1L).setName("aa").setPwd("ss").setAge("ss").setDel_flag(2);
        UserDao dao = new UserDao();
        // dao.query(u).eq("z", "ss").eq("a", "ssss").limit(1, 2);//.executeQuery();
        // dao.query(u).executeQuery();

        long name = Thread.currentThread().getId();
        System.out.println(name);
        dao.updateById(u).executeUpdate();
    }

}
