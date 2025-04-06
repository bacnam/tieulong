/*     */ package business.player.feature.task;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.FetchStatus;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefDailyActive;
/*     */ import core.database.game.bo.DailyactiveBO;
/*     */ import core.network.proto.TaskActive;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskActivityFeature
/*     */   extends Feature
/*     */ {
/*     */   public DailyactiveBO dailyactiveBO;
/*     */   
/*     */   public TaskActivityFeature(Player owner) {
/*  28 */     super(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  33 */     this.dailyactiveBO = (DailyactiveBO)BM.getBM(DailyactiveBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DailyactiveBO getOrCreate() {
/*  42 */     DailyactiveBO dailyactiveBO = this.dailyactiveBO;
/*  43 */     if (dailyactiveBO == null) {
/*  44 */       dailyactiveBO = new DailyactiveBO();
/*  45 */       dailyactiveBO.setPid(this.player.getPid());
/*  46 */       dailyactiveBO.setValue(0);
/*  47 */       dailyactiveBO.setTeamLevel(this.player.getPlayerBO().getLv());
/*  48 */       dailyactiveBO.insert();
/*  49 */       this.dailyactiveBO = dailyactiveBO;
/*     */     } 
/*  51 */     return dailyactiveBO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDailyActive(int value) {
/*  60 */     DailyactiveBO dailyactiveBO = getOrCreate();
/*  61 */     dailyactiveBO.saveValue(dailyactiveBO.getValue() + value);
/*  62 */     pushTaskActiveInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pushTaskActiveInfo() {
/*  70 */     DailyactiveBO dailyactiveBO = getOrCreate();
/*  71 */     TaskActive builder = new TaskActive();
/*  72 */     int refId = ((Integer)RefDailyActive.level2refId.get(Integer.valueOf(dailyactiveBO.getTeamLevel()))).intValue();
/*  73 */     builder.setId(refId);
/*  74 */     builder.setValue(dailyactiveBO.getValue());
/*     */     
/*  76 */     List<Integer> fetchedTaskIndex = StringUtils.string2Integer(dailyactiveBO.getFetchedTaskIndex());
/*  77 */     RefDailyActive refDailyActive = (RefDailyActive)RefDataMgr.get(RefDailyActive.class, Integer.valueOf(refId));
/*  78 */     for (int i = 0; i < refDailyActive.Condition.size(); i++) {
/*  79 */       if (fetchedTaskIndex.contains(Integer.valueOf(i))) {
/*  80 */         builder.stepStatus.add(Integer.valueOf(FetchStatus.Fetched.ordinal()));
/*     */       } else {
/*     */         
/*  83 */         Integer needValue = refDailyActive.Condition.get(i);
/*  84 */         if (dailyactiveBO.getValue() >= needValue.intValue())
/*  85 */         { builder.stepStatus.add(Integer.valueOf(FetchStatus.Can.ordinal())); }
/*     */         else
/*     */         
/*  88 */         { builder.stepStatus.add(Integer.valueOf(FetchStatus.Cannot.ordinal())); } 
/*     */       } 
/*  90 */     }  this.player.pushProto("taskActiveInfo", builder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/*  99 */       DailyactiveBO dailyactiveBO = getOrCreate();
/* 100 */       dailyactiveBO.setValue(0);
/* 101 */       dailyactiveBO.setTeamLevel(this.player.getPlayerBO().getLv());
/* 102 */       dailyactiveBO.setFetchedTaskIndex("");
/* 103 */       dailyactiveBO.saveAll();
/* 104 */       pushTaskActiveInfo();
/* 105 */     } catch (Exception e) {
/*     */       
/* 107 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/task/TaskActivityFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */