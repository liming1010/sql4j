package com.tpy.core.manager;

import com.tpy.pojo.model.QueryModel;

public interface QueryFun5Manager<T> {

    <T> QueryModel executeQuery();

    <T> T executeQueryOne();

}
