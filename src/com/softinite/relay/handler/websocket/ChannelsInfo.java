package com.softinite.relay.handler.websocket;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.MessageConsumer;

import java.io.Serializable;

/**
 * Created by Sergiu Ivasenco on 17/07/16.
 */
public class ChannelsInfo implements Serializable {

    private MessageConsumer<String> inputChannel;
    private String outputChannel;
    private String id;
    private Handler<String> inputMessageHandler;

    public ChannelsInfo(String id) {
        setId(id);
    }

    public MessageConsumer<String> getInputChannel() {
        return inputChannel;
    }

    public void setInputChannel(MessageConsumer<String> inputChannel) {
        this.inputChannel = inputChannel;
    }

    public String getOutputChannel() {
        return outputChannel;
    }

    public void setOutputChannel(String outputChannel) {
        this.outputChannel = outputChannel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Handler<String> getInputMessageHandler() {
        return inputMessageHandler;
    }

    public void setInputMessageHandler(Handler<String> inputMessageHandler) {
        this.inputMessageHandler = inputMessageHandler;
    }
}
