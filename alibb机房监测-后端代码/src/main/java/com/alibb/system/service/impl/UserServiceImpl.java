package com.alibb.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.UserParam;
import com.alibb.system.entity.User;
import com.alibb.system.exception.Asserts;
import com.alibb.system.mapper.UserMapper;
import com.alibb.system.service.UserService;
import com.alibb.system.utils.UserHolder;
import com.alibb.system.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String,Object> getUserVoPageList(PageParam pageParam) {
        HashMap<String, Object> map = new HashMap<>();
        List<User> userList = null;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(User::getCreateTime);
        if (!StrUtil.isEmpty(pageParam.getKeyWord())) {
            queryWrapper.like(User::getUserName,pageParam.getKeyWord()).or()
                    .like(User::getNickName,pageParam.getKeyWord());
        }
        if (!ObjectUtil.isEmpty(pageParam.getCurrent()) && !ObjectUtil .isEmpty(pageParam.getSize())) {
            Page<User> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
            Page<User> userPage = userMapper.selectPage(page, queryWrapper);
            userList = userPage.getRecords();
            map.put("total", userPage.getTotal());
        } else {
            userList = userMapper.selectList(queryWrapper);
            map.put("total", userList.size());
        }
        ArrayList<UserVo> userVoList = new ArrayList<>();
        for(User user : userList) {
            userVoList.add(copy(user));
        }
        map.put("userList", userVoList);
        return map;
    }

    @Override
    public List<User> getUserListByRole(Integer roleId) {
        List<User> userList = userMapper.getUserListByRole(roleId);
        return userList;
    }

    @Override
    public Integer deleteUserById(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public Integer updateUserById(Long id, UserParam userParam) {
        User user = new User();
        BeanUtil.copyProperties(userParam, user);
        User loginUser = UserHolder.getUser();
        if (ObjectUtil.isEmpty(loginUser)) {
            Asserts.fail(ReturnCode.RC302);
        }
        user.setUpdateBy(loginUser.getId());
        user.setUpdateTime(new Date());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        return userMapper.update(user, queryWrapper);
    }

    @Override
    public Integer updateUserStatus(Long id, Character status) {
        if (status == '0' || status == '1') {
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(User::getStatus, status).eq(User::getId, id);
            return userMapper.update(null, updateWrapper);
        }
        return 0;
    }

    @Override
    public Long getDeviceCount() {
        return userMapper.selectCount(null);
    }

    @Override
    public UserParam getUserById(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getNickName, User::getEmail, User::getPhone, User::getSex)
                .eq(User::getId, id);
        User user = userMapper.selectOne(queryWrapper);
        UserParam userParam = new UserParam();
        BeanUtil.copyProperties(user, userParam);
        return userParam;
    }

    public UserVo copy(User user) {
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user, userVo);
        if (user.getSex() != null) {
            switch (user.getSex()) {
                case '0' : userVo.setSex("保密");break;
                case '1' : userVo.setSex("男");break;
                case '2' : userVo.setSex("女");break;
            }
        } else {
            userVo.setSex("保密");
        }
        if (user.getCreateBy() != null) {
            User createUser = userMapper.selectById(user.getCreateBy());
            userVo.setCreateBy(createUser.getUserName());
        }
        if (user.getUpdateBy() != null) {
            User updateUser = userMapper.selectById(user.getUpdateBy());
            userVo.setCreateBy(updateUser.getUserName());
        }
        userVo.setCreateTime(DateUtil.format(user.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        userVo.setUpdateTime(DateUtil.format(user.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
        return userVo;
    }
}
