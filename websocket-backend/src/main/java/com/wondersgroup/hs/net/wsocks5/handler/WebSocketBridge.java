package com.wondersgroup.hs.net.wsocks5.handler;


import com.wondersgroup.hs.net.wsocks5.work.ReadSocketToWSThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by xiashenpin on 15/9/23.
 */
public class WebSocketBridge extends BinaryWebSocketHandler {

    Logger logger = LoggerFactory.getLogger(WebSocketBridge.class);

    static private final String KEYSocketToRealSocks5Server = "KEYSocketToRealSocks5Server";
    static private final String KEYThread = "KEYThread";

    String socks5ProxyIP;
    int socks5ProxyPort;

    public String getSocks5ProxyIP() {
        return socks5ProxyIP;
    }

    public void setSocks5ProxyIP(String socks5ProxyIP) {
        this.socks5ProxyIP = socks5ProxyIP;
    }

    public int getSocks5ProxyPort() {
        return socks5ProxyPort;
    }

    public void setSocks5ProxyPort(int socks5ProxyPort) {
        this.socks5ProxyPort = socks5ProxyPort;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String,Object> attributes = session.getAttributes();
        Socket socket = new Socket(getSocks5ProxyIP(),getSocks5ProxyPort());
        logger.info("open socket:"+getSocks5ProxyIP()+":"+getSocks5ProxyPort());
        attributes.put(KEYSocketToRealSocks5Server,socket);
        ReadSocketToWSThread thread = new ReadSocketToWSThread(socket.getInputStream(),session);
        attributes.put(KEYThread,thread);
        thread.start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        Map<String,Object> attributes = session.getAttributes();
        Socket socket = (Socket) attributes.get(KEYSocketToRealSocks5Server);
        ReadSocketToWSThread thread = (ReadSocketToWSThread) attributes.get(KEYThread);

        socket.getOutputStream().flush();
        socket.close();

        thread.setRunning(false);

        thread.join(1000);


    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        logger.info("handleBinaryMessage");
        //super.handleBinaryMessage(session, message);
        ByteBuffer bf = message.getPayload();
        int size = bf.remaining();
        logger.info("get buffer size:"+size);
        byte[] buffer = new byte[size];
        bf.get(buffer,0,size);
        Socket socket = (Socket) session.getAttributes().get(KEYSocketToRealSocks5Server);
        socket.getOutputStream().write(buffer);
    }
}
