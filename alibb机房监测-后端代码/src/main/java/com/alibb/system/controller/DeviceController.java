package com.alibb.system.controller;

import com.alibb.system.component.Result;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.DeviceParam;
import com.alibb.system.dto.PageParam;
import com.alibb.system.entity.Device;
import com.alibb.system.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PreAuthorize("hasAuthority('device:list')")
    @GetMapping
    public Result getDevicePageList(@Valid PageParam pageParam) {
        Map<String, Object> map = deviceService.getDevicePageList(pageParam);
        return Result.success(map);
    }

    @PreAuthorize("hasAuthority('device:one')")
    @GetMapping("/{id}")
    public Result getDeviceById(@PathVariable("id") Long id) {
        Device device = deviceService.getDeviceById(id);
        return Result.success(device);
    }

    @PreAuthorize("hasAuthority('device:create')")
    @PostMapping
    public Result addDevice(@Valid @RequestBody DeviceParam deviceParam) {
        Integer i = deviceService.addDevice(deviceParam);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC202);
    }

    @PreAuthorize("hasAuthority('device:update')")
    @PutMapping("/{id}")
    public Result updateDevice(@PathVariable("id")Long id, @Valid @RequestBody DeviceParam deviceParam) {
        Integer i = deviceService.updateDevice(id, deviceParam);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC203);
    }

    @PreAuthorize("hasAuthority('device:delete')")
    @DeleteMapping("/{id}")
    public Result deleteDeviceById(@PathVariable("id") Long id) {
        Integer i = deviceService.deleteDeviceById(id);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC204);
    }

    @PreAuthorize("hasAuthority('device:update:status')")
    @PatchMapping("/{id}")
    public Result updateDeviceStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        Integer i = deviceService.updateDeviceStatus(id, status);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC205);
    }

    @PreAuthorize("hasAuthority('device:count')")
    @GetMapping("/count")
    public Result getDeviceCount() {
        Long count = deviceService.getDeviceCount();
        return Result.success(count);
    }

}
