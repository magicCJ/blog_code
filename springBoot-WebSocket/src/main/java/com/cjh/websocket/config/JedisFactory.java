package com.cjh.websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description:
 * @author: magic
 * @date: 2018/8/30 上午11:33
 */
@Component
public class JedisFactory {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;


    @Value("${spring.redis.jedis.pool.max-active}")
    private int poolMaxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int poolMaxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int poolMaxWait;

    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(poolMaxIdle);
        poolConfig.setMaxTotal(poolMaxActive);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxWaitMillis(poolMaxWait);
        JedisPool jp = new JedisPool(poolConfig, host, port,
                200, password, 0);
        return jp;
    }



}
