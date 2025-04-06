/*     */ package com.notnoop.apns;
/*     */ 
/*     */ import com.notnoop.apns.internal.Utilities;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
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
/*     */ public class EnhancedApnsNotification
/*     */   implements ApnsNotification
/*     */ {
/*     */   private static final byte COMMAND = 1;
/*  44 */   private static int nextId = 0; private final int identifier;
/*     */   private final int expiry;
/*     */   private final byte[] deviceToken;
/*     */   private final byte[] payload;
/*     */   public static final int MAXIMUM_EXPIRY = 2147483647;
/*     */   
/*     */   public static int INCREMENT_ID() {
/*  51 */     return ++nextId;
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
/*  62 */   public static final Date MAXIMUM_DATE = new Date(2147483647000L);
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
/*     */   private byte[] marshall;
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
/*     */   public EnhancedApnsNotification(int identifier, int expiryTime, String dtoken, String payload)
/*     */   {
/* 120 */     this.marshall = null; this.identifier = identifier; this.expiry = expiryTime; this.deviceToken = Utilities.decodeHex(dtoken); this.payload = Utilities.toUTF8Bytes(payload); } public EnhancedApnsNotification(int identifier, int expiryTime, byte[] dtoken, byte[] payload) { this.marshall = null;
/*     */     this.identifier = identifier;
/*     */     this.expiry = expiryTime;
/*     */     this.deviceToken = Utilities.copyOf(dtoken);
/*     */     this.payload = Utilities.copyOf(payload); } public byte[] getDeviceToken() {
/*     */     return Utilities.copyOf(this.deviceToken);
/*     */   } public byte[] getPayload() {
/*     */     return Utilities.copyOf(this.payload);
/*     */   } public byte[] marshall() {
/* 129 */     if (this.marshall == null) {
/* 130 */       this.marshall = Utilities.marshallEnhanced((byte)1, this.identifier, this.expiry, this.deviceToken, this.payload);
/*     */     }
/*     */     
/* 133 */     return this.marshall;
/*     */   }
/*     */   public int getIdentifier() {
/*     */     return this.identifier;
/*     */   }
/*     */   
/*     */   public int getExpiry() {
/*     */     return this.expiry;
/*     */   }
/*     */   
/*     */   public int length() {
/* 144 */     int length = 11 + this.deviceToken.length + 2 + this.payload.length;
/* 145 */     assert (marshall()).length == length;
/* 146 */     return length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 151 */     return 21 + 31 * this.identifier + 31 * this.expiry + 31 * Arrays.hashCode(this.deviceToken) + 31 * Arrays.hashCode(this.payload);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 160 */     if (!(obj instanceof EnhancedApnsNotification))
/* 161 */       return false; 
/* 162 */     EnhancedApnsNotification o = (EnhancedApnsNotification)obj;
/* 163 */     return (this.identifier == o.identifier && this.expiry == o.expiry && Arrays.equals(this.deviceToken, o.deviceToken) && Arrays.equals(this.payload, o.payload));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     String payloadString = "???";
/*     */     try {
/* 173 */       payloadString = new String(this.payload, "UTF-8");
/* 174 */     } catch (Exception _) {}
/* 175 */     return "Message(Id=" + this.identifier + "; Token=" + Utilities.encodeHex(this.deviceToken) + "; Payload=" + payloadString + ")";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/EnhancedApnsNotification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */