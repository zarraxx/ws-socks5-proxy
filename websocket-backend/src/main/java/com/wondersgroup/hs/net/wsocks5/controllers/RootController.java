package com.wondersgroup.hs.net.wsocks5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xiashenpin on 15/9/23.
 */
@Controller
@RequestMapping("/test")
public class RootController {
    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("test");
        return  mv;
    }
}
