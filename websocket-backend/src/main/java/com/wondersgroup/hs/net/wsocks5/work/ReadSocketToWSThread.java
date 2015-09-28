package com.wondersgroup.hs.net.wsocks5.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiashenpin on 15/9/23.
 */
public class ReadSocketToWSThread extends Thread {
    InputStream in;
    WebSocketSession session;

    Boolean running;

    Logger logger =  LoggerFactory.getLogger(ReadSocketToWSThread.class);

    private byte[] buffer = new byte[409600];

    public ReadSocketToWSThread(InputStream in,WebSocketSession session){
        this.in = in;
        this.session = session;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public void run() {
        try {
            int len = 0;
            setRunning(true);
            while (  running &&  (len = in.read(buffer)) != -1) {
                if (len > 0) {
                    //System.out.println(new String(buffer, 0, len));
                    BinaryMessage bm = new BinaryMessage(buffer,0,len,true);
                    logger.info("send "+len +" bytes");
                    session.sendMessage(bm);

                    //osIn.write(buffer, 0, len);
                    //osIn.flush();
                }
            }
            if (len == -1)
                logger.info("remote disconnect");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
