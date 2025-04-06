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
/*    */ public class SynchronizedLongHolder
/*    */   implements ThreadSafeLongHolder, Serializable
/*    */ {
/*    */   transient long value;
/*    */   static final long serialVersionUID = 1L;
/*    */   private static final short VERSION = 1;
/*    */   
/*    */   public synchronized long getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   public synchronized void setValue(long paramLong) {
/* 49 */     this.value = paramLong;
/*    */   }
/*    */   public SynchronizedLongHolder(long paramLong) {
/* 52 */     this.value = paramLong;
/*    */   }
/*    */   public SynchronizedLongHolder() {
/* 55 */     this(0L);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 64 */     paramObjectOutputStream.writeShort(1);
/* 65 */     paramObjectOutputStream.writeLong(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/* 70 */     short s = paramObjectInputStream.readShort();
/* 71 */     switch (s) {
/*    */       
/*    */       case 1:
/* 74 */         this.value = paramObjectInputStream.readLong();
/*    */         return;
/*    */     } 
/* 77 */     throw new UnsupportedVersionException(this, s);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/holders/SynchronizedLongHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */