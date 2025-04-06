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
/*    */ public abstract class ReflectManager
/*    */ {
/*    */   private static ReflectManager rfm;
/*    */   
/*    */   public static ReflectManager getReflectManager() throws Capabilities.Unavailable {
/* 56 */     if (rfm == null) {
/*    */       
/*    */       try {
/*    */         
/* 60 */         Class<?> clas = Class.forName("bsh.reflect.ReflectManagerImpl");
/* 61 */         rfm = (ReflectManager)clas.newInstance();
/* 62 */       } catch (Exception e) {
/* 63 */         throw new Capabilities.Unavailable("Reflect Manager unavailable: " + e);
/*    */       } 
/*    */     }
/*    */     
/* 67 */     return rfm;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean RMSetAccessible(Object obj) throws Capabilities.Unavailable {
/* 78 */     return getReflectManager().setAccessible(obj);
/*    */   }
/*    */   
/*    */   public abstract boolean setAccessible(Object paramObject);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ReflectManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */