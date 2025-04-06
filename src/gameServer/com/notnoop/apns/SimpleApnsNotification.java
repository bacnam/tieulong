/*     */ package com.notnoop.apns;
/*     */ 
/*     */ import com.notnoop.apns.internal.Utilities;
/*     */ import java.util.Arrays;
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
/*     */ public class SimpleApnsNotification
/*     */   implements ApnsNotification
/*     */ {
/*     */   private static final byte COMMAND = 0;
/*     */   private final byte[] deviceToken;
/*     */   private final byte[] payload;
/*     */   private byte[] marshall;
/*     */   
/*     */   public SimpleApnsNotification(String dtoken, String payload) {
/*  87 */     this.marshall = null; this.deviceToken = Utilities.decodeHex(dtoken); this.payload = Utilities.toUTF8Bytes(payload); } public SimpleApnsNotification(byte[] dtoken, byte[] payload) { this.marshall = null;
/*     */     this.deviceToken = Utilities.copyOf(dtoken);
/*     */     this.payload = Utilities.copyOf(payload); }
/*     */    public byte[] getDeviceToken() {
/*     */     return Utilities.copyOf(this.deviceToken);
/*     */   } public byte[] getPayload() {
/*     */     return Utilities.copyOf(this.payload);
/*     */   }
/*     */   public byte[] marshall() {
/*  96 */     if (this.marshall == null)
/*  97 */       this.marshall = Utilities.marshall((byte)0, this.deviceToken, this.payload); 
/*  98 */     return this.marshall;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 109 */     int length = 3 + this.deviceToken.length + 2 + this.payload.length;
/* 110 */     assert (marshall()).length == length;
/* 111 */     return length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 116 */     return 21 + 31 * Arrays.hashCode(this.deviceToken) + 31 * Arrays.hashCode(this.payload);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 123 */     if (!(obj instanceof SimpleApnsNotification))
/* 124 */       return false; 
/* 125 */     SimpleApnsNotification o = (SimpleApnsNotification)obj;
/* 126 */     return (Arrays.equals(this.deviceToken, o.deviceToken) && Arrays.equals(this.payload, o.payload));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIdentifier() {
/* 131 */     return -1;
/*     */   }
/*     */   
/*     */   public int getExpiry() {
/* 135 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     String payloadString = "???";
/*     */     try {
/* 142 */       payloadString = new String(this.payload, "UTF-8");
/* 143 */     } catch (Exception _) {}
/* 144 */     return "Message(Token=" + Utilities.encodeHex(this.deviceToken) + "; Payload=" + payloadString + ")";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/SimpleApnsNotification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */