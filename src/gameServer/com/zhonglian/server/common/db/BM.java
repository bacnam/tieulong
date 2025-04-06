/*     */ package com.zhonglian.server.common.db;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.AsynTask.AsyncCallBackTaskBase;
/*     */ import BaseTask.AsynTask.AsyncTaskBase;
/*     */ import BaseTask.AsynTask.AsyncTaskManager;
/*     */ import com.zhonglian.server.common.Config;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ public class BM<T extends IBaseBO>
/*     */ {
/*  21 */   private static Map<Class<? extends IBaseBO>, BM<?>> map = new ConcurrentHashMap<>(); private String tableName;
/*     */   private T boInctance;
/*     */   
/*     */   public static <M extends IBaseBO> BM<M> getBM(Class<M> classType) {
/*  25 */     BM<M> boInfo = (BM<M>)map.get(classType);
/*  26 */     if (boInfo == null) {
/*  27 */       map.put(classType, boInfo = new BM<>(classType));
/*     */     }
/*  29 */     return boInfo;
/*     */   }
/*     */   private final AtomicLong _nextId;
/*     */   private final AtomicLong begin_id;
/*     */   private static final int MAX_ID_RANGE = 100;
/*     */   
/*     */   private IDBConnectionFactory getConnectionFactory() {
/*  36 */     return DBCons.getDBFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BM(Class<T> classType)
/*     */   {
/*  47 */     this._nextId = new AtomicLong(-1L);
/*  48 */     this.begin_id = new AtomicLong(0L);
/*     */     try {
/*     */       this.boInctance = classType.newInstance();
/*     */     } catch (InstantiationException|IllegalAccessException instantiationException) {}
/*  52 */     this.tableName = this.boInctance.getTableName(); } public synchronized long getNextId() { long newId = this._nextId.getAndIncrement();
/*  53 */     if (newId < this.begin_id.get() || newId >= this.begin_id.get() + 100L) {
/*  54 */       long curMax = getCurMaxId();
/*  55 */       curMax = Math.max(curMax, this._nextId.get());
/*  56 */       this._nextId.set(curMax + 1L);
/*  57 */       this.begin_id.set(curMax + 1L);
/*  58 */       newId = this._nextId.getAndIncrement();
/*     */     } 
/*  60 */     return newId; }
/*     */ 
/*     */   
/*     */   private long getCurMaxId() {
/*  64 */     PreparedStatement pstm = null;
/*  65 */     ResultSet rs = null;
/*  66 */     Connection conn = null;
/*     */     try {
/*  68 */       conn = getConnectionFactory().getConnection();
/*  69 */       String sql = String.format("SELECT MAX(id) FROM %s", new Object[] { this.tableName });
/*  70 */       pstm = conn.prepareStatement(sql);
/*  71 */       rs = pstm.executeQuery();
/*     */       
/*  73 */       long maxId = 0L;
/*  74 */       if (rs.next()) {
/*  75 */         maxId = rs.getLong(1);
/*     */       }
/*  77 */       if (maxId == 0L) {
/*  78 */         return Config.getInitialID();
/*     */       }
/*  80 */       return maxId;
/*  81 */     } catch (SQLException e) {
/*  82 */       CommLog.error(e.getMessage(), e);
/*  83 */       return 0L;
/*     */     } finally {
/*  85 */       SQLUtil.close(rs, pstm, conn);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void delAll() {
/*  90 */     Conditions cond = new Conditions();
/*  91 */     cond.setTablesName(this.tableName);
/*  92 */     SQLExecutor.delByCondition(getConnectionFactory(), cond);
/*     */   }
/*     */   
/*     */   public void delAll(String key, Object value) {
/*  96 */     Conditions cond = new Conditions();
/*  97 */     cond.addAndEquals(String.format("`%s`", new Object[] { key }), value);
/*  98 */     cond.setTablesName(this.tableName);
/*  99 */     SQLExecutor.delByCondition(getConnectionFactory(), cond);
/*     */   }
/*     */   
/*     */   public void delAll(String key, Object value, AsyncCallBackTaskBase<Integer> _callBackObj) {
/* 103 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_DelAll(key, value), _callBackObj);
/*     */   }
/*     */   
/*     */   public void delAll(String key, Object value, AsyncCallBackTaskBase<Integer> _callBackObj, int cid) {
/* 107 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_DelAll(key, value), _callBackObj, cid);
/*     */   }
/*     */   
/*     */   public List<T> findAll() {
/* 111 */     Conditions cond = new Conditions();
/* 112 */     return SQLExecutor.getListByCondition(getConnectionFactory(), cond, this.boInctance);
/*     */   }
/*     */   
/*     */   public List<T> findAll(String key, Object value) {
/* 116 */     Conditions cond = new Conditions();
/* 117 */     cond.addAndEquals(String.format("`%s`", new Object[] { key }), value);
/* 118 */     return SQLExecutor.getListByCondition(getConnectionFactory(), cond, this.boInctance);
/*     */   }
/*     */   
/*     */   public List<T> findAll(HashMap<String, Object> conditions) {
/* 122 */     Conditions cond = new Conditions();
/* 123 */     for (String key : conditions.keySet()) {
/* 124 */       cond.addAndEquals(String.format("`%s`", new Object[] { key }), conditions.get(key));
/* 125 */     }  return SQLExecutor.getListByCondition(getConnectionFactory(), cond, this.boInctance);
/*     */   }
/*     */   
/*     */   public void findAll(String key, Object value, AsyncCallBackTaskBase<List<T>> _callBackObj) {
/* 129 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_FindAll<>(key, value), _callBackObj);
/*     */   }
/*     */   
/*     */   public void findAll(String key, Object value, AsyncCallBackTaskBase<List<T>> _callBackObj, int cid) {
/* 133 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_FindAll<>(key, value), _callBackObj, cid);
/*     */   }
/*     */   
/*     */   public List<T> findAllBySort(String sortKey, boolean asc, int limit) {
/* 137 */     Conditions cond = new Conditions();
/* 138 */     cond.setTablesName(this.tableName);
/* 139 */     cond.setLimit(limit);
/* 140 */     cond.addOrderItemsInfo(sortKey);
/* 141 */     cond.setSortType(asc ? "ASC" : "DESC");
/* 142 */     return SQLExecutor.getListByCondition(getConnectionFactory(), cond, this.boInctance);
/*     */   }
/*     */   
/*     */   public int Count() {
/* 146 */     return SQLExecutor.executeQueryCount("select count(1) from " + this.tableName, getConnectionFactory());
/*     */   }
/*     */   public T findOne(HashMap<String, Object> conditions) {
/*     */     IBaseBO iBaseBO;
/* 150 */     List<T> bos = findAll(conditions);
/*     */     
/* 152 */     T tmp = null;
/*     */     
/* 154 */     if (bos != null && !bos.isEmpty()) {
/* 155 */       iBaseBO = (IBaseBO)bos.get(0);
/*     */     }
/*     */     
/* 158 */     return (T)iBaseBO;
/*     */   }
/*     */   public T findOne(String key, Object value) {
/*     */     IBaseBO iBaseBO;
/* 162 */     List<T> bos = findAll(key, value);
/*     */     
/* 164 */     T tmp = null;
/*     */     
/* 166 */     if (bos != null && !bos.isEmpty()) {
/* 167 */       iBaseBO = (IBaseBO)bos.get(0);
/*     */     }
/*     */     
/* 170 */     return (T)iBaseBO;
/*     */   }
/*     */   
/*     */   public <M extends IBaseBO> void findOne(String key, Object value, AsyncCallBackTaskBase<M> _callBackObj) {
/* 174 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Find<>(key, value), _callBackObj);
/*     */   }
/*     */   
/*     */   public <M extends IBaseBO> void findOne(String key, Object value, AsyncCallBackTaskBase<M> _callBackObj, int cid) {
/* 178 */     AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Find<>(key, value), _callBackObj, cid);
/*     */   }
/*     */   
/*     */   public class AsynCall_Find<M extends IBaseBO>
/*     */     implements AsyncTaskBase<M>
/*     */   {
/*     */     private final String key;
/*     */     private final Object value;
/*     */     
/*     */     public AsynCall_Find(String key, Object value) {
/* 188 */       this.key = key;
/* 189 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public M doAsynTask() {
/* 195 */       return BM.this.findOne(this.key, this.value);
/*     */     }
/*     */   }
/*     */   
/*     */   public class AsynCall_FindAll<M extends IBaseBO>
/*     */     implements AsyncTaskBase<List<M>> {
/*     */     private final String key;
/*     */     private final Object value;
/*     */     
/*     */     public AsynCall_FindAll(String key, Object value) {
/* 205 */       this.key = key;
/* 206 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<M> doAsynTask() {
/* 212 */       return BM.this.findAll(this.key, this.value);
/*     */     }
/*     */   }
/*     */   
/*     */   public class AsynCall_DelAll
/*     */     implements AsyncTaskBase<Integer> {
/*     */     private final String key;
/*     */     private final Object value;
/*     */     
/*     */     public AsynCall_DelAll(String key, Object value) {
/* 222 */       this.key = key;
/* 223 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer doAsynTask() {
/* 228 */       Conditions cond = new Conditions();
/* 229 */       cond.addAndEquals(String.format("`%s`", new Object[] { this.key }), this.value);
/* 230 */       cond.setTablesName(BM.this.tableName);
/* 231 */       int res = SQLExecutor.delByCondition(BM.this.getConnectionFactory(), cond);
/* 232 */       return Integer.valueOf(res);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/BM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */