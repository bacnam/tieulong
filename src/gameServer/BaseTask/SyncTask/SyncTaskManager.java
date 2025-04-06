/*    */ package BaseTask.SyncTask;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import java.util.HashMap;
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
/*    */ public class SyncTaskManager
/*    */ {
/* 17 */   private static SyncTaskManager g_instance = new SyncTaskManager();
/*    */   
/*    */   public static SyncTaskManager getInstance() {
/* 20 */     return g_instance;
/*    */   }
/*    */   
/* 23 */   private HashMap<String, SyncTaskQueue> syncQueues = new HashMap<>();
/*    */   
/*    */   public SyncTaskQueue getQueue(String tag) {
/* 26 */     SyncTaskQueue ret = this.syncQueues.get(tag);
/* 27 */     if (ret == null) {
/* 28 */       synchronized (this.syncQueues) {
/* 29 */         ret = this.syncQueues.get(tag);
/* 30 */         if (ret == null) {
/* 31 */           ret = new SyncTaskQueue(tag);
/* 32 */           this.syncQueues.put(tag, ret);
/*    */         } 
/*    */       } 
/*    */     }
/* 36 */     return ret;
/*    */   }
/*    */   
/*    */   public static void task(SyncTask _task) {
/* 40 */     getInstance().getQueue("Default").RegisterTask(_task);
/*    */   }
/*    */   
/*    */   public static void task(SyncTask _task, int _time) {
/* 44 */     getInstance().getQueue("Default").RegisterTask(_task, _time);
/*    */   }
/*    */   
/*    */   public static void task(SyncTask _task, long _time) {
/* 48 */     getInstance().getQueue("Default").RegisterTask(_task, _time);
/*    */   }
/*    */   
/*    */   public static void task(SyncTask _task, String info) {
/* 52 */     getInstance().getQueue("Default").RegisterTask(_task, info);
/*    */   }
/*    */   
/*    */   public static void task(SyncTask _task, int _time, String info) {
/* 56 */     getInstance().getQueue("Default").RegisterTask(_task, _time, info);
/*    */   }
/*    */   
/*    */   public static void task(SyncTask _task, long _time, String info) {
/* 60 */     getInstance().getQueue("Default").RegisterTask(_task, _time, info);
/*    */   }
/*    */   
/*    */   public static void schedule(final int interval, final SyncTimer timer) {
/* 64 */     task(new SyncTask()
/*    */         {
/*    */           public void run() {
/* 67 */             boolean cont = true;
/*    */             try {
/* 69 */               cont = timer.run();
/* 70 */             } catch (Exception e) {
/* 71 */               CommLog.error("schedule Exception", e);
/*    */             } 
/* 73 */             if (cont)
/* 74 */               SyncTaskManager.task(this, interval); 
/*    */           }
/* 76 */         }interval);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 80 */     StringBuilder sBuilder = new StringBuilder();
/*    */     
/* 82 */     sBuilder.append("SyncTaskManager, size:" + this.syncQueues.size());
/* 83 */     sBuilder.append("\n");
/* 84 */     sBuilder.append(String.format("%-20s%-20s%-20s%-20s\n", new Object[] { "tag", "ThreadSize", "TaskSize", "TimerSize" }));
/*    */     
/* 86 */     for (SyncTaskQueue queue : this.syncQueues.values()) {
/* 87 */       sBuilder.append(queue.toString());
/*    */     }
/*    */     
/* 90 */     return sBuilder.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/SyncTask/SyncTaskManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */