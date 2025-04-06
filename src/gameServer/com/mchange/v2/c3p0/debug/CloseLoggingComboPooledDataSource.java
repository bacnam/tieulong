/*    */ package com.mchange.v2.c3p0.debug;
/*    */ 
/*    */ import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
/*    */ import com.mchange.v2.log.MLevel;
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
/*    */ public final class CloseLoggingComboPooledDataSource
/*    */   extends AbstractComboPooledDataSource
/*    */   implements Serializable, Referenceable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 49 */   volatile MLevel level = MLevel.INFO; private static final short VERSION = 1;
/*    */   public void setCloseLogLevel(MLevel level) {
/* 51 */     this.level = level; } public MLevel getCloseLogLevel() {
/* 52 */     return this.level;
/*    */   }
/*    */   
/*    */   public CloseLoggingComboPooledDataSource() {}
/*    */   
/*    */   public CloseLoggingComboPooledDataSource(boolean autoregister) {
/* 58 */     super(autoregister);
/*    */   }
/*    */   public CloseLoggingComboPooledDataSource(String configName) {
/* 61 */     super(configName);
/*    */   }
/*    */   public Connection getConnection() throws SQLException {
/* 64 */     return (Connection)new CloseLoggingConnectionWrapper(super.getConnection(), this.level);
/*    */   }
/*    */   public Connection getConnection(String user, String password) throws SQLException {
/* 67 */     return (Connection)new CloseLoggingConnectionWrapper(super.getConnection(user, password), this.level);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 75 */     oos.writeShort(1);
/*    */   }
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 80 */     short version = ois.readShort();
/* 81 */     switch (version) {
/*    */       case 1:
/*    */         return;
/*    */     } 
/*    */ 
/*    */     
/* 87 */     throw new IOException("Unsupported Serialized Version: " + version);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/debug/CloseLoggingComboPooledDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */