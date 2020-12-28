package com.tpy.core.manager;

/**
 * 查询条件
 * @param <T>
 */
public interface QueryFun1Manager<T> extends QueryFun2Manager<T> {

    QueryFun1Manager eq(String clos, String value);

    /**
     * BETWEEN  AND
     * @param between
     * @param and
     */
    QueryFun1Manager betweenAnd(String clo, Object between, Object and);

    /**
     * like
     * @param clo
     */
    QueryFun1Manager like(String clo, String value);

    /**
     * order by
     * @param clo
     */
    QueryFun1Manager orderBy(String clo);

    /**
     * 分组
     */
    QueryFun1Manager groupBy(String... clo);

    /**
     * 降序
     */
    QueryFun1Manager desc();

    /**
     * 升序
     */
    QueryFun1Manager asc();

    /**
     * 去重
     */
    QueryFun1Manager distinct(String... clo);

    /**
     * 字符串 s1,s2 等多个字符串合并为一个字符串
     * SELECT CONCAT("SQL ", "Runoob ", "Gooogle ", "Facebook") AS ConcatenatedString
     * @param clo
     * @return
     */
    QueryFun1Manager concat(String... clo);

    /**
     * 正则表示
     * @param regexp
     * @return
     */
    QueryFun1Manager regexp(String clo, String regexp);

    /**
     * IF(条件表达式,"结果为true",'结果为false') as alisName;
     * eg: IF(salary >10000,'高端人群','低端人群') as '级别'
     * @param expression 对象
     * @return
     */
    QueryFun1Manager ifQuery(IfExpression expression);


    /*List<T> executeQuery();

    T executeQueryOne();

    T executeQueryById();*/


}
