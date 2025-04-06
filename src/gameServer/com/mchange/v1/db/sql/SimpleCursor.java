/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import com.mchange.v1.util.UIterator;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
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
/*    */ public abstract class SimpleCursor
/*    */   implements UIterator
/*    */ {
/*    */   ResultSet rs;
/* 45 */   int available = -1;
/*    */   
/*    */   public SimpleCursor(ResultSet paramResultSet) {
/* 48 */     this.rs = paramResultSet;
/*    */   }
/*    */   
/*    */   public boolean hasNext() throws SQLException {
/* 52 */     ratchet();
/* 53 */     return (this.available == 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object next() throws SQLException {
/* 58 */     ratchet();
/* 59 */     Object object = objectFromResultSet(this.rs);
/* 60 */     clear();
/* 61 */     return object;
/*    */   }
/*    */   
/*    */   public void remove() {
/* 65 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void close() throws Exception {
/* 69 */     this.rs.close();
/* 70 */     this.rs = null;
/*    */   }
/*    */   
/*    */   public void finalize() throws Exception {
/* 74 */     if (this.rs != null) close(); 
/*    */   }
/*    */   
/*    */   protected abstract Object objectFromResultSet(ResultSet paramResultSet) throws SQLException;
/*    */   
/*    */   private void ratchet() throws SQLException {
/* 80 */     if (this.available == -1)
/* 81 */       this.available = this.rs.next() ? 1 : 0; 
/*    */   }
/*    */   
/*    */   private void clear() {
/* 85 */     this.available = -1;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/SimpleCursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */