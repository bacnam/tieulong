/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.task.TaskActivityFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.utils.StringUtils;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefDailyActive;
/*    */ import core.config.refdata.ref.RefReward;
/*    */ import core.database.game.bo.DailyactiveBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class PickUpTaskActivePrize
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int index;
/*    */   }
/*    */   
/*    */   public static class Response
/*    */   {
/*    */     int index;
/*    */     Reward reward;
/*    */     
/*    */     private Response(int index, Reward reward) {
/* 35 */       this.index = index;
/* 36 */       this.reward = reward;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 43 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 44 */     int rewardIndex = req.index;
/*    */     
/* 46 */     TaskActivityFeature taskActivityFeature = (TaskActivityFeature)player.getFeature(TaskActivityFeature.class);
/* 47 */     DailyactiveBO dailyactiveBO = taskActivityFeature.getOrCreate();
/*    */     
/* 49 */     List<Integer> fetchedTaskIndex = StringUtils.string2Integer(dailyactiveBO.getFetchedTaskIndex());
/* 50 */     if (fetchedTaskIndex.contains(Integer.valueOf(rewardIndex))) {
/* 51 */       throw new WSException(ErrorCode.NotEnough_TaskActiveValue, "奖励已领取过");
/*    */     }
/*    */     
/* 54 */     int value = dailyactiveBO.getValue();
/* 55 */     int refId = ((Integer)RefDailyActive.level2refId.get(Integer.valueOf(dailyactiveBO.getTeamLevel()))).intValue();
/* 56 */     RefDailyActive refDailyActive = (RefDailyActive)RefDataMgr.get(RefDailyActive.class, Integer.valueOf(refId));
/* 57 */     int needValue = ((Integer)refDailyActive.Condition.get(rewardIndex)).intValue();
/*    */     
/* 59 */     if (value < needValue) {
/* 60 */       throw new WSException(ErrorCode.NotEnough_TaskActiveValue, "玩家 %s 当前活跃度:%s 需要活跃度 : %s  ", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(value), Integer.valueOf(needValue) });
/*    */     }
/*    */     
/* 63 */     int rewardId = ((Integer)refDailyActive.RewardID.get(rewardIndex)).intValue();
/*    */     
/* 65 */     Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(rewardId))).genReward();
/*    */     
/* 67 */     fetchedTaskIndex.add(Integer.valueOf(rewardIndex));
/* 68 */     dailyactiveBO.saveFetchedTaskIndex(StringUtils.list2String(fetchedTaskIndex));
/* 69 */     Reward prize = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.TaskActiveGain);
/*    */     
/* 71 */     taskActivityFeature.pushTaskActiveInfo();
/* 72 */     request.response(new Response(req.index, prize, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/PickUpTaskActivePrize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */