/*     */ package core.network.proto;
/*     */ 
/*     */ import core.database.game.bo.PlayerMailBO;
/*     */ 
/*     */ 
/*     */ public class MailInfo
/*     */ {
/*     */   long id;
/*     */   long pid;
/*     */   String senderName;
/*     */   String title;
/*     */   String content;
/*     */   int createTime;
/*     */   int pickUpTime;
/*     */   int pickUpExistTime;
/*     */   int existTime;
/*     */   int leftTime;
/*     */   String uniformIDList;
/*     */   String uniformCountList;
/*     */   
/*     */   public MailInfo() {}
/*     */   
/*     */   public MailInfo(PlayerMailBO bo) {
/*  24 */     this.id = bo.getId();
/*  25 */     this.pid = bo.getPid();
/*  26 */     this.senderName = bo.getSenderName();
/*  27 */     this.title = bo.getTitle();
/*  28 */     this.content = bo.getContent();
/*  29 */     this.createTime = bo.getCreateTime();
/*  30 */     this.pickUpTime = bo.getPickUpTime();
/*  31 */     this.pickUpExistTime = bo.getPickUpExistTime();
/*  32 */     this.existTime = bo.getExistTime();
/*  33 */     this.uniformIDList = bo.getUniformIDList();
/*  34 */     this.uniformCountList = bo.getUniformCountList();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLeftTime() {
/*  39 */     return this.leftTime;
/*     */   }
/*     */   
/*     */   public void setLeftTime(int leftTime) {
/*  43 */     this.leftTime = leftTime;
/*     */   }
/*     */   
/*     */   public long getId() {
/*  47 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(long id) {
/*  51 */     this.id = id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  55 */     return this.pid;
/*     */   }
/*     */   
/*     */   public void setPid(long pid) {
/*  59 */     this.pid = pid;
/*     */   }
/*     */   
/*     */   public String getSenderName() {
/*  63 */     return this.senderName;
/*     */   }
/*     */   
/*     */   public void setSenderName(String senderName) {
/*  67 */     this.senderName = senderName;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  71 */     return this.title;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/*  75 */     this.title = title;
/*     */   }
/*     */   
/*     */   public String getContent() {
/*  79 */     return this.content;
/*     */   }
/*     */   
/*     */   public void setContent(String content) {
/*  83 */     this.content = content;
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/*  87 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public void setCreateTime(int createTime) {
/*  91 */     this.createTime = createTime;
/*     */   }
/*     */   
/*     */   public int getPickUpTime() {
/*  95 */     return this.pickUpTime;
/*     */   }
/*     */   
/*     */   public void setPickUpTime(int pickUpTime) {
/*  99 */     this.pickUpTime = pickUpTime;
/*     */   }
/*     */   
/*     */   public int getPickUpExistTime() {
/* 103 */     return this.pickUpExistTime;
/*     */   }
/*     */   
/*     */   public void setPickUpExistTime(int pickUpExistTime) {
/* 107 */     this.pickUpExistTime = pickUpExistTime;
/*     */   }
/*     */   
/*     */   public int getExistTime() {
/* 111 */     return this.existTime;
/*     */   }
/*     */   
/*     */   public void setExistTime(int existTime) {
/* 115 */     this.existTime = existTime;
/*     */   }
/*     */   
/*     */   public String getUniformIDList() {
/* 119 */     return this.uniformIDList;
/*     */   }
/*     */   
/*     */   public void setUniformIDList(String uniformIDList) {
/* 123 */     this.uniformIDList = uniformIDList;
/*     */   }
/*     */   
/*     */   public String getUniformCountList() {
/* 127 */     return this.uniformCountList;
/*     */   }
/*     */   
/*     */   public void setUniformCountList(String uniformCountList) {
/* 131 */     this.uniformCountList = uniformCountList;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/MailInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */