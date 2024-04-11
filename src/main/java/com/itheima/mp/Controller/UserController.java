package com.itheima.mp.Controller;


import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口")
@RequiredArgsConstructor
public class UserController {

//    @Autowired
    private final IUserService userService;

    @ApiOperation("新增用户")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO){
        User user = new User();
        BeanUtils.copyProperties(userFormDTO, user);
        userService.save(user);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id){
        userService.removeById(id);

    }

    @ApiOperation("根据id查询用户")
    @GetMapping("/{id}")
    public UserVO getUserById(@PathVariable Long id){
        User user = userService.getById(id);
        UserVO userVO = userService.queryUserAndAddress(id);

        UserVO userVO1 = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @ApiOperation("根据id批量查询用户")
    @GetMapping
    public List<UserVO> getUserByIds(@RequestParam("ids") List<Long> ids){

        return userService.queryUserAndAddressByIds(ids);
    }


    @PutMapping("/{id}/deduction/{money}")
    @ApiOperation("扣减用户余额")
    public void deductionBalanceById(@PathVariable Long id,
                                   @PathVariable Integer money){
        userService.deductionBalanceById(id, money);

    }

    @GetMapping("/list")
    @ApiOperation("复杂条件查询")
    public List<UserVO> queryUser(UserQuery userQuery){
        List<User> users = userService.queryUsers(userQuery.getName(), userQuery.getStatus(),
                userQuery.getMaxBalance(), userQuery.getMinBalance());
        return BeanUtil.copyToList(users, UserVO.class);
    }

}
