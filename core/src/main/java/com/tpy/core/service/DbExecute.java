package com.tpy.core.service;


import com.tpy.utils.commons.JdbcResultHandler;
import com.tpy.utils.commons.ParamCommons;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * sql执行
 */
public class DbExecute<T> {

    Connection conn = null;

    DbExecute(){
    }

    public DbExecute setConn(Connection conn) {
        this.conn = conn;
        return this;
    }

    public Long queryCount(String sql, List params){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            //2. 创建语句对象
            ps = connHandler().prepareStatement(sql);
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
            try {
                if(conn.getAutoCommit()){
                    // 注释的这条是对的，但是有问题
                    DataSourceUtils.close(conn, ps, resultSet);
                }else{
                    DataSourceUtils.close(ps, resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public List<T> query(String sql, List params, Class clazz){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<T> list = new ArrayList();
        try {
            //2. 创建语句对象
            ps = connHandler().prepareStatement(sql);
            ps = ParamCommons.paramsHandle(ps, params);

            resultSet = ps.executeQuery();
            list = JdbcResultHandler.putResult(resultSet, clazz);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            try {
                if(conn.getAutoCommit()){
                    DataSourceUtils.close(conn, ps, resultSet);
                }else{
                    DataSourceUtils.close(ps, resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T queryOne(String sql, List params, Class clazz){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<T> list = new ArrayList();
        try {
            //2. 创建语句对象
            ps = connHandler().prepareStatement(sql);
            ps = ParamCommons.paramsHandle(ps, params);

            resultSet = ps.executeQuery();
            list = JdbcResultHandler.putResult(resultSet, clazz);
            if(list.size() > 0) return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            try {
                if(conn.getAutoCommit()){
                    DataSourceUtils.close(conn, ps, resultSet);
                }else{
                    DataSourceUtils.close(ps, resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public Long insert(String sql, List params){
        //1. 创建连接对象，通过连接池工具类
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        int row = 0;
        try {
            //2. 创建语句对象
            ps = connHandler().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
            try {
                if(conn.getAutoCommit()){
                    DataSourceUtils.close(conn, ps, resultSet);
                }else{
                    DataSourceUtils.close(ps, resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

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
        ResultSet resultSet = null;
        int[] i = {};
        try {
            //2. 创建语句对象
            ps = connHandler().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
            try {
                if(conn.getAutoCommit()){
                    DataSourceUtils.close(conn, ps, resultSet);
                }else{
                    DataSourceUtils.close(ps, resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        ResultSet resultSet = null;
        int row = 0;
        try {
            //2. 创建语句对象
            ps = connHandler().prepareStatement(sql);
            ps = ParamCommons.paramsHandle(ps, params);

            row = ps.executeUpdate();
            return row > 1 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 释放资源
            try {
                if(conn.getAutoCommit()){
                    DataSourceUtils.close(conn, ps, resultSet);
                }else{
                    DataSourceUtils.close(ps, resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    Connection connHandler() throws Exception {
        if(conn.isClosed()){
            conn = DataSourceUtils.getConnection();
        }
        return conn;
    }






}
