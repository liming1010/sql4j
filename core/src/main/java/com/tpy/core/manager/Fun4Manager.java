package com.tpy.core.manager;

public interface Fun4Manager<T> {



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
