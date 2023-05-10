package com.alibb.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibb.system.dto.PageParam;
import com.alibb.system.dto.WorkOrderParam;
import com.alibb.system.entity.User;
import com.alibb.system.entity.WorkOrder;
import com.alibb.system.mapper.UserMapper;
import com.alibb.system.mapper.WorkOrderMapper;
import com.alibb.system.service.WorkOrderService;
import com.alibb.system.vo.WorkOrderVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer addWorkOrder(WorkOrderParam workOrderParam) {
        WorkOrder workOrder = new WorkOrder();
        BeanUtil.copyProperties(workOrderParam, workOrder);
        workOrder.setCreateTime(new Date());
        int i = workOrderMapper.insert(workOrder);
        return i;
    }

    @Override
    public Map<String, Object> getWorkOrderPageList(Long userId, PageParam pageParam) {
        HashMap<String, Object> map = new HashMap<>();
        List<WorkOrder> workOrderList = null;
        LambdaQueryWrapper<WorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(WorkOrder::getCreateTime);
        if (userId != 0) {
            queryWrapper.eq(WorkOrder::getUserId, userId);
        }
        if (!ObjectUtil.isEmpty(pageParam.getCurrent()) && !ObjectUtil.isEmpty(pageParam.getSize())) {
            Page<WorkOrder> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
            Page<WorkOrder> workOrderPage = workOrderMapper.selectPage(page, queryWrapper);
            workOrderList = workOrderPage.getRecords();
            map.put("total", workOrderPage.getTotal());
        } else {
            workOrderList = workOrderMapper.selectList(queryWrapper);
            map.put("total", workOrderList.size());
        }
        List<WorkOrderVo> workOrderVoList = new ArrayList<>();
        for (WorkOrder workOrder : workOrderList) {
            WorkOrderVo workOrderVo = new WorkOrderVo();
            BeanUtil.copyProperties(workOrder, workOrderVo);
            User user = userMapper.selectById(workOrder.getUserId());
            if (ObjectUtil.isEmpty(user)) {
                workOrderVo.setUserName("N/A");
                workOrderVo.setNickName("N/A");
            } else {
                workOrderVo.setUserName(user.getUserName());
                workOrderVo.setNickName(user.getNickName());
            }
            workOrderVo.setCreateTime(DateUtil.format(workOrder.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            workOrderVoList.add(workOrderVo);
        }
        map.put("workOrderList", workOrderVoList);
        return map;
    }

    @Override
    public Integer deleteWorkOrderById(Long id) {
        return workOrderMapper.deleteById(id);
    }

    @Override
    public Integer completeWorkOrder(Long id) {
        LambdaUpdateWrapper<WorkOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(WorkOrder::getStatus, 1)
                .eq(WorkOrder::getId,id);
        return workOrderMapper.update(null, updateWrapper);
    }

    @Override
    public Long getWorkOrderCount() {
        return workOrderMapper.selectCount(null);
    }
}
