package com.alibb.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.RoleParam;
import com.alibb.system.entity.Role;
import com.alibb.system.entity.User;
import com.alibb.system.entity.UserRole;
import com.alibb.system.exception.Asserts;
import com.alibb.system.mapper.RoleMapper;
import com.alibb.system.mapper.UserMapper;
import com.alibb.system.mapper.UserRoleMapper;
import com.alibb.system.service.RoleService;
import com.alibb.system.utils.UserHolder;
import com.alibb.system.vo.UserRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Role> getRoleList() {
        List<Role> roleList = roleMapper.selectList(null);
        return roleList;
    }

    @Override
    public Integer updateUserRole(Long userId, Integer roleId) {
        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(UserRole::getRoleId,roleId).eq(UserRole::getUserId, userId);
        return userRoleMapper.update(null,updateWrapper);
    }

    @Override
    public Map<String, Object> getUserRoleList(PageParam pageParam) {
        HashMap<String, Object> map = new HashMap<>();
        List<UserRole> userRoleList = null;
        if (!ObjectUtil.isEmpty(pageParam.getCurrent()) && !ObjectUtil .isEmpty(pageParam.getSize())) {
            Page<UserRole> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
            Page<UserRole> userRolePage = userRoleMapper.selectPage(page, null);
            userRoleList =userRolePage.getRecords();
            map.put("total", userRolePage.getTotal());
        } else {
            userRoleList = userRoleMapper.selectList(null);
            map.put("total", userRoleList.size());
        }
        List<UserRoleVo> userRoleVoList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            UserRoleVo userRoleVo = new UserRoleVo();
            User user = userMapper.selectById(userRole.getUserId());
            if (ObjectUtil.isEmpty(user)) {
                continue;
            }
            userRoleVo.setUserId(user.getId());
            userRoleVo.setUserName(user.getUserName());
            userRoleVo.setNickName(user.getNickName());
            Role role = roleMapper.selectById(userRole.getRoleId());
            if (ObjectUtil.isEmpty(role)) {
                userRoleVo.setRoleName("角色已删除");
            } else {
                userRoleVo.setRoleId(role.getId());
                userRoleVo.setRoleName(role.getName());
            }
            userRoleVoList.add(userRoleVo);
        }
        map.put("userRoleList", userRoleVoList);
        return map;
    }

    /*@Override
    public Integer addRole(RoleParam roleParam) {
        Role role = new Role();
        BeanUtil.copyProperties(roleParam, role);
        User user = UserHolder.getUser();
        if (ObjectUtil.isEmpty(user)) {
            Asserts.fail(ReturnCode.RC302);
        }
        role.setCreateBy(user.getId());
        role.setCreateTime(new Date());
        return roleMapper.insert(role);
    }*/

    @Override
    public Integer updateRole(Integer roleId, RoleParam roleParam) {
        Role role = new Role();
        BeanUtil.copyProperties(roleParam, role);
        User user = UserHolder.getUser();
        if (ObjectUtil.isEmpty(user)) {
            Asserts.fail(ReturnCode.RC302);
        }
        role.setUpdateBy(user.getId());
        role.setUpdateTime(new Date());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, roleId);
        return roleMapper.update(role,queryWrapper);
    }
}
