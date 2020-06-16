package com.atguigu.springcloud.alibaba.controller;

import com.atguigu.springcloud.alibaba.service.StorageService;
import com.atguigu.springcloud.entities.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RestController
public class StorageController {
    @Resource
    private StorageService storageService;

    @GetMapping("/storage/decrease")
    public CommonResult decrease(@PathParam("productId") Long productId, @PathParam("count") Integer count) {
        storageService.decrease(productId, count);
        return new CommonResult(200, "扣减库存成功");
    }
}
