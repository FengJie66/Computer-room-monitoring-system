package com.alibb.system.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ServerEncoder implements Encoder.Text<JSONObject>{

    @Override
    public String encode(JSONObject jsonObject) throws EncodeException {
        Object data = jsonObject.toBean(Object.class);
        return JSONUtil.toJsonStr(data);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
