package com.tpy.core.manager;

public interface  Fun3ManagerQuery<T> {

    /**
     * 查询多个对象
     * @param t
     * @param <T>
     * @return
     */
    <T> Fun4Manager <T> query(T... t);


    /**
     * 自定义列名查询
     * @param clos
     * @param <T>
     * @return
     */
    <T> Fun4Manager <T> query(String... clos);
}
