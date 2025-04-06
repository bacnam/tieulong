/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Guild;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildMemberList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     GuildMemberFeature guildFeature = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 23 */     Guild guild = guildFeature.getGuild();
/* 24 */     if (guild == null) {
/* 25 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 27 */     List<Long> members = guild.getMembers();
/* 28 */     PlayerMgr playerMgr = PlayerMgr.getInstance();
/* 29 */     List<Guild.member> memberProto = (List<Guild.member>)members.stream().map(x -> ((GuildMemberFeature)paramPlayerMgr.getPlayer(x.longValue()).getFeature(GuildMemberFeature.class)).memberInfo())
/*    */       
/* 31 */       .collect(Collectors.toList());
/*    */     
/* 33 */     request.response(memberProto);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildMemberList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */