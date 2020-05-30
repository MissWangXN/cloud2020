package com.atguigu.springcloud;

import com.atguigu.myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
//@RibbonClient注解的作用：访问"CLOUD-PAYMENT-SERVICE"这个服务的时候，使用负载均衡配置：MySelfRule
//@RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)//TODO 阳哥的修改负载均衡策略的做法不生效
//@EnableDiscoveryClient 该注解在springCloud的Greenwich版本上已经不再需要了
//@EnableEurekaClient 该注解适用于eureka注册中心，也是可用可不用，配置文件的优先权更大（配置文件默认配置为注册到注册中心）

public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class, args);
    }
    //修改Ribbon负载均衡策略--生效
//    @Bean
//    public IRule loadBalanceRule(){
//        return new RandomRule();
//    }
}
