package com.kt.game.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wen TingTing by 2020/3/11
 */
@RestController
public class EurekaControllerApplication {

    @Value("${server.port}")
    private int port;

    @RequestMapping("hi")
    public String hi(@RequestParam(required = false) String name) {
        if (name == null)
            name = "Anonymous";
        return "hi " + name + ", my port=" + port;
    }
}
