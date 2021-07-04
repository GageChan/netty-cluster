package com.gagechan.socket.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gagechan.socket.handler.AbstractHandler;
import com.gagechan.socket.protocol.Command;
import com.gagechan.socket.protocol.ReqPacket;
import com.gagechan.socket.protocol.ResPacket;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class UnSupportCommandHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(UnSupportCommandHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, ReqPacket reqPacket) {
        Channel channel = ctx.channel();
        logger.info("UnSupportCommandHandler: {}, msg: {}", channel.id().asShortText(), JSONUtil.toJsonStr(reqPacket));

        ResPacket packet = new ResPacket();
        JSONObject body = JSONUtil.createObj();
        body.set("msg", "unsupport command");
        packet.header(Command.HEARTBEAT).setBody(body);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(packet)));
        ctx.channel().close();
    }
}
