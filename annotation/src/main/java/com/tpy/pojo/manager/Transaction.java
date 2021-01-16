package com.tpy.pojo.manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//注解在运行时起作用
@Target(ElementType.METHOD)//只能标记在方法上
public @interface Transaction {
    /**0=>REQUIRED 1=>REQUIRES_NEW*/
    int way() default 0;
}
