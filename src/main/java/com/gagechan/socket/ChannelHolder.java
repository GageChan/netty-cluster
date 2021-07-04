package com.gagechan.socket;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelHolder {

    private static final Map<Channel, String> cache = new ConcurrentHashMap<>();

    public static void bind(Channel channel, String address) {
        cache.put(channel, address);
//        Integer conter = cache.get(channel);
//        if (conter == null) {
//            cache.put(channel, 1);
//        } else {
//            cache.put(channel, conter+1);
//        }
    }

    public static void remove(Channel channel) {
        cache.remove(channel);
    }

    public static Map<Channel, String> getCache() {
        return cache;
    }
}
