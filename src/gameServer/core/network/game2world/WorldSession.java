/*    */ package core.network.game2world;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.TerminalType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*    */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*    */ import com.zhonglian.server.websocket.server.ServerSession;
/*    */ import core.server.ServerConfig;
/*    */ import java.io.IOException;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import proto.common.Login;
/*    */ import proto.common.Server;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldSession
/*    */   extends ServerSession
/*    */ {
/*    */   public WorldSession(IoSession session, long sessionID) {
/* 25 */     super(TerminalType.GameServer, ServerConfig.ServerID(), TerminalType.WorldServer, session, sessionID);
/* 26 */     setRemoteServerID(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onCreated() {
/* 31 */     Login.G_Login loginProto = new Login.G_Login(ServerConfig.ServerID());
/* 32 */     request("base.login", loginProto, new ResponseHandler()
/*    */         {
/*    */           public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException
/*    */           {
/* 36 */             CommLog.info("login to [[[world server]]] success!");
/* 37 */             Login.Z_Login req = (Login.Z_Login)(new Gson()).fromJson(body, Login.Z_Login.class);
/* 38 */             for (Server.ServerInfo serverInfo : req.servers) {
/* 39 */               WorldServerMgr.getInstance().addServer(serverInfo);
/*    */             }
/* 41 */             WorldConnector.getInstance().setLogined(true);
/* 42 */             WorldConnector.getInstance().onConnect();
/*    */           }
/*    */ 
/*    */           
/*    */           public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
/* 47 */             CommLog.error("{} resive error:{}, message:{}", new Object[] { getEvent(), Short.valueOf(statusCode), message });
/* 48 */             WorldSession.this.onClosed();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClosed() {
/* 55 */     WorldConnector.getInstance().setLogined(false);
/* 56 */     SyncTaskManager.task(() -> WorldConnector.getInstance().reconnect(System.getProperty("GameServer.World_IP", "127.0.0.1"), Integer.getInteger("GameServer.Zone_Port", 8002).intValue()), 
/*    */         
/* 58 */         3000);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/game2world/WorldSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */