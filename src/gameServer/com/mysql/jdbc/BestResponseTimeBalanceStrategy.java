/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BestResponseTimeBalanceStrategy
/*     */   implements BalanceStrategy
/*     */ {
/*     */   public void destroy() {}
/*     */   
/*     */   public void init(Connection conn, Properties props) throws SQLException {}
/*     */   
/*     */   public ConnectionImpl pickConnection(LoadBalancingConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries) throws SQLException {
/*  50 */     Map<String, Long> blackList = proxy.getGlobalBlacklist();
/*     */     
/*  52 */     SQLException ex = null;
/*     */     
/*  54 */     for (int attempts = 0; attempts < numRetries; ) {
/*  55 */       long minResponseTime = Long.MAX_VALUE;
/*     */       
/*  57 */       int bestHostIndex = 0;
/*     */ 
/*     */       
/*  60 */       if (blackList.size() == configuredHosts.size()) {
/*  61 */         blackList = proxy.getGlobalBlacklist();
/*     */       }
/*     */       
/*  64 */       for (int i = 0; i < responseTimes.length; i++) {
/*  65 */         long candidateResponseTime = responseTimes[i];
/*     */         
/*  67 */         if (candidateResponseTime < minResponseTime && !blackList.containsKey(configuredHosts.get(i))) {
/*     */           
/*  69 */           if (candidateResponseTime == 0L) {
/*  70 */             bestHostIndex = i;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/*  75 */           bestHostIndex = i;
/*  76 */           minResponseTime = candidateResponseTime;
/*     */         } 
/*     */       } 
/*     */       
/*  80 */       String bestHost = configuredHosts.get(bestHostIndex);
/*     */       
/*  82 */       ConnectionImpl conn = liveConnections.get(bestHost);
/*     */       
/*  84 */       if (conn == null) {
/*     */         try {
/*  86 */           conn = proxy.createConnectionForHost(bestHost);
/*  87 */         } catch (SQLException sqlEx) {
/*  88 */           ex = sqlEx;
/*     */           
/*  90 */           if (proxy.shouldExceptionTriggerFailover(sqlEx)) {
/*  91 */             proxy.addToGlobalBlacklist(bestHost);
/*  92 */             blackList.put(bestHost, null);
/*     */ 
/*     */             
/*  95 */             if (blackList.size() == configuredHosts.size()) {
/*  96 */               attempts++;
/*     */               try {
/*  98 */                 Thread.sleep(250L);
/*  99 */               } catch (InterruptedException e) {}
/*     */               
/* 101 */               blackList = proxy.getGlobalBlacklist();
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 107 */           throw sqlEx;
/*     */         } 
/*     */       }
/*     */       
/* 111 */       return conn;
/*     */     } 
/*     */     
/* 114 */     if (ex != null) {
/* 115 */       throw ex;
/*     */     }
/*     */     
/* 118 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/BestResponseTimeBalanceStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */