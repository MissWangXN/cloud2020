package com.atguigu.springcloud.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class PaymentHystrixService {
    @Resource
    private RestTemplate restTemplate;

    private static String URL = "http://cloud-provider-hystrix-payment";

    public String paymentInfo_Ok(Integer id) {
        return restTemplate.getForObject(URL + "/payment/hystrix/ok/" + id, String.class);
    }

    public String paymentInfo_TimeOut(Integer id) {
        return restTemplate.getForObject(URL + "/payment/hystrix/timeout/" + id, String.class);
    }
}
