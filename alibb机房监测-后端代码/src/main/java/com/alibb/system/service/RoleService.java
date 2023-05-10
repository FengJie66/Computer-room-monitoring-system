package com.alibb.system.service;

import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.RoleParam;
import com.alibb.system.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    List<Role> getRoleList();

    Integer updateUserRole(Long userId, Integer roleId);

    Map<String, Object> getUserRoleList(PageParam pageParam);

    // Integer addRole(RoleParam roleParam);

    Integer updateRole(Integer roleId, RoleParam roleParam);
}
