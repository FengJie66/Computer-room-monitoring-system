package com.alibb.system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WorkOrder {

    private Long id;
    private Integer type;
    private String description;
    private Integer status; // 0-进行中；1-已完成
    private Long userId;
    private Date createTime;

}
