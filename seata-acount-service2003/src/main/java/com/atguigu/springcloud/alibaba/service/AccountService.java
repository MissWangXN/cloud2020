package com.atguigu.springcloud.alibaba.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Service
public interface AccountService {
    void decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
