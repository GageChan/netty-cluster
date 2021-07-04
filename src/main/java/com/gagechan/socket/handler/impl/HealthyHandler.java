package com.gagechan.socket.handler.impl;

import com.gagechan.socket.ChannelHolder;
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

import java.util.Collection;

public class HealthyHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(HealthyHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, ReqPacket reqPacket) {
        Channel channel = ctx.channel();
        logger.info("handle healthy: {}, msg: {}", channel.id().asShortText(), JSONUtil.toJsonStr(reqPacket));

        ResPacket packet = new ResPacket();
        JSONObject body = JSONUtil.createObj();
        body.set("online", ChannelHolder.getCache().size());
        packet.header(Command.HEALTHY).setBody(body);

        logger.info("now print the online ip:");
        ChannelHolder.getCache().values().forEach(logger::info);

        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(packet)));
    }
}
