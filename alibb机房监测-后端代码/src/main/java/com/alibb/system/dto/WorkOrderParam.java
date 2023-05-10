package com.alibb.system.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class WorkOrderParam {

    @NotNull
    @Min(1)
    private Integer type;
    @NotEmpty
    private String description;
    @NotNull
    @Min(1)
    private Long userId;

}
