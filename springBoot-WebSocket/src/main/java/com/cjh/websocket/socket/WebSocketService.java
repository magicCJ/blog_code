package com.cjh.websocket.socket;

import com.cjh.websocket.socket.vo.BaseSubPubBean;
import com.cjh.websocket.socket.vo.User;
import com.cjh.websocket.util.SerializationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
@AllArgsConstructor
public class WebSocketService {

    private final JedisPool jedispool;

    public void publishData(Object obj, String userId) {
        try (Jedis jedis = jedispool.getResource()) {
            BaseSubPubBean baseSubPubBean = new BaseSubPubBean(userId, BaseSubPubBean.Type.LOGIN, obj);
            try {
                jedis.publish(SubThread.serverChannel, SerializationUtil.serializeToString(baseSubPubBean));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
