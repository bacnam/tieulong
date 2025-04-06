/*    */ package org.apache.http.conn.util;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.Args;
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
/*    */ 
/*    */ @Immutable
/*    */ public final class PublicSuffixList
/*    */ {
/*    */   private final List<String> rules;
/*    */   private final List<String> exceptions;
/*    */   
/*    */   public PublicSuffixList(List<String> rules, List<String> exceptions) {
/* 51 */     this.rules = Collections.unmodifiableList((List<? extends String>)Args.notNull(rules, "Domain suffix rules"));
/* 52 */     this.exceptions = Collections.unmodifiableList((List<? extends String>)Args.notNull(exceptions, "Domain suffix exceptions"));
/*    */   }
/*    */   
/*    */   public List<String> getRules() {
/* 56 */     return this.rules;
/*    */   }
/*    */   
/*    */   public List<String> getExceptions() {
/* 60 */     return this.exceptions;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/util/PublicSuffixList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */