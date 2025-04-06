/*    */ package core.network.client2game;
/*    */ 
/*    */ import com.zhonglian.server.websocket.BaseAcceptor;
/*    */ import com.zhonglian.server.websocket.BaseIoHandler;
/*    */ import com.zhonglian.server.websocket.BaseSession;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientAcceptor
/*    */   extends BaseAcceptor<ClientSession>
/*    */ {
/*    */   public static class ClientAcceptorIoHandler
/*    */     extends BaseIoHandler<ClientSession>
/*    */   {
/*    */     public ClientSession createSession(IoSession session, long sessionID) {
/* 19 */       return new ClientSession(session, sessionID);
/*    */     }
/*    */   }
/*    */   
/* 23 */   private static ClientAcceptor _instance = new ClientAcceptor();
/*    */   
/*    */   public static final ClientAcceptor getInstance() {
/* 26 */     return _instance;
/*    */   }
/*    */   
/*    */   public ClientAcceptor() {
/* 30 */     super(new ClientAcceptorIoHandler());
/* 31 */     ClientHandlerDispatcher dispatcher = new ClientHandlerDispatcher();
/* 32 */     dispatcher.init();
/* 33 */     this.handler.setMessageDispatcher(dispatcher);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/ClientAcceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */