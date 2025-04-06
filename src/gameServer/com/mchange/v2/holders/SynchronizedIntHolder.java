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
/*    */ public class SynchronizedIntHolder
/*    */   implements ThreadSafeIntHolder, Serializable
/*    */ {
/*    */   transient int value;
/*    */   static final long serialVersionUID = 1L;
/*    */   private static final short VERSION = 1;
/*    */   
/*    */   public SynchronizedIntHolder(int paramInt) {
/* 46 */     this.value = paramInt;
/*    */   }
/*    */   public SynchronizedIntHolder() {
/* 49 */     this(0);
/*    */   }
/*    */   public synchronized int getValue() {
/* 52 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(int paramInt) {
/* 55 */     this.value = paramInt;
/*    */   }
/*    */   public synchronized void increment() {
/* 58 */     this.value++;
/*    */   }
/*    */   public synchronized void decrement() {
/* 61 */     this.value--;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 69 */     paramObjectOutputStream.writeShort(1);
/* 70 */     paramObjectOutputStream.writeInt(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/* 75 */     short s = paramObjectInputStream.readShort();
/* 76 */     switch (s) {
/*    */       
/*    */       case 1:
/* 79 */         this.value = paramObjectInputStream.readInt();
/*    */         return;
/*    */     } 
/* 82 */     throw new UnsupportedVersionException(this, s);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/holders/SynchronizedIntHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */