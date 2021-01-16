package com.tpy.example.example;

import com.tpy.core.service.tran.ProxyHandler;
import com.tpy.example.dao.TestDao;
import com.tpy.example.dao.UserDao;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemController {

    static Logger log = LoggerFactory.getLogger(SystemController.class);

    public static Router create(RouterHandler routerHandler) {
        // 实获取一个路由
        Router router = routerHandler.getRouter();
        // 增加一个处理器，将请求的上下文信息，放到RoutingContext中
        router.route().handler(routerHandler);
        // 处理一个post方法的rest接口
        router.post("/sys/query").handler(SystemController::queryUser);

        return router;
    }

    // 处理post请求的handler
    private static void queryUser(RoutingContext context) {
        // 从上下文获取请求参数，类似于从httprequest中获取parameter一样
        TestDao dao = ProxyHandler.getCglibProxy(TestDao.class);
        dao.tran();

        // 申明response类型为json格式，结束response并且输出json字符串
        context.response().putHeader("content-type", "application/json")
                .end("ok");
    }

}
