package com.kt.game.routingribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wen TingTing by 2020/5/10
 */
@RestController
public class HelloController {

    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @RequestMapping(value = "/hi")
    public String hi(@RequestParam String name) {
        return helloService.hiService(name);
    }


}