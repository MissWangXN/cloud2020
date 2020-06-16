package com.atguigu.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //取消数据源的自动配置
//@SpringBootApplication
public class SeataOrderApp2001 {
    public static void main(String[] args) {
        SpringApplication.run(SeataOrderApp2001.class, args);
    }
}
