package com.cjh.websocket.util;

import com.cjh.websocket.socket.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebsocketMapUtil {

    public static Map<String, Object> serverChannelMap = new ConcurrentHashMap<>();

    public enum channelEnum {
        SERVER_CHANNEL_
    }

    public static boolean inServerChannel(String id) {
        return serverChannelMap.containsKey(new StringBuffer().append(channelEnum.SERVER_CHANNEL_).append(id).toString());
    }

    public static void putServerChannel(User user) {
        log.info("serverChannelMap.put:" + new StringBuffer().append(channelEnum.SERVER_CHANNEL_).append(user.getId()).toString());
        serverChannelMap.put(new StringBuffer().append(channelEnum.SERVER_CHANNEL_).append(user.getId()).toString(), user);
    }

    public static void removeServerChannel(User user) {
        log.info("serverChannelMap.remove:" + new StringBuffer().append(channelEnum.SERVER_CHANNEL_).append(user.getId()).toString());
        serverChannelMap.remove(new StringBuffer().append(channelEnum.SERVER_CHANNEL_).append(user.getId()).toString(), user);
    }
}
