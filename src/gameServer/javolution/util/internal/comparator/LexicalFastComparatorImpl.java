/*    */ package javolution.util.internal.comparator;
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
/*    */ public class LexicalFastComparatorImpl
/*    */   extends LexicalComparatorImpl
/*    */ {
/*    */   private static final long serialVersionUID = -1449702752185594025L;
/*    */   
/*    */   public int hashCodeOf(CharSequence csq) {
/* 20 */     if (csq == null)
/* 21 */       return 0; 
/* 22 */     int n = csq.length();
/* 23 */     if (n == 0) {
/* 24 */       return 0;
/*    */     }
/* 26 */     return csq.charAt(0) + csq.charAt(n - 1) * 31 + csq.charAt(n >> 1) * 1009 + csq.charAt(n >> 2) * 27583 + csq.charAt(n - 1 - (n >> 2)) * 73408859;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/comparator/LexicalFastComparatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */