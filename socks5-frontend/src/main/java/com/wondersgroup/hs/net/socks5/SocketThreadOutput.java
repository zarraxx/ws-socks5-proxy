package com.wondersgroup.hs.net.socks5;

/**
 * Created by xiashenpin on 15/9/26.
 */
import java.io.InputStream;
import java.io.OutputStream;
public class SocketThreadOutput extends Thread {
    private InputStream isIn;
    private OutputStream osOut;

    public SocketThreadOutput(InputStream isIn, OutputStream osOut) {
        this.isIn = isIn;
        this.osOut = osOut;
    }

    private byte[] buffer = new byte[409600];

    public void run() {
        try {
            int len;
            while ((len = isIn.read(buffer)) != -1) {
                if (len > 0) {
                    System.out.println(new String(buffer, 0, len));
                    osOut.write(buffer, 0, len);
                    osOut.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("SocketThreadOutput leave");
        }
    }
}