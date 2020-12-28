//package com.tpy.core.manager;
//
//import com.tpy.db.commons.ClazzUtils;
//import com.tpy.db.commons.ParamCommons;
//import com.tpy.db.service.DbExecute;
//import com.tpy.db.service.DbFactory;
//import com.tpy.db.service.JdbcResultHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class SqlMangerx<T> implements ConditionManager<T>, QueryManager<T>, QueryMoreManager<T> {
//
//    Logger log = LoggerFactory.getLogger(this.getClass());
//
//    ClazzUtils clazzUtils = new ClazzUtils();
//    String sql = "";
//    // and条件
//    Map<String, Map<Object, String>> m = new HashMap();
//    // funManager条件
//    List li = new ArrayList();
//    // 字段条件
//    List<String> cloList = new ArrayList<>();
//
//    T tx;
//
//    @Override
//    public Fun1Manager query(T t) {
//        tx = t;
//        Map map = clazzUtils.clazzToModel(t);
//        sql = "select %s from " + map.get("tablename").toString().replaceAll(" ", "") + " where 1 = 1 ";
//        for (Object key : map.keySet()) {
//            if (key.equals("tablename")) continue;
//            String colName = JdbcResultHandler.HumpToUnderline(key.toString());
//            // 查询字段
//            cloList.add(colName);
//            if (map.get(key) != null) {
//                Map<Object, String> mx = new HashMap<>();
//                String par = "and " + colName.replaceAll(" ", "") + " = ? ";
//                mx.put(map.get(key), par);
//                m.put(colName, mx);
//            }
//        }
//        return this;
//    }
//
//
//    @Override
//    public Fun1Manager queryCount(T t) {
//        return null;
//    }
//
//
//    @Override
//    public List<T> executeQuery() {
//        List list = beforeQuery();
//        DbExecute <T> db = DbFactory.getInstance();
//        List query = db.query(sql, list, tx.getClass());
//        return query;
//    }
//
//    @Override
//    public T executeQueryOne() {
//        List list = beforeQuery();
//        DbExecute <T> db = DbFactory.getInstance();
//        return db.queryOne(sql, list, tx.getClass());
//    }
//
//    @Override
//    public T executeQueryById() {
//        return null;
//    }
//
//
//    @Override
//    public Integer update(T t) {
//        return null;
//    }
//
//    @Override
//    public Integer updateById(T t) {
//        return null;
//    }
//
//    @Override
//    public Integer insert(T t) {
//        return null;
//    }
//
//    @Override
//    public Integer insertBatch(List<T> list) {
//        return null;
//    }
//
//    @Override
//    public Integer del(T t) {
//        return null;
//    }
//
//    @Override
//    public Integer delById(Object id) {
//        return null;
//    }
//
//    @Override
//    public Fun1Manager betweenAnd(String clo, Object a, Object b) {
//        if ((a instanceof Integer && b instanceof Integer)
//                || (a instanceof Long && b instanceof Long)
//                || (a instanceof Date && b instanceof Date)) {
//            Map<Object, String> mx = new HashMap<>();
//            if (a instanceof Date && b instanceof Date) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String between = sdf.format((Date) a);
//                String and = sdf.format((Date) b);
//                mx.put(between, "and "+ clo.replaceAll(" ","") +" between ? ");
//                mx.put(and, "and ? ");
//            } else {
//                mx.put(a, "and " + clo.replaceAll(" ", "") + " between ? ");
//                mx.put(b, "and ? ");
//            }
//            m.put(clo, mx);
//        } else {
//            throw new RuntimeException("比较应为Integer, Long, Date类型");
//        }
//        return this;
//    }
//
//    @Override
//    public Fun1Manager like(String clo, String value) {
//        String par = "and " + clo.replaceAll(" ", "") + " like ? ";
//        Map<Object, String> mx = new HashMap<>();
//        mx.put("%" + value + "%", par);
//        m.put(clo, mx);
//        return this;
//    }
//
//
//    @Override
//    public Fun1Manager orderBy(String clo) {
//        String par = "order by " + clo + " ";
//        li.add(par);
//        return this;
//    }
//
//    @Override
//    public Fun1Manager groupBy(String... clo) {
//        String par = "group by ";
//        for(String s : clo){
//            par += s + ",";
//        }
//        par = par.substring(0, par.length() - 1) + " ";
//        li.add(ParamCommons.arrIndex(li, "order by"), par);
//        return this;
//    }
//
//    @Override
//    public Fun1Manager desc() {
//        li.add(ParamCommons.arrIndex(li, "order by") + 1, "desc ");
//        ParamCommons.isContain(li);
//        return this;
//    }
//
//    @Override
//    public Fun1Manager asc() {
//        li.add(ParamCommons.arrIndex(li, "order by") + 1, "asc ");
//        ParamCommons.isContain(li);
//        return this;
//    }
//
//    @Override
//    public Fun1Manager distinct(String... clo) {
//        if(clo.length == 0) throw new RuntimeException("distinct必须要有列名");
//        List<String> temp = new ArrayList<>();
//        for (String s : clo) {
//            temp.add(s);
//        }
//        log.debug("注意: distinct选择的列就是查询的列");
//        String s = temp.get(0);
//        temp.remove(0);
//        temp.add(0, "distinct " + s);
//        cloList = temp;
//        return this;
//    }
//
//    @Override
//    public Fun1Manager regexp(String clo, String regexp) {
//        String par = "REGEXP " + clo.replaceAll(" ", "") + " = ? ";
//        Map<Object, String> mx = new HashMap<>();
//        mx.put(regexp, par);
//        m.put(clo, mx);
//        return this;
//    }
//
//
//    @Override
//    public Fun1Manager ifQuery(IfExpression expression) {
//        cloList.add(expression.getExpression());
//        return null;
//    }
//
//    @Override
//    public Fun1Manager concat(String... clo) {
//        return this;
//    }
//
//    @Override
//    public Fun1Manager limit(Integer offset, Integer limit) {
//        String par = "limit " + offset + ", " + limit + "";
//        li.add(li.size(), par);
//        return this;
//    }
//
//
//    @Override
//    public QueryMoreManager as() {
//        return null;
//    }
//
//    @Override
//    public QueryMoreManager on() {
//        return null;
//    }
//
//    @Override
//    public QueryMoreManager leftJoin() {
//        return null;
//    }
//
//    @Override
//    public QueryMoreManager rightJoin() {
//        return null;
//    }
//
//    @Override
//    public QueryMoreManager innerJoin() {
//        return null;
//    }
//
//    @Override
//    public QueryMoreManager union() {
//        return null;
//    }
//
//    @Override
//    public QueryMoreManager childQuery() {
//        return null;
//    }
//
//    private List beforeQuery(){
//        List list = new ArrayList();
//        // 查询字段
//        String clos = "";
//        for (String clo : cloList) {
//            clos += clo + ",";
//        }
//        clos = clos.substring(0, clos.length() - 1);
//        sql = String.format(sql, clos);
//
//        // and条件
//        for (String clo : m.keySet()) {
//            Map<Object, String> par = m.get(clo);
//            for (Object key : par.keySet()) {
//                list.add(key);
//                sql += par.get(key);
//            }
//        }
//
//        // funManager条件
//        for (Object o : li) {
//            sql += o;
//        }
//        return list;
//    }
//}
