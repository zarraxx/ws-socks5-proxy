package com.wondersgroup.hs.net.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.codec.http2.Http2OrHttpChooser;
import io.netty.handler.codec.http2.Http2SecurityUtil;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SupportedCipherSuiteFilter;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import javax.net.ssl.SSLException;
import java.net.URI;

/**
 * Created by zarra on 15/9/28.
 */
public class NettyWebSocketClient extends NettyClient implements HandlerFactory{

    URI uri;

    SslContext sslCtx;

    WebSocketClientHandshaker handshaker;

    WebSocketClientHandler handler;

    HandlerFactory webSocketfactory;

    public NettyWebSocketClient(){
        super.setFactory(this);

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
                    sslCtx = SslContext.newClientContext(null,
                            null, InsecureTrustManagerFactory.INSTANCE, Http2SecurityUtil.CIPHERS,
                    /* NOTE: the following filter may not include all ciphers required by the HTTP/2 specification
                     * Please refer to the HTTP/2 specification for cipher requirements. */
                            SupportedCipherSuiteFilter.INSTANCE,
                            new ApplicationProtocolConfig(
                                    ApplicationProtocolConfig.Protocol.ALPN,
                                    ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL,
                                    ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL,
                                    Http2OrHttpChooser.SelectedProtocol.HTTP_2.protocolName(),
                                    Http2OrHttpChooser.SelectedProtocol.HTTP_1_1.protocolName()),
                            0, 0);
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

        connect(getHost(),getPort());

        handler.handshakeFuture().sync();

    }

    protected  WebSocketClientHandler createWebSockethandler(){
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders());
        WebSocketClientHandler handler =
                new WebSocketClientHandler(handshaker) {
                    @Override
                    protected void onOpen() {

                    }

                    @Override
                    protected void onClose() {

                    }

                    @Override
                    protected void onTextFrame(TextWebSocketFrame frame) {

                    }

                    @Override
                    protected void onBinaryFrame(BinaryWebSocketFrame frame) {

                    }

                    @Override
                    protected void onPongFrame(PongWebSocketFrame frame) {

                    }
                };

        return handler;
    }

    @Override
    public ChannelHandlerAdapter[] createHandler() {
        return new ChannelHandlerAdapter[]{ new HttpClientCodec(),
                new HttpObjectAggregator(8192),
                new WebSocketClientCompressionHandler(),
                handler};
    }
}
