/*    */ package core.network.zone2game.handler.base;
/*    */ 
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.zone2game.handler.ZBaseHandler;
/*    */ import proto.common.Mail;
/*    */ 
/*    */ 
/*    */ public class SendMail
/*    */   extends ZBaseHandler
/*    */ {
/*    */   public void handle(WebSocketRequest request, String message) throws WSException {
/* 17 */     Mail.SendMail req = (Mail.SendMail)(new Gson()).fromJson(message, Mail.SendMail.class);
/*    */     
/* 19 */     if (PlayerMgr.getInstance().getPlayer(req.scid) == null) {
/* 20 */       throw new WSException(ErrorCode.Player_NotFound, "%s玩家不存在，无法发送邮件", new Object[] { Long.valueOf(req.scid) });
/*    */     }
/* 22 */     MailCenter.getInstance().sendMail(req.scid, req.mailID, req.uniformIDList, req.uniformCountList, req.params);
/*    */     
/* 24 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/zone2game/handler/base/SendMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */