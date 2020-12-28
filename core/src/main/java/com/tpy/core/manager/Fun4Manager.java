package com.tpy.core.manager;

public interface Fun4Manager<T> extends Fun6Manager<T> {


    /**
     * 左连接查询
     * @param t
     * @return
     */
    <T> Fun6Manager leftJoin(T t);

    /**
     * 右链接查询
     * @return
     */
    Fun6Manager rightJoin();

    /**
     * 内连接查询
     * @return
     */
    Fun6Manager innerJoin();

    /**
     * 全连接查询
     * @return
     */
    Fun4Manager union();

    /**
     * 子查询
     * @return
     */
    Fun4Manager childQuery();

    QueryFun1Manager where();

}
