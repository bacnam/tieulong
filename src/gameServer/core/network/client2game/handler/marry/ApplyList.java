/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.MarryApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.MarryApplyInfo;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ApplyList
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     List<MarryApplyInfo> applys;
/*    */     MarryApplyInfo sendapply;
/*    */     
/*    */     private Response(List<MarryApplyInfo> applys, MarryApplyInfo sendapply) {
/* 25 */       this.applys = applys;
/* 26 */       this.sendapply = sendapply;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 35 */     List<MarryApplyInfo> infolist = new ArrayList<>();
/* 36 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 37 */     feature.getApplyList().stream().forEach(x -> {
/*    */           MarryApplyInfo info = new MarryApplyInfo();
/*    */           info.setSummary(((PlayerBase)PlayerMgr.getInstance().getPlayer(x.getFromPid()).getFeature(PlayerBase.class)).summary());
/*    */           info.setLeftTime(paramMarryFeature.getLeftTime(x));
/*    */           paramList.add(info);
/*    */         });
/* 43 */     MarryApplyInfo info = new MarryApplyInfo();
/* 44 */     if (feature.sendApplys != null) {
/* 45 */       int time = feature.getLeftTime(feature.sendApplys);
/* 46 */       if (time > 0) {
/* 47 */         info.setSummary(((PlayerBase)PlayerMgr.getInstance().getPlayer(feature.sendApplys.getPid()).getFeature(PlayerBase.class)).summary());
/* 48 */         info.setLeftTime(time);
/*    */       } 
/*    */     } 
/* 51 */     request.response(new Response(infolist, info, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/ApplyList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */