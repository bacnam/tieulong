/*    */ package com.mchange.v1.db.sql.schemarep;
/*    */ 
/*    */ import com.mchange.v1.util.ListUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public class ForeignKeyRepImpl
/*    */   implements ForeignKeyRep
/*    */ {
/*    */   List locColNames;
/*    */   String refTableName;
/*    */   List refColNames;
/*    */   
/*    */   public ForeignKeyRepImpl(List<?> paramList1, String paramString, List<?> paramList2) {
/* 51 */     this.locColNames = Collections.unmodifiableList(new ArrayList(paramList1));
/* 52 */     this.refTableName = paramString;
/* 53 */     this.refColNames = Collections.unmodifiableList(new ArrayList(paramList2));
/*    */   }
/*    */   
/*    */   public List getLocalColumnNames() {
/* 57 */     return this.locColNames;
/*    */   }
/*    */   public String getReferencedTableName() {
/* 60 */     return this.refTableName;
/*    */   }
/*    */   public List getReferencedColumnNames() {
/* 63 */     return this.refColNames;
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 67 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 68 */       return false;
/*    */     }
/* 70 */     ForeignKeyRepImpl foreignKeyRepImpl = (ForeignKeyRepImpl)paramObject;
/* 71 */     return (ListUtils.equivalent(this.locColNames, foreignKeyRepImpl.locColNames) && this.refTableName.equals(foreignKeyRepImpl.refTableName) && ListUtils.equivalent(this.refColNames, foreignKeyRepImpl.refColNames));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     return ListUtils.hashContents(this.locColNames) ^ this.refTableName.hashCode() ^ ListUtils.hashContents(this.refColNames);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/ForeignKeyRepImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */