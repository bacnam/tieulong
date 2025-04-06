/*     */ package core.network.client2game;
/*     */ 
/*     */ import business.player.Player;
/*     */ import com.zhonglian.server.websocket.def.MessageType;
/*     */ import com.zhonglian.server.websocket.def.TerminalType;
/*     */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*     */ import com.zhonglian.server.websocket.server.ServerSession;
/*     */ import core.server.ServerConfig;
/*     */ import org.apache.mina.core.session.IoSession;
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
/*     */ public class ClientSession
/*     */   extends ServerSession
/*     */ {
/*     */   private Player player;
/*     */   private String playerOpenId;
/*     */   private int playerSid;
/*     */   private String accessToken;
/*     */   private int encryptedkey;
/*     */   private boolean valid;
/*     */   private boolean isPrint = false;
/*     */   
/*     */   public ClientSession(IoSession session, long sessionID) {
/*  35 */     super(TerminalType.GameServer, ServerConfig.ServerID(), TerminalType.Client, session, sessionID);
/*  36 */     this.valid = false;
/*  37 */     this.encryptedkey = (int)sessionID;
/*     */   }
/*     */   
/*     */   public Player getPlayer() {
/*  41 */     return this.player;
/*     */   }
/*     */   
/*     */   public void bindPlayer(Player player) {
/*  45 */     this.player = player;
/*     */   }
/*     */   
/*     */   public void losePlayer() {
/*  49 */     if (this.player != null) {
/*  50 */       this.player.loseSession();
/*  51 */       this.player = null;
/*     */     } 
/*  53 */     notifyMessage("kickout", "账户已失效，请重新连接");
/*  54 */     close();
/*     */   }
/*     */   
/*     */   public boolean isPrint() {
/*  58 */     return this.isPrint;
/*     */   }
/*     */   
/*     */   public void setPrint(boolean isPrint) {
/*  62 */     this.isPrint = isPrint;
/*     */   }
/*     */   
/*     */   public void setAccessToken(String access_token) {
/*  66 */     this.accessToken = access_token;
/*     */   }
/*     */   
/*     */   public String getAccessToken() {
/*  70 */     return this.accessToken;
/*     */   }
/*     */   
/*     */   public int getEncryptedkey() {
/*  74 */     return this.encryptedkey;
/*     */   }
/*     */   
/*     */   public void setEncryptedkey(int encryptedkey) {
/*  78 */     this.encryptedkey = encryptedkey;
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/*  82 */     return this.valid;
/*     */   }
/*     */   
/*     */   public void setValid(boolean isValid) {
/*  86 */     this.valid = isValid;
/*     */   }
/*     */   
/*     */   public String getOpenId() {
/*  90 */     return this.playerOpenId;
/*     */   }
/*     */   
/*     */   public void setOpenId(String playerPid) {
/*  94 */     this.playerOpenId = playerPid;
/*     */   }
/*     */   
/*     */   public int getPlayerSid() {
/*  98 */     return this.playerSid;
/*     */   }
/*     */   
/*     */   public void setPlayerSid(int playerSid) {
/* 102 */     this.playerSid = playerSid;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCreated() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClosed() {
/* 112 */     if (this.player != null)
/* 113 */       this.player.loseSession(); 
/*     */   }
/*     */   
/*     */   protected void onSendPacket(MessageHeader header, String body) {
/* 117 */     if (this.player != null && header.messageType != MessageType.Notify)
/* 118 */       this.player.getPacketCache().cacheSent(header, body); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/ClientSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */