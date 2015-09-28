package com.wondersgroup.hs.net.ui;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.swing.*;

/**
 * Created by zarra on 15/9/28.
 */
public class MainWindow extends JFrame {
    @Autowired
    MainWindowUI ui;


    public MainWindow(){

    }

    @PostConstruct
    public void setup(){
        //mainFrame = new MainFrame();
        this.setContentPane(ui.getPanelRoot());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();

        this.setSize(800,600);
    }
}
