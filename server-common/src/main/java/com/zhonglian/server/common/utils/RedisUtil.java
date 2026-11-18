package com.zhonglian.server.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    private static String ADDR = System.getProperty("Redis.ADDR");
    private static int PORT = Integer.valueOf(System.getProperty("Redis.PORT"));
    private static String AUTH = System.getProperty("Redis.AUTH");
    private static int MAX_ACTIVE = 1024;
    private static int MAX_IDLE = 200;
    private static int MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;
    private static boolean TEST_ON_BORROW = true;
    private static JedisPool jedisPool = null;

    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWait(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    public static synchronized Jedis getJedis() {
        try {
            return jedisPool != null ? jedisPool.getResource() : null;
        } catch (Exception var1) {
            var1.printStackTrace();
            return null;
        }
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}
