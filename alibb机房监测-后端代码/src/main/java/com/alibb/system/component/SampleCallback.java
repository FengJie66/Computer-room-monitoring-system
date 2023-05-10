package com.alibb.system.component;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibb.system.controller.WebSocketServer;
import com.alibb.system.entity.Device;
import com.alibb.system.entity.Log;
import com.alibb.system.mapper.DeviceMapper;
import com.alibb.system.mapper.LogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

public class SampleCallback implements MqttCallback {

    public static final Logger logger = LoggerFactory.getLogger(SampleCallback.class);

    // 连接丢失
    public void connectionLost(Throwable cause) {
        logger.error("connection lost: {}", cause.getMessage());
    }

    //  收到消息
    public void messageArrived(String topic, MqttMessage message) throws IOException {

        logger.info("Received message: \n  topic：" + topic + "\n  Qos：" + message.getQos() + "\n  payload：" + new String(message.getPayload()));
        JSONObject jsonObject = JSONUtil.parseObj(new String(message.getPayload()));
        String deviceId = (String) jsonObject.get("deviceId");
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getDeviceId,deviceId);
        DeviceMapper deviceMapper = SpringUtil.getBean(DeviceMapper.class);
        Device device = deviceMapper.selectOne(queryWrapper);
        if (ObjectUtil.isEmpty(device)) {
            WebSocketServer.BroadCastInfoByStr("检测到未知设备");
            return;
        }
        if (device.getStatus() != '0') {
            WebSocketServer.BroadCastInfoByObj(jsonObject);
            Integer tem = (Integer) jsonObject.get("tem");
            Integer hum = (Integer) jsonObject.get("hum");
            Integer smoke = (Integer) jsonObject.get("smoke");

            LogMapper logMapper = SpringUtil.getBean(LogMapper.class);
            if (!ObjectUtil.isEmpty(device.getTemLimit()) && tem > device.getTemLimit()) {
                Log log = new Log();
                log.setDeviceId(device.getId());
                log.setEntityType(1);
                log.setData(tem);
                log.setDataLimit(device.getTemLimit());
                log.setCreateTime(new Date());
                logMapper.insert(log);
            }
            if (!ObjectUtil.isEmpty(device.getHumLimit()) && hum > device.getHumLimit()) {
                Log log = new Log();
                log.setDeviceId(device.getId());
                log.setEntityType(2);
                log.setData(hum);
                log.setDataLimit(device.getHumLimit());
                log.setCreateTime(new Date());
                logMapper.insert(log);
            }
            if (!ObjectUtil.isEmpty(device.getSmokeLimit()) && smoke > device.getSmokeLimit()) {
                Log log = new Log();
                log.setDeviceId(device.getId());
                log.setEntityType(3);
                log.setData(smoke);
                log.setDataLimit(device.getSmokeLimit());
                log.setCreateTime(new Date());
                logMapper.insert(log);
            }

            // System.out.println(device);
        }

        //logger.info("deviceID:{},temp:{},hum:{},smoke:{}",deviceID,temp,hum,smoke);
    }

    // 消息传递成功
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


}
