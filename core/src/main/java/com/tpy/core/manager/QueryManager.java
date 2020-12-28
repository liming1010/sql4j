package com.tpy.core.manager;

import java.util.List;

/**
 * 查询
 * @param <T>
 */
public interface QueryManager<T>{


    /**
     * 查询集合
     * @param t
     * @return
     */
    QueryFun1Manager query(T t);

    Fun4Manager query();

    /**
     * 查询总数
     * @param t
     * @return
     */
    QueryFun1Manager queryCount(T t);

}
