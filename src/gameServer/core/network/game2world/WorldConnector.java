/*     */ package core.network.game2world;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.global.chat.ChatMgr;
/*     */ import com.zhonglian.server.websocket.BaseConnector;
/*     */ import com.zhonglian.server.websocket.BaseIoHandler;
/*     */ import com.zhonglian.server.websocket.BaseSession;
/*     */ import com.zhonglian.server.websocket.IMessageDispatcher;
/*     */ import com.zhonglian.server.websocket.codecfactory.ServerDecoder;
/*     */ import com.zhonglian.server.websocket.codecfactory.WebEncoder;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.def.MessageType;
/*     */ import com.zhonglian.server.websocket.def.TerminalType;
/*     */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*     */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*     */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*     */ import core.server.ServerConfig;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoder;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoder;
/*     */ 
/*     */ public class WorldConnector extends BaseConnector<WorldSession> {
/*  22 */   private static WorldConnector instance = new WorldConnector();
/*     */   private boolean logined = false;
/*     */   
/*     */   public static WorldConnector getInstance() {
/*  26 */     return instance;
/*     */   }
/*     */   
/*     */   public static class WorldConnectorIoHandler
/*     */     extends BaseIoHandler<WorldSession> {
/*     */     public WorldSession createSession(IoSession session, long sessionID) {
/*  32 */       return new WorldSession(session, sessionID);
/*     */     }
/*     */   }
/*     */   
/*     */   private WorldConnector() {
/*  37 */     super(new WorldConnectorIoHandler(), (ProtocolEncoder)new WebEncoder(), (ProtocolDecoder)new ServerDecoder());
/*  38 */     WorldMessageDispatcher dispatcher = new WorldMessageDispatcher();
/*  39 */     dispatcher.init();
/*  40 */     this.handler.setMessageDispatcher((IMessageDispatcher)dispatcher);
/*     */   }
/*     */   
/*     */   public boolean isLogined() {
/*  44 */     return this.logined;
/*     */   }
/*     */   
/*     */   public void setLogined(boolean logined) {
/*  48 */     this.logined = logined;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void request(String event, Object protocol, ResponseHandler handler) {
/*  53 */     if (!instance.logined || instance._session == null) {
/*  54 */       instance.onSendFailed(event, protocol, handler);
/*     */     } else {
/*  56 */       ((WorldSession)instance._session).request(event, protocol, handler);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void request(int descServer, String event, Object protocol, ResponseHandler handler) {
/*  61 */     if (!instance.logined || instance._session == null) {
/*  62 */       instance.onSendFailed(event, protocol, handler);
/*     */     } else {
/*  64 */       ((WorldSession)instance._session).request(TerminalType.GameServer, descServer, event, protocol, handler);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void notifyMessage(String event, Object protocol) {
/*  69 */     if (instance._session != null)
/*  70 */       ((WorldSession)instance._session).notifyMessage(event, protocol); 
/*     */   }
/*     */   
/*     */   public void notifyMessage(int descServer, String event, Object protocol) {
/*  74 */     if (this._session != null) {
/*  75 */       ((WorldSession)this._session).notifyMessage(TerminalType.GameServer, descServer, event, protocol);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onSendFailed(String event, Object protocol, ResponseHandler handler) {
/*  83 */     SyncTaskManager.task(() -> {
/*     */           MessageHeader header = new MessageHeader();
/*     */           header.messageType = MessageType.Request;
/*     */           header.srcType = (byte)TerminalType.GameServer.ordinal();
/*     */           header.srcId = ServerConfig.ServerID();
/*     */           header.descType = (byte)TerminalType.WorldServer.ordinal();
/*     */           header.descId = ServerConfig.ServerID();
/*     */           header.event = paramString;
/*     */           header.descId = -1L;
/*     */           header.descType = (byte)TerminalType.WorldServer.ordinal();
/*     */           header.sequence = 0;
/*     */           WebSocketResponse response = new WebSocketResponse(null, header);
/*     */           paramResponseHandler.handleError(response, ErrorCode.Server_NotConnected.value(), "未连接WorldServer");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnect() {
/* 112 */     ChatMgr.getInstance().init();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/game2world/WorldConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */