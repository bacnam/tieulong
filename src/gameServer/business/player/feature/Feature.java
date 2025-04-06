/*    */ package business.player.feature;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseThread.BaseMutexObject;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.utils.CommTime;
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
/*    */ public abstract class Feature
/*    */ {
/*    */   protected final Player player;
/* 23 */   private Boolean _loaded = Boolean.valueOf(false);
/* 24 */   private int lastActiveTime = 0;
/*    */   
/* 26 */   protected final BaseMutexObject m_mutex = new BaseMutexObject();
/*    */   
/*    */   public Player getPlayer() {
/* 29 */     return this.player;
/*    */   }
/*    */   
/*    */   public String getPlayerName() {
/* 33 */     return this.player.getName();
/*    */   }
/*    */   
/*    */   public long getPid() {
/* 37 */     return this.player.getPid();
/*    */   }
/*    */   
/*    */   protected void lock() {
/* 41 */     this.m_mutex.lock();
/*    */   }
/*    */   
/*    */   protected void unlock() {
/* 45 */     this.m_mutex.unlock();
/*    */   }
/*    */   
/*    */   public Feature(Player player) {
/* 49 */     this.player = player;
/*    */   }
/*    */   
/*    */   public int getLastActiveTime() {
/* 53 */     return this.lastActiveTime;
/*    */   }
/*    */   
/*    */   public void updateLastActiveTime() {
/* 57 */     this.lastActiveTime = CommTime.RecentSec;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Feature tryLoadDBData() {
/* 64 */     this.lastActiveTime = CommTime.nowSecond();
/*    */     
/* 66 */     if (this._loaded.booleanValue()) {
/* 67 */       return this;
/*    */     }
/*    */     
/* 70 */     synchronized (this._loaded) {
/* 71 */       if (this._loaded.booleanValue()) {
/* 72 */         return this;
/*    */       }
/*    */       
/*    */       try {
/* 76 */         loadDB();
/* 77 */       } catch (Exception e) {
/* 78 */         CommLog.error("Feature.tryLoadDBData", e);
/*    */       } 
/* 80 */       this._loaded = Boolean.valueOf(true);
/*    */     } 
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public abstract void loadDB();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/Feature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */