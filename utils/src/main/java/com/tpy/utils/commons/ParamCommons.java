package com.tpy.utils.commons;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamCommons {

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
            }else if(o instanceof Date){
                ps.setDate(index, (Date)o);
            }else{
                throw new RuntimeException("参数不知道什么类型");
            }
        }
        return ps;
    }

    public static Object argsPars(Object o){
        if(o instanceof String){
            return o.toString();
        }else if(o instanceof Integer){
            return Integer.parseInt(o.toString());
        }else if(o instanceof Boolean){
            return  Boolean.parseBoolean(o.toString());
        }else if(o instanceof Double){
            return  Double.parseDouble(o.toString());
        }else if(o instanceof Long){
            return  Long.parseLong(o.toString());
        }else if(o instanceof Date){
            return  (Date)o;
        }else{
            throw new RuntimeException("参数不知道什么类型");
        }

    }

    /**
     * 按顺序排序
     * @param li
     * @return
     */
    public static int arrIndex(List li, String s){
        int i = 0;
        for(Object o : li){
            if(o.toString().contains(s)){
                break;
            }
            i++;
        }
        return i;
    }

    public static Boolean isContain(List li){
        Boolean b = true, a = false, d = false;
        for(Object o : li){
            if(o.toString().contains("order by")){
                b = false;
            }
            if(o.toString().contains("asc")){
                a = true;
            }
            if(o.toString().contains("desc")){
                d = true;
            }
        }
        if(b) throw new RuntimeException("asc desc方法请先调用orderBy方法");
        if(a && d) throw new RuntimeException("asc desc不能同时存在");
        return true;
    }

    /**
     * 返回泛型的名称
     * @param clazz
     * @return
     */
    public static String getGeneric (Class clazz){
        Type type = clazz.getGenericSuperclass();
        ParameterizedType pt = null;
        try{
            pt = (ParameterizedType )type;
        }catch (Exception e){
        }
        if(pt == null) throw new RuntimeException("得这格式:public class DemoDao extends SqlManagerImp<User>");
        Type [] actualTypes = pt.getActualTypeArguments();
        return actualTypes[0].getTypeName();
    }


}
