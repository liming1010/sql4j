package com.tpy.core.manager;

public interface QueryFun2Manager<T> extends QueryFun5Manager<T> {

    /**
     * 分页查询
     * @param offset
     * @param limit
     * @return
     */
    <T> QueryFun5Manager limit(Integer offset, Integer limit);

}
