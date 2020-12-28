package com.tpy.example.dao;

import com.tpy.core.manager.SqlManager;
import com.tpy.core.manager.queryimp.SqlManagerImp;
import com.tpy.example.model.User;
import com.tpy.pojo.manager.Sql4jDb;
import com.tpy.pojo.manager.sql;


@Sql4jDb
public interface UserDao extends SqlManager<User> {
    @sql(sql = "sssss")
    void index();
}
