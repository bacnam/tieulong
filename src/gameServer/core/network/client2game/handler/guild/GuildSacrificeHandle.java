/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.SacrificeType;
/*    */ import com.zhonglian.server.common.utils.CommMath;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildSacrifice;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GuildSacrificeHandle
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     SacrificeType type;
/*    */   }
/*    */   
/*    */   private static class Sacrifice {
/*    */     boolean iscritical;
/*    */     int donate;
/*    */     int exp;
/*    */     int totalexp;
/*    */     int leftTimes;
/*    */     
/*    */     private Sacrifice(boolean iscritical, int donate, int exp, int totalexp, int leftTimes) {
/* 35 */       this.iscritical = iscritical;
/* 36 */       this.donate = donate;
/* 37 */       this.exp = exp;
/* 38 */       this.totalexp = totalexp;
/* 39 */       this.leftTimes = leftTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 46 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 47 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 48 */     Guild guild = guildMember.getGuild();
/*    */     
/* 50 */     if (guild == null) {
/* 51 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 54 */     if (guildMember.getSacrificeLeftTimes() <= 0) {
/* 55 */       throw new WSException(ErrorCode.Guild_SacrificeAlready, "玩家[%s]祭天次数已满", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */ 
/*    */     
/* 59 */     RefGuildSacrifice ref = (RefGuildSacrifice)RefDataMgr.get(RefGuildSacrifice.class, req.type);
/* 60 */     PlayerItem playerItem = (PlayerItem)player.getFeature(PlayerItem.class);
/* 61 */     if (!playerItem.check(ref.CostItemID, ref.CostCount)) {
/* 62 */       throw new WSException(ErrorCode.NotEnough_GuildSacrificeCost, "玩家[%s]资源不足，不能进行相关类型的祭天", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 64 */     playerItem.consume(ref.CostItemID, ref.CostCount, ItemFlow.Guild_Sacrifice);
/*    */     
/* 66 */     boolean critical = (CommMath.randomInt(10000) < ref.Critical);
/* 67 */     int crivalue = critical ? ref.CriticalValue : 10000;
/*    */     
/* 69 */     int exp = ref.GuildExp * crivalue / 10000;
/* 70 */     int donate = ref.GuildDonate * crivalue / 10000;
/*    */     
/* 72 */     guild.gainExp(exp);
/* 73 */     guild.incSacrificeCount(player.getPid());
/*    */     
/* 75 */     guildMember.setSacrificed();
/* 76 */     guildMember.gainDonate(donate, ItemFlow.Guild_GuildSacrifice);
/* 77 */     guildMember.gainSacriDonate(donate);
/*    */     
/* 79 */     Sacrifice sacrifice = new Sacrifice(critical, donate, exp, guild.bo.getExp(), guildMember.getSacrificeLeftTimes(), null);
/*    */     
/* 81 */     guild.broadcast("sacrifice", sacrifice, player.getPid());
/* 82 */     request.response(sacrifice);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildSacrificeHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */