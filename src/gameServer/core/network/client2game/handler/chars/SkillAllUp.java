/*     */ package core.network.client2game.handler.chars;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCharacter;
/*     */ import core.config.refdata.ref.RefSkill;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SkillAllUp
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */   }
/*     */   
/*     */   public static class SkillNotify {
/*     */     int charId;
/*     */     List<Integer> skill;
/*     */     
/*     */     public SkillNotify(int charId, List<Integer> skill) {
/*  35 */       this.charId = charId;
/*  36 */       this.skill = skill;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  42 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  43 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/*  44 */     if (character == null) {
/*  45 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*     */     }
/*     */     
/*  48 */     int skillSize = character.getBo().getSkillSize();
/*  49 */     int skillNum = 0;
/*  50 */     int totalMoney = 0;
/*  51 */     for (int i = 0; i < skillSize * player.getLv(); i++) {
/*  52 */       int index = 0;
/*  53 */       for (int j = 0; j < skillSize - 1; j++) {
/*  54 */         RefCharacter refCharacter1 = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(req.charId));
/*  55 */         RefSkill refSkill1 = (RefSkill)RefDataMgr.get(RefSkill.class, refCharacter1.SkillList.get(j + 1));
/*  56 */         if (refSkill1 == null) {
/*  57 */           throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
/*     */         }
/*  59 */         if (refSkill1.Require <= player.getLv())
/*     */         {
/*  61 */           if (character.getBo().getSkill(index) > character.getBo().getSkill(j + 1))
/*  62 */             index = j + 1; 
/*     */         }
/*     */       } 
/*  65 */       int skillLevel = character.getBo().getSkill(index);
/*     */ 
/*     */       
/*  68 */       if (skillLevel + 1 >= player.getLv()) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/*  73 */       if (skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
/*     */         break;
/*     */       }
/*     */       
/*  77 */       RefCharacter refCharacter = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(req.charId));
/*  78 */       RefSkill refSkill = (RefSkill)RefDataMgr.get(RefSkill.class, refCharacter.SkillList.get(index));
/*  79 */       if (refSkill == null) {
/*  80 */         throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  85 */       int goldRequired = refSkill.GoldAdd * (skillLevel + 1 - 1) + refSkill.Gold;
/*     */       
/*  87 */       totalMoney += goldRequired;
/*     */       
/*  89 */       if (player.getPlayerBO().getGold() < totalMoney) {
/*  90 */         totalMoney -= goldRequired;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*  95 */       character.getBo().setSkill(index, skillLevel + 1);
/*  96 */       skillNum++;
/*     */     } 
/*     */     
/*  99 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 100 */     if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
/* 101 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(totalMoney) });
/*     */     }
/*     */     
/* 104 */     playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.SkillLevelUp);
/* 105 */     character.getBo().saveAll();
/*     */ 
/*     */     
/* 108 */     character.onAttrChanged();
/*     */ 
/*     */     
/* 111 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp, Integer.valueOf(skillNum));
/* 112 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M1, Integer.valueOf(skillNum));
/* 113 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M2, Integer.valueOf(skillNum));
/* 114 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M3, Integer.valueOf(skillNum));
/*     */ 
/*     */ 
/*     */     
/* 118 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillTotal, Integer.valueOf(skillNum));
/*     */ 
/*     */     
/* 121 */     SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getSkillAll());
/* 122 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/SkillAllUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */