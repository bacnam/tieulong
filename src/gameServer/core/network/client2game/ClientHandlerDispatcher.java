/*    */ package core.network.client2game;
/*    */ 
/*    */ import BaseCommon.CommClass;
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.BaseSession;
/*    */ import com.zhonglian.server.websocket.IMessageDispatcher;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.def.MessageType;
/*    */ import com.zhonglian.server.websocket.def.TerminalType;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.handler.requset.RequestHandler;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import core.network.client2game.handler.WorldForwardHandler;
/*    */ import core.network.client2game.handler.ZoneForwardHandler;
/*    */ import core.server.ServerConfig;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ public class ClientHandlerDispatcher
/*    */   implements IMessageDispatcher<ClientSession>
/*    */ {
/* 27 */   protected final Map<String, RequestHandler> _handlers = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   public void init() {
/* 31 */     addHandlerByPath(BaseHandler.class.getPackage().getName());
/* 32 */     ZoneForwardHandler zfh = new ZoneForwardHandler();
/* 33 */     ProtoForward.ZoneForwardList.forEach(hander -> { 
/* 34 */         }); WorldForwardHandler wfh = new WorldForwardHandler();
/* 35 */     ProtoForward.WorldForwardList.forEach(hander -> {
/*    */         
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void addHandlerByPath(String path) {
/* 44 */     List<Class<?>> dealers = CommClass.getAllClassByInterface(RequestHandler.class, path);
/* 45 */     for (Class<?> cs : dealers) {
/* 46 */       RequestHandler dealer = null;
/*    */       try {
/* 48 */         dealer = CommClass.forName(cs.getName()).newInstance();
/* 49 */       } catch (Exception e) {
/* 50 */         CommLog.error(e.getMessage(), e);
/*    */       } 
/*    */       
/* 53 */       if (dealer == null) {
/*    */         continue;
/*    */       }
/* 56 */       this._handlers.put(dealer.getEvent().toLowerCase(), dealer);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int handleRawMessage(ClientSession session, ByteBuffer stream) {
/* 62 */     MessageHeader header = new MessageHeader();
/* 63 */     short eventlength = 0;
/*    */     try {
/* 65 */       header.messageType = MessageType.values()[stream.get()];
/* 66 */       header.srcType = (byte)TerminalType.Client.ordinal();
/* 67 */       header.srcId = (session.getPlayer() == null) ? 0L : session.getPlayer().getPid();
/* 68 */       header.descType = (byte)TerminalType.GameServer.ordinal();
/* 69 */       header.descId = ServerConfig.ServerID();
/* 70 */       header.sequence = stream.getShort();
/*    */       
/* 72 */       byte[] event = new byte[eventlength = stream.getShort()];
/* 73 */       stream.get(event);
/* 74 */       header.event = new String(event, "UTF-8");
/*    */       
/* 76 */       RequestHandler handler = this._handlers.get(header.event.toLowerCase());
/* 77 */       if (handler == null) {
/* 78 */         session.sendError(header, ErrorCode.Request_NotFoundHandler, "没有相关handler:" + header.event);
/* 79 */         CommLog.error("[WSBaseSocketListener] 没有相关handler:{}", header.event);
/* 80 */         return -1;
/*    */       } 
/*    */       
/* 83 */       byte[] msg = new byte[stream.getShort()];
/* 84 */       stream.get(msg);
/* 85 */       SyncTaskManager.task(() -> {
/*    */             try {
/*    */               paramRequestHandler.handleMessage(new WebSocketRequest(paramClientSession, paramMessageHeader), new String(paramArrayOfbyte, "UTF-8"));
/* 88 */             } catch (Exception e) {
/*    */               CommLog.error("[WSBaseSocketListener] 协议处理协议信息处理时错误:", e);
/*    */             } 
/*    */           });
/* 92 */       return 0;
/* 93 */     } catch (Exception e) {
/* 94 */       CommLog.error("错误的消息头:{},event长度:{}", (new Gson()).toJson(header), Short.valueOf(eventlength));
/*    */       
/* 96 */       return -2;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/ClientHandlerDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */