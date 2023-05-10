package com.alibb.system.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {

    private Long Id;
    private String userName;
    private String nickName;
    private String email;
    private String phone;
    private String sex;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private Character status;
    private Integer delFlag;

}
