/*    */ package javolution.util.internal.comparator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javolution.util.function.Equality;
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
/*    */ public class LexicalCaseInsensitiveComparatorImpl
/*    */   implements Equality<CharSequence>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -1046672327934410697L;
/*    */   
/*    */   public int hashCodeOf(CharSequence csq) {
/* 25 */     if (csq == null)
/* 26 */       return 0; 
/* 27 */     int h = 0;
/* 28 */     for (int i = 0, n = csq.length(); i < n;) {
/* 29 */       h = 31 * h + up(csq.charAt(i++));
/*    */     }
/* 31 */     return h;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean areEqual(CharSequence csq1, CharSequence csq2) {
/* 36 */     if (csq1 == csq2)
/* 37 */       return true; 
/* 38 */     if (csq1 == null || csq2 == null)
/* 39 */       return false; 
/* 40 */     if (csq1 instanceof String && csq2 instanceof String)
/* 41 */       return ((String)csq1).equalsIgnoreCase((String)csq2); 
/* 42 */     int n = csq1.length();
/* 43 */     if (csq2.length() != n)
/* 44 */       return false; 
/* 45 */     for (int i = 0; i < n;) {
/* 46 */       if (up(csq1.charAt(i)) != up(csq2.charAt(i++)))
/* 47 */         return false; 
/*    */     } 
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(CharSequence left, CharSequence right) {
/* 54 */     if (left == null)
/* 55 */       return -1; 
/* 56 */     if (right == null)
/* 57 */       return 1; 
/* 58 */     if (left instanceof String && right instanceof String)
/* 59 */       return ((String)left).compareToIgnoreCase((String)right); 
/* 60 */     int i = 0;
/* 61 */     int n = Math.min(left.length(), right.length());
/* 62 */     while (n-- != 0) {
/* 63 */       char c1 = up(left.charAt(i));
/* 64 */       char c2 = up(right.charAt(i++));
/* 65 */       if (c1 != c2)
/* 66 */         return c1 - c2; 
/*    */     } 
/* 68 */     return left.length() - right.length();
/*    */   }
/*    */ 
/*    */   
/*    */   private static char up(char c) {
/* 73 */     return Character.toUpperCase(c);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/comparator/LexicalCaseInsensitiveComparatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */