package com.tpy.example.example;

import com.tpy.core.service.tran.ProxyHandler;
import com.tpy.example.dao.UserDao;
import com.tpy.example.model.*;
import com.tpy.pojo.manager.Autowired;
import com.tpy.pojo.manager.Transaction;



public class QueryExample {


    static ProxyHandler handler=new ProxyHandler();

    UserDao dao = ProxyHandler.getCglibProxy(UserDao.class);

    @Transaction
    public void query(Integer i){

                dao.tran();




        // User u = new User();
        // u.setId(1L).setName("aa").setPwd("ss").setAge("ss").setDel_flag(2);

        // dao.query(u).eq("z", "ss").eq("a", "ssss").limit(1, 2);//.executeQuery();
         // dao.query(u).orderBy("xxx"); //.executeQuery();
        // dao.updateById(u); //.executeUpdate();
        // dao.update(u).executeUpdate();

        /*dao.update(u).where().eq("aa", "sdfs").eq("sss", "sd").like("sdf", "4跟4")
                .executeUpdate();*/

        // dao.updateById(u).executeUpdate();

       // dao.delByLogic(u).where().executeUpdate();

        // dao.delByLogicByPrimaryKkey(u).executeUpdate();

        // dao.delByPhysics(u); //.executeUpdate();

        // dao.delPhysicsByPrimaryKkey(u).executeUpdate();

    }

    @Transaction
    public static void insert(){

    }

}
