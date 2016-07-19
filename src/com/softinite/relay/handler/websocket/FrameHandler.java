package com.softinite.relay.handler.websocket;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.WebSocketFrame;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sergiu Ivasenco on 17/07/16.
 */
public class FrameHandler implements Handler<WebSocketFrame> {

    private static final Pattern HOOK_PATTERN = Pattern.compile("(hook)\\:(\\w+)\\|?(\\w*)");
    private static final String EXCLUDE_HEADER_NAME = "exclude";

    private Vertx vertx;
    private ChannelsInfo channelsInfo;

    public FrameHandler(Vertx v, ChannelsInfo channelsInfo) {
        vertx = v;
        setChannelsInfo(channelsInfo);
    }

    protected FrameHandler() {}

    @Override
    public void handle(WebSocketFrame webSocketFrame) {
        processMessage(webSocketFrame.textData());
    }

    protected void processMessage(String msg) {
        Matcher matcher = HOOK_PATTERN.matcher(msg);
        if (matcher.matches()) {
            processKeywordMessage(matcher.group(1), matcher.group(2), matcher.group(3));
        } else {
            sendMessageToEventBus(msg);
        }
    }

    protected void sendMessageToEventBus(String msg) {
        ChannelsInfo channelsInfo = getChannelsInfo();
        String outputChannel = channelsInfo.getOutputChannel();
        if (StringUtils.isNotBlank(outputChannel)) {
            DeliveryOptions options = new DeliveryOptions();
            options.addHeader(EXCLUDE_HEADER_NAME, channelsInfo.getId());
            vertx.eventBus().publish(outputChannel, msg, options);
        }
    }

    protected void processKeywordMessage(String kwd, String ch1, String ch2) {
        switch (kwd) {
            case "hook": tuneChannels(ch1, ch2); break;
        };
    }

    private void tuneChannels(String in, String out) {
        if (StringUtils.isBlank(out)) {
            out = in;
        }
        ChannelsInfo channelsInfo = getChannelsInfo();
        channelsInfo.setOutputChannel(out);
        channelsInfo.setInputChannel(vertx.eventBus().consumer(in, message -> {
            String excludeHeader = message.headers().get(EXCLUDE_HEADER_NAME);
            if (!StringUtils.equals(excludeHeader, channelsInfo.getId())) {
                channelsInfo.getInputMessageHandler().handle(message.body().toString());
            }
        }));
    }

    public ChannelsInfo getChannelsInfo() {
        return channelsInfo;
    }

    public void setChannelsInfo(ChannelsInfo channelsInfo) {
        this.channelsInfo = channelsInfo;
    }
}
