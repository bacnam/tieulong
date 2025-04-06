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
/*    */ public class RankRewardOpenServerPick
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     ConstEnum.RankRewardType type;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 37 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 39 */     RankActivity rankActivity = null;
/*    */     
/* 41 */     switch (req.type) {
/*    */       case WingRank:
/* 43 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankWing.class);
/*    */         break;
/*    */       case DungeonRank:
/* 46 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankDungeon.class);
/*    */         break;
/*    */       case LevelRank:
/* 49 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankLevel.class);
/*    */         break;
/*    */       case PowerRank:
/* 52 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankPower.class);
/*    */         break;
/*    */       case DroiyanRank:
/* 55 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankDroiyan.class);
/*    */         break;
/*    */       case null:
/* 58 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankArena.class);
/*    */         break;
/*    */       case GumuRank:
/* 61 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankGumu.class);
/*    */         break;
/*    */       case TianLongRank:
/* 64 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankTianLong.class);
/*    */         break;
/*    */       case XiaoyaoRank:
/* 67 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankXiaoyao.class);
/*    */         break;
/*    */       case ArtificeRank:
/* 70 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankArtifice.class);
/*    */         break;
/*    */       case GuildRank:
/* 73 */         rankActivity = (RankActivity)ActivityMgr.getActivity(RankGuild.class);
/*    */         break;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 79 */     if (rankActivity.getStatus() == ActivityStatus.Close) {
/* 80 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { rankActivity.getType() });
/*    */     }
/*    */     
/* 83 */     request.response(rankActivity.pickUpReward(player));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/RankRewardOpenServerPick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */