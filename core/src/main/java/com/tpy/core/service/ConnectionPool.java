package com.tpy.core.service;

import com.tpy.pojo.manager.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jdbc连接池
 */
public class ConnectionPool {

    static Logger log = LoggerFactory.getLogger(ConnectionPool.class);

    private static Map<Long, Connection> connmap = new ConcurrentHashMap<>();


    /**
     * 设置连接
     * @param method
     * @param threadId
     * @throws SQLException
     */
    public static void startConn(Method method, Long threadId) throws SQLException {
        if(getConn(threadId) != null) return;
        //获取注解
        Transaction transcation = method.getAnnotation(Transaction.class);
        if(transcation == null){
            if(connmap.get(threadId) == null){
                log.debug("-------------不需要事务------------threadId: {}", threadId);
                connmap.put(threadId, DataSourceUtils.getConnection());
            }
        }else{
            if(connmap.get(threadId) == null){
                log.debug("----------------开启事务-------------------");
                Connection connection = DataSourceUtils.getConnection();
                connection.setAutoCommit(false);
                connmap.put(threadId, connection);
            }
        }
    }

    /**
     * 获取连接
     * @param threadId
     * @return
     */
    public static Connection getConn(Long threadId){
        return connmap.get(threadId);
    }

    public static void commit(Long threadId) throws SQLException {
        Connection conn = getConn(threadId);
        conn.commit();
        close(threadId);
    }

    public static void rollback(Long threadId) throws SQLException {
        Connection conn = getConn(threadId);
        conn.rollback();
        close(threadId);
    }

    public static void close(Long threadId) throws SQLException{
        Connection conn = getConn(threadId);
        connmap.remove(threadId);
        conn.close();
    }


    public static Map<Long, Connection> getConnmap() {
        return connmap;
    }
}
