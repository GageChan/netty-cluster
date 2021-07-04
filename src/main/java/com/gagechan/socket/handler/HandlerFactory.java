package com.gagechan.socket.handler;


import com.gagechan.socket.handler.impl.HealthyHandler;
import com.gagechan.socket.handler.impl.HeartbeatHandler;
import com.gagechan.socket.handler.impl.UnSupportCommandHandler;
import com.gagechan.socket.protocol.ReqPacket;

public class HandlerFactory {
    public static AbstractHandler build(ReqPacket reqPacket) {
        switch (reqPacket.getCommand()) {
            case HEARTBEAT:
                return new HeartbeatHandler();
            case HEALTHY:
                return new HealthyHandler();
            default:
                return new UnSupportCommandHandler();
        }
    }
}
