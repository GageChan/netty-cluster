package com.gagechan.socket;

import com.gagechan.AppConfig;
import com.gagechan.Constant;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // http协议解码器
        channel.pipeline().addLast(new HttpServerCodec());
        // 防止数据半包粘包发生
        channel.pipeline().addLast(new HttpObjectAggregator(65536));
        channel.pipeline().addLast(new ChunkedWriteHandler());
        // 心跳
        channel.pipeline().addLast(new IdleStateHandler(AppConfig.getDict().getInt(Constant.SOCKET_SERVER_READER_IDLE_TIME),
                AppConfig.getDict().getInt(Constant.SOCKET_SERVER_WRITER_IDLE_TIME),
                AppConfig.getDict().getInt(Constant.SOCKET_SERVER_IDLE_TIME), TimeUnit.SECONDS));
        // 自定义处理器
        channel.pipeline().addLast(new DefaultChannelHandler());
    }
}
