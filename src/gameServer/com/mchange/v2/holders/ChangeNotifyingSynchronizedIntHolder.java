/*     */ package com.mchange.v2.holders;
/*     */ 
/*     */ import com.mchange.v2.ser.UnsupportedVersionException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
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
/*     */ public final class ChangeNotifyingSynchronizedIntHolder
/*     */   implements ThreadSafeIntHolder, Serializable
/*     */ {
/*     */   transient int value;
/*     */   transient boolean notify_all;
/*     */   static final long serialVersionUID = 1L;
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public ChangeNotifyingSynchronizedIntHolder(int paramInt, boolean paramBoolean) {
/*  48 */     this.value = paramInt;
/*  49 */     this.notify_all = paramBoolean;
/*     */   }
/*     */   
/*     */   public ChangeNotifyingSynchronizedIntHolder() {
/*  53 */     this(0, true);
/*     */   }
/*     */   public synchronized int getValue() {
/*  56 */     return this.value;
/*     */   }
/*     */   
/*     */   public synchronized void setValue(int paramInt) {
/*  60 */     if (paramInt != this.value) {
/*     */       
/*  62 */       this.value = paramInt;
/*  63 */       doNotify();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void increment() {
/*  69 */     this.value++;
/*  70 */     doNotify();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void decrement() {
/*  75 */     this.value--;
/*  76 */     doNotify();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doNotify() {
/*  82 */     if (this.notify_all) { notifyAll(); }
/*  83 */     else { notify(); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/*  92 */     paramObjectOutputStream.writeShort(1);
/*  93 */     paramObjectOutputStream.writeInt(this.value);
/*  94 */     paramObjectOutputStream.writeBoolean(this.notify_all);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/*  99 */     short s = paramObjectInputStream.readShort();
/* 100 */     switch (s) {
/*     */       
/*     */       case 1:
/* 103 */         this.value = paramObjectInputStream.readInt();
/* 104 */         this.notify_all = paramObjectInputStream.readBoolean();
/*     */         return;
/*     */     } 
/* 107 */     throw new UnsupportedVersionException(this, s);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/holders/ChangeNotifyingSynchronizedIntHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */