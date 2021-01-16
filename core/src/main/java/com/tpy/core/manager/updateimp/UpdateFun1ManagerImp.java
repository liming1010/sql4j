package com.tpy.core.manager.updateimp;

import com.tpy.core.manager.UpdateFun1Manager;
import com.tpy.core.manager.UpdateFun3Manager;
import com.tpy.core.manager.UpdateFun4Manager;

import java.util.Map;

public class UpdateFun1ManagerImp<T>  implements UpdateFun1Manager<T> {
    String sql = "";
    Map<String, Object> map = null;

    public UpdateFun1ManagerImp(String sql){
        this.sql = sql;
    }
    public UpdateFun1ManagerImp(String sql, Map<String, Object> map){
        this.map = map;
        this.sql = sql;
    }

    @Override
    public UpdateFun3Manager where() {
        sql += " where 1 = 1 ";
        if(map != null){
            for(String key : map.keySet()){
                sql += "and " + key + " = " + map.get(key) + " ";
            }
        }
        return new UpdateFun3ManagerImp(sql);
    }


    @Override
    public Boolean executeUpdate() {
        if(map != null){
            UpdateFun3Manager uf3 = where();
            return uf3.executeUpdate();
        }else{
            UpdateFun4Manager uf4 = new UpdateFun4ManagerImp(sql);
            return uf4.executeUpdate();
        }
    }
}
