package com.atguigu.springcloud.alibaba.controller;

import com.atguigu.springcloud.alibaba.service.AccountService;
import com.atguigu.springcloud.entities.CommonResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
public class AccountController {
    @Resource
    private AccountService accountService;

    @GetMapping("/account/decrease")
    public CommonResult decrease(@Param("userId") Long userId, @Param("money") BigDecimal money) {
        accountService.decrease(userId, money);
        return new CommonResult(200, "扣减账户余额成功");
    }
}
