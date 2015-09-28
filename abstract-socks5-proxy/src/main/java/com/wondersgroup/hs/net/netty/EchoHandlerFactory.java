package com.wondersgroup.hs.net.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by zarra on 15/9/28.
 */
public class EchoHandlerFactory implements HandlerFactory {
    @Override
    public ChannelHandlerAdapter[] createHandler() {
        return new ChannelHandlerAdapter[]{new EchoServerHandler()};
    }
}
