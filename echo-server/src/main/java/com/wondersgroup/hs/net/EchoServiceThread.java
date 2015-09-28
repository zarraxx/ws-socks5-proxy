package com.wondersgroup.hs.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by xiashenpin on 15/9/26.
 */
public class EchoServiceThread extends Thread {

    Socket socket;
    private byte[] buffer = new byte[409600];
    boolean running;

    EchoServiceThread(Socket socket){
        this.socket = socket;
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
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            int len = 0;

            while (  running && (len = in.read(buffer)) != -1) {
                if (len > 0) {
                    System.out.println("read size:"+len);
                    out.write(buffer,0,len);
                }
            }

        }catch (IOException e){

        }
    }
}
