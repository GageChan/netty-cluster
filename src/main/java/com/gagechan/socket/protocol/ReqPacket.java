package com.gagechan.socket.protocol;

import cn.hutool.json.JSONObject;

/*{
    "header": 1,
    "body": {
        "ping": "12"
    }
}*/
// 请求协议包
public class ReqPacket {
    private Integer header;
    private JSONObject body;

    public Command getCommand() {
        return Command.from(this.header);
    }

    public Integer getHeader() {
        return header;
    }

    public void setHeader(Integer header) {
        this.header = header;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}