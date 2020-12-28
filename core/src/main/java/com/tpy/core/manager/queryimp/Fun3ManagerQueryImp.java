package com.tpy.core.manager.queryimp;

import com.tpy.core.manager.Fun3ManagerQuery;
import com.tpy.core.manager.Fun4Manager;
import com.tpy.utils.commons.ClazzUtils;
import com.tpy.utils.commons.JdbcResultHandler;

import java.util.HashMap;
import java.util.Map;

public class Fun3ManagerQueryImp<T> implements Fun3ManagerQuery<T> {

    ClazzUtils clazzUtils = new ClazzUtils();
    String as;

    public <T> Fun3ManagerQueryImp(String as){
        this.as = as;
    }

    @Override
    public <T> Fun4Manager query(T... t) {
        if(t == null || t.length == 0){
            throw new RuntimeException("query参数不能为空");
        }
        for(T e : t ){
            Map map = clazzUtils.clazzToModel(t);
            String sql = "select %s from " + map.get("tablename").toString().replaceAll(" ", "") + " where 1 = 1 ";
            // and条件
            Map<String, Map<Object, String>> m = new HashMap();
            for (Object key : map.keySet()) {
                if (key.equals("tablename")) continue;
                String colName = JdbcResultHandler.HumpToUnderline(key.toString());
                // 查询字段
                // cloList.add(colName);

                if (map.get(key) != null) {
                    Map<Object, String> mx = new HashMap<>();
                    String par = "and " + colName.replaceAll(" ", "") + " = ? ";
                    mx.put(map.get(key), par);
                    m.put(colName, mx);
                }
            }
        }


        return null;
    }

    @Override
    public <T1> Fun4Manager query(String... clos) {
        return null;
    }
}
