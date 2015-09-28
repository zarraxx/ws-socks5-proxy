package com.wondersgroup.hs.net;

import com.wondersgroup.hs.net.netty.EchoHandlerFactory;
import com.wondersgroup.hs.net.netty.NettyServer;

import java.io.IOException;

/**
 * Created by zarra on 15/9/28.
 */
public class NettyRun {
    public static void main( String[] args ) throws IOException, InterruptedException {
        NettyServer service = new NettyServer();
        service.setFactory(new EchoHandlerFactory());

        service.start();

        Thread.sleep(1000L);
        service.stop();
        Thread.sleep(1000L);
    }
}
