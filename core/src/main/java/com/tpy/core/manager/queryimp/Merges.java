package com.tpy.core.manager.queryimp;

import com.tpy.core.manager.InsertFun2Manager;
import com.tpy.core.manager.InsertImp.InsertFun2ManagerImp;
import com.tpy.core.manager.QueryFun1Manager;
import com.tpy.core.manager.UpdateFun1Manager;
import com.tpy.core.manager.UpdateFun4Manager;
import com.tpy.core.manager.updateimp.UpdateFun1ManagerImp;
import com.tpy.core.manager.updateimp.UpdateFun4ManagerImp;
import com.tpy.core.proxy.ProxyFactory;
import com.tpy.utils.commons.ClazzUtils;
import com.tpy.utils.commons.JdbcResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Merges<T> {

    static Logger log = LoggerFactory.getLogger(Merges.class);

    static ClazzUtils clazzUtils = new ClazzUtils();

    // 字段条件
    static List<String> cloList = new ArrayList<>();

    static <T> QueryFun1Manager querys(T t){
        Map map = clazzUtils.clazzToModel(t);
        String sql = "select %s from " + map.get("tablename").toString().replaceAll(" ", "") + " where 1 = 1 ";
        // and条件
        Map<String, Map<Object, String>> m = new HashMap();
        for (Object key : map.keySet()) {
            if (key.equals("tablename")) continue;
            if (key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value"))
                continue;
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            // 查询字段
            cloList.add(colName);
            if(OtherType.isNullOrEmpty(map.get(key))){
                Map<Object, String> mx = new HashMap<>();
                String par = "and " + colName.replaceAll(" ", "") + " = ? ";
                mx.put(map.get(key), par);
                m.put(colName, mx);
            }
        }
        QueryFun1Manager fm = new QueryFun1ManagerImp(sql, m, cloList, t);
        return fm;
    }

     static <T> UpdateFun1Manager dels(String sql, T t, int flag) {
        Map map = clazzUtils.clazzToModel(t);
        Map<String, Object> mx = new HashMap<>();
        for (Object key : map.keySet()) {
            if (OtherType.isContinue(key, 0, map)) {
                continue;
            }
            /*if (key.equals("tablename")) continue;
            if(key.equals("id") || key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;*/
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            if (OtherType.isNullOrEmpty(map.get(key))) {
                mx.put(colName.replaceAll(" ", ""), map.get(key));
            }
        }

        if (map.get("primary_key") != null) {
            mx.put(map.get("primary_key").toString(), map.get("primary_key_value"));
        } else if (map.get("id") != null) {
            mx.put("id", map.get("id"));
        }
        String s = map.get("del_flag") + " = " + map.get("del_flag_value");
        if (flag == 0) {
            sql = String.format(sql, map.get("tablename"), s);
        } else if (flag == 1) {
            sql = String.format(sql, map.get("tablename"));
        }
        UpdateFun1Manager uf1 = new UpdateFun1ManagerImp(sql, mx);
         // UpdateFun1Manager proxy = (UpdateFun1Manager) new ProxyFactory(uf1).getProxyInstance();
        return uf1;
    }

     static <T> UpdateFun4Manager delsid(String sql, T t, int flag) {
        Map map = clazzUtils.clazzToModel(t);
        /*if(map.get("primary_key") == null && map.get("id") == null) throw new RuntimeException("请指定主键ID(默认为ID)");
        if(map.get("primary_key_value") == null) throw new RuntimeException("设置的主键的值为空");*/
        Map<String, Object> primarykeyvalue = OtherType.primarykeyvalue(map);
        for(String key : primarykeyvalue.keySet()){
            log.debug("{}, {}, {}, {}", map.get("tablename"), map.get("del_flag"),  key, primarykeyvalue.get(key));
            if (flag == 0) {
                if (map.get("del_flag") == null) throw new RuntimeException("请指定@DelFlag");
                sql = String.format(sql, map.get("tablename"), map.get("del_flag"), key, primarykeyvalue.get(key));
            } else if (flag == 1) {
                sql = String.format(sql, map.get("tablename"), key, primarykeyvalue.get(key));
            }
            UpdateFun4Manager ef4 = new UpdateFun4ManagerImp(sql);
            // UpdateFun4Manager proxy = (UpdateFun4Manager) new ProxyFactory(ef4).getProxyInstance();
            return ef4;
        }
        throw new RuntimeException("没有发现主键");
    }

     static <T> InsertFun2Manager inserts(List<T> list) {
        String sql = "insert into %s (%s) values (%s) ";
        List<List> valuesBatch = new ArrayList();
        Map<String, Object> cloMapp = new LinkedHashMap<>();
        for (T t : list) {
            Map map = clazzUtils.clazzToModel(t);
            List temp = new ArrayList();
            for (Object key : map.keySet()) {
                if (OtherType.isContinue(key, 0, map)) {
                    cloMapp.put("tablename", map.get(key));
                    continue;
                }
                /*if (key.equals("tablename"))continue;
                if(key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;*/
                String colName = JdbcResultHandler.HumpToUnderline(key.toString());
                if (OtherType.isNullOrEmpty(map.get(key)) || OtherType.isNullOrEmpty(cloMapp.get(colName))) {
                    cloMapp.put(colName, colName);
                    temp.add(map.get(key));
                }
            }
            valuesBatch.add(temp);
        }

        String s = "", sv = "";
        for (String key : cloMapp.keySet()) {
            if (key.equals("tablename")) continue;
            s += key + ", ";
            sv += "? , ";
        }
        s = s.substring(0, s.length() - 2);
        sv = sv.substring(0, sv.length() - 2);
        sql = String.format(sql, cloMapp.get("tablename"), s, sv);

        InsertFun2Manager if2 = new InsertFun2ManagerImp(sql, valuesBatch);
         // InsertFun2Manager proxy = (InsertFun2Manager) new ProxyFactory(if2).getProxyInstance();
        return if2;
    }

    static <T> UpdateFun1Manager updetas(T t, int flag) {
        String sql = "update %s set %s ";
        String s = "";
        Map map = clazzUtils.clazzToModel(t);
        for (Object key : map.keySet()) {
            if (OtherType.isContinue(key, flag, map)) {
                continue;
            }
            /*if (key.equals("tablename")) continue;
            if(key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value") ) continue;
            if(flag == 1 && key.equals(map.get("primary_key"))) continue;*/
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            if (OtherType.isNullOrEmpty(map.get(key))) {
                s += colName.replaceAll(" ", "") + " = " + map.get(key) + ", ";
            }
        }
        if (s.equals("")) throw new RuntimeException("更新字段不能为空");
        sql = String.format(sql, map.get("tablename"), s.substring(0, s.length() - 2));

        UpdateFun1Manager uf1 = null;
        if (flag == 0) {
            uf1 = new UpdateFun1ManagerImp(sql);
        } else {
            uf1 = new UpdateFun1ManagerImp(sql, OtherType.primarykeyvalue(map));
        }

        // UpdateFun1Manager proxy = (UpdateFun1Manager) new ProxyFactory(uf1).getProxyInstance();
        return uf1;
    }



}
