/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.character.WarSpiritFeature;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefRebirth;
/*    */ import core.config.refdata.ref.RefWarSpiritLv;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class WarSpiritLv
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class SpiritNotify
/*    */   {
/*    */     long warSpiritLv;
/*    */     long warSpiritExp;
/*    */     long gainExp;
/*    */     
/*    */     private SpiritNotify(long rebirthLevel, long rebirthExp, long gainExp) {
/* 27 */       this.warSpiritLv = rebirthLevel;
/* 28 */       this.warSpiritExp = rebirthExp;
/* 29 */       this.gainExp = gainExp;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 36 */     int nowLevel = player.getPlayerBO().getWarspiritLv();
/* 37 */     if (nowLevel >= RefDataMgr.size(RefRebirth.class) - 1) {
/* 38 */       throw new WSException(ErrorCode.Rebirth_LevelFull, "转生已满级");
/*    */     }
/*    */ 
/*    */     
/* 42 */     RefWarSpiritLv now_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(nowLevel));
/* 43 */     RefWarSpiritLv next_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(nowLevel + 1));
/* 44 */     int gainExp = 0;
/*    */ 
/*    */     
/* 47 */     if (now_ref.Level != next_ref.Level) {
/* 48 */       player.getPlayerBO().saveWarspiritLv(nowLevel + 1);
/*    */     } else {
/* 50 */       if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(next_ref.UniformId, next_ref.UniformCount, ItemFlow.WarSpiritLv)) {
/* 51 */         throw new WSException(ErrorCode.NotEnough_WarSpiritLv, "玩家材料不够");
/*    */       }
/*    */       
/* 54 */       gainExp = next_ref.GainExp * next_ref.getCrit();
/* 55 */       int nowExp = player.getPlayerBO().getWarspiritExp() + gainExp;
/* 56 */       rebirthLevelUp(player, now_ref, next_ref, nowExp);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 61 */     ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).updatePower();
/*    */ 
/*    */     
/* 64 */     SpiritNotify notify = new SpiritNotify(player.getPlayerBO().getWarspiritLv(), player.getPlayerBO().getWarspiritExp(), gainExp, null);
/* 65 */     request.response(notify);
/*    */   }
/*    */ 
/*    */   
/*    */   private void rebirthLevelUp(Player player, RefWarSpiritLv now_ref, RefWarSpiritLv next_ref, int nowExp) {
/* 70 */     int levelUp = 0;
/* 71 */     for (int i = now_ref.id; ((RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(i))).Star <= ((List)RefWarSpiritLv.sameLevel.get(Integer.valueOf(now_ref.Level))).size(); i++) {
/* 72 */       RefWarSpiritLv temp_now_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(i));
/* 73 */       RefWarSpiritLv temp_next_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(i + 1));
/*    */       
/* 75 */       if (temp_next_ref == null)
/*    */         break; 
/* 77 */       long needExp = temp_next_ref.Exp;
/*    */       
/* 79 */       if (needExp > nowExp) {
/*    */         break;
/*    */       }
/* 82 */       if (temp_now_ref.Level != temp_next_ref.Level) {
/*    */         break;
/*    */       }
/* 85 */       levelUp++;
/* 86 */       nowExp = (int)(nowExp - needExp);
/*    */     } 
/*    */     
/* 89 */     player.getPlayerBO().saveWarspiritExp(nowExp);
/* 90 */     player.getPlayerBO().saveWarspiritLv(player.getPlayerBO().getWarspiritLv() + levelUp);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/WarSpiritLv.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */