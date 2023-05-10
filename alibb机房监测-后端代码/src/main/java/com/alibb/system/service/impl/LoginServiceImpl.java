package com.alibb.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibb.system.component.Result;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.LoginParam;
import com.alibb.system.dto.RegisterParam;
import com.alibb.system.entity.LoginUser;
import com.alibb.system.entity.User;
import com.alibb.system.entity.UserRole;
import com.alibb.system.exception.Asserts;
import com.alibb.system.mapper.RoleMapper;
import com.alibb.system.mapper.UserMapper;
import com.alibb.system.mapper.UserRoleMapper;
import com.alibb.system.service.LoginService;
import com.alibb.system.service.UserService;
import com.alibb.system.utils.JwtTokenUtil;
import com.alibb.system.utils.UserHolder;
import com.alibb.system.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Map<String, Object> login(LoginParam loginParam) {
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(loginParam.getUserName());
        if (!passwordEncoder.matches(loginParam.getPassword(), loginUser.getPassword())) {
            Asserts.fail(ReturnCode.RC301);
        }
        if (!loginUser.isEnabled()) {
            Asserts.fail(ReturnCode.RC309);
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateToken(loginUser);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("userName", loginUser.getUsername());
        map.put("nickName", loginUser.getUser().getNickName());
        return map;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Integer register(RegisterParam registerParam) {
        // 查询是否有相同用户名的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,registerParam.getUserName());
        List<User> userList = userMapper.selectList(queryWrapper);
        if (userList.size() > 0) {
            Asserts.fail(ReturnCode.RC304);
        }
        User user = new User();
        BeanUtil.copyProperties(registerParam, user);
        user.setPassword(passwordEncoder.encode(registerParam.getPassword()));
        if (StrUtil.isEmpty(registerParam.getNickName())) {
            user.setNickName(RandomUtil.randomString(6));
        }
        if (ObjectUtil.isEmpty(UserHolder.getUser())) {
            Asserts.fail(ReturnCode.RC302);
        }
        user.setCreateBy(UserHolder.getUser().getId());
        user.setCreateTime(new Date());
        if (ObjectUtil.isEmpty(registerParam.getSex())) {
            user.setSex('0');
        }
        int i = userMapper.insert(user);
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(registerParam.getRoleId());
        userRoleMapper.insert(userRole);
        //roleMapper.addUserRole(user.getId(),registerParam.getRoleId());
        return i;
    }
}
