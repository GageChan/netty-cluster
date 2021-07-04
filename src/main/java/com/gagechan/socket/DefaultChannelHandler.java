package com.gagechan.socket;

import cn.hutool.json.JSONUtil;
import com.gagechan.socket.handler.AbstractHandler;
import com.gagechan.socket.handler.HandlerFactory;
import com.gagechan.socket.protocol.ReqPacket;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;

public class DefaultChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultChannelHandler.class);

    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{}与服务器建立了连接", ctx.channel().id().asShortText());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // websocket握手,在升级成webdocket前发送的协议是http协议
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
            handshaker = wsFactory.newHandshaker(httpRequest);
            if (null == handshaker) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // 握手
                ChannelFuture channelFuture = handshaker.handshake(ctx.channel(), httpRequest);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            Channel channel = ctx.channel();
                            SocketAddress socketAddress = channel.remoteAddress();
                            ChannelHolder.bind(channel, socketAddress.toString());
                        }
                    }
                });
            }
            return;
        }
        // 消息处理
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame)msg;
            try {
                ReqPacket reqPacket = JSONUtil.toBean(frame.text(), ReqPacket.class);
                AbstractHandler handler = HandlerFactory.build(reqPacket);
                assert handler != null;
                handler.handle(ctx, reqPacket);
            } catch (Exception e) {
                // 传入错误的包, 剔除该连接, 移除关联
                logger.error("receive the bad packet:{}", msg, e);
                ctx.channel().close();
            }
            return;
        }
        // 断开时移除握手的缓存数据
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), ((CloseWebSocketFrame) msg).retain());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame frame) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelHolder.remove(ctx.channel());
        logger.info("{}与服务器断开了连接", ctx.channel().id().asShortText());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.ALL_IDLE) {
            ChannelHolder.remove(ctx.channel());
            logger.info("心跳停止:{}", ctx.channel().id().asShortText());
            ctx.channel().close();
        }
    }
}
