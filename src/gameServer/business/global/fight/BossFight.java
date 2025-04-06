/*     */ package business.global.fight;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.RankDungeon;
/*     */ import business.global.arena.ArenaConfig;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.pve.DungeonFeature;
/*     */ import business.player.item.Reward;
/*     */ import business.player.item.UniformItem;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefBuff;
/*     */ import core.config.refdata.ref.RefDungeon;
/*     */ import core.config.refdata.ref.RefDungeonRebirth;
/*     */ import core.config.refdata.ref.RefMonster;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.config.refdata.ref.RefSkill;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BossFight
/*     */   extends Fight<Reward>
/*     */ {
/*     */   private Player player;
/*     */   private Map<Integer, Fighter> attackers;
/*     */   private int level;
/*     */   
/*     */   public BossFight(Player player, int level) {
/*  38 */     this.player = player;
/*  39 */     if (level <= 0) {
/*  40 */       this.level = player.getPlayerBO().getDungeonLv();
/*     */     } else {
/*  42 */       this.level = level;
/*     */     } 
/*  44 */     this.attackers = ((CharFeature)player.getFeature(CharFeature.class)).getFighters();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Reward onLost() {
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Reward onWin() {
/*  55 */     DungeonFeature dungeon = (DungeonFeature)this.player.getFeature(DungeonFeature.class);
/*     */ 
/*     */     
/*  58 */     Reward reward = new Reward();
/*  59 */     for (int i = dungeon.getLevel(); i < this.level + 1; i++) {
/*     */       
/*  61 */       RefDungeon ref = (RefDungeon)RefDataMgr.get(RefDungeon.class, Integer.valueOf(i));
/*  62 */       reward.combine(((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.BossDrop))).genReward());
/*     */       
/*  64 */       dungeon.nextDungeon();
/*     */     } 
/*  66 */     Reward newreward = new Reward();
/*  67 */     for (UniformItem item : reward.merge()) {
/*  68 */       newreward.add(item);
/*     */     }
/*  70 */     ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(newreward, ItemFlow.Dungeon_BossWin);
/*  71 */     dungeon.DungeonUp();
/*     */ 
/*     */     
/*  74 */     RankManager.getInstance().update(RankType.Dungeon, this.player.getPid(), this.player.getPlayerBO().getDungeonLv());
/*     */     
/*  76 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
/*  77 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M1, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
/*  78 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M2, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
/*  79 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M3, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
/*  80 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M4, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
/*  81 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M5, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
/*  82 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M6, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
/*  83 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M7, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
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
/*     */ 
/*     */     
/* 101 */     ((RankDungeon)ActivityMgr.getActivity(RankDungeon.class)).UpdateMaxRequire_cost(this.player, this.player.getPlayerBO().getDungeonLv());
/* 102 */     return newreward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Integer, Fighter> getDeffenders() {
/* 107 */     Map<Integer, Fighter> rtn = new HashMap<>();
/*     */     
/* 109 */     RefDungeon ref = (RefDungeon)RefDataMgr.get(RefDungeon.class, Integer.valueOf(this.level));
/* 110 */     RefMonster refMonster = (RefMonster)RefDataMgr.get(RefMonster.class, Integer.valueOf(ref.BossID));
/*     */     
/* 112 */     Fighter fighter = new Fighter();
/* 113 */     fighter.id = ref.BossID;
/*     */     int i;
/* 115 */     for (i = 0; i < refMonster.SkillList.size(); i++) {
/* 116 */       RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, refMonster.SkillList.get(i));
/* 117 */       fighter.skills.put(Integer.valueOf(skill.id), skill);
/* 118 */       fighter.skillsLv.put(Integer.valueOf(skill.id), Integer.valueOf(0));
/*     */     } 
/* 120 */     for (i = 0; i < refMonster.BuffList.size(); i++) {
/* 121 */       RefBuff refBuff = (RefBuff)RefDataMgr.get(RefBuff.class, refMonster.BuffList.get(i));
/* 122 */       Buff buff = new Buff(refBuff, 0);
/* 123 */       buff.time = (refBuff.Time * 1000);
/* 124 */       buff.cd = (refBuff.CD * 1000);
/* 125 */       fighter.buffs.put(Integer.valueOf(buff.id), buff);
/*     */     } 
/*     */     
/* 128 */     fighter.putAttr(Attribute.MaxHP, refMonster.MaxHP);
/* 129 */     fighter.putAttr(Attribute.ATK, refMonster.ATK);
/* 130 */     fighter.putAttr(Attribute.DEF, refMonster.DEF);
/* 131 */     fighter.putAttr(Attribute.RGS, refMonster.RGS);
/* 132 */     fighter.putAttr(Attribute.Hit, refMonster.Hit);
/* 133 */     fighter.putAttr(Attribute.Dodge, refMonster.Dodge);
/* 134 */     fighter.putAttr(Attribute.Critical, refMonster.Critical);
/* 135 */     fighter.putAttr(Attribute.Tenacity, refMonster.Tenacity);
/*     */     
/* 137 */     fighter.hp = fighter.attr(Attribute.MaxHP);
/* 138 */     rtn.put(Integer.valueOf(ref.BossID), fighter);
/* 139 */     return rtn;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Integer, Fighter> getAttackers() {
/* 144 */     return this.attackers;
/*     */   }
/*     */ 
/*     */   
/*     */   public int fightTime() {
/* 149 */     return ArenaConfig.fightTime() + 90;
/*     */   }
/*     */   
/*     */   public void initAttr() {
/* 153 */     RefDungeonRebirth ref = ((DungeonFeature)this.player.getFeature(DungeonFeature.class)).getRebirthRef(this.level);
/* 154 */     if (ref != null) {
/* 155 */       for (int i = 0; i < ref.AttrList.size(); i++) {
/* 156 */         Attribute attr = ref.AttrList.get(i);
/* 157 */         int value = ((Integer)ref.AttrValue.get(i)).intValue();
/* 158 */         for (Fighter fight : this.attackers.values()) {
/* 159 */           double finalvalue = ((Double)fight.attrs.get(attr)).doubleValue() * (100 + value) / 100.0D;
/* 160 */           fight.putAttr(attr, finalvalue);
/*     */         } 
/*     */       } 
/* 163 */       ((DungeonFeature)this.player.getFeature(DungeonFeature.class)).removeRebirthRef(this.level);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/BossFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */