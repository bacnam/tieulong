/*     */ package business.global.guild;
/*     */ 
/*     */ import com.zhonglian.server.common.db.DBCons;
/*     */ import com.zhonglian.server.common.db.SQLExecutor;
/*     */ import com.zhonglian.server.common.enums.GuildRankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.database.game.bo.GuildRankRecordBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GuildRank
/*     */ {
/*     */   private GuildRankType type;
/*     */   List<GuildRecord> list;
/*     */   Map<Long, GuildRecord> map;
/*     */   private Comparator<GuildRankRecordBO> comparator;
/*     */   
/*     */   public GuildRank(GuildRankType type, Comparator<GuildRankRecordBO> comparator) {
/*  25 */     this.type = type;
/*  26 */     this.comparator = comparator;
/*  27 */     this.list = new ArrayList<>();
/*  28 */     this.list.add(null);
/*  29 */     this.map = new HashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   void resort() {
/*  34 */     for (GuildRecord record : this.map.values()) {
/*  35 */       if (record.recordBO.getValue() != 0L) {
/*  36 */         this.list.add(record);
/*     */       }
/*     */     } 
/*  39 */     this.list.sort((left, right) -> (left == null) ? -1 : ((right == null) ? 1 : this.comparator.compare(left.recordBO, right.recordBO)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     for (int i = 1; i < this.list.size(); i++) {
/*  47 */       ((GuildRecord)this.list.get(i)).rank = i;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getRank(long ownerid) {
/*  52 */     GuildRecord record = this.map.get(Long.valueOf(ownerid));
/*  53 */     return (record == null) ? 0 : record.rank;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int update(long ownerid, long value, long... ext) {
/*  59 */     if (filter(ownerid)) {
/*  60 */       return 0;
/*     */     }
/*  62 */     synchronized (this) {
/*  63 */       GuildRecord record = this.map.get(Long.valueOf(ownerid));
/*  64 */       if (record == null) {
/*  65 */         GuildRankRecordBO recordBO = createRecordBO(ownerid, value);
/*  66 */         this.map.put(Long.valueOf(ownerid), record = new GuildRecord(recordBO));
/*  67 */         record.rank = 0;
/*     */       } 
/*     */       
/*  70 */       if (record.rank != 0) {
/*  71 */         return setValue(record, value);
/*     */       }
/*  73 */       return insert(record);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int setValue(GuildRecord record, long value) {
/*  79 */     if (record.recordBO.getValue() == value) {
/*  80 */       return record.rank;
/*     */     }
/*  82 */     GuildRankRecordBO recordBO = record.copy();
/*  83 */     recordBO.setValue(value);
/*     */     
/*  85 */     int pos = getInsertPos(recordBO);
/*  86 */     record.saveValue(value);
/*  87 */     if (record.rank == pos || record.rank == pos - 1) {
/*  88 */       return pos;
/*     */     }
/*  90 */     if (record.rank > pos) {
/*     */       
/*  92 */       for (int i = record.rank; i > pos; i--) {
/*  93 */         GuildRecord r = this.list.get(i - 1);
/*  94 */         this.list.set(i, r);
/*  95 */         r.rank = i;
/*     */       } 
/*  97 */       this.list.set(pos, record);
/*  98 */       record.rank = pos;
/*     */     } else {
/*     */       
/* 101 */       for (int i = record.rank; i < pos - 1; i++) {
/* 102 */         GuildRecord r = this.list.get(i + 1);
/* 103 */         this.list.set(i, r);
/* 104 */         r.rank = i;
/*     */       } 
/* 106 */       this.list.set(pos - 1, record);
/* 107 */       record.rank = pos - 1;
/*     */     } 
/* 109 */     return record.rank;
/*     */   }
/*     */   
/*     */   public long getValue(long ownerid) {
/* 113 */     GuildRecord record = this.map.get(Long.valueOf(ownerid));
/* 114 */     if (record != null) {
/* 115 */       return record.recordBO.getValue();
/*     */     }
/* 117 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   private int insert(GuildRecord record) {
/* 122 */     int pos = getInsertPos(record.recordBO);
/* 123 */     this.list.add(pos, record);
/* 124 */     record.rank = pos;
/*     */ 
/*     */     
/* 127 */     for (int i = pos + 1; i < this.list.size(); i++) {
/* 128 */       ((GuildRecord)this.list.get(i)).rank = i;
/*     */     }
/* 130 */     return pos;
/*     */   }
/*     */   
/*     */   protected GuildRankRecordBO createRecordBO(long ownerid, long value) {
/* 134 */     GuildRankRecordBO recordBO = new GuildRankRecordBO();
/* 135 */     recordBO.setType(this.type.ordinal());
/* 136 */     recordBO.setOwner(ownerid);
/* 137 */     recordBO.setValue(value);
/* 138 */     recordBO.setUpdateTime(CommTime.nowSecond());
/* 139 */     recordBO.insert();
/* 140 */     return recordBO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getInsertPos(GuildRankRecordBO recordBo) {
/* 148 */     int left = 0, right = this.list.size();
/* 149 */     while (right - left > 1) {
/* 150 */       int m = (left + right) / 2;
/* 151 */       GuildRankRecordBO mid = ((GuildRecord)this.list.get(m)).recordBO;
/* 152 */       if (this.comparator.compare(mid, recordBo) < 0) {
/* 153 */         left = m; continue;
/* 154 */       }  if (this.comparator.compare(mid, recordBo) > 0) {
/* 155 */         right = m;
/*     */         
/*     */         continue;
/*     */       } 
/* 159 */       left = right = m;
/*     */     } 
/*     */     
/* 162 */     return left + 1;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 166 */     int deleteTime = CommTime.nowSecond() - 604800;
/*     */     
/* 168 */     String delsql = String.format("DELETE from rank_record where value=0 and type=%d and updateTime<%d;", new Object[] { Integer.valueOf(this.type.ordinal()), Integer.valueOf(deleteTime) });
/* 169 */     SQLExecutor.execute(delsql, DBCons.getDBFactory());
/*     */     
/* 171 */     String updatesql = String.format("UPDATE rank_record SET value=0, updateTime=%d where type=%d;", new Object[] { Integer.valueOf(CommTime.nowSecond()), Integer.valueOf(this.type.ordinal()) });
/* 172 */     SQLExecutor.executeUpdate(updatesql, DBCons.getDBFactory());
/*     */     
/* 174 */     this.list.clear();
/* 175 */     this.list.add(null);
/* 176 */     List<GuildRecord> todel = new ArrayList<>();
/* 177 */     for (GuildRecord record : this.map.values()) {
/* 178 */       if (record.recordBO.getUpdateTime() < deleteTime) {
/* 179 */         todel.add(record);
/*     */         continue;
/*     */       } 
/* 182 */       record.rank = 0;
/* 183 */       record.recordBO.setValue(0L);
/*     */     } 
/* 185 */     for (GuildRecord record : todel) {
/* 186 */       this.map.remove(Long.valueOf(record.recordBO.getOwner()));
/*     */     }
/*     */   }
/*     */   
/*     */   public int add(long ownerid, long value) {
/* 191 */     GuildRecord record = this.map.get(Long.valueOf(ownerid));
/* 192 */     if (record == null) {
/* 193 */       GuildRankRecordBO recordBO = createRecordBO(ownerid, value);
/* 194 */       this.map.put(Long.valueOf(ownerid), record = new GuildRecord(recordBO));
/* 195 */       record.rank = 0;
/*     */     } 
/*     */     
/* 198 */     if (record.rank != 0) {
/* 199 */       return setValue(record, record.getValue() + value);
/*     */     }
/* 201 */     return insert(record);
/*     */   }
/*     */ 
/*     */   
/*     */   public int minus(long ownerid, int value) {
/* 206 */     GuildRecord record = this.map.get(Long.valueOf(ownerid));
/* 207 */     if (record == null) {
/* 208 */       return 0;
/*     */     }
/*     */     
/* 211 */     return setValue(record, Math.max(0L, record.getValue() - value));
/*     */   }
/*     */   
/*     */   synchronized void del(long ownerid) {
/* 215 */     GuildRecord record = this.map.get(Long.valueOf(ownerid));
/* 216 */     if (record != null) {
/* 217 */       this.map.remove(Long.valueOf(ownerid));
/* 218 */       this.list.remove(record);
/* 219 */       for (int i = record.rank; i < this.list.size(); i++) {
/* 220 */         if (this.list.get(i) != null)
/* 221 */           ((GuildRecord)this.list.get(i)).rank = i; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract boolean filter(long paramLong);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/GuildRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */