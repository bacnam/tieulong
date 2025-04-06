/*     */ package core.network.client2game.handler.chars;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefStrengthenInfo;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class StrengthenAllUp
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */   }
/*     */   
/*     */   public static class SkillNotify {
/*     */     int charId;
/*     */     List<Integer> StrengthenLevel;
/*     */     
/*     */     public SkillNotify(int charId, List<Integer> StrengthenLevel) {
/*  35 */       this.charId = charId;
/*  36 */       this.StrengthenLevel = StrengthenLevel;
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
/*  48 */     if (character.getEquips().size() == 0) {
/*  49 */       throw new WSException(ErrorCode.Equip_NotEquip, "角色[%s]没穿装备", new Object[] { Integer.valueOf(req.charId) });
/*     */     }
/*     */     
/*  52 */     int Size = (EquipPos.values()).length - 1;
/*  53 */     int Num = 0;
/*  54 */     int totalMoney = 0;
/*  55 */     int totalMaterial = 0;
/*     */     
/*  57 */     for (int i = 0; i < Size * player.getLv(); i++) {
/*  58 */       int index = 1;
/*  59 */       if (character.getEquip(EquipPos.values()[index]) == null) {
/*  60 */         index++;
/*     */       }
/*  62 */       for (int j = index; j < Size; j++) {
/*     */         
/*  64 */         if (character.getEquip(EquipPos.values()[j + 1]) != null && character.getBo().getStrengthen(index) > character.getBo().getStrengthen(j + 1)) {
/*  65 */           index = j + 1;
/*     */         }
/*     */       } 
/*     */       
/*  69 */       int Level = character.getBo().getStrengthen(index);
/*     */ 
/*     */       
/*  72 */       if (Level >= player.getLv()) {
/*     */         break;
/*     */       }
/*     */       
/*  76 */       if (Level >= RefDataMgr.size(RefStrengthenInfo.class)) {
/*     */         break;
/*     */       }
/*     */       
/*  80 */       RefStrengthenInfo ref = (RefStrengthenInfo)RefDataMgr.get(RefStrengthenInfo.class, Integer.valueOf(Level + 1));
/*     */       
/*  82 */       int goldRequired = ref.Gold;
/*  83 */       int material = ref.Material;
/*     */       
/*  85 */       totalMoney += goldRequired;
/*  86 */       totalMaterial += material;
/*     */       
/*  88 */       if (player.getPlayerBO().getGold() < totalMoney || player.getPlayerBO().getStrengthenMaterial() < totalMaterial) {
/*  89 */         totalMoney -= goldRequired;
/*  90 */         totalMaterial -= material;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*  95 */       character.getBo().setStrengthen(index, Level + 1);
/*  96 */       Num++;
/*     */     } 
/*     */     
/*  99 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 100 */     if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
/* 101 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(totalMoney) });
/*     */     }
/*     */ 
/*     */     
/* 105 */     if (!playerCurrency.check(PrizeType.StrengthenMaterial, totalMaterial)) {
/* 106 */       throw new WSException(ErrorCode.NotEnough_StengthenMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getStrengthenMaterial()), Integer.valueOf(totalMaterial) });
/*     */     }
/*     */     
/* 109 */     playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.StrengthenLevelUp);
/*     */     
/* 111 */     playerCurrency.consume(PrizeType.StrengthenMaterial, totalMaterial, ItemFlow.StrengthenLevelUp);
/* 112 */     character.getBo().saveAll();
/*     */ 
/*     */     
/* 115 */     character.onAttrChanged();
/*     */ 
/*     */     
/* 118 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen, Integer.valueOf(Num));
/* 119 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M1, Integer.valueOf(Num));
/* 120 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M2, Integer.valueOf(Num));
/* 121 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M3, Integer.valueOf(Num));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StrengthTotal, Integer.valueOf(Num));
/*     */ 
/*     */     
/* 132 */     SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getStrengthenAll());
/* 133 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/StrengthenAllUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */