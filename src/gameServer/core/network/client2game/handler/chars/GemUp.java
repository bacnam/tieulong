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
/*    */ import core.config.refdata.ref.RefGem;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GemUp
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int charId;
/*    */     EquipPos pos;
/*    */   }
/*    */   
/*    */   public static class GemupNotify {
/*    */     int charId;
/*    */     EquipPos pos;
/*    */     long GemLevel;
/*    */     
/*    */     public GemupNotify(int charId, EquipPos pos, long GemLevel) {
/* 36 */       this.charId = charId;
/* 37 */       this.pos = pos;
/* 38 */       this.GemLevel = GemLevel;
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
/* 50 */     int GemLevel = character.getBo().getGem(req.pos.ordinal());
/* 51 */     if (GemLevel >= RefDataMgr.size(RefGem.class)) {
/* 52 */       throw new WSException(ErrorCode.Gem_LevelFull, "位置[%s]宝石已满级", new Object[] { req.pos });
/*    */     }
/*    */     
/* 55 */     RefGem ref = (RefGem)RefDataMgr.get(RefGem.class, Integer.valueOf(GemLevel + 1));
/* 56 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/*    */ 
/*    */     
/* 59 */     if (!playerCurrency.check(PrizeType.GemMaterial, ref.Material)) {
/* 60 */       throw new WSException(ErrorCode.NotEnough_GemMaterial, "玩家材料:%s<宝石升级需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGemMaterial()), Integer.valueOf(ref.Material) });
/*    */     }
/*    */ 
/*    */     
/* 64 */     playerCurrency.consume(PrizeType.GemMaterial, ref.Material, ItemFlow.GemLevelUp);
/*    */ 
/*    */     
/* 67 */     character.getBo().saveGem(req.pos.ordinal(), character.getBo().getGem(req.pos.ordinal()) + 1);
/*    */ 
/*    */     
/* 70 */     character.onAttrChanged();
/*    */ 
/*    */     
/* 73 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.GemMax, Integer.valueOf(character.getBo().getGem(req.pos.ordinal())));
/* 74 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M1);
/* 75 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M2);
/*    */     
/* 77 */     GemupNotify notify = new GemupNotify(character.getCharId(), req.pos, character.getBo().getGem(req.pos.ordinal()));
/* 78 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/GemUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */