package com.atguigu.springcloud.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.IMyLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

    @Resource
    private IMyLoadBalancer loadBalancer;

    @Resource
    private EurekaDiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

//    private static final String PAYMENT_URL = "http://localhost:8001"; //单一支付服务

    //搭建支付服务的集群后，访问订单服务集群时，通过订单微服务在eureka注册的微服务名称进行调用
    public static final String PAYMENT_URL = "http://cloud-payment-service"; //支付服务的集群


    @PostMapping("/consumer/payment/create")
    public CommonResult<Integer> create(Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    //记住路径传参写法（参数是路径的一部分）
    @GetMapping("/consumer/payment/get/{id}") //uri中参数前面是"/",参数用大括号
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) { //方法入参前加注解@PathVariable表示路径传参
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);//rest风格调用服务时，参数放在url中
    }

    @GetMapping("/consumer/payment/getForEntity/{id}") //uri中参数前面是"/",参数用大括号
    public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id) { //方法入参前加注解@PathVariable表示路径传参

        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);//rest风格调用服务时，参数放在url中
        if (forEntity.getStatusCode().is2xxSuccessful()) return forEntity.getBody();
        else return new CommonResult<>(444, "操作失败");
    }

    /**
     * 调用本地自开发负载均衡轮询算法选择rest请求访问的服务实例
     * @return
     */
    @GetMapping("/consumer/payment/lb")
    public String getPaymentLB() {
        List<ServiceInstance> list = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (CollectionUtils.isEmpty(list)) return null;

        ServiceInstance serviceInstance = loadBalancer.getServiceInstance(list);

        String uri = serviceInstance.getUri().toString();
        String port = restTemplate.getForObject(uri + "/payment/lb", String.class);
        System.out.println(port);
        return port;
    }
}
