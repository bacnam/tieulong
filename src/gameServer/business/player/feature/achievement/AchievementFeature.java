/*     */ package business.player.feature.achievement;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.features.RechargeFeature;
/*     */ import business.player.feature.task.TaskActivityFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefAchievement;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.database.game.bo.AchievementBO;
/*     */ import core.database.game.bo.UnlockRewardBO;
/*     */ import core.network.proto.AchieveInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AchievementFeature
/*     */   extends Feature
/*     */ {
/*     */   public UnlockRewardBO unlockRewardBO;
/*     */   public Map<Achievement.AchievementType, AchievementIns> achievemap;
/*     */   
/*     */   public AchievementFeature(Player owner) {
/*  40 */     super(owner);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     this.achievemap = Maps.newMap();
/*     */   }
/*     */   
/*     */   public void loadDB() {
/*  50 */     List<AchievementBO> achieveList = BM.getBM(AchievementBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  51 */     for (AchievementBO bo : achieveList) {
/*  52 */       RefAchievement refData = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(bo.getAchieveId()));
/*  53 */       if (refData == null) {
/*  54 */         bo.del();
/*     */         continue;
/*     */       } 
/*  57 */       AchievementIns info = new AchievementIns(bo, refData);
/*  58 */       this.achievemap.put(refData.AchieveName, info);
/*     */     } 
/*  60 */     this.unlockRewardBO = (UnlockRewardBO)BM.getBM(UnlockRewardBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*  61 */     if (this.unlockRewardBO == null) {
/*  62 */       this.unlockRewardBO = new UnlockRewardBO();
/*  63 */       this.unlockRewardBO.setPid(this.player.getPid());
/*  64 */       this.unlockRewardBO.insert();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class AchievementIns {
/*     */     public AchievementBO bo;
/*     */     public RefAchievement refData;
/*     */     
/*     */     public AchievementIns(AchievementBO bo, RefAchievement refData) {
/*  73 */       this.bo = bo;
/*  74 */       this.refData = refData;
/*     */     }
/*     */   }
/*     */   
/*     */   public AchieveInfo build(AchievementBO achieveBo) {
/*  79 */     AchieveInfo res = new AchieveInfo();
/*  80 */     res.setAchieveId(achieveBo.getAchieveId());
/*  81 */     res.setCompleteCount(achieveBo.getCompleteCount());
/*  82 */     res.setAchieveCount(achieveBo.getAchieveCount());
/*  83 */     res.setArgument1(achieveBo.getArgument());
/*  84 */     res.setArgument2(achieveBo.getArgument2());
/*  85 */     res.setArgument3(achieveBo.getArgument3());
/*  86 */     res.setGainPrizeList(StringUtils.string2Integer(achieveBo.getGainPrizeList()));
/*  87 */     return res;
/*     */   }
/*     */   
/*     */   public List<AchieveInfo> loadAchieveList() {
/*  91 */     checkMonthCard(Achievement.AchievementType.MonthCardCrystal);
/*  92 */     checkMonthCard(Achievement.AchievementType.YearCardCrystal);
/*  93 */     List<AchieveInfo> achieveInfoList = new ArrayList<>();
/*  94 */     for (AchievementIns ins : this.achievemap.values()) {
/*  95 */       achieveInfoList.add(build(ins.bo));
/*     */     }
/*  97 */     return achieveInfoList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnlockRewardBO getUnlockRewardBO() {
/* 106 */     return this.unlockRewardBO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AchievementIns getOrCreate(Achievement.AchievementType type) {
/* 116 */     AchievementIns ret = this.achievemap.get(type);
/* 117 */     if (ret == null) {
/* 118 */       synchronized (this.achievemap) {
/* 119 */         RefAchievement ref = RefAchievement.getByType(type);
/* 120 */         if (ref == null) {
/* 121 */           return null;
/*     */         }
/* 123 */         AchievementBO bo = new AchievementBO();
/* 124 */         bo.setAchieveId(ref.id);
/* 125 */         bo.setPid(this.player.getPid());
/* 126 */         bo.insert();
/* 127 */         ret = new AchievementIns(bo, ref);
/* 128 */         this.achievemap.put(type, ret);
/*     */       } 
/*     */     }
/* 131 */     return ret;
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
/*     */ 
/*     */   
/*     */   public void update0(Achievement.AchievementType type, IUpdateAchievement iUpdate, Integer... args) {
/* 145 */     AchievementIns ins = getOrCreate(type);
/* 146 */     if (ins == null) {
/*     */       return;
/*     */     }
/*     */     
/* 150 */     if (ins.refData.IsHide) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     if (ins.refData.FirstArgsList.size() <= ins.bo.getAchieveCount()) {
/*     */       return;
/*     */     }
/* 163 */     int oldvalue = ins.bo.getCompleteCount();
/* 164 */     iUpdate.update(ins.bo, ins.refData, args);
/* 165 */     if (oldvalue != ins.bo.getCompleteCount()) {
/* 166 */       this.player.pushProto("UpdateAchieve", build(ins.bo));
/*     */     }
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
/*     */   public void updateInc(Achievement.AchievementType type, Integer value) {
/* 179 */     update0(type, (bo, ref, values) -> { AchievementIns ins = this.achievemap.get(paramAchievementType); if (ins.refData.ConditionPreTaskId != 0) { RefAchievement pre_ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(ins.refData.ConditionPreTaskId)); AchievementIns pre_ins = this.achievemap.get(pre_ref.AchieveName); if (pre_ins == null) return;  List<Integer> pre_gainList = StringUtils.string2Integer(pre_ins.bo.getGainPrizeList()); if (pre_gainList.size() < pre_ref.PrizeIDList.size()) return;  }  int addCount = values[0].intValue(); synchronized (bo) { int count = bo.getCompleteCount(); bo.saveCompleteCount(count + addCount); taskChieveCount(bo, ref); }  }new Integer[] {
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
/*     */           
/* 198 */           value
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateInc(Achievement.AchievementType type) {
/* 208 */     updateInc(type, Integer.valueOf(1));
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
/*     */   public void updateMax(Achievement.AchievementType type, Integer value) {
/* 220 */     update0(type, (bo, ref, values) -> { int newvalue = values[0].intValue(); synchronized (bo) { int count = bo.getCompleteCount(); bo.saveCompleteCount(Math.max(count, newvalue)); taskChieveCount(bo, ref); }  }new Integer[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 228 */           value
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateMin(Achievement.AchievementType type, Integer value) {
/* 238 */     update0(type, (bo, ref, values) -> { int newvalue = values[0].intValue(); synchronized (bo) { int count = bo.getCompleteCount(); if (count == 0) count = newvalue;  bo.saveCompleteCount(Math.min(count, newvalue)); int i = bo.getAchieveCount(); while (i < ref.FirstArgsList.size() && bo.getCompleteCount() <= ((Integer)ref.FirstArgsList.get(i)).intValue()) bo.saveAchieveCount(++i);  }  }new Integer[] {
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
/* 254 */           value
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearValue(Achievement.AchievementType type) {
/* 263 */     update0(type, (bo, ref, values) -> { synchronized (bo) { bo.saveCompleteCount(0); }  }new Integer[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/* 275 */       resetAchievement(ConstEnum.AchieveReset.EveryDay);
/* 276 */       this.player.isOnline();
/*     */     }
/* 278 */     catch (Exception e) {
/*     */       
/* 280 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void weekRefresh() {
/* 288 */     resetAchievement(ConstEnum.AchieveReset.EveryWeek);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetAchievement(ConstEnum.AchieveReset resetType) {
/* 297 */     synchronized (this.achievemap) {
/* 298 */       for (AchievementIns achieve : this.achievemap.values()) {
/* 299 */         if (achieve.refData.Reset != resetType) {
/*     */           continue;
/*     */         }
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
/* 312 */         if (achieve.refData.AchieveName == Achievement.AchievementType.SkillUp) {
/* 313 */           int level = 0;
/* 314 */           for (Character charc : ((CharFeature)this.player.getFeature(CharFeature.class)).getAll().values()) {
/* 315 */             for (Iterator<Integer> iterator = charc.getBo().getSkillAll().iterator(); iterator.hasNext(); ) { int skill = ((Integer)iterator.next()).intValue();
/* 316 */               level += skill; }
/*     */           
/*     */           } 
/* 319 */           if (level >= 1485) {
/* 320 */             achieve.bo.setGainPrizeList("");
/* 321 */             achieve.bo.saveAll();
/* 322 */             this.player.pushProto("UpdateAchieve", build(achieve.bo));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 337 */         achieve.bo.setAchieveCount(0);
/* 338 */         achieve.bo.setCompleteCount(0);
/* 339 */         achieve.bo.setGainPrizeList("");
/* 340 */         achieve.bo.saveAll();
/* 341 */         this.player.pushProto("UpdateAchieve", build(achieve.bo));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward cmd_pickUpActivityPrize(int achieveID, int achieveCount) throws WSException {
/*     */     Reward pack;
/* 355 */     RefAchievement ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(achieveID));
/* 356 */     AchievementIns ins = this.achievemap.get(ref.AchieveName);
/* 357 */     if (ins == null) {
/* 358 */       throw new WSException(ErrorCode.NotFound_Achievement, "没有找到该成就列表 cid:%s,achieveID:%s", new Object[] { Long.valueOf(this.player.getPid()), Integer.valueOf(achieveID) });
/*     */     }
/* 360 */     List<Integer> gainList = StringUtils.string2Integer(ins.bo.getGainPrizeList());
/* 361 */     if (gainList.contains(Integer.valueOf(achieveCount))) {
/* 362 */       throw new WSException(ErrorCode.ALREADY_FETCH, "已经领取了该阶段的奖励");
/*     */     }
/* 364 */     int achieveIndex = achieveCount;
/* 365 */     int maxIndex = ins.refData.FirstArgsList.size();
/*     */     
/* 367 */     if (achieveIndex >= maxIndex) {
/* 368 */       throw new WSException(ErrorCode.NotEnough_CompleteLevel, "achieveIndex%s>=ins.refData.FirstArgsList.size():%s", new Object[] { Integer.valueOf(achieveIndex), Integer.valueOf(maxIndex) });
/*     */     }
/*     */     
/* 371 */     int curCnt = ins.bo.getCompleteCount();
/* 372 */     int needCnt = ((Integer)ins.refData.FirstArgsList.get(achieveIndex)).intValue();
/* 373 */     if (isRankAchievement(ref.AchieveName)) {
/* 374 */       if (curCnt > needCnt) {
/* 375 */         throw new WSException(ErrorCode.NotEnough_CompleteCount, "奖励还没达到领取条件cid:%s,achieveID:%s,index:%s,curCnt:%s,needCnt:%s", new Object[] { Long.valueOf(this.player.getPid()), 
/* 376 */               Integer.valueOf(achieveID), Integer.valueOf(achieveIndex), Integer.valueOf(curCnt), Integer.valueOf(needCnt) });
/*     */       }
/*     */     }
/* 379 */     else if (curCnt < needCnt) {
/* 380 */       throw new WSException(ErrorCode.NotEnough_CompleteCount, "奖励还没达到领取条件cid:%s,achieveID:%s,index:%s,curCnt:%s,needCnt:%s", new Object[] { Long.valueOf(this.player.getPid()), 
/* 381 */             Integer.valueOf(achieveID), Integer.valueOf(achieveIndex), Integer.valueOf(curCnt), Integer.valueOf(needCnt) });
/*     */     } 
/*     */     
/* 384 */     int prizeID = ((Integer)ins.refData.PrizeIDList.get(achieveIndex)).intValue();
/* 385 */     Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(prizeID))).genReward();
/*     */     
/* 387 */     if (reward != null) {
/* 388 */       pack = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Achievement);
/*     */     } else {
/* 390 */       pack = new Reward();
/*     */     } 
/* 392 */     gainList.add(Integer.valueOf(achieveCount));
/* 393 */     if (ins.refData.AchieveName == Achievement.AchievementType.MonthCardCrystal) {
/* 394 */       ins.bo.saveCompleteCount(ins.bo.getCompleteCount() - 1);
/*     */     }
/* 396 */     ins.bo.saveGainPrizeList(StringUtils.list2String(gainList));
/*     */     
/* 398 */     this.player.pushProto("UpdateAchieve", build(ins.bo));
/* 399 */     return pack;
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
/*     */   
/*     */   public boolean isRankAchievement(Achievement.AchievementType type) {
/* 412 */     return false;
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
/*     */   public Reward cmd_pickUpPrize(int achieveID, int achieveCount) throws WSException {
/* 424 */     RefAchievement ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(achieveID));
/* 425 */     AchievementIns ins = this.achievemap.get(ref.AchieveName);
/* 426 */     if (ins == null) {
/* 427 */       throw new WSException(ErrorCode.NotFound_Achievement, "没有找到该成就列表 cid:%s,achieveID : %s", new Object[] { Long.valueOf(this.player.getPid()), Integer.valueOf(achieveID) });
/*     */     }
/* 429 */     List<Integer> gainList = StringUtils.string2Integer(ins.bo.getGainPrizeList());
/* 430 */     if (gainList.contains(Integer.valueOf(achieveCount))) {
/* 431 */       throw new WSException(ErrorCode.ALREADY_FETCH, "已经领取了该阶段的奖励");
/*     */     }
/* 433 */     if (achieveCount >= ins.refData.FirstArgsList.size() || achieveCount < 0) {
/* 434 */       throw new WSException(ErrorCode.InvalidParam, "客户端发来的参数错误,achieveCount不能<0||achieveCount>任务数量");
/*     */     }
/* 436 */     if (achieveCount >= ins.bo.getAchieveCount()) {
/* 437 */       throw new WSException(ErrorCode.NotEnough_CompleteCount, "奖励还没达到领取条件 ");
/*     */     }
/* 439 */     if (ref.ConditionPreTaskId != 0) {
/* 440 */       RefAchievement pre_ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(ref.ConditionPreTaskId));
/* 441 */       AchievementIns pre_ins = this.achievemap.get(pre_ref.AchieveName);
/* 442 */       if (pre_ins == null) {
/* 443 */         throw new WSException(ErrorCode.NotFound_Achievement, "没有找到前置成就列表 cid:%s,achieveID : %s", new Object[] { Long.valueOf(this.player.getPid()), Integer.valueOf(ref.ConditionPreTaskId) });
/*     */       }
/* 445 */       List<Integer> pre_gainList = StringUtils.string2Integer(pre_ins.bo.getGainPrizeList());
/*     */       
/* 447 */       if (pre_gainList.size() < pre_ref.PrizeIDList.size()) {
/* 448 */         throw new WSException(ErrorCode.NotEnough_CompleteCount, "前置任务没有完成 ");
/*     */       }
/*     */     } 
/*     */     
/* 452 */     if (ref.TaskType == ConstEnum.TaskClassify.DailyTask) {
/* 453 */       TaskActivityFeature taskActivityFeature = (TaskActivityFeature)this.player.getFeature(TaskActivityFeature.class);
/* 454 */       taskActivityFeature.addDailyActive(ins.refData.ActiveScore);
/*     */     } 
/*     */     
/* 457 */     int prizeID = ((Integer)ins.refData.PrizeIDList.get(achieveCount)).intValue();
/* 458 */     Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(prizeID))).genReward();
/* 459 */     Reward pack = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Achievement);
/* 460 */     gainList.add(Integer.valueOf(achieveCount));
/* 461 */     if (ins.refData.AchieveName == Achievement.AchievementType.MonthCardCrystal) {
/* 462 */       ins.bo.saveCompleteCount(ins.bo.getCompleteCount() - 1);
/*     */     }
/* 464 */     ins.bo.saveGainPrizeList(StringUtils.list2String(gainList));
/* 465 */     this.player.pushProto("UpdateAchieve", build(ins.bo));
/* 466 */     return pack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshSevenDay() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkMonthCard(Achievement.AchievementType type) {
/* 477 */     AchievementIns ins = getOrCreate(type);
/* 478 */     if (ins == null) {
/*     */       return;
/*     */     }
/*     */     
/* 482 */     int achieveIndex = ins.bo.getAchieveCount();
/* 483 */     int maxIndex = ins.refData.FirstArgsList.size();
/*     */     
/* 485 */     if (achieveIndex >= maxIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 489 */     RechargeFeature rechargeFeature = (RechargeFeature)this.player.getFeature(RechargeFeature.class);
/* 490 */     int dayNum = rechargeFeature.getRebateRemains(type);
/* 491 */     if (dayNum == 0 || dayNum < -1) {
/*     */       return;
/*     */     }
/* 494 */     int count = ins.bo.getCompleteCount();
/* 495 */     ins.bo.saveCompleteCount(dayNum);
/* 496 */     ins.bo.saveAchieveCount(count + 1);
/* 497 */     this.player.pushProto("UpdateAchieve", build(ins.bo));
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkAchieve(Achievement.AchievementType type) {
/* 502 */     AchievementIns ins = getOrCreate(type);
/* 503 */     if (ins == null) {
/*     */       return;
/*     */     }
/*     */     
/* 507 */     if (this.player.getPlayerBO().getLv() < ins.refData.Condition) {
/*     */       return;
/*     */     }
/* 510 */     long now = CommTime.nowMS();
/* 511 */     if (now >= CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(0)).intValue()) && now < CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(1)).intValue())) {
/* 512 */       int count = ins.bo.getCompleteCount();
/* 513 */       ins.bo.saveAchieveCount(count + 1);
/* 514 */       this.player.pushProto("UpdateAchieve", build(ins.bo));
/* 515 */     } else if (now >= CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(1)).intValue())) {
/* 516 */       if (StringUtils.string2Integer(ins.bo.getGainPrizeList()).size() != 0) {
/*     */         return;
/*     */       }
/* 519 */       ins.bo.saveAchieveCount(0);
/* 520 */       this.player.pushProto("UpdateAchieve", build(ins.bo));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openGetEnergy(Achievement.AchievementType type) {
/* 525 */     AchievementIns ins = getOrCreate(type);
/*     */     
/* 527 */     if (this.player.getPlayerBO().getLv() < ins.refData.Condition) {
/*     */       return;
/*     */     }
/*     */     
/* 531 */     long now = CommTime.nowMS();
/* 532 */     if (now < CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(0)).intValue()) || now > CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(1)).intValue())) {
/*     */       return;
/*     */     }
/* 535 */     int count = ins.bo.getCompleteCount();
/* 536 */     ins.bo.saveAchieveCount(count + 1);
/* 537 */     this.player.pushProto("UpdateAchieve", build(ins.bo));
/*     */   }
/*     */   
/*     */   public void closeGetEnergy(Achievement.AchievementType type) {
/* 541 */     AchievementIns ins = getOrCreate(type);
/*     */     
/* 543 */     if (this.player.getPlayerBO().getLv() < ins.refData.Condition) {
/*     */       return;
/*     */     }
/* 546 */     if (StringUtils.string2Integer(ins.bo.getGainPrizeList()).size() != 0) {
/*     */       return;
/*     */     }
/* 549 */     ins.bo.saveAchieveCount(0);
/* 550 */     this.player.pushProto("UpdateAchieve", build(ins.bo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void effectActivation(int cardId) throws WSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void taskChieveCount(AchievementBO bo, RefAchievement ref) {
/* 568 */     for (int i = bo.getAchieveCount(); i < ref.FirstArgsList.size() && 
/* 569 */       bo.getCompleteCount() >= ((Integer)ref.FirstArgsList.get(i)).intValue();)
/*     */     {
/*     */       
/* 572 */       bo.saveAchieveCount(++i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/achievement/AchievementFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */