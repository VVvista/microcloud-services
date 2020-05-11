package com.kt.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Wen TingTing by 2020/5/10
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RoutingDemoFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoutingDemoFeignApplication.class, args);
    }
}