package com.alibb.system.vo;

import lombok.Data;

@Data
public class LogVo {

    private String deviceId;
    private String deviceName;
    private Integer entityType;
    private Integer data;
    private Double dataLimit;
    private String location;
    private String createTime;

}
