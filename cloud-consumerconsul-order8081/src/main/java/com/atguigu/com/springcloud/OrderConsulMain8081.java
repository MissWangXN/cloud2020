package com.atguigu.com.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderConsulMain8081 {
    public static void main(String[] args) {
        SpringApplication.run(OrderConsulMain8081.class, args);
    }
}
