/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.ResultSetMetaData;
/*    */ import java.util.Map;
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
/*    */ public class CachedResultSetMetaData
/*    */ {
/* 31 */   Map<String, Integer> columnNameToIndex = null;
/*    */ 
/*    */   
/*    */   Field[] fields;
/*    */ 
/*    */   
/* 37 */   Map<String, Integer> fullColumnNameToIndex = null;
/*    */   
/*    */   ResultSetMetaData metadata;
/*    */ 
/*    */   
/*    */   public Map<String, Integer> getColumnNameToIndex() {
/* 43 */     return this.columnNameToIndex;
/*    */   }
/*    */   
/*    */   public Field[] getFields() {
/* 47 */     return this.fields;
/*    */   }
/*    */   
/*    */   public Map<String, Integer> getFullColumnNameToIndex() {
/* 51 */     return this.fullColumnNameToIndex;
/*    */   }
/*    */   
/*    */   public ResultSetMetaData getMetadata() {
/* 55 */     return this.metadata;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/CachedResultSetMetaData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */