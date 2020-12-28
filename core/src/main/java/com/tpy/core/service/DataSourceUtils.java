package com.tpy.core.service;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourceUtils{

    static Logger log = LoggerFactory.getLogger(DataSourceUtils.class);

    private static DruidDataSource dataSource;

    public static  void init(String dbname, String dbpwd, String dbip){
        //数据源配置
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://"+dbip+"?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT&useSSL=false&allowMultiQueries=true");
        // dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); //这个可以缺省的，会根据url自动识别
        dataSource.setUsername(dbname);
        dataSource.setPassword(dbpwd);

        //下面都是可选的配置
        dataSource.setInitialSize(30);  //初始连接数，默认0
        dataSource.setMaxActive(100);  //最大连接数，默认8
        dataSource.setMinIdle(30);  //最小闲置数
        dataSource.setMaxWait(10000);  //获取连接的最大等待时间，单位毫秒
        dataSource.setPoolPreparedStatements(true); //缓存PreparedStatement，默认false
        dataSource.setMaxOpenPreparedStatements(20); //缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码
        log.debug("-----数据库初始化完成-----");
    }

    /**
     * 得到数据源
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 得到连接对象
     */
    static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 释放资源
     */
    static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt!=null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     * @param conn
     * @param stmt
     */
    static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    static void close(Statement stme, ResultSet rs){close(null,stme, rs);}

}
