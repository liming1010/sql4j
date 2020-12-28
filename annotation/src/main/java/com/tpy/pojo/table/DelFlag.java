package com.tpy.pojo.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 逻辑删除标志字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelFlag {

    /**
     * 删除或为删除的值, 如指定 0为不删除, 1为删除
     * @return
     */
    int delValue() default 1;
}
