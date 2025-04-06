/*    */ package org.junit.runners.model;
/*    */ 
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FrameworkMember<T extends FrameworkMember<T>>
/*    */   implements Annotatable
/*    */ {
/*    */   abstract boolean isShadowedBy(T paramT);
/*    */   
/*    */   boolean isShadowedBy(List<T> members) {
/* 16 */     for (FrameworkMember frameworkMember : members) {
/* 17 */       if (isShadowedBy((T)frameworkMember)) {
/* 18 */         return true;
/*    */       }
/*    */     } 
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract int getModifiers();
/*    */ 
/*    */   
/*    */   public boolean isStatic() {
/* 30 */     return Modifier.isStatic(getModifiers());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPublic() {
/* 37 */     return Modifier.isPublic(getModifiers());
/*    */   }
/*    */   
/*    */   public abstract String getName();
/*    */   
/*    */   public abstract Class<?> getType();
/*    */   
/*    */   public abstract Class<?> getDeclaringClass();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/runners/model/FrameworkMember.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */