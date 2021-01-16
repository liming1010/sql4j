package com.tpy.core.manager.queryimp;

import com.mysql.cj.util.StringUtils;
import com.tpy.core.manager.*;
import com.tpy.core.manager.InsertImp.InsertFun2ManagerImp;
import com.tpy.core.manager.updateimp.UpdateFun1ManagerImp;
import com.tpy.core.manager.updateimp.UpdateFun4ManagerImp;
import com.tpy.core.proxy.AuthProxy;
import com.tpy.core.proxy.AuthProxyFilter;
import com.tpy.core.proxy.ProxyFactory;
import com.tpy.core.service.DataSourceUtils;
import com.tpy.core.service.DbExecuteImp;
import com.tpy.core.service.DbFactory;
import com.tpy.core.service.tran.ProxyHandler;
import com.tpy.pojo.manager.Transaction;
import com.tpy.utils.commons.ClazzUtils;
import com.tpy.utils.commons.JdbcResultHandler;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.*;
import java.util.logging.Handler;

public class SqlManagerImp<T> implements SqlManager<T> {

        Logger log = LoggerFactory.getLogger(SqlManagerImp.class);


        @Override
        public QueryFun1Manager query(T t) {
            StackTraceElement[] stack = (new Throwable()).getStackTrace();
            Method[] declaredMethods = this.getClass().getDeclaredMethods();
            for(Method method : declaredMethods){
                if(method.getName().equals(stack[1].getMethodName())){
                    Transaction transaction = method.getAnnotation(Transaction.class);
                    if(transaction != null){
                        long id = Thread.currentThread().getId();
                        System.out.println(id+"-----------事务方法----------, ");
                    }
                    break;
                }
            }

            return Merges.querys(t);
        }


        @Override
        public Fun4Manager query() {
            Type type = this.getClass().getGenericSuperclass();
            ParameterizedType pt = null;
            try {
                pt = (ParameterizedType) type;
            } catch (Exception e) {
            }
            if (pt == null) throw new RuntimeException("得这格式:public class UserDao extends SqlManager<User>");
            Type[] actualTypes = pt.getActualTypeArguments();
            if (actualTypes.length == 0) throw new RuntimeException("得这格式:public class UserDao extends SqlManager<User>");
            Fun4Manager f4 = null;
            try {
                Object o = Class.forName(actualTypes[0].getTypeName()).getDeclaredConstructor().newInstance();
                f4 = new Fun4ManagerImp(o);
                // 给目标对象，创建代理对象
            } catch (Exception e) {
                e.printStackTrace();
            }


            return f4;
        }

    @Override
    public QueryFun1Manager queryCount(T t) {
        return null;
    }

    @Override
    public UpdateFun1Manager update(T t) {
        UpdateFun1Manager uf1 = Merges.updetas(t, 0);
        return uf1;
    }

    @Override
    public UpdateFun4Manager updateById(T t) {
        /**
         * tran为真是事务方法
         *     为假非事务方法
         */
        // Boolean className = ClazzUtils.getTran();

        return Merges.updetas(t, 1);
    }

    @Override
    public UpdateFun1Manager delByLogic(T t) {
        String sql = "update %s set %s ";
        return Merges.dels(sql, t, 0);
    }

    @Override
    public UpdateFun4Manager delByLogicByPrimaryKkey(T t) {
        String sql = "update %s set %s = 1 where %s = %s";
        return Merges.delsid(sql, t, 0);
    }

    @Override
    public UpdateFun1Manager delByPhysics(T t) {
        /**
         * tran为真是事务方法
         *     为假非事务方法
         */
        String sql = "delete from %s ";
        return Merges.dels(sql, t, 1);
    }

    @Override
    public UpdateFun4Manager delPhysicsByPrimaryKkey(T t) {
        String sql = "delete from %s where %s = %s";
        return Merges.delsid(sql, t, 1);
    }

    @Override
    public InsertFun2Manager insert(T t) {
        List list = new ArrayList();
        list.add(t);
        InsertFun2Manager inserts = Merges.inserts(list);
        return inserts;
    }

    @Override
    public InsertFun2Manager insertBatch(List<T> list) {
        InsertFun2Manager inserts = Merges.inserts(list);
        return inserts;
    }



}
