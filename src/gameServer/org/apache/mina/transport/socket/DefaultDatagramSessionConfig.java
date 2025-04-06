/*     */ package org.apache.mina.transport.socket;
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
/*     */ 
/*     */ 
/*     */ public class DefaultDatagramSessionConfig
/*     */   extends AbstractDatagramSessionConfig
/*     */ {
/*     */   private static boolean DEFAULT_BROADCAST = false;
/*     */   private static boolean DEFAULT_REUSE_ADDRESS = false;
/*  35 */   private static int DEFAULT_RECEIVE_BUFFER_SIZE = -1;
/*     */ 
/*     */   
/*  38 */   private static int DEFAULT_SEND_BUFFER_SIZE = -1;
/*     */   
/*  40 */   private static int DEFAULT_TRAFFIC_CLASS = 0;
/*     */   
/*  42 */   private boolean broadcast = DEFAULT_BROADCAST;
/*     */   
/*  44 */   private boolean reuseAddress = DEFAULT_REUSE_ADDRESS;
/*     */   
/*  46 */   private int receiveBufferSize = DEFAULT_RECEIVE_BUFFER_SIZE;
/*     */   
/*  48 */   private int sendBufferSize = DEFAULT_SEND_BUFFER_SIZE;
/*     */   
/*  50 */   private int trafficClass = DEFAULT_TRAFFIC_CLASS;
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
/*     */   public boolean isBroadcast() {
/*  63 */     return this.broadcast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBroadcast(boolean broadcast) {
/*  70 */     this.broadcast = broadcast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*  77 */     return this.reuseAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReuseAddress(boolean reuseAddress) {
/*  84 */     this.reuseAddress = reuseAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReceiveBufferSize() {
/*  91 */     return this.receiveBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReceiveBufferSize(int receiveBufferSize) {
/*  98 */     this.receiveBufferSize = receiveBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/* 105 */     return this.sendBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSendBufferSize(int sendBufferSize) {
/* 112 */     this.sendBufferSize = sendBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/* 119 */     return this.trafficClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrafficClass(int trafficClass) {
/* 126 */     this.trafficClass = trafficClass;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isBroadcastChanged() {
/* 131 */     return (this.broadcast != DEFAULT_BROADCAST);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isReceiveBufferSizeChanged() {
/* 136 */     return (this.receiveBufferSize != DEFAULT_RECEIVE_BUFFER_SIZE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isReuseAddressChanged() {
/* 141 */     return (this.reuseAddress != DEFAULT_REUSE_ADDRESS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSendBufferSizeChanged() {
/* 146 */     return (this.sendBufferSize != DEFAULT_SEND_BUFFER_SIZE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isTrafficClassChanged() {
/* 151 */     return (this.trafficClass != DEFAULT_TRAFFIC_CLASS);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/DefaultDatagramSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */