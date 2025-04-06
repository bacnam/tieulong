/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildCreate
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     String name;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 29 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     Guild oldGuild = guildMember.getGuild();
/* 41 */     if (oldGuild != null) {
/* 42 */       throw new WSException(ErrorCode.Guild_AlreadyInGuild, "[%s]玩家已经在帮会[%s]，不能再创建帮会", new Object[] { Long.valueOf(player.getPid()), oldGuild.getName() });
/*    */     }
/*    */     
/* 45 */     GuildMgr.CheckNameValid(req.name, player.getPid());
/* 46 */     int createcost = RefDataMgr.getFactor("CreateGuildCost", 500);
/* 47 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 48 */     if (!currency.check(PrizeType.Crystal, createcost)) {
/* 49 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家[%s]不能创建帮会,其钻石[%s]不足[%s]", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(createcost) });
/*    */     }
/* 51 */     currency.consume(PrizeType.Crystal, createcost, ItemFlow.Guild_Create);
/*    */     
/* 53 */     GuildMgr.getInstance().createGuild(req.name, 0, player);
/* 54 */     GuildMgr.getInstance().removeApply(player.getPid());
/*    */     
/* 56 */     ((CharFeature)player.getFeature(CharFeature.class)).updateCharPower();
/*    */     
/* 58 */     player.pushProto("guildSkill", ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuildSkillList());
/*    */     
/* 60 */     request.response(new GuildInfoHandler.GuildBaseInfo(guildMember.getGuild().guildInfo(), guildMember.memberInfo(), guildMember.getJoinCD()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildCreate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */