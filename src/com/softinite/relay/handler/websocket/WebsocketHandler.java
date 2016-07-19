package com.softinite.relay.handler.websocket;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiu Ivasenco on 16/07/16.
 */
public class WebsocketHandler implements Handler<ServerWebSocket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketHandler.class);

    private Vertx vertx;

    public WebsocketHandler(Vertx v) {
        vertx = v;
    }

    @Override
    public void handle(ServerWebSocket serverWebSocket) {
        final ChannelsInfo channelsInfo = new ChannelsInfo(serverWebSocket.textHandlerID());
        LOGGER.info("Opening websocket " + channelsInfo.getId());
        channelsInfo.setInputMessageHandler(message -> {
            serverWebSocket.writeFinalTextFrame(message);
        });
        serverWebSocket.frameHandler(new FrameHandler(vertx, channelsInfo));
        serverWebSocket.exceptionHandler(exception -> {
            LOGGER.error("Websocket closed with exception.", exception);
            closeInputChannel(channelsInfo);
        });
        serverWebSocket.closeHandler(close -> {
            LOGGER.info("Closing websocket " + channelsInfo.getId());
            closeInputChannel(channelsInfo);
        });
    }

    private void closeInputChannel(ChannelsInfo channelsInfo) {
        MessageConsumer<String> inputChannel = channelsInfo.getInputChannel();
        if (inputChannel != null) {
            inputChannel.unregister();
        }
    }

}
