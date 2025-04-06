/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.activity.detail.RankArena;
/*    */ import business.global.activity.detail.RankArtifice;
/*    */ import business.global.activity.detail.RankDroiyan;
/*    */ import business.global.activity.detail.RankDungeon;
/*    */ import business.global.activity.detail.RankGuild;
/*    */ import business.global.activity.detail.RankGumu;
/*    */ import business.global.activity.detail.RankLevel;
/*    */ import business.global.activity.detail.RankPower;
/*    */ import business.global.activity.detail.RankTianLong;
/*    */ import business.global.activity.detail.RankWing;
/*    */ import business.global.activity.detail.RankXiaoyao;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RankRewardOpenServerAward
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     ConstEnum.RankRewardType type;
/*    */     int awardId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 38 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 39 */     int awardId = req.awardId;
/* 40 */     if (awardId < 0) {
/* 41 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数awardId=%s", new Object[] { Integer.valueOf(awardId) });
/*    */     }
/* 43 */     RankActivity rankActivity = null;
/*    */     
/* 45 */     switch (req.type) {
/*    */       case WingRank:
/* 47 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankWing.class);
/*    */         break;
/*    */       case DungeonRank:
/* 50 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankDungeon.class);
/*    */         break;
/*    */       case LevelRank:
/* 53 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankLevel.class);
/*    */         break;
/*    */       case PowerRank:
/* 56 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankPower.class);
/*    */         break;
/*    */       case DroiyanRank:
/* 59 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankDroiyan.class);
/*    */         break;
/*    */       case null:
/* 62 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankArena.class);
/*    */         break;
/*    */       case GumuRank:
/* 65 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankGumu.class);
/*    */         break;
/*    */       case TianLongRank:
/* 68 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankTianLong.class);
/*    */         break;
/*    */       case XiaoyaoRank:
/* 71 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankXiaoyao.class);
/*    */         break;
/*    */       case ArtificeRank:
/* 74 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankArtifice.class);
/*    */         break;
/*    */       case GuildRank:
/* 77 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankGuild.class);
/*    */         break;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 84 */     if (rankActivity.getStatus() == ActivityStatus.Close) {
/* 85 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { rankActivity.getType() });
/*    */     }
/*    */     
/* 88 */     request.response(rankActivity.doWeeklyReceive(player, awardId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/RankRewardOpenServerAward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */