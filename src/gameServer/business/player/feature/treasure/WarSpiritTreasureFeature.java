/*     */ package business.player.feature.treasure;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.item.Reward;
/*     */ import business.player.item.UniformItem;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefTreasureWarspirit;
/*     */ import core.config.refdata.ref.RefUniformItem;
/*     */ import core.config.refdata.ref.RefWarSpirit;
/*     */ import core.database.game.bo.PlayerFindTreasureBO;
/*     */ import core.database.game.bo.WarSpiritTreasureRecordBO;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class WarSpiritTreasureFeature
/*     */   extends Feature
/*     */ {
/*     */   public PlayerFindTreasureBO findTreasure;
/*     */   
/*     */   public WarSpiritTreasureFeature(Player player) {
/*  24 */     super(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  32 */     this.findTreasure = (PlayerFindTreasureBO)BM.getBM(PlayerFindTreasureBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */   
/*     */   public PlayerFindTreasureBO getOrCreate() {
/*  36 */     PlayerFindTreasureBO bo = this.findTreasure;
/*  37 */     if (bo != null) {
/*  38 */       return bo;
/*     */     }
/*  40 */     synchronized (this) {
/*  41 */       bo = this.findTreasure;
/*  42 */       if (bo != null) {
/*  43 */         return bo;
/*     */       }
/*  45 */       bo = new PlayerFindTreasureBO();
/*  46 */       bo.setPid(this.player.getPid());
/*  47 */       bo.setTotal(0);
/*  48 */       bo.insert();
/*  49 */       this.findTreasure = bo;
/*     */     } 
/*  51 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RefTreasureWarspirit selectRef(int level) {
/*  61 */     for (RefTreasureWarspirit ref : RefDataMgr.getAll(RefTreasureWarspirit.class).values()) {
/*  62 */       if (ref.LevelRange.within(level)) {
/*  63 */         return ref;
/*     */       }
/*     */     } 
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward findTen() {
/*  76 */     RefTreasureWarspirit ref = selectRef(this.player.getLv());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     ArrayList<Integer> weightList = ref.FixedTenWeightList;
/*  82 */     ArrayList<Integer> idList = ref.FixedTenIdList;
/*  83 */     ArrayList<Integer> countList = ref.FixedTenCountList;
/*  84 */     Reward reward = null;
/*  85 */     for (int i = 0; i < 9; i++) {
/*  86 */       int j = CommMath.getRandomIndexByRate(weightList);
/*  87 */       int k = ((Integer)idList.get(j)).intValue();
/*  88 */       int m = ((Integer)countList.get(j)).intValue();
/*  89 */       if (reward != null) {
/*  90 */         reward.add(k, m);
/*     */       } else {
/*  92 */         reward = new Reward(k, m);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  97 */     int index = CommMath.getRandomIndexByRate(ref.LeastTenWeightList);
/*  98 */     int uniformID = ((Integer)ref.LeastIdList.get(index)).intValue();
/*  99 */     int count = ((Integer)ref.LeastTenCountList.get(index)).intValue();
/*     */     
/* 101 */     reward.add(uniformID, count);
/*     */     
/* 103 */     return reward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward find() {
/*     */     ArrayList<Integer> weightList, idList, countList;
/* 114 */     RefTreasureWarspirit ref = selectRef(this.player.getLv());
/*     */     
/* 116 */     int total = 0;
/* 117 */     total = getOrCreate().getWarspiritTotal();
/*     */ 
/*     */ 
/*     */     
/* 121 */     if (total >= 9) {
/* 122 */       weightList = ref.LeastTenWeightList;
/* 123 */       idList = ref.LeastIdList;
/* 124 */       countList = ref.LeastTenCountList;
/*     */     } else {
/* 126 */       weightList = ref.NormalWeightList;
/* 127 */       idList = ref.NormalIdList;
/* 128 */       countList = ref.NormalCountList;
/*     */     } 
/* 130 */     int index = CommMath.getRandomIndexByRate(weightList);
/* 131 */     int uniformID = ((Integer)idList.get(index)).intValue();
/* 132 */     int count = ((Integer)countList.get(index)).intValue();
/* 133 */     getOrCreate().saveWarspiritTotal(total + 1);
/* 134 */     if (getOrCreate().getWarspiritTotal() == 10) {
/* 135 */       getOrCreate().saveWarspiritTotal(0);
/*     */     }
/* 137 */     Reward reward = new Reward(uniformID, count);
/*     */     
/* 139 */     return reward;
/*     */   }
/*     */   
/*     */   public Reward findTreasure(int times) {
/* 143 */     Reward reward = null;
/* 144 */     if (times == 1) {
/* 145 */       reward = find();
/*     */     }
/* 147 */     if (times == 10) {
/* 148 */       reward = findTen();
/*     */     }
/* 150 */     if (reward != null) {
/* 151 */       for (UniformItem item : reward) {
/* 152 */         RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(item.getUniformId()));
/* 153 */         RefWarSpirit refwarspirit = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(ref.ItemID));
/* 154 */         if (refwarspirit != null) {
/* 155 */           WarSpiritTreasureRecordBO bo = new WarSpiritTreasureRecordBO();
/* 156 */           bo.savePid(this.player.getPid());
/* 157 */           bo.setSpiritId(refwarspirit.id);
/* 158 */           bo.setTime(CommTime.nowSecond());
/* 159 */           bo.insert();
/* 160 */           WarSpiritTreasureRecord.getInstance().add(bo);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 165 */     return reward;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/treasure/WarSpiritTreasureFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */