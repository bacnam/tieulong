/*     */ package business.player.feature.marry;
/*     */ 
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCrystalPrice;
/*     */ import core.config.refdata.ref.RefGuildLevel;
/*     */ import core.config.refdata.ref.RefMarryLevel;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.config.refdata.ref.RefSignIn;
/*     */ import core.database.game.bo.MarryApplyBO;
/*     */ import core.database.game.bo.MarryBO;
/*     */ import core.database.game.bo.MarryDivorceApplyBO;
/*     */ import core.network.proto.LoverInfo;
/*     */ import core.network.proto.MarryApplyInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class MarryFeature
/*     */   extends Feature
/*     */ {
/*     */   private int Max_List_Size;
/*     */   private int receive_apply_size;
/*     */   public MarryBO bo;
/*     */   
/*     */   public MarryFeature(Player player) {
/*  46 */     super(player);
/*     */ 
/*     */ 
/*     */     
/*  50 */     this.Max_List_Size = RefDataMgr.getFactor("MarryList", 4);
/*  51 */     this.receive_apply_size = RefDataMgr.getFactor("MarryReceiveApply", 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public MarryApplyBO sendApplys;
/*     */   public List<MarryApplyBO> receiveApplys;
/*     */   public MarryDivorceApplyBO divorceApplys;
/*     */   public List<MarryDivorceApplyBO> receiveDivorceApplys;
/*     */   
/*     */   public void loadDB() {
/*  61 */     this.bo = (MarryBO)BM.getBM(MarryBO.class).findOne("pid", Long.valueOf(getPid()));
/*  62 */     this.sendApplys = (MarryApplyBO)BM.getBM(MarryApplyBO.class).findOne("from_pid", Long.valueOf(getPid()));
/*  63 */     this.receiveApplys = BM.getBM(MarryApplyBO.class).findAll("pid", Long.valueOf(getPid()));
/*  64 */     this.receiveDivorceApplys = BM.getBM(MarryDivorceApplyBO.class).findAll("pid", Long.valueOf(getPid()));
/*  65 */     if (this.bo == null) {
/*  66 */       this.bo = new MarryBO();
/*  67 */       this.bo.setPid(getPid());
/*  68 */       this.bo.insert();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSex(ConstEnum.SexType sex) throws WSException {
/*  77 */     if (this.bo.getSex() != 0) {
/*  78 */       throw new WSException(ErrorCode.Marry_SexExit, "玩家已有性别");
/*     */     }
/*  80 */     this.bo.saveSex(sex.ordinal());
/*  81 */     (PlayerMgr.getInstance()).sexPlayers.put(this.player.getName(), this.player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Player> getMarryList() throws WSException {
/*  90 */     if (this.bo.getSex() == 0) {
/*  91 */       throw new WSException(ErrorCode.Marry_SexNot, "玩家没选择性别");
/*     */     }
/*  93 */     List<Long> except = new ArrayList<>();
/*  94 */     except.add(Long.valueOf(getPid()));
/*  95 */     List<Player> list = PlayerMgr.getInstance().randomLoadSexPlayers(getSex(), this.Max_List_Size, except);
/*  96 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MarryApplyBO> getApplyList() throws WSException {
/* 105 */     List<MarryApplyBO> remove = new ArrayList<>();
/* 106 */     this.receiveApplys.stream().filter(x -> !checkApply(x)).forEach(x -> paramList.add(x));
/*     */ 
/*     */     
/* 109 */     this.receiveApplys.removeAll(remove);
/* 110 */     return this.receiveApplys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Player> search(String name) throws WSException {
/* 119 */     List<Player> players = new ArrayList<>();
/*     */     
/* 121 */     Player one = (Player)(PlayerMgr.getInstance()).sexPlayers.get(name);
/* 122 */     if (one != null && 
/* 123 */       ((MarryFeature)one.getFeature(MarryFeature.class)).bo.getSex() != this.bo.getSex()) {
/* 124 */       players.add(one);
/* 125 */       return players;
/*     */     } 
/*     */ 
/*     */     
/* 129 */     for (Player player : (PlayerMgr.getInstance()).sexPlayers.values()) {
/* 130 */       if (((MarryFeature)player.getFeature(MarryFeature.class)).bo.getSex() == this.bo.getSex()) {
/*     */         continue;
/*     */       }
/* 133 */       if (player.getName().contains(name)) {
/* 134 */         players.add(player);
/*     */       }
/* 136 */       if (players.size() >= this.Max_List_Size) {
/*     */         break;
/*     */       }
/*     */     } 
/* 140 */     Collections.shuffle(players);
/* 141 */     return players;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void marryApply(long pid) throws WSException {
/* 150 */     if (this.sendApplys != null) {
/* 151 */       throw new WSException(ErrorCode.Marry_ApplyFull, "玩家已求婚");
/*     */     }
/*     */     
/* 154 */     Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
/* 155 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/*     */     
/* 157 */     List<MarryApplyBO> remove = new ArrayList<>();
/* 158 */     to_feature.receiveApplys.stream().filter(x -> !checkApply(x)).forEach(x -> paramList.add(x));
/*     */ 
/*     */     
/* 161 */     to_feature.receiveApplys.removeAll(remove);
/* 162 */     if (to_feature.receiveApplys.size() >= to_feature.receive_apply_size) {
/* 163 */       throw new WSException(ErrorCode.Marry_ApplyFull, "玩家申请已满");
/*     */     }
/*     */     
/* 166 */     if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).check(MarryConfig.getMarryApplyCostId(), MarryConfig.getMarryApplyCostCount())) {
/* 167 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家缺少结婚戒指");
/*     */     }
/*     */     
/* 170 */     MarryApplyBO bo = to_feature.receiveApply(this.player);
/* 171 */     marryApply(bo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarryApplyBO receiveApply(Player applyPlayer) throws WSException {
/* 180 */     synchronized (this) {
/* 181 */       MarryApplyBO applybo = new MarryApplyBO();
/* 182 */       applybo.setPid(getPid());
/* 183 */       applybo.setFromPid(applyPlayer.getPid());
/* 184 */       applybo.setApplyTime(CommTime.nowSecond());
/* 185 */       applybo.insert();
/* 186 */       this.receiveApplys.add(applybo);
/* 187 */       MarryApplyInfo info = new MarryApplyInfo();
/* 188 */       info.setSummary(((PlayerBase)applyPlayer.getFeature(PlayerBase.class)).summary());
/* 189 */       info.setLeftTime(getLeftTime(applybo));
/* 190 */       this.player.pushProto("newMarryApply", info);
/* 191 */       return applybo;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void marryApply(MarryApplyBO applybo) {
/* 200 */     this.sendApplys = applybo;
/*     */   }
/*     */   
/*     */   public MarryApplyBO getApply(long pid) {
/* 204 */     for (MarryApplyBO applybo : this.receiveApplys) {
/* 205 */       if (applybo.getFromPid() == pid) {
/* 206 */         return applybo;
/*     */       }
/*     */     } 
/* 209 */     return null;
/*     */   }
/*     */   
/*     */   public MarryDivorceApplyBO getDivorceApply(long pid) {
/* 213 */     for (MarryDivorceApplyBO bo : this.receiveDivorceApplys) {
/* 214 */       if (bo.getFromPid() == pid) {
/* 215 */         return bo;
/*     */       }
/*     */     } 
/* 218 */     return null;
/*     */   }
/*     */   
/*     */   public boolean checkApply(MarryApplyBO bo) {
/* 222 */     boolean check = (CommTime.nowSecond() - bo.getApplyTime() < RefDataMgr.getFactor("MarryApplyTime", 1800));
/* 223 */     if (!check) {
/* 224 */       Player toPlayer = PlayerMgr.getInstance().getPlayer(bo.getFromPid());
/* 225 */       MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/* 226 */       to_feature.removeApply(bo);
/*     */     } 
/*     */ 
/*     */     
/* 230 */     return check;
/*     */   }
/*     */   
/*     */   public boolean checkApply(MarryDivorceApplyBO bo) {
/* 234 */     boolean check = (CommTime.nowSecond() - bo.getApplyTime() < RefDataMgr.getFactor("MarryApplyTime", 1800));
/* 235 */     if (!check) {
/* 236 */       Player toPlayer = PlayerMgr.getInstance().getPlayer(bo.getFromPid());
/* 237 */       MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/* 238 */       to_feature.removeApply(bo);
/*     */     } 
/*     */ 
/*     */     
/* 242 */     return check;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void agreedMarry(long pid) throws WSException {
/* 250 */     MarryApplyBO bo = getApply(pid);
/* 251 */     if (bo == null || !checkApply(bo)) {
/* 252 */       throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
/*     */     }
/*     */     
/* 255 */     Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
/* 256 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/*     */     
/* 258 */     if (to_feature.isMarried()) {
/* 259 */       throw new WSException(ErrorCode.Marry_Already, "玩家已结婚");
/*     */     }
/*     */     
/* 262 */     if (!((PlayerItem)toPlayer.getFeature(PlayerItem.class)).checkAndConsume(MarryConfig.getMarryApplyCostId(), MarryConfig.getMarryApplyCostCount(), 
/* 263 */         ItemFlow.MarryApply)) {
/* 264 */       throw new WSException(ErrorCode.NotEnough_Crystal, "对方缺少结婚戒指");
/*     */     }
/*     */     
/* 267 */     to_feature.beLoverWith(getPid());
/* 268 */     to_feature.sendApplys.del();
/* 269 */     to_feature.sendApplys = null;
/* 270 */     toPlayer.pushProto("beAgreed", to_feature.getLoveInfo());
/*     */     
/* 272 */     this.receiveApplys.remove(bo);
/* 273 */     beLoverWith(pid);
/*     */     
/* 275 */     Player man = null;
/* 276 */     if (this.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
/* 277 */       man = this.player;
/*     */     } else {
/* 279 */       man = toPlayer;
/*     */     } 
/*     */     
/* 282 */     RankManager.getInstance().update(RankType.Lovers, man.getPid(), ((
/* 283 */         (CharFeature)this.player.getFeature(CharFeature.class)).getPower() + ((CharFeature)toPlayer.getFeature(CharFeature.class)).getPower()));
/*     */     
/* 285 */     broadcast();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void broadcast() {
/* 295 */     broadcastProtol pro = new broadcastProtol(null);
/* 296 */     pro.husbend = (getLoveInfo().getHusband()).name;
/* 297 */     pro.wife = (getLoveInfo().getWife()).name;
/* 298 */     pro.reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(RefDataMgr.getFactor("MarryAllReward", 500001)))).genReward();
/*     */     
/* 300 */     for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/* 301 */       ((PlayerItem)player.getFeature(PlayerItem.class)).gain(pro.reward, ItemFlow.MarryAllReward);
/* 302 */       player.pushProto("NewMarry", pro);
/*     */     } 
/*     */ 
/*     */     
/* 306 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.NewMarry, new String[] { pro.husbend, pro.wife });
/*     */   } private static class broadcastProtol {
/*     */     String husbend; String wife; Reward reward;
/*     */     private broadcastProtol() {} }
/*     */   public void beLoverWith(long pid) {
/* 311 */     this.bo.saveLoverPid(pid);
/* 312 */     this.bo.saveMarried(ConstEnum.MarryType.Married.ordinal());
/*     */     
/* 314 */     RefReward ref = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(MarryConfig.getMarryReward()));
/* 315 */     if (ref != null) {
/* 316 */       ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(ref.genReward(), ItemFlow.MarryReward);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disagreedMarry(long pid) throws WSException {
/* 325 */     MarryApplyBO bo = getApply(pid);
/* 326 */     if (bo == null || !checkApply(bo)) {
/* 327 */       throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
/*     */     }
/* 329 */     bo.del();
/* 330 */     Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
/* 331 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/* 332 */     to_feature.removeApply(bo);
/* 333 */     this.receiveApplys.remove(bo);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeApply(MarryApplyBO bo) {
/* 338 */     synchronized (this) {
/* 339 */       if (this.sendApplys == null) {
/*     */         return;
/*     */       }
/* 342 */       this.sendApplys = null;
/* 343 */       bo.del();
/* 344 */       this.player.pushProto("beReject", "");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeApply(MarryDivorceApplyBO bo) {
/* 350 */     synchronized (this) {
/* 351 */       if (this.divorceApplys == null || this.divorceApplys != bo) {
/*     */         return;
/*     */       }
/* 354 */       this.divorceApplys = null;
/* 355 */       bo.del();
/* 356 */       this.player.pushProto("beDivorceReject", "");
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
/*     */   public void sendFlower() throws WSException {
/* 369 */     PlayerRecord record = (PlayerRecord)this.player.getFeature(PlayerRecord.class);
/* 370 */     int times = record.getValue(ConstEnum.DailyRefresh.LoversSend);
/*     */     
/* 372 */     PlayerCurrency playerCurrency = (PlayerCurrency)this.player.getFeature(PlayerCurrency.class);
/* 373 */     if (!playerCurrency.checkAndConsume(PrizeType.Crystal, (RefCrystalPrice.getPrize(times)).LoversSend, ItemFlow.MarryApply)) {
/* 374 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝不足");
/*     */     }
/* 376 */     record.addValue(ConstEnum.DailyRefresh.LoversSend);
/* 377 */     Player toPlayer = getLover();
/* 378 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/* 379 */     to_feature.gainExp(RefDataMgr.getFactor("sendFlowerExp", 100));
/* 380 */     toPlayer.pushProto("flower", to_feature.bo);
/* 381 */     gainExp(RefDataMgr.getFactor("sendFlowerExp", 100));
/* 382 */     this.player.pushProto("flower", this.bo);
/*     */   }
/*     */   
/*     */   public Player getLover() {
/* 386 */     return PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid());
/*     */   }
/*     */   
/*     */   public MarryFeature getLoverFeature() {
/* 390 */     return (MarryFeature)PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid()).getFeature(MarryFeature.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int gainExp(int Exp) {
/* 396 */     RefMarryLevel nextLeveLinfo = (RefMarryLevel)RefDataMgr.get(RefMarryLevel.class, Integer.valueOf(this.bo.getLevel() + 1));
/* 397 */     if (nextLeveLinfo == null) {
/* 398 */       return 0;
/*     */     }
/* 400 */     this.bo.saveExp(this.bo.getExp() + Exp);
/* 401 */     RefMarryLevel levelinfo = (RefMarryLevel)RefDataMgr.get(RefMarryLevel.class, Integer.valueOf(this.bo.getLevel()));
/* 402 */     if (this.bo.getExp() < levelinfo.NeedExp) {
/* 403 */       return Exp;
/*     */     }
/* 405 */     this.bo.setExp(this.bo.getExp() - levelinfo.NeedExp);
/* 406 */     this.bo.setLevel(this.bo.getLevel() + 1);
/* 407 */     if (RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(this.bo.getLevel() + 1)) == null) {
/* 408 */       this.bo.setExp(0);
/*     */     }
/* 410 */     this.bo.saveAll();
/*     */     
/* 412 */     return Exp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void divorce() {
/* 420 */     if (this.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
/* 421 */       RankManager.getInstance().clearPlayerData(this.player, RankType.Lovers);
/*     */     }
/* 423 */     this.bo.setLoverPid(0L);
/* 424 */     this.bo.setMarried(0);
/* 425 */     this.bo.setExp(0);
/* 426 */     this.bo.setLevel(0);
/* 427 */     this.bo.setSignin(0);
/* 428 */     this.bo.setSignReward("");
/* 429 */     this.bo.setLevelReward("");
/* 430 */     this.bo.setIsSign(false);
/* 431 */     this.bo.saveAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void marryApply(MarryDivorceApplyBO applybo) {
/* 438 */     this.divorceApplys = applybo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarryDivorceApplyBO marryDivorceApply(long pid) throws WSException {
/* 447 */     if (this.divorceApplys != null) {
/* 448 */       throw new WSException(ErrorCode.Marry_ApplyFull, "玩家已发送过离婚");
/*     */     }
/* 450 */     if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).check(MarryConfig.getMarryDivorceApplyCostId(), MarryConfig.getMarryDivorceApplyCostCount())) {
/* 451 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家缺少离婚许可");
/*     */     }
/*     */     
/* 454 */     Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
/* 455 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/* 456 */     MarryDivorceApplyBO bo = to_feature.receiveDivorceApply(this.player);
/* 457 */     marryApply(bo);
/* 458 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarryDivorceApplyBO receiveDivorceApply(Player applyPlayer) throws WSException {
/* 467 */     synchronized (this) {
/* 468 */       MarryDivorceApplyBO applybo = new MarryDivorceApplyBO();
/* 469 */       applybo.setPid(getPid());
/* 470 */       applybo.setFromPid(applyPlayer.getPid());
/* 471 */       applybo.setApplyTime(CommTime.nowSecond());
/* 472 */       applybo.insert();
/* 473 */       this.receiveDivorceApplys.add(applybo);
/* 474 */       MarryApplyInfo info = new MarryApplyInfo();
/* 475 */       info.setSummary(((PlayerBase)applyPlayer.getFeature(PlayerBase.class)).summary());
/* 476 */       info.setLeftTime(getDivorceLeftTime(applybo));
/* 477 */       this.player.pushProto("newDivorceApply", info);
/* 478 */       return applybo;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disagreedDivorce(long pid) throws WSException {
/* 487 */     MarryDivorceApplyBO bo = getDivorceApply(pid);
/* 488 */     if (bo == null || !checkApply(bo)) {
/* 489 */       throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
/*     */     }
/* 491 */     bo.del();
/* 492 */     Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
/* 493 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/* 494 */     to_feature.removeApply(bo);
/* 495 */     this.receiveDivorceApplys.remove(bo);
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
/*     */   public void agreedDivorce(long pid) throws WSException {
/* 509 */     MarryDivorceApplyBO bo = getDivorceApply(pid);
/* 510 */     if (bo == null) {
/* 511 */       throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
/*     */     }
/*     */     
/* 514 */     Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
/* 515 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/*     */     
/* 517 */     if (!to_feature.isMarried()) {
/* 518 */       throw new WSException(ErrorCode.Marry_DivorceAlready, "玩家已离婚");
/*     */     }
/*     */     
/* 521 */     if (!((PlayerItem)toPlayer.getFeature(PlayerItem.class)).checkAndConsume(MarryConfig.getMarryDivorceApplyCostId(), MarryConfig.getMarryDivorceApplyCostCount(), 
/* 522 */         ItemFlow.MarryApply)) {
/* 523 */       throw new WSException(ErrorCode.NotEnough_Crystal, "对方缺少离婚许可");
/*     */     }
/*     */     
/* 526 */     to_feature.divorce();
/* 527 */     bo.del();
/* 528 */     to_feature.divorceApplys = null;
/* 529 */     toPlayer.pushProto("beDivorce", to_feature.getLoveInfo());
/* 530 */     divorce();
/*     */     
/* 532 */     removeApply(this.divorceApplys);
/* 533 */     this.receiveDivorceApplys.remove(bo);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLeftTime(MarryApplyBO bo) {
/* 538 */     return Math.max(0, RefDataMgr.getFactor("MarryApplyTime", 1800) - CommTime.nowSecond() - bo.getApplyTime());
/*     */   }
/*     */   
/*     */   public int getDivorceLeftTime(MarryDivorceApplyBO bo) {
/* 542 */     return Math.max(0, RefDataMgr.getFactor("MarryApplyTime", 1800) - CommTime.nowSecond() - bo.getApplyTime());
/*     */   }
/*     */   
/*     */   public LoverInfo getLoveInfo() {
/* 546 */     if (this.bo.getSex() == 0 || this.bo.getLoverPid() == 0L) {
/* 547 */       return null;
/*     */     }
/*     */     
/* 550 */     LoverInfo info = new LoverInfo();
/* 551 */     if (this.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
/* 552 */       info.setHusband(((PlayerBase)this.player.getFeature(PlayerBase.class)).summary());
/* 553 */       info.setWife(((PlayerBase)PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid()).getFeature(PlayerBase.class)).summary());
/*     */     } else {
/* 555 */       info.setHusband(((PlayerBase)PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid()).getFeature(PlayerBase.class)).summary());
/* 556 */       info.setWife(((PlayerBase)this.player.getFeature(PlayerBase.class)).summary());
/*     */     } 
/*     */     
/* 559 */     info.setLevel(this.bo.getLevel());
/* 560 */     info.setExp(this.bo.getExp());
/*     */     
/* 562 */     PlayerRecord record = (PlayerRecord)this.player.getFeature(PlayerRecord.class);
/* 563 */     int times = record.getValue(ConstEnum.DailyRefresh.LoversSend);
/* 564 */     info.setFlowerTimes(times);
/*     */     
/* 566 */     info.setAlreadyPick(StringUtils.string2Integer(this.bo.getLevelReward()));
/* 567 */     return info;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMarried() {
/* 572 */     return (this.bo.getMarried() != 0);
/*     */   }
/*     */   
/*     */   public int getSex() {
/* 576 */     return this.bo.getSex();
/*     */   }
/*     */   
/*     */   public int signIn() throws WSException {
/* 580 */     if (!isMarried()) {
/* 581 */       throw new WSException(ErrorCode.Marry_NotYet, "尚未结婚");
/*     */     }
/*     */     
/* 584 */     if (this.bo.getIsSign()) {
/* 585 */       throw new WSException(ErrorCode.Marry_SignAlready, "已签到");
/*     */     }
/*     */     
/* 588 */     this.bo.saveSignin(getSignin() + 1);
/* 589 */     this.bo.saveIsSign(true);
/* 590 */     getLover().pushProto("LoverSign", Integer.valueOf(getSignin()));
/* 591 */     return getSignin();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignin() {
/* 596 */     return this.bo.getSignin();
/*     */   }
/*     */   
/*     */   public Reward pickSignInReward(int signId) throws WSException {
/* 600 */     RefSignIn ref = (RefSignIn)RefDataMgr.get(RefSignIn.class, Integer.valueOf(signId));
/* 601 */     if (ref == null) {
/* 602 */       throw new WSException(ErrorCode.Marry_SignNotEnough, "签到未找到");
/*     */     }
/*     */     
/* 605 */     if (ref.Day > getSignin() || ref.Day > getLoverFeature().getSignin()) {
/* 606 */       throw new WSException(ErrorCode.Marry_SignNotEnough, "签到天数不足");
/*     */     }
/* 608 */     List<Integer> pickReward = StringUtils.string2Integer(this.bo.getSignReward());
/* 609 */     if (pickReward.contains(Integer.valueOf(signId))) {
/* 610 */       throw new WSException(ErrorCode.Marry_SignAlreadyPick, "签到奖励已领取");
/*     */     }
/* 612 */     Reward reward = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(ref.UniformitemId, ref.Count, ItemFlow.MarrySign);
/* 613 */     pickReward.add(Integer.valueOf(signId));
/* 614 */     this.bo.saveSignReward(StringUtils.list2String(pickReward));
/* 615 */     return reward;
/*     */   }
/*     */   
/*     */   public Reward pickLevelReward(int level) throws WSException {
/* 619 */     RefMarryLevel ref = (RefMarryLevel)RefDataMgr.get(RefMarryLevel.class, Integer.valueOf(level));
/* 620 */     if (ref == null) {
/* 621 */       throw new WSException(ErrorCode.Marry_LevelNotFound, "等级未找到");
/*     */     }
/* 623 */     if (ref.id > this.bo.getLevel()) {
/* 624 */       throw new WSException(ErrorCode.Marry_LevelNotEnough, "恩爱等级不足");
/*     */     }
/* 626 */     List<Integer> pickReward = StringUtils.string2Integer(this.bo.getLevelReward());
/* 627 */     if (pickReward.contains(Integer.valueOf(level))) {
/* 628 */       throw new WSException(ErrorCode.Marry_SignAlreadyPick, "奖励已领取");
/*     */     }
/* 630 */     Reward reward = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.RewardId))).genReward(), ItemFlow.MarrySign);
/* 631 */     pickReward.add(Integer.valueOf(level));
/* 632 */     this.bo.saveLevelReward(StringUtils.list2String(pickReward));
/* 633 */     return reward;
/*     */   }
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/* 638 */       this.bo.saveIsSign(false);
/* 639 */     } catch (Exception e) {
/*     */       
/* 641 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/marry/MarryFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */