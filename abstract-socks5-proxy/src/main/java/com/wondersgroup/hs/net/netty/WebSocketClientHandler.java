package com.wondersgroup.hs.net.netty;

/**
 * Created by zarra on 15/9/28.
 */
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

public abstract class  WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

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
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
        onClose();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            System.out.println("WebSocket Client connected!");
            handshakeFuture.setSuccess();
            onOpen();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println("WebSocket Client received message: " + textFrame.text());
            onTextFrame(textFrame);
        } else if(frame instanceof BinaryWebSocketFrame){
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame)frame;
            onBinaryFrame(binaryFrame);
        }

        else if (frame instanceof PongWebSocketFrame) {
            PongWebSocketFrame pongFrame = (PongWebSocketFrame) frame;
            System.out.println("WebSocket Client received pong");
            onPongFrame(pongFrame);
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }


    protected abstract void onOpen();
    protected abstract void onClose();
    protected abstract void onTextFrame(TextWebSocketFrame frame);
    protected abstract void onBinaryFrame(BinaryWebSocketFrame frame);
    protected abstract void onPongFrame(PongWebSocketFrame frame);


//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        Channel ch = ctx.channel();
//        if (!handshaker.isHandshakeComplete()) {
//            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
//            System.out.println("WebSocket Client connected!");
//            handshakeFuture.setSuccess();
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
//        } else if (frame instanceof PongWebSocketFrame) {
//            System.out.println("WebSocket Client received pong");
//        } else if (frame instanceof CloseWebSocketFrame) {
//            System.out.println("WebSocket Client received closing");
//            ch.close();
//        }
//    }
}