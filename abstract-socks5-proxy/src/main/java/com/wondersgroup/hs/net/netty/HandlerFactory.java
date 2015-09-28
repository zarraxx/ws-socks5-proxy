package com.wondersgroup.hs.net.netty;

import io.netty.channel.ChannelHandlerAdapter;


/**
 * Created by zarra on 15/9/28.
 */
public interface HandlerFactory {
    ChannelHandlerAdapter[] createHandler();
}
