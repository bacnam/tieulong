/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.Character;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.EquipPos;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefStarInfo;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class StarUp
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int charId;
/*    */     EquipPos pos;
/*    */   }
/*    */   
/*    */   public static class StarLevelNotify {
/*    */     int charId;
/*    */     EquipPos pos;
/*    */     long StarLevel;
/*    */     
/*    */     public StarLevelNotify(int charId, EquipPos pos, long StarLevel) {
/* 36 */       this.charId = charId;
/* 37 */       this.pos = pos;
/* 38 */       this.StarLevel = StarLevel;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 44 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 45 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/* 46 */     if (character == null) {
/* 47 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*    */     }
/*    */     
/* 50 */     int StarLevel = character.getBo().getStar(req.pos.ordinal());
/* 51 */     if (StarLevel >= RefDataMgr.size(RefStarInfo.class)) {
/* 52 */       throw new WSException(ErrorCode.Star_LevelFull, "位置[%s]升星已满级", new Object[] { req.pos });
/*    */     }
/*    */     
/* 55 */     RefStarInfo ref = (RefStarInfo)RefDataMgr.get(RefStarInfo.class, Integer.valueOf(StarLevel + 1));
/*    */ 
/*    */     
/* 58 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 59 */     if (!playerCurrency.check(PrizeType.Gold, ref.Gold)) {
/* 60 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(ref.Gold) });
/*    */     }
/*    */ 
/*    */     
/* 64 */     if (!playerCurrency.check(PrizeType.StarMaterial, ref.Material)) {
/* 65 */       throw new WSException(ErrorCode.NotEnough_StarMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getStarMaterial()), Integer.valueOf(ref.Material) });
/*    */     }
/*    */ 
/*    */     
/* 69 */     playerCurrency.consume(PrizeType.Gold, ref.Gold, ItemFlow.StarLevelUp);
/*    */ 
/*    */     
/* 72 */     playerCurrency.consume(PrizeType.StarMaterial, ref.Material, ItemFlow.StarLevelUp);
/*    */ 
/*    */     
/* 75 */     character.getBo().saveStar(req.pos.ordinal(), character.getBo().getStar(req.pos.ordinal()) + 1);
/*    */ 
/*    */     
/* 78 */     character.onAttrChanged();
/*    */ 
/*    */     
/* 81 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarUp);
/*    */ 
/*    */     
/* 84 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.StarMax, Integer.valueOf(character.getBo().getStar(req.pos.ordinal())));
/* 85 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M1, Integer.valueOf(character.getBo().getStar(req.pos.ordinal())));
/* 86 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M2, Integer.valueOf(character.getBo().getStar(req.pos.ordinal())));
/*    */     
/* 88 */     StarLevelNotify notify = new StarLevelNotify(character.getCharId(), req.pos, character.getBo().getStar(req.pos.ordinal()));
/* 89 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/StarUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */