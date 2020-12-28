package com.tpy.core.manager;

public interface UpdateFun3Manager<T> extends UpdateFun4Manager {

    /**
     *
     * @param clos
     * @param value
     * @return
     */
    UpdateFun3Manager eq(String clos, T value);

    /**
     * BETWEEN  AND
     * @param between
     * @param and
     */
    UpdateFun3Manager betweenAnd(String clo, Object between, Object and);

    /**
     * like
     * @param clo
     */
    UpdateFun3Manager like(String clo, String value);

    /**
     * 正则表示
     * @param regexp
     * @return
     */
    UpdateFun3Manager regexp(String clo, String regexp);


}
