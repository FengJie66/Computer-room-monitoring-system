package com.alibb.system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Role {

    private Integer id;
    private String name;
    private String roleKey;
    private Character status;
    private Integer delFlag;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String remark;

}
