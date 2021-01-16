/*
package com.tpy.core.service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import com.tpy.pojo.manager.Transaction;
import com.tpy.utils.commons.ClazzUtils;

*/
/**
 * @author Administrator
 *
 *//*

public class ConnectionUtil {
    */
/**
     * 单条线程方法调用链开启的Connection集合，单条线程维护一个开启Connection的栈，先开启的，最后关闭
     * methodA 调用 methodB ，methodB调用methodC，从而形成ConnectionA，ConnectionB，ConnectionC
     * *//*

    private static ThreadLocal<Stack<Connection>> threadLocal=new ThreadLocal<>();
    */
/**
     * 多个线程调用同一个方法产生的connection队列
     * 线程1执行方法A，产生connection1；线程2执行方法A，产生connection2
     * 先开启的先执行完
     * 只有当传播行为是REQUIRED才会维护该集合
     * *//*

    private static Map<Method,Deque<Connection>> openConnList=new ConcurrentHashMap<>();

    */
/**
     * 重构成原型模式-深拷贝/浅拷贝 ？
     * 对于多条线程在同一个事务的传播行为
     * 某条线程回滚，其他线程也要回滚，从这个方向看，多条线程操作的是同一个对象--浅拷贝
     * 对于多线程不在同一个事务
     * 线程之间事务状态互不影响，操作的不是同一对象，可能同一时刻各自事务的状态也不同--开辟新对象新空间，不能用原型模式
     * 没法实现原型模式，由于connection是mysql定义，无法使用多态
     * @param method
     * @return
     * @throws Exception
     *//*

    public static Connection getConnection(Method method) throws Exception{
        if(method==null){
            return threadLocal.get().peek();
        }
        //获取注解
        Transaction transcation = method.getAnnotation(Transaction.class);

        if(threadLocal.get()==null){
            Stack<Connection> stack=new Stack<>();
            threadLocal.set(stack);
        }
        //当前线程没有已开启的connection
        if(threadLocal.get().isEmpty()){
            Connection connection=DataSourceUtils.getConnection();
            threadLocal.get().push(connection);
        }
        //单条线程执行的方法再调用别的方法开启的Connection
        else if(transcation.way()==0&&!threadLocal.get().isEmpty()){//被调用的方法且传播行为是0，相当于spring的REQUIRED
            //同一条线程内就不需要拷贝Connection了
            Connection peek = threadLocal.get().peek();
            threadLocal.get().push(peek);
        }else if(transcation.way()==1&&!threadLocal.get().isEmpty()){//被调用的方法且传播行为是1，相当于spring的REQUIRES_NEW
            Connection connection=DataSourceUtils.getConnection();
            threadLocal.get().push(connection);
        }
        //返回栈顶元素，即最里面Connection
        return threadLocal.get().peek();
    }
    public  static void startTranscation(Method method) throws Exception{
        Transaction transcation = method.getAnnotation(Transaction.class);
        Connection connection=null;
        if(transcation.way()==0){
            //由于openConnList是静态变量需要同步锁，ConnectionUtil.class作为锁颗粒度太大，应该用openConnList+双重检查机制
            synchronized (ConnectionUtil.class) {
                //该方法connection队列为空
                if(openConnList.get(method)==null){
                    Deque<Connection> value=new LinkedList<>();
                    openConnList.put(method, value);
                }
                //获取队首元素
                connection = openConnList.get(method).peek();
                System.out.println("["+Thread.currentThread().getName()+"] openConnFirst>>"+connection);
                //新开一个connection
                Connection connection2 = getConnection(method);
                //当前没有其它线程在method方法上开启过事务
                if(connection==null){
                    connection=connection2;
                }else{//有线程开启过事务，将已开启的connection的值赋给新开的connection2(对于多线程)
                    //由于是多个线程同处一个事务，需要拷贝
                    ClazzUtils.copyProperties(connection, connection2);
                    Connection pop = threadLocal.get().pop();
                    pop=connection2;
                    threadLocal.get().push(pop);
                }
                //入队
                openConnList.get(method).offer(connection2);
            }
        }else{
            connection = getConnection(method);
        }
        System.out.println("["+Thread.currentThread().getName()+"] start connection >>"+connection);

        connection.setAutoCommit(false);
    }
    public static void commit(Method method) throws Exception{
        if(threadLocal.get()==null){
            return ;
        }
        //栈顶元素,获取当前线程最后开启的connection
        Connection connection = threadLocal.get().peek();
        Transaction transcation = method.getAnnotation(Transaction.class);
        if(transcation.way()==0){
            synchronized (ConnectionUtil.class) {
                //多线程情况下，method上最后一条线程执行完才会真正提交事务
                if(openConnList.get(method).size()==1){
                    if(!connection.isClosed()){
                        System.out.println("["+Thread.currentThread().getName()+"] method>>"+method.getName()+" commit>>");
                        if(threadLocal.get().size()==1){//对于单线程本身，最先开启connection的方法执行完才是真正提交事务(即栈底元素)
                            if(!connection.isClosed()){
                                connection.commit();
                                connection.close();
                            }
                            threadLocal.set(null);
                        }else{//单线程中，不是最先开启connection(非栈底元素)
                            threadLocal.get().pop();
                        }
                    }
                }
                //多个线程的connection出队
                if(!openConnList.get(method).isEmpty()){
                    openConnList.get(method).pop();
                }
            }
        }else{
            if(!connection.isClosed()){
                connection.commit();
                connection.close();
            }

            if(threadLocal.get().size()>1){//非最先开启的connection
                threadLocal.get().pop();//单个线程中connection出栈
            }else{
                threadLocal.set(null);
            }
        }
    }
    public static void rollback(Method method) throws Exception{
        if(threadLocal.get()==null){
            return ;
        }
        //取出栈顶元素
        Connection connection = threadLocal.get().pop();
        Transaction transcation = method.getAnnotation(Transaction.class);
        if(transcation.way()==0){
            synchronized (ConnectionUtil.class) {
                if(!openConnList.get(method).isEmpty()){
                    openConnList.get(method).pop();
                }
                if(connection!=null&&!connection.isClosed()){
                    System.out.println("["+Thread.currentThread().getName()+"] rollback>>");
                    connection.rollback();
                    connection.close();
                }
            }
        }else{
            if(connection!=null&&!connection.isClosed()){
                connection.rollback();
                connection.close();
            }
        }
        if(threadLocal.get().isEmpty()){
            threadLocal.set(null);
        }
//		threadLocal.set(null);
    }
    public static void close(ResultSet rs,PreparedStatement ps,Connection conn){
        try {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(conn!=null){
                if(conn.getAutoCommit()){
                    conn.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
*/
