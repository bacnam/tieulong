/*    */ package org.apache.mina.core.session;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class AttributeKey
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -583377473376683096L;
/*    */   private final String name;
/*    */   
/*    */   public AttributeKey(Class<?> source, String name) {
/* 59 */     this.name = source.getName() + '.' + name + '@' + Integer.toHexString(hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     int h = 629 + ((this.name == null) ? 0 : this.name.hashCode());
/* 73 */     return h;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 78 */     if (this == obj) {
/* 79 */       return true;
/*    */     }
/*    */     
/* 82 */     if (!(obj instanceof AttributeKey)) {
/* 83 */       return false;
/*    */     }
/*    */     
/* 86 */     AttributeKey other = (AttributeKey)obj;
/*    */     
/* 88 */     return this.name.equals(other.name);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/AttributeKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */