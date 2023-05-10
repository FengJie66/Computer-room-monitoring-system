package com.alibb.system.vo;

import lombok.Data;

@Data
public class DeviceVo {

    private Long id;
    private String name; // 设备创建人员
    private String deviceId; // 设备唯一标识
    private String deviceName; // 设备名称
    private Integer status; // 设备状态 0-停用 1-正常
    private String location; // 设备所在地点
    private String createTime; // 设备创建时间
    private Double temLimit; // 温度
    private Double humLimit; // 湿度
    private Double smokeLimit; // 烟感

}
