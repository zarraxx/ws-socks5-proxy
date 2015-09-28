package com.wondersgroup.hs.net.forward;

/**
 * Created by xiashenpin on 15/9/26.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketProxy extends Thread{

    Logger logger = LoggerFactory.getLogger(SocketProxy.class);


    boolean running = true;

    int port = 1234;

    ServerSocket serverSocket = null;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        logger.info("forward server start ...");
        while (running) {
            try {
                serverSocket = new ServerSocket(1234);
                Socket socket = serverSocket.accept();
//                new SocketThread(socket).start();
                //System.out.println();
                new ForwardToWSThread(socket).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        SocketProxy proxy = new SocketProxy();
        proxy.start();

        proxy.join();
    }
}
