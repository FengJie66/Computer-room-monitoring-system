package com.alibb.system.controller;

import com.alibb.system.component.Result;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.WorkOrderParam;
import com.alibb.system.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @PreAuthorize("hasAuthority('workOrder:create')")
    @PostMapping
    public Result addWorkOrder(@Valid @RequestBody WorkOrderParam workOrderParam) {
        Integer i = workOrderService.addWorkOrder(workOrderParam);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC601);
    }

    @PreAuthorize("hasAuthority('workOrder:list')")
    @GetMapping
    public Result getWorkOrderPageList(@Valid @NotNull @Min(0) Long userId,@Valid PageParam pageParam) {
        Map<String, Object> map = workOrderService.getWorkOrderPageList(userId, pageParam);
        return Result.success(map);
    }

    @PreAuthorize("hasAuthority('workOrder:delete')")
    @DeleteMapping("/{id}")
    public Result deleteWorkOrderById(@PathVariable("id") Long id) {
        Integer i  = workOrderService.deleteWorkOrderById(id);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC602);
    }

    @PreAuthorize("hasAuthority('workOrder:update:status')")
    @PatchMapping("/{id}")
    public Result completeWorkOrder(@PathVariable("id") Long id) {
        Integer i  = workOrderService.completeWorkOrder(id);
        if (i > 0) {
            return Result.success(i);
        }
        return Result.fail(ReturnCode.RC603);
    }

    @PreAuthorize("hasAuthority('workOrder:count')")
    @GetMapping("/count")
    public Result getWorkOrderCount() {
        Long count = workOrderService.getWorkOrderCount();
        return Result.success(count);
    }


}
