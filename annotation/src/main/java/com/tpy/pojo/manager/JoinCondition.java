package com.tpy.pojo.manager;

import java.io.Serializable;

public class JoinCondition<T> implements Serializable {

    private static final long serialVersionUID = 3688993179073666012L;

    T t;
    String clo;

    public JoinCondition(T t, String clo){
        this.t = t;
        this.clo = clo;
    }

    public T getT() {
        return t;
    }

    public JoinCondition<T> setT(T t) {
        this.t = t;
        return this;
    }

    public String getClo() {
        return clo;
    }

    public JoinCondition<T> setClo(String clo) {
        this.clo = clo;
        return this;
    }
}
