package com.alibb.system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Device {

    private Long id;
    private Long userId; // 设备创建人员
    private String deviceId; // 设备唯一标识
    private String deviceName; // 设备名称
    private Double temLimit; // 温度阈值
    private Double humLimit; // 湿度阈值
    private Double smokeLimit; // 烟感阈值
    private Integer status; // 设备状态 0-停用 1-正常
    private String location; // 设备所在地点
    private Date createTime; // 设备创建时间

}
