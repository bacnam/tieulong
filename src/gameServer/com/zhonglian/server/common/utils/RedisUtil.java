/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import org.apache.commons.pool.impl.GenericObjectPool;
/*    */ import redis.clients.jedis.Jedis;
/*    */ import redis.clients.jedis.JedisPool;
/*    */ import redis.clients.jedis.JedisPoolConfig;
/*    */ 
/*    */ public class RedisUtil {
/*  9 */   private static String ADDR = System.getProperty("Redis.ADDR");
/*    */ 
/*    */   
/* 12 */   private static int PORT = Integer.valueOf(System.getProperty("Redis.PORT")).intValue();
/*    */ 
/*    */   
/* 15 */   private static String AUTH = System.getProperty("Redis.AUTH");
/*    */ 
/*    */ 
/*    */   
/* 19 */   private static int MAX_ACTIVE = 1024;
/*    */ 
/*    */   
/* 22 */   private static int MAX_IDLE = 200;
/*    */ 
/*    */   
/* 25 */   private static int MAX_WAIT = 10000;
/*    */   
/* 27 */   private static int TIMEOUT = 10000;
/*    */ 
/*    */   
/*    */   private static boolean TEST_ON_BORROW = true;
/*    */   
/* 32 */   private static JedisPool jedisPool = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/*    */     try {
/* 39 */       JedisPoolConfig config = new JedisPoolConfig();
/* 40 */       config.setMaxActive(MAX_ACTIVE);
/* 41 */       config.setMaxIdle(MAX_IDLE);
/* 42 */       config.setMaxWait(MAX_WAIT);
/* 43 */       config.setTestOnBorrow(TEST_ON_BORROW);
/* 44 */       jedisPool = new JedisPool((GenericObjectPool.Config)config, ADDR, PORT, TIMEOUT, AUTH);
/* 45 */     } catch (Exception e) {
/* 46 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static synchronized Jedis getJedis() {
/*    */     try {
/* 57 */       if (jedisPool != null) {
/* 58 */         Jedis resource = (Jedis)jedisPool.getResource();
/* 59 */         return resource;
/*    */       } 
/* 61 */       return null;
/*    */     }
/* 63 */     catch (Exception e) {
/* 64 */       e.printStackTrace();
/* 65 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void returnResource(Jedis jedis) {
/* 75 */     if (jedis != null)
/* 76 */       jedisPool.returnResource(jedis); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/RedisUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */