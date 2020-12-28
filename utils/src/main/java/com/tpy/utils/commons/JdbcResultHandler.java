package com.tpy.utils.commons;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class JdbcResultHandler {


    /**
     * 将下划线转换为驼峰的形式，例如：user_name-->userName
     *
     * @param json
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <T> T toSnakeObject(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        T reqJson = null;
        try {
            reqJson = mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return reqJson;
    }

    /**
     * 把ResultSet的结果放到java对象中
     *
     * @param <T>
     * @param rs
     *            ResultSet
     * @param obj
     *            java类的class
     * @return
     */
    public static <T> ArrayList<T> putResult(ResultSet rs, Class<T> obj) {
        try {
            ArrayList<T> arrayList = new ArrayList<T>();
            ResultSetMetaData metaData = rs.getMetaData();
            /**
             * 获取总列数
             */
            int count = metaData.getColumnCount();
            while (rs.next()) {
                /**
                 * 创建对象实例
                 */
                T newInstance = obj.newInstance();
                for (int i = 1; i <= count; i++) {
                    /**
                     * 给对象的某个属性赋值
                     */
                    String name = metaData.getColumnName(i).toLowerCase();
                    name = toJavaField(name);// 改变列名格式成java命名格式
                    String substring = name.substring(0, 1);// 首字母大写
                    String replace = name.replaceFirst(substring, substring.toUpperCase());
                    Class<?> type = null;
                    try {
                        type = obj.getDeclaredField(name).getType();// 获取字段类型
                    } catch (NoSuchFieldException e) { // Class对象未定义该字段时,跳过
                        continue;
                    }

                    Method method = obj.getMethod("set" + replace, type);
                    /**
                     * 判断读取数据的类型
                     */
                    if (type.isAssignableFrom(String.class)) {
                        method.invoke(newInstance, rs.getString(i));
                    } else if (type.isAssignableFrom(byte.class) || type.isAssignableFrom(Byte.class)) {
                        method.invoke(newInstance, rs.getByte(i));// byte 数据类型是8位、有符号的，以二进制补码表示的整数
                    } else if (type.isAssignableFrom(short.class) || type.isAssignableFrom(Short.class)) {
                        method.invoke(newInstance, rs.getShort(i));// short 数据类型是 16 位、有符号的以二进制补码表示的整数
                    } else if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
                        method.invoke(newInstance, rs.getInt(i));// int 数据类型是32位、有符号的以二进制补码表示的整数
                    } else if (type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) {
                        method.invoke(newInstance, rs.getLong(i));// long 数据类型是 64 位、有符号的以二进制补码表示的整数
                    } else if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) {
                        method.invoke(newInstance, rs.getFloat(i));// float 数据类型是单精度、32位、符合IEEE 754标准的浮点数
                    } else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) {
                        method.invoke(newInstance, rs.getDouble(i));// double 数据类型是双精度、64 位、符合IEEE 754标准的浮点数
                    } else if (type.isAssignableFrom(BigDecimal.class)) {
                        method.invoke(newInstance, rs.getBigDecimal(i));
                    } else if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) {
                        method.invoke(newInstance, rs.getBoolean(i));// boolean数据类型表示一位的信息
                    } else if (type.isAssignableFrom(Date.class)) {
                        method.invoke(newInstance, rs.getDate(i));
                    }
                }
                arrayList.add(newInstance);
            }
            return arrayList;

        } catch (InstantiationException | IllegalAccessException | SQLException | SecurityException | NoSuchMethodException | IllegalArgumentException
                | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据库命名格式转java命名格式
     *
     * @param str
     *            数据库字段名
     * @return java字段名
     */
    public static String toJavaField(String str) {

        String[] split = str.split("_");
        StringBuilder builder = new StringBuilder();
        builder.append(split[0]);// 拼接第一个字符

        // 如果数组不止一个单词
        if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
                // 去掉下划线，首字母变为大写
                String string = split[i];
                String substring = string.substring(0, 1);
                split[i] = string.replaceFirst(substring, substring.toUpperCase());
                builder.append(split[i]);
            }
        }

        return builder.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */
    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//偏移量，第i个下划线的位置是 当前的位置+ 偏移量（i-1）,第一个下划线偏移量是0
        for(int i=0;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                if(i == 0){
                    sb.insert(i+temp, "");
                }else{
                    sb.insert(i+temp, "_");
                }

                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }
}
