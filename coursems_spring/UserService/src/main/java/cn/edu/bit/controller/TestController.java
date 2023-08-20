package cn.edu.bit.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${server.port}")
    String port;

    @RequestMapping("/test")
    public String test(){
        return "Hello world!";
    }

    @RequestMapping("/index")
    public String index(){
        return this.port;
    }
}
