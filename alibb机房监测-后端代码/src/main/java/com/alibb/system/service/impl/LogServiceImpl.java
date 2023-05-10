package com.alibb.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibb.system.component.Result;
import com.alibb.system.dto.PageParam;
import com.alibb.system.entity.Device;
import com.alibb.system.entity.Log;
import com.alibb.system.mapper.DeviceMapper;
import com.alibb.system.mapper.LogMapper;
import com.alibb.system.service.LogService;
import com.alibb.system.vo.LogVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Map<String, Object> getLogVoList(PageParam pageParam) {
        HashMap<String, Object> map = new HashMap<>();
        List<Log> logList = null;
        LambdaQueryWrapper<Log> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Log::getCreateTime);
        if (!ObjectUtil.isEmpty(pageParam.getCurrent()) && !ObjectUtil .isEmpty(pageParam.getSize())) {
            Page<Log> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
            Page<Log> logPage = logMapper.selectPage(page, queryWrapper);
            logList = logPage.getRecords();
            map.put("total", logPage.getTotal());
        } else {
            logList = logMapper.selectList(queryWrapper);
            map.put("total", logList.size());
        }

        ArrayList<LogVo> logVoList = new ArrayList<>();
        for (Log log : logList) {
            LogVo logVo = new LogVo();
            BeanUtil.copyProperties(log,logVo);
            Device device = deviceMapper.selectById(log.getDeviceId());
            if (!ObjectUtil.isEmpty(device)) {
                logVo.setDeviceId(device.getDeviceId());
                logVo.setDeviceName(device.getDeviceName());
                logVo.setLocation(device.getLocation());
            }
            logVo.setCreateTime(DateUtil.format(log.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            logVoList.add(logVo);
        }
        map.put("logList", logVoList);
        return map;
    }

    @Override
    public Long getLogCount() {
        return logMapper.selectCount(null);
    }
}
