package com.wondersgroup.hs.net.netty;

import io.netty.channel.ChannelHandler;

import java.util.List;


/**
 * Created by zarra on 15/9/28.
 */
public interface HandlerFactory {
    List<ChannelHandler> createHandler();
}
