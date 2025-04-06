/*    */ package bsh;
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
/*    */ public class ClassIdentifier
/*    */ {
/*    */   Class clas;
/*    */   
/*    */   public ClassIdentifier(Class clas) {
/* 41 */     this.clas = clas;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class getTargetClass() {
/* 46 */     return this.clas;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 50 */     return "Class Identifier: " + this.clas.getName();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ClassIdentifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */