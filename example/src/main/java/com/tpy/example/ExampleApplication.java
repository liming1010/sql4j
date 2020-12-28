package com.tpy.example;

import com.tpy.core.service.DataSourceUtils;
import com.tpy.example.example.QueryExample;

public class ExampleApplication {

    public static void main(String[] args) {
        DataSourceUtils.init("root", "root", "127.0.0.1:3306/aw");
        QueryExample.query();
    }

}
