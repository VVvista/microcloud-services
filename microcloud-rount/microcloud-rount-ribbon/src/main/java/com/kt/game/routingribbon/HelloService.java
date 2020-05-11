package com.kt.game.routingribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Wen TingTing by 2020/5/10
 */
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    public String hiService(String name) {
        return restTemplate.getForObject("http://EUREKA-DEMO-CLIENT/hi?name=" + name, String.class);
    }

}