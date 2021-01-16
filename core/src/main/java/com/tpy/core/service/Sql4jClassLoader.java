package com.tpy.core.service;

import com.tpy.pojo.manager.Autowired;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class Sql4jClassLoader extends ClassLoader {

    public static Object getBean(String name) {
        try {
            Class<?> clazz = Class.forName(name);
            Object bean = clazz.newInstance();
            Field[] fileds = clazz.getDeclaredFields();
            for (Field f : fileds) {
                if (f.isAnnotationPresent(Autowired.class)) {
                    // 基于类型注入
                    Class<?> c = f.getType();
                    Object value = c.newInstance();
                    //允许访问private字段
                    f.setAccessible(true);
                    //把引用对象注入属性
                    f.set(bean, value);
                }
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
