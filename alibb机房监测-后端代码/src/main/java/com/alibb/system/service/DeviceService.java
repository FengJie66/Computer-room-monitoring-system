package com.alibb.system.service;

import com.alibb.system.dto.DeviceParam;
import com.alibb.system.dto.PageParam;
import com.alibb.system.entity.Device;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface DeviceService {

    Integer addDevice(DeviceParam deviceParam);

    Map<String, Object> getDevicePageList(@RequestBody PageParam pageParam);

    Device getDeviceById(Long id);

    Integer updateDevice(Long id, DeviceParam deviceParam);

    Integer deleteDeviceById(Long id);

    Integer updateDeviceStatus(Long id, Integer status);

    Long getDeviceCount();

}
