package com.wondersgroup.hs.net;

import com.wondersgroup.hs.net.netty.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zarra on 15/9/28.
 */
public class NettyRun {

    static Logger logger = LoggerFactory.getLogger(NettyRun.class);

    public static void main( String[] args ) throws IOException, InterruptedException, URISyntaxException {
        //NettyServer service = new NettyServer();
        //service.setFactory(new EchoHandlerFactory());

        //service.start();


        HandlerFactory factory = new HandlerFactory() {
            @Override
            public ChannelHandlerAdapter[] createHandler() {
                return new ChannelHandlerAdapter[]{
                        new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                super.channelInactive(ctx);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //super.channelRead(ctx, msg);
                                if (msg instanceof String){
                                    logger.info(""+msg);
                                }
                            }
                        }
                };
            }
        };

        NettyWebSocketClient webSocketClient = new NettyWebSocketClient();
        webSocketClient.setFactory(factory);
        webSocketClient.setShowLog(false);
        webSocketClient.connect(new URI("ws://localhost:8088/echo"));

        Channel channel = webSocketClient.getChannel();

//        WebSocketClientHandler handler = webSocketClient.getHandler();
//        handler.handshakeFuture().sync();


        //Thread.sleep(1000L);
        //service.stop();
        Thread.sleep(1000L);

        channel.writeAndFlush("Hello websocket");

        Thread.sleep(1000L);


        webSocketClient.close();


    }
}
