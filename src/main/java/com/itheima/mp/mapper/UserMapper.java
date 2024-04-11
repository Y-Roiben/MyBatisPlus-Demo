package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.yaml.snakeyaml.scanner.Constant;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {


    List<User> queryUserByIds(List<Long> ids);

    void updateBalanceByIds(@Param(Constants.WRAPPER) LambdaQueryWrapper<User> wrapper, @Param("amount") int amount);

    @Update("update tb_user set balance = balance - #{money} where id = #{id}")
    void deductionBalance(@Param("id") Long id, @Param("money") Integer money);
}
