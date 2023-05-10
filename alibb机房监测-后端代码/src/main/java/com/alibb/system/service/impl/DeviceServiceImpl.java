package com.alibb.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibb.system.constant.ReturnCode;
import com.alibb.system.dto.DeviceParam;
import com.alibb.system.dto.PageParam;
import com.alibb.system.entity.Device;
import com.alibb.system.entity.User;
import com.alibb.system.exception.Asserts;
import com.alibb.system.mapper.DeviceMapper;
import com.alibb.system.mapper.UserMapper;
import com.alibb.system.service.DeviceService;
import com.alibb.system.utils.UserHolder;
import com.alibb.system.vo.DeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer addDevice(DeviceParam deviceParam) {
        // 查看设备是否已经注册
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getDeviceId,deviceParam.getDeviceId());
        List<Device> deviceList = deviceMapper.selectList(queryWrapper);
        if (deviceList.size() > 0) {
            Asserts.fail(ReturnCode.RC201);
        }
        Device device = new Device();
        BeanUtil.copyProperties(deviceParam, device);
        if (ObjectUtil.isEmpty(UserHolder.getUser())) {
            Asserts.fail(ReturnCode.RC302);
        }
        device.setUserId(UserHolder.getUser().getId());
        device.setCreateTime(new Date());
        return deviceMapper.insert(device);
    }

    @Override
    public Map<String, Object> getDevicePageList(@RequestBody PageParam pageParam) {
        HashMap<String, Object> map = new HashMap<>();
        List<Device> deviceList = null;
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Device::getCreateTime);
        if (!StrUtil.isEmpty(pageParam.getKeyWord())) {
            queryWrapper.like(Device::getDeviceId,pageParam.getKeyWord()).or()
                    .like(Device::getDeviceName,pageParam.getKeyWord());
        }
        if (!ObjectUtil.isEmpty(pageParam.getCurrent()) && !ObjectUtil .isEmpty(pageParam.getSize())) {
            Page<Device> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
            Page<Device> devicePage = deviceMapper.selectPage(page, queryWrapper);
            deviceList = devicePage.getRecords();
            map.put("total", devicePage.getTotal());
        } else {
            deviceList = deviceMapper.selectList(queryWrapper);
            map.put("total", deviceList.size());
        }
        ArrayList<DeviceVo> deviceVoList = new ArrayList<>();
        for (Device device : deviceList) {
            DeviceVo deviceVo = new DeviceVo();
            BeanUtil.copyProperties(device,deviceVo);
            User user = userMapper.selectById(device.getUserId());
            if (!ObjectUtil.isEmpty(user)) {
                deviceVo.setName(user.getUserName());
            }
            deviceVo.setCreateTime(DateUtil.format(device.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            deviceVoList.add(deviceVo);
        }
        map.put("deviceList", deviceVoList);
        return map;
    }

    @Override
    public Device getDeviceById(Long id) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getId,id);
        return deviceMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateDevice(Long id, DeviceParam deviceParam) {
        Device device = new Device();
        BeanUtil.copyProperties(deviceParam,device);
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getId, id);
        return deviceMapper.update(device, queryWrapper);
    }


    @Override
    public Integer deleteDeviceById(Long id) {
        return deviceMapper.deleteById(id);
    }

    @Override
    public Integer updateDeviceStatus(Long id, Integer status) {
        if (status == 0 || status == 1) {
            LambdaUpdateWrapper<Device> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Device::getStatus, status).eq(Device::getId, id);
            return deviceMapper.update(null, updateWrapper);
        }
        return 0;
    }

    @Override
    public Long getDeviceCount() {
        return deviceMapper.selectCount(null);
    }
}
