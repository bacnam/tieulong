/*    */ package jsc.util;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CaseInsensitiveVector
/*    */   extends Vector
/*    */ {
/*    */   public CaseInsensitiveVector() {}
/*    */   
/*    */   public CaseInsensitiveVector(int paramInt) {
/* 18 */     super(paramInt);
/*    */   }
/*    */   public CaseInsensitiveVector(int paramInt1, int paramInt2) {
/* 21 */     super(paramInt1, paramInt2);
/*    */   }
/*    */   public boolean containsString(String paramString) {
/* 24 */     return (indexOfString(paramString) >= 0);
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
/*    */   public int indexOfString(String paramString) {
/* 36 */     for (byte b = 0; b < size(); b++) {
/*    */       
/* 38 */       String str = (String)elementAt(b);
/* 39 */       if (paramString.equalsIgnoreCase(str)) return b; 
/*    */     } 
/* 41 */     return -1;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/CaseInsensitiveVector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */