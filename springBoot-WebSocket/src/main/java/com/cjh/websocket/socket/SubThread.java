package com.cjh.websocket.socket;

import com.cjh.websocket.config.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@Slf4j
@Component
public class SubThread extends Thread {

    @Resource
    protected JedisPool jedisPool;

    @Autowired
    Subscriber subscriber;

    final public static String serverChannel = "server_channel";

    @Override
    public void run() {
        log.info(String.format("subscribe redis, channel %s, thread will be blocked", serverChannel));
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.subscribe(subscriber, serverChannel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
