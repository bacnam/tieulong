/*     */ package core.network.client2game.handler.player;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.character.Equip;
/*     */ import business.player.feature.character.EquipFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.Quality;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefEquip;
/*     */ import core.config.refdata.ref.RefSmelt;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ public class SmeltAll
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     boolean isKeep;
/*     */   }
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  36 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  37 */     EquipFeature equipFeature = (EquipFeature)player.getFeature(EquipFeature.class);
/*     */     
/*  39 */     Map<Integer, Map<EquipPos, Equip>> map = new HashMap<>();
/*     */     
/*  41 */     equipFeature.getAllEquips().forEach(equip -> {
/*     */           RefEquip ref = equip.getRef();
/*     */           
/*     */           EquipPos weakest = null;
/*     */           
/*     */           int power = equip.getBasePower();
/*     */           
/*     */           for (EquipPos pos : ref.validEquipPos()) {
/*     */             if (paramMap.get(Integer.valueOf(ref.CharID)) == null) {
/*     */               Map<EquipPos, Equip> map1 = new HashMap<>();
/*     */               map1.put(pos, equip);
/*     */               paramMap.put(Integer.valueOf(ref.CharID), map1);
/*     */               return;
/*     */             } 
/*     */             Equip cur = (Equip)((Map)paramMap.get(Integer.valueOf(ref.CharID))).get(pos);
/*     */             if (cur == null) {
/*     */               weakest = pos;
/*     */               break;
/*     */             } 
/*     */             if (cur.getBasePower() < power) {
/*     */               weakest = pos;
/*     */               power = cur.getBasePower();
/*     */             } 
/*     */           } 
/*     */           if (weakest != null) {
/*     */             ((Map<EquipPos, Equip>)paramMap.get(Integer.valueOf(ref.CharID))).put(weakest, equip);
/*     */           }
/*     */         });
/*  69 */     int StrengthenMaterial = 0;
/*  70 */     int Gold = 0;
/*  71 */     int RedPiece = 0;
/*  72 */     for (Equip equip : equipFeature.getAllEquips()) {
/*  73 */       Character onwer = equip.getOwner();
/*     */       
/*  75 */       RefEquip ref = equip.getRef();
/*     */       
/*  77 */       if (onwer != null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       if (req.isKeep) {
/*     */         
/*  86 */         boolean flag = false;
/*  87 */         for (EquipPos pos : ref.validEquipPos()) {
/*  88 */           if (((Map)map.get(Integer.valueOf(ref.CharID))).get(pos) == equip) {
/*  89 */             flag = true;
/*     */             break;
/*     */           } 
/*     */         } 
/*  93 */         if (flag) {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */       
/*  98 */       RefSmelt refsmelt = (RefSmelt)RefDataMgr.get(RefSmelt.class, String.valueOf(ref.getQuality().toString()) + equip.getLevel());
/*     */       
/* 100 */       StrengthenMaterial += refsmelt.Strengthen;
/* 101 */       Gold += refsmelt.Gold;
/* 102 */       if (ref.getQuality() == Quality.Red) {
/* 103 */         RedPiece += refsmelt.RedPiece;
/*     */       }
/* 105 */       equipFeature.consume(equip);
/*     */     } 
/* 107 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 108 */     if (StrengthenMaterial != 0)
/* 109 */       playerCurrency.gain(PrizeType.StrengthenMaterial, StrengthenMaterial, ItemFlow.Smelt); 
/* 110 */     if (Gold != 0)
/* 111 */       playerCurrency.gain(PrizeType.Gold, Gold, ItemFlow.Smelt); 
/* 112 */     if (RedPiece != 0)
/* 113 */       playerCurrency.gain(PrizeType.RedPiece, RedPiece, ItemFlow.Smelt); 
/* 114 */     Reward reward = new Reward();
/* 115 */     reward.add(PrizeType.StrengthenMaterial, StrengthenMaterial);
/* 116 */     reward.add(PrizeType.Gold, Gold);
/* 117 */     reward.add(PrizeType.RedPiece, RedPiece);
/*     */ 
/*     */     
/* 120 */     if (reward.size() > 0) {
/* 121 */       ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt);
/* 122 */       ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt_M1);
/* 123 */       ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt_M2);
/* 124 */       ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt_M3);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     request.response(reward);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/SmeltAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */