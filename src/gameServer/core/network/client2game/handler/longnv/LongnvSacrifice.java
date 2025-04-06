/*     */ package core.network.client2game.handler.longnv;
/*     */ 
/*     */ import business.global.guild.Guild;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.LongnvSacrificeType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCrystalPrice;
/*     */ import core.config.refdata.ref.RefLongnvLevel;
/*     */ import core.config.refdata.ref.RefLongnvSacrifice;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class LongnvSacrifice
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     LongnvSacrificeType type;
/*     */   }
/*     */   
/*     */   private static class Sacrifice {
/*     */     boolean iscritical;
/*     */     int nowExp;
/*     */     int exp;
/*     */     int times;
/*     */     LongnvSacrificeType type;
/*     */     
/*     */     private Sacrifice(boolean iscritical, int nowExp, int exp, int times, LongnvSacrificeType type) {
/*  38 */       this.iscritical = iscritical;
/*  39 */       this.nowExp = nowExp;
/*  40 */       this.exp = exp;
/*  41 */       this.times = times;
/*  42 */       this.type = type;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  49 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  50 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/*  51 */     Guild guild = guildMember.getGuild();
/*     */     
/*  53 */     if (guild == null) {
/*  54 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*     */     }
/*     */ 
/*     */     
/*  58 */     RefLongnvLevel nextLeveLinfo = (RefLongnvLevel)RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(guild.getLongnvLevel() + 1));
/*  59 */     if (nextLeveLinfo == null) {
/*  60 */       throw new WSException(ErrorCode.Guild_LevelMax, "龙女等级已满");
/*     */     }
/*     */ 
/*     */     
/*  64 */     RefLongnvSacrifice ref = (RefLongnvSacrifice)RefDataMgr.get(RefLongnvSacrifice.class, req.type);
/*     */     
/*  66 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*  67 */     int times = recorder.getValue(ref.RefreshType);
/*     */     
/*  69 */     if (ref.Limit >= 0 && times >= ref.Limit) {
/*  70 */       throw new WSException(ErrorCode.Guild_SacrificeAlready, "玩家[%s]祭天次数已满", new Object[] { Long.valueOf(player.getPid()) });
/*     */     }
/*  72 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
/*  73 */     int cost = 0;
/*  74 */     int basicexp = 0;
/*  75 */     if (ref.id == LongnvSacrificeType.Donate) {
/*  76 */       cost = prize.LongnvDonate;
/*  77 */       basicexp = ref.Exp;
/*  78 */     } else if (ref.id == LongnvSacrificeType.Crystal) {
/*  79 */       cost = prize.LongnvCrystal;
/*  80 */       basicexp = prize.LongnvCrystalExp;
/*     */     } 
/*     */     
/*  83 */     PlayerItem playerItem = (PlayerItem)player.getFeature(PlayerItem.class);
/*  84 */     if (!playerItem.check(ref.CostItemID, cost)) {
/*  85 */       throw new WSException(ErrorCode.NotEnough_GuildSacrificeCost, "玩家[%s]资源不足，不能进行相关类型的祭天", new Object[] { Long.valueOf(player.getPid()) });
/*     */     }
/*  87 */     playerItem.consume(ref.CostItemID, cost, ItemFlow.Guild_Sacrifice);
/*     */     
/*  89 */     boolean critical = (CommMath.randomInt(10000) < ref.Critical);
/*  90 */     int crivalue = critical ? ref.CriticalValue : 10000;
/*     */     
/*  92 */     int exp = basicexp * crivalue / 10000;
/*     */     
/*  94 */     guild.gainLongnvExp(exp);
/*     */     
/*  96 */     recorder.addValue(ref.RefreshType);
/*     */     
/*  98 */     Sacrifice sacrifice = new Sacrifice(critical, guild.bo.getLnexp(), exp, recorder.getValue(ref.RefreshType), req.type, null);
/*     */     
/* 100 */     guild.broadcast("Longnvsacrifice", Integer.valueOf(exp), player.getPid());
/* 101 */     request.response(sacrifice);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/longnv/LongnvSacrifice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */