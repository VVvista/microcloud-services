package com.kt.game;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wen TingTing by 2020/5/10
 */
@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class ConfigReaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigReaderApplication.class, args);
    }

    @Value("${message}")
    private String value;

    @RequestMapping(value = "/")
    public String read() {
        return value;
    }
}