package com.alibb.system.service;

import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.WorkOrderParam;

import java.util.Map;

public interface WorkOrderService {
    Integer addWorkOrder(WorkOrderParam workOrderParam);


    Map<String, Object> getWorkOrderPageList(Long userId, PageParam pageParam);

    Integer deleteWorkOrderById(Long id);

    Integer completeWorkOrder(Long id);

    Long getWorkOrderCount();

}
