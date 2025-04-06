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
/*    */ import core.config.refdata.ref.RefCharacter;
/*    */ import core.config.refdata.ref.RefSkill;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class SkillUp
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int charId;
/*    */     int index;
/*    */   }
/*    */   
/*    */   public static class SkillNotify {
/*    */     int charId;
/*    */     int index;
/*    */     long skillLevel;
/*    */     
/*    */     public SkillNotify(int charId, long skillLevel, int index) {
/* 36 */       this.charId = charId;
/* 37 */       this.skillLevel = skillLevel;
/* 38 */       this.index = index;
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
/* 49 */     int skillLevel = character.getBo().getSkill(req.index);
/*    */     
/* 51 */     if (skillLevel + 1 >= player.getLv()) {
/* 52 */       throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]不能超过人物等级", new Object[] { Integer.valueOf(skillLevel) });
/*    */     }
/*    */ 
/*    */     
/* 56 */     if (skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
/* 57 */       throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]已满", new Object[] { Integer.valueOf(skillLevel) });
/*    */     }
/*    */     
/* 60 */     RefCharacter refCharacter = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(req.charId));
/* 61 */     RefSkill refSkill = (RefSkill)RefDataMgr.get(RefSkill.class, refCharacter.SkillList.get(req.index));
/* 62 */     if (refSkill == null) {
/* 63 */       throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
/*    */     }
/*    */     
/* 66 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 67 */     int goldRequired = refSkill.GoldAdd * (skillLevel + 1 - 1) + refSkill.Gold;
/* 68 */     if (!playerCurrency.check(PrizeType.Gold, goldRequired)) {
/* 69 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(goldRequired) });
/*    */     }
/*    */ 
/*    */     
/* 73 */     playerCurrency.consume(PrizeType.Gold, goldRequired, ItemFlow.SkillLevelUp);
/*    */ 
/*    */     
/* 76 */     character.getBo().saveSkill(req.index, skillLevel + 1);
/*    */ 
/*    */     
/* 79 */     character.onAttrChanged();
/*    */ 
/*    */     
/* 82 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp);
/* 83 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M1);
/* 84 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M2);
/* 85 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M3);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 90 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillTotal);
/*    */ 
/*    */     
/* 93 */     SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getSkill(req.index), req.index);
/* 94 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/SkillUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */