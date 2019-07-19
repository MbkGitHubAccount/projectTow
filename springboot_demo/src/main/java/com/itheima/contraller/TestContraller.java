package com.itheima.contraller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestContraller {
    @RequestMapping("hello")
    public String hello(){
        return "hello, spring boot!";
    }
}
