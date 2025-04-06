/*     */ package com.mchange.v2.async.test;
/*     */ 
/*     */ import com.mchange.v2.async.RoundRobinAsynchronousRunner;
/*     */ import com.mchange.v2.lang.ThreadUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterruptTaskThread
/*     */ {
/*  44 */   static Set interruptedThreads = Collections.synchronizedSet(new HashSet());
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/*  50 */       RoundRobinAsynchronousRunner roundRobinAsynchronousRunner = new RoundRobinAsynchronousRunner(5, false);
/*  51 */       (new Interrupter()).start();
/*  52 */       for (byte b = 0; b < 'Ϩ'; b++) {
/*     */         
/*     */         try {
/*  55 */           roundRobinAsynchronousRunner.postRunnable(new DumbTask());
/*  56 */         } catch (Exception exception) {
/*  57 */           exception.printStackTrace();
/*  58 */         }  Thread.sleep(50L);
/*     */       } 
/*  60 */       System.out.println("Interrupted Threads: " + interruptedThreads.size());
/*     */     }
/*  62 */     catch (Exception exception) {
/*  63 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static class Interrupter
/*     */     extends Thread
/*     */   {
/*     */     public void run() {
/*     */       try {
/*     */         while (true) {
/*  74 */           Thread[] arrayOfThread = new Thread[1000];
/*  75 */           ThreadUtils.enumerateAll(arrayOfThread);
/*  76 */           for (byte b = 0; arrayOfThread[b] != null; b++) {
/*     */             
/*  78 */             if (arrayOfThread[b].getName().indexOf("RunnableQueue.TaskThread") >= 0) {
/*     */               
/*  80 */               arrayOfThread[b].interrupt();
/*  81 */               System.out.println("INTERRUPTED!");
/*  82 */               InterruptTaskThread.interruptedThreads.add(arrayOfThread[b]);
/*     */               break;
/*     */             } 
/*     */           } 
/*  86 */           Thread.sleep(1000L);
/*     */         }
/*     */       
/*  89 */       } catch (Exception exception) {
/*  90 */         exception.printStackTrace();
/*     */         return;
/*     */       } 
/*     */     } }
/*     */   
/*     */   static class DumbTask implements Runnable {
/*  96 */     static int count = 0;
/*     */     static synchronized int number() {
/*  98 */       return count++;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 104 */         Thread.sleep(200L);
/* 105 */         System.out.println("DumbTask complete! " + number());
/*     */       }
/* 107 */       catch (Exception exception) {
/* 108 */         exception.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/async/test/InterruptTaskThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */