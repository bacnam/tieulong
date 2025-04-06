/*     */ package BaseTask.SyncTask;
/*     */ 
/*     */ import BaseServer.Monitor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
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
/*     */ public class SyncTaskQueue
/*     */ {
/*     */   private String tag;
/*     */   private LinkedList<SyncTaskWrapper> _m_lNormalTaskList;
/*     */   private ReentrantLock _m_lNormalTaskMutex;
/*  30 */   private List<SyncTaskDealThread> taskDealThread = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private SyncTaskTimerCheckThread timerCheck;
/*     */   
/*     */   private HashMap<Long, ArrayList<SyncTaskWrapper>> _m_htTimerTaskTable;
/*     */   
/*     */   private ReentrantLock _m_lTimerTaskMutex;
/*     */   
/*  39 */   private int _m_iTimerTaskCheckTime = 50;
/*     */   
/*     */   private long _m_lNowTime;
/*     */   
/*     */   private Semaphore _m_sTaskEvent;
/*     */ 
/*     */   
/*     */   public SyncTaskQueue(String tag) {
/*  47 */     this.tag = tag;
/*     */     
/*  49 */     this._m_lNormalTaskList = new LinkedList<>();
/*  50 */     this._m_lNormalTaskMutex = new ReentrantLock();
/*  51 */     this._m_htTimerTaskTable = new HashMap<>();
/*  52 */     this._m_lTimerTaskMutex = new ReentrantLock();
/*  53 */     this._m_sTaskEvent = new Semaphore(0);
/*     */ 
/*     */     
/*  56 */     this.timerCheck = new SyncTaskTimerCheckThread(this);
/*  57 */     this.timerCheck.start();
/*     */ 
/*     */     
/*  60 */     int cpuCnt = Runtime.getRuntime().availableProcessors();
/*  61 */     setDealerCount(Math.max(2, cpuCnt));
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setDealerCount(int dealderCount) {
/*  66 */     int addCount = dealderCount - this.taskDealThread.size();
/*     */     
/*  68 */     while (addCount > 0) {
/*     */       
/*  70 */       SyncTaskDealThread newTaskDealThread = new SyncTaskDealThread(this, this.taskDealThread.size());
/*  71 */       newTaskDealThread.start();
/*  72 */       this.taskDealThread.add(newTaskDealThread);
/*     */       
/*  74 */       addCount--;
/*     */     } 
/*     */     
/*  77 */     while (addCount < 0 && 
/*  78 */       this.taskDealThread.size() != 0) {
/*     */ 
/*     */       
/*  81 */       SyncTaskDealThread toRemoveDealThread = this.taskDealThread.remove(this.taskDealThread.size() - 1);
/*  82 */       toRemoveDealThread.dispose();
/*     */       
/*  84 */       addCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  92 */     this.timerCheck.dispose();
/*  93 */     setDealerCount(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTag() {
/* 102 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void RegisterTask(SyncTask _task, String info) {
/* 111 */     _lockNormalTaskList();
/*     */     
/* 113 */     this._m_lNormalTaskList.add(new SyncTaskWrapper(_task, 0L, info, this));
/*     */     
/* 115 */     _releaseTaskEvent();
/*     */     
/* 117 */     _unlockNormalTaskList();
/*     */     
/* 119 */     if (this._m_lNormalTaskList.size() > 10000) {
/* 120 */       Monitor.getInstance().regLog();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void RegisterTask(SyncTask _task) {
/* 130 */     RegisterTask(_task, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void RegisterTask(SyncTask _task, int _time) {
/* 139 */     if (_time <= 0) {
/* 140 */       RegisterTask(_task);
/*     */       
/*     */       return;
/*     */     } 
/* 144 */     _lockTimerTaskList();
/*     */ 
/*     */     
/* 147 */     _addTimerTask(_time, _task, "");
/*     */     
/* 149 */     _unlockTimerTaskList();
/*     */   }
/*     */   
/*     */   public void RegisterTask(SyncTask _task, int _time, String info) {
/* 153 */     _lockTimerTaskList();
/*     */ 
/*     */     
/* 156 */     _addTimerTask(_time, _task, info);
/*     */     
/* 158 */     _unlockTimerTaskList();
/*     */   }
/*     */   
/*     */   public void RegisterTask(SyncTask _task, long _time) {
/* 162 */     _lockTimerTaskList();
/*     */ 
/*     */     
/* 165 */     _addTimerTask(_time, _task, "");
/*     */     
/* 167 */     _unlockTimerTaskList();
/*     */   }
/*     */   
/*     */   public void RegisterTask(SyncTask _task, long _time, String info) {
/* 171 */     _lockTimerTaskList();
/*     */ 
/*     */     
/* 174 */     _addTimerTask(_time, _task, info);
/*     */     
/* 176 */     _unlockTimerTaskList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SyncTaskWrapper PopTask() {
/* 185 */     _acquireTaskEvent();
/* 186 */     _lockNormalTaskList();
/*     */     
/* 188 */     if (this._m_lNormalTaskList.isEmpty()) {
/* 189 */       _unlockNormalTaskList();
/* 190 */       return null;
/*     */     } 
/*     */     
/* 193 */     SyncTaskWrapper task = this._m_lNormalTaskList.removeFirst();
/*     */     
/* 195 */     _unlockNormalTaskList();
/* 196 */     return task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void transTimer2NormalList(long _startTime) {
/* 207 */     ArrayList<SyncTaskWrapper> needAddTaskList = _popTimerTask(_startTime);
/* 208 */     if (needAddTaskList != null && !needAddTaskList.isEmpty())
/*     */     {
/* 210 */       RegisterTaskList(needAddTaskList);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void RegisterTaskList(ArrayList<SyncTaskWrapper> _taskList) {
/* 220 */     _lockNormalTaskList();
/*     */     
/* 222 */     int taskCount = _taskList.size();
/* 223 */     this._m_lNormalTaskList.addAll(_taskList);
/*     */     
/* 225 */     _releaseTaskEvent(taskCount);
/*     */     
/* 227 */     _unlockNormalTaskList();
/*     */   }
/*     */   
/*     */   protected long _getNowTime() {
/* 231 */     return this._m_lNowTime;
/*     */   }
/*     */   
/*     */   protected long _refreshTimerTaskNowTime() {
/* 235 */     long nowTime = (new Date()).getTime();
/*     */     
/* 237 */     int deltaTime = (int)(nowTime % this._m_iTimerTaskCheckTime);
/* 238 */     this._m_lNowTime = nowTime - deltaTime;
/*     */     
/* 240 */     return this._m_lNowTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _lockNormalTaskList() {
/* 247 */     this._m_lNormalTaskMutex.lock();
/*     */   }
/*     */   
/*     */   protected void _unlockNormalTaskList() {
/* 251 */     this._m_lNormalTaskMutex.unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _lockTimerTaskList() {
/* 258 */     this._m_lTimerTaskMutex.lock();
/*     */   }
/*     */   
/*     */   protected void _unlockTimerTaskList() {
/* 262 */     this._m_lTimerTaskMutex.unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _releaseTaskEvent() {
/* 269 */     this._m_sTaskEvent.release();
/*     */   }
/*     */   
/*     */   protected void _releaseTaskEvent(int count) {
/* 273 */     this._m_sTaskEvent.release(count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _acquireTaskEvent() {
/* 280 */     this._m_sTaskEvent.acquireUninterruptibly();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 284 */     return String.format("%-20s%-20s%-20s%-20s\n", new Object[] { this.tag, Integer.valueOf(this.taskDealThread.size()), Integer.valueOf(getNormalTaskSize()), Integer.valueOf(getTimerTaskSize()) });
/*     */   }
/*     */   
/*     */   public int getTimerTaskSize() {
/* 288 */     int ret = 0;
/* 289 */     for (ArrayList<SyncTaskWrapper> cnt : this._m_htTimerTaskTable.values()) {
/* 290 */       ret += cnt.size();
/*     */     }
/* 292 */     return ret;
/*     */   }
/*     */   
/*     */   public int getNormalTaskSize() {
/* 296 */     return this._m_lNormalTaskList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _addTimerTask(long _waitTime, SyncTask _task, String info) {
/* 305 */     long _dealTime = _getNowTime() + _waitTime;
/*     */     
/* 307 */     int deltaTime = (int)(_dealTime % this._m_iTimerTaskCheckTime);
/*     */ 
/*     */     
/* 310 */     long realDealTime = _dealTime;
/* 311 */     if (deltaTime != 0) {
/* 312 */       realDealTime = _dealTime - deltaTime + this._m_iTimerTaskCheckTime;
/*     */     }
/* 314 */     _lockTimerTaskList();
/*     */     
/* 316 */     ArrayList<SyncTaskWrapper> taskList = this._m_htTimerTaskTable.get(Long.valueOf(realDealTime));
/* 317 */     if (taskList == null) {
/* 318 */       taskList = new ArrayList<>();
/* 319 */       this._m_htTimerTaskTable.put(Long.valueOf(realDealTime), taskList);
/*     */     } 
/*     */     
/* 322 */     taskList.add(new SyncTaskWrapper(_task, _waitTime, info, this));
/*     */     
/* 324 */     _unlockTimerTaskList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArrayList<SyncTaskWrapper> _popTimerTask(long _startTime) {
/* 334 */     ArrayList<SyncTaskWrapper> list = new ArrayList<>();
/*     */     
/* 336 */     _lockTimerTaskList();
/*     */     
/* 338 */     long endTime = _getNowTime();
/*     */ 
/*     */     
/* 341 */     long time = _startTime;
/* 342 */     while (time <= endTime) {
/* 343 */       ArrayList<SyncTaskWrapper> tmpList = this._m_htTimerTaskTable.remove(Long.valueOf(time));
/* 344 */       if (tmpList != null) {
/* 345 */         list.addAll(tmpList);
/*     */       }
/* 347 */       time += this._m_iTimerTaskCheckTime;
/*     */     } 
/*     */     
/* 350 */     _unlockTimerTaskList();
/*     */     
/* 352 */     return list;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseTask/SyncTask/SyncTaskQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */