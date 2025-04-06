/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.GuildBossBO;
/*    */ import core.database.game.bo.GuildBossChallengeBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class GuildBossEnter
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int bossId;
/*    */   }
/*    */   
/*    */   private static class EnterInfo
/*    */   {
/*    */     List<Player.FightInfo> players;
/*    */     GuildBossBO boss;
/*    */     GuildBossChallengeBO challenge;
/*    */     
/*    */     private EnterInfo(List<Player.FightInfo> fullInfo, GuildBossBO boss, GuildBossChallengeBO challenge) {
/* 34 */       this.players = fullInfo;
/* 35 */       this.boss = boss;
/* 36 */       this.challenge = challenge;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 43 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 44 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 45 */     Guild guild = guildMember.getGuild();
/* 46 */     if (guild == null) {
/* 47 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 50 */     GuildBossChallengeBO challengBo = guildMember.beginFightGuildBoss(req.bossId);
/* 51 */     GuildBossBO boss = guild.getboss(req.bossId);
/* 52 */     List<Player> players = guild.getPlayerList(boss);
/* 53 */     List<Player.FightInfo> fullInfoList = new ArrayList<>();
/* 54 */     for (Player tmpPlay : players) {
/* 55 */       if (tmpPlay != null && tmpPlay != player) {
/* 56 */         fullInfoList.add(((PlayerBase)tmpPlay.getFeature(PlayerBase.class)).fightInfo());
/*    */       }
/*    */     } 
/* 59 */     request.response(new EnterInfo(fullInfoList, boss, challengBo, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildBossEnter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */