/*     */ package core.network.client2game.handler.activity;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.RankActivity;
/*     */ import business.global.activity.detail.RankArena;
/*     */ import business.global.activity.detail.RankArtifice;
/*     */ import business.global.activity.detail.RankDroiyan;
/*     */ import business.global.activity.detail.RankDungeon;
/*     */ import business.global.activity.detail.RankGuild;
/*     */ import business.global.activity.detail.RankGumu;
/*     */ import business.global.activity.detail.RankLevel;
/*     */ import business.global.activity.detail.RankPower;
/*     */ import business.global.activity.detail.RankTianLong;
/*     */ import business.global.activity.detail.RankWing;
/*     */ import business.global.activity.detail.RankXiaoyao;
/*     */ import business.global.guild.Guild;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import core.network.proto.VipAwardInfo;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RankRewardOpenServer
/*     */   extends PlayerHandler
/*     */ {
/*     */   class Request
/*     */   {
/*     */     ConstEnum.RankRewardType type;
/*     */   }
/*     */   
/*     */   private static class RankReward
/*     */   {
/*     */     String firstName;
/*     */     int firstVip;
/*     */     int myRank;
/*     */     Reward reward;
/*     */     int cost;
/*     */     int require;
/*     */     boolean isPicked;
/*     */     List<VipAwardInfo> vipawards;
/*     */     List<RankActivity.RankAward> rankrewards;
/*     */     
/*     */     private RankReward(String firstName, int firstVip, int myRank, Reward reward, int cost, int require, boolean isPicked, List<VipAwardInfo> vipawards, List<RankActivity.RankAward> rankrewards) {
/*  57 */       this.firstName = firstName;
/*  58 */       this.firstVip = firstVip;
/*  59 */       this.myRank = myRank;
/*  60 */       this.reward = reward;
/*  61 */       this.cost = cost;
/*  62 */       this.require = require;
/*  63 */       this.isPicked = isPicked;
/*  64 */       this.vipawards = vipawards;
/*  65 */       this.rankrewards = rankrewards;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  72 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*     */     
/*  74 */     RankActivity rankActivity = null;
/*  75 */     RankType type = null;
/*     */     
/*  77 */     switch (req.type) {
/*     */       case WingRank:
/*  79 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankWing.class);
/*  80 */         type = RankType.WingLevel;
/*     */         break;
/*     */       case DungeonRank:
/*  83 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankDungeon.class);
/*  84 */         type = RankType.Dungeon;
/*     */         break;
/*     */       case LevelRank:
/*  87 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankLevel.class);
/*  88 */         type = RankType.Level;
/*     */         break;
/*     */       case PowerRank:
/*  91 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankPower.class);
/*  92 */         type = RankType.Power;
/*     */         break;
/*     */       case DroiyanRank:
/*  95 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankDroiyan.class);
/*  96 */         type = RankType.Droiyan;
/*     */         break;
/*     */       case null:
/*  99 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankArena.class);
/* 100 */         type = RankType.Arena;
/*     */         break;
/*     */       case GumuRank:
/* 103 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankGumu.class);
/* 104 */         type = RankType.GuMuPower;
/*     */         break;
/*     */       case TianLongRank:
/* 107 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankTianLong.class);
/* 108 */         type = RankType.TianLongPower;
/*     */         break;
/*     */       case XiaoyaoRank:
/* 111 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankXiaoyao.class);
/* 112 */         type = RankType.XiaoYaoPower;
/*     */         break;
/*     */       case ArtificeRank:
/* 115 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankArtifice.class);
/* 116 */         type = RankType.Artifice;
/*     */         break;
/*     */       case GuildRank:
/* 119 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankGuild.class);
/* 120 */         type = RankType.Guild;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 126 */     if (rankActivity.getStatus() == ActivityStatus.Close) {
/* 127 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { rankActivity.getType() });
/*     */     }
/*     */     
/* 130 */     String firstName = "";
/* 131 */     int firstVip = 0;
/* 132 */     long firstId = RankManager.getInstance().getPlayerId(type, 1);
/*     */     
/* 134 */     if (PlayerMgr.getInstance().getPlayer(firstId) != null) {
/* 135 */       firstName = PlayerMgr.getInstance().getPlayer(firstId).getName();
/* 136 */       firstVip = PlayerMgr.getInstance().getPlayer(firstId).getVipLevel();
/*     */     } 
/* 138 */     int myRank = 0;
/* 139 */     if (type == RankType.Guild) {
/* 140 */       Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 141 */       if (guild != null)
/* 142 */         myRank = RankManager.getInstance().getRank(type, guild.getGuildId()); 
/*     */     } else {
/* 144 */       myRank = RankManager.getInstance().getRank(type, player.getPid());
/*     */     } 
/* 146 */     Reward reward = rankActivity.getReward();
/* 147 */     int cost = rankActivity.getCost(player);
/* 148 */     int require = rankActivity.getRequire_cost();
/*     */     
/* 150 */     request.response(new RankReward(firstName, firstVip, myRank, reward, cost, require, rankActivity.isPicked(player), rankActivity.vipAwardProto(player), 
/* 151 */           rankActivity.rankrewardList, null));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/RankRewardOpenServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */