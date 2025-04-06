/*     */ package business.global.rank;
/*     */ 
/*     */ import BaseCommon.CommClass;
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.rank.catagere.GuildRank;
/*     */ import business.global.rank.catagere.NormalRank;
/*     */ import business.player.Player;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import core.database.game.bo.RankRecordBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RankManager
/*     */ {
/*  21 */   private static RankManager instance = new RankManager(); private Map<RankType, Rank> ranks;
/*     */   
/*     */   public static RankManager getInstance() {
/*  24 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RankManager() {
/*  30 */     this.ranks = new HashMap<>();
/*     */   }
/*     */   public void init() {
/*  33 */     Set<Class<?>> clazzs = CommClass.getClasses(NormalRank.class.getPackage().getName());
/*  34 */     for (Class<?> clz : clazzs) {
/*     */       
/*     */       try {
/*  37 */         Class<? extends Rank> clazz = (Class)clz;
/*  38 */         Ranks types = clazz.<Ranks>getAnnotation(Ranks.class);
/*  39 */         if (types == null || (types.value()).length == 0) {
/*  40 */           CommLog.error("[{}]的类型没有相关排行榜类型，请检查", clazz.getSimpleName());
/*  41 */           System.exit(0);
/*     */         }  byte b; int i; RankType[] arrayOfRankType;
/*  43 */         for (i = (arrayOfRankType = types.value()).length, b = 0; b < i; ) { RankType type = arrayOfRankType[b];
/*  44 */           if (this.ranks.containsKey(type)) {
/*  45 */             String preClass = ((Rank)this.ranks.get(type)).getClass().getSimpleName();
/*  46 */             CommLog.error("[{},{}]重复定义[{}]类型的排行榜", preClass, clazz.getSimpleName());
/*  47 */             System.exit(0);
/*     */           } 
/*  49 */           Rank instance = clazz.getConstructor(new Class[] { RankType.class }).newInstance(new Object[] { type });
/*  50 */           this.ranks.put(type, instance); b++; }
/*     */       
/*  52 */       } catch (Exception e) {
/*  53 */         CommLog.error("排行榜[{}]初始化失败", clz.getSimpleName(), e);
/*  54 */         System.exit(0);
/*     */       } 
/*     */     } 
/*  57 */     List<RankRecordBO> list = BM.getBM(RankRecordBO.class).findAll();
/*  58 */     for (RankRecordBO recordBO : list) {
/*  59 */       RankType type = RankType.values()[recordBO.getType()];
/*  60 */       Rank rank = this.ranks.get(type);
/*  61 */       Record record = new Record(recordBO);
/*  62 */       rank.map.put(Long.valueOf(recordBO.getOwner()), record);
/*     */     } 
/*  64 */     for (Rank rank : this.ranks.values()) {
/*  65 */       rank.resort();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Record> getRankList(RankType type, int size) {
/*  74 */     List<Record> records, list = (getRank(type)).list;
/*  75 */     if (list.size() > size + 1) {
/*  76 */       records = list.subList(0, size + 1);
/*     */     } else {
/*  78 */       records = new ArrayList<>(list);
/*     */     } 
/*  80 */     return records;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPlayerId(RankType type, int rank) {
/*     */     int num;
/*  88 */     List<Record> records = (getRank(type)).list;
/*     */     
/*  90 */     if (rank < 1 || records.size() < 2) {
/*  91 */       return 0L;
/*     */     }
/*  93 */     if (records.size() - 1 < rank) {
/*  94 */       num = records.size() - 1;
/*     */     } else {
/*  96 */       num = rank;
/*     */     } 
/*     */     
/*  99 */     return ((Record)records.get(num)).getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRank(RankType type, long ownerid) {
/* 104 */     return getRank(type).getRank(ownerid);
/*     */   }
/*     */   
/*     */   public long getValue(RankType type, long ownerid) {
/* 108 */     return getRank(type).getValue(ownerid);
/*     */   }
/*     */   
/*     */   public int update(RankType type, long ownerid, long value) {
/* 112 */     return getRank(type).update(ownerid, value, new long[0]);
/*     */   }
/*     */   
/*     */   public int update(RankType type, long ownerid, long value, long... ext) {
/* 116 */     return getRank(type).update(ownerid, value, ext);
/*     */   }
/*     */   
/*     */   public int minus(RankType type, long ownerid, int value) {
/* 120 */     return getRank(type).minus(ownerid, value);
/*     */   }
/*     */   
/*     */   private Rank getRank(RankType type) {
/* 124 */     Rank rank = this.ranks.get(type);
/* 125 */     if (rank == null) {
/* 126 */       CommLog.error("排行榜[]未注册", type);
/*     */     }
/* 128 */     return rank;
/*     */   }
/*     */   
/*     */   public int getRankSize(RankType type) {
/* 132 */     Rank rank = this.ranks.get(type);
/* 133 */     if (rank == null) {
/* 134 */       CommLog.error("排行榜[]未注册", type);
/*     */     }
/* 136 */     return rank.list.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void settle(RankType type) {
/* 143 */     settle(type, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void settle(RankType type, boolean clear) {
/*     */     try {
/* 151 */       Rank rank = getRank(type);
/* 152 */       rank.sendReward();
/* 153 */       if (clear) {
/* 154 */         rank.clear();
/*     */       }
/* 156 */     } catch (Exception e) {
/*     */       
/* 158 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear(RankType type) {
/* 166 */     getRank(type).clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearPlayerData(Player player) {
/* 173 */     BM.getBM(RankRecordBO.class).delAll("owner", Long.valueOf(player.getPid()));
/* 174 */     Ranks types = NormalRank.class.<Ranks>getAnnotation(Ranks.class); byte b; int i; RankType[] arrayOfRankType;
/* 175 */     for (i = (arrayOfRankType = types.value()).length, b = 0; b < i; ) { RankType type = arrayOfRankType[b];
/* 176 */       ((Rank)this.ranks.get(type)).del(player.getPid());
/*     */       b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearPlayerData(Player player, RankType type) {
/* 184 */     ((Rank)this.ranks.get(type)).del(player.getPid());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearGuildDataById(long ownerid) {
/* 191 */     BM.getBM(RankRecordBO.class).delAll("owner", Long.valueOf(ownerid));
/* 192 */     Ranks types = GuildRank.class.<Ranks>getAnnotation(Ranks.class); byte b; int i; RankType[] arrayOfRankType;
/* 193 */     for (i = (arrayOfRankType = types.value()).length, b = 0; b < i; ) { RankType type = arrayOfRankType[b];
/* 194 */       ((Rank)this.ranks.get(type)).del(ownerid);
/*     */       b++; }
/*     */   
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/rank/RankManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */