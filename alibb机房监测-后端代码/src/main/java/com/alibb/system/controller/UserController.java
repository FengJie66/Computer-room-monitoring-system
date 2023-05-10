package com.alibb.system.controller;

import com.alibb.system.component.Result;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.UserParam;
import com.alibb.system.entity.User;
import com.alibb.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('user:one')")
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") Long id) {
        UserParam user = userService.getUserById(id);
        return Result.success(user);
    }

    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping
    public Result getUserVoPageList(@Valid PageParam pageParam) {
        Map<String, Object> map = userService.getUserVoPageList(pageParam);
        return Result.success(map);
    }

    @PreAuthorize("hasAuthority('user:listByRole')")
    @GetMapping("/role/{roleId}")
    public Result getUserListByRole(@PathVariable("roleId") Integer roleId) {
        List<User> userList = userService.getUserListByRole(roleId);
        return Result.success(userList);
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public Result deleteUserById(@PathVariable("id") Long id) {
        Integer i = userService.deleteUserById(id);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC306);
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PutMapping("/{id}")
    public Result updateUserById(@PathVariable("id") Long id, @Valid @RequestBody UserParam userParam) {
        Integer i = userService.updateUserById(id, userParam);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC307);
    }

    @PreAuthorize("hasAuthority('user:update:status')")
    @PatchMapping("/{id}")
    public Result updateUserStatus(@PathVariable("id") Long id, @RequestParam("status") Character status) {
        Integer i = userService.updateUserStatus(id, status);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC308);
    }

    @PreAuthorize("hasAuthority('user:count')")
    @GetMapping("/count")
    public Result getUserCount() {
        Long count = userService.getDeviceCount();
        return Result.success(count);
    }

}
