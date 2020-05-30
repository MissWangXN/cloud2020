package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
//===============================服务降级=========================

    /**
     * hystrix使用了线程池
     * 正常访问，肯定ok的接口
     *
     * @param id
     * @return
     */
    public String paymentInfo_Ok(Integer id) {
        return "线程池" + Thread.currentThread().getName()
                + " paymentInfo_Ok，id:" + id + "\t" + "O(∩_∩)O哈哈~";
    }

    /**
     * 有延时的接口
     * 服务超时，演示降级
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String paymentInfo_TimeOut(Integer id) {
        int timeNumber = 5;
//        int age = 10/0; //同样会出发服务降级
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池" + Thread.currentThread().getName()
                + " paymentInfo_TimeOut，id:" + id + "\t" + "O(∩_∩)O哈哈~"
                + "耗时(秒)：" + timeNumber;
    }

    public String paymentInfo_TimeOutHandler(Integer id) {//回调函数，处理服务降级
        return "线程池：" + Thread.currentThread().getName()
                + "系统繁忙，请稍后再试 id:" + id;
    }

    //===============================服务熔断=========================
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//跳闸时的失败率
    })//以上参数在类HystrixCommandProperties中
    // 配置参数的含义：在一个时间窗口期内10S，如果请求次数达到10次且失败率达到60%,则开启断路器
    //断路器开启后，服务拒绝访问，一定时间后，服务尝试放行请求，如果成功
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("id不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();// 等价 UUID.randomUUID().toString();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id不能是负数，id:" + id;
    }
}
