/*      */ package business.player.feature;
/*      */ 
/*      */ import BaseCommon.CommLog;
/*      */ import business.global.activity.ActivityMgr;
/*      */ import business.global.activity.detail.AccumConsume;
/*      */ import business.player.Player;
/*      */ import business.player.PlayerMgr;
/*      */ import business.player.feature.guild.GuildMemberFeature;
/*      */ import com.zhonglian.server.common.enums.ConstEnum;
/*      */ import com.zhonglian.server.common.enums.PrizeType;
/*      */ import com.zhonglian.server.common.enums.RankType;
/*      */ import com.zhonglian.server.logger.flow.ItemFlow;
/*      */ import core.config.refdata.RefDataMgr;
/*      */ import core.config.refdata.ref.RefTeamExp;
/*      */ import core.config.refdata.ref.RefVIP;
/*      */ import core.database.game.bo.PlayerBO;
/*      */ import core.logger.flow.FlowLogger;
/*      */ import core.network.proto.Player;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PlayerCurrency
/*      */   extends Feature
/*      */ {
/*      */   public PlayerCurrency(Player data) {
/*   33 */     super(data);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadDB() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean check(PrizeType type, int count) {
/*   48 */     switch (type) {
/*      */       case Crystal:
/*   50 */         return (this.player.getPlayerBO().getCrystal() >= count);
/*      */       case Gold:
/*   52 */         return (this.player.getPlayerBO().getGold() >= count);
/*      */       case Exp:
/*   54 */         return (this.player.getPlayerBO().getExp() >= count);
/*      */       case StrengthenMaterial:
/*   56 */         return (this.player.getPlayerBO().getStrengthenMaterial() >= count);
/*      */       case GemMaterial:
/*   58 */         return (this.player.getPlayerBO().getGemMaterial() >= count);
/*      */       case StarMaterial:
/*   60 */         return (this.player.getPlayerBO().getStarMaterial() >= count);
/*      */       case MerMaterial:
/*   62 */         return (this.player.getPlayerBO().getMerMaterial() >= count);
/*      */       case WingMaterial:
/*   64 */         return (this.player.getPlayerBO().getWingMaterial() >= count);
/*      */       case null:
/*   66 */         return (this.player.getPlayerBO().getArenaToken() >= count);
/*      */       case EquipInstanceMaterial:
/*   68 */         return (this.player.getPlayerBO().getEquipInstanceMaterial() >= count);
/*      */       case GemInstanceMaterial:
/*   70 */         return (this.player.getPlayerBO().getGemInstanceMaterial() >= count);
/*      */       case MeridianInstanceMaterial:
/*   72 */         return (this.player.getPlayerBO().getMeridianInstanceMaterial() >= count);
/*      */       case RedPiece:
/*   74 */         return (this.player.getPlayerBO().getRedPiece() >= count);
/*      */       case ArtificeMaterial:
/*   76 */         return (this.player.getPlayerBO().getArtificeMaterial() >= count);
/*      */       case WarspiritTalentMaterial:
/*   78 */         return (this.player.getPlayerBO().getWarspiritTalentMaterial() >= count);
/*      */       case GuildDonate:
/*   80 */         return (((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).getDonate() >= count);
/*      */       case Lottery:
/*   82 */         return (this.player.getPlayerBO().getLottery() >= count);
/*      */       case DressMaterial:
/*   84 */         return (this.player.getPlayerBO().getDressMaterial() >= count);
/*      */     } 
/*   86 */     CommLog.warn("类型{}未指定货币或商品，无法check", type, new Throwable());
/*   87 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void consume(PrizeType type, int count, ItemFlow reason) {
/*   99 */     if (count <= 0) {
/*  100 */       CommLog.error("PlayerCurrency.consume 只能正参, type:{}, count:{}, reason:{}", new Object[] { type, Integer.valueOf(count), reason });
/*      */       return;
/*      */     } 
/*  103 */     switch (type) {
/*      */       case Crystal:
/*  105 */         consumeCrystal(count, reason);
/*      */         return;
/*      */       case Gold:
/*  108 */         consumeMoney(count, reason);
/*      */         return;
/*      */       case Exp:
/*  111 */         consumeExp(count, reason);
/*      */         return;
/*      */       case StrengthenMaterial:
/*  114 */         consumeStrengthenMaterial(count, reason);
/*      */         return;
/*      */       case GemMaterial:
/*  117 */         consumeGemMaterial(count, reason);
/*      */         return;
/*      */       case StarMaterial:
/*  120 */         consumeStarMaterial(count, reason);
/*      */         return;
/*      */       case MerMaterial:
/*  123 */         consumeMerMaterial(count, reason);
/*      */         return;
/*      */       case WingMaterial:
/*  126 */         consumeWingMaterial(count, reason);
/*      */         return;
/*      */       case null:
/*  129 */         consumeArenaToken(count, reason);
/*      */         return;
/*      */       case EquipInstanceMaterial:
/*  132 */         consumeEquipInstanceMaterial(count, reason);
/*      */         return;
/*      */       case GemInstanceMaterial:
/*  135 */         consumeGemInstanceMaterial(count, reason);
/*      */         return;
/*      */       case MeridianInstanceMaterial:
/*  138 */         consumeMeridianInstanceMaterial(count, reason);
/*      */         return;
/*      */       case RedPiece:
/*  141 */         consumeRedPiece(count, reason);
/*      */         return;
/*      */       case ArtificeMaterial:
/*  144 */         consumeArtificeMaterial(count, reason);
/*      */         return;
/*      */       case WarspiritTalentMaterial:
/*  147 */         consumeWarspiritTalentMaterial(count, reason);
/*      */         return;
/*      */       case GuildDonate:
/*  150 */         consumeGuildDonate(count, reason);
/*      */         return;
/*      */       case Lottery:
/*  153 */         consumeLottery(count, reason);
/*      */         return;
/*      */       case DressMaterial:
/*  156 */         consumeDressMaterial(count, reason);
/*      */         return;
/*      */       case ExpMaterial:
/*  159 */         consumeExpMaterial(count, reason);
/*      */         return;
/*      */     } 
/*  162 */     CommLog.warn("类型{}未指定货币或商品，无法consume。", type, new Throwable());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkAndConsume(PrizeType prizeType, int count, ItemFlow reason) {
/*  176 */     if (!check(prizeType, count)) {
/*  177 */       return false;
/*      */     }
/*  179 */     consume(prizeType, count, reason);
/*  180 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int gain(PrizeType type, int count, ItemFlow reason) {
/*      */     int gain, i;
/*  192 */     if (count <= 0) {
/*  193 */       CommLog.error("PlayerCurrency.gain 禁止传负参, type:{}, count:{}, reason:{}", new Object[] { type, Integer.valueOf(count), reason });
/*  194 */       return 0;
/*      */     } 
/*  196 */     switch (type) {
/*      */       case Crystal:
/*  198 */         return gainCrystal(count, reason);
/*      */       case Gold:
/*  200 */         return gainGold(count, reason);
/*      */       case VipExp:
/*  202 */         return gainVipExp(count, reason);
/*      */       case Exp:
/*  204 */         return gainExp(count, reason);
/*      */       case StrengthenMaterial:
/*  206 */         return gainStrengthenMaterial(count, reason);
/*      */       case GemMaterial:
/*  208 */         return gainGemMaterial(count, reason);
/*      */       case StarMaterial:
/*  210 */         return gainStarMaterial(count, reason);
/*      */       case MerMaterial:
/*  212 */         return gainMerMaterial(count, reason);
/*      */       case WingMaterial:
/*  214 */         return gainWingMaterial(count, reason);
/*      */       case null:
/*  216 */         return gainArenaToken(count, reason);
/*      */       case EquipInstanceMaterial:
/*  218 */         return gainEquipInstanceMaterial(count, reason);
/*      */       case GemInstanceMaterial:
/*  220 */         return gainGemInstanceMaterial(count, reason);
/*      */       case MeridianInstanceMaterial:
/*  222 */         return gainMeridianInstanceMaterial(count, reason);
/*      */       case RedPiece:
/*  224 */         return gainRedPiece(count, reason);
/*      */       case ArtificeMaterial:
/*  226 */         return gainArtificeMaterial(count, reason);
/*      */       case WarspiritTalentMaterial:
/*  228 */         return gainWarspiritTalentMaterial(count, reason);
/*      */       case GuildDonate:
/*  230 */         return gainGuildDonate(count, reason);
/*      */       case Lottery:
/*  232 */         return gainLottery(count, reason);
/*      */       case DressMaterial:
/*  234 */         return gainDressMaterial(count, reason);
/*      */       case ExpMaterial:
/*  236 */         gain = gainExpMaterial(count, reason);
/*  237 */         consume(type, gain, reason);
/*  238 */         for (i = 0; i < count; i++) {
/*  239 */           gain(PrizeType.Exp, RefDataMgr.getFactor("ExpMaterialForExp", 4000000), reason);
/*      */         }
/*  241 */         return gain;
/*      */     } 
/*  243 */     CommLog.warn("类型{}未指定货币或商品，无法 获得。", type, new Throwable());
/*  244 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void consumeCrystal(int crystal, ItemFlow reason) {
/*  250 */     if (crystal <= 0) {
/*      */       return;
/*      */     }
/*  253 */     lock();
/*  254 */     int before = this.player.getPlayerBO().getCrystal();
/*  255 */     int finalCrystal = Math.max(0, before - crystal);
/*      */     
/*  257 */     this.player.getPlayerBO().saveCrystal(finalCrystal);
/*  258 */     unlock();
/*      */     
/*  260 */     ((AccumConsume)ActivityMgr.getActivity(AccumConsume.class)).handlePlayerChange(this.player, crystal);
/*      */     
/*  262 */     ActivityMgr.updateWorldRank(this.player, crystal, RankType.WorldConsume);
/*      */ 
/*      */     
/*  265 */     crystalLog(reason.value(), -crystal, finalCrystal, before, ConstEnum.ResOpType.Lose);
/*  266 */     this.player.pushProperties("crystal", this.player.getPlayerBO().getCrystal());
/*      */   }
/*      */ 
/*      */   
/*      */   private int gainCrystal(int crystal, ItemFlow reason) {
/*  271 */     lock();
/*  272 */     int before = this.player.getPlayerBO().getCrystal();
/*  273 */     int finalCrystal = Math.min(RefDataMgr.getFactor("Hero_Max_Crystal", 999999999), before + crystal);
/*      */     
/*  275 */     this.player.getPlayerBO().saveCrystal(finalCrystal);
/*  276 */     unlock();
/*  277 */     this.player.pushProperties("crystal", this.player.getPlayerBO().getCrystal());
/*      */ 
/*      */     
/*  280 */     crystalLog(reason.value(), crystal, finalCrystal, before, ConstEnum.ResOpType.Gain);
/*      */     
/*  282 */     return finalCrystal - before;
/*      */   }
/*      */   
/*      */   private void consumeMoney(int money, ItemFlow reason) {
/*  286 */     lock();
/*  287 */     int before = this.player.getPlayerBO().getGold();
/*  288 */     int finalMoney = Math.max(0, before - money);
/*      */     
/*  290 */     this.player.getPlayerBO().saveGold(finalMoney);
/*  291 */     unlock();
/*  292 */     this.player.pushProperties("gold", this.player.getPlayerBO().getGold());
/*  293 */     goldLog(reason.value(), -money, finalMoney, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainGold(int money, ItemFlow reason) {
/*  297 */     lock();
/*  298 */     int before = this.player.getPlayerBO().getGold();
/*  299 */     int finalMoney = Math.min(RefDataMgr.getFactor("Hero_Max_Gold", 999999999), before + money);
/*      */     
/*  301 */     this.player.getPlayerBO().saveGold(finalMoney);
/*  302 */     unlock();
/*  303 */     this.player.pushProperties("gold", this.player.getPlayerBO().getGold());
/*      */     
/*  305 */     goldLog(reason.value(), money, finalMoney, before, ConstEnum.ResOpType.Gain);
/*  306 */     return money;
/*      */   }
/*      */   
/*      */   private void consumeStrengthenMaterial(int material, ItemFlow reason) {
/*  310 */     lock();
/*  311 */     int before = this.player.getPlayerBO().getStrengthenMaterial();
/*  312 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  314 */     this.player.getPlayerBO().saveStrengthenMaterial(finalMaterial);
/*  315 */     unlock();
/*  316 */     this.player.pushProperties("strengthenMaterial", this.player.getPlayerBO().getStrengthenMaterial());
/*  317 */     strengthenMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainStrengthenMaterial(int material, ItemFlow reason) {
/*  321 */     lock();
/*  322 */     int before = this.player.getPlayerBO().getStrengthenMaterial();
/*  323 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_StrenghenMaterial", 999999999), before + material);
/*      */     
/*  325 */     this.player.getPlayerBO().saveStrengthenMaterial(finalMaterial);
/*  326 */     unlock();
/*  327 */     this.player.pushProperties("strengthenMaterial", this.player.getPlayerBO().getStrengthenMaterial());
/*  328 */     strengthenMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  329 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeGemMaterial(int material, ItemFlow reason) {
/*  333 */     lock();
/*  334 */     int before = this.player.getPlayerBO().getGemMaterial();
/*  335 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  337 */     this.player.getPlayerBO().saveGemMaterial(finalMaterial);
/*  338 */     unlock();
/*  339 */     this.player.pushProperties("gemMaterial", this.player.getPlayerBO().getGemMaterial());
/*  340 */     gemMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainGemMaterial(int material, ItemFlow reason) {
/*  344 */     lock();
/*  345 */     int before = this.player.getPlayerBO().getGemMaterial();
/*  346 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemMaterial", 999999999), before + material);
/*      */     
/*  348 */     this.player.getPlayerBO().saveGemMaterial(finalMaterial);
/*  349 */     unlock();
/*  350 */     this.player.pushProperties("gemMaterial", this.player.getPlayerBO().getGemMaterial());
/*  351 */     gemMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  352 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeDressMaterial(int material, ItemFlow reason) {
/*  356 */     lock();
/*  357 */     int before = this.player.getPlayerBO().getDressMaterial();
/*  358 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  360 */     this.player.getPlayerBO().saveDressMaterial(finalMaterial);
/*  361 */     unlock();
/*  362 */     this.player.pushProperties("dressMaterial", this.player.getPlayerBO().getDressMaterial());
/*      */   }
/*      */   
/*      */   private int gainDressMaterial(int material, ItemFlow reason) {
/*  366 */     lock();
/*  367 */     int before = this.player.getPlayerBO().getDressMaterial();
/*  368 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemMaterial", 999999999), before + material);
/*      */     
/*  370 */     this.player.getPlayerBO().saveDressMaterial(finalMaterial);
/*  371 */     unlock();
/*  372 */     this.player.pushProperties("dressMaterial", this.player.getPlayerBO().getDressMaterial());
/*  373 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeArtificeMaterial(int material, ItemFlow reason) {
/*  377 */     lock();
/*  378 */     int before = this.player.getPlayerBO().getArtificeMaterial();
/*  379 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  381 */     this.player.getPlayerBO().saveArtificeMaterial(finalMaterial);
/*  382 */     unlock();
/*  383 */     this.player.pushProperties("artificeMaterial", this.player.getPlayerBO().getArtificeMaterial());
/*  384 */     artificeMaterialChargeLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainArtificeMaterial(int material, ItemFlow reason) {
/*  388 */     lock();
/*  389 */     int before = this.player.getPlayerBO().getArtificeMaterial();
/*  390 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_ArtificeMaterial", 999999999), before + material);
/*      */     
/*  392 */     this.player.getPlayerBO().saveArtificeMaterial(finalMaterial);
/*  393 */     unlock();
/*  394 */     this.player.pushProperties("artificeMaterial", this.player.getPlayerBO().getArtificeMaterial());
/*  395 */     artificeMaterialChargeLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  396 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeStarMaterial(int material, ItemFlow reason) {
/*  400 */     lock();
/*  401 */     int before = this.player.getPlayerBO().getStarMaterial();
/*  402 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  404 */     this.player.getPlayerBO().saveStarMaterial(finalMaterial);
/*  405 */     unlock();
/*  406 */     this.player.pushProperties("starMaterial", this.player.getPlayerBO().getStarMaterial());
/*  407 */     starMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainStarMaterial(int material, ItemFlow reason) {
/*  411 */     lock();
/*  412 */     int before = this.player.getPlayerBO().getStarMaterial();
/*  413 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_StarMaterial", 999999999), before + material);
/*      */     
/*  415 */     this.player.getPlayerBO().saveStarMaterial(finalMaterial);
/*  416 */     unlock();
/*  417 */     this.player.pushProperties("starMaterial", this.player.getPlayerBO().getStarMaterial());
/*  418 */     starMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  419 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeMerMaterial(int material, ItemFlow reason) {
/*  423 */     lock();
/*  424 */     int before = this.player.getPlayerBO().getMerMaterial();
/*  425 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  427 */     this.player.getPlayerBO().saveMerMaterial(finalMaterial);
/*  428 */     unlock();
/*  429 */     this.player.pushProperties("merMaterial", this.player.getPlayerBO().getMerMaterial());
/*  430 */     merMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainMerMaterial(int material, ItemFlow reason) {
/*  434 */     lock();
/*  435 */     int before = this.player.getPlayerBO().getMerMaterial();
/*  436 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_MerMaterial", 999999999), before + material);
/*      */     
/*  438 */     this.player.getPlayerBO().saveMerMaterial(finalMaterial);
/*  439 */     unlock();
/*  440 */     this.player.pushProperties("merMaterial", this.player.getPlayerBO().getMerMaterial());
/*  441 */     merMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  442 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeWingMaterial(int material, ItemFlow reason) {
/*  446 */     lock();
/*  447 */     int before = this.player.getPlayerBO().getWingMaterial();
/*  448 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  450 */     this.player.getPlayerBO().saveWingMaterial(finalMaterial);
/*  451 */     unlock();
/*  452 */     this.player.pushProperties("wingMaterial", this.player.getPlayerBO().getWingMaterial());
/*  453 */     wingMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainWingMaterial(int material, ItemFlow reason) {
/*  457 */     lock();
/*  458 */     int before = this.player.getPlayerBO().getWingMaterial();
/*  459 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_WingMaterial", 999999999), before + material);
/*      */     
/*  461 */     this.player.getPlayerBO().saveWingMaterial(finalMaterial);
/*  462 */     unlock();
/*  463 */     this.player.pushProperties("wingMaterial", this.player.getPlayerBO().getWingMaterial());
/*  464 */     wingMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  465 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeArenaToken(int count, ItemFlow reason) {
/*  469 */     lock();
/*  470 */     int before = this.player.getPlayerBO().getArenaToken();
/*  471 */     int final0 = Math.max(0, before - count);
/*      */     
/*  473 */     this.player.getPlayerBO().saveArenaToken(final0);
/*  474 */     unlock();
/*  475 */     this.player.pushProperties("arenaToken", this.player.getPlayerBO().getArenaToken());
/*  476 */     arenaTokenLog(reason.value(), -count, final0, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainArenaToken(int count, ItemFlow reason) {
/*  480 */     lock();
/*  481 */     int before = this.player.getPlayerBO().getArenaToken();
/*  482 */     int final0 = Math.min(RefDataMgr.getFactor("Hero_Max_ArenaToken", 999999999), before + count);
/*      */     
/*  484 */     this.player.getPlayerBO().saveArenaToken(final0);
/*  485 */     unlock();
/*  486 */     this.player.pushProperties("arenaToken", this.player.getPlayerBO().getArenaToken());
/*  487 */     arenaTokenLog(reason.value(), count, final0, before, ConstEnum.ResOpType.Gain);
/*  488 */     return count;
/*      */   }
/*      */   
/*      */   private void consumeEquipInstanceMaterial(int material, ItemFlow reason) {
/*  492 */     lock();
/*  493 */     int before = this.player.getPlayerBO().getEquipInstanceMaterial();
/*  494 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  496 */     this.player.getPlayerBO().saveEquipInstanceMaterial(finalMaterial);
/*  497 */     unlock();
/*  498 */     this.player.pushProperties("equipInstanceMaterial", this.player.getPlayerBO().getEquipInstanceMaterial());
/*  499 */     EquipInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainEquipInstanceMaterial(int material, ItemFlow reason) {
/*  503 */     lock();
/*  504 */     int before = this.player.getPlayerBO().getEquipInstanceMaterial();
/*  505 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_EquipInstanceMaterial", 999999999), before + material);
/*      */     
/*  507 */     this.player.getPlayerBO().saveEquipInstanceMaterial(finalMaterial);
/*  508 */     unlock();
/*  509 */     this.player.pushProperties("equipInstanceMaterial", this.player.getPlayerBO().getEquipInstanceMaterial());
/*  510 */     EquipInstanceMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  511 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeGemInstanceMaterial(int material, ItemFlow reason) {
/*  515 */     lock();
/*  516 */     int before = this.player.getPlayerBO().getGemInstanceMaterial();
/*  517 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  519 */     this.player.getPlayerBO().saveGemInstanceMaterial(finalMaterial);
/*  520 */     unlock();
/*  521 */     this.player.pushProperties("gemInstanceMaterial", this.player.getPlayerBO().getGemInstanceMaterial());
/*  522 */     GemInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainGemInstanceMaterial(int material, ItemFlow reason) {
/*  526 */     lock();
/*  527 */     int before = this.player.getPlayerBO().getGemInstanceMaterial();
/*  528 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemInstanceMaterial", 999999999), before + material);
/*      */     
/*  530 */     this.player.getPlayerBO().saveGemInstanceMaterial(finalMaterial);
/*  531 */     unlock();
/*  532 */     this.player.pushProperties("gemInstanceMaterial", this.player.getPlayerBO().getGemInstanceMaterial());
/*  533 */     GemInstanceMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  534 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeMeridianInstanceMaterial(int material, ItemFlow reason) {
/*  538 */     lock();
/*  539 */     int before = this.player.getPlayerBO().getMeridianInstanceMaterial();
/*  540 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  542 */     this.player.getPlayerBO().saveMeridianInstanceMaterial(finalMaterial);
/*  543 */     unlock();
/*  544 */     this.player.pushProperties("meridianInstanceMaterial", this.player.getPlayerBO().getMeridianInstanceMaterial());
/*  545 */     meridianInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainMeridianInstanceMaterial(int material, ItemFlow reason) {
/*  549 */     lock();
/*  550 */     int before = this.player.getPlayerBO().getMeridianInstanceMaterial();
/*  551 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_MeridianInstanceMaterial", 999999999), before + material);
/*      */     
/*  553 */     this.player.getPlayerBO().saveMeridianInstanceMaterial(finalMaterial);
/*  554 */     unlock();
/*  555 */     this.player.pushProperties("meridianInstanceMaterial", this.player.getPlayerBO().getMeridianInstanceMaterial());
/*  556 */     meridianInstanceMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  557 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeRedPiece(int material, ItemFlow reason) {
/*  561 */     lock();
/*  562 */     int before = this.player.getPlayerBO().getRedPiece();
/*  563 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  565 */     this.player.getPlayerBO().saveRedPiece(finalMaterial);
/*  566 */     unlock();
/*  567 */     this.player.pushProperties("redPiece", this.player.getPlayerBO().getRedPiece());
/*  568 */     redPieceLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainRedPiece(int material, ItemFlow reason) {
/*  572 */     lock();
/*  573 */     int before = this.player.getPlayerBO().getRedPiece();
/*  574 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_RedPiece", 999999999), before + material);
/*      */     
/*  576 */     this.player.getPlayerBO().saveRedPiece(finalMaterial);
/*  577 */     unlock();
/*  578 */     this.player.pushProperties("redPiece", this.player.getPlayerBO().getRedPiece());
/*  579 */     redPieceLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  580 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeWarspiritTalentMaterial(int material, ItemFlow reason) {
/*  584 */     lock();
/*  585 */     int before = this.player.getPlayerBO().getWarspiritTalentMaterial();
/*  586 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  588 */     this.player.getPlayerBO().saveWarspiritTalentMaterial(finalMaterial);
/*  589 */     unlock();
/*  590 */     warspiritTalentMaterialChargeLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*  591 */     this.player.pushProperties("warspiritTalentMaterial", this.player.getPlayerBO().getWarspiritTalentMaterial());
/*      */   }
/*      */   
/*      */   private int gainWarspiritTalentMaterial(int material, ItemFlow reason) {
/*  595 */     lock();
/*  596 */     int before = this.player.getPlayerBO().getWarspiritTalentMaterial();
/*  597 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_WarspiritTalentMaterial", 999999999), before + material);
/*      */     
/*  599 */     this.player.getPlayerBO().saveWarspiritTalentMaterial(finalMaterial);
/*  600 */     unlock();
/*  601 */     this.player.pushProperties("warspiritTalentMaterial", this.player.getPlayerBO().getWarspiritTalentMaterial());
/*  602 */     warspiritTalentMaterialChargeLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  603 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeGuildDonate(int donate, ItemFlow reason) {
/*  607 */     ((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).consumeDonate(donate, reason);
/*      */   }
/*      */   
/*      */   private int gainGuildDonate(int donate, ItemFlow reason) {
/*  611 */     return ((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).gainDonate(donate, reason);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int gainVipExp(int addExp, ItemFlow reason) {
/*  622 */     lock();
/*      */     
/*  624 */     int oldLvl = this.player.getPlayerBO().getVipLevel();
/*  625 */     int oldExp = this.player.getPlayerBO().getVipExp();
/*      */     
/*  627 */     int nowexp = oldExp + addExp;
/*  628 */     if (nowexp < 0) {
/*  629 */       nowexp = 0;
/*      */     }
/*  631 */     int nowLvl = oldLvl;
/*      */     while (true) {
/*  633 */       RefVIP VIP = (RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(nowLvl + 1));
/*      */       
/*  635 */       if (VIP == null) {
/*      */         break;
/*      */       }
/*      */       
/*  639 */       if (nowexp >= VIP.CurNeedExp) {
/*  640 */         nowLvl++;
/*  641 */         nowexp -= VIP.CurNeedExp;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/*  648 */     PlayerBO bo = this.player.getPlayerBO();
/*      */     
/*  650 */     bo.saveVipExp(nowexp);
/*  651 */     if (nowLvl != oldLvl) {
/*  652 */       bo.saveVipLevel(nowLvl);
/*  653 */       ((PlayerBase)this.player.getFeature(PlayerBase.class)).onVipLevelUp(nowLvl, oldLvl);
/*      */       
/*  655 */       PlayerMgr.getInstance().tryNotify(this.player);
/*      */     } 
/*  657 */     unlock();
/*      */ 
/*      */     
/*  660 */     if (nowLvl != oldLvl || nowexp != oldExp) {
/*  661 */       this.player.pushProperties("vipExp", this.player.getPlayerBO().getVipExp(), "vipLv", this.player.getPlayerBO().getVipLevel());
/*      */     }
/*      */     
/*  664 */     return addExp;
/*      */   }
/*      */   
/*      */   private int gainExp(int addExp, ItemFlow reason) {
/*  668 */     if (addExp <= 0) {
/*  669 */       return 0;
/*      */     }
/*      */     
/*  672 */     lock();
/*      */     
/*  674 */     int oldLvl = this.player.getPlayerBO().getLv();
/*  675 */     int oldExp = this.player.getPlayerBO().getExp();
/*      */     
/*  677 */     int nowexp = oldExp + addExp;
/*  678 */     if (nowexp < 0) {
/*  679 */       nowexp = 0;
/*      */     }
/*      */     
/*  682 */     nowexp = Math.min(nowexp, RefDataMgr.getFactor("HeroMaxExp", 999999999));
/*      */     
/*  684 */     int nowLvl = oldLvl;
/*      */ 
/*      */     
/*      */     while (true) {
/*  688 */       int MAX_TEAMLV = RefDataMgr.getFactor("HeroMaxLevel", 80);
/*  689 */       if (nowLvl >= MAX_TEAMLV) {
/*      */         break;
/*      */       }
/*  692 */       RefTeamExp teamExp = (RefTeamExp)RefDataMgr.get(RefTeamExp.class, Integer.valueOf(nowLvl));
/*  693 */       if (teamExp == null) {
/*      */         break;
/*      */       }
/*  696 */       int levelUpExp = teamExp.UpExp;
/*      */       
/*  698 */       if (nowexp >= levelUpExp) {
/*  699 */         nowLvl++;
/*  700 */         nowexp -= levelUpExp;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  706 */     List<Player.Property> properties = new ArrayList<>();
/*  707 */     if (oldExp != nowexp) {
/*  708 */       this.player.getPlayerBO().saveExp(nowexp);
/*  709 */       properties.add(new Player.Property("exp", this.player.getPlayerBO().getExp()));
/*      */     } 
/*  711 */     if (oldLvl != nowLvl) {
/*  712 */       this.player.getPlayerBO().saveLv(nowLvl);
/*  713 */       properties.add(new Player.Property("lv", this.player.getPlayerBO().getLv()));
/*  714 */       ((PlayerBase)this.player.getFeature(PlayerBase.class)).onLevelUp(nowLvl);
/*      */     } 
/*  716 */     unlock();
/*  717 */     if (properties.size() > 0) {
/*  718 */       this.player.pushProperties(properties);
/*      */     }
/*  720 */     expLog(reason.value(), addExp, nowexp, oldExp, ConstEnum.ResOpType.Gain);
/*  721 */     return addExp;
/*      */   }
/*      */   
/*      */   private void consumeExp(int exp, ItemFlow reason) {
/*  725 */     lock();
/*  726 */     int before = this.player.getPlayerBO().getExp();
/*  727 */     int finalMaterial = Math.max(0, before - exp);
/*      */     
/*  729 */     this.player.getPlayerBO().saveExp(finalMaterial);
/*  730 */     unlock();
/*  731 */     this.player.pushProperties("exp", this.player.getPlayerBO().getExp());
/*  732 */     expLog(reason.value(), -exp, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updateMaxGlobalMailID(long mailID) {
/*  742 */     boolean ret = false;
/*  743 */     lock();
/*  744 */     if (this.player.getPlayerBO().getGmMailCheckId() < mailID) {
/*  745 */       this.player.getPlayerBO().saveGmMailCheckId(mailID);
/*  746 */       ret = true;
/*      */     } 
/*  748 */     unlock();
/*  749 */     return ret;
/*      */   }
/*      */   
/*      */   private void consumeLottery(int material, ItemFlow reason) {
/*  753 */     lock();
/*  754 */     int before = this.player.getPlayerBO().getLottery();
/*  755 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  757 */     this.player.getPlayerBO().saveLottery(finalMaterial);
/*  758 */     unlock();
/*  759 */     this.player.pushProperties("lottery", this.player.getPlayerBO().getLottery());
/*  760 */     lotteryChargeLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
/*      */   }
/*      */   
/*      */   private int gainLottery(int material, ItemFlow reason) {
/*  764 */     lock();
/*  765 */     int before = this.player.getPlayerBO().getLottery();
/*  766 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_Lottery", 999999999), before + material);
/*      */     
/*  768 */     this.player.getPlayerBO().saveLottery(finalMaterial);
/*  769 */     unlock();
/*  770 */     this.player.pushProperties("lottery", this.player.getPlayerBO().getLottery());
/*  771 */     lotteryChargeLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
/*  772 */     return material;
/*      */   }
/*      */   
/*      */   private void consumeExpMaterial(int material, ItemFlow reason) {
/*  776 */     lock();
/*  777 */     int before = this.player.getPlayerBO().getExpMaterial();
/*  778 */     int finalMaterial = Math.max(0, before - material);
/*      */     
/*  780 */     this.player.getPlayerBO().saveExpMaterial(finalMaterial);
/*  781 */     unlock();
/*      */   }
/*      */ 
/*      */   
/*      */   private int gainExpMaterial(int material, ItemFlow reason) {
/*  786 */     lock();
/*  787 */     int before = this.player.getPlayerBO().getExpMaterial();
/*  788 */     int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_RedPiece", 999999999), before + material);
/*      */     
/*  790 */     this.player.getPlayerBO().saveExpMaterial(finalMaterial);
/*  791 */     unlock();
/*      */     
/*  793 */     return material;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void arenaTokenLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  799 */     FlowLogger.arenaTokenChargeLog(
/*  800 */         this.player.getPid(), 
/*  801 */         this.player.getVipLevel(), 
/*  802 */         this.player.getLv(), 
/*  803 */         reason, 
/*  804 */         crystal, 
/*  805 */         finalCrystal, 
/*  806 */         before, 
/*  807 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void crystalLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  812 */     FlowLogger.crystalChargeLog(
/*  813 */         this.player.getPid(), 
/*  814 */         this.player.getVipLevel(), 
/*  815 */         this.player.getLv(), 
/*  816 */         reason, 
/*  817 */         crystal, 
/*  818 */         finalCrystal, 
/*  819 */         before, 
/*  820 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void EquipInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  825 */     FlowLogger.EquipInstanceMaterialChargeLog(
/*  826 */         this.player.getPid(), 
/*  827 */         this.player.getVipLevel(), 
/*  828 */         this.player.getLv(), 
/*  829 */         reason, 
/*  830 */         crystal, 
/*  831 */         finalCrystal, 
/*  832 */         before, 
/*  833 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void expLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  838 */     FlowLogger.expChargeLog(
/*  839 */         this.player.getPid(), 
/*  840 */         this.player.getVipLevel(), 
/*  841 */         this.player.getLv(), 
/*  842 */         reason, 
/*  843 */         crystal, 
/*  844 */         finalCrystal, 
/*  845 */         before, 
/*  846 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void GemInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  851 */     FlowLogger.GemInstanceMaterialChargeLog(
/*  852 */         this.player.getPid(), 
/*  853 */         this.player.getVipLevel(), 
/*  854 */         this.player.getLv(), 
/*  855 */         reason, 
/*  856 */         crystal, 
/*  857 */         finalCrystal, 
/*  858 */         before, 
/*  859 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void gemMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  864 */     FlowLogger.gemMaterialChargeLog(
/*  865 */         this.player.getPid(), 
/*  866 */         this.player.getVipLevel(), 
/*  867 */         this.player.getLv(), 
/*  868 */         reason, 
/*  869 */         crystal, 
/*  870 */         finalCrystal, 
/*  871 */         before, 
/*  872 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void goldLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  877 */     FlowLogger.goldChargeLog(
/*  878 */         this.player.getPid(), 
/*  879 */         this.player.getVipLevel(), 
/*  880 */         this.player.getLv(), 
/*  881 */         reason, 
/*  882 */         crystal, 
/*  883 */         finalCrystal, 
/*  884 */         before, 
/*  885 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void meridianInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  890 */     FlowLogger.meridianInstanceMaterialChargeLog(
/*  891 */         this.player.getPid(), 
/*  892 */         this.player.getVipLevel(), 
/*  893 */         this.player.getLv(), 
/*  894 */         reason, 
/*  895 */         crystal, 
/*  896 */         finalCrystal, 
/*  897 */         before, 
/*  898 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void merMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  903 */     FlowLogger.merMaterialChargeLog(
/*  904 */         this.player.getPid(), 
/*  905 */         this.player.getVipLevel(), 
/*  906 */         this.player.getLv(), 
/*  907 */         reason, 
/*  908 */         crystal, 
/*  909 */         finalCrystal, 
/*  910 */         before, 
/*  911 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void redPieceLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  916 */     FlowLogger.redPieceChargeLog(
/*  917 */         this.player.getPid(), 
/*  918 */         this.player.getVipLevel(), 
/*  919 */         this.player.getLv(), 
/*  920 */         reason, 
/*  921 */         crystal, 
/*  922 */         finalCrystal, 
/*  923 */         before, 
/*  924 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void starMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  929 */     FlowLogger.starMaterialChargeLog(
/*  930 */         this.player.getPid(), 
/*  931 */         this.player.getVipLevel(), 
/*  932 */         this.player.getLv(), 
/*  933 */         reason, 
/*  934 */         crystal, 
/*  935 */         finalCrystal, 
/*  936 */         before, 
/*  937 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void strengthenMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  942 */     FlowLogger.strengthenMaterialChargeLog(
/*  943 */         this.player.getPid(), 
/*  944 */         this.player.getVipLevel(), 
/*  945 */         this.player.getLv(), 
/*  946 */         reason, 
/*  947 */         crystal, 
/*  948 */         finalCrystal, 
/*  949 */         before, 
/*  950 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void wingMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  955 */     FlowLogger.wingMaterialChargeLog(
/*  956 */         this.player.getPid(), 
/*  957 */         this.player.getVipLevel(), 
/*  958 */         this.player.getLv(), 
/*  959 */         reason, 
/*  960 */         crystal, 
/*  961 */         finalCrystal, 
/*  962 */         before, 
/*  963 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void warspiritTalentMaterialChargeLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  968 */     FlowLogger.warspiritTalentMaterialChargeLog(
/*  969 */         this.player.getPid(), 
/*  970 */         this.player.getVipLevel(), 
/*  971 */         this.player.getLv(), 
/*  972 */         reason, 
/*  973 */         crystal, 
/*  974 */         finalCrystal, 
/*  975 */         before, 
/*  976 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void artificeMaterialChargeLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  981 */     FlowLogger.artificeMaterialChargeLog(
/*  982 */         this.player.getPid(), 
/*  983 */         this.player.getVipLevel(), 
/*  984 */         this.player.getLv(), 
/*  985 */         reason, 
/*  986 */         crystal, 
/*  987 */         finalCrystal, 
/*  988 */         before, 
/*  989 */         opType.ordinal());
/*      */   }
/*      */ 
/*      */   
/*      */   public void lotteryChargeLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/*  994 */     FlowLogger.lotteryChargeLog(
/*  995 */         this.player.getPid(), 
/*  996 */         this.player.getVipLevel(), 
/*  997 */         this.player.getLv(), 
/*  998 */         reason, 
/*  999 */         crystal, 
/* 1000 */         finalCrystal, 
/* 1001 */         before, 
/* 1002 */         opType.ordinal());
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/PlayerCurrency.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */