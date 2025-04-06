/*    */ package com.mchange.v2.csv;
/*    */ 
/*    */ import com.mchange.lang.PotentiallySecondaryException;
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
/*    */ public class MalformedCsvException
/*    */   extends PotentiallySecondaryException
/*    */ {
/*    */   public MalformedCsvException(String paramString, Throwable paramThrowable) {
/* 43 */     super(paramString, paramThrowable);
/*    */   }
/*    */   public MalformedCsvException(Throwable paramThrowable) {
/* 46 */     super(paramThrowable);
/*    */   }
/*    */   public MalformedCsvException(String paramString) {
/* 49 */     super(paramString);
/*    */   }
/*    */   
/*    */   public MalformedCsvException() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/csv/MalformedCsvException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */