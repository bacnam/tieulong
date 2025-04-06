/*    */ package BaseTask.AsynTask;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncTaskManager
/*    */ {
/* 13 */   private static AsyncTaskManager g_instance = new AsyncTaskManager();
/*    */   
/*    */   public static AsyncTaskManager getInstance() {
/* 16 */     return g_instance;
/*    */   }
/*    */   
/* 19 */   private HashMap<String, AsyncTaskQueue> syncQueues = new HashMap<>();
/*    */   
/*    */   public AsyncTaskQueue getQueue(String tag) {
/* 22 */     return getQueue(tag, false);
/*    */   }
/*    */   
/*    */   public AsyncTaskQueue getQueue(String tag, boolean isMultiQueue) {
/* 26 */     AsyncTaskQueue ret = this.syncQueues.get(tag);
/* 27 */     if (ret == null) {
/* 28 */       synchronized (this.syncQueues) {
/* 29 */         ret = this.syncQueues.get(tag);
/* 30 */         if (ret == null) {
/* 31 */           ret = new AsyncTaskQueue(tag, isMultiQueue);
/* 32 */           this.syncQueues.put(tag, ret);
/*    */         } 
/*    */       } 
/*    */     }
/* 36 */     return ret;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AsyncTaskQueue getDefaultMultQueue() {
/* 45 */     return getInstance().getQueue("DB", true);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 49 */     StringBuilder sBuilder = new StringBuilder();
/*    */     
/* 51 */     sBuilder.append("AsyncTaskManager, size:" + this.syncQueues.size());
/* 52 */     sBuilder.append("\n");
/* 53 */     sBuilder.append(String.format("%-20s%-20s%-20s%-20s\n", new Object[] { "tag", "MultQueue", "ThreadSize", "QueueSize" }));
/*    */     
/* 55 */     for (AsyncTaskQueue queue : this.syncQueues.values()) {
/* 56 */       sBuilder.append(queue.toString());
/*    */     }
/*    */     
/* 59 */     return sBuilder.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/AsynTask/AsyncTaskManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */