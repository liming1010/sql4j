package com.tpy.core.manager.updateimp;

import com.tpy.core.manager.UpdateFun4Manager;
import com.tpy.core.service.ConnectionPool;
import com.tpy.core.service.DbExecute;
import com.tpy.core.service.DbExecuteImp;
import com.tpy.core.service.DbFactory;

import java.util.ArrayList;
import java.util.List;

public class UpdateFun4ManagerImp<T> implements UpdateFun4Manager<T> {

    String sql = "";
    List list = null;

    public UpdateFun4ManagerImp(String sql) {
        this.sql = sql;
    }

    public UpdateFun4ManagerImp(String sql, List list) {
        this.sql = sql;
        this.list = list;
    }

    @Override
    public Boolean executeUpdate() {
        DbExecuteImp<T> db = DbFactory.getInstance();
        if (list != null) {
            Boolean update = db.update(sql, list);
            return update;
        }else{
            Boolean update = db.update(sql, new ArrayList());
            return update;
        }

    }
}
