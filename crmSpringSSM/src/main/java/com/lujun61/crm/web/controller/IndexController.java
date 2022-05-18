package com.lujun61.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
    /*
        理论上，给Controller方法分配url：为http://127.0.0.1:8080/crm/……
        框架为了简便，【协议://ip:port/应用名称】必须省去，用/代表应用根目录下的/
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
