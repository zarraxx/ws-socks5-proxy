package com.wondersgroup.hs.net.netty;

/**
 * Created by zarra on 15/9/28.
 */
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        logger.info("handshakeFuture init");
        handshakeFuture = ctx.newPromise();
        logger.info("handshakeFuture:"+handshakeFuture);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
        ctx.fireChannelInactive();
    }

//    @Override
//    public void messageReceived(ChannelHandlerContext ctx, Object msg) {
//        Channel ch = ctx.channel();
//        if (!handshaker.isHandshakeComplete()) {
//            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
//            System.out.println("WebSocket Client connected!");
//            handshakeFuture.setSuccess();
//            ctx.fireChannelActive();
//            return;
//        }
//
//        if (msg instanceof FullHttpResponse) {
//            FullHttpResponse response = (FullHttpResponse) msg;
//            throw new IllegalStateException(
//                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
//                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
//        }
//
//        WebSocketFrame frame = (WebSocketFrame) msg;
//        if (frame instanceof TextWebSocketFrame) {
//            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
//            System.out.println("WebSocket Client received message: " + textFrame.text());
//            String text = textFrame.text();
//            ctx.fireChannelRead(text);
//        } else if(frame instanceof BinaryWebSocketFrame){
//            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame)frame;
//            ByteBuf byteBuf = binaryFrame.content();
//            ctx.fireChannelRead(byteBuf);
//        }
//
//        else if (frame instanceof PongWebSocketFrame) {
//            PongWebSocketFrame pongFrame = (PongWebSocketFrame) frame;
//            System.out.println("WebSocket Client received pong");
//            //onPongFrame(pongFrame);
//        } else if (frame instanceof CloseWebSocketFrame) {
//            System.out.println("WebSocket Client received closing");
//            ch.close();
//        }
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        logger.info("write in handler");
//        if (msg instanceof ByteBuf){
//            ByteBuf byteBuf = (ByteBuf) msg;
//            WebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
//            ctx.write(frame,promise);
//            return;
//        }
//        else if(msg instanceof String){
//            String text = (String) msg;
//            logger.info("write:"+text+ " to server");
//            WebSocketFrame frame = new TextWebSocketFrame(text);
//            ctx.write(frame,promise);
//            return ;
//        }else {
//            ctx.write(msg, promise);
//        }
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            System.out.println("WebSocket Client connected!");
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.getStatus() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String text = textFrame.text();
            ctx.fireChannelRead(text);
            //System.out.println("WebSocket Client received message: " + textFrame.text());
        } else if(frame instanceof BinaryWebSocketFrame){
           BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame)frame;
            ByteBuf byteBuf = binaryFrame.content();
            ctx.fireChannelRead(byteBuf);
        }else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
        }
    }
}