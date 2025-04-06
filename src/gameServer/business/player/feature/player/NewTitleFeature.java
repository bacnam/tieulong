/*     */ package business.player.feature.player;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefNewTitle;
/*     */ import core.config.refdata.ref.RefNewTitleLevel;
/*     */ import core.database.game.bo.NewtitleBO;
/*     */ import core.network.proto.TitleInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class NewTitleFeature extends Feature {
/*     */   public Map<Integer, NewtitleBO> titleMap;
/*     */   private NewtitleBO usingTitle;
/*     */   
/*     */   public NewTitleFeature(Player player) {
/*  27 */     super(player);
/*     */ 
/*     */ 
/*     */     
/*  31 */     this.titleMap = Maps.newConcurrentMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  37 */     List<NewtitleBO> titleList = BM.getBM(NewtitleBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  38 */     for (NewtitleBO bo : titleList) {
/*  39 */       Map<Integer, RefNewTitleLevel> refData = RefNewTitleLevel.getTitleByType(bo.getTitleId());
/*  40 */       if (refData == null) {
/*  41 */         bo.del();
/*     */         continue;
/*     */       } 
/*  44 */       this.titleMap.put(Integer.valueOf(bo.getTitleId()), bo);
/*  45 */       if (bo.getIsUsing()) {
/*  46 */         this.usingTitle = bo;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public TitleInfo upgradeTitle(int titleId) throws WSException {
/*  52 */     NewtitleBO bo = this.titleMap.get(Integer.valueOf(titleId));
/*  53 */     if (bo == null) {
/*  54 */       throw new WSException(ErrorCode.Title_NotFound, "称号没找到");
/*     */     }
/*  56 */     Map<Integer, RefNewTitleLevel> refmap = RefNewTitleLevel.getTitleByType(bo.getTitleId());
/*  57 */     if (refmap == null) {
/*  58 */       throw new WSException(ErrorCode.Title_NotFound, "未找到称号");
/*     */     }
/*  60 */     if (bo.getLevel() >= refmap.values().size()) {
/*  61 */       throw new WSException(ErrorCode.Title_AlreadyFull, "称号已满级");
/*     */     }
/*  63 */     RefNewTitleLevel ref = refmap.get(Integer.valueOf(bo.getLevel() + 1));
/*  64 */     if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).checkAndConsume(ref.UniformIdList, ref.UniformCountList, ItemFlow.TitleUpgrade)) {
/*  65 */       throw new WSException(ErrorCode.NotEnough_Currency, "材料不足");
/*     */     }
/*  67 */     bo.saveLevel(bo.getLevel() + 1);
/*     */     
/*  69 */     ((CharFeature)this.player.getFeature(CharFeature.class)).updateCharPower();
/*  70 */     return new TitleInfo(bo);
/*     */   }
/*     */   
/*     */   public TitleInfo activeTitle(int titleId) throws WSException {
/*  74 */     if (this.titleMap.get(Integer.valueOf(titleId)) != null) {
/*  75 */       throw new WSException(ErrorCode.Title_AlreadyActive, "称号已激活");
/*     */     }
/*  77 */     RefNewTitle ref = (RefNewTitle)RefDataMgr.get(RefNewTitle.class, Integer.valueOf(titleId));
/*  78 */     if (ref == null) {
/*  79 */       throw new WSException(ErrorCode.Title_NotFound, "称号没找到");
/*     */     }
/*  81 */     if (this.player.getLv() < ref.level) {
/*  82 */       throw new WSException(ErrorCode.Title_NotFound, "激活等级不足");
/*     */     }
/*  84 */     if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).checkAndConsume(ref.ActiveId, ref.ActiveCount, ItemFlow.TitleActive)) {
/*  85 */       throw new WSException(ErrorCode.NotEnough_Currency, "激活材料不足");
/*     */     }
/*  87 */     NewtitleBO newbo = new NewtitleBO();
/*  88 */     newbo.setPid(this.player.getPid());
/*  89 */     newbo.setTitleId(ref.id);
/*  90 */     newbo.setLevel(1);
/*  91 */     newbo.setActiveTime(CommTime.nowSecond());
/*  92 */     newbo.insert();
/*  93 */     this.titleMap.put(Integer.valueOf(ref.id), newbo);
/*  94 */     ((CharFeature)this.player.getFeature(CharFeature.class)).updateCharPower();
/*  95 */     return new TitleInfo(newbo);
/*     */   }
/*     */   
/*     */   public TitleInfo useTitle(int titleId) throws WSException {
/*  99 */     NewtitleBO bo = this.titleMap.get(Integer.valueOf(titleId));
/* 100 */     if (bo == null) {
/* 101 */       throw new WSException(ErrorCode.Title_NotFound, "称号没找到");
/*     */     }
/* 103 */     NewtitleBO old = this.usingTitle;
/* 104 */     if (old != null) {
/* 105 */       old.saveIsUsing(false);
/*     */     }
/* 107 */     this.usingTitle = bo;
/* 108 */     bo.saveIsUsing(true);
/* 109 */     return new TitleInfo(bo);
/*     */   }
/*     */   
/*     */   public List<TitleInfo> getAllTitleInfo() {
/* 113 */     List<TitleInfo> list = new ArrayList<>();
/* 114 */     for (NewtitleBO bo : this.titleMap.values()) {
/* 115 */       list.add(new TitleInfo(bo));
/*     */     }
/*     */     
/* 118 */     return list;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/player/NewTitleFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */