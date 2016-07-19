package com.softinite.relay.handler.websocket;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Responsible for unit testing FrameHandler
 * Created by Sergiu Ivasenco on 16/07/16.
 */

public class FrameHandlerTest {

    @Test
    public void processMessageNonKeywordDoesNotCallProcessKeyword() {
        FrameHandler handler = Mockito.spy(FrameHandler.class);
        String msg = "Some message";
        Mockito.doNothing().when(handler).sendMessageToEventBus(msg);
        handler.processMessage(msg);

        Mockito.verify(handler, Mockito.times(1)).sendMessageToEventBus(msg);
    }

    @Test
    public void processMessageIfKeywordWithTwoChannelsThenProcessKeywordMessage() {
        FrameHandler handler = Mockito.spy(FrameHandler.class);
        Mockito.doNothing().when(handler).processKeywordMessage("hook", "in", "out");
        String keywordMsg = "hook:in|out";
        handler.processMessage(keywordMsg);

        Mockito.verify(handler, Mockito.times(1)).processKeywordMessage("hook", "in", "out");

    }

    @Test
    public void processMessageIfKeywordWithOneChannelThenProcessKeywordMessage() {
        FrameHandler handler = Mockito.spy(FrameHandler.class);
        Mockito.doNothing().when(handler).processKeywordMessage("hook", "duplex", "");
        String keywordMsg = "hook:duplex";
        handler.processMessage(keywordMsg);

        Mockito.verify(handler, Mockito.times(1)).processKeywordMessage("hook", "duplex", "");

    }

}
