package com.wondersgroup.hs.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import javax.net.ssl.SSLException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zarra on 15/9/28.
 */
public class NettyWebSocketClient extends NettyClient {

    URI uri;

    SslContext sslCtx;

    WebSocketClientHandler handler;


    public NettyWebSocketClient(){


    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri)  {
        if (!uri.equals(this.uri)) {
            this.uri = uri;
            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            final int port;
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }

            final boolean ssl = "wss".equalsIgnoreCase(scheme);

            if (ssl) {
                try {
//                    sslCtx = SslContext.newClientContext(null,
//                            null, InsecureTrustManagerFactory.INSTANCE, Http2SecurityUtil.CIPHERS,
//                    /* NOTE: the following filter may not include all ciphers required by the HTTP/2 specification
//                     * Please refer to the HTTP/2 specification for cipher requirements. */
//                            SupportedCipherSuiteFilter.INSTANCE,
//                            new ApplicationProtocolConfig(
//                                    ApplicationProtocolConfig.Protocol.ALPN,
//                                    ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL,
//                                    ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL,
//                                    Http2OrHttpChooser.SelectedProtocol.HTTP_2.protocolName(),
//                                    Http2OrHttpChooser.SelectedProtocol.HTTP_1_1.protocolName()),
//                            0, 0);
                    sslCtx = SslContextBuilder.forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                } catch (SSLException e) {
                    sslCtx = null;
                }
            } else {
                sslCtx = null;
            }

            setHost(host);
            setPort(port);
        }
    }

    public void connect(URI uri) throws InterruptedException {
        setUri(uri);

        handler = createWebSockethandler();

        connect(getHost(), getPort());

       handler.handshakeFuture().sync();

    }

    protected WebSocketClientHandler createWebSockethandler(){
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders());
        WebSocketClientHandler handler =
                new WebSocketClientHandler(handshaker) ;

        return handler;
    }

    @Override
    protected ChannelHandlerAdapter[] getHandlers() {

        List<ChannelHandler> result = new LinkedList<ChannelHandler>();
        result.add( new HttpClientCodec());
        result.add(new HttpObjectAggregator(8192));
        result.add(getHandler());
        result.add(new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (msg instanceof ByteBuf) {
                    ByteBuf byteBuf = (ByteBuf) msg;
                    WebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
                    ctx.write(frame, promise);
                    return;
                } else if (msg instanceof String) {
                    String text = (String) msg;
                    //logger.info("write:" + text + " to server");
                    WebSocketFrame frame = new TextWebSocketFrame(text);
                    ctx.write(frame, promise);
                    return;
                } else {
                    super.write(ctx, msg, promise);
                }

            }
        });
        for (ChannelHandlerAdapter handlerAdapter:getFactory().createHandler()){
            result.add(handlerAdapter);
        }
        return result.toArray(new ChannelHandlerAdapter[0]);
    }

    public WebSocketClientHandler getHandler(){
        return handler;
    }


}
