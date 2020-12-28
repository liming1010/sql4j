package com.tpy.pojo.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryModel<T> implements Serializable {

    private static final long serialVersionUID = 2740449386842299520L;
    List<T> list = new ArrayList<T>();
    Long count;

    public List<T> getList() {
        return list;
    }

    public QueryModel setList(List<T> list) {
        this.list = list;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public QueryModel<T> setCount(Long count) {
        this.count = count;
        return this;
    }
}
