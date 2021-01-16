package com.tpy.example.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WcService extends AbstractVerticle {

    Logger log = LoggerFactory.getLogger(this.getClass());

    public void start() {

        /*vertx.eventBus().addInboundInterceptor(inhandle -> {
            Message<Object> message = inhandle.message();
            log.info("in: {}", message.toString());
            inhandle.next();
        }).addOutboundInterceptor(outHandle -> {
            Message<Object> message = outHandle.message();
            log.info("out: {}", message.toString());
            outHandle.next();
        });*/


        // 创建一个httpserver，监听8080端口，并交由路由器分发处理用户请求
        RouterHandler routerHandler = new RouterHandler(vertx);

        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setSsl(false)
                .setIdleTimeout(10);// 闲置超时时间

        vertx.createHttpServer(httpServerOptions)
                .requestHandler(SystemController.create(routerHandler)::accept)
                .listen(8081, server -> {
                    if (server.succeeded()) {
                        // 初始化数据库连接
                        log.info("-------启动成功-------");
                    }
                });
    }

}
