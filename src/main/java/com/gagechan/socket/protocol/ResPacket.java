package com.gagechan.socket.protocol;

import cn.hutool.json.JSONObject;

public class ResPacket {
    private Integer header;
    private JSONObject body;

    public ResPacket header(Command command) {
        this.setHeader(command.getCode());
        return this;
    }

    public Integer getHeader() {
        return header;
    }

    private void setHeader(Integer header) {
        this.header = header;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}