package com.gagechan.socket.handler;


import com.gagechan.socket.protocol.ReqPacket;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractHandler {

    public abstract void handle(ChannelHandlerContext ctx, ReqPacket reqPacket);

}
