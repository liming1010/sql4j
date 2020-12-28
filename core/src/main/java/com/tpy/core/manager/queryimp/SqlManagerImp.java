package com.tpy.core.manager.queryimp;

import com.tpy.core.manager.*;
import com.tpy.core.manager.InsertImp.InsertFun2ManagerImp;
import com.tpy.core.manager.updateimp.UpdateFun1ManagerImp;
import com.tpy.core.manager.updateimp.UpdateFun4ManagerImp;
import com.tpy.utils.commons.ClazzUtils;
import com.tpy.utils.commons.JdbcResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlManagerImp<T>  implements SqlManager<T> {

    Logger log = LoggerFactory.getLogger(SqlManagerImp.class);

    ClazzUtils clazzUtils = new ClazzUtils();

    // 字段条件
    List<String> cloList = new ArrayList<>();

    @Override
    public QueryFun1Manager query(T t) {
        Map map = clazzUtils.clazzToModel(t);
        String sql = "select %s from " + map.get("tablename").toString().replaceAll(" ", "") + " where 1 = 1 ";
        // and条件
        Map<String, Map<Object, String>> m = new HashMap();
        for (Object key : map.keySet()) {
            if (key.equals("tablename")) continue;
            if( key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            // 查询字段
            cloList.add(colName);

            if (map.get(key) != null) {
                Map<Object, String> mx = new HashMap<>();
                String par = "and " + colName.replaceAll(" ", "") + " = ? ";
                mx.put(map.get(key), par);
                m.put(colName, mx);
            }
        }


        QueryFun1Manager fm = new QueryFun1ManagerImp(sql, m, cloList, t);
        return fm;
    }


    @Override
    public Fun4Manager query() {
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType pt = null;
        try{
            pt = (ParameterizedType )type;
        }catch (Exception e){
        }
        if(pt == null) throw new RuntimeException("得这格式:public class UserDao extends SqlManager<User>");
        Type [] actualTypes = pt.getActualTypeArguments();
        if(actualTypes.length == 0) throw new RuntimeException("得这格式:public class UserDao extends SqlManager<User>");
        Fun4Manager f4 = null;
        try {
            Object o = Class.forName(actualTypes[0].getTypeName()).getDeclaredConstructor().newInstance();
            f4 = new Fun4ManagerImp(o);
            // 给目标对象，创建代理对象
        } catch (Exception e) {
            e.printStackTrace();
        }


        return f4;
    }

    @Override
    public QueryFun1Manager queryCount(T t) {
        return null;
    }

    @Override
    public UpdateFun1Manager update(T t) {
        String sql = "update %s set %s ";
        String s = "";
        Map map = clazzUtils.clazzToModel(t);
        for (Object key : map.keySet()) {
            if (key.equals("tablename")) continue;
            if(key.equals("id") || key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            if (map.get(key) != null) {
                s += colName.replaceAll(" ", "") + " = " + map.get(key) + ", ";
            }
        }
        if(s.equals("")) throw new RuntimeException("更新字段不能为空");
        sql = String.format(sql, map.get("tablename"), s.substring(0, s.length() - 2));
        UpdateFun1Manager uf1 = new UpdateFun1ManagerImp(sql);
        return uf1;
    }

    @Override
    public UpdateFun4Manager updateById(T t) {
        String sql = "update %s set %s ";
        String s = "";
        Map map = clazzUtils.clazzToModel(t);
        for (Object key : map.keySet()) {
            if (key.equals("tablename")) continue;
            if(key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            if (map.get(key) != null) {
                s += colName.replaceAll(" ", "") + " = " + map.get(key) + ", ";
            }
        }
        if(s.equals("")) throw new RuntimeException("更新字段不能为空");
        sql = String.format(sql, map.get("tablename"), s.substring(0, s.length() - 2));
        UpdateFun1Manager uf1 = new UpdateFun1ManagerImp(sql);
        String id = map.get("primary_key") != null ? map.get("primary_key").toString() : "id";
        Object value = map.get("primary_key_value") != null ? map.get("primary_key_value") : map.get(id);
        UpdateFun3Manager eq = uf1.where().eq(id , value);
        return eq;
    }

    @Override
    public UpdateFun1Manager delByLogic(T t) {
        String sql = "update %s set %s ";
        return dels(sql, t, 0);
    }

    @Override
    public UpdateFun4Manager delByLogicByPrimaryKkey(T t) {
        String sql = "update %s set %s = 1 where %s = %s";
        return delsid(sql, t, 0);
    }

    @Override
    public UpdateFun1Manager delByPhysics(T t) {
        String sql = "delete from %s ";
        return dels(sql, t, 1);
    }

    @Override
    public UpdateFun4Manager delPhysicsByPrimaryKkey(T t) {
        String sql = "delete from %s where %s = %s";
        return delsid(sql, t, 1);
    }


    private UpdateFun1Manager dels(String sql, T t, int flag){
        Map map = clazzUtils.clazzToModel(t);
        Map<String, Object> mx = new HashMap<>();
        for (Object key : map.keySet()) {
            if (key.equals("tablename")) continue;
            if(key.equals("id") || key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            if (map.get(key) != null) {
                mx.put(colName.replaceAll(" ", ""), map.get(key));
            }
        }

        if(map.get("primary_key") != null){
            mx.put(map.get("primary_key").toString(), map.get("primary_key_value"));
        }else if(map.get("id") != null){
            mx.put("id", map.get("id"));
        }
        String s = map.get("del_flag") + " = " + map.get("del_flag_value");
        if(flag == 0){
            sql = String.format(sql, map.get("tablename"), s);
        }else if(flag == 1){
            sql = String.format(sql, map.get("tablename"));
        }
        UpdateFun1Manager uf1 = new UpdateFun1ManagerImp(sql, mx);
        return uf1;
    }

    private UpdateFun4Manager delsid(String sql, T t, int flag){
        Map map = clazzUtils.clazzToModel(t);
        if(map.get("primary_key") == null && map.get("id") == null) throw new RuntimeException("请指定主键ID(默认为ID)");
        if(map.get("primary_key_value") == null) throw new RuntimeException("设置的主键的值为空");
        log.debug("{}, {}, {}, {}", map.get("tablename"), map.get("del_flag"),
                map.get("primary_key"), map.get("primary_key_value"));
        if(flag == 0){
            if(map.get("del_flag") == null) throw new RuntimeException("请指定@DelFlag");
            sql = String.format(sql, map.get("tablename"), map.get("del_flag"), map.get("primary_key"), map.get("primary_key_value"));
        }else if(flag == 1){
            sql = String.format(sql, map.get("tablename"), map.get("primary_key"), map.get("primary_key_value"));
        }

        UpdateFun4Manager ef4 = new UpdateFun4ManagerImp(sql);
        return ef4;
    }

    private InsertFun2Manager inserts(List<T> list){
        String  sql = "insert into %s (%s) values (%s) ";
        List<List> valuesBatch = new ArrayList();
        Map<String, Object> cloMapp = new HashMap<>();
        for(T t : list){
            String s = "", sv = "";
            Map map = clazzUtils.clazzToModel(t);
            List temp = new ArrayList();
            for (Object key : map.keySet()) {
                if (key.equals("tablename")) {
                    cloMapp.put("tablename", map.get(key));
                    continue;
                }
                if(key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;
                String colName = JdbcResultHandler.HumpToUnderline(key.toString());
                if (map.get(key) != null || cloMapp.get(colName) != null) {
                    cloMapp.put(colName, colName);
                    temp.add(map.get(key));
                    /*s += colName + ", ";
                    sv += "? , ";
                    values.add(map.get(key));*/
                }
            }
            valuesBatch.add(temp);
            /*s = s.substring(0, s.length() - 2);
            sv = sv.substring(0, sv.length() - 2);
            sql = String.format(sql, map.get("tablename"), s, sv);*/
        }

        String s = "", sv = "";
        for(String key : cloMapp.keySet()){
            if(key.equals("tablename"))continue;
            s += key + ", ";
            sv += "? , ";
        }
        s = s.substring(0, s.length() - 2);
        sv = sv.substring(0, sv.length() - 2);
        sql = String.format(sql, cloMapp.get("tablename"), s, sv);

        InsertFun2Manager if2 = new InsertFun2ManagerImp(sql, valuesBatch);
        log.debug(sql);

        return if2;
    }


    @Override
    public InsertFun2Manager insert(T t) {
        /*String  sql = "insert into %s (%s) values (%s) ";
        Map map = clazzUtils.clazzToModel(t);
        String s = "";
        String sv = "";
        List<Object> list = new ArrayList();
        for (Object key : map.keySet()) {
            if (key.equals("tablename")) continue;
            if(key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value")) continue;
            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
            if (map.get(key) != null) {
                s += colName + ", ";
                sv += "? , ";
                list.add(map.get(key));
            }
        }
        s = s.substring(0, s.length() - 2);
        sv = sv.substring(0, sv.length() - 2);
        sql = String.format(sql, map.get("tablename"), s, sv);
        log.debug(sql);
        InsertFun2Manager if2 = new InsertFun2ManagerImp(sql, list);*/
        List list = new ArrayList();
        list.add(t);
        InsertFun2Manager inserts = inserts(list);
        return inserts;
    }

    @Override
    public InsertFun2Manager insertBatch(List<T> list) {
        InsertFun2Manager inserts = inserts(list);
        return inserts;
    }
}
