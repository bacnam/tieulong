/*    */ package core.network.client2game.handler.guildwar;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildWarMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildJobInfo;
/*    */ import core.config.refdata.ref.RefGuildWarCenter;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildWarApply
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int centerId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 27 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 28 */     Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 29 */     RefGuildJobInfo job = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getJobRef();
/* 30 */     if (!job.GuildwarApply) {
/* 31 */       throw new WSException(ErrorCode.GuildWar_NotPermit, "权限不足");
/*    */     }
/* 33 */     guild.applyGuildWar(req.centerId);
/* 34 */     RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(req.centerId));
/* 35 */     request.response(GuildWarMgr.getInstance().centerInfo(ref));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/GuildWarApply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */