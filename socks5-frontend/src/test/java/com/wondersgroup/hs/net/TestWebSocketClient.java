package com.wondersgroup.hs.net;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by xiashenpin on 15/9/26.
 */
public class TestWebSocketClient extends WebSocketClient {


    public TestWebSocketClient(URI serverURI) {
        super(serverURI, new Draft_17());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("abcd".getBytes(Charset.forName("UTF-8")));
    }

//    @Override
//    public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
//        FrameBuilder builder = (FrameBuilder) frame;
//        builder.setTransferemasked( true );
//        getConnection().sendFrame( frame );
//    }

    @Override
    public void onMessage(String message) {
        System.out.println("onTextMessage");
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        //super.onMessage(bytes);
        System.out.println("onMessage");
        int size = bytes.remaining();
        byte[] buffer = new byte[size];
        bytes.get(buffer);
        String msg = new String(buffer, Charset.forName("UTF-8"));
        System.out.println(msg);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
