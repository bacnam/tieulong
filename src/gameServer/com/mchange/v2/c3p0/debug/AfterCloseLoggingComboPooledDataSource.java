/*    */ package com.mchange.v2.c3p0.debug;
/*    */ 
/*    */ import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
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
/*    */ public final class AfterCloseLoggingComboPooledDataSource
/*    */   extends AbstractComboPooledDataSource
/*    */   implements Serializable, Referenceable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final short VERSION = 1;
/*    */   
/*    */   public AfterCloseLoggingComboPooledDataSource() {}
/*    */   
/*    */   public AfterCloseLoggingComboPooledDataSource(boolean autoregister) {
/* 53 */     super(autoregister);
/*    */   }
/*    */   public AfterCloseLoggingComboPooledDataSource(String configName) {
/* 56 */     super(configName);
/*    */   }
/*    */   public Connection getConnection() throws SQLException {
/* 59 */     return AfterCloseLoggingConnectionWrapper.wrap(super.getConnection());
/*    */   }
/*    */   public Connection getConnection(String user, String password) throws SQLException {
/* 62 */     return AfterCloseLoggingConnectionWrapper.wrap(super.getConnection(user, password));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 70 */     oos.writeShort(1);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 75 */     short version = ois.readShort();
/* 76 */     switch (version) {
/*    */       case 1:
/*    */         return;
/*    */     } 
/*    */ 
/*    */     
/* 82 */     throw new IOException("Unsupported Serialized Version: " + version);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/debug/AfterCloseLoggingComboPooledDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */