/*     */ package com.zhonglian.server.common.db;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.AsynTask.AsyncCallBackTaskBase;
/*     */ import BaseTask.AsynTask.AsyncTaskBase;
/*     */ import BaseTask.AsynTask.AsyncTaskManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseBO
/*     */   implements IBaseBO, Comparable<BaseBO>
/*     */ {
/*  19 */   public static AtomicInteger AsynTaskCnt = new AtomicInteger(0);
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getAsynTaskCnt() {
/*  24 */     return AsynTaskCnt.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public IDBConnectionFactory getConnectionFactory() {
/*  29 */     return DBCons.getDBFactory();
/*     */   }
/*     */   
/*     */   public void insert(AsynCallBack_Insert _callback) {
/*  33 */     if (getId() == 0L) {
/*  34 */       setId(BM.getBM(getClass()).getNextId());
/*     */     }
/*  36 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Insert(this), _callback, getAsynTaskTag());
/*     */   }
/*     */   
/*     */   public boolean insert_sync() {
/*  40 */     if (getId() == 0L) {
/*  41 */       setId(BM.getBM(getClass()).getNextId());
/*     */     }
/*  43 */     return SQLExecutor.InsertBO(getConnectionFactory(), this, getInsertValueBytes());
/*     */   }
/*     */   
/*     */   public void insert() {
/*  47 */     if (getId() == 0L) {
/*  48 */       setId(BM.getBM(getClass()).getNextId());
/*  49 */       AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Insert(this), null, getAsynTaskTag());
/*     */     } else {
/*  51 */       CommLog.error("[{}:{}]数据已经存在，进行saveall操作", getTableName(), Long.valueOf(getId()));
/*  52 */       AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateAll(), null, getAsynTaskTag());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void del() {
/*  57 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Del(this), null, getAsynTaskTag());
/*     */   }
/*     */   
/*     */   protected void saveField(String field, Object newValue) {
/*  61 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateField(field, newValue), null, getAsynTaskTag());
/*     */   }
/*     */   
/*     */   protected void saveFieldBytes(String field, byte[] newValue) {
/*  65 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateFieldBytes(field, newValue), null, getAsynTaskTag());
/*     */   }
/*     */   
/*     */   public void saveAll() {
/*  69 */     if (getId() == 0L) {
/*  70 */       CommLog.error("[{}]数据还未创建，进行insert操作", getTableName());
/*  71 */       setId(BM.getBM(getClass()).getNextId());
/*  72 */       AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Insert(this), null, getAsynTaskTag());
/*     */     } else {
/*  74 */       AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateAll(), null, getAsynTaskTag());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static abstract class AsynCallBack_Insert
/*     */     extends AsyncCallBackTaskBase<Boolean> {
/*     */     protected BaseBO _bo;
/*     */     
/*     */     public AsynCallBack_Insert(BaseBO _bo) {
/*  83 */       this._bo = _bo;
/*     */     }
/*     */   }
/*     */   
/*     */   public class AsynCall_Insert
/*     */     implements AsyncTaskBase<Boolean> {
/*     */     BaseBO bo;
/*     */     
/*     */     public AsynCall_Insert(BaseBO _bo) {
/*  92 */       BaseBO.AsynTaskCnt.getAndIncrement();
/*  93 */       this.bo = _bo;
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean doAsynTask() {
/*  98 */       BaseBO.AsynTaskCnt.getAndDecrement();
/*  99 */       return Boolean.valueOf(SQLExecutor.InsertBO(BaseBO.this.getConnectionFactory(), this.bo, this.bo.getInsertValueBytes()));
/*     */     }
/*     */   }
/*     */   
/*     */   public class AsynCall_Del
/*     */     implements AsyncTaskBase<Integer> {
/*     */     BaseBO _bo;
/*     */     
/*     */     public AsynCall_Del(BaseBO _bo) {
/* 108 */       BaseBO.AsynTaskCnt.getAndIncrement();
/* 109 */       this._bo = _bo;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer doAsynTask() {
/* 114 */       Conditions cond = new Conditions();
/* 115 */       cond.setTablesName(this._bo.getTableName());
/* 116 */       cond.addAndEquals("`id`", Long.valueOf(this._bo.getId()));
/*     */       
/* 118 */       BaseBO.AsynTaskCnt.getAndDecrement();
/* 119 */       return Integer.valueOf(SQLExecutor.delByCondition(BaseBO.this.getConnectionFactory(), cond));
/*     */     }
/*     */   }
/*     */   
/*     */   public class AsynRun_UpdateField
/*     */     implements AsyncTaskBase<Object> {
/*     */     private final String _field;
/*     */     private final Object _newValue;
/*     */     
/*     */     public AsynRun_UpdateField(String _field, Object _newValue) {
/* 129 */       this._field = _field;
/* 130 */       this._newValue = _newValue;
/* 131 */       BaseBO.AsynTaskCnt.getAndIncrement();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object doAsynTask() {
/* 136 */       Conditions cond = new Conditions();
/* 137 */       cond.setTablesName(BaseBO.this.getTableName());
/* 138 */       cond.addAndEquals("`id`", Long.valueOf(BaseBO.this.getId()));
/*     */       
/* 140 */       Object val = this._newValue;
/* 141 */       if (this._newValue instanceof Boolean) {
/* 142 */         boolean tmp = ((Boolean)this._newValue).booleanValue();
/* 143 */         val = Integer.valueOf(tmp ? 1 : 0);
/*     */       } 
/* 145 */       if (this._newValue instanceof String) {
/* 146 */         val = ((String)this._newValue).replace("'", "''");
/*     */       }
/* 148 */       String sql = String.format("`%s` = '%s'", new Object[] { this._field, val });
/*     */       
/* 150 */       BaseBO.AsynTaskCnt.getAndDecrement();
/* 151 */       SQLExecutor.updateByCondition(BaseBO.this.getConnectionFactory(), cond, sql);
/* 152 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public class AsynRun_UpdateFieldBytes
/*     */     implements AsyncTaskBase<Object> {
/*     */     private final String _field;
/*     */     private final byte[] _newValue;
/*     */     
/*     */     public AsynRun_UpdateFieldBytes(String _field, byte[] _newValue) {
/* 162 */       this._field = _field;
/* 163 */       this._newValue = _newValue;
/* 164 */       BaseBO.AsynTaskCnt.getAndIncrement();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object doAsynTask() {
/* 169 */       Conditions cond = new Conditions();
/* 170 */       cond.setTablesName(BaseBO.this.getTableName());
/* 171 */       cond.addAndEquals("`id`", Long.valueOf(BaseBO.this.getId()));
/*     */       
/* 173 */       String sql = String.format("`%s` = ?", new Object[] { this._field });
/* 174 */       ArrayList<byte[]> _binaryList = (ArrayList)new ArrayList<>();
/* 175 */       _binaryList.add(this._newValue);
/*     */       
/* 177 */       BaseBO.AsynTaskCnt.getAndDecrement();
/* 178 */       SQLExecutor.updateByCondition(BaseBO.this.getConnectionFactory(), cond, sql, _binaryList);
/* 179 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void saveAll_sync() {
/* 184 */     Conditions cond = new Conditions();
/* 185 */     cond.setTablesName(getTableName());
/* 186 */     cond.addAndEquals("`id`", Long.valueOf(getId()));
/* 187 */     SQLExecutor.updateByCondition(getConnectionFactory(), cond, getUpdateKeyValue(), getInsertValueBytes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class AsynRun_UpdateAll
/*     */     implements AsyncTaskBase<Object>
/*     */   {
/*     */     public AsynRun_UpdateAll() {
/* 202 */       BaseBO.AsynTaskCnt.getAndIncrement();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object doAsynTask() {
/* 207 */       BaseBO.AsynTaskCnt.getAndDecrement();
/* 208 */       BaseBO.this.saveAll_sync();
/* 209 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(BaseBO o) {
/* 215 */     return (int)(getId() - o.getId());
/*     */   }
/*     */   
/*     */   public abstract long getAsynTaskTag();
/*     */   
/*     */   protected abstract String getUpdateKeyValue();
/*     */   
/*     */   protected abstract ArrayList<byte[]> getInsertValueBytes();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/BaseBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */