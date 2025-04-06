/*    */ package core.network.client2game.handler.pvc;
/*    */ 
/*    */ import business.global.worldboss.WorldBossMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.worldboss.WorldBossFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.database.game.bo.WorldBossBO;
/*    */ import core.database.game.bo.WorldBossChallengeBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class WorldBossEnter
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request {
/*    */     int bossId;
/*    */   }
/*    */   
/*    */   public class FightInfo1 {
/*    */     List<Player.FightInfo> players;
/*    */     WorldBossBO boss;
/*    */     
/*    */     public FightInfo1(List<Player.FightInfo> fullInfo, WorldBossBO boss) {
/* 31 */       this.players = fullInfo;
/* 32 */       this.boss = boss;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 38 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 39 */     WorldBossChallengeBO challengBo = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).beginFightWorldBoss(req.bossId);
/* 40 */     WorldBossBO boss = WorldBossMgr.getInstance().getBO(req.bossId);
/* 41 */     List<Player> players = WorldBossMgr.getInstance().getPlayerList(boss);
/* 42 */     player.pushProto("challengInfo", challengBo);
/* 43 */     List<Player.FightInfo> fullInfoList = new ArrayList<>();
/* 44 */     for (Player tmpPlay : players) {
/* 45 */       if (tmpPlay != null && tmpPlay != player)
/* 46 */         fullInfoList.add(((PlayerBase)tmpPlay.getFeature(PlayerBase.class)).fightInfo()); 
/* 47 */       if (fullInfoList.size() > RefDataMgr.getFactor("MaxWorldBossRole", 15)) {
/*    */         break;
/*    */       }
/*    */     } 
/*    */     
/* 52 */     int size = fullInfoList.size();
/* 53 */     for (int i = 0; i < RefDataMgr.getFactor("MaxWorldBossRobot", 15) - 1 - size; i++) {
/* 54 */       fullInfoList.add(((PlayerBase)((Player)(WorldBossMgr.getInstance()).robotPlayers.get(i)).getFeature(PlayerBase.class)).fightInfo());
/*    */     }
/* 56 */     request.response(new FightInfo1(fullInfoList, boss));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvc/WorldBossEnter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */