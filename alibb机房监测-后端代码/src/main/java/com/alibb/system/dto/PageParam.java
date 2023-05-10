package com.alibb.system.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PageParam {

    @Min(1)
    private Integer current;
    @Min(1)
    private Integer size;
    private String keyWord;

}
