package com.alibb.system.controller;

import com.alibb.system.component.Result;
import com.alibb.system.dto.PageParam;
import com.alibb.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @PreAuthorize("hasAuthority('log:list')")
    @GetMapping
    public Result getLogVoList(@Valid PageParam pageParam) {
        Map<String, Object> map = logService.getLogVoList(pageParam);
        return Result.success(map);
    }

    @PreAuthorize("hasAuthority('log:count')")
    @GetMapping("/count")
    public Result getLogCount() {
        Long count = logService.getLogCount();
        return Result.success(count);
    }

}
