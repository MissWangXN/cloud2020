package com.atguigu.springcloud.lb.impl;

import com.atguigu.springcloud.lb.IMyLoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 手写一个本地负载均衡轮询算法
 */
@Component
public class MyLoadBalanceImpl implements IMyLoadBalancer {

    //接口访问次数变量
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 实现：接口访问次数++（难点）
     * 1.使用自旋锁，并发修改变量
     * 2.该方法不允许修改（final）
     *
     * @return
     */
    public final int getAndIncrement() {
        int current; //当前接口请求次数
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next)); //CAS(期望值，修改值)
        System.out.println("第" + next + "次访问");
        return next;
    }

    /**
     * 获得要访问的服务器
     *
     * @param list
     * @return
     */
    @Override
    public ServiceInstance getServiceInstance(List<ServiceInstance> list) {

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //服务器索引
        int index = getAndIncrement() % list.size();
        return list.get(index);
    }
}
