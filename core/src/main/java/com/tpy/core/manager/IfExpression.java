package com.tpy.core.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * IF(条件表达式,"结果为true",'结果为false') as alisName;
 * eg: IF(salary >10000,'高端人群','低端人群') as '级别'
 */
public class IfExpression {

    Logger log = LoggerFactory.getLogger(IfExpression.class);

    static String expression;


    /**
     * 大于
     * @param clo 列
     * @param value 比较的值
     * @param isTrue 为真返回
     * @param isFalse 为假返回
     * @param alisName 别名
     */
    public static void setGreaterThan(String clo, Object value, String isTrue, String isFalse, String alisName){
        if(value instanceof Integer){
        }else if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            value = sdf.format((Date)value);
        }else{
            throw new RuntimeException("if条件 "+clo+" 条件异常, 参数类型为Integer or Date");
        }
        String par = "if("+clo+" > "+ value +", "+isTrue+", "+isFalse+") as "+alisName;
        expression = par;
    }

    /**
     * 小于
     * @param clo 列
     * @param value 比较值
     * @param isTrue 为真返回
     * @param isFalse 为假返回
     * @param alisName 别名
     */
    public static void setLessThan(String clo, Object value, String isTrue, String isFalse, String alisName){
        if(value instanceof Integer){
        }else if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            value = sdf.format((Date)value);
        }else{
            throw new RuntimeException("if条件 "+clo+" 条件异常, 参数类型为Integer or Date");
        }
        String par = "if("+clo+" < "+ value +", "+isTrue+", "+isFalse+") as " + alisName;
        expression = par;
    }

    /**
     * 大于小的，小于大的  a > b > c
     * @param clo
     * @param greaterValue 大于的值
     * @param lessValue 小于的值
     * @param isTrue 为真返回
     * @param isFalse 为假返回
     * @param alisName 别名
     */
    public static void than(String clo, Object greaterValue, Object lessValue, String isTrue, String isFalse, String alisName){
        if(greaterValue instanceof Integer && lessValue instanceof Integer){
        }else if(greaterValue instanceof Date && lessValue instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            greaterValue = sdf.format((Date)greaterValue);
            lessValue = sdf.format((Date)lessValue);
        }else{
            throw new RuntimeException("if条件 "+clo+" 条件异常, 参数类型为Integer or Date");
        }
        String par = "if("+clo+" < "+ greaterValue +" and "+clo+" > "+lessValue+", "+isTrue+", "+isFalse+") as "+alisName;
        expression = par;
    }

    public String getExpression() {
        return expression;
    }

}
