package com.tpy.example.model;

import com.tpy.pojo.table.TableName;

import java.io.Serializable;

@TableName(name = "test")
public class TestModel implements Serializable {

    private static final long serialVersionUID = -575137406594721390L;
    String name;
    String age;

    public String getName() {
        return name;
    }

    public TestModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getAge() {
        return age;
    }

    public TestModel setAge(String age) {
        this.age = age;
        return this;
    }
}
