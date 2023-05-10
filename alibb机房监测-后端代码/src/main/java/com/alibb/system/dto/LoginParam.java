package com.alibb.system.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginParam {

    @NotEmpty(message = "用户名不能为空")
    @Length(min = 3, max = 64, message = "用户名长度必须位于3-64之间")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 1,max = 64,message = "密码长度必须位于1-64之间")
    private String password;
}
