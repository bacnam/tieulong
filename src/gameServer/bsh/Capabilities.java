/*     */ package bsh;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Capabilities
/*     */ {
/*     */   private static boolean accessibility = false;
/*     */   
/*     */   public static boolean haveSwing() {
/*  55 */     return classExists("javax.swing.JButton");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canGenerateInterfaces() {
/*  60 */     return classExists("java.lang.reflect.Proxy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean haveAccessibility() {
/*  74 */     return accessibility;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setAccessibility(boolean b) throws Unavailable {
/*  80 */     if (!b) {
/*     */       
/*  82 */       accessibility = false;
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     if (!classExists("java.lang.reflect.AccessibleObject") || !classExists("bsh.reflect.ReflectManagerImpl"))
/*     */     {
/*     */       
/*  89 */       throw new Unavailable("Accessibility unavailable");
/*     */     }
/*     */     
/*     */     try {
/*  93 */       String.class.getDeclaredMethods();
/*  94 */     } catch (SecurityException e) {
/*  95 */       throw new Unavailable("Accessibility unavailable: " + e);
/*     */     } 
/*     */     
/*  98 */     accessibility = true;
/*     */   }
/*     */   
/* 101 */   private static Hashtable classes = new Hashtable<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean classExists(String name) {
/* 113 */     Object<?> c = (Object<?>)classes.get(name);
/*     */     
/* 115 */     if (c == null) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 122 */         c = (Object<?>)Class.forName(name);
/* 123 */       } catch (ClassNotFoundException e) {}
/*     */       
/* 125 */       if (c != null) {
/* 126 */         classes.put(c, "unused");
/*     */       }
/*     */     } 
/* 129 */     return (c != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Unavailable
/*     */     extends UtilEvalError
/*     */   {
/*     */     public Unavailable(String s) {
/* 139 */       super(s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Capabilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */