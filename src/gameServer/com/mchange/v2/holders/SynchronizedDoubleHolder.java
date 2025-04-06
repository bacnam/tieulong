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
/*    */ public class SynchronizedDoubleHolder
/*    */   implements ThreadSafeDoubleHolder, Serializable
/*    */ {
/*    */   transient double value;
/*    */   static final long serialVersionUID = 1L;
/*    */   private static final short VERSION = 1;
/*    */   
/*    */   public synchronized double getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(double paramDouble) {
/* 49 */     this.value = paramDouble;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 58 */     paramObjectOutputStream.writeShort(1);
/* 59 */     paramObjectOutputStream.writeDouble(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/* 64 */     short s = paramObjectInputStream.readShort();
/* 65 */     switch (s) {
/*    */       
/*    */       case 1:
/* 68 */         this.value = paramObjectInputStream.readDouble();
/*    */         return;
/*    */     } 
/* 71 */     throw new UnsupportedVersionException(this, s);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/holders/SynchronizedDoubleHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */