/*     */ package org.apache.mina.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IoUtil
/*     */ {
/*  41 */   private static final IoSession[] EMPTY_SESSIONS = new IoSession[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<WriteFuture> broadcast(Object message, Collection<IoSession> sessions) {
/*  49 */     List<WriteFuture> answer = new ArrayList<WriteFuture>(sessions.size());
/*  50 */     broadcast(message, sessions.iterator(), answer);
/*  51 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<WriteFuture> broadcast(Object message, Iterable<IoSession> sessions) {
/*  60 */     List<WriteFuture> answer = new ArrayList<WriteFuture>();
/*  61 */     broadcast(message, sessions.iterator(), answer);
/*  62 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<WriteFuture> broadcast(Object message, Iterator<IoSession> sessions) {
/*  71 */     List<WriteFuture> answer = new ArrayList<WriteFuture>();
/*  72 */     broadcast(message, sessions, answer);
/*  73 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<WriteFuture> broadcast(Object message, IoSession... sessions) {
/*  82 */     if (sessions == null) {
/*  83 */       sessions = EMPTY_SESSIONS;
/*     */     }
/*     */     
/*  86 */     List<WriteFuture> answer = new ArrayList<WriteFuture>(sessions.length);
/*  87 */     if (message instanceof IoBuffer) {
/*  88 */       for (IoSession s : sessions) {
/*  89 */         answer.add(s.write(((IoBuffer)message).duplicate()));
/*     */       }
/*     */     } else {
/*  92 */       for (IoSession s : sessions) {
/*  93 */         answer.add(s.write(message));
/*     */       }
/*     */     } 
/*  96 */     return answer;
/*     */   }
/*     */   
/*     */   private static void broadcast(Object message, Iterator<IoSession> sessions, Collection<WriteFuture> answer) {
/* 100 */     if (message instanceof IoBuffer) {
/* 101 */       while (sessions.hasNext()) {
/* 102 */         IoSession s = sessions.next();
/* 103 */         answer.add(s.write(((IoBuffer)message).duplicate()));
/*     */       } 
/*     */     } else {
/* 106 */       while (sessions.hasNext()) {
/* 107 */         IoSession s = sessions.next();
/* 108 */         answer.add(s.write(message));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void await(Iterable<? extends IoFuture> futures) throws InterruptedException {
/* 114 */     for (IoFuture f : futures) {
/* 115 */       f.await();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void awaitUninterruptably(Iterable<? extends IoFuture> futures) {
/* 120 */     for (IoFuture f : futures) {
/* 121 */       f.awaitUninterruptibly();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean await(Iterable<? extends IoFuture> futures, long timeout, TimeUnit unit) throws InterruptedException {
/* 127 */     return await(futures, unit.toMillis(timeout));
/*     */   }
/*     */   
/*     */   public static boolean await(Iterable<? extends IoFuture> futures, long timeoutMillis) throws InterruptedException {
/* 131 */     return await0(futures, timeoutMillis, true);
/*     */   }
/*     */   
/*     */   public static boolean awaitUninterruptibly(Iterable<? extends IoFuture> futures, long timeout, TimeUnit unit) {
/* 135 */     return awaitUninterruptibly(futures, unit.toMillis(timeout));
/*     */   }
/*     */   
/*     */   public static boolean awaitUninterruptibly(Iterable<? extends IoFuture> futures, long timeoutMillis) {
/*     */     try {
/* 140 */       return await0(futures, timeoutMillis, false);
/* 141 */     } catch (InterruptedException e) {
/* 142 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean await0(Iterable<? extends IoFuture> futures, long timeoutMillis, boolean interruptable) throws InterruptedException {
/* 148 */     long startTime = (timeoutMillis <= 0L) ? 0L : System.currentTimeMillis();
/* 149 */     long waitTime = timeoutMillis;
/*     */     
/* 151 */     boolean lastComplete = true;
/* 152 */     Iterator<? extends IoFuture> i = futures.iterator();
/* 153 */     while (i.hasNext()) {
/* 154 */       IoFuture f = i.next();
/*     */       do {
/* 156 */         if (interruptable) {
/* 157 */           lastComplete = f.await(waitTime);
/*     */         } else {
/* 159 */           lastComplete = f.awaitUninterruptibly(waitTime);
/*     */         } 
/*     */         
/* 162 */         waitTime = timeoutMillis - System.currentTimeMillis() - startTime;
/*     */         
/* 164 */         if (lastComplete || waitTime <= 0L) {
/*     */           break;
/*     */         }
/* 167 */       } while (!lastComplete);
/*     */       
/* 169 */       if (waitTime <= 0L) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 174 */     return (lastComplete && !i.hasNext());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/IoUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */