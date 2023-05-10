package com.alibb.system.controller;

import com.alibb.system.component.SampleCallback;
import com.alibb.system.component.ServerEncoder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws", encoders = {ServerEncoder.class})
@Component
public class WebSocketServer {

    private static final String broker = "tcp://broker-cn.emqx.io:1883";
    private static final String topic = "/hgfsmarthome/pub";
    private static final Integer qos = 2;
    private static final String username = "emqx_user";
    private static final String password = "emqx_password";

    private MqttClient client = null;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    // 记录当前在线连接数
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        logger.info("有连接加入，当前连接数为：{}", cnt);
        //SendMessage(session, "连接成功");
        sub();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        logger.info("有连接关闭，当前连接数为：{}", cnt);
        try {
            this.client.disconnect();
            this.client.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /**
     * 出现错误
     * @param session
     * @param e
     */
    @OnError
    public void onError(Session session, Throwable e) {
        logger.error("发生错误：{}，Session ID： {}", e.getMessage(), session.getId());
        try {
            this.client.disconnect();
            this.client.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端的消息：{}",message);
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            // session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void SendMessage(Session session, Object message) {
        try {
            // session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            logger.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfoByStr(String message) throws IOException {
        for (Session session : SessionSet) {
            if(session.isOpen()){
                SendMessage(session, message);
            }
        }
    }
    public static void BroadCastInfoByObj(Object message) throws IOException {
        for (Session session : SessionSet) {
            if(session.isOpen()){
                SendMessage(session, message);
            }
        }
    }

    public void sub() {
        String clientId = MqttClient.generateClientId();
        //  持久化
        MemoryPersistence persistence = new MemoryPersistence();
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 设置认证信息
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());

        logger.info("qos: {}", qos);

        try {
            this.client = new MqttClient(broker, clientId, persistence);
            // 设置回调
            client.setCallback(new SampleCallback());
            // 建立连接
            logger.info("Connecting to broker: {}", broker);
            client.connect(connOpts);
            logger.info("Connected to broker: {}", broker);
            // 订阅 topic
            client.subscribe(topic, qos);
            logger.info("Subscribed to topic: {}", topic);
        } catch (MqttException me) {
            logger.error("reason：{}", me.getReasonCode());
            logger.error("msg：{}", me.getMessage());
            me.printStackTrace();
        }
    }

}
