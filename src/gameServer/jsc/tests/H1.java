/*    */ package jsc.tests;
/*    */ 
/*    */ import jsc.distributions.Tail;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class H1
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   private H1(String paramString) {
/* 17 */     this.name = paramString;
/*    */   } public String toString() {
/* 19 */     return this.name;
/*    */   }
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
/* 31 */   public static final H1 GREATER_THAN = new H1("greater than");
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
/* 42 */   public static final H1 LESS_THAN = new H1("less than");
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
/* 53 */   public static final H1 NOT_EQUAL = new H1("not equal to");
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
/*    */   public static Tail toTail(H1 paramH1) {
/* 67 */     if (paramH1 == NOT_EQUAL) return Tail.TWO; 
/* 68 */     if (paramH1 == GREATER_THAN) return Tail.UPPER; 
/* 69 */     return Tail.LOWER;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/tests/H1.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */