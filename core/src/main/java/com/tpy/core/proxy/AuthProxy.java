package com.tpy.core.proxy;


import com.tpy.core.service.tran.TranscationManager;
import com.tpy.pojo.manager.Autowired;
import com.tpy.utils.commons.ParamCommons;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AuthProxy<T> implements MethodInterceptor {

    Logger log = LoggerFactory.getLogger(AuthProxy.class);



    @Override
    public T intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Long st = System.currentTimeMillis();
        //通过代理类调用父类中的方法
        T result = (T) methodProxy.invokeSuper(o, objects);
        replaceString(objects, st);

        return result;
    }

    /*public static void main(String[] args) {
        String sql = "select * from teacher  where id = ? and name = ?";
        List list = new ArrayList();
        list.add(2);
        list.add("jack");
        System.out.println(replaceString(sql, list));
    }*/
    public void replaceString(Object[] objects, Long st) {
        if (objects.length == 0 || objects[0] == null) return;
        String sql = objects[0].toString();
        if (objects.length == 1) return;
        /*List<Object> list = (ArrayList)objects[1];
        StringBuilder sb = new StringBuilder(sql);
        int index = 0;
        String args = "";
        for(Object o : list){
            index = sb.indexOf("?", index + 1);

            if(o instanceof String){
                args = "'"+o+"'";
            }else if(o instanceof Date){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                args = "'"+sdf.format((Date)o)+"'";
            }else{
                args = o.toString();
            }
            sb.replace(index, index+1, args);
        }
        Long et = System.currentTimeMillis();
        Double t = (et -st) / 1000d;
        log.debug("sql: {}, 耗时: {}", sb.toString(), t);*/


        Long et = System.currentTimeMillis();
        Double t = (et - st) / 1000d;
        List<Object> list = (ArrayList) objects[1];
        log.info("sql : {}", sql);
        String args = "";
        for (Object o : list) {
            args += o.toString() + ", ";
        }
        log.info("参数: {}", args);
        log.info("耗时: {}", t);

    }

}
