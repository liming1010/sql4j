package com.tpy.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DbTransactionExecute {

    static Logger log = LoggerFactory.getLogger(DbTransactionExecute.class);
    Connection connection;
    DbExecute db;
    static Long startTime;

    DbTransactionExecute()  {

    }

    public static DbTransactionExecute getTransaction(){
        try {
            log.debug("----------事务开始-----------");
            startTime = System.currentTimeMillis();
            Connection conn = DataSourceUtils.getConnection();
            conn.setAutoCommit(false);
            // 设置事务级别
            DbTransactionExecute dbTransactionExecute = new DbTransactionExecute();
            dbTransactionExecute.setConnection(conn);
            DbExecute db = DbFactory.getInstance();
            db.setConn(dbTransactionExecute.getConnection());
            dbTransactionExecute.setDb(db);

            return dbTransactionExecute;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 提交并关闭
     */
    public void commit(){
        try {
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();
        Double t = (endTime - startTime) / 1000d;
        log.debug("-----------事务结束, 耗时: {}----------", t);

    }

    public Connection getConnection() {
        return connection;
    }

    public DbTransactionExecute setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public DbExecute getDb() {
        return db;
    }

    public DbTransactionExecute setDb(DbExecute db) {
        this.db = db;
        return this;
    }
}
