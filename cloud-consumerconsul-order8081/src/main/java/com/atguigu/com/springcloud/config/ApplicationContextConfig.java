package com.atguigu.com.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration //配置类，配置需要注入到容器中的组件
public class ApplicationContextConfig {

    @Bean //向Spring容器中注入一个bean
    @LoadBalanced //支付服务配置集群后，订单服务访问支付服务时需要给RestTemplate配置负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
//spring 框架
//applicationContext.xml <bean id="" class=""> 等效于注解@Bean