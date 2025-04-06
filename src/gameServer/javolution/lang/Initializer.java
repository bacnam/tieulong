/*     */ package javolution.lang;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Vector;
/*     */ import javolution.context.LogContext;
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
/*     */ public class Initializer
/*     */ {
/*  56 */   public static final Configurable<Boolean> SHOW_INITIALIZED = new Configurable<Boolean>()
/*     */     {
/*     */       protected Boolean getDefault() {
/*  59 */         return Boolean.valueOf(false);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */   
/*     */   public Initializer(ClassLoader classLoader) {
/*  70 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?>[] loadedClasses() {
/*  79 */     Class<?> cls = this.classLoader.getClass();
/*  80 */     while (cls != ClassLoader.class) {
/*  81 */       cls = cls.getSuperclass();
/*     */     }
/*     */     try {
/*  84 */       Field fldClasses = cls.getDeclaredField("classes");
/*     */       
/*  86 */       fldClasses.setAccessible(true);
/*  87 */       Vector<Class<?>> list = (Vector<Class<?>>)fldClasses.get(this.classLoader);
/*     */       
/*  89 */       Class<?>[] classes = new Class[list.size()];
/*  90 */       for (int i = 0; i < classes.length; i++) {
/*  91 */         classes[i] = list.get(i);
/*     */       }
/*  93 */       return classes;
/*  94 */     } catch (Throwable e) {
/*  95 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadClass(Class<?> cls) {
/*     */     try {
/* 105 */       this.classLoader.loadClass(cls.getName());
/* 106 */     } catch (ClassNotFoundException e) {
/* 107 */       LogContext.debug(new Object[] { "Class " + cls + " not found." });
/*     */     } 
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
/*     */   public boolean initializeLoadedClasses() {
/* 120 */     boolean isInitializationSuccessful = true;
/* 121 */     int nbrClassesInitialized = 0;
/*     */     while (true) {
/* 123 */       Class<?>[] classes = loadedClasses();
/* 124 */       if (classes == null) {
/* 125 */         LogContext.debug(new Object[] { "Automatic class initialization not supported." });
/*     */         
/* 127 */         return false;
/*     */       } 
/* 129 */       if (nbrClassesInitialized >= classes.length)
/*     */         break; 
/* 131 */       for (int i = nbrClassesInitialized; i < classes.length; i++) {
/* 132 */         Class<?> cls = classes[i];
/*     */         try {
/* 134 */           if (((Boolean)SHOW_INITIALIZED.get()).booleanValue())
/* 135 */             LogContext.debug(new Object[] { "Initialize ", cls.getName() }); 
/* 136 */           Class.forName(cls.getName(), true, this.classLoader);
/* 137 */         } catch (ClassNotFoundException ex) {
/* 138 */           isInitializationSuccessful = false;
/* 139 */           LogContext.error(new Object[] { ex });
/*     */         } 
/*     */       } 
/* 142 */       nbrClassesInitialized = classes.length;
/*     */     } 
/* 144 */     LogContext.debug(new Object[] { "Initialization of ", Integer.valueOf(nbrClassesInitialized), " classes loaded by ", this.classLoader });
/*     */     
/* 146 */     return isInitializationSuccessful;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/lang/Initializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */