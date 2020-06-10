package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource //服务的发现（获取注册在eureka上的当前服务的信息）
    private EurekaDiscoveryClient discoveryClient;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        if (result > 0) {
            return new CommonResult(200, "插入数据库成功，serverPort: " + serverPort, result);
        } else return new CommonResult(444, "插入失败", null);
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        if (null != payment) {
            return new CommonResult(200, "查询成功,serverPort: " + serverPort, payment);
        } else return new CommonResult(444, "没有对应的记录，查询id" + id, null);
    }

    @GetMapping("/payment/discovery")
    public Object discovery() {

        //获取eureka注册中心的已注册微服务的名称列表
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("serviceName:" + service);
        }
        //获取某个微服务的实例列表（集群的话有多个实例）
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            //获取eureka注册中心某个微服务下某个实例的具体信息（实例名称列表，每个实例的Id地址，端口号，uri）
            log.info("serviceId:" + instance.getServiceId()
                    + "\t" + "host:" + instance.getHost()//主机名称
                    + "\t" + "instanceId" + instance.getInstanceId()
                    + "\t" + "port:" + instance.getPort()
                    + "\t" + "uri:" + instance.getUri());
        }
        return this.discoveryClient;
    }

    /**
     * 手写轮询负载均衡算法
     * 1.返回当前服务的接口
     *
     * @return
     */
    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    /**
     * spring cloud sleuth全链路监控
     * @return
     */
    @GetMapping("/payment/zipkin")
    public String paymentZipkin() {
        return "zipkin";
    }
}
