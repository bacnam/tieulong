/*    */ package com.mchange.v2.holders;
/*    */ 
/*    */ import com.mchange.v2.ser.UnsupportedVersionException;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VolatileIntHolder
/*    */   implements ThreadSafeIntHolder, Serializable
/*    */ {
/*    */   volatile transient int value;
/*    */   static final long serialVersionUID = 1L;
/*    */   private static final short VERSION = 1;
/*    */   
/*    */   public int getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public void setValue(int paramInt) {
/* 49 */     this.value = paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 57 */     paramObjectOutputStream.writeShort(1);
/* 58 */     paramObjectOutputStream.writeInt(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/* 63 */     short s = paramObjectInputStream.readShort();
/* 64 */     switch (s) {
/*    */       
/*    */       case 1:
/* 67 */         this.value = paramObjectInputStream.readInt();
/*    */         return;
/*    */     } 
/* 70 */     throw new UnsupportedVersionException(this, s);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/holders/VolatileIntHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */