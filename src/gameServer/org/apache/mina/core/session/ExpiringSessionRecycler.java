/*    */ package org.apache.mina.core.session;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import org.apache.mina.util.ExpirationListener;
/*    */ import org.apache.mina.util.ExpiringMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExpiringSessionRecycler
/*    */   implements IoSessionRecycler
/*    */ {
/*    */   private ExpiringMap<SocketAddress, IoSession> sessionMap;
/*    */   private ExpiringMap<SocketAddress, IoSession>.Expirer mapExpirer;
/*    */   
/*    */   public ExpiringSessionRecycler() {
/* 41 */     this(60);
/*    */   }
/*    */   
/*    */   public ExpiringSessionRecycler(int timeToLive) {
/* 45 */     this(timeToLive, 1);
/*    */   }
/*    */   
/*    */   public ExpiringSessionRecycler(int timeToLive, int expirationInterval) {
/* 49 */     this.sessionMap = new ExpiringMap(timeToLive, expirationInterval);
/* 50 */     this.mapExpirer = this.sessionMap.getExpirer();
/* 51 */     this.sessionMap.addExpirationListener(new DefaultExpirationListener());
/*    */   }
/*    */   
/*    */   public void put(IoSession session) {
/* 55 */     this.mapExpirer.startExpiringIfNotStarted();
/*    */     
/* 57 */     SocketAddress key = session.getRemoteAddress();
/*    */     
/* 59 */     if (!this.sessionMap.containsKey(key)) {
/* 60 */       this.sessionMap.put(key, session);
/*    */     }
/*    */   }
/*    */   
/*    */   public IoSession recycle(SocketAddress remoteAddress) {
/* 65 */     return (IoSession)this.sessionMap.get(remoteAddress);
/*    */   }
/*    */   
/*    */   public void remove(IoSession session) {
/* 69 */     this.sessionMap.remove(session.getRemoteAddress());
/*    */   }
/*    */   
/*    */   public void stopExpiring() {
/* 73 */     this.mapExpirer.stopExpiring();
/*    */   }
/*    */   
/*    */   public int getExpirationInterval() {
/* 77 */     return this.sessionMap.getExpirationInterval();
/*    */   }
/*    */   
/*    */   public int getTimeToLive() {
/* 81 */     return this.sessionMap.getTimeToLive();
/*    */   }
/*    */   
/*    */   public void setExpirationInterval(int expirationInterval) {
/* 85 */     this.sessionMap.setExpirationInterval(expirationInterval);
/*    */   }
/*    */   
/*    */   public void setTimeToLive(int timeToLive) {
/* 89 */     this.sessionMap.setTimeToLive(timeToLive);
/*    */   }
/*    */   
/*    */   private class DefaultExpirationListener implements ExpirationListener<IoSession> {
/*    */     public void expired(IoSession expiredSession) {
/* 94 */       expiredSession.close(true);
/*    */     }
/*    */     
/*    */     private DefaultExpirationListener() {}
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/ExpiringSessionRecycler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */