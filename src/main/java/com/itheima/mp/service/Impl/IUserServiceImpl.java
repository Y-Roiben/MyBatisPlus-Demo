package com.itheima.mp.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.enums.UserStatus;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public void deductionBalanceById(Long id, Integer money) {
//        查询用户
        User user = getById(id);
//        校验用户状态
        if (user == null || user.getStatus() == UserStatus.FREEZE){
            throw new RuntimeException("用户状态异常");
        }
//        检查是否余额是否充足
        if (user.getBalance() < money){
            throw new RuntimeException("用户余额异常");
        }
        int remainBalance = user.getBalance() - money;
//        扣减余额 扣完改变状态
//        baseMapper.deductionBalance(id, money);
        lambdaUpdate().set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, UserStatus.FREEZE)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) // 防止并发
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer maxBalance, Integer minBalance) {
        List<User> users = lambdaQuery().like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();


        return users;
    }

    @Override
    public UserVO queryUserAndAddress(Long id) {
//        查询用户
        User user = getById(id);
        if (user==null || user.getStatus() == UserStatus.FREEZE){
            throw new RuntimeException("用户状态异常");
        }
//        查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        
        if (!addresses.isEmpty()){
            List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddresses(addressVOS);
        }
        return userVO;

    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        List<User> users = listByIds(ids);
        if (users.isEmpty()){
            return null;
        }
//        获取用户Id集合
        List<Long> idCollect = users.stream().map(User::getId).collect(Collectors.toList());

        List<Address> addresses = Db.lambdaQuery(Address.class).in(Address::getUserId, idCollect).list();

        List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);

        Map<Long, List<AddressVO>> addressMap = new HashMap<>();

        // 地址分组
        if (!addressVOS.isEmpty()){
            addressMap = addressVOS.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        List<UserVO> userVOS = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVO.setAddresses(addressMap.get(user.getId()));
            userVOS.add(userVO);
        }

        return userVOS;
    }
}
