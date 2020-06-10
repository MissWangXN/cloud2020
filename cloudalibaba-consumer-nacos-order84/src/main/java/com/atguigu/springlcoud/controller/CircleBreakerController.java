package com.atguigu.springlcoud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class CircleBreakerController {
    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback") //没有任何配置
//    @SentinelResource(value = "fallback", fallback = "handlerFallback")//fallback属性负责运行时异常，调用回调函数，实现服务降级
//    @SentinelResource(value = "fallback", blockHandler = "blockHandler")//blockHandler只负责控制台配置违规
//    @SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler")
    @SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler"
            , exceptionsToIgnore = {IllegalArgumentException.class})
//都配置
    public CommonResult<Payment> fallback(@PathVariable Long id) {
        CommonResult<Payment> result =
                restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class, id);
        if (id == 4) {
            throw new IllegalArgumentException("IllegalArgumentException,非法参数异常。");
        } else if (null == result.getData()) {
            throw new NullPointerException("NullPointerException,空指针异常");
        }
        return result;
    }

    //本例是fallback: 出现运行时异常，调用回调函数fallback
    public CommonResult<Payment> handlerFallback(@PathVariable Long id, Throwable e) {
        Payment payment = new Payment(id, "null");
        return new CommonResult<>(444, "服务降级回调方法，exception内容：" + e.getMessage());
    }

    //本例是blockHandler
    public CommonResult<Payment> blockHandler(@PathVariable Long id, BlockException e) {
        Payment payment = new Payment(id, "null");
        return new CommonResult<>(445, "sentinel限流，exception内容：" + e.getMessage());
    }
}
