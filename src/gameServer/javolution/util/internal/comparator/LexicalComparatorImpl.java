/*    */ package javolution.util.internal.comparator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javolution.lang.MathLib;
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
/*    */ public class LexicalComparatorImpl
/*    */   implements Equality<CharSequence>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 7904852144917623728L;
/*    */   
/*    */   public int hashCodeOf(CharSequence csq) {
/* 26 */     if (csq == null)
/* 27 */       return 0; 
/* 28 */     if (csq instanceof String)
/* 29 */       return csq.hashCode(); 
/* 30 */     int h = 0;
/* 31 */     for (int i = 0, n = csq.length(); i < n;) {
/* 32 */       h = 31 * h + csq.charAt(i++);
/*    */     }
/* 34 */     return h;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean areEqual(CharSequence csq1, CharSequence csq2) {
/* 39 */     if (csq1 == csq2)
/* 40 */       return true; 
/* 41 */     if (csq1 == null || csq2 == null)
/* 42 */       return false; 
/* 43 */     if (csq1 instanceof String) {
/* 44 */       if (csq2 instanceof String)
/* 45 */         return csq1.equals(csq2); 
/* 46 */       return ((String)csq1).contentEquals(csq2);
/* 47 */     }  if (csq2 instanceof String) return ((String)csq2).contentEquals(csq1);
/*    */ 
/*    */ 
/*    */     
/* 51 */     int n = csq1.length();
/* 52 */     if (csq2.length() != n)
/* 53 */       return false; 
/* 54 */     for (int i = 0; i < n;) {
/* 55 */       if (csq1.charAt(i) != csq2.charAt(i++))
/* 56 */         return false; 
/*    */     } 
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(CharSequence left, CharSequence right) {
/* 63 */     if (left == null)
/* 64 */       return -1; 
/* 65 */     if (right == null)
/* 66 */       return 1; 
/* 67 */     if (left instanceof String && right instanceof String)
/* 68 */       return ((String)left).compareTo((String)right); 
/* 69 */     int i = 0;
/* 70 */     int n = MathLib.min(left.length(), right.length());
/* 71 */     while (n-- != 0) {
/* 72 */       char c1 = left.charAt(i);
/* 73 */       char c2 = right.charAt(i++);
/* 74 */       if (c1 != c2)
/* 75 */         return c1 - c2; 
/*    */     } 
/* 77 */     return left.length() - right.length();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/comparator/LexicalComparatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */