package com.alibb.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.entity.LoginUser;
import com.alibb.system.entity.User;
import com.alibb.system.exception.Asserts;
import com.alibb.system.mapper.OperationMapper;
import com.alibb.system.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        if (ObjectUtil.isEmpty(user)) {
            Asserts.fail(ReturnCode.RC301);
        }
        List<String> permissionKeyList =  operationMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user,permissionKeyList);
    }
}
