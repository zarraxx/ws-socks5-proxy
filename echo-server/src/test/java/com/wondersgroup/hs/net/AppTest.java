package com.wondersgroup.hs.net;


import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    @Test
    public void testApp() throws IOException {


        InetSocketAddress dest = new InetSocketAddress("127.0.0.1", 4321);
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 1234);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);

        Socket socket = new Socket(proxy);

        socket.connect(dest);


        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        String send= "ABCDEFG";
        int length = send.length();
        outputStream.write(send.getBytes());

        byte[] buffer = new byte[1024];

        inputStream.read(buffer,0,length);

        String receive = new String(buffer,0,length, Charset.forName("UTF-8"));


        System.out.println("receive:"+receive);

    }
}
