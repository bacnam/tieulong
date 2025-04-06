/*     */ package business.gmcmd.cmds;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.DailySign;
/*     */ import business.global.activity.detail.FirstRecharge;
/*     */ import business.global.arena.ArenaManager;
/*     */ import business.global.chat.ChatMgr;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.recharge.RechargeMgr;
/*     */ import business.gmcmd.annotation.Command;
/*     */ import business.gmcmd.annotation.Commander;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.WarSpiritFeature;
/*     */ import business.player.feature.pve.DungeonFeature;
/*     */ import business.player.feature.pvp.DroiyanFeature;
/*     */ import business.player.feature.pvp.StealGoldFeature;
/*     */ import business.player.feature.task.TaskActivityFeature;
/*     */ import business.player.feature.treasure.FindTreasureFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.enums.ChatType;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefDroiyanTreasure;
/*     */ import core.config.refdata.ref.RefDungeon;
/*     */ import core.config.refdata.ref.RefRecharge;
/*     */ import core.config.refdata.ref.RefWarSpirit;
/*     */ import core.database.game.bo.RechargeOrderBO;
/*     */ import core.network.proto.AchieveInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Commander(name = "player", comment = "玩家相关命令")
/*     */ public class CmdPlayer
/*     */ {
/*     */   @Command(comment = "获得物品[uniformId][数量]")
/*     */   public String gain(Player player, int item, int value) {
/*  49 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(item, value, ItemFlow.Command);
/*  50 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "发送邮件")
/*     */   public String mail(Player player, int mailId, String param) {
/*  55 */     MailCenter.getInstance().sendMail(player.getPid(), mailId, new String[] { param });
/*  56 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "修正最后登出时间，参数为时间差")
/*     */   public String offline(Player player, int offset) {
/*  61 */     player.getPlayerBO().saveLastLogout(CommTime.nowSecond() - offset);
/*  62 */     ((DungeonFeature)player.getFeature(DungeonFeature.class)).calcOfflineReward();
/*  63 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   @Command(comment = "设置关卡等级")
/*     */   public String dungeon(Player player, int level) {
/*  69 */     if (level > RefDataMgr.getAll(RefDungeon.class).size())
/*  70 */       return "fail"; 
/*  71 */     player.getPlayerBO().saveDungeonLv(level);
/*  72 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "跑马灯")
/*     */   public String marquee(Player player, String parms) {
/*  77 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.UnlockChar, new String[] { parms });
/*  78 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "寻宝")
/*     */   public Reward find(Player player, int times) {
/*  83 */     Reward reward = ((FindTreasureFeature)player.getFeature(FindTreasureFeature.class)).findTreasure(times);
/*  84 */     return reward;
/*     */   }
/*     */   
/*     */   @Command(comment = "充值传参商品id")
/*     */   public String recharge(Player player, String goodis) {
/*  89 */     if (RefDataMgr.get(RefRecharge.class, goodis) == null) {
/*  90 */       return "查无此商品" + goodis + "，请重新确定";
/*     */     }
/*  92 */     RechargeOrderBO bo = new RechargeOrderBO();
/*  93 */     bo.setCporderid("模拟cporderid" + CommTime.nowMS());
/*  94 */     bo.setCarrier("模拟carrier");
/*  95 */     bo.setPlatform("模拟platform");
/*  96 */     bo.setAdfrom("模拟 adfrom");
/*  97 */     bo.setAdfrom2("模拟 adfrom2");
/*  98 */     bo.setGameid("模拟 gameid");
/*  99 */     bo.setServerID("模拟 server_id");
/* 100 */     bo.setAppID("模拟 appid");
/* 101 */     bo.setOpenId(player.getPlayerBO().getOpenId());
/* 102 */     bo.setPid(player.getPid());
/* 103 */     bo.setAdfromOrderid("模拟  adfrom_orderid");
/* 104 */     bo.setQuantity(1);
/* 105 */     bo.setProductid(goodis);
/* 106 */     bo.setStatus(RechargeMgr.OrderStatus.Paid.toString());
/* 107 */     bo.insert();
/*     */     
/* 109 */     RechargeMgr.getInstance().sendPrize(bo);
/* 110 */     return "充值完毕";
/*     */   }
/*     */   
/*     */   @Command(comment = "清空排行榜")
/*     */   public String clearrank(Player player, RankType type) {
/* 115 */     RankManager.getInstance().clear(type);
/* 116 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "查看任务 信息")
/*     */   public String tasklist(Player player) {
/* 121 */     AchievementFeature achievementContainer = (AchievementFeature)player.getFeature(AchievementFeature.class);
/* 122 */     List<AchieveInfo> achieveInfoList = achievementContainer.loadAchieveList();
/* 123 */     TaskActivityFeature taskActivityFeature = (TaskActivityFeature)player.getFeature(TaskActivityFeature.class);
/* 124 */     taskActivityFeature.pushTaskActiveInfo();
/* 125 */     return achieveInfoList.toString();
/*     */   }
/*     */   
/*     */   @Command(comment = "完成任务")
/*     */   public String taskpick(Player player, int id) {
/* 130 */     AchievementFeature achievementContainer = (AchievementFeature)player.getFeature(AchievementFeature.class);
/* 131 */     List<AchieveInfo> achieveInfoList = achievementContainer.loadAchieveList();
/* 132 */     TaskActivityFeature taskActivityFeature = (TaskActivityFeature)player.getFeature(TaskActivityFeature.class);
/* 133 */     taskActivityFeature.pushTaskActiveInfo();
/* 134 */     return achieveInfoList.toString();
/*     */   }
/*     */   
/*     */   @Command(comment = "模拟世界聊天:内容")
/*     */   public String worldchat(Player player, String content) throws WSException {
/* 139 */     ChatMgr.getInstance().addChat(player, content, ChatType.CHATTYPE_WORLD, 0L);
/* 140 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "模拟首冲")
/*     */   public String firstrecharge(Player player) {
/* 145 */     ((FirstRecharge)ActivityMgr.getActivity(FirstRecharge.class)).sendFirstRechargeReward(player);
/* 146 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "每日签到刷新")
/*     */   public String dailySignInRefresh(Player player) {
/* 151 */     ((DailySign)ActivityMgr.getActivity(DailySign.class)).handDailyRefresh(player, 1);
/* 152 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "发放竞技场奖励")
/*     */   public String arenareward(Player player) {
/* 157 */     ArenaManager.getInstance().settle();
/* 158 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "发放决战奖励")
/*     */   public String droiyanreward(Player player) {
/* 163 */     RankManager.getInstance().settle(RankType.Droiyan, true);
/* 164 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "添加仇人")
/*     */   public String addEnemy(Player player) {
/* 169 */     int rank = RankManager.getInstance().getRank(RankType.Power, player.getPid());
/* 170 */     int Max = Math.min(rank + 20, RankManager.getInstance().getRankSize(RankType.Power));
/* 171 */     int begin = Math.max(rank + 1, 1);
/*     */     
/* 173 */     List<Long> rankList = new ArrayList<>();
/* 174 */     for (int i = begin; i < Max; i++) {
/* 175 */       long pid = RankManager.getInstance().getPlayerId(RankType.Power, i);
/* 176 */       rankList.add(Long.valueOf(pid));
/*     */     } 
/*     */     
/* 179 */     Collections.shuffle(rankList);
/* 180 */     if (rankList.size() >= 1)
/* 181 */       ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).addEnemy(((Long)rankList.get(0)).longValue()); 
/* 182 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "添加仇人")
/*     */   public String addDroiyan(Player player, long pid) {
/* 187 */     ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).addDroiyan(pid);
/* 188 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "解锁战灵")
/*     */   public String unlockspirit(Player player) {
/* 193 */     for (RefWarSpirit ref : RefDataMgr.getAll(RefWarSpirit.class).values()) {
/*     */       try {
/* 195 */         WarSpiritFeature feature = (WarSpiritFeature)player.getFeature(WarSpiritFeature.class);
/* 196 */         if (feature.getWarSpirit(ref.id) == null)
/* 197 */           ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).unlockWarSpirit(ref.id); 
/* 198 */       } catch (WSException e) {
/* 199 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 202 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "决战宝图")
/*     */   public String droiyantreasure(Player player, int id) {
/* 207 */     ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).addTreasure((RefDroiyanTreasure)RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(id)));
/* 208 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "刷新探金手玩家列表")
/*     */   public String refreshstealgold(Player player) {
/* 213 */     ((StealGoldFeature)player.getFeature(StealGoldFeature.class)).search();
/* 214 */     return "OK";
/*     */   }
/*     */   
/*     */   @Command(comment = "踢出所有人")
/*     */   public String kickall(Player player) {
/* 219 */     PlayerMgr.getInstance().kickoutAllPlayer();
/* 220 */     return "ok";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */