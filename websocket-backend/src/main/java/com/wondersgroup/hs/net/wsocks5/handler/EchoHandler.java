package com.wondersgroup.hs.net.wsocks5.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by xiashenpin on 15/9/28.
 */
public class EchoHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(EchoHandler.class);
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("afterConnectionEstablished");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //super.handleTextMessage(session, message);
        String text = message.getPayload();
        logger.info("client send:"+text);
        TextMessage send = new TextMessage("you sent:"+text);
        session.sendMessage(send);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info("afterConnectionClosed");
    }
}
