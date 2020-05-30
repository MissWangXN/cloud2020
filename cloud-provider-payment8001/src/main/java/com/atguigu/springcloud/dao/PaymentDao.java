package com.atguigu.springcloud.dao;


import com.atguigu.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

//@Repository  插入操作可能会有问题
@Mapper
public interface PaymentDao {
    //读写
    public int create(Payment payment);
    public Payment getPaymentById(@Param("id") Long id);
}
