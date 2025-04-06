/*    */ package com.mchange.v2.cfg;
/*    */ 
/*    */ import com.mchange.v2.lang.ObjectUtils;
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
/*    */ public final class DelayedLogItem
/*    */ {
/*    */   private Level level;
/*    */   private String text;
/*    */   private Throwable exception;
/*    */   
/*    */   public enum Level
/*    */   {
/* 43 */     ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Level getLevel() {
/* 49 */     return this.level;
/* 50 */   } public String getText() { return this.text; } public Throwable getException() {
/* 51 */     return this.exception;
/*    */   }
/*    */   
/*    */   public DelayedLogItem(Level paramLevel, String paramString, Throwable paramThrowable) {
/* 55 */     this.level = paramLevel;
/* 56 */     this.text = paramString;
/* 57 */     this.exception = paramThrowable;
/*    */   }
/*    */   
/*    */   public DelayedLogItem(Level paramLevel, String paramString) {
/* 61 */     this(paramLevel, paramString, null);
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 65 */     if (paramObject instanceof DelayedLogItem) {
/*    */       
/* 67 */       DelayedLogItem delayedLogItem = (DelayedLogItem)paramObject;
/* 68 */       return (this.level.equals(delayedLogItem.level) && this.text.equals(delayedLogItem.text) && ObjectUtils.eqOrBothNull(this.exception, delayedLogItem.exception));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 74 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     return this.level.hashCode() ^ this.text.hashCode() ^ ObjectUtils.hashOrZero(this.exception);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     StringBuffer stringBuffer = new StringBuffer();
/* 88 */     stringBuffer.append(getClass().getName());
/* 89 */     stringBuffer.append(String.format(" [ level -> %s, text -> \"%s\", exception -> %s]", new Object[] { this.level, this.text, this.exception }));
/* 90 */     return stringBuffer.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/DelayedLogItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */