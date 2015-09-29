package com.wondersgroup.hs.net;

/**
 * Created by zarra on 15/9/29.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ClientEndpoint
@ServerEndpoint(value="/events/")
public class EventSocket
{
    Session session;
    Logger logger = LoggerFactory.getLogger(EventSocket.class);

    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
        logger.info("Socket Connected: " + sess);
        session = sess;
    }

    @OnMessage
    public void onWebSocketText(String message) throws IOException {
        logger.info("!!!!!Received TEXT message: " + message);
        session.getBasicRemote().sendText(message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        logger.info("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
