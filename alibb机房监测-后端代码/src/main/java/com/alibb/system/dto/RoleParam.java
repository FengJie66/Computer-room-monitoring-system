package com.alibb.system.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RoleParam {

    @NotEmpty
    private String name;
    @NotEmpty
    private String roleKey;
    private String remark;

}
