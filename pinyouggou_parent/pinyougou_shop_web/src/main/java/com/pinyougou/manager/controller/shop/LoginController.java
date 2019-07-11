package com.pinyougou.manager.controller.shop;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {


    @RequestMapping("/loadLoginName")
    public Map<String,String > login(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<String,String> map=new HashMap<>();
         map.put("userName",name);
         return map;



    }
}
