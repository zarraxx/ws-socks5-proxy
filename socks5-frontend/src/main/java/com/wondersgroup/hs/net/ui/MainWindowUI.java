package com.wondersgroup.hs.net.ui;

import com.wondersgroup.hs.net.forward.SocketProxy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zarra on 15/9/28.
 */
@Component
public class MainWindowUI {
    private JPanel panelRoot;
    private JButton startButton;

    SocketProxy proxy;


    public MainWindowUI() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proxy = new SocketProxy();
                proxy.start();
            }
        });
    }



    public JPanel getPanelRoot() {
        return panelRoot;
    }

    public void setPanelRoot(JPanel panelRoot) {
        this.panelRoot = panelRoot;
    }
}
