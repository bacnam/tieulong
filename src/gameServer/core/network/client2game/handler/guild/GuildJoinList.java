/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildConfig;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.GuildApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Guild;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildJoinList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     List<Guild> guilds = GuildMgr.getInstance().getAllGuild();
/*    */     
/* 26 */     GuildMgr guildMgr = GuildMgr.getInstance();
/*    */ 
/*    */     
/* 29 */     List<GuildApplyBO> applys = GuildMgr.getInstance().getApplyByCid(player.getPid());
/* 30 */     List<GuildApplyBO> applysOverTime = new ArrayList<>();
/* 31 */     for (GuildApplyBO applyBO : applys) {
/* 32 */       if (CommTime.nowSecond() - applyBO.getApplyTime() > GuildConfig.getGuildJoinTimeLimit()) {
/* 33 */         applysOverTime.add(applyBO);
/*    */       }
/*    */     } 
/* 36 */     for (GuildApplyBO applyBO : applysOverTime) {
/* 37 */       guildMgr.removeApply(applyBO.getPid(), applyBO.getGuildId());
/*    */     }
/*    */     
/* 40 */     List<Guild.JoinInfo> proto = new ArrayList<>();
/*    */     
/* 42 */     for (GuildApplyBO apply : applys) {
/* 43 */       Guild guild = guildMgr.getGuild(apply.getGuildId());
/* 44 */       if (guild != null) {
/* 45 */         Guild.JoinInfo info = guild.joinInfo();
/* 46 */         info.setHasRequest(true);
/* 47 */         proto.add(info);
/*    */       } 
/*    */     } 
/*    */     
/* 51 */     int size = proto.size();
/* 52 */     Collections.shuffle(guilds);
/* 53 */     guilds.stream().filter(x -> {
/*    */           for (GuildApplyBO applyBO : paramList) {
/*    */             if (x.getGuildId() == applyBO.getGuildId())
/*    */               return false; 
/*    */           } 
/*    */           return true;
/* 59 */         }).limit((GuildConfig.getGuildJoinListSize() - size)).forEach(x -> {
/*    */           Guild.JoinInfo info = x.joinInfo();
/*    */           
/*    */           info.setHasRequest(false);
/*    */           paramList.add(info);
/*    */         });
/* 65 */     request.response(proto);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildJoinList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */