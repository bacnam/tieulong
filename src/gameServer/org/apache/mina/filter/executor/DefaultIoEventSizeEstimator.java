/*     */ package org.apache.mina.filter.executor;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.IoEvent;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultIoEventSizeEstimator
/*     */   implements IoEventSizeEstimator
/*     */ {
/*  48 */   private final ConcurrentMap<Class<?>, Integer> class2size = new ConcurrentHashMap<Class<?>, Integer>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIoEventSizeEstimator() {
/*  55 */     this.class2size.put(boolean.class, Integer.valueOf(4));
/*  56 */     this.class2size.put(byte.class, Integer.valueOf(1));
/*  57 */     this.class2size.put(char.class, Integer.valueOf(2));
/*  58 */     this.class2size.put(int.class, Integer.valueOf(4));
/*  59 */     this.class2size.put(short.class, Integer.valueOf(2));
/*  60 */     this.class2size.put(long.class, Integer.valueOf(8));
/*  61 */     this.class2size.put(float.class, Integer.valueOf(4));
/*  62 */     this.class2size.put(double.class, Integer.valueOf(8));
/*  63 */     this.class2size.put(void.class, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int estimateSize(IoEvent event) {
/*  70 */     return estimateSize(event) + estimateSize(event.getParameter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int estimateSize(Object message) {
/*  79 */     if (message == null) {
/*  80 */       return 8;
/*     */     }
/*     */     
/*  83 */     int answer = 8 + estimateSize(message.getClass(), null);
/*     */     
/*  85 */     if (message instanceof IoBuffer) {
/*  86 */       answer += ((IoBuffer)message).remaining();
/*  87 */     } else if (message instanceof WriteRequest) {
/*  88 */       answer += estimateSize(((WriteRequest)message).getMessage());
/*  89 */     } else if (message instanceof CharSequence) {
/*  90 */       answer += ((CharSequence)message).length() << 1;
/*  91 */     } else if (message instanceof Iterable) {
/*  92 */       for (Object m : message) {
/*  93 */         answer += estimateSize(m);
/*     */       }
/*     */     } 
/*     */     
/*  97 */     return align(answer);
/*     */   }
/*     */   
/*     */   private int estimateSize(Class<?> clazz, Set<Class<?>> visitedClasses) {
/* 101 */     Integer objectSize = this.class2size.get(clazz);
/*     */     
/* 103 */     if (objectSize != null) {
/* 104 */       return objectSize.intValue();
/*     */     }
/*     */     
/* 107 */     if (visitedClasses != null) {
/* 108 */       if (visitedClasses.contains(clazz)) {
/* 109 */         return 0;
/*     */       }
/*     */     } else {
/* 112 */       visitedClasses = new HashSet<Class<?>>();
/*     */     } 
/*     */     
/* 115 */     visitedClasses.add(clazz);
/*     */     
/* 117 */     int answer = 8;
/*     */     
/* 119 */     for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
/* 120 */       Field[] fields = c.getDeclaredFields();
/*     */       
/* 122 */       for (Field f : fields) {
/* 123 */         if ((f.getModifiers() & 0x8) == 0)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 128 */           answer += estimateSize(f.getType(), visitedClasses);
/*     */         }
/*     */       } 
/*     */     } 
/* 132 */     visitedClasses.remove(clazz);
/*     */ 
/*     */     
/* 135 */     answer = align(answer);
/*     */ 
/*     */     
/* 138 */     Integer tmpAnswer = this.class2size.putIfAbsent(clazz, Integer.valueOf(answer));
/*     */     
/* 140 */     if (tmpAnswer != null) {
/* 141 */       answer = tmpAnswer.intValue();
/*     */     }
/*     */     
/* 144 */     return answer;
/*     */   }
/*     */   
/*     */   private static int align(int size) {
/* 148 */     if (size % 8 != 0) {
/* 149 */       size /= 8;
/* 150 */       size++;
/* 151 */       size *= 8;
/*     */     } 
/*     */     
/* 154 */     return size;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/executor/DefaultIoEventSizeEstimator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */