/*    */ package core.network.client2game.handler.longnv;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.LongnvSacrificeType;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class LongnvSacrificeInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     LongnvSacrificeType type;
/*    */   }
/*    */   
/*    */   private static class Sacrifice {
/*    */     int level;
/*    */     int exp;
/*    */     int donateTimes;
/*    */     int crystalTimes;
/*    */     
/*    */     private Sacrifice(int level, int exp, int donateTimes, int crystalTimes) {
/* 30 */       this.level = level;
/* 31 */       this.exp = exp;
/* 32 */       this.donateTimes = donateTimes;
/* 33 */       this.crystalTimes = crystalTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 40 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 41 */     Guild guild = guildMember.getGuild();
/*    */     
/* 43 */     if (guild == null) {
/* 44 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 46 */     int level = guild.bo.getLnlevel();
/* 47 */     int exp = guild.bo.getLnexp();
/* 48 */     int donateTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.LongnvDonate);
/* 49 */     int crystalTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.LongnvCrystal);
/*    */     
/* 51 */     request.response(new Sacrifice(level, exp, donateTimes, crystalTimes, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/longnv/LongnvSacrificeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */