package com.tpy.example.example;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterHandler implements Handler<RoutingContext> {

    static Logger log = LoggerFactory.getLogger(RouterHandler.class);

    Router router = null;

    public RouterHandler(Vertx vertx){
        router = createRouter(vertx);
    }

    @Override
    public void handle(RoutingContext routingContext) {

        String token = routingContext.request().getHeader("token");
        log.info("=================== {}", token);
        routingContext.next();
    }

    Router createRouter(Vertx vertx){
        // 实例化一个路由器出来，用来路由不同的rest接口
        Router router = Router.router(vertx);

        router.route().failureHandler(handle -> {
            log.error("Routing error:{}",handle.failure());
            String error = handle.failure().getMessage();
            int code = error != null ? 404 : 500;
            JsonObject obj = new JsonObject();
            obj.put("method", "post").put("error", handle.failure().getMessage()).put("code", code);
            handle.response().putHeader("content-type", "application/json")
                    .end(obj.encodePrettily());
        });
        return router;
    }

    public Router getRouter() {
        return router;
    }
}
