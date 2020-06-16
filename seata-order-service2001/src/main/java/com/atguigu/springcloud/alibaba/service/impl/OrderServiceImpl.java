package com.atguigu.springcloud.alibaba.service.impl;

import com.atguigu.springcloud.alibaba.dao.OrderDao;
import com.atguigu.springcloud.alibaba.domain.Order;
import com.atguigu.springcloud.alibaba.service.OrderService;
import com.atguigu.springcloud.entities.CommonResult;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Autowired
    private RestTemplate restTemplate;

    private static String ACCOUNT_URL = "http://seata-account-service";
    private static String STORAGE_URL = "http://seata-storage-service";

    @Override
    @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class) //异常后回滚
    public void create(Order order) {

        log.info("开始新建订单");
        orderDao.create(order);

        log.info("订单微服务调用库存微服务，做扣减开始Count");
        restTemplate.getForObject(STORAGE_URL + "/storage/decrease?productId=" + order.getProductId()
                + "&count=" + order.getCount(), CommonResult.class);
        log.info("订单微服务调用库存微服务，做扣减结束");

        log.info("订单微服务调用账户微服务，做扣减Money");
        restTemplate.getForObject(ACCOUNT_URL + "/account/decrease?userId=" + order.getUserId()
                + "&money=" + order.getMoney(), CommonResult.class);
        log.info("订单微服务调用账户微服务，做扣减结束");

        //修改订单的状态 0-》1
        log.info("修改订单状态开始");
        orderDao.update(order.getUserId(), 0);
        log.info("修改订单状态结束");

        log.info("下订单结束");
    }
}
