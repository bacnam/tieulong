/*     */ package business.player.feature.treasure;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.Quality;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefTreasure;
/*     */ import core.config.refdata.ref.RefUniformItem;
/*     */ import core.database.game.bo.PlayerFindTreasureBO;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ public class FindTreasureFeature
/*     */   extends Feature
/*     */ {
/*     */   public PlayerFindTreasureBO findTreasure;
/*     */   
/*     */   public FindTreasureFeature(Player player) {
/*  27 */     super(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  35 */     this.findTreasure = (PlayerFindTreasureBO)BM.getBM(PlayerFindTreasureBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */   
/*     */   public PlayerFindTreasureBO getOrCreate() {
/*  39 */     PlayerFindTreasureBO bo = this.findTreasure;
/*  40 */     if (bo != null) {
/*  41 */       return bo;
/*     */     }
/*  43 */     synchronized (this) {
/*  44 */       bo = this.findTreasure;
/*  45 */       if (bo != null) {
/*  46 */         return bo;
/*     */       }
/*  48 */       bo = new PlayerFindTreasureBO();
/*  49 */       bo.setPid(this.player.getPid());
/*  50 */       bo.setTotal(0);
/*  51 */       bo.insert();
/*  52 */       this.findTreasure = bo;
/*     */     } 
/*  54 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RefTreasure selectRef(int level) {
/*  64 */     for (RefTreasure ref : RefDataMgr.getAll(RefTreasure.class).values()) {
/*  65 */       if (ref.LevelRange.within(level)) {
/*  66 */         return ref;
/*     */       }
/*     */     } 
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward findTen() {
/*  79 */     PlayerFindTreasureBO findTreasure = getOrCreate();
/*  80 */     findTreasure.saveTentimes(this.findTreasure.getTentimes() + 1);
/*  81 */     RefTreasure ref = selectRef(this.player.getLv());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     ArrayList<Integer> weightList = ref.FixedTenWeightList;
/*  87 */     ArrayList<Integer> idList = ref.FixedTenIdList;
/*  88 */     ArrayList<Integer> countList = ref.FixedTenCountList;
/*  89 */     Reward reward = null;
/*  90 */     for (int i = 0; i < 9; i++) {
/*  91 */       int j = CommMath.getRandomIndexByRate(weightList);
/*  92 */       int k = ((Integer)idList.get(j)).intValue();
/*  93 */       int m = ((Integer)countList.get(j)).intValue();
/*  94 */       if (reward != null) {
/*  95 */         reward.add(k, m);
/*     */       } else {
/*  97 */         reward = new Reward(k, m);
/*     */       } 
/*     */ 
/*     */       
/* 101 */       RefUniformItem refUniformItem = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(k));
/* 102 */       if (refUniformItem.Quality == Quality.Red.ordinal() && refUniformItem.Type == PrizeType.Equip) {
/* 103 */         NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.FindTreasure, new String[] { this.player.getName(), refUniformItem.Name });
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 108 */     int index = CommMath.getRandomIndexByRate(ref.LeastTenWeightList);
/* 109 */     int uniformID = ((Integer)ref.LeastIdList.get(index)).intValue();
/* 110 */     int count = ((Integer)ref.LeastTenCountList.get(index)).intValue();
/*     */ 
/*     */     
/* 113 */     RefUniformItem refUniform = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
/* 114 */     if (refUniform.Quality == Quality.Red.ordinal() && refUniform.Type == PrizeType.Equip) {
/* 115 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.FindTreasure, new String[] { this.player.getName(), refUniform.Name });
/*     */     }
/* 117 */     reward.add(uniformID, count);
/* 118 */     reward.combine(getExtReward(10));
/*     */     
/* 120 */     return reward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward find() {
/*     */     ArrayList<Integer> weightList, idList, countList;
/* 130 */     PlayerFindTreasureBO findTreasure = getOrCreate();
/* 131 */     findTreasure.saveTimes(this.findTreasure.getTimes() + 1);
/*     */     
/* 133 */     RefTreasure ref = selectRef(this.player.getLv());
/*     */     
/* 135 */     int total = 0;
/* 136 */     total = getOrCreate().getTotal();
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (total >= 9) {
/* 141 */       weightList = ref.LeastTenWeightList;
/* 142 */       idList = ref.LeastIdList;
/* 143 */       countList = ref.LeastTenCountList;
/*     */     } else {
/* 145 */       weightList = ref.NormalWeightList;
/* 146 */       idList = ref.NormalIdList;
/* 147 */       countList = ref.NormalCountList;
/*     */     } 
/* 149 */     int index = CommMath.getRandomIndexByRate(weightList);
/* 150 */     int uniformID = ((Integer)idList.get(index)).intValue();
/* 151 */     int count = ((Integer)countList.get(index)).intValue();
/* 152 */     getOrCreate().saveTotal(total + 1);
/* 153 */     if (getOrCreate().getTotal() == 10) {
/* 154 */       getOrCreate().saveTotal(0);
/*     */     }
/* 156 */     Reward reward = new Reward(uniformID, count);
/* 157 */     reward.combine(getExtReward(1));
/*     */ 
/*     */     
/* 160 */     RefUniformItem refUniform = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
/* 161 */     if (refUniform.Quality == Quality.Red.ordinal() && refUniform.Type == PrizeType.Equip) {
/* 162 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.FindTreasure, new String[] { this.player.getName(), refUniform.Name });
/*     */     }
/*     */     
/* 165 */     return reward;
/*     */   }
/*     */ 
/*     */   
/*     */   public Reward findTreasure(int times) {
/* 170 */     ActivityMgr.updateWorldRank(this.player, times, RankType.WorldTreasure);
/*     */     
/* 172 */     if (times == 1) {
/* 173 */       return find();
/*     */     }
/* 175 */     if (times == 10) {
/* 176 */       return findTen();
/*     */     }
/*     */     
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward getExtReward(int times) {
/* 189 */     RefTreasure ref = selectRef(this.player.getLv());
/* 190 */     Reward reward = new Reward();
/* 191 */     for (int i = 0; i < times; i++) {
/* 192 */       reward.add(ref.UniformId, ref.UniformCount);
/*     */     }
/* 194 */     return reward;
/*     */   }
/*     */   
/*     */   public int getLeftTimes(ConstEnum.FindTreasureType find) {
/* 198 */     if (find == ConstEnum.FindTreasureType.single) {
/* 199 */       int times = RefDataMgr.getFactor("findTreasureTimes", 20);
/* 200 */       if (times < 0) {
/* 201 */         return times;
/*     */       }
/* 203 */       return times - getOrCreate().getTimes();
/*     */     } 
/* 205 */     if (find == ConstEnum.FindTreasureType.Ten) {
/* 206 */       int times = RefDataMgr.getFactor("findTreasureTentimes", 2);
/* 207 */       if (times < 0) {
/* 208 */         return times;
/*     */       }
/* 210 */       return RefDataMgr.getFactor("findTreasureTentimes", 2) - getOrCreate().getTentimes();
/*     */     } 
/*     */     
/* 213 */     return 0;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/treasure/FindTreasureFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */