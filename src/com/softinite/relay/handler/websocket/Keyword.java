package com.softinite.relay.handler.websocket;

/**
 * Created by Sergiu Ivasenco on 16/07/16.
 */
public enum Keyword {

    HOOK("hook");

    private String code;

    private Keyword(String code) {
        setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
