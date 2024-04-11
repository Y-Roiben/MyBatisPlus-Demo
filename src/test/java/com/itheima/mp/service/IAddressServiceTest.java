package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IAddressServiceTest {

    @Autowired
    private IAddressService iAddressService;

    @Test
    void test(){
        iAddressService.removeById(59L);
        Address address = iAddressService.getById(59L);
        System.out.println(address);
    }

}