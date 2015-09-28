package com.wondersgroup.hs.net;


import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * Unit test for simple App.
 */

public class AppTest
{


    @org.junit.Test
    public void testApp() throws URISyntaxException, InterruptedException {
        //WebSocketImpl.DEBUG = true;
        URI uri = new URI("ws://localhost:8088/bridge");
        WebSocketClient wsc = new TestWebSocketClient(uri);
        wsc.connectBlocking();

        wsc.send("hello wold".getBytes(Charset.forName("UTF-8")));

        Thread.sleep(3000);


    }
}
