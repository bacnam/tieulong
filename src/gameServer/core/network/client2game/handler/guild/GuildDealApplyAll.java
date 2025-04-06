/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildJobInfo;
/*    */ import core.config.refdata.ref.RefGuildLevel;
/*    */ import core.database.game.bo.GuildApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildDealApplyAll
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 25 */     Guild guild = guildMember.getGuild();
/* 26 */     if (guild == null) {
/* 27 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 29 */     RefGuildJobInfo job = guildMember.getJobRef();
/* 30 */     if (!job.DealRequest) {
/* 31 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有处理申请[DealRequest]的权限", new Object[] { Long.valueOf(player.getPid()), job.id });
/*    */     }
/* 33 */     for (GuildApplyBO bo : guild.getApplies()) {
/* 34 */       RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(guild.getLevel()));
/* 35 */       if (guild.getMemberCnt() >= refGuildLevel.MaxMemberAmount) {
/*    */         break;
/*    */       }
/* 38 */       Player newMember = PlayerMgr.getInstance().getPlayer(bo.getPid());
/* 39 */       guild.takeinMember(newMember);
/*    */     } 
/* 41 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildDealApplyAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */