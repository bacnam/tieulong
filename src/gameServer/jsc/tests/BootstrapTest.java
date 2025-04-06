/*     */ package jsc.tests;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import jsc.distributions.Tail;
/*     */ import jsc.event.StatisticEvent;
/*     */ import jsc.event.StatisticListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BootstrapTest
/*     */   implements SignificanceTest
/*     */ {
/*  34 */   private long critCount = 0L;
/*  35 */   private long totalRepCount = 0L;
/*     */   private final double tObs;
/*  37 */   private double SP = 0.0D;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Tail tail;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Random rand;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set statisticListeners;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BootstrapTest(double paramDouble, Tail paramTail, StatisticListener paramStatisticListener) {
/*  55 */     this.tail = paramTail;
/*  56 */     this.tObs = paramDouble;
/*  57 */     this.rand = new Random();
/*     */ 
/*     */     
/*  60 */     this.statisticListeners = new HashSet(1);
/*  61 */     if (paramStatisticListener != null) addStatisticListener(paramStatisticListener);
/*     */   
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
/*     */   protected abstract double bootstrapSample();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double calculateSP(int paramInt) {
/*  85 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/*  87 */       double d = bootstrapSample();
/*     */       
/*  89 */       if (!this.statisticListeners.isEmpty()) {
/*  90 */         fireStatisticEvent(new StatisticEvent(this, d));
/*     */       }
/*  92 */       if (this.tail == Tail.UPPER)
/*  93 */       { if (d >= this.tObs) this.critCount++;  }
/*  94 */       else if (this.tail == Tail.LOWER)
/*  95 */       { if (d <= this.tObs) this.critCount++;
/*     */          }
/*  97 */       else if (Math.abs(d) >= Math.abs(this.tObs)) { this.critCount++; }
/*     */       
/*  99 */       this.totalRepCount++;
/*     */     } 
/*     */     
/* 102 */     this.SP = this.critCount / this.totalRepCount;
/* 103 */     return this.SP;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 134 */     return this.critCount / this.totalRepCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 141 */     return this.tObs;
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
/*     */   public long getTotalRepCount() {
/* 155 */     return this.totalRepCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 164 */     this.rand.setSeed(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStatisticListener(StatisticListener paramStatisticListener) {
/* 171 */     this.statisticListeners.add(paramStatisticListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeStatisticListener(StatisticListener paramStatisticListener) {
/* 178 */     this.statisticListeners.remove(paramStatisticListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireStatisticEvent(StatisticEvent paramStatisticEvent) {
/* 189 */     Iterator iterator = this.statisticListeners.iterator();
/* 190 */     while (iterator.hasNext()) {
/*     */ 
/*     */       
/* 193 */       StatisticListener statisticListener = iterator.next();
/* 194 */       statisticListener.statisticCreated(paramStatisticEvent);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/tests/BootstrapTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */