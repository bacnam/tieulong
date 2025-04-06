/*     */ package business.player.feature;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.LevelReward;
/*     */ import business.global.activity.detail.RankLevel;
/*     */ import business.global.fight.Fighter;
/*     */ import business.global.guild.Guild;
/*     */ import business.global.guild.GuildWarConfig;
/*     */ import business.global.guild.GuildWarMgr;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.worldboss.WorldBossMgr;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.character.DressFeature;
/*     */ import business.player.feature.character.Equip;
/*     */ import business.player.feature.character.WarSpiritFeature;
/*     */ import business.player.feature.features.MailFeature;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.feature.features.RechargeFeature;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import business.player.feature.marry.MarryFeature;
/*     */ import business.player.feature.player.LingBaoFeature;
/*     */ import business.player.feature.player.NewTitleFeature;
/*     */ import business.player.feature.pve.DungeonFeature;
/*     */ import business.player.feature.pve.InstanceFeature;
/*     */ import business.player.feature.store.PlayerStore;
/*     */ import business.player.feature.store.StoreFeature;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.DressType;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.InstanceType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.enums.StoreType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.http.client.IResponseHandler;
/*     */ import com.zhonglian.server.http.server.GMParam;
/*     */ import com.zhonglian.server.http.server.HttpUtils;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefGuildJobInfo;
/*     */ import core.config.refdata.ref.RefRobot;
/*     */ import core.config.refdata.ref.RefVIP;
/*     */ import core.database.game.bo.CharacterBO;
/*     */ import core.database.game.bo.DressBO;
/*     */ import core.database.game.bo.InstanceInfoBO;
/*     */ import core.database.game.bo.PlayerBO;
/*     */ import core.database.game.bo.WorldBossBO;
/*     */ import core.network.proto.Player;
/*     */ import core.network.proto.WarSpiritInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import proto.gamezone.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerBase
/*     */   extends Feature
/*     */ {
/*     */   public int chatTime;
/*     */   public int worldChatTime;
/*     */   public int gmChatTime;
/*     */   
/*     */   public PlayerBase(Player data) {
/*  77 */     super(data);
/*     */ 
/*     */     
/*  80 */     this.chatTime = 0;
/*  81 */     this.worldChatTime = 0;
/*  82 */     this.gmChatTime = 0;
/*     */   }
/*     */   
/*     */   public void onCreate(int selected) throws WSException {
/*  86 */     ((CharFeature)this.player.getFeature(CharFeature.class)).unlockChar(selected, ItemFlow.PlayerCreate);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCreate(RefRobot init) throws WSException {
/*  91 */     List<Integer> simulateTeam = init.randTeam();
/*  92 */     CharFeature feature = (CharFeature)this.player.getFeature(CharFeature.class);
/*  93 */     int leader = 0;
/*  94 */     for (Integer simulateId : simulateTeam) {
/*  95 */       int cardid = feature.simulate(simulateId);
/*  96 */       if (leader == 0)
/*  97 */         leader = cardid; 
/*     */     } 
/*  99 */     this.player.getPlayerBO().saveIcon(leader);
/* 100 */     feature.updateRobotPower();
/*     */   }
/*     */   
/*     */   public void bannedLogin(int duration) {
/* 104 */     if (duration > 0) {
/* 105 */       duration = CommTime.nowSecond() + duration;
/*     */     }
/* 107 */     int expiredTime = duration;
/* 108 */     this.player.getPlayerBO().saveBannedLoginExpiredTime(expiredTime);
/* 109 */     if (expiredTime != 0) {
/* 110 */       this.player.getPlayerBO().saveBannedTimes(this.player.getPlayerBO().getBannedTimes() + 1);
/* 111 */       ((PlayerBase)this.player.getFeature(PlayerBase.class)).onBanned(this.player);
/* 112 */       kickout();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void kickout() {
/* 117 */     this.player.pushProto("kickout", " ");
/* 118 */     if (this.player.getClientSession() != null) {
/* 119 */       this.player.getClientSession().close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onLevelUp(int nowLvl) {
/* 124 */     ((CharFeature)this.player.getFeature(CharFeature.class)).updateCharPower();
/*     */ 
/*     */     
/* 127 */     RankManager.getInstance().update(RankType.Level, this.player.getPid(), nowLvl);
/*     */     
/* 129 */     AchievementFeature achievement = (AchievementFeature)this.player.getFeature(AchievementFeature.class);
/*     */     
/* 131 */     achievement.updateMax(Achievement.AchievementType.PlayerLevel, Integer.valueOf(nowLvl));
/*     */ 
/*     */     
/* 134 */     StoreFeature feature = (StoreFeature)this.player.getFeature(StoreFeature.class);
/* 135 */     PlayerStore playerStore = feature.getOrCreate(StoreType.ArenaStore);
/* 136 */     playerStore.doAutoRefresh();
/*     */ 
/*     */     
/* 139 */     ((RankLevel)ActivityMgr.getActivity(RankLevel.class)).UpdateMaxRequire_cost(this.player, nowLvl);
/*     */ 
/*     */     
/* 142 */     ((LevelReward)ActivityMgr.getActivity(LevelReward.class)).handLevelChange(this.player);
/*     */ 
/*     */     
/* 145 */     NoticeGmPlayerInfo();
/*     */ 
/*     */     
/* 148 */     PlayerMgr.getInstance().tryNotify(this.player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVipLevelUp(int nowLvl, int oldLvl) {
/* 153 */     RefVIP oldRef = (RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(oldLvl));
/* 154 */     RefVIP nowRef = (RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(nowLvl));
/* 155 */     int addPackage = nowRef.AddPackage - oldRef.AddPackage;
/* 156 */     this.player.getPlayerBO().saveExtPackage(this.player.getPlayerBO().getExtPackage() + addPackage);
/* 157 */     this.player.pushProto("extPackage", Integer.valueOf(this.player.getPlayerBO().getExtPackage()));
/*     */ 
/*     */     
/* 160 */     InstanceInfoBO instanceBO = ((InstanceFeature)this.player.getFeature(InstanceFeature.class)).getOrCreate(); byte b; int i; InstanceType[] arrayOfInstanceType;
/* 161 */     for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) { InstanceType type = arrayOfInstanceType[b];
/* 162 */       if (type == InstanceType.EquipInstance) {
/* 163 */         int addNum = nowRef.EquipInstanceTimes - oldRef.EquipInstanceTimes;
/* 164 */         instanceBO.setChallengTimes(type.ordinal(), instanceBO.getChallengTimes(type.ordinal()) + addNum);
/*     */       } 
/* 166 */       if (type == InstanceType.MeridianInstance) {
/* 167 */         int addNum = nowRef.MeridianInstanceTimes - oldRef.MeridianInstanceTimes;
/* 168 */         instanceBO.setChallengTimes(type.ordinal(), instanceBO.getChallengTimes(type.ordinal()) + addNum);
/*     */       } 
/* 170 */       if (type == InstanceType.GemInstance) {
/* 171 */         int addNum = nowRef.GemInstanceTimes - oldRef.GemInstanceTimes;
/* 172 */         instanceBO.setChallengTimes(type.ordinal(), instanceBO.getChallengTimes(type.ordinal()) + addNum);
/*     */       }  b++; }
/*     */     
/* 175 */     instanceBO.saveAll();
/*     */ 
/*     */     
/* 178 */     NoticeGmPlayerInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBanned(Player player) {
/* 184 */     RankManager.getInstance().clearPlayerData(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnect() {
/* 191 */     this.player.getDailyRefreshFeature().reload();
/*     */     
/* 193 */     this.player.getDailyRefreshFeature().process(CommTime.nowSecond());
/*     */     
/* 195 */     ((MailFeature)this.player.getFeature(MailFeature.class)).onConnect();
/*     */     
/* 197 */     ((DungeonFeature)this.player.getFeature(DungeonFeature.class)).calcOfflineReward();
/*     */     
/* 199 */     if (this.player.getVipLevel() >= RefDataMgr.getFactor("VIP_Marque", 6)) {
/* 200 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.VipLogin, new String[] { this.player.getVipLevel(), this.player.getName() });
/*     */     }
/* 202 */     if (RankManager.getInstance().getRank(RankType.Power, this.player.getPid()) == 1) {
/* 203 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.PowerLogin, new String[] { this.player.getName() });
/*     */     }
/*     */     
/* 206 */     NoticeGmPlayerInfo();
/*     */     
/* 208 */     this.player.getPlayerBO().saveOnlineUpdate(CommTime.nowSecond());
/*     */     
/* 210 */     PlayerMgr.getInstance().tryNotify(this.player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect() {
/* 215 */     this.player.getPlayerBO().saveLastLogout(CommTime.nowSecond());
/* 216 */     for (WorldBossBO bo : WorldBossMgr.getInstance().getBOList()) {
/* 217 */       WorldBossMgr.getInstance().doLeaveWorldBoss(this.player, (int)bo.getBossId());
/*     */     }
/*     */ 
/*     */     
/* 221 */     calOnlineTime();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMsg(String key) {}
/*     */ 
/*     */   
/*     */   public void sendMsg(String key, List<String> msgList) {}
/*     */ 
/*     */   
/*     */   public void sendSysMessage(String msg) {}
/*     */ 
/*     */   
/*     */   public void sendPopupMessage(String msg) {}
/*     */ 
/*     */   
/*     */   public Player.FullInfo fullInfo() {
/* 239 */     Player.FullInfo info = new Player.FullInfo();
/* 240 */     PlayerBO bo = this.player.getPlayerBO();
/* 241 */     info.pid = bo.getId();
/* 242 */     info.name = bo.getName();
/* 243 */     info.icon = bo.getIcon();
/* 244 */     info.createTime = bo.getCreateTime();
/* 245 */     info.vipLv = bo.getVipLevel();
/* 246 */     info.vipExp = bo.getVipExp();
/* 247 */     info.lv = bo.getLv();
/* 248 */     info.exp = bo.getExp();
/* 249 */     info.dungeonlv = bo.getDungeonLv();
/* 250 */     info.gold = bo.getGold();
/* 251 */     info.crystal = bo.getCrystal();
/* 252 */     info.strengthenMaterial = bo.getStrengthenMaterial();
/* 253 */     info.gemMaterial = bo.getGemMaterial();
/* 254 */     info.starMaterial = bo.getStarMaterial();
/* 255 */     info.merMaterial = bo.getMerMaterial();
/* 256 */     info.wingMaterial = bo.getWingMaterial();
/* 257 */     info.arenaToken = bo.getArenaToken();
/* 258 */     info.equipInstanceMaterial = bo.getEquipInstanceMaterial();
/* 259 */     info.gemInstanceMaterial = bo.getGemInstanceMaterial();
/* 260 */     info.meridianInstanceMaterial = bo.getMeridianInstanceMaterial();
/* 261 */     info.redPiece = bo.getRedPiece();
/* 262 */     info.extPackage = bo.getExtPackage();
/* 263 */     info.artificeMaterial = bo.getArtificeMaterial();
/* 264 */     info.warspiritTalentMaterial = bo.getWarspiritTalentMaterial();
/* 265 */     info.warspiritLv = bo.getWarspiritLv();
/* 266 */     info.warspiritExp = bo.getWarspiritExp();
/* 267 */     info.warspiritTalent = bo.getWarspiritTalent();
/* 268 */     info.lottery = bo.getLottery();
/* 269 */     info.dressMaterial = bo.getDressMaterial();
/*     */     
/* 271 */     GuildMemberFeature guildMemberFeature = (GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class);
/* 272 */     if (guildMemberFeature.getGuild() != null) {
/* 273 */       info.guildSkill = guildMemberFeature.getGuildSkillList();
/*     */     }
/*     */     
/* 276 */     PlayerRecord recorder = (PlayerRecord)this.player.getFeature(PlayerRecord.class);
/* 277 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.PackageBuyTimes);
/* 278 */     info.buyPackageTimes = curTimes;
/* 279 */     info.dresses = ((DressFeature)this.player.getFeature(DressFeature.class)).getAllDressInfo();
/* 280 */     info.characters = ((CharFeature)this.player.getFeature(CharFeature.class)).getAllInfo();
/* 281 */     info.warspirits = ((WarSpiritFeature)this.player.getFeature(WarSpiritFeature.class)).getAllInfo();
/* 282 */     info.sex = ((MarryFeature)this.player.getFeature(MarryFeature.class)).bo.getSex();
/* 283 */     return info;
/*     */   }
/*     */   
/*     */   public Player.FightInfo fightInfo() {
/* 287 */     Player.FightInfo info = new Player.FightInfo();
/* 288 */     PlayerBO bo = this.player.getPlayerBO();
/* 289 */     info.pid = bo.getId();
/* 290 */     info.name = bo.getName();
/* 291 */     info.icon = bo.getIcon();
/* 292 */     info.vipLv = bo.getVipLevel();
/* 293 */     info.vipExp = bo.getVipExp();
/* 294 */     info.lv = bo.getLv();
/* 295 */     info.warspiritLv = bo.getWarspiritLv();
/* 296 */     info.warspiritTalent = bo.getWarspiritTalent();
/*     */     
/* 298 */     info.characters = ((CharFeature)this.player.getFeature(CharFeature.class)).getAllInfo();
/* 299 */     if (((WarSpiritFeature)this.player.getFeature(WarSpiritFeature.class)).getWarSpiritNow() != null) {
/* 300 */       info.warspirit = new WarSpiritInfo(((WarSpiritFeature)this.player.getFeature(WarSpiritFeature.class)).getWarSpiritNow());
/*     */     } else {
/* 302 */       info.warspirit = null;
/* 303 */     }  info.title = ((NewTitleFeature)this.player.getFeature(NewTitleFeature.class)).getAllTitleInfo();
/* 304 */     info.LingBaoLevel = ((LingBaoFeature)this.player.getFeature(LingBaoFeature.class)).getLevel();
/* 305 */     GuildMemberFeature guildfeature = (GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class);
/* 306 */     Guild guild = guildfeature.getGuild();
/*     */     
/* 308 */     if (guild != null) {
/* 309 */       RefGuildJobInfo job = guildfeature.getJobRef();
/* 310 */       info.guildName = guild.getName();
/* 311 */       info.guildJob = job.JobName;
/*     */     } 
/* 313 */     return info;
/*     */   }
/*     */   
/*     */   public Player.Summary summary() {
/* 317 */     Player.Summary summary = new Player.Summary();
/* 318 */     PlayerBO bo = this.player.getPlayerBO();
/* 319 */     summary.pid = bo.getId();
/* 320 */     summary.name = bo.getName();
/* 321 */     summary.lv = bo.getLv();
/* 322 */     summary.icon = bo.getIcon();
/* 323 */     summary.vipLv = bo.getVipLevel();
/* 324 */     summary.power = ((CharFeature)this.player.getFeature(CharFeature.class)).getPower();
/* 325 */     summary.sex = ((MarryFeature)this.player.getFeature(MarryFeature.class)).bo.getSex();
/* 326 */     summary.is_married = ((MarryFeature)this.player.getFeature(MarryFeature.class)).isMarried();
/*     */     
/* 328 */     RechargeFeature rechargeFeature = (RechargeFeature)this.player.getFeature(RechargeFeature.class);
/* 329 */     int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
/* 330 */     summary.MonthCard = (monthNum > 0);
/* 331 */     int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
/* 332 */     summary.YearCard = (yearNum == -1);
/*     */     
/* 334 */     return summary;
/*     */   }
/*     */   
/*     */   public Player.showModle modle() {
/* 338 */     Player.showModle modle = new Player.showModle();
/* 339 */     PlayerBO bo = this.player.getPlayerBO();
/* 340 */     modle.pid = bo.getId();
/* 341 */     modle.name = bo.getName();
/* 342 */     modle.icon = bo.getIcon();
/* 343 */     List<Character> characters = 
/* 344 */       new ArrayList<>(((CharFeature)this.player.getFeature(CharFeature.class)).getAll().values());
/* 345 */     characters.sort((left, right) -> (left.getBo().getId() < right.getBo().getId()) ? -1 : 1);
/* 346 */     Character character = characters.get(0);
/* 347 */     CharacterBO bocha = character.getBo();
/*     */     
/* 349 */     Equip equip = character.getEquip(EquipPos.Body);
/* 350 */     int equipid = (equip == null) ? 0 : equip.getEquipId();
/* 351 */     modle.model = equipid;
/* 352 */     modle.wing = bocha.getWing();
/*     */     
/* 354 */     DressBO dressbody = character.getDress(DressType.Role);
/* 355 */     modle.modelDress = (dressbody == null) ? 0 : dressbody.getDressId();
/*     */     
/* 357 */     DressBO dresswing = character.getDress(DressType.Wing);
/* 358 */     modle.wingDress = (dresswing == null) ? 0 : dresswing.getDressId();
/*     */     
/* 360 */     if (GuildWarConfig.puppetPlayer.class.isInstance(this.player)) {
/* 361 */       GuildWarConfig.puppetPlayer p_player = (GuildWarConfig.puppetPlayer)this.player;
/* 362 */       modle.puppet_id = p_player.getPuppet_id();
/* 363 */       modle.is_puppet = p_player.isIs_puppet();
/*     */     } 
/*     */     
/* 366 */     Map<Integer, Fighter> fighters = ((CharFeature)this.player.getFeature(CharFeature.class)).getFighters();
/* 367 */     List<Double> maxhp = new ArrayList<>();
/* 368 */     for (Map.Entry<Integer, Fighter> pair : fighters.entrySet()) {
/* 369 */       Fighter fighter = pair.getValue();
/* 370 */       maxhp.add(Double.valueOf(fighter.attr(Attribute.MaxHP)));
/*     */     } 
/* 372 */     modle.maxhp = maxhp;
/*     */     
/* 374 */     List<Double> hp = (List<Double>)(GuildWarMgr.getInstance()).playersHP.get(Long.valueOf(this.player.getPid()));
/* 375 */     if (hp == null) {
/* 376 */       hp = maxhp;
/*     */     }
/* 378 */     modle.hp = hp;
/* 379 */     return modle;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {}
/*     */ 
/*     */   
/*     */   public Player.PlayerInfo zoneProto() {
/* 387 */     Player.PlayerInfo info = new Player.PlayerInfo();
/* 388 */     PlayerBO bo = this.player.getPlayerBO();
/*     */     
/* 390 */     info.pid = bo.getId();
/* 391 */     info.name = bo.getName();
/* 392 */     info.icon = bo.getIcon();
/* 393 */     info.vipLv = bo.getVipLevel();
/* 394 */     info.lv = bo.getLv();
/* 395 */     info.serverId = bo.getSid();
/* 396 */     info.maxPower = bo.getMaxFightPower();
/*     */     
/* 398 */     Guild guild = ((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).getGuild();
/* 399 */     if (guild != null) {
/* 400 */       info.guildID = guild.getGuildId();
/* 401 */       info.guildName = guild.getName();
/*     */     } else {
/* 403 */       info.guildID = 0L;
/* 404 */       info.guildName = "";
/*     */     } 
/* 406 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void NoticeGmPlayerInfo() {
/* 412 */     GMParam params = new GMParam();
/* 413 */     params.put("open_id", this.player.getOpenId());
/* 414 */     params.put("name", this.player.getName());
/* 415 */     params.put("level", Integer.valueOf(this.player.getLv()));
/* 416 */     params.put("icon", Integer.valueOf(this.player.getPlayerBO().getIcon()));
/* 417 */     params.put("vip", Integer.valueOf(this.player.getVipLevel()));
/* 418 */     params.put("world_id", System.getProperty("world_sid", "0"));
/* 419 */     params.put("server_id", Integer.valueOf(this.player.getSid()));
/*     */     
/* 421 */     String baseurl = System.getProperty("downConfUrl");
/* 422 */     HttpUtils.RequestGM("http://" + baseurl + "/gm/user!info", params, new IResponseHandler()
/*     */         {
/*     */           public void compeleted(String response) {
/*     */             try {
/* 426 */               JsonObject json = (new JsonParser()).parse(response).getAsJsonObject();
/* 427 */               if (json.get("state").getAsInt() != 1000) {
/* 428 */                 CommLog.error("发送GM用户信息失败" + json.get("state").getAsInt());
/*     */               }
/* 430 */             } catch (Exception exception) {}
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void failed(Exception exception) {
/* 436 */             CommLog.error("发送GM用户信息失败");
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void calOnlineTime() {
/* 442 */     int time = CommTime.nowSecond() - this.player.getPlayerBO().getOnlineUpdate();
/* 443 */     ((PlayerRecord)this.player.getFeature(PlayerRecord.class)).addValue(ConstEnum.DailyRefresh.OnlineSecond, time);
/* 444 */     this.player.getPlayerBO().saveOnlineUpdate(CommTime.nowSecond());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/PlayerBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */