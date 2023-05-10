package com.alibb.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long Id;
    private String userName;
    private String nickName;
    private String password;
    private Character status;
    private String email;
    private String phone;
    private Character sex;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    @TableLogic
    private Integer delFlag;

}
