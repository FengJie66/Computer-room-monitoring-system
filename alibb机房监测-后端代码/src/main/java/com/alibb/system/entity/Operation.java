package com.alibb.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Operation {

    @TableId
    private Long id;
    private String operationName;
    /**
     * 操作状态（0正常 1停用）
     */
    private String status;
    /**
     * 操作标识
     */
    private String perms;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    /**
     * 是否删除（0未删除 1已删除）
     */
    private Integer delFlag;
    /**
     * 备注
     */
    private String remark;
}
