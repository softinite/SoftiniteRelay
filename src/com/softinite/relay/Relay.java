package com.softinite.relay;

import com.softinite.relay.handler.websocket.WebsocketHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Created by Sergiu Ivasenco on 16/07/16.
 */
public class Relay extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.websocketHandler(new WebsocketHandler(vertx));
        setupHttpHandler(httpServer);
        httpServer.listen(8080);
    }

    private void setupHttpHandler(HttpServer httpServer) {
        Router router = Router.router(vertx);
        router.route("/simpleClient").handler(context -> {
            context.response().sendFile("simpleClient.html");
        });
        httpServer.requestHandler(router::accept);
    }
}
