package com.cjh.websocket.config;

import com.cjh.websocket.socket.vo.BaseSubPubBean;
import com.cjh.websocket.socket.vo.User;
import com.cjh.websocket.util.SerializationUtil;
import com.cjh.websocket.util.WebsocketMapUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import java.util.Random;


@Slf4j
@Component
@AllArgsConstructor
public class Subscriber extends JedisPubSub {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private static final String DESTINATION = "/message";

    @Override
    //监听到消息进行处理
    public void onMessage(String channel, String messageJson) {
        BaseSubPubBean baseSubPubBean = null;
        try {
            baseSubPubBean = (BaseSubPubBean) SerializationUtil.deserializeToObject(messageJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (baseSubPubBean == null) {
            return;
        }
        switch (baseSubPubBean.getType()) {
            case LOGIN:
                String message = (String) baseSubPubBean.getData();
                if (WebsocketMapUtil.inServerChannel(baseSubPubBean.getId())) {
                    simpMessageSendingOperations.convertAndSendToUser(
                            baseSubPubBean.getId(),
                            DESTINATION,
                            message);
                }
                break;
            default:
                break;
        }
        System.out.println("信息发送成功");
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}
