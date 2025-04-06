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
/*     */ import core.config.refdata.ref.RefGem;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ public class GemAllUp
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */   }
/*     */   
/*     */   public static class SkillNotify {
/*     */     int charId;
/*     */     List<Integer> GemLevel;
/*     */     
/*     */     public SkillNotify(int charId, List<Integer> GemLevel) {
/*  35 */       this.charId = charId;
/*  36 */       this.GemLevel = GemLevel;
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
/*  50 */     int totalMaterial = 0;
/*  51 */     for (int i = 0; i < Size * RefDataMgr.size(RefGem.class); i++) {
/*  52 */       int index = 1;
/*  53 */       for (int j = index; j < Size; j++) {
/*  54 */         if (character.getBo().getGem(index) > character.getBo().getGem(j + 1)) {
/*  55 */           index = j + 1;
/*     */         }
/*     */       } 
/*     */       
/*  59 */       int Level = character.getBo().getGem(index);
/*     */ 
/*     */       
/*  62 */       if (Level >= RefDataMgr.size(RefGem.class)) {
/*     */         break;
/*     */       }
/*     */       
/*  66 */       RefGem ref = (RefGem)RefDataMgr.get(RefGem.class, Integer.valueOf(Level + 1));
/*     */       
/*  68 */       int material = ref.Material;
/*     */       
/*  70 */       totalMaterial += material;
/*     */       
/*  72 */       if (player.getPlayerBO().getGemMaterial() < totalMaterial) {
/*  73 */         totalMaterial -= material;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*  78 */       character.getBo().setGem(index, Level + 1);
/*  79 */       Num++;
/*     */     } 
/*     */     
/*  82 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/*     */ 
/*     */     
/*  85 */     if (!playerCurrency.check(PrizeType.GemMaterial, totalMaterial)) {
/*  86 */       throw new WSException(ErrorCode.NotEnough_GemMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGemMaterial()), Integer.valueOf(totalMaterial) });
/*     */     }
/*     */     
/*  89 */     playerCurrency.consume(PrizeType.GemMaterial, totalMaterial, ItemFlow.GemLevelUp);
/*  90 */     character.getBo().saveAll();
/*     */ 
/*     */     
/*  93 */     character.onAttrChanged();
/*     */ 
/*     */     
/*  96 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.GemMax, Integer.valueOf(character.getBo().getGem(1)));
/*  97 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M1, Integer.valueOf(Num));
/*  98 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M2, Integer.valueOf(Num));
/*     */ 
/*     */     
/* 101 */     SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getGemAll());
/* 102 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/GemAllUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */