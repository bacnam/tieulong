/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Guild;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GuildPetSkillUpgrade
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int skillid;
/*    */   }
/*    */   
/*    */   private static class Response
/*    */   {
/*    */     Guild.GuildSkill skill;
/*    */     int leftTimes;
/*    */     
/*    */     private Response(Guild.GuildSkill skill, int leftTimes) {
/* 28 */       this.skill = skill;
/* 29 */       this.leftTimes = leftTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 35 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 36 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 37 */     Guild guild = guildMember.getGuild();
/* 38 */     if (guild == null) {
/* 39 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 41 */     int skillId = req.skillid;
/* 42 */     if (skillId <= 0) {
/* 43 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数skillId=:%s", new Object[] { Integer.valueOf(skillId) });
/*    */     }
/* 45 */     guildMember.memberSkillUpgrade(skillId);
/* 46 */     Guild.GuildSkill builder = new Guild.GuildSkill();
/* 47 */     builder.setSkillid(req.skillid);
/* 48 */     builder.setLevel(guildMember.getMemberSkill(skillId).getLevel());
/* 49 */     request.response(new Response(builder, guildMember.getSkillPointsLeft(), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildPetSkillUpgrade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */