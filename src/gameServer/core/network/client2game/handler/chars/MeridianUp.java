/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.Character;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefMeridian;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class MeridianUp
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int charId;
/*    */   }
/*    */   
/*    */   public static class MeridianNotify
/*    */   {
/*    */     int charId;
/*    */     long MeridianLevel;
/*    */     
/*    */     public MeridianNotify(int charId, long MeridianLevel) {
/* 34 */       this.charId = charId;
/* 35 */       this.MeridianLevel = MeridianLevel;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 41 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 42 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/* 43 */     if (character == null) {
/* 44 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*    */     }
/*    */     
/* 47 */     int nowLevel = character.getBo().getMeridian();
/* 48 */     if (nowLevel >= RefDataMgr.size(RefMeridian.class) - 1) {
/* 49 */       throw new WSException(ErrorCode.Strengthen_LevelFull, "经脉已满级");
/*    */     }
/* 51 */     RefMeridian ref = (RefMeridian)RefDataMgr.get(RefMeridian.class, Integer.valueOf(nowLevel + 1));
/*    */ 
/*    */     
/* 54 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 55 */     if (!playerCurrency.check(PrizeType.MerMaterial, ref.Material)) {
/* 56 */       throw new WSException(ErrorCode.NotEnough_MerMaterial, "玩家材料:%s<升级材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getMerMaterial()), Integer.valueOf(ref.Material) });
/*    */     }
/*    */     
/* 59 */     playerCurrency.consume(PrizeType.MerMaterial, ref.Material, ItemFlow.MerLevelUp);
/*    */ 
/*    */     
/* 62 */     character.getBo().saveMeridian(nowLevel + 1);
/*    */     
/* 64 */     character.onAttrChanged();
/*    */ 
/*    */     
/* 67 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal);
/* 68 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal_M1);
/* 69 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal_M2);
/* 70 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal_M3);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 78 */     MeridianNotify notify = new MeridianNotify(req.charId, character.getBo().getMeridian());
/* 79 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/MeridianUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */