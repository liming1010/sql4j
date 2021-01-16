package com.tpy.core.service;

import com.tpy.utils.commons.JdbcResultHandler;
import com.tpy.utils.commons.ParamCommons;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DbExecute<T> {
    public DbExecuteImp setConn(Connection conn);

    public Long queryCount(String sql, List params);

    public List<T> query(String sql, List params);


    public List<T> query(String sql, List params, Class clazz);

    public <T> T queryOne(String sql, List params, Class clazz);


    public Long insert(String sql, List params);

    /**
     * 批量插入
     * @param sql
     * @param params
     * @return
     */
    public int[] insertBatch(String sql, List<List> params);

    /**
     * 根据ID进行更新
     * @param sql
     * @param params
     * @return
     */
    public Boolean update(String sql, List params);



}
