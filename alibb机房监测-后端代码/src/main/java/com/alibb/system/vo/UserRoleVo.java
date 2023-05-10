package com.alibb.system.vo;

import lombok.Data;

@Data
public class UserRoleVo {

    private Long userId;
    private Integer roleId;
    private String userName;
    private String nickName;
    private String roleName;

}
