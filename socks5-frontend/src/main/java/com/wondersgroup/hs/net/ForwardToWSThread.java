package com.wondersgroup.hs.net;

import org.java_websocket.client.WebSocketClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * Created by xiashenpin on 15/9/26.
 */
public class ForwardToWSThread extends Thread {

    Socket socket;
    WebSocketClient wsClient;

    InputStream in;
    OutputStream out;

    private byte[] buffer = new byte[409600];

    boolean running;

    public ForwardToWSThread(Socket socketIn){
        this.socket = socketIn;
        running = true;

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        try{
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
            //URI uri = new URI("ws://localhost:8088/bridge");
            URI uri = new URI("ws://218.80.1.144:18000/proxy2/bridge");
            this.wsClient = new WSForwardClient(uri,socket);
            wsClient.connectBlocking();
            int len = 0;
            System.out.println("11 len:"+len);
            while (  running && (len = in.read(buffer)) != -1) {
                if (len > 0) {
                    System.out.println("read from client size:"+len);
                    ByteBuffer bm =ByteBuffer.wrap(buffer,0,len);
                    byte[] send = new byte[len];
                    bm.get(send);
                    wsClient.send(send);
                    //osIn.write(buffer, 0, len);
                    //osIn.flush();
                }
            }

            System.out.println("len:"+len);
            if (len == -1)
                System.out.println("client closed");
            wsClient.close();
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }finally {
            if (wsClient!= null)
                wsClient.close();
            if (socket!= null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
