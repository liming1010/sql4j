package com.tpy.core.manager.queryimp;

import com.mysql.cj.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class OtherType {

    public static Boolean isContinue(Object key, int flag, Map map){
        if (key.equals("tablename")) return true;
        if(key.equals("del_flag") || key.equals("del_flag_value") || key.equals("primary_key") || key.equals("primary_key_value") ) return true;
        // 需要where id = ? 是跳过
        if(flag == 1 && key.equals(map.get("primary_key"))) return true;
        return false;
    }

    /**
     * 获取主键和主键的值
     * @param map
     * @return
     */
    public static Map<String, Object> primarykeyvalue(Map map){
        if(map.get("primary_key") == null && map.get("id") == null) throw new RuntimeException("请指定主键ID(默认为ID)");
        if(map.get("primary_key_value") == null) throw new RuntimeException("设置的主键的值为空");

        /*String id = map.get("primary_key") != null ? map.get("primary_key").toString() : "id";
        Object value = map.get("primary_key_value") != null ? map.get("primary_key_value") : map.get(id);*/
        String id = map.get("primary_key").toString();
        Object value = map.get("primary_key_value");
        Map<String, Object> m = new HashMap<>();
        m.put(id, value);
        return m;
    }

    public static Boolean isNullOrEmpty(Object o){
        if(o == null) return false;
        if(o instanceof String){
            if(StringUtils.isNullOrEmpty(o.toString()))return false;
        }
        return true;
    }

}
