package com.alibb.system.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserParam {

    @NotEmpty
    private String nickName;
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "请填写正确的邮箱地址")
    private String email;
    @Pattern(regexp = "^(?:(?:\\+|00)86)?1\\d{10}$",message = "请填写正确的手机号码")
    private String phone;
    private Character sex;

}
