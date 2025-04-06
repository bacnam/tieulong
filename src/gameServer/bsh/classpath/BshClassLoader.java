/*     */ package bsh.classpath;
/*     */ 
/*     */ import bsh.BshClassManager;
/*     */ import bsh.Interpreter;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
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
/*     */ public class BshClassLoader
/*     */   extends URLClassLoader
/*     */ {
/*     */   BshClassManager classManager;
/*     */   
/*     */   public BshClassLoader(BshClassManager classManager, URL[] bases) {
/*  54 */     super(bases);
/*  55 */     this.classManager = classManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BshClassLoader(BshClassManager classManager, BshClassPath bcp) {
/*  62 */     this(classManager, bcp.getPathComponents());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BshClassLoader(BshClassManager classManager) {
/*  70 */     this(classManager, new URL[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addURL(URL url) {
/*  75 */     super.addURL(url);
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
/*     */   public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
/*  89 */     Class<?> c = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     c = findLoadedClass(name);
/*  96 */     if (c != null) {
/*  97 */       return c;
/*     */     }
/*     */ 
/*     */     
/* 101 */     if (name.startsWith("bsh")) {
/*     */       try {
/* 103 */         return Interpreter.class.getClassLoader().loadClass(name);
/* 104 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 111 */       c = findClass(name);
/* 112 */     } catch (ClassNotFoundException e) {}
/*     */     
/* 114 */     if (c == null) {
/* 115 */       throw new ClassNotFoundException("here in loaClass");
/*     */     }
/* 117 */     if (resolve) {
/* 118 */       resolveClass(c);
/*     */     }
/* 120 */     return c;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class findClass(String name) throws ClassNotFoundException {
/* 138 */     ClassManagerImpl bcm = (ClassManagerImpl)getClassManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     ClassLoader cl = bcm.getLoaderForClass(name);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (cl != null && cl != this) {
/*     */       try {
/* 153 */         return cl.loadClass(name);
/* 154 */       } catch (ClassNotFoundException e) {
/* 155 */         throw new ClassNotFoundException("Designated loader could not find class: " + e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 160 */     if ((getURLs()).length > 0) {
/*     */       try {
/* 162 */         return super.findClass(name);
/* 163 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     cl = bcm.getBaseLoader();
/*     */     
/* 172 */     if (cl != null && cl != this) {
/*     */       try {
/* 174 */         return cl.loadClass(name);
/* 175 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */     
/* 178 */     return bcm.plainClassForName(name);
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
/*     */ 
/*     */ 
/*     */   
/*     */   BshClassManager getClassManager() {
/* 195 */     return this.classManager;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/classpath/BshClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */