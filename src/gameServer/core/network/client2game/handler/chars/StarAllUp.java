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
/*     */ import core.config.refdata.ref.RefStarInfo;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class StarAllUp
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */   }
/*     */   
/*     */   public static class SkillNotify {
/*     */     int charId;
/*     */     List<Integer> StarLevel;
/*     */     
/*     */     public SkillNotify(int charId, List<Integer> StarLevel) {
/*  35 */       this.charId = charId;
/*  36 */       this.StarLevel = StarLevel;
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
/*  48 */     int Size = (EquipPos.values()).length - 1;
/*  49 */     int Num = 0;
/*  50 */     int totalMoney = 0;
/*  51 */     int totalMaterial = 0;
/*  52 */     for (int i = 0; i < Size * RefDataMgr.size(RefStarInfo.class); i++) {
/*  53 */       int index = 1;
/*  54 */       for (int j = index; j < Size; j++) {
/*     */         
/*  56 */         if (character.getBo().getStar(index) > character.getBo().getStar(j + 1)) {
/*  57 */           index = j + 1;
/*     */         }
/*     */       } 
/*     */       
/*  61 */       int Level = character.getBo().getStar(index);
/*     */ 
/*     */       
/*  64 */       if (Level >= RefDataMgr.size(RefStarInfo.class)) {
/*     */         break;
/*     */       }
/*     */       
/*  68 */       RefStarInfo ref = (RefStarInfo)RefDataMgr.get(RefStarInfo.class, Integer.valueOf(Level + 1));
/*     */       
/*  70 */       int goldRequired = ref.Gold;
/*  71 */       int material = ref.Material;
/*     */       
/*  73 */       totalMoney += goldRequired;
/*  74 */       totalMaterial += material;
/*     */       
/*  76 */       if (player.getPlayerBO().getGold() < totalMoney || player.getPlayerBO().getStarMaterial() < totalMaterial) {
/*  77 */         totalMoney -= goldRequired;
/*  78 */         totalMaterial -= material;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*  83 */       character.getBo().setStar(index, Level + 1);
/*  84 */       Num++;
/*     */     } 
/*     */     
/*  87 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/*  88 */     if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
/*  89 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(totalMoney) });
/*     */     }
/*     */ 
/*     */     
/*  93 */     if (!playerCurrency.check(PrizeType.StarMaterial, totalMaterial)) {
/*  94 */       throw new WSException(ErrorCode.NotEnough_StarMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getStarMaterial()), Integer.valueOf(totalMaterial) });
/*     */     }
/*     */     
/*  97 */     playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.StarLevelUp);
/*     */     
/*  99 */     playerCurrency.consume(PrizeType.StarMaterial, totalMaterial, ItemFlow.StarLevelUp);
/* 100 */     character.getBo().saveAll();
/*     */ 
/*     */     
/* 103 */     character.onAttrChanged();
/*     */ 
/*     */     
/* 106 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarUp, Integer.valueOf(Num));
/*     */ 
/*     */     
/* 109 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.StarMax, Integer.valueOf(character.getBo().getStar(1)));
/* 110 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M1, Integer.valueOf(Num));
/* 111 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M2, Integer.valueOf(Num));
/*     */ 
/*     */     
/* 114 */     SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getStarAll());
/* 115 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/StarAllUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */