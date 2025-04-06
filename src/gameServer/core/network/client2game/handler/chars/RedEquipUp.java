/*     */ package core.network.client2game.handler.chars;
/*     */ 
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.character.Equip;
/*     */ import business.player.feature.character.EquipFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.Quality;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefEquip;
/*     */ import core.config.refdata.ref.RefSmelt;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RedEquipUp
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int charId;
/*     */     EquipPos pos;
/*     */   }
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  37 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  38 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/*  39 */     if (character == null) {
/*  40 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*     */     }
/*  42 */     Equip equip = character.getEquip(req.pos);
/*  43 */     int fromLevel = 0;
/*  44 */     if (equip == null || equip.getRef().getQuality() != Quality.Red) {
/*  45 */       fromLevel = 0;
/*     */     } else {
/*  47 */       fromLevel = equip.getLevel();
/*     */     } 
/*     */     
/*  50 */     int toLevel = player.getLv();
/*  51 */     if (toLevel < 10) {
/*  52 */       toLevel = 1;
/*     */     } else {
/*  54 */       toLevel = toLevel / 10 * 10;
/*     */     } 
/*     */     
/*  57 */     if (fromLevel >= toLevel) {
/*  58 */       throw new WSException(ErrorCode.Equip_LevelRequired, "升级后超过角色等级，无法升级");
/*     */     }
/*     */     
/*  61 */     toLevel = (fromLevel >= 100) ? (fromLevel + 10) : ((toLevel > 100) ? 100 : toLevel);
/*     */     
/*  63 */     String equipPos = req.pos.toString();
/*  64 */     String left = "Left";
/*  65 */     String right = "Right";
/*  66 */     if (equipPos.endsWith(left) || equipPos.endsWith(right)) {
/*  67 */       equipPos = equipPos.replace(left, "");
/*  68 */       equipPos = equipPos.replace(right, "");
/*     */     } 
/*     */     
/*  71 */     String key = String.valueOf(req.charId) + equipPos + toLevel;
/*  72 */     RefEquip ref = (RefEquip)RefEquip.redEquip.get(key);
/*  73 */     if (ref == null) {
/*  74 */       throw new WSException(ErrorCode.Equip_NotFound, "装备不存在");
/*     */     }
/*     */     
/*  77 */     int material = 0;
/*  78 */     RefSmelt toRefSmelt = (RefSmelt)RefDataMgr.get(RefSmelt.class, String.valueOf(Quality.Red.toString()) + toLevel);
/*  79 */     if (fromLevel == 0) {
/*  80 */       material = toRefSmelt.RedPiece;
/*     */     } else {
/*  82 */       RefSmelt fromRefSmelt = (RefSmelt)RefDataMgr.get(RefSmelt.class, String.valueOf(Quality.Red.toString()) + fromLevel);
/*  83 */       material = toRefSmelt.RedPiece - fromRefSmelt.RedPiece;
/*     */     } 
/*     */ 
/*     */     
/*  87 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/*  88 */     if (!playerCurrency.check(PrizeType.RedPiece, material)) {
/*  89 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家红碎片:%s<升级所需碎片:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getRedPiece()), Integer.valueOf(material) });
/*     */     }
/*  91 */     EquipFeature equipFeature = (EquipFeature)player.getFeature(EquipFeature.class);
/*     */     
/*  93 */     Equip newEquip = equipFeature.gainOneEquip(ref.id, ItemFlow.MakeRedEquip);
/*  94 */     if (newEquip == null) {
/*  95 */       throw new WSException(ErrorCode.Package_Full, "玩家背包已满");
/*     */     }
/*     */     
/*  98 */     playerCurrency.consume(PrizeType.RedPiece, material, ItemFlow.MakeRedEquip);
/*     */ 
/*     */     
/* 101 */     Equip preEquip = equipFeature.equipOn(newEquip.getSid(), req.pos, req.charId, character);
/*     */     
/* 103 */     if (preEquip != null && preEquip.getRef().getQuality() == Quality.Red) {
/* 104 */       equipFeature.consume(preEquip);
/*     */     }
/* 106 */     character.onAttrChanged();
/* 107 */     EquipOn.EquipNotify notify = new EquipOn.EquipNotify(character.getCharId(), req.pos, newEquip.getSid());
/*     */ 
/*     */     
/* 110 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.MakeRedEquip, new String[] { player.getName(), ref.Name });
/*     */     
/* 112 */     request.response(notify);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/RedEquipUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */