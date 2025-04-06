/*     */ package bsh;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
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
/*     */ public class CollectionManager
/*     */ {
/*     */   private static CollectionManager manager;
/*     */   
/*     */   public static synchronized CollectionManager getCollectionManager() {
/*  56 */     if (manager == null && Capabilities.classExists("java.util.Collection")) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/*  61 */         Class<?> clas = Class.forName("bsh.collection.CollectionManagerImpl");
/*  62 */         manager = (CollectionManager)clas.newInstance();
/*  63 */       } catch (Exception e) {
/*  64 */         Interpreter.debug("unable to load CollectionManagerImpl: " + e);
/*     */       } 
/*     */     }
/*     */     
/*  68 */     if (manager == null) {
/*  69 */       manager = new CollectionManager();
/*     */     }
/*  71 */     return manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBshIterable(Object obj) {
/*     */     try {
/*  80 */       getBshIterator(obj);
/*  81 */       return true;
/*  82 */     } catch (IllegalArgumentException e) {
/*  83 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BshIterator getBshIterator(Object obj) throws IllegalArgumentException {
/*  90 */     return new BasicBshIterator(obj);
/*     */   }
/*     */   
/*     */   public boolean isMap(Object obj) {
/*  94 */     return obj instanceof Hashtable;
/*     */   }
/*     */   
/*     */   public Object getFromMap(Object map, Object key) {
/*  98 */     return ((Hashtable)map).get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object putInMap(Object map, Object key, Object value) {
/* 103 */     return ((Hashtable<Object, Object>)map).put(key, value);
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
/*     */   public static class BasicBshIterator
/*     */     implements BshIterator
/*     */   {
/*     */     Enumeration enumeration;
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
/*     */     public BasicBshIterator(Object iterateOverMe) {
/* 130 */       this.enumeration = createEnumeration(iterateOverMe);
/*     */     }
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
/*     */     protected Enumeration createEnumeration(Object iterateOverMe) {
/* 148 */       if (iterateOverMe == null) {
/* 149 */         throw new NullPointerException("Object arguments passed to the BasicBshIterator constructor cannot be null.");
/*     */       }
/*     */       
/* 152 */       if (iterateOverMe instanceof Enumeration) {
/* 153 */         return (Enumeration)iterateOverMe;
/*     */       }
/* 155 */       if (iterateOverMe instanceof Vector) {
/* 156 */         return ((Vector)iterateOverMe).elements();
/*     */       }
/* 158 */       if (iterateOverMe.getClass().isArray()) {
/* 159 */         final Object array = iterateOverMe;
/* 160 */         return new Enumeration() {
/* 161 */             int index = 0; int length = Array.getLength(array);
/*     */             public Object nextElement() {
/* 163 */               return Array.get(array, this.index++);
/*     */             } public boolean hasMoreElements() {
/* 165 */               return (this.index < this.length);
/*     */             }
/*     */           };
/*     */       } 
/* 169 */       if (iterateOverMe instanceof String) {
/* 170 */         return createEnumeration(((String)iterateOverMe).toCharArray());
/*     */       }
/* 172 */       if (iterateOverMe instanceof StringBuffer) {
/* 173 */         return createEnumeration(iterateOverMe.toString().toCharArray());
/*     */       }
/*     */       
/* 176 */       throw new IllegalArgumentException("Cannot enumerate object of type " + iterateOverMe.getClass());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object next() {
/* 186 */       return this.enumeration.nextElement();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 196 */       return this.enumeration.hasMoreElements();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/CollectionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */