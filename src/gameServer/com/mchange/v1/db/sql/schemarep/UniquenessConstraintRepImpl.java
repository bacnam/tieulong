/*    */ package com.mchange.v1.db.sql.schemarep;
/*    */ 
/*    */ import com.mchange.v1.util.SetUtils;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class UniquenessConstraintRepImpl
/*    */   implements UniquenessConstraintRep
/*    */ {
/*    */   Set uniqueColNames;
/*    */   
/*    */   public UniquenessConstraintRepImpl(Collection<?> paramCollection) {
/* 49 */     this.uniqueColNames = Collections.unmodifiableSet(new HashSet(paramCollection));
/*    */   }
/*    */   public Set getUniqueColumnNames() {
/* 52 */     return this.uniqueColNames;
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 56 */     return (paramObject != null && getClass() == paramObject.getClass() && SetUtils.equivalentDisregardingSort(this.uniqueColNames, ((UniquenessConstraintRepImpl)paramObject).uniqueColNames));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 65 */     return getClass().hashCode() ^ SetUtils.hashContentsDisregardingSort(this.uniqueColNames);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/UniquenessConstraintRepImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */