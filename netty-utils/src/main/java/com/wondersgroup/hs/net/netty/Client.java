package com.wondersgroup.hs.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zarra on 15/9/28.
 */
public class Client {
    Logger logger = LoggerFactory.getLogger(Client.class);
    EventLoopGroup group = null;
    ChannelFuture f = null;

    boolean running = false;
    String host;
    int port;
    LogLevel logLevel = LogLevel.INFO;

    HandlerFactory factory;

    public HandlerFactory getFactory() {
        return factory;
    }

    public void setFactory(HandlerFactory factory) {
        this.factory = factory;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public boolean isRunning() {
        return running;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ChannelFuture getChannelFuture(){
        return f;
    }

    protected ChannelHandler[] getHandlers(){
        return getFactory().createHandler().toArray(new ChannelHandler[0]);
    }

    public void connect(String host, int port){
        if (!host.equals(this.host))
            setHost(host);
        if (port!= this.port)
            setPort(port);

        group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     if (getLogLevel()!= null) {
                         ch.pipeline().addLast(
                                 new LoggingHandler(getLogLevel())
                         );
                     }
                     ch.pipeline().addLast(getHandlers());
                 }
             });
             f = b.connect(host,port).sync();

//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        logger.info("echo server start...");
//                        f.channel().closeFuture().sync();
//                    } catch (InterruptedException var2) {
//                        logger.info("InterruptedException");
//                        var2.printStackTrace();
//                    }
//
//                }
//            }).start();

            running = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        connect(host,port);
    }

    public void close(){
        if(this.f != null) {
            this.f.channel().close();
        }

        if(this.group != null) {
            this.group.shutdownGracefully();
        }

        running = false;
    }

}
