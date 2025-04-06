/*    */ package business.global.broadcast;
/*    */ 
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import BaseThread.BaseMutexObject;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ public class BroadcastMgr
/*    */ {
/*    */   public static BroadcastMgr instance;
/*    */   public final LinkedList<BroadcastRecord> queue;
/*    */   private final BaseMutexObject m_mutex;
/*    */   
/*    */   public static BroadcastMgr getInstance() {
/* 18 */     if (instance == null) {
/* 19 */       instance = new BroadcastMgr();
/*    */     }
/* 21 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BroadcastMgr() {
/* 28 */     this.queue = Lists.newLinkedList();
/*    */     
/* 30 */     this.m_mutex = new BaseMutexObject();
/*    */     regBroadCastTask();
/*    */   } public void lock() {
/* 33 */     this.m_mutex.lock();
/*    */   }
/*    */   
/*    */   public void unlock() {
/* 37 */     this.m_mutex.unlock();
/*    */   }
/*    */   
/*    */   public void regBroadCastTask() {
/* 41 */     SyncTaskManager.schedule(1000, () -> {
/*    */           broadcastTask();
/*    */           return true;
/*    */         });
/*    */   }
/*    */   
/*    */   public void addNewBroadcastTask(BroadcastTask task) {
/* 48 */     Collection<Player> players = PlayerMgr.getInstance().getOnlinePlayers();
/*    */     
/* 50 */     lock();
/*    */     
/* 52 */     BroadcastRecord record = new BroadcastRecord(Lists.newLinkedList(players), task);
/* 53 */     this.queue.add(record);
/*    */     
/* 55 */     unlock();
/*    */   }
/*    */   
/*    */   public void addNewBroadcastTask(LinkedList<Player> players, BroadcastTask task) {
/* 59 */     lock();
/*    */     
/* 61 */     BroadcastRecord record = new BroadcastRecord(players, task);
/* 62 */     this.queue.add(record);
/*    */     
/* 64 */     unlock();
/*    */   }
/*    */   
/*    */   public void broadcastTask() {
/* 68 */     for (int i = 0; i < 100; i++) {
/* 69 */       Player player = null;
/* 70 */       BroadcastTask task = null;
/* 71 */       lock();
/* 72 */       while (!this.queue.isEmpty()) {
/* 73 */         BroadcastRecord peek = this.queue.peek();
/* 74 */         player = peek.players.poll();
/* 75 */         if (peek.players.isEmpty()) {
/* 76 */           this.queue.pop();
/*    */         }
/* 78 */         if (player != null) {
/* 79 */           task = peek.task;
/*    */           break;
/*    */         } 
/*    */       } 
/* 83 */       unlock();
/* 84 */       if (player != null && task != null)
/* 85 */         task.poll(player); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/broadcast/BroadcastMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */