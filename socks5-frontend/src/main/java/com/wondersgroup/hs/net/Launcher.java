package com.wondersgroup.hs.net;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.*;

/**
 * Created by zarra on 15/9/28.
 */
public class Launcher {
    public int launch(){
        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        Window window = context.getBean("firstUI", Window.class);

        window.setVisible(true);

        return  0;
    }
    public static void main(String[] args){
        Launcher launcher = new Launcher();
        launcher.launch();
    }
}
