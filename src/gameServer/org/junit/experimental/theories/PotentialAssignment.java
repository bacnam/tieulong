/*    */ package org.junit.experimental.theories;
/*    */ 
/*    */ public abstract class PotentialAssignment
/*    */ {
/*    */   public static class CouldNotGenerateValueException
/*    */     extends Exception
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public CouldNotGenerateValueException() {}
/*    */     
/*    */     public CouldNotGenerateValueException(Throwable e) {
/* 13 */       super(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public static PotentialAssignment forValue(final String name, final Object value) {
/* 18 */     return new PotentialAssignment()
/*    */       {
/*    */         public Object getValue() {
/* 21 */           return value;
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 26 */           return String.format("[%s]", new Object[] { this.val$value });
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public String getDescription() {
/*    */           String str;
/* 33 */           if (value == null) {
/* 34 */             str = "null";
/*    */           } else {
/*    */             try {
/* 37 */               str = String.format("\"%s\"", new Object[] { this.val$value });
/* 38 */             } catch (Throwable e) {
/* 39 */               str = String.format("[toString() threw %s: %s]", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
/*    */             } 
/*    */           } 
/*    */ 
/*    */           
/* 44 */           return String.format("%s <from %s>", new Object[] { str, this.val$name });
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public abstract Object getValue() throws CouldNotGenerateValueException;
/*    */   
/*    */   public abstract String getDescription() throws CouldNotGenerateValueException;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/experimental/theories/PotentialAssignment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */