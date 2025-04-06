/*     */ package com.mchange.v1.db.sql.schemarep;
/*     */ 
/*     */ import com.mchange.lang.ArrayUtils;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColumnRepImpl
/*     */   implements ColumnRep
/*     */ {
/*     */   String colName;
/*     */   int col_type;
/*     */   int[] colSize;
/*     */   boolean accepts_nulls;
/*     */   Object defaultValue;
/*     */   
/*     */   public ColumnRepImpl(String paramString, int paramInt) {
/*  50 */     this(paramString, paramInt, null);
/*     */   }
/*     */   public ColumnRepImpl(String paramString, int paramInt, int[] paramArrayOfint) {
/*  53 */     this(paramString, paramInt, paramArrayOfint, false, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ColumnRepImpl(String paramString, int paramInt, int[] paramArrayOfint, boolean paramBoolean, Object paramObject) {
/*  58 */     this.colName = paramString;
/*  59 */     this.col_type = paramInt;
/*  60 */     this.colSize = paramArrayOfint;
/*  61 */     this.accepts_nulls = paramBoolean;
/*  62 */     this.defaultValue = paramObject;
/*     */   }
/*     */   
/*     */   public String getColumnName() {
/*  66 */     return this.colName;
/*     */   }
/*     */   public int getColumnType() {
/*  69 */     return this.col_type;
/*     */   }
/*     */   public int[] getColumnSize() {
/*  72 */     return this.colSize;
/*     */   }
/*     */   public boolean acceptsNulls() {
/*  75 */     return this.accepts_nulls;
/*     */   }
/*     */   public Object getDefaultValue() {
/*  78 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  82 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/*  83 */       return false;
/*     */     }
/*  85 */     ColumnRepImpl columnRepImpl = (ColumnRepImpl)paramObject;
/*  86 */     if (!this.colName.equals(columnRepImpl.colName) || this.col_type != columnRepImpl.col_type || this.accepts_nulls != columnRepImpl.accepts_nulls)
/*     */     {
/*     */       
/*  89 */       return false;
/*     */     }
/*  91 */     if (this.colSize != columnRepImpl.colSize && !Arrays.equals(this.colSize, columnRepImpl.colSize)) {
/*  92 */       return false;
/*     */     }
/*  94 */     if (this.defaultValue != columnRepImpl.defaultValue && this.defaultValue != null && !this.defaultValue.equals(columnRepImpl.defaultValue))
/*     */     {
/*     */       
/*  97 */       return false;
/*     */     }
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     int i = this.colName.hashCode() ^ this.col_type;
/*     */ 
/*     */ 
/*     */     
/* 108 */     if (!this.accepts_nulls) i ^= 0xFFFFFFFF;
/*     */     
/* 110 */     if (this.colSize != null) {
/* 111 */       i ^= ArrayUtils.hashAll(this.colSize);
/*     */     }
/* 113 */     if (this.defaultValue != null) {
/* 114 */       i ^= this.defaultValue.hashCode();
/*     */     }
/* 116 */     return i;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/ColumnRepImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */