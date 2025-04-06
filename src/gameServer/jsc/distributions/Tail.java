/*    */ package jsc.distributions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Tail
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   private Tail(String paramString) {
/* 15 */     this.name = paramString;
/*    */   } public String toString() {
/* 17 */     return this.name;
/*    */   }
/*    */   
/* 20 */   public static final Tail LOWER = new Tail("lower tail");
/*    */ 
/*    */   
/* 23 */   public static final Tail UPPER = new Tail("upper tail");
/*    */ 
/*    */   
/* 26 */   public static final Tail TWO = new Tail("two-tail");
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Tail.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */