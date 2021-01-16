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
