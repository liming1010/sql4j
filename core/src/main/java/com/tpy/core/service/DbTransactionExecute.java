package com.tpy.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DbTransactionExecute {

    static Logger log = LoggerFactory.getLogger(DbTransactionExecute.class);
    Connection connection;
    DbExecuteImp db;
    static Long startTime;

    DbTransactionExecute() {

    }

    public static DbTransactionExecute getTransaction() throws SQLException {
        log.debug("----------事务开始-----------");
        startTime = System.currentTimeMillis();
        Connection conn = DataSourceUtils.getConnection();
        conn.setAutoCommit(false);
        // 设置事务级别
        DbTransactionExecute dbTransactionExecute = new DbTransactionExecute();
        dbTransactionExecute.setConnection(conn);
        DbExecuteImp db = DbFactory.getInstance();
        db.setConn(dbTransactionExecute.getConnection());
        dbTransactionExecute.setDb(db);

        return dbTransactionExecute;
    }


    /**
     * 提交并关闭
     */
    public void commit() throws SQLException {
        connection.commit();
        connection.close();

        Long endTime = System.currentTimeMillis();
        Double t = (endTime - startTime) / 1000d;
        log.debug("-----------事务结束, 耗时: {}----------", t);
    }

    public void rollback() throws SQLException {
        connection.rollback();
        connection.close();
        Long endTime = System.currentTimeMillis();
        Double t = (endTime - startTime) / 1000d;
        log.error("-----------事务异常结束结束, 耗时: {}----------", t);
    }


    public Connection getConnection() {
        return connection;
    }

    public DbTransactionExecute setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public DbExecuteImp getDb() {
        return db;
    }

    public DbTransactionExecute setDb(DbExecuteImp db) {
        this.db = db;
        return this;
    }
}
