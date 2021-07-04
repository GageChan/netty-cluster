package com.gagechan;

import com.gagechan.socket.DefaultChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class SocketServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup childGroup = new NioEventLoopGroup();
    private Channel channel;

    public void start(Integer port) throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new DefaultChannelInitializer());

            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
            channelFuture = b.bind(new InetSocketAddress(port))
                .syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("socket server start error, {}", e.getMessage());
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("socket server start on {}. ", port);
            } else {
                logger.error("socket server start error. ");
            }
        }
    }

    public void destroy() {
        if (null == channel)
            return;
        channel.close();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }

}
