/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import business.player.feature.task.TaskActivityFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.AchieveInfo;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class AchieveList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 18 */     AchievementFeature achievementContainer = (AchievementFeature)player.getFeature(AchievementFeature.class);
/* 19 */     List<AchieveInfo> achieveInfoList = achievementContainer.loadAchieveList();
/* 20 */     TaskActivityFeature taskActivityFeature = (TaskActivityFeature)player.getFeature(TaskActivityFeature.class);
/* 21 */     taskActivityFeature.pushTaskActiveInfo();
/* 22 */     request.response(achieveInfoList);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/AchieveList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */