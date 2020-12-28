package com.tpy.utils.commons;



import com.tpy.pojo.table.Column;
import com.tpy.pojo.table.DelFlag;
import com.tpy.pojo.table.Primarykey;
import com.tpy.pojo.table.TableName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClazzUtils<T> {

    static Logger log = LoggerFactory.getLogger(ClazzUtils.class);

    public static PreparedStatement paramsHandle(PreparedStatement ps, List params) throws Exception {
        for(int i = 0; i < params.size(); i++){
            int index = i + 1;
            Object o = params.get(i);
            if(o instanceof String){
                ps.setString(index, o.toString());
            }else if(o instanceof Integer){
                ps.setInt(index, Integer.parseInt(o.toString()));
            }else if(o instanceof Boolean){
                ps.setBoolean(index, Boolean.parseBoolean(o.toString()));
            }else if(o instanceof Double){
                ps.setDouble(index, Double.parseDouble(o.toString()));
            }else if(o instanceof Long){
                ps.setLong(index, Long.parseLong(o.toString()));
            }else if(o instanceof java.sql.Date){
                ps.setDate(index, (java.sql.Date)o);
            }else{
                throw new RuntimeException("参数不知道什么类型");
            }
        }
        return ps;
    }

    /**
     * 单获取字段
     * @param t
     * @return
     */
    public  Map<String, Object> clazzToModel(T t) {
        Class<?> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        TableName tn = clazz.getAnnotation(TableName.class);
        Map<String, Object> map = declearfields(declaredFields, t);
        if(tn != null){
            map.put("tablename", tn.name());
        }else{
            String name = clazz.getSimpleName();
            map.put("tablename", JdbcResultHandler.HumpToUnderline(name));
        }

        return map;
    }

    public Map<String, Object> getId(T t, Class clazz){
        try{
            Map<String, Object> map = new HashMap<>();
            String generic = ParamCommons.getGeneric(clazz);
            Object o = Class.forName(generic).getDeclaredConstructor().newInstance();
            Map<String, Object> m = clazzToModel((T)o);
            map.put("tablename", m.get("tablename"));
            map.put("primary_key", t.toString());

            if(t instanceof String){
                Map<String, String> mv = new HashMap<>();
                mv.put(m.get("primary_key").toString(), t.toString());

                map.put("primary_key", mv);
            }else if(t instanceof Long){
                Map<String, Long> mv = new HashMap<>();
                mv.put(m.get("primary_key").toString(), Long.parseLong(t.toString()));

                map.put("primary_key", mv);
            }else if(t instanceof Integer){
                Map<String, Integer> mv = new HashMap<>();
                mv.put(m.get("primary_key").toString(), Integer.parseInt(t.toString()));

                map.put("primary_key", mv);
            }else{
                Map<String, Object> mx = clazzToModel(t);
                Map<String, Object> mv = new HashMap<>();
                mv.put(m.get("primary_key").toString(), mx.get("id"));

                map.put("primary_key", mv);
            }

            return map;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    private Map<String, Object> declearfields(Field[] declaredFields, T t){
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : declaredFields) {


                // 如果类型是String
                if (field.getGenericType().toString().equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    /**
                     * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
                     * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
                     * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
                     */
                    Method m = (Method) t.getClass().getMethod("get" + getMethodName(field.getName()));
                    String val = (String) m.invoke(t);// 调用getter方法获取属性值
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
                // 如果类型是Integer
                if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                    Method m = (Method) t.getClass().getMethod("get" + getMethodName(field.getName()));
                    Integer val = (Integer) m.invoke(t);
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
                // 如果类型是Double
                if (field.getGenericType().toString().equals("class java.lang.Double")) {
                    Method m = (Method) t.getClass().getMethod("get" + getMethodName(field.getName()));
                    Double val = (Double) m.invoke(t);
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
                // 如果类型是Boolean 是封装类
                if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                    Method m = (Method) t.getClass().getMethod(field.getName());
                    Boolean val = (Boolean) m.invoke(t);
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
                // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
                // 反射找不到getter的具体名
                if (field.getGenericType().toString().equals("boolean")) {
                    Method m = (Method) t.getClass().getMethod(field.getName());
                    Boolean val = (Boolean) m.invoke(t);
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
                // 如果类型是Date
                if (field.getGenericType().toString().equals("class java.util.Date")) {
                    Method m = (Method) t.getClass().getMethod("get" + getMethodName(field.getName()));
                    Date val = (Date) m.invoke(t);
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
                // 如果类型是Short
                if (field.getGenericType().toString().equals("class java.lang.Short")) {
                    Method m = (Method) t.getClass().getMethod("get" + getMethodName(field.getName()));
                    Short val = (Short) m.invoke(t);
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
                if (field.getGenericType().toString().equals("class java.lang.Long")) {
                    Method m = (Method) t.getClass().getMethod("get" + getMethodName(field.getName()));
                    Long val = (Long) m.invoke(t);
                    Map<String, Object> mx = setMapValue(val, field);
                    map.putAll(mx);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    private Map<String, Object> setMapValue(Object val, Field field) throws Exception{
        DelFlag delFlag = field.getAnnotation(DelFlag.class);
        Column col = field.getAnnotation(Column.class);
        Primarykey key = field.getAnnotation(Primarykey.class);
        Map<String, Object> map = new HashMap<>();

        if(delFlag != null){
            map.put("del_flag", col != null ? col.col() : field.getName());
            map.put("del_flag_value", delFlag.delValue());
        }else{
            if(col == null){
                map.put(field.getName(), val);
            }else{
                map.put(col.col(), val);
            }
        }
        if(key != null){
            map.put("primary_key", col != null ? col.col() : field.getName());
            map.put("primary_key_value", val);
        }else{
            if(col == null){
                map.put(field.getName(), val);
            }else{
                map.put(col.col(), val);
            }
        }
        return map;
    }

}
