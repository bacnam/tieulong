/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.global.recharge.RechargeMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.RechargeFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.PlayerRechargeRecordBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ 
/*    */ public class RechargeInfo
/*    */   extends PlayerHandler
/*    */ {
/* 18 */   private static RechargeMgr rechargeManager = null;
/*    */   
/*    */   private static class Recharged {
/*    */     private String goodsID;
/*    */     private int lastBuyTime;
/*    */     private int buyCount;
/*    */     
/*    */     private Recharged() {}
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 29 */     if (rechargeManager == null) {
/* 30 */       rechargeManager = RechargeMgr.getInstance();
/*    */     }
/* 32 */     Collection<PlayerRechargeRecordBO> recharge = ((RechargeFeature)player.getFeature(RechargeFeature.class)).getRecharged();
/* 33 */     List<Recharged> rtn = new ArrayList<>();
/* 34 */     for (PlayerRechargeRecordBO recordBO : recharge) {
/*    */       
/* 36 */       if (rechargeManager.getResetSign(recordBO.getGoodsID()).equals(recordBO.getResetSign())) {
/* 37 */         Recharged recharged = new Recharged(null);
/* 38 */         recharged.goodsID = recordBO.getGoodsID();
/* 39 */         recharged.buyCount = recordBO.getBuyCount();
/* 40 */         recharged.lastBuyTime = recordBO.getLastBuyTime();
/* 41 */         rtn.add(recharged);
/*    */       } 
/*    */     } 
/* 44 */     request.response(rtn);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/RechargeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */