/*    */ package com.facebook.fb303;
/*    */ 
/*    */ import java.util.AbstractMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.thrift.TException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FacebookBase
/*    */   implements FacebookService.Iface
/*    */ {
/*    */   private String name_;
/*    */   private long alive_;
/* 32 */   private final ConcurrentHashMap<String, Long> counters_ = new ConcurrentHashMap<String, Long>();
/*    */ 
/*    */   
/* 35 */   private final ConcurrentHashMap<String, String> options_ = new ConcurrentHashMap<String, String>();
/*    */ 
/*    */   
/*    */   protected FacebookBase(String name) {
/* 39 */     this.name_ = name;
/* 40 */     this.alive_ = System.currentTimeMillis() / 1000L;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name_;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getStatusDetails() {
/* 50 */     return "";
/*    */   }
/*    */   
/*    */   public void deleteCounter(String key) {
/* 54 */     this.counters_.remove(key);
/*    */   }
/*    */   
/*    */   public void resetCounter(String key) {
/* 58 */     this.counters_.put(key, Long.valueOf(0L));
/*    */   }
/*    */   
/*    */   public long incrementCounter(String key) {
/* 62 */     long val = getCounter(key) + 1L;
/* 63 */     this.counters_.put(key, Long.valueOf(val));
/* 64 */     return val;
/*    */   }
/*    */   
/*    */   public AbstractMap<String, Long> getCounters() {
/* 68 */     return this.counters_;
/*    */   }
/*    */   
/*    */   public long getCounter(String key) {
/* 72 */     Long val = this.counters_.get(key);
/* 73 */     if (val == null) {
/* 74 */       return 0L;
/*    */     }
/* 76 */     return val.longValue();
/*    */   }
/*    */   
/*    */   public void setOption(String key, String value) {
/* 80 */     this.options_.put(key, value);
/*    */   }
/*    */   
/*    */   public String getOption(String key) {
/* 84 */     return this.options_.get(key);
/*    */   }
/*    */   
/*    */   public AbstractMap<String, String> getOptions() {
/* 88 */     return this.options_;
/*    */   }
/*    */   
/*    */   public long aliveSince() {
/* 92 */     return this.alive_;
/*    */   }
/*    */   
/*    */   public String getCpuProfile() {
/* 96 */     return "";
/*    */   }
/*    */   
/*    */   public void reinitialize() {}
/*    */   
/*    */   public void shutdown() {}
/*    */   
/*    */   public abstract fb_status getStatus();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/facebook/fb303/FacebookBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */