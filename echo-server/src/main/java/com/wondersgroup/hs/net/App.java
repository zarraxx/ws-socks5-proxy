package com.wondersgroup.hs.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4321);
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
//                new SocketThread(socket).start();
                new EchoServiceThread(socket).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
