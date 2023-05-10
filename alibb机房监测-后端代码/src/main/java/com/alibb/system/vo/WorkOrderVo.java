package com.alibb.system.vo;

import lombok.Data;

@Data
public class WorkOrderVo {

    private Long id;
    private Integer type;
    private String description;
    private Integer status; // 0-进行中；1-已完成
    private String userName;
    private String nickName;
    private String createTime;

}
