package com.wondersgroup.hs.net.netty.socks;

import com.wondersgroup.hs.net.netty.Server;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.socksx.SocksPortUnificationServerHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by zarra on 15/9/29.
 */
public final class SocksProxyServer extends Server {
    @Override
    protected ChannelHandler[] getHandlers() {
        return new ChannelHandler[]{
                new LoggingHandler(LogLevel.DEBUG),
                new SocksPortUnificationServerHandler(),
                SocksServerHandler.INSTANCE};
    }
}
