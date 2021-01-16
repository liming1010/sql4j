package com.tpy.example.model;

import com.tpy.pojo.table.Column;
import com.tpy.pojo.table.DelFlag;
import com.tpy.pojo.table.Primarykey;
import com.tpy.pojo.table.TableName;

@TableName(name = "user_t")
public class User {

    @Primarykey
            @Column(col = "xx")
    Long id;

    String name;

    String pwd;

    @Column(col = "agers")
    String age;

    @DelFlag
    @Column(col = "flag")
    Integer del_flag;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public User setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getAge() {
        return age;
    }

    public User setAge(String age) {
        this.age = age;
        return this;
    }

    public Integer getDel_flag() {
        return del_flag;
    }

    public User setDel_flag(Integer del_flag) {
        this.del_flag = del_flag;
        return this;
    }
}
