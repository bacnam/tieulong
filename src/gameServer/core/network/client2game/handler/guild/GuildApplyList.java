/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.GuildApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildApplyList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     GuildMemberFeature guildFeature = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 25 */     Guild guild = guildFeature.getGuild();
/* 26 */     if (guild == null) {
/* 27 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 29 */     Collection<GuildApplyBO> applicants = guild.getApplies();
/* 30 */     PlayerMgr playerMgr = PlayerMgr.getInstance();
/* 31 */     Collection<Player.Summary> proto = (Collection<Player.Summary>)applicants.stream().map(x -> ((PlayerBase)paramPlayerMgr.getPlayer(x.getPid()).getFeature(PlayerBase.class)).summary())
/*    */       
/* 33 */       .collect(Collectors.toList());
/*    */     
/* 35 */     request.response(proto);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildApplyList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */