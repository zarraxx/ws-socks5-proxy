package com.wondersgroup.hs.net;

import com.wondersgroup.hs.net.netty.socks.SocksProxyServer;
import io.netty.channel.ChannelFuture;
import org.junit.Test;

/**
 * Created by zarra on 15/9/29.
 */
public class SocksProxyTest {
    @Test
    public void socksProxyTest() throws InterruptedException {
        SocksProxyServer socksProxyServer = new SocksProxyServer();
        socksProxyServer.setPort(1181);

        socksProxyServer.start();

        ChannelFuture f= socksProxyServer.getChannelFuture();


        f.channel().closeFuture().sync();


        socksProxyServer.stop();


    }
}
