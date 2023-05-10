package com.alibb.system.controller;

import com.alibb.system.component.Result;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.RoleParam;
import com.alibb.system.entity.Role;
import com.alibb.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping
    public Result getRoleList() {
        List<Role> roleList = roleService.getRoleList();
        return Result.success(roleList);
    }

    @PreAuthorize("hasAuthority('userRole:update')")
    @PutMapping
    public Result updateUserRole(@RequestParam("uid") Long userId, @RequestParam("rid") Integer roleId) {
        Integer i = roleService.updateUserRole(userId, roleId);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC701);
    }

    @PreAuthorize("hasAuthority('userRole:list')")
    @GetMapping("/user")
    public Result getUserRoleList(@Valid PageParam pageParam) {
        Map<String,Object> map = roleService.getUserRoleList(pageParam);
        return Result.success(map);
    }

    /*@PostMapping
    public Result addRole(@Valid @RequestBody RoleParam roleParam) {
        Integer i = roleService.addRole(roleParam);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC702);
    }*/

    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/{id}")
    public Result updateRole(@PathVariable("id") Integer roleId, @Valid @RequestBody RoleParam roleParam) {
        Integer i = roleService.updateRole(roleId, roleParam);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC703);
    }

}
