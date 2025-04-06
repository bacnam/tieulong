/*     */ package BaseTask.AsynTask;
/*     */ 
/*     */ import BaseServer.Monitor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class AsyncTaskQueue
/*     */ {
/*     */   public class AsyncThreadQueue
/*     */   {
/*  28 */     private LinkedList<AsyncTaskWrapper> infoList = new LinkedList<>();
/*  29 */     private Semaphore _m_sSemaphore = new Semaphore(0);
/*  30 */     private ReentrantLock _m_rMutex = new ReentrantLock();
/*     */     private String tag;
/*     */     private int id;
/*     */     
/*     */     public AsyncThreadQueue(String tag, int id) {
/*  35 */       this.tag = tag;
/*  36 */       this.id = id;
/*     */     }
/*     */     
/*     */     public int getID() {
/*  40 */       return this.id;
/*     */     }
/*     */     
/*     */     public String getTag() {
/*  44 */       return this.tag;
/*     */     }
/*     */     
/*     */     protected void _lock() {
/*  48 */       this._m_rMutex.lock();
/*     */     }
/*     */     
/*     */     protected void _unlock() {
/*  52 */       this._m_rMutex.unlock();
/*     */     }
/*     */     
/*     */     public <T> void regAsynTask(AsyncTaskWrapper<T> obj) {
/*  56 */       _lock();
/*  57 */       this.infoList.add(obj);
/*  58 */       this._m_sSemaphore.release();
/*  59 */       _unlock();
/*     */       
/*  61 */       if (this.infoList.size() > 10000) {
/*  62 */         Monitor.getInstance().regLog();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public AsyncTaskWrapper popFirstAsynTask() {
/*  68 */       this._m_sSemaphore.acquireUninterruptibly();
/*  69 */       AsyncTaskWrapper ret = null;
/*  70 */       _lock();
/*  71 */       ret = this.infoList.pop();
/*  72 */       _unlock();
/*  73 */       return ret;
/*     */     }
/*     */     
/*     */     public int getSize() {
/*  77 */       return this.infoList.size();
/*     */     }
/*     */   }
/*     */   
/*  81 */   private ReentrantLock _m_rMutex = new ReentrantLock();
/*     */   
/*  83 */   private List<AsyncThreadQueue> taskDealQueue = new ArrayList<>();
/*  84 */   private List<AsyncTaskDealThread> taskDealThread = new ArrayList<>();
/*     */   private String tag;
/*     */   private boolean isMultQueue;
/*     */   
/*     */   public AsyncTaskQueue(String tag, boolean isMultQueue) {
/*  89 */     this.tag = tag;
/*  90 */     this.isMultQueue = isMultQueue;
/*     */ 
/*     */     
/*  93 */     int cpuCnt = Runtime.getRuntime().availableProcessors();
/*  94 */     setDealerCount(Math.max(4, cpuCnt));
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized AsyncThreadQueue getSubQueue(int id) {
/*  99 */     if (this.isMultQueue) {
/* 100 */       if (this.taskDealQueue.size() <= id) {
/* 101 */         this.taskDealQueue.add(new AsyncThreadQueue(this.tag, id));
/*     */       }
/* 103 */       return this.taskDealQueue.get(id);
/*     */     } 
/*     */     
/* 106 */     if (this.taskDealQueue.size() == 0) {
/* 107 */       this.taskDealQueue.add(new AsyncThreadQueue(this.tag, 0));
/*     */     }
/* 109 */     return this.taskDealQueue.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeSubQueue(int id) {
/* 114 */     if (this.isMultQueue) {
/* 115 */       this.taskDealQueue.remove(id);
/*     */     }
/* 117 */     else if (id == 0) {
/* 118 */       this.taskDealQueue.remove(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDealerCount(int dealderCount) {
/* 124 */     _lock();
/*     */     
/* 126 */     int addCount = dealderCount - this.taskDealThread.size();
/*     */     
/* 128 */     while (addCount > 0) {
/*     */       
/* 130 */       int id = this.taskDealThread.size();
/* 131 */       AsyncThreadQueue queue = getSubQueue(id);
/* 132 */       AsyncTaskDealThread dealer = new AsyncTaskDealThread(queue, id);
/* 133 */       dealer.start();
/* 134 */       this.taskDealThread.add(dealer);
/*     */       
/* 136 */       addCount--;
/*     */     } 
/*     */     
/* 139 */     while (addCount < 0 && 
/* 140 */       this.taskDealThread.size() != 0) {
/*     */ 
/*     */       
/* 143 */       AsyncTaskDealThread toRemoveDealThread = this.taskDealThread.remove(this.taskDealThread.size() - 1);
/* 144 */       toRemoveDealThread.dispose();
/* 145 */       removeSubQueue(this.taskDealThread.size() - 1);
/*     */       
/* 147 */       addCount++;
/*     */     } 
/*     */     
/* 150 */     _unlock();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 154 */     return String.format("%-20s%-20s%-20s%-20s\n", new Object[] { this.tag, Boolean.valueOf(this.isMultQueue), Integer.valueOf(this.taskDealThread.size()), getQueueInfo() });
/*     */   }
/*     */   
/*     */   public String getQueueInfo() {
/* 158 */     StringBuilder sBuilder = new StringBuilder();
/* 159 */     for (int index = 0; index < this.taskDealQueue.size(); index++) {
/* 160 */       AsyncThreadQueue queue = this.taskDealQueue.get(index);
/* 161 */       sBuilder.append(String.format("{%s:%s}", new Object[] { Integer.valueOf(queue.getID()), Integer.valueOf(queue.getSize()) }));
/*     */     } 
/* 163 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public <T> void regAsynTask(AsyncTaskBase<T> _callObj, AsyncCallBackTaskBase<T> _callBackObj, long index) {
/* 167 */     index = Math.abs(index);
/* 168 */     AsyncTaskWrapper<T> info = new AsyncTaskWrapper<>(_callObj, _callBackObj);
/*     */     
/* 170 */     _lock();
/*     */     
/* 172 */     if (this.taskDealQueue.size() == 0) {
/* 173 */       _unlock();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 178 */     if (this.isMultQueue) {
/* 179 */       int id = (int)(index % this.taskDealQueue.size());
/* 180 */       AsyncThreadQueue mgr = this.taskDealQueue.get(id);
/* 181 */       mgr.regAsynTask(info);
/*     */     } else {
/* 183 */       ((AsyncThreadQueue)this.taskDealQueue.get(0)).regAsynTask(info);
/*     */     } 
/*     */     
/* 186 */     _unlock();
/*     */   }
/*     */   
/*     */   public <T> void regAsynTask(AsyncTaskBase<T> _callObj, AsyncCallBackTaskBase<T> _callBackObj) {
/* 190 */     regAsynTask(_callObj, _callBackObj, 0L);
/*     */   }
/*     */   
/*     */   protected void _lock() {
/* 194 */     this._m_rMutex.lock();
/*     */   }
/*     */   
/*     */   protected void _unlock() {
/* 198 */     this._m_rMutex.unlock();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/AsynTask/AsyncTaskQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */