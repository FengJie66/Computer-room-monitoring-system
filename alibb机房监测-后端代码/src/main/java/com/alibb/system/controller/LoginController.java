package com.alibb.system.controller;

import com.alibb.system.component.Result;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.LoginParam;
import com.alibb.system.dto.RegisterParam;
import com.alibb.system.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginParam loginParam) {
        Map<String, Object> map = loginService.login(loginParam);
        return Result.success(map);
    }

    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterParam registerParam) {
        Integer i = loginService.register(registerParam);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC305);
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.success(null);
    }

}
