/*     */ package business.player.feature.player;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.UnlockType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefLingBao;
/*     */ import core.config.refdata.ref.RefUnlockFunction;
/*     */ import core.database.game.bo.LingbaoBO;
/*     */ 
/*     */ public class LingBaoFeature
/*     */   extends Feature
/*     */ {
/*     */   LingbaoBO bo;
/*     */   
/*     */   public LingBaoFeature(Player player) {
/*  23 */     super(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  28 */     this.bo = (LingbaoBO)BM.getBM(LingbaoBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */   
/*     */   public LingbaoBO getOrCreate() {
/*  32 */     LingbaoBO bo = this.bo;
/*  33 */     if (bo != null) {
/*  34 */       return bo;
/*     */     }
/*  36 */     synchronized (this) {
/*  37 */       bo = this.bo;
/*  38 */       if (bo != null) {
/*  39 */         return bo;
/*     */       }
/*  41 */       bo = new LingbaoBO();
/*  42 */       bo.setPid(this.player.getPid());
/*  43 */       bo.insert();
/*  44 */       this.bo = bo;
/*     */     } 
/*  46 */     return bo;
/*     */   }
/*     */ 
/*     */   
/*     */   public int levelUp() throws WSException {
/*  51 */     RefUnlockFunction.checkUnlock(this.player, UnlockType.LingBao);
/*     */     
/*  53 */     LingbaoBO bo = getOrCreate();
/*     */ 
/*     */     
/*  56 */     int level = bo.getLevel();
/*  57 */     if (level >= RefDataMgr.getAll(RefLingBao.class).size() - 1) {
/*  58 */       throw new WSException(ErrorCode.LingBao_Full, "灵宝等级已满");
/*     */     }
/*     */     
/*  61 */     RefLingBao ref = (RefLingBao)RefDataMgr.get(RefLingBao.class, Integer.valueOf(level + 1));
/*  62 */     RefLingBao now_ref = (RefLingBao)RefDataMgr.get(RefLingBao.class, Integer.valueOf(level));
/*  63 */     int gainExp = 0;
/*     */     
/*  65 */     if (now_ref.Level != ref.Level) {
/*  66 */       bo.saveLevel(bo.getLevel() + 1);
/*  67 */       ((CharFeature)this.player.getFeature(CharFeature.class)).updateCharPower();
/*  68 */       return 0;
/*     */     } 
/*     */ 
/*     */     
/*  72 */     boolean check = ((PlayerItem)this.player.getFeature(PlayerItem.class)).checkAndConsume(ref.CostIdList, ref.CostCountList, ItemFlow.LingBao);
/*  73 */     if (!check) {
/*  74 */       throw new WSException(ErrorCode.LingBao_NotEnough, "材料不足");
/*     */     }
/*  76 */     gainExp = RefDataMgr.getFactor("LingBaoExp", 10);
/*  77 */     int nowExp = bo.getExp() + gainExp;
/*  78 */     int levelUp = 0;
/*  79 */     for (int i = ref.id; i <= RefDataMgr.size(RefLingBao.class); i++) {
/*  80 */       RefLingBao temp_ref = (RefLingBao)RefDataMgr.get(RefLingBao.class, Integer.valueOf(i - 1));
/*  81 */       RefLingBao next_ref = (RefLingBao)RefDataMgr.get(RefLingBao.class, Integer.valueOf(i));
/*     */       
/*  83 */       if (next_ref == null)
/*     */         break; 
/*  85 */       long needExp = next_ref.Exp;
/*     */       
/*  87 */       if (needExp > nowExp) {
/*     */         break;
/*     */       }
/*  90 */       if (temp_ref.Level != next_ref.Level) {
/*     */         break;
/*     */       }
/*  93 */       levelUp++;
/*  94 */       nowExp = (int)(nowExp - needExp);
/*     */     } 
/*  96 */     bo.saveExp(nowExp);
/*  97 */     bo.saveLevel(level + levelUp);
/*     */     
/*  99 */     ((CharFeature)this.player.getFeature(CharFeature.class)).updateCharPower();
/*     */     
/* 101 */     return gainExp;
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 105 */     return getOrCreate().getLevel();
/*     */   }
/*     */   
/*     */   public int getExp() {
/* 109 */     return getOrCreate().getExp();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/player/LingBaoFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */