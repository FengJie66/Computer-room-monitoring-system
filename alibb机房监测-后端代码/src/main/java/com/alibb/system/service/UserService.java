package com.alibb.system.service;

import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.UserParam;
import com.alibb.system.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String,Object> getUserVoPageList(PageParam pageParam);

    List<User> getUserListByRole(Integer roleId);

    Integer deleteUserById(Long id);

    Integer updateUserById(Long id, UserParam userParam);

    Integer updateUserStatus(Long id, Character status);

    Long getDeviceCount();

    UserParam getUserById(Long id);
}
