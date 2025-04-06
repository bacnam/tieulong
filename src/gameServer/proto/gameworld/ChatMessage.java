/*     */ package proto.gameworld;
/*     */ 
/*     */ import com.zhonglian.server.common.enums.ChatType;
/*     */ 
/*     */ public class ChatMessage {
/*     */   long id;
/*     */   ChatType type;
/*     */   long senderPid;
/*     */   String senderName;
/*     */   int senderLv;
/*     */   int senderVipLv;
/*     */   int senderIcon;
/*     */   int senderServerId;
/*     */   String message;
/*     */   String content;
/*     */   int sendTime;
/*     */   long receivePid;
/*     */   String receiveName;
/*     */   boolean MonthCard;
/*     */   boolean YearCard;
/*     */   boolean is_married;
/*     */   
/*     */   public int getSenderServerId() {
/*  24 */     return this.senderServerId;
/*     */   }
/*     */   
/*     */   public void setSenderServerId(int senderServerId) {
/*  28 */     this.senderServerId = senderServerId;
/*     */   }
/*     */   
/*     */   public boolean isIs_married() {
/*  32 */     return this.is_married;
/*     */   }
/*     */   
/*     */   public void setIs_married(boolean is_married) {
/*  36 */     this.is_married = is_married;
/*     */   }
/*     */   
/*     */   public boolean isMonthCard() {
/*  40 */     return this.MonthCard;
/*     */   }
/*     */   
/*     */   public void setMonthCard(boolean monthCard) {
/*  44 */     this.MonthCard = monthCard;
/*     */   }
/*     */   
/*     */   public boolean isYearCard() {
/*  48 */     return this.YearCard;
/*     */   }
/*     */   
/*     */   public void setYearCard(boolean yearCard) {
/*  52 */     this.YearCard = yearCard;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getId() {
/*  60 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(long id) {
/*  64 */     this.id = id;
/*     */   }
/*     */   
/*     */   public ChatType getType() {
/*  68 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(ChatType type) {
/*  72 */     this.type = type;
/*     */   }
/*     */   
/*     */   public long getSenderPid() {
/*  76 */     return this.senderPid;
/*     */   }
/*     */   
/*     */   public void setSenderPid(long senderPid) {
/*  80 */     this.senderPid = senderPid;
/*     */   }
/*     */   
/*     */   public String getSenderName() {
/*  84 */     return this.senderName;
/*     */   }
/*     */   
/*     */   public void setSenderName(String senderName) {
/*  88 */     this.senderName = senderName;
/*     */   }
/*     */   
/*     */   public int getSenderLv() {
/*  92 */     return this.senderLv;
/*     */   }
/*     */   
/*     */   public void setSenderLv(int senderLv) {
/*  96 */     this.senderLv = senderLv;
/*     */   }
/*     */   
/*     */   public int getSenderVipLv() {
/* 100 */     return this.senderVipLv;
/*     */   }
/*     */   
/*     */   public void setSenderVipLv(int senderVipLv) {
/* 104 */     this.senderVipLv = senderVipLv;
/*     */   }
/*     */   
/*     */   public int getSenderIcon() {
/* 108 */     return this.senderIcon;
/*     */   }
/*     */   
/*     */   public void setSenderIcon(int senderIcon) {
/* 112 */     this.senderIcon = senderIcon;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 116 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/* 120 */     this.message = message;
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 124 */     return this.content;
/*     */   }
/*     */   
/*     */   public void setContent(String content) {
/* 128 */     this.content = content;
/*     */   }
/*     */   
/*     */   public int getSendTime() {
/* 132 */     return this.sendTime;
/*     */   }
/*     */   
/*     */   public void setSendTime(int sendTime) {
/* 136 */     this.sendTime = sendTime;
/*     */   }
/*     */   
/*     */   public long getReceivePid() {
/* 140 */     return this.receivePid;
/*     */   }
/*     */   
/*     */   public void setReceivePid(long receivePid) {
/* 144 */     this.receivePid = receivePid;
/*     */   }
/*     */   
/*     */   public String getReceiveName() {
/* 148 */     return this.receiveName;
/*     */   }
/*     */   
/*     */   public void setReceiveName(String receiveName) {
/* 152 */     this.receiveName = receiveName;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/gameworld/ChatMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */