package com.wondersgroup.hs.net;

import com.wondersgroup.hs.net.netty.HandlerFactory;
import com.wondersgroup.hs.net.netty.WebSocketClient;
import io.netty.channel.*;
import io.netty.handler.logging.LogLevel;
import io.netty.util.AttributeKey;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zarra on 15/9/29.
 */
public class WebSocketTest {

    Logger logger = LoggerFactory.getLogger(WebSocketTest.class);

    Server server ;
    ServerConnector connector ;

    int port = 8123;

    final static AttributeKey<ChannelPromise> ReadPromiseKey = AttributeKey.valueOf("readPromise");


    class PrintTextAdapter extends ChannelInboundHandlerAdapter{
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
            logger.info("recevie from server:" + msg);
            ChannelPromise promise = ctx.channel().attr(ReadPromiseKey).get();
            Assert.assertNotNull(promise);
            if (promise!=null) {
                promise.setSuccess();
            }
        }
    }

    @Before
    public void setupServer() throws Exception {
        server = new Server();
        connector = new ServerConnector(server);

        connector.setPort(port);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);


        ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

        // Add WebSocket endpoint to javax.websocket layer
        wscontainer.addEndpoint(EventSocket.class);

        server.start();
        //server.dump(System.err);
    }

    @Test
    public void socksProxyTest() throws InterruptedException {
        String urlString = String.format("ws://localhost:%d/events/",port);
        URI uri = URI.create(urlString);
        final ChannelInboundHandlerAdapter adapter = new PrintTextAdapter();

        WebSocketClient webSocketClient = new WebSocketClient(uri, new HandlerFactory() {
            @Override
            public List<ChannelHandler> createHandler() {
                return Arrays.<ChannelHandler>asList(adapter);
            }
        });

        webSocketClient.setLogLevel(LogLevel.DEBUG);

        webSocketClient.connect();


        Channel channel = webSocketClient.getChannelFuture().channel();

        ChannelPromise promise = channel.newPromise();

        channel.attr(ReadPromiseKey).set(promise);

        //readPromise = ;
        channel.write("Hello world");

        promise.sync();


    }

    @After
    public void cleanServer() throws Exception {
        server.stop();
    }
}
