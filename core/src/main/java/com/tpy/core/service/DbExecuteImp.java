package com.tpy.core.service;


import com.tpy.utils.commons.JdbcResultHandler;
import com.tpy.utils.commons.ParamCommons;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sql执行
 */
public class DbExecuteImp<T> implements DbExecute<T> {

    Connection conn = null;


    public DbExecuteImp setConn(Connection conn) {
        this.conn = conn;
        return this;
    }

    public Long queryCount(String sql, List params){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Boolean b = false;
        if(conn == null) {
            conn = DataSourceUtils.getConnection();
            b = true;
        }
        try {
            //2. 创建语句对象
            ps = conn.prepareStatement(sql);
            ps = ParamCommons.paramsHandle(ps, params);

            resultSet = ps.executeQuery();
            Long count = 0L;
            if(resultSet.next()){
                count = resultSet.getLong(1);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            DataSourceUtils.close(b ? conn : null, ps, resultSet);
        }
        return null;
    }

    public List<T> query(String sql, List params){
        return this.query(sql, params, null);
    }


    public List<T> query(String sql, List params, Class clazz){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List list = new ArrayList();
        Boolean b = false;
        if(conn == null) {
            conn = DataSourceUtils.getConnection();
            b = true;
        }
        try {
            //2. 创建语句对象
            ps = conn.prepareStatement(sql);
            ps = ParamCommons.paramsHandle(ps, params);

            resultSet = ps.executeQuery();
            if(clazz == null){
                ResultSetMetaData md = resultSet.getMetaData();
                int columnCount = md.getColumnCount();
                while (resultSet.next()) {
                    Map rowData = new HashMap();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.put(md.getColumnName(i), resultSet.getObject(i));
                    }
                    list.add(rowData);
                }
                return list;
            }
            return JdbcResultHandler.putResult(resultSet, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            DataSourceUtils.close(b ? conn : null, ps, resultSet);
        }
        return null;
    }

    public <T> T queryOne(String sql, List params, Class clazz){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<T> list = new ArrayList<T>();
        Boolean b = false;
        if(conn == null) {
            conn = DataSourceUtils.getConnection();
            b = true;
        }
        try {
            //2. 创建语句对象
            ps = conn.prepareStatement(sql);
            ps = ParamCommons.paramsHandle(ps, params);

            resultSet = ps.executeQuery();
            list = JdbcResultHandler.putResult(resultSet, clazz);
            if(list.size() > 0) return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            DataSourceUtils.close(b ? conn : null,  ps, resultSet);
        }
        return null;
    }


    public Long insert(String sql, List params){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        int row = 0;
        Boolean b = false;
        if(conn == null) {
            conn = DataSourceUtils.getConnection();
            b = true;
        }
        try {
            //2. 创建语句对象
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps = ParamCommons.paramsHandle(ps, params);

            row = ps.executeUpdate();
            if(row > 0){
                resultSet = ps.getGeneratedKeys();
                if(resultSet.next()){
                    return resultSet.getLong(1);
                }
            }
            throw new RuntimeException("插入失败");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            DataSourceUtils.close(b ? conn : null, ps, resultSet);

        }
        return 0L;
    }

    /**
     * 批量插入
     * @param sql
     * @param params
     * @return
     */
    public int[] insertBatch(String sql, List<List> params){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        int[] i = {};
        Boolean b = false;
        if(conn == null) {
            conn = DataSourceUtils.getConnection();
            b = true;
        }
        try {
            //2. 创建语句对象
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for(List list : params){
                ps = ParamCommons.paramsHandle(ps, list);
                ps.addBatch();
                ps.clearParameters();
            }

            i = ps.executeBatch();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            DataSourceUtils.close(b ? conn : null, ps, null);
        }
        return i;
    }

    /**
     * 根据ID进行更新
     * @param sql
     * @param params
     * @return
     */
    public Boolean update(String sql, List params){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        int row = 0;
        Boolean b = false;
        if(conn == null) {
            conn = DataSourceUtils.getConnection();
            b = true;
        }
        try {
            //2. 创建语句对象
            ps = conn.prepareStatement(sql);
            ps = ParamCommons.paramsHandle(ps, params);

            row = ps.executeUpdate();
            return row > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            DataSourceUtils.close(b ? conn : null, ps, null);
        }
        return null;
    }


}
