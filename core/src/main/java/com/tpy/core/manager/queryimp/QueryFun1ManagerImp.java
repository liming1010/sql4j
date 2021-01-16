package com.tpy.core.manager.queryimp;


import com.mysql.cj.util.StringUtils;
import com.tpy.core.manager.*;
import com.tpy.core.service.DbExecute;
import com.tpy.core.service.DbExecuteImp;
import com.tpy.core.service.DbFactory;
import com.tpy.pojo.model.QueryModel;
import com.tpy.utils.commons.ParamCommons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QueryFun1ManagerImp<T> implements QueryFun1Manager<T> {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private String sql;
    private String sqlCount;
    // funManager条件
    List li = new ArrayList();
    // and条件
    Map<String, Map<Object, String>> m;
    // 字段条件
    List<String> cloList;

    T tx;

    public QueryFun1ManagerImp(String sql, Map<String, Map<Object, String>> m, List<String> cloList, T tx){
        this.sql = sql;
        this.m = m;
        this.tx = tx;
        this.cloList = cloList;
    }

    @Override
    public QueryFun1Manager eq(String clo, Object value) {
        String par = "and " + clo + " = ? ";
        Map<Object, String> mx = new HashMap<>();
        mx.put(value, par);
        m.put(clo, mx);
        return this;
    }

    @Override
    public QueryFun1Manager gtep(String clos, Object value) {
        String par = "and " + clos + " >= ? ";
        Map<Object, String> mx = new HashMap<>();
        mx.put(value, par);
        m.put(clos, mx);
        return this;
    }

    @Override
    public QueryFun1Manager lteq(String clos, Object value) {
        String par = "and " + clos + " <= ? ";
        Map<Object, String> mx = new HashMap<>();
        mx.put(value, par);
        m.put(clos, mx);
        return this;
    }

    @Override
    public QueryFun1Manager betweenAnd(String clo, Object a, Object b) {
        /*if ((a instanceof Integer && b instanceof Integer)
                || (a instanceof Long && b instanceof Long)
                || (a instanceof Date && b instanceof Date)) {
            Map<Object, String> mx = new HashMap<>();
            if (a instanceof Date && b instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String between = sdf.format((Date) a);
                String and = sdf.format((Date) b);
                mx.put(between, "and "+ clo.replaceAll(" ","") +" between ? ");
                mx.put(and, "and ? ");
            } else {
                mx.put(a, "and " + clo.replaceAll(" ", "") + " between ? ");
                mx.put(b, "and ? ");
            }
            m.put(clo, mx);
        } else {
            throw new RuntimeException("比较应为Integer, Long, Date类型");
        }*/
        Map<Object, String> mx = new HashMap<>();
        mx.put(a, "and "+ clo.replaceAll(" ","") +" between ? ");
        mx.put(b, "and ? ");
        return this;
    }

    @Override
    public QueryFun1Manager like(String clo, Object value) {
        String par = "and " + clo.replaceAll(" ", "") + " like ? ";
        Map<Object, String> mx = new HashMap<>();
        mx.put("%" + value + "%", par);
        m.put(clo, mx);
        return this;
    }


    @Override
    public QueryFun1Manager orderBy(String clo) {
        String par = "order by " + clo + " ";
        li.add(par);
        return this;
    }

    @Override
    public QueryFun1Manager groupBy(String... clo) {
        String par = "group by ";
        for(Object s : clo){
            par += s + ",";
        }
        par = par.substring(0, par.length() - 1) + " ";
        li.add(ParamCommons.arrIndex(li, "order by"), par);
        return this;
    }

    @Override
    public QueryFun1Manager desc() {
        li.add(ParamCommons.arrIndex(li, "order by") + 1, "desc ");
        ParamCommons.isContain(li);
        return this;
    }

    @Override
    public QueryFun1Manager asc() {
        li.add(ParamCommons.arrIndex(li, "order by") + 1, "asc ");
        ParamCommons.isContain(li);
        return this;
    }

    @Override
    public QueryFun1Manager distinct(String... clo) {
        if(clo.length == 0) throw new RuntimeException("distinct必须要有列名");
        List<String> temp = new ArrayList<>();
        for (String s : clo) {
            temp.add(s);
        }
        log.debug("注意: distinct选择的列就是查询的列");
        String s = temp.get(0);
        temp.remove(0);
        temp.add(0, "distinct " + s);
        cloList = temp;
        return this;
    }

    @Override
    public QueryFun1Manager regexp(String clo, String regexp) {
        String par = "REGEXP " + clo.replaceAll(" ", "") + " = ? ";
        Map<Object, String> mx = new HashMap<>();
        mx.put(regexp, par);
        m.put(clo, mx);
        return this;
    }


    @Override
    public QueryFun1Manager ifQuery(IfExpression expression) {
        cloList.add(expression.getExpression());
        return null;
    }

    @Override
    public QueryFun1Manager concat(String... clo) {
        return this;
    }

    @Override
    public QueryFun5Manager limit(Integer offset, Integer limit) {
        String par = "limit " + offset + ", " + limit + "";
        li.add(li.size(), par);
        return this;
    }


    @Override
    public QueryModel executeQuery() {
        List list = beforeQuery();
        DbExecute<T> db = DbFactory.getInstance();
        List<T> query = db.query(sql, list, tx.getClass());
        Long count = db.queryCount(sqlCount, list);
        QueryModel<T> qm = new QueryModel<>();
        qm.setCount(count).setList(query);
        return qm;
    }

    @Override
    public T executeQueryOne() {
        List list = beforeQuery();
        DbExecuteImp<T> db = DbFactory.getInstance();
        return db.queryOne(sql, list, tx.getClass());
    }

    private List beforeQuery(){
        List list = new ArrayList();
        // 查询字段
        String clos = "";
        for (String clo : cloList) {
            clos += clo + ",";
        }
        if(StringUtils.isNullOrEmpty(clos)){
            throw new RuntimeException(tx.getClass().getSimpleName() + " 实体类没有属性");
        }
        clos = clos.substring(0, clos.length() - 1);
        String oldSql = sql;
        sql = String.format(oldSql, clos);
        sqlCount = String.format(oldSql, " count(*) ");

        // and条件
        for (String clo : m.keySet()) {
            Map<Object, String> par = m.get(clo);
            for (Object key : par.keySet()) {
                list.add(key);
                sql += par.get(key);
                sqlCount += par.get(key);
            }
        }

        // funManager条件
        for (Object o : li) {
            sql += o;
            sqlCount += o;
        }
        return list;
    }

}
