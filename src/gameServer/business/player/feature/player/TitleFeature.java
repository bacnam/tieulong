/*     */ package business.player.feature.player;
/*     */ 
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.Title;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefTitle;
/*     */ import core.database.game.bo.TitleBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TitleFeature
/*     */   extends Feature
/*     */ {
/*  24 */   public final Map<Title, TitleIns> titleMap = new HashMap<>();
/*     */   
/*  26 */   private List<TitleIns> oldData = new ArrayList<>();
/*     */   
/*     */   public TitleFeature(Player owner) {
/*  29 */     super(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  34 */     List<TitleBO> titleList = BM.getBM(TitleBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  35 */     for (TitleBO bo : titleList) {
/*  36 */       RefTitle refData = (RefTitle)RefDataMgr.get(RefTitle.class, Integer.valueOf(bo.getTitleId()));
/*  37 */       if (refData == null) {
/*  38 */         bo.del();
/*     */         
/*     */         continue;
/*     */       } 
/*  42 */       TitleIns info = new TitleIns(bo, refData);
/*  43 */       this.titleMap.put(refData.Type, info);
/*     */       
/*  45 */       if (info.bo.getIsActive() && !info.bo.getIsReward())
/*  46 */         this.oldData.add(info); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class TitleIns
/*     */   {
/*     */     public TitleBO bo;
/*     */     public RefTitle refData;
/*     */     
/*     */     public TitleIns(TitleBO bo, RefTitle refData) {
/*  56 */       this.bo = bo;
/*  57 */       this.refData = refData;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TitleIns getOrCreate(Title type) {
/*  68 */     TitleIns ret = this.titleMap.get(type);
/*  69 */     if (ret == null) {
/*  70 */       synchronized (this.titleMap) {
/*  71 */         RefTitle ref = RefTitle.getTitleByType(type);
/*  72 */         if (ref == null) {
/*  73 */           return null;
/*     */         }
/*  75 */         TitleBO bo = new TitleBO();
/*  76 */         bo.setTitleId(ref.id);
/*  77 */         bo.setPid(this.player.getPid());
/*  78 */         bo.insert();
/*  79 */         ret = new TitleIns(bo, ref);
/*  80 */         this.titleMap.put(type, ret);
/*     */       } 
/*     */     }
/*  83 */     return ret;
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
/*     */   public void update0(Title type, IUpdateTitle iUpdate, Integer... args) {
/*  97 */     TitleIns ins = getOrCreate(type);
/*  98 */     if (ins == null) {
/*     */       return;
/*     */     }
/*     */     
/* 102 */     if (ins.bo.getIsActive()) {
/*     */       return;
/*     */     }
/* 105 */     iUpdate.update(ins.bo, ins.refData, args);
/* 106 */     ins.bo.getIsActive();
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
/*     */   public void updateInc(Title type, Integer value) {
/* 119 */     update0(type, (bo, ref, values) -> { int addCount = values[0].intValue(); synchronized (bo) { long count = bo.getValue(); bo.saveValue(count + addCount); taskActive(bo, ref); }  }new Integer[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 127 */           value
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateInc(Title type) {
/* 137 */     updateInc(type, Integer.valueOf(1));
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
/*     */   public void updateMax(Title type, Integer value) {
/* 149 */     update0(type, (bo, ref, values) -> { int newvalue = values[0].intValue(); synchronized (bo) { long count = bo.getValue(); bo.saveValue(Math.max(count, newvalue)); taskActive(bo, ref); }  }new Integer[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 157 */           value
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateMin(Title type, Integer value) {
/* 167 */     update0(type, (bo, ref, values) -> { int newvalue = values[0].intValue(); synchronized (bo) { long count = bo.getValue(); if (count == 0L) count = newvalue;  bo.saveValue(Math.min(count, newvalue)); taskActive(bo, ref); }  }new Integer[] {
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
/* 178 */           value });
/*     */   }
/*     */   
/*     */   private void taskActive(TitleBO bo, RefTitle ref) {
/* 182 */     if (ref.NumRange.within((int)bo.getValue())) {
/* 183 */       bo.saveActiveTime(CommTime.getTodayZeroClockS());
/* 184 */       bo.saveIsActive(true);
/*     */       
/* 186 */       if (ref.id == 6 && bo.getIsActive()) {
/* 187 */         NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.Richman, new String[] { this.player.getName() });
/*     */       }
/*     */       
/* 190 */       MailCenter.getInstance().sendMail(this.player.getPid(), ref.MailId, new String[0]);
/* 191 */       bo.saveIsReward(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TitleIns getTitleByType(Title type) {
/* 196 */     TitleIns ins = this.titleMap.get(type);
/* 197 */     return ins;
/*     */   }
/*     */   
/*     */   public void checkAllTitle() {
/* 201 */     if (this.oldData.size() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 205 */     for (TitleIns ins : this.oldData) {
/* 206 */       MailCenter.getInstance().sendMail(this.player.getPid(), ins.refData.MailId, new String[0]);
/* 207 */       ins.bo.saveIsReward(true);
/*     */     } 
/* 209 */     this.oldData.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetAchievement(ConstEnum.AchieveReset resetType) {
/* 218 */     synchronized (this.titleMap) {
/* 219 */       for (TitleIns achieve : this.titleMap.values()) {
/* 220 */         if (achieve.refData.Reset != resetType) {
/*     */           continue;
/*     */         }
/* 223 */         achieve.bo.setActiveTime(0);
/* 224 */         achieve.bo.setIsActive(false);
/* 225 */         achieve.bo.setIsReward(false);
/* 226 */         achieve.bo.setValue(0L);
/* 227 */         achieve.bo.saveAll();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/* 237 */       resetAchievement(ConstEnum.AchieveReset.EveryDay);
/* 238 */     } catch (Exception e) {
/*     */       
/* 240 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/player/TitleFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */