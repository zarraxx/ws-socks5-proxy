package com.wondersgroup.hs.net.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zarra on 15/9/28.
 */
public class NettyServer {
    int port = 1234;
    HandlerFactory factory;
    boolean running = false;

    Logger logger = LoggerFactory.getLogger(NettyServer.class);

    EventLoopGroup bossGroup = null;
    EventLoopGroup workerGroup = null;
    ChannelFuture f = null;

    public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(getFactory().createHandler());
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            //logger.info("handlerRemoved");
            super.handlerRemoved(ctx);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HandlerFactory getFactory() {
        return factory;
    }

    public void setFactory(HandlerFactory factory) {
        this.factory = factory;
    }

    public boolean isRunning() {
        return running;
    }

    public void start(){
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChildChannelHandler());

            f = b.bind(port).sync();
            running = true;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        logger.info("server start...");
                        f.channel().closeFuture().sync();
                        logger.info("server closed...");
                    } catch (InterruptedException var2) {
                        logger.info("InterruptedException");
                        var2.printStackTrace();
                    }

                }
            }).start();

        } catch (InterruptedException e) {
            e.printStackTrace();

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop(){
        logger.info("now try to close server...");
        if(this.f != null) {
            this.f.channel().close();
        }

        if(this.bossGroup != null) {
            this.bossGroup.shutdownGracefully();
        }

        if(this.workerGroup != null) {
            this.workerGroup.shutdownGracefully();
        }
        running = false;
    }
}
