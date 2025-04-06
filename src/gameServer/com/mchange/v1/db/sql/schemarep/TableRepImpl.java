/*     */ package com.mchange.v1.db.sql.schemarep;
/*     */ 
/*     */ import com.mchange.v1.util.ListUtils;
/*     */ import com.mchange.v1.util.MapUtils;
/*     */ import com.mchange.v1.util.SetUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class TableRepImpl
/*     */   implements TableRep
/*     */ {
/*     */   String tableName;
/*     */   List colNameList;
/*     */   Map namesToColReps;
/*     */   Set primaryKeyColNames;
/*     */   Set foreignKeyReps;
/*     */   Set uniqConstrReps;
/*     */   
/*     */   public TableRepImpl(String paramString, List<ColumnRep> paramList, Collection<?> paramCollection1, Collection<?> paramCollection2, Collection<?> paramCollection3) {
/*  57 */     this.tableName = paramString;
/*  58 */     ArrayList<String> arrayList = new ArrayList();
/*  59 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>(); byte b; int i;
/*  60 */     for (b = 0, i = paramList.size(); b < i; b++) {
/*     */       
/*  62 */       ColumnRep columnRep = paramList.get(b);
/*  63 */       String str = columnRep.getColumnName();
/*  64 */       arrayList.add(str);
/*  65 */       hashMap.put(str, columnRep);
/*     */     } 
/*  67 */     this.colNameList = Collections.unmodifiableList(arrayList);
/*  68 */     this.namesToColReps = Collections.unmodifiableMap(hashMap);
/*  69 */     this.primaryKeyColNames = (paramCollection1 == null) ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet(paramCollection1));
/*     */ 
/*     */     
/*  72 */     this.foreignKeyReps = (paramCollection2 == null) ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet(paramCollection2));
/*     */ 
/*     */     
/*  75 */     this.uniqConstrReps = (paramCollection3 == null) ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet(paramCollection3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  81 */     return this.tableName;
/*     */   }
/*     */   public Iterator getColumnNames() {
/*  84 */     return this.colNameList.iterator();
/*     */   }
/*     */   public ColumnRep columnRepForName(String paramString) {
/*  87 */     return (ColumnRep)this.namesToColReps.get(paramString);
/*     */   }
/*     */   public Set getPrimaryKeyColumnNames() {
/*  90 */     return this.primaryKeyColNames;
/*     */   }
/*     */   public Set getForeignKeyReps() {
/*  93 */     return this.foreignKeyReps;
/*     */   }
/*     */   public Set getUniquenessConstraintReps() {
/*  96 */     return this.uniqConstrReps;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 100 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 101 */       return false;
/*     */     }
/* 103 */     TableRepImpl tableRepImpl = (TableRepImpl)paramObject;
/* 104 */     return (this.tableName.equals(tableRepImpl.tableName) && ListUtils.equivalent(this.colNameList, tableRepImpl.colNameList) && MapUtils.equivalentDisregardingSort(this.namesToColReps, tableRepImpl.namesToColReps) && SetUtils.equivalentDisregardingSort(this.primaryKeyColNames, tableRepImpl.primaryKeyColNames) && SetUtils.equivalentDisregardingSort(this.foreignKeyReps, tableRepImpl.foreignKeyReps) && SetUtils.equivalentDisregardingSort(this.uniqConstrReps, tableRepImpl.uniqConstrReps));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 115 */     return this.tableName.hashCode() ^ ListUtils.hashContents(this.colNameList) ^ MapUtils.hashContentsDisregardingSort(this.namesToColReps) ^ SetUtils.hashContentsDisregardingSort(this.primaryKeyColNames) ^ SetUtils.hashContentsDisregardingSort(this.foreignKeyReps) ^ SetUtils.hashContentsDisregardingSort(this.uniqConstrReps);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/TableRepImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */