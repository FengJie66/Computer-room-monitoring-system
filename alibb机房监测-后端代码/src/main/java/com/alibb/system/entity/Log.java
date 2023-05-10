package com.alibb.system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Log {

    private Long id;
    private Long deviceId;
    private Integer entityType; // 1-温度 2-湿度 3-烟感
    private Integer data; // 温度/湿度/烟感
    private Double dataLimit; // 当时设置的阈值
    private Date createTime;

}
