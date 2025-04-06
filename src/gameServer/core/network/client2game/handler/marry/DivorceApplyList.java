/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.MarryDivorceApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.MarryApplyInfo;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DivorceApplyList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     List<MarryApplyInfo> infolist = new ArrayList<>();
/* 23 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 24 */     feature.receiveDivorceApplys.stream().forEach(x -> {
/*    */           MarryApplyInfo info = new MarryApplyInfo();
/*    */           info.setSummary(((PlayerBase)PlayerMgr.getInstance().getPlayer(x.getFromPid()).getFeature(PlayerBase.class)).summary());
/*    */           info.setLeftTime(paramMarryFeature.getDivorceLeftTime(x));
/*    */           paramList.add(info);
/*    */         });
/* 30 */     request.response(infolist);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/DivorceApplyList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */