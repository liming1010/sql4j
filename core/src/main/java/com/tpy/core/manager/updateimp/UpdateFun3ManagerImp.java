package com.tpy.core.manager.updateimp;

import com.mysql.cj.util.StringUtils;
import com.tpy.core.manager.UpdateFun3Manager;
import com.tpy.core.manager.UpdateFun4Manager;
import com.tpy.core.service.DbExecute;
import com.tpy.core.service.DbFactory;
import com.tpy.pojo.table.QueryModel;
import com.tpy.utils.commons.ParamCommons;

import java.text.SimpleDateFormat;
import java.util.*;

public class UpdateFun3ManagerImp<T> implements UpdateFun3Manager<T> {

    Map<String, Map<Object, String>> m = new HashMap();
    String sql = "";

    public UpdateFun3ManagerImp(String sql){
        this.sql = sql;
    }

    @Override
    public UpdateFun3Manager eq(String clos, T value) {
        Map<Object, String> mx = new HashMap<>();
        String par = "and " + clos.replaceAll(" ", "") + " = ? ";
        mx.put(value, par);
        m.put(clos, mx);
        return this;
    }


    @Override
    public UpdateFun3Manager betweenAnd(String clo, Object between, Object and) {
        if ((between instanceof Integer && and instanceof Integer)
                || (between instanceof Long && and instanceof Long)
                || (between instanceof Date && and instanceof Date)) {
            Map<Object, String> mx = new HashMap<>();
            if (between instanceof Date && and instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                between = sdf.format((Date) between);
                and = sdf.format((Date) and);
                mx.put(between, "and "+ clo.replaceAll(" ","") +" between ? ");
                mx.put(and, "and ? ");
            } else {
                mx.put(between, "and " + clo.replaceAll(" ", "") + " between ? ");
                mx.put(and, "and ? ");
            }
            m.put(clo, mx);
        } else {
            throw new RuntimeException("比较应为Integer, Long, Date类型");
        }
        return this;
    }

    @Override
    public UpdateFun3Manager like(String clo, String value) {
        String par = "and " + clo.replaceAll(" ", "") + " like ? ";
        Map<Object, String> mx = new HashMap<>();
        mx.put("%" + value + "%", par);
        m.put(clo, mx);
        return this;
    }

    @Override
    public UpdateFun3Manager regexp(String clo, String regexp) {
        String par = "REGEXP " + clo.replaceAll(" ", "") + " = ? ";
        Map<Object, String> mx = new HashMap<>();
        mx.put(regexp, par);
        m.put(clo, mx);
        return this;
    }

    @Override
    public Boolean executeUpdate() {
        List list = beforeQuery();
        UpdateFun4Manager uf4 = new UpdateFun4ManagerImp(sql, list);
        return uf4.executeUpdate();
    }

    private List beforeQuery(){
        List list = new ArrayList();
        for(String key : m.keySet()){
            Map<Object, String> om = m.get(key);
            for(Object k : om.keySet()){
                sql += om.get(k);
                list.add(k);
            }
        }
        System.out.println(sql);
        return list;
    }
}
