package com.alibb.system.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeviceParam {

    @NotEmpty
    private String deviceId; // 设备唯一标识
    private String deviceName; // 设备名称
    @NotEmpty
    private String location; // 设备所在地点
    private Double temLimit; // 温度阈值
    private Double humLimit; // 湿度阈值
    private Double smokeLimit; // 烟感阈值

}
