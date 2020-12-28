package com.tpy.core.manager;

/**
 * 多表关联查询
 */
public interface QueryMoreManagerQuery<T> extends QueryFun1Manager<T> {

    /**
     * 别名
     * @return
     */
    QueryMoreManagerQuery as();

    /**
     * on条件
     * @return
     */
    QueryMoreManagerQuery on();

    /**
     * 左连接查询
     * @return
     */
    QueryMoreManagerQuery leftJoin();

    /**
     * 右链接查询
     * @return
     */
    QueryMoreManagerQuery rightJoin();

    /**
     * 内连接查询
     * @return
     */
    QueryMoreManagerQuery innerJoin();

    /**
     * 全连接查询
     * @return
     */
    QueryMoreManagerQuery union();

    /**
     * 子查询
     * @return
     */
    QueryMoreManagerQuery childQuery();

}
