/*    */ package com.mchange.v2.c3p0;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import javax.naming.Referenceable;
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
/*    */ public final class ComboPooledDataSource
/*    */   extends AbstractComboPooledDataSource
/*    */   implements Serializable, Referenceable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final short VERSION = 2;
/*    */   
/*    */   public ComboPooledDataSource() {}
/*    */   
/*    */   public ComboPooledDataSource(boolean autoregister) {
/* 50 */     super(autoregister);
/*    */   }
/*    */   public ComboPooledDataSource(String configName) {
/* 53 */     super(configName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 62 */     oos.writeShort(2);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 67 */     short version = ois.readShort();
/* 68 */     switch (version) {
/*    */       case 2:
/*    */         return;
/*    */     } 
/*    */ 
/*    */     
/* 74 */     throw new IOException("Unsupported Serialized Version: " + version);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/ComboPooledDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */