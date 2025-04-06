/*    */ package bsh;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Modifiers
/*    */   implements Serializable
/*    */ {
/*    */   public static final int CLASS = 0;
/*    */   public static final int METHOD = 1;
/*    */   public static final int FIELD = 2;
/*    */   Hashtable modifiers;
/*    */   
/*    */   public void addModifier(int context, String name) {
/* 22 */     if (this.modifiers == null) {
/* 23 */       this.modifiers = new Hashtable<Object, Object>();
/*    */     }
/* 25 */     Object<void> existing = (Object<void>)this.modifiers.put(name, void.class);
/* 26 */     if (existing != null) {
/* 27 */       throw new IllegalStateException("Duplicate modifier: " + name);
/*    */     }
/* 29 */     int count = 0;
/* 30 */     if (hasModifier("private")) count++; 
/* 31 */     if (hasModifier("protected")) count++; 
/* 32 */     if (hasModifier("public")) count++; 
/* 33 */     if (count > 1) {
/* 34 */       throw new IllegalStateException("public/private/protected cannot be used in combination.");
/*    */     }
/*    */     
/* 37 */     switch (context) {
/*    */       
/*    */       case 0:
/* 40 */         validateForClass();
/*    */         break;
/*    */       case 1:
/* 43 */         validateForMethod();
/*    */         break;
/*    */       case 2:
/* 46 */         validateForField();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasModifier(String name) {
/* 53 */     if (this.modifiers == null)
/* 54 */       this.modifiers = new Hashtable<Object, Object>(); 
/* 55 */     return (this.modifiers.get(name) != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void validateForMethod() {
/* 61 */     insureNo("volatile", "Method");
/* 62 */     insureNo("transient", "Method");
/*    */   }
/*    */   
/*    */   private void validateForField() {
/* 66 */     insureNo("synchronized", "Variable");
/* 67 */     insureNo("native", "Variable");
/* 68 */     insureNo("abstract", "Variable");
/*    */   }
/*    */   
/*    */   private void validateForClass() {
/* 72 */     validateForMethod();
/* 73 */     insureNo("native", "Class");
/* 74 */     insureNo("synchronized", "Class");
/*    */   }
/*    */ 
/*    */   
/*    */   private void insureNo(String modifier, String context) {
/* 79 */     if (hasModifier(modifier)) {
/* 80 */       throw new IllegalStateException(context + " cannot be declared '" + modifier + "'");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return "Modifiers: " + this.modifiers;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Modifiers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */