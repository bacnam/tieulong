/*     */ package business.global.arena;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.player.Player;
/*     */ import business.player.RobotManager;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRankReward;
/*     */ import core.database.game.bo.ArenaCompetitorBO;
/*     */ import core.database.game.bo.ArenaFightRecordBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArenaManager
/*     */ {
/*  33 */   private static ArenaManager instance = new ArenaManager();
/*     */   
/*     */   public static ArenaManager getInstance() {
/*  36 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int Max_Record = 20;
/*     */   
/*  42 */   private Map<Long, Competitor> competitors = new HashMap<>();
/*  43 */   private List<Competitor> rank = new ArrayList<>();
/*     */   
/*     */   public List<Competitor> getRank() {
/*  46 */     return this.rank;
/*     */   }
/*     */   
/*     */   public void setRank(List<Competitor> rank) {
/*  50 */     this.rank = rank;
/*     */   }
/*     */ 
/*     */   
/*  54 */   private Map<Long, Queue<ArenaFightRecordBO>> records = new HashMap<>();
/*     */ 
/*     */   
/*     */   public void init() {
/*  58 */     List<ArenaCompetitorBO> bos = BM.getBM(ArenaCompetitorBO.class).findAll();
/*  59 */     for (ArenaCompetitorBO bo : bos) {
/*  60 */       this.competitors.put(Long.valueOf(bo.getPid()), new Competitor(bo));
/*     */     }
/*  62 */     resortRanks();
/*     */ 
/*     */     
/*  65 */     List<Player> robots = Lists.newArrayList(RobotManager.getInstance().getAll().values());
/*  66 */     robots.sort((o1, o2) -> o2.getPlayerBO().getMaxFightPower() - o1.getPlayerBO().getMaxFightPower());
/*     */ 
/*     */     
/*  69 */     for (Player player : robots) {
/*  70 */       if (player.getLv() > RefDataMgr.getFactor("ArenaMaxLevel", 89)) {
/*     */         continue;
/*     */       }
/*  73 */       getOrCreate(player.getPid());
/*     */     } 
/*     */ 
/*     */     
/*  77 */     List<ArenaFightRecordBO> records = BM.getBM(ArenaFightRecordBO.class).findAll();
/*  78 */     records.sort((left, right) -> left.getEndTime() - right.getEndTime());
/*     */ 
/*     */     
/*  81 */     int cur = CommTime.nowSecond();
/*  82 */     for (ArenaFightRecordBO bo : records) {
/*     */       
/*  84 */       if (cur > bo.getBeginTime() + 1209600) {
/*  85 */         bo.del(); continue;
/*     */       } 
/*  87 */       addFightRecord(bo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Competitor getOrCreate(long pid) {
/*  92 */     Competitor competitor = this.competitors.get(Long.valueOf(pid));
/*  93 */     if (competitor != null) {
/*  94 */       return competitor;
/*     */     }
/*  96 */     synchronized (this.competitors) {
/*  97 */       competitor = this.competitors.get(Long.valueOf(pid));
/*  98 */       if (competitor != null) {
/*  99 */         return competitor;
/*     */       }
/* 101 */       ArenaCompetitorBO bo = new ArenaCompetitorBO();
/* 102 */       bo.setPid(pid);
/* 103 */       bo.setRank(this.competitors.size() + 1);
/* 104 */       bo.setHighestRank(bo.getRank());
/* 105 */       bo.setLastRankTime(CommTime.nowSecond());
/* 106 */       bo.insert();
/* 107 */       competitor = new Competitor(bo);
/* 108 */       this.competitors.put(Long.valueOf(pid), competitor);
/*     */     } 
/* 110 */     synchronized (this.rank) {
/* 111 */       this.rank.add(competitor);
/*     */     } 
/* 113 */     return competitor;
/*     */   }
/*     */   
/*     */   public void addFightRecord(ArenaFightRecordBO record) {
/* 117 */     synchronized (this.records) {
/* 118 */       addFightRecord(record, record.getAtkPid());
/* 119 */       addFightRecord(record, record.getDefPid());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addFightRecord(ArenaFightRecordBO record, long pid) {
/* 124 */     Queue<ArenaFightRecordBO> queue = this.records.get(Long.valueOf(pid));
/* 125 */     if (queue == null) {
/* 126 */       this.records.put(Long.valueOf(pid), queue = new LinkedList<>());
/*     */     }
/* 128 */     queue.add(record);
/* 129 */     if (queue.size() > 20) {
/* 130 */       tryDel(queue.poll());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryDel(ArenaFightRecordBO bo) {
/* 138 */     Queue<ArenaFightRecordBO> atkBos = this.records.get(Long.valueOf(bo.getAtkPid()));
/* 139 */     Queue<ArenaFightRecordBO> defBos = this.records.get(Long.valueOf(bo.getDefPid()));
/* 140 */     if ((atkBos == null || !atkBos.contains(bo)) && (defBos == null || !defBos.contains(bo))) {
/* 141 */       bo.del();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resortRanks() {
/* 150 */     Collection<Competitor> curCompetitors = new ArrayList<>();
/* 151 */     synchronized (this.competitors) {
/* 152 */       curCompetitors.addAll(this.competitors.values());
/*     */     } 
/* 154 */     List<Competitor> ranklist = new ArrayList<>();
/* 155 */     List<Competitor> idlelist = new ArrayList<>();
/*     */     
/* 157 */     for (int index = 1; index <= this.competitors.size(); index++) {
/* 158 */       ranklist.add((Competitor)null);
/*     */     }
/*     */     
/* 161 */     for (Competitor competitor : curCompetitors) {
/* 162 */       int rank = competitor.bo.getRank();
/*     */       
/* 164 */       if (rank <= 0 || rank > this.competitors.size()) {
/* 165 */         idlelist.add(competitor);
/*     */       }
/*     */ 
/*     */       
/* 169 */       Competitor preOne = ranklist.get(rank - 1);
/* 170 */       if (preOne == null) {
/* 171 */         ranklist.set(rank - 1, competitor);
/*     */         
/*     */         continue;
/*     */       } 
/* 175 */       if (competitor.bo.getLastRankTime() > preOne.bo.getLastRankTime()) {
/*     */         
/* 177 */         ranklist.set(rank - 1, competitor);
/* 178 */         idlelist.add(preOne); continue;
/*     */       } 
/* 180 */       idlelist.add(competitor);
/*     */     } 
/*     */ 
/*     */     
/* 184 */     if (idlelist.size() != 0) {
/*     */       
/* 186 */       idlelist.sort((left, right) -> (left.bo.getRank() != right.bo.getRank()) ? (left.bo.getRank() - right.bo.getRank()) : (right.bo.getLastRankTime() - left.bo.getLastRankTime()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 192 */       int next = 0;
/* 193 */       for (Competitor arenaInfo : idlelist) {
/* 194 */         while (next < ranklist.size()) {
/* 195 */           if (ranklist.get(next) != null) {
/*     */             next++; continue;
/*     */           } 
/* 198 */           ranklist.set(next, arenaInfo);
/* 199 */           int preRank = arenaInfo.bo.getRank();
/* 200 */           arenaInfo.bo.saveRank(next + 1);
/* 201 */           arenaInfo.bo.saveLastRankTime(CommTime.nowSecond());
/* 202 */           CommLog.warn("arena fix rank cid:{} preRank:{} newRank:{}", new Object[] { Long.valueOf(arenaInfo.bo.getPid()), Integer.valueOf(preRank), Integer.valueOf(arenaInfo.bo.getRank()) });
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 207 */     synchronized (this.rank) {
/* 208 */       this.rank = ranklist;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Competitor> getOpponents(int rank) {
/* 214 */     List<Competitor> list = new ArrayList<>();
/* 215 */     int OpponentsSize = ArenaConfig.opponentsSize();
/*     */     
/* 217 */     if (rank <= OpponentsSize) {
/* 218 */       for (int r = 1; r <= OpponentsSize + 1; r++) {
/* 219 */         if (r != rank)
/*     */         {
/* 221 */           list.add(this.rank.get(r - 1)); } 
/*     */       } 
/* 223 */     } else if (rank < 50) {
/* 224 */       List<Competitor> head = getCompetitors(rank - OpponentsSize * 2, rank, rank);
/* 225 */       if (head.size() > OpponentsSize - 1) {
/* 226 */         Collections.shuffle(head);
/* 227 */         list.addAll(head.subList(0, 4));
/*     */       } 
/* 229 */       int more = OpponentsSize - list.size();
/* 230 */       List<Competitor> tail = getCompetitors(rank + 1, rank + more * 2, rank);
/* 231 */       Collections.shuffle(tail);
/* 232 */       list.addAll(tail.subList(0, more));
/*     */     } else {
/* 234 */       int offset = ArenaConfig.opponentsOffset();
/* 235 */       int r = rank * 17 / 16;
/* 236 */       if (r <= this.rank.size()) {
/* 237 */         List<Competitor> tail = getCompetitors(r - offset, r + offset, rank);
/* 238 */         list.add((Competitor)CommMath.randomOne(tail));
/* 239 */         OpponentsSize--;
/*     */       } 
/*     */       
/* 242 */       for (int i = 1; i <= OpponentsSize; i++) {
/* 243 */         r = rank * (16 - i) / 16;
/* 244 */         List<Competitor> head = getCompetitors(r - offset, r + offset, rank);
/* 245 */         head.removeAll(list);
/* 246 */         list.add((Competitor)CommMath.randomOne(head));
/*     */       } 
/*     */     } 
/* 249 */     list.sort((left, right) -> left.bo.getRank() - right.bo.getRank());
/*     */ 
/*     */     
/* 252 */     return list;
/*     */   }
/*     */   
/*     */   private List<Competitor> getCompetitors(int begin, int end, int exclude) {
/* 256 */     List<Competitor> list = new ArrayList<>();
/* 257 */     int max = Math.min(end, this.rank.size());
/* 258 */     for (int r = Math.max(1, begin); r <= max; r++) {
/* 259 */       if (r != exclude)
/*     */       {
/*     */         
/* 262 */         list.add(this.rank.get(r - 1)); } 
/*     */     } 
/* 264 */     return list;
/*     */   }
/*     */   
/*     */   public List<Competitor> getRankList(int size) {
/* 268 */     synchronized (this.rank) {
/* 269 */       return this.rank.subList(0, Math.min(size, this.rank.size()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void swithRank(int r1, int r2) {
/* 274 */     Competitor competitor1 = this.rank.get(r1 - 1);
/* 275 */     Competitor competitor2 = this.rank.get(r2 - 1);
/*     */     
/* 277 */     competitor1.bo.setRank(r2);
/* 278 */     competitor2.bo.setRank(r1);
/* 279 */     int cur = CommTime.nowSecond();
/* 280 */     competitor1.bo.setLastRankTime(cur);
/* 281 */     competitor2.bo.setLastRankTime(cur);
/* 282 */     synchronized (this.rank) {
/* 283 */       this.rank.set(r2 - 1, competitor1);
/* 284 */       this.rank.set(r1 - 1, competitor2);
/*     */     } 
/* 286 */     competitor1.bo.saveAll();
/* 287 */     competitor2.bo.saveAll();
/* 288 */     competitor1.resetOpponents();
/* 289 */     competitor2.resetOpponents();
/*     */   }
/*     */   
/*     */   public Collection<ArenaFightRecordBO> getFightRecords(long pid) {
/* 293 */     return this.records.get(Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public void settle() {
/*     */     try {
/* 298 */       List<Competitor> ranklist = new ArrayList<>();
/* 299 */       synchronized (this.rank) {
/* 300 */         ranklist.addAll(this.rank);
/*     */       } 
/* 302 */       List<RefRankReward> rewards = RefRankReward.getRewards(RankType.Arena);
/* 303 */       for (RefRankReward ref : rewards) {
/* 304 */         for (int rnk = ref.MinRank; rnk <= ref.MaxRank && 
/* 305 */           rnk <= ranklist.size(); rnk++) {
/*     */ 
/*     */           
/* 308 */           Competitor r = ranklist.get(rnk - 1);
/* 309 */           if (r != null)
/*     */           {
/*     */             
/* 312 */             MailCenter.getInstance().sendMail(r.getPid(), ref.MailId, new String[] { (new StringBuilder(String.valueOf(rnk))).toString() }); } 
/*     */         } 
/*     */       } 
/* 315 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Long> randCompetitor(long pid, int size) {
/* 321 */     Competitor competitor = this.competitors.get(Long.valueOf(pid));
/* 322 */     int rank = (competitor == null) ? this.rank.size() : competitor.getRank();
/* 323 */     int offset = 50 + size * 2;
/* 324 */     List<Competitor> list = getCompetitors(rank - offset, rank + offset, rank);
/* 325 */     Collections.shuffle(list);
/* 326 */     return (List<Long>)list.subList(0, size).stream().map(c -> Long.valueOf(c.getPid())).collect(Collectors.toList());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/arena/ArenaManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */