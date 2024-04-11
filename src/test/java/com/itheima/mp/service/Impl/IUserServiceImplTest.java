package com.itheima.mp.service.Impl;

import com.itheima.mp.domain.po.User;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IUserServiceImplTest {

    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser(){
        User user = new User();
//        user.setId(5L);
        user.setUsername("zhang3");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
    }

    @Test
    void testQuery(){
        List<User> users = userService.listByIds(List.of(1L, 2L, 5L));
        users.forEach(System.out::println);

    }


    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));  // 耗时：406003
    }

    @Test
    void testSaveBatch() {
        // 我们每次批量插入1000条件，插入100次即10万条数据
        //        耗时：6066
        // 1.准备一个容量为1000的集合
        List<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            // 2.添加一个user
            list.add(buildUser(i));
            // 3.每1000条批量插入一次
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                // 4.清空集合，准备下一批数据
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }


}