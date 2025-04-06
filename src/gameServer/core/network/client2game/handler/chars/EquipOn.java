/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.Character;
/*    */ import business.player.feature.character.Equip;
/*    */ import business.player.feature.character.EquipFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.EquipPos;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class EquipOn
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request {
/*    */     long equipSid;
/*    */     int charId;
/*    */     EquipPos pos;
/*    */   }
/*    */   
/*    */   public static class EquipNotify {
/*    */     int charId;
/*    */     EquipPos pos;
/*    */     long equipSid;
/*    */     
/*    */     public EquipNotify(int charId, EquipPos pos, long equipSid) {
/* 31 */       this.charId = charId;
/* 32 */       this.pos = pos;
/* 33 */       this.equipSid = equipSid;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 39 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 40 */     EquipFeature feature = (EquipFeature)player.getFeature(EquipFeature.class);
/* 41 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/* 42 */     if (character == null) {
/* 43 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*    */     }
/*    */     
/* 46 */     if (req.equipSid != 0L) {
/* 47 */       feature.equipOn(req.equipSid, req.pos, req.charId, character);
/*    */     } else {
/* 49 */       Equip preEquip = character.getEquip(req.pos);
/* 50 */       if (preEquip != null) {
/* 51 */         character.unEquip(req.pos);
/* 52 */         preEquip.saveOwner(null, req.pos);
/*    */       } 
/*    */     } 
/* 55 */     character.onAttrChanged();
/* 56 */     EquipNotify notify = new EquipNotify(character.getCharId(), req.pos, req.equipSid);
/* 57 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/EquipOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */