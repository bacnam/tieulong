/*     */ package business.global.rank;
/*     */ 
/*     */ import business.global.gmmail.MailCenter;
/*     */ import com.zhonglian.server.common.db.DBCons;
/*     */ import com.zhonglian.server.common.db.SQLExecutor;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.config.refdata.ref.RefRankReward;
/*     */ import core.database.game.bo.RankRecordBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Rank
/*     */ {
/*     */   private RankType type;
/*     */   List<Record> list;
/*     */   Map<Long, Record> map;
/*     */   private Comparator<RankRecordBO> comparator;
/*     */   
/*     */   public Rank(RankType type, Comparator<RankRecordBO> comparator) {
/*  27 */     this.type = type;
/*  28 */     this.comparator = comparator;
/*  29 */     this.list = new ArrayList<>();
/*  30 */     this.list.add(null);
/*  31 */     this.map = new HashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   void resort() {
/*  36 */     for (Record record : this.map.values()) {
/*  37 */       if (record.recordBO.getValue() != 0L) {
/*  38 */         this.list.add(record);
/*     */       }
/*     */     } 
/*  41 */     this.list.sort((left, right) -> (left == null) ? -1 : ((right == null) ? 1 : this.comparator.compare(left.recordBO, right.recordBO)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     for (int i = 1; i < this.list.size(); i++) {
/*  49 */       ((Record)this.list.get(i)).rank = i;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getRank(long ownerid) {
/*  54 */     Record record = this.map.get(Long.valueOf(ownerid));
/*  55 */     return (record == null) ? 0 : record.rank;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int update(long ownerid, long value, long... ext) {
/*  61 */     if (filter(ownerid)) {
/*  62 */       return 0;
/*     */     }
/*  64 */     synchronized (this) {
/*  65 */       Record record = this.map.get(Long.valueOf(ownerid));
/*  66 */       if (record == null) {
/*  67 */         RankRecordBO recordBO = createRecordBO(ownerid, value);
/*  68 */         this.map.put(Long.valueOf(ownerid), record = new Record(recordBO));
/*  69 */         record.rank = 0;
/*     */       } 
/*     */       
/*  72 */       if (record.rank != 0) {
/*  73 */         return setValue(record, value);
/*     */       }
/*  75 */       return insert(record, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int setValue(Record record, long value) {
/*  81 */     if (record.recordBO.getValue() == value) {
/*  82 */       return record.rank;
/*     */     }
/*  84 */     RankRecordBO recordBO = record.copy();
/*  85 */     recordBO.setValue(value);
/*     */     
/*  87 */     int pos = getInsertPos(recordBO);
/*  88 */     record.saveValue(value);
/*  89 */     if (record.rank == pos || record.rank == pos - 1) {
/*  90 */       return pos;
/*     */     }
/*  92 */     if (record.rank > pos) {
/*     */       
/*  94 */       for (int i = record.rank; i > pos; i--) {
/*  95 */         Record r = this.list.get(i - 1);
/*  96 */         this.list.set(i, r);
/*  97 */         r.rank = i;
/*     */       } 
/*  99 */       this.list.set(pos, record);
/* 100 */       record.rank = pos;
/*     */     } else {
/*     */       
/* 103 */       for (int i = record.rank; i < pos - 1; i++) {
/* 104 */         Record r = this.list.get(i + 1);
/* 105 */         this.list.set(i, r);
/* 106 */         r.rank = i;
/*     */       } 
/* 108 */       this.list.set(pos - 1, record);
/* 109 */       record.rank = pos - 1;
/*     */     } 
/* 111 */     return record.rank;
/*     */   }
/*     */   
/*     */   public long getValue(long ownerid) {
/* 115 */     Record record = this.map.get(Long.valueOf(ownerid));
/* 116 */     if (record != null) {
/* 117 */       return record.recordBO.getValue();
/*     */     }
/* 119 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   private int insert(Record record, long value) {
/* 124 */     record.saveValue(value);
/* 125 */     int pos = getInsertPos(record.recordBO);
/* 126 */     this.list.add(pos, record);
/* 127 */     record.rank = pos;
/*     */ 
/*     */     
/* 130 */     for (int i = pos + 1; i < this.list.size(); i++) {
/* 131 */       ((Record)this.list.get(i)).rank = i;
/*     */     }
/* 133 */     return pos;
/*     */   }
/*     */   
/*     */   protected RankRecordBO createRecordBO(long ownerid, long value) {
/* 137 */     RankRecordBO recordBO = new RankRecordBO();
/* 138 */     recordBO.setType(this.type.ordinal());
/* 139 */     recordBO.setOwner(ownerid);
/* 140 */     recordBO.setValue(value);
/* 141 */     recordBO.setUpdateTime(CommTime.nowSecond());
/* 142 */     recordBO.insert();
/* 143 */     return recordBO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getInsertPos(RankRecordBO recordBo) {
/* 151 */     int left = 0, right = this.list.size();
/* 152 */     while (right - left > 1) {
/* 153 */       int m = (left + right) / 2;
/* 154 */       RankRecordBO mid = ((Record)this.list.get(m)).recordBO;
/* 155 */       if (this.comparator.compare(mid, recordBo) < 0) {
/* 156 */         left = m; continue;
/* 157 */       }  if (this.comparator.compare(mid, recordBo) > 0) {
/* 158 */         right = m;
/*     */         
/*     */         continue;
/*     */       } 
/* 162 */       left = right = m;
/*     */     } 
/*     */     
/* 165 */     return left + 1;
/*     */   }
/*     */   
/*     */   public void sendReward() {
/* 169 */     if (this.list.size() == 0) {
/*     */       return;
/*     */     }
/* 172 */     List<RefRankReward> rewards = RefRankReward.getRewards(this.type);
/* 173 */     for (RefRankReward ref : rewards) {
/* 174 */       for (int rnk = ref.MinRank; rnk <= ref.MaxRank && 
/* 175 */         rnk < this.list.size(); rnk++) {
/*     */ 
/*     */         
/* 178 */         Record r = this.list.get(rnk);
/* 179 */         if (r != null)
/*     */         {
/*     */           
/* 182 */           MailCenter.getInstance().sendMail(r.recordBO.getOwner(), ref.MailId, new String[] { (new StringBuilder(String.valueOf(rnk))).toString() });
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 190 */     int deleteTime = CommTime.nowSecond();
/*     */     
/* 192 */     String delsql = String.format("DELETE from rank_record where value=0 and type=%d and updateTime<%d;", new Object[] { Integer.valueOf(this.type.ordinal()), Integer.valueOf(deleteTime) });
/* 193 */     SQLExecutor.execute(delsql, DBCons.getDBFactory());
/*     */     
/* 195 */     String updatesql = String.format("UPDATE rank_record SET value=0, updateTime=%d where type=%d;", new Object[] { Integer.valueOf(CommTime.nowSecond()), Integer.valueOf(this.type.ordinal()) });
/* 196 */     SQLExecutor.executeUpdate(updatesql, DBCons.getDBFactory());
/*     */     
/* 198 */     this.list.clear();
/* 199 */     this.list.add(null);
/* 200 */     List<Record> todel = new ArrayList<>();
/* 201 */     for (Record record : this.map.values()) {
/* 202 */       if (record.recordBO.getUpdateTime() < deleteTime) {
/* 203 */         todel.add(record);
/*     */         continue;
/*     */       } 
/* 206 */       record.rank = 0;
/* 207 */       record.recordBO.setValue(0L);
/*     */     } 
/* 209 */     for (Record record : todel) {
/* 210 */       this.map.remove(Long.valueOf(record.recordBO.getOwner()));
/*     */     }
/*     */   }
/*     */   
/*     */   public int add(long ownerid, long value) {
/* 215 */     Record record = this.map.get(Long.valueOf(ownerid));
/* 216 */     if (record == null) {
/* 217 */       RankRecordBO recordBO = createRecordBO(ownerid, value);
/* 218 */       this.map.put(Long.valueOf(ownerid), record = new Record(recordBO));
/* 219 */       record.rank = 0;
/*     */     } 
/*     */     
/* 222 */     if (record.rank != 0) {
/* 223 */       return setValue(record, record.getValue() + value);
/*     */     }
/* 225 */     return insert(record, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int minus(long ownerid, int value) {
/* 230 */     Record record = this.map.get(Long.valueOf(ownerid));
/* 231 */     if (record == null) {
/* 232 */       return 0;
/*     */     }
/*     */     
/* 235 */     return setValue(record, Math.max(0L, record.getValue() - value));
/*     */   }
/*     */   
/*     */   synchronized void del(long ownerid) {
/* 239 */     Record record = this.map.get(Long.valueOf(ownerid));
/* 240 */     if (record != null) {
/* 241 */       this.map.remove(Long.valueOf(ownerid));
/* 242 */       this.list.remove(record);
/* 243 */       for (int i = record.rank; i < this.list.size(); i++) {
/* 244 */         if (this.list.get(i) != null)
/* 245 */           ((Record)this.list.get(i)).rank = i; 
/*     */       } 
/* 247 */       record.recordBO.del();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract boolean filter(long paramLong);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/rank/Rank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */