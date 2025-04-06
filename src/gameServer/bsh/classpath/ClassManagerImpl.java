/*     */ package bsh.classpath;
/*     */ 
/*     */ import bsh.BshClassManager;
/*     */ import bsh.ClassPathException;
/*     */ import bsh.Interpreter;
/*     */ import bsh.InterpreterError;
/*     */ import bsh.UtilEvalError;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassManagerImpl
/*     */   extends BshClassManager
/*     */ {
/*     */   static final String BSH_PACKAGE = "bsh";
/*     */   private BshClassPath baseClassPath;
/*     */   private boolean superImport;
/*     */   private BshClassPath fullClassPath;
/* 123 */   private Vector listeners = new Vector();
/* 124 */   private ReferenceQueue refQueue = new ReferenceQueue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BshClassLoader baseLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map loaderMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassManagerImpl() {
/* 143 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class classForName(String name) {
/* 152 */     Class<?> c = (Class)this.absoluteClassCache.get(name);
/* 153 */     if (c != null) {
/* 154 */       return c;
/*     */     }
/*     */     
/* 157 */     if (this.absoluteNonClasses.get(name) != null) {
/* 158 */       if (Interpreter.DEBUG)
/* 159 */         Interpreter.debug("absoluteNonClass list hit: " + name); 
/* 160 */       return null;
/*     */     } 
/*     */     
/* 163 */     if (Interpreter.DEBUG) {
/* 164 */       Interpreter.debug("Trying to load class: " + name);
/*     */     }
/*     */     
/* 167 */     ClassLoader overlayLoader = getLoaderForClass(name);
/* 168 */     if (overlayLoader != null) {
/*     */       
/*     */       try {
/* 171 */         c = overlayLoader.loadClass(name);
/* 172 */       } catch (Exception e) {
/*     */ 
/*     */       
/* 175 */       } catch (NoClassDefFoundError e2) {
/* 176 */         throw noClassDefFound(name, e2);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     if (c == null && 
/* 185 */       name.startsWith("bsh")) {
/*     */       try {
/* 187 */         c = Interpreter.class.getClassLoader().loadClass(name);
/* 188 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */ 
/*     */     
/* 192 */     if (c == null && 
/* 193 */       this.baseLoader != null) {
/*     */       try {
/* 195 */         c = this.baseLoader.loadClass(name);
/* 196 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */ 
/*     */     
/* 200 */     if (c == null && 
/* 201 */       this.externalClassLoader != null) {
/*     */       try {
/* 203 */         c = this.externalClassLoader.loadClass(name);
/* 204 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     if (c == null) {
/*     */ 
/*     */       
/* 214 */       try { ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */         
/* 216 */         if (contextClassLoader != null)
/* 217 */           c = Class.forName(name, true, contextClassLoader);  }
/* 218 */       catch (ClassNotFoundException e) {  }
/* 219 */       catch (SecurityException e) {}
/*     */     }
/*     */ 
/*     */     
/* 223 */     if (c == null) {
/*     */       try {
/* 225 */         c = plainClassForName(name);
/* 226 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */     
/* 229 */     if (c == null) {
/* 230 */       c = loadSourceClass(name);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 235 */     cacheClassInfo(name, c);
/*     */     
/* 237 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getResource(String path) {
/* 246 */     URL url = null;
/* 247 */     if (this.baseLoader != null)
/*     */     {
/* 249 */       url = this.baseLoader.getResource(path.substring(1)); } 
/* 250 */     if (url == null)
/* 251 */       url = super.getResource(path); 
/* 252 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getResourceAsStream(String path) {
/* 261 */     InputStream in = null;
/* 262 */     if (this.baseLoader != null)
/*     */     {
/*     */       
/* 265 */       in = this.baseLoader.getResourceAsStream(path.substring(1));
/*     */     }
/* 267 */     if (in == null)
/*     */     {
/* 269 */       in = super.getResourceAsStream(path);
/*     */     }
/* 271 */     return in;
/*     */   }
/*     */   
/*     */   ClassLoader getLoaderForClass(String name) {
/* 275 */     return (ClassLoader)this.loaderMap.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassPath(URL path) throws IOException {
/* 285 */     if (this.baseLoader == null) {
/* 286 */       setClassPath(new URL[] { path });
/*     */     } else {
/*     */       
/* 289 */       this.baseLoader.addURL(path);
/* 290 */       this.baseClassPath.add(path);
/* 291 */       classLoaderChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 301 */     this.baseClassPath = new BshClassPath("baseClassPath");
/* 302 */     this.baseLoader = null;
/* 303 */     this.loaderMap = new HashMap<Object, Object>();
/* 304 */     classLoaderChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassPath(URL[] cp) {
/* 312 */     this.baseClassPath.setPath(cp);
/* 313 */     initBaseLoader();
/* 314 */     this.loaderMap = new HashMap<Object, Object>();
/* 315 */     classLoaderChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reloadAllClasses() throws ClassPathException {
/* 326 */     BshClassPath bcp = new BshClassPath("temp");
/* 327 */     bcp.addComponent(this.baseClassPath);
/* 328 */     bcp.addComponent(BshClassPath.getUserClassPath());
/* 329 */     setClassPath(bcp.getPathComponents());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initBaseLoader() {
/* 336 */     this.baseLoader = new BshClassLoader(this, this.baseClassPath);
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
/*     */   public void reloadClasses(String[] classNames) throws ClassPathException {
/* 352 */     if (this.baseLoader == null) {
/* 353 */       initBaseLoader();
/*     */     }
/* 355 */     DiscreteFilesClassLoader.ClassSourceMap map = new DiscreteFilesClassLoader.ClassSourceMap();
/*     */ 
/*     */     
/* 358 */     for (int i = 0; i < classNames.length; i++) {
/* 359 */       String name = classNames[i];
/*     */ 
/*     */       
/* 362 */       BshClassPath.ClassSource classSource = this.baseClassPath.getClassSource(name);
/*     */ 
/*     */       
/* 365 */       if (classSource == null) {
/* 366 */         BshClassPath.getUserClassPath().insureInitialized();
/* 367 */         classSource = BshClassPath.getUserClassPath().getClassSource(name);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 374 */       if (classSource == null) {
/* 375 */         throw new ClassPathException("Nothing known about class: " + name);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 381 */       if (classSource instanceof BshClassPath.JarClassSource) {
/* 382 */         throw new ClassPathException("Cannot reload class: " + name + " from source: " + classSource);
/*     */       }
/*     */       
/* 385 */       map.put(name, classSource);
/*     */     } 
/*     */ 
/*     */     
/* 389 */     ClassLoader cl = new DiscreteFilesClassLoader(this, map);
/*     */ 
/*     */     
/* 392 */     Iterator<K> it = map.keySet().iterator();
/* 393 */     while (it.hasNext()) {
/* 394 */       this.loaderMap.put((String)it.next(), cl);
/*     */     }
/* 396 */     classLoaderChanged();
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
/*     */   public void reloadPackage(String pack) throws ClassPathException {
/* 408 */     Collection classes = this.baseClassPath.getClassesForPackage(pack);
/*     */ 
/*     */     
/* 411 */     if (classes == null) {
/* 412 */       classes = BshClassPath.getUserClassPath().getClassesForPackage(pack);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 417 */     if (classes == null) {
/* 418 */       throw new ClassPathException("No classes found for package: " + pack);
/*     */     }
/* 420 */     reloadClasses((String[])classes.toArray((Object[])new String[0]));
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
/*     */   
/*     */   public BshClassPath getClassPath() throws ClassPathException {
/* 439 */     if (this.fullClassPath != null) {
/* 440 */       return this.fullClassPath;
/*     */     }
/* 442 */     this.fullClassPath = new BshClassPath("BeanShell Full Class Path");
/* 443 */     this.fullClassPath.addComponent(BshClassPath.getUserClassPath());
/*     */     try {
/* 445 */       this.fullClassPath.addComponent(BshClassPath.getBootClassPath());
/* 446 */     } catch (ClassPathException e) {
/* 447 */       System.err.println("Warning: can't get boot class path");
/*     */     } 
/* 449 */     this.fullClassPath.addComponent(this.baseClassPath);
/*     */     
/* 451 */     return this.fullClassPath;
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
/*     */   public void doSuperImport() throws UtilEvalError {
/*     */     try {
/* 464 */       getClassPath().insureInitialized();
/*     */       
/* 466 */       getClassNameByUnqName("");
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 471 */     catch (ClassPathException e) {
/* 472 */       throw new UtilEvalError("Error importing classpath " + e);
/*     */     } 
/*     */     
/* 475 */     this.superImport = true;
/*     */   }
/*     */   protected boolean hasSuperImport() {
/* 478 */     return this.superImport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassNameByUnqName(String name) throws ClassPathException {
/* 487 */     return getClassPath().getClassNameByUnqName(name);
/*     */   }
/*     */   
/*     */   public void addListener(BshClassManager.Listener l) {
/* 491 */     this.listeners.addElement(new WeakReference<BshClassManager.Listener>(l, this.refQueue));
/*     */     
/*     */     Reference deadref;
/*     */     
/* 495 */     while ((deadref = this.refQueue.poll()) != null) {
/* 496 */       boolean ok = this.listeners.removeElement(deadref);
/* 497 */       if (ok) {
/*     */         continue;
/*     */       }
/* 500 */       if (Interpreter.DEBUG) Interpreter.debug("tried to remove non-existent weak ref: " + deadref);
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeListener(BshClassManager.Listener l) {
/* 507 */     throw new Error("unimplemented");
/*     */   }
/*     */   
/*     */   public ClassLoader getBaseLoader() {
/* 511 */     return this.baseLoader;
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
/*     */   
/*     */   public Class defineClass(String name, byte[] code) {
/* 530 */     this.baseClassPath.setClassSource(name, new BshClassPath.GeneratedClassSource(code));
/*     */     try {
/* 532 */       reloadClasses(new String[] { name });
/* 533 */     } catch (ClassPathException e) {
/* 534 */       throw new InterpreterError("defineClass: " + e);
/*     */     } 
/* 536 */     return classForName(name);
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
/*     */   protected void classLoaderChanged() {
/* 549 */     clearCaches();
/*     */     
/* 551 */     Vector<WeakReference<BshClassManager.Listener>> toRemove = new Vector();
/* 552 */     for (Enumeration<WeakReference> enumeration = this.listeners.elements(); enumeration.hasMoreElements(); ) {
/*     */       
/* 554 */       WeakReference<BshClassManager.Listener> wr = enumeration.nextElement();
/* 555 */       BshClassManager.Listener l = wr.get();
/* 556 */       if (l == null) {
/* 557 */         toRemove.add(wr); continue;
/*     */       } 
/* 559 */       l.classLoaderChanged();
/*     */     } 
/* 561 */     for (Enumeration<WeakReference<BshClassManager.Listener>> e = toRemove.elements(); e.hasMoreElements();) {
/* 562 */       this.listeners.removeElement(e.nextElement());
/*     */     }
/*     */   }
/*     */   
/*     */   public void dump(PrintWriter i) {
/* 567 */     i.println("Bsh Class Manager Dump: ");
/* 568 */     i.println("----------------------- ");
/* 569 */     i.println("baseLoader = " + this.baseLoader);
/* 570 */     i.println("loaderMap= " + this.loaderMap);
/* 571 */     i.println("----------------------- ");
/* 572 */     i.println("baseClassPath = " + this.baseClassPath);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/classpath/ClassManagerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */