package com.tpy.core.manager.InsertImp;

import com.tpy.core.manager.InsertFun2Manager;
import com.tpy.core.service.ConnectionPool;
import com.tpy.core.service.DbExecute;
import com.tpy.core.service.DbExecuteImp;
import com.tpy.core.service.DbFactory;

import java.util.List;

public class InsertFun2ManagerImp implements InsertFun2Manager {

    String sql;
    List<List> valuesBatch;

/*    public InsertFun2ManagerImp(String sql, List<Object> list){
        this.sql = sql;
        this.list = list;
    }*/

    public InsertFun2ManagerImp(String sql, List<List> valuesBatch){
        this.sql = sql;
        this.valuesBatch = valuesBatch;
    }

    @Override
    public Long executeInsert() {
        if(valuesBatch == null) throw new RuntimeException("插入参数为空");
        DbExecuteImp db = DbFactory.getInstance();
        Long id = db.insert(sql, valuesBatch.get(0));
        return id;
    }

    @Override
    public Integer executeBatchInsert() {
        if(valuesBatch == null) throw new RuntimeException("插入参数为空");
        DbExecuteImp db = DbFactory.getInstance();
        int[] ints = db.insertBatch(sql, valuesBatch);
        return ints.length;
    }
}
