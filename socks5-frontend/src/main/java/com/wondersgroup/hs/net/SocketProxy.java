package com.wondersgroup.hs.net;

/**
 * Created by xiashenpin on 15/9/26.
 */
import java.net.ServerSocket;
import java.net.Socket;

public class SocketProxy {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
//                new SocketThread(socket).start();
                System.out.println();
                new ForwardToWSThread(socket).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
