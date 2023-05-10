package com.alibb.system.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterParam {

    @Length(min = 3, max = 64, message = "用户名长度必须位于3-64之间")
    private String userName;
    private String nickName;
    @NotEmpty
    @Length(max = 64,message = "密码长度必须位于1-64之间")
    private String password;
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "请填写正确的邮箱地址")
    private String email;
    @Pattern(regexp = "^(?:(?:\\+|00)86)?1\\d{10}$",message = "请填写正确的手机号码")
    private String phone;
    private Character sex;
    @NotNull
    private Integer roleId;

}
