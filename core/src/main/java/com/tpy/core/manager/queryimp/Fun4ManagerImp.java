package com.tpy.core.manager.queryimp;

import com.tpy.core.manager.QueryFun1Manager;
import com.tpy.core.manager.Fun4Manager;
import com.tpy.core.proxy.ProxyFactory;
import com.tpy.pojo.manager.JoinCondition;
import com.tpy.utils.commons.ClazzUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 多表查询
 * @param <T>
 */
public class Fun4ManagerImp<T> implements Fun4Manager<T> {

    ClazzUtils clazzUtils = new ClazzUtils();
    // 字段条件
    List<String> cloList = new ArrayList<>();
    List list = new ArrayList();
    String sql = "";
    Integer flag = 0;

    public Fun4ManagerImp(T t){
        list.add(t);
    }

    @Override
    public Fun4Manager on(JoinCondition jc1, JoinCondition jc2) {

        /*Class<?> aClass = clo1.getClass();

        String [] arr = new String[2];
        for(int i = 0; i < list.size(); i++){
            Map map = clazzUtils.clazzToModel(list.get(i));
            arr[i] = map.get("tablename").toString();
            for (Object key : map.keySet()) {
                if (key.equals("tablename")) continue;
                String colName = JdbcResultHandler.HumpToUnderline(key.toString());
                // 查询字段
                cloList.add(colName);
            }
        }
        list.clear();
        if(StringUtils.isNullOrEmpty(sql)){
            sql = "select * from %s a %s %s b on a.%s = b.%s ";
            sql = String.format(sql, arr[0], "left join", arr[1], clo1, clo2);
        }else{
            sql += "%s %s c on b.%s = c.%s ";
            sql = String.format(sql, "left join", arr[1], clo1, clo2);
        }


        System.out.println(sql);*/
        return this;
    }


    @Override
    public <T> Fun6Manager leftJoin(T t) {
        list.add(t);
        flag = 1;
        return (Fun6Manager)new ProxyFactory(this).getProxyInstance();
    }

    @Override
    public Fun6Manager rightJoin() {
        return null;
    }

    @Override
    public Fun6Manager innerJoin() {
        return null;
    }

    @Override
    public Fun4Manager union() {
        return null;
    }

    @Override
    public Fun4Manager childQuery() {
        return null;
    }

    @Override
    public QueryFun1Manager where() {
        return null;
    }
}
