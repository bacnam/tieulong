/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.Character;
/*    */ import business.player.feature.character.DressFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.DressType;
/*    */ import com.zhonglian.server.common.enums.UnlockType;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefUnlockFunction;
/*    */ import core.database.game.bo.DressBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class DressOn
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long sid;
/*    */     int charId;
/*    */     DressType type;
/*    */   }
/*    */   
/*    */   public static class DressNotify {
/*    */     int charId;
/*    */     DressType type;
/*    */     long sid;
/*    */     
/*    */     public DressNotify(int charId, DressType type, long sid) {
/* 35 */       this.charId = charId;
/* 36 */       this.type = type;
/* 37 */       this.sid = sid;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 43 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 45 */     RefUnlockFunction refunlock = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, UnlockType.Dress);
/* 46 */     if (refunlock.UnlockLevel > player.getLv()) {
/* 47 */       throw new WSException(ErrorCode.NotEnough_UnlockCond, "解锁条件不足");
/*    */     }
/* 49 */     DressFeature feature = (DressFeature)player.getFeature(DressFeature.class);
/* 50 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/* 51 */     if (character == null) {
/* 52 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*    */     }
/*    */     
/* 55 */     if (req.sid != 0L) {
/* 56 */       feature.Active(req.sid, req.type, req.charId, character);
/*    */     } else {
/* 58 */       DressBO dressbo = character.getDress(req.type);
/* 59 */       if (dressbo != null) {
/* 60 */         character.unEquipDress(req.type);
/*    */       }
/*    */     } 
/* 63 */     character.onAttrChanged();
/* 64 */     DressNotify notify = new DressNotify(character.getCharId(), req.type, req.sid);
/* 65 */     ((CharFeature)player.getFeature(CharFeature.class)).updateCharPower();
/* 66 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/DressOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */