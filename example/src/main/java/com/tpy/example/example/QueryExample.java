package com.tpy.example.example;

import com.tpy.example.dao.UserDao;
import com.tpy.example.model.A;
import com.tpy.example.model.B;
import com.tpy.example.model.CodeModel;
import com.tpy.example.model.User;
import com.tpy.pojo.manager.JoinCondition;
import com.tpy.pojo.table.QueryModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class QueryExample {

    public static void query() {
        User u = new User();
        u.setId(1L).setName("aa").setPwd("ss").setAge("ss").setDel_flag(2);
        UserDao dao = new UserDao();
        // dao.query(u).eq("z", "ss").eq("a", "ssss").limit(1, 2).executeQuery();
        // dao.query(u).executeQuery();


        /*dao.update(u).where().eq("aa", "sdfs").eq("sss", "sd").like("sdf", "4跟4")
                .executeUpdate();*/

        // dao.updateById(u).executeUpdate();


       // dao.delByLogic(u).where().executeUpdate();


        // dao.delByLogicByPrimaryKkey(u).executeUpdate();

        // dao.delByPhysics(u).executeUpdate();

        // dao.delPhysicsByPrimaryKkey(u).executeUpdate();

        User ux = new User();
        ux.setPwd("sss");
        List list = new ArrayList();
        list.add(u);
        list.add(ux);
        dao.insert(u).executeInsert();
        // dao.insertBatch(list).executeInsert();


    }

}
