package com.wondersgroup.hs.net.forward;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;

/**
 * Created by xiashenpin on 15/9/26.
 */
public class WSForwardClient extends WebSocketClient {
    OutputStream outputStream;
    Socket socket;
    //private static  final  int bufferSize = 4096;

    public WSForwardClient(URI serverURI,Socket socket) throws IOException {
        super(serverURI,new Draft_17());
        this.socket = socket;
        this.outputStream = socket.getOutputStream();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("onOpen server close");
    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        try {
            System.out.println("remote server close");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception e) {
        try {
            socket.close();
            this.close();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    @Override
    public void onMessage(ByteBuffer data) {
        int bufferLen = data.remaining();
        byte[] buffer = new byte[bufferLen];
        data.get(buffer,0,bufferLen);
        try {
            System.out.println("read from websocket size:"+bufferLen);
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
