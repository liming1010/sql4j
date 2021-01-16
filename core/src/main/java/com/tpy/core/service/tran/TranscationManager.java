package com.tpy.core.service.tran;

import com.tpy.core.service.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


public class TranscationManager {

    Logger log = LoggerFactory.getLogger(TranscationManager.class);

    public void setConn(Method method, Long threadId){
        try {
            ConnectionPool.startConn(method, threadId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void commit(Method method, Long id) throws Exception{
            ConnectionPool.commit(id);
    }
    public void rollback(Method method, Long id){
        try {
            ConnectionPool.rollback(id);
            log.debug("-----------------回滚成功------------");
        } catch (Exception e) {
            log.error("-----------------回滚异常------------");
            e.printStackTrace();
        }
    }

    public void close(Long id){
        try {
            ConnectionPool.close(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
