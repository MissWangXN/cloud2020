package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;


public interface IMyLoadBalancer {

    //从服务实例列表中筛选出此次rest请求将要访问的服务实例
    //也就是负载均衡算法
    ServiceInstance getServiceInstance(List<ServiceInstance> list);
}
