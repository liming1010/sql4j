package com.tpy.example;

import com.tpy.core.service.DataSourceUtils;
import com.tpy.example.example.TestExample;
import com.tpy.example.example.WcService;
import io.vertx.core.Vertx;

public class ExampleApplication {

    public static void main(String[] args) throws ClassNotFoundException {
        DataSourceUtils.init("root", "root", "127.0.0.1:3306/aw");
        Vertx.vertx().deployVerticle(WcService.class.getName());

        // 秒生花严选

       /* ProxyHandler proxyHandler = new ProxyHandler();
        QueryExample cglibProxy = (QueryExample)proxyHandler.getCglibProxy(QueryExample.class);
        cglibProxy.query();*/

        // TestExample.query();

        // QueryAexample.index();

       /* Class<?> clazz = Class.forName("com.tpy.example.example.QueryExample");
        //获取当前类所有方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method:declaredMethods){
            //获取当前类有注解的方法
            Transaction declaredAnnotationMethod = method.getDeclaredAnnotation(Transaction.class);
            if (declaredAnnotationMethod == null){
                continue;
            }
            int age = declaredAnnotationMethod.way();
            System.out.println(age);
        }*/
    }

}
