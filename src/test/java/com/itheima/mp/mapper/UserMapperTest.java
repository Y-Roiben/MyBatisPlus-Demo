package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
//        user.setId(5L);
        user.setUsername("tt");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(2000);
        user.setInfo("{\"age\": 24, \"intro\": \"日文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testQueryByIds1() {
        List<User> users = userMapper.queryUserByIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }

    @Test
    void testQueryWrapper(){

        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 800);
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByWrapper(){
//        更新数据
        User user = new User();
        user.setBalance(2000);
//      更新条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("username", "jack");
//        执行更新
        userMapper.update(user, wrapper);
    }

    @Test
    void testUpdateWrapper(){
        List<Long> ids = List.of(1L,2L,4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance-200")
                .in("id", ids);
//        执行更新
        userMapper.update(null, wrapper);
    }


    @Test
    void testLambdaQueryWrapper(){
//        lambda表达式构建条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 800);
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testCustomSqlUpdate(){
//        更新条件
        int amount = 200;
        List<Long> ids = List.of(1L, 2L, 4L);
//        定义条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>().in(User::getId, ids);

//        调用自定义sql方法
        userMapper.updateBalanceByIds(wrapper,amount);

    }
}