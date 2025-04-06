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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SynchronizedBooleanHolder
/*    */   implements ThreadSafeBooleanHolder, Serializable
/*    */ {
/*    */   transient boolean value;
/*    */   static final long serialVersionUID = 1L;
/*    */   private static final short VERSION = 1;
/*    */   
/*    */   public synchronized boolean getValue() {
/* 53 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(boolean paramBoolean) {
/* 56 */     this.value = paramBoolean;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 65 */     paramObjectOutputStream.writeShort(1);
/* 66 */     paramObjectOutputStream.writeBoolean(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/* 71 */     short s = paramObjectInputStream.readShort();
/* 72 */     switch (s) {
/*    */       
/*    */       case 1:
/* 75 */         this.value = paramObjectInputStream.readBoolean();
/*    */         return;
/*    */     } 
/* 78 */     throw new UnsupportedVersionException(this, s);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/holders/SynchronizedBooleanHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */