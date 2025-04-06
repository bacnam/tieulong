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
/*     */ 
/*     */ 
/*     */ public abstract class ResampleTest
/*     */   implements SignificanceTest
/*     */ {
/*  36 */   private long critCount = 0L;
/*  37 */   private long totalRepCount = 0L;
/*     */   private final double tObs;
/*  39 */   private double SP = 0.0D;
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
/*     */   public ResampleTest(double paramDouble, Tail paramTail, StatisticListener paramStatisticListener) {
/*  57 */     this.tail = paramTail;
/*  58 */     this.tObs = paramDouble;
/*  59 */     this.rand = new Random();
/*     */ 
/*     */     
/*  62 */     this.statisticListeners = new HashSet(1);
/*  63 */     if (paramStatisticListener != null) addStatisticListener(paramStatisticListener);
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
/*  87 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/*  89 */       double d = bootstrapSample();
/*     */       
/*  91 */       if (!this.statisticListeners.isEmpty()) {
/*  92 */         fireStatisticEvent(new StatisticEvent(this, d));
/*     */       }
/*  94 */       if (this.tail == Tail.UPPER)
/*  95 */       { if (d >= this.tObs) this.critCount++;  }
/*  96 */       else if (this.tail == Tail.LOWER)
/*  97 */       { if (d <= this.tObs) this.critCount++;
/*     */          }
/*  99 */       else if (Math.abs(d) >= Math.abs(this.tObs)) { this.critCount++; }
/*     */       
/* 101 */       this.totalRepCount++;
/*     */     } 
/*     */     
/* 104 */     this.SP = this.critCount / this.totalRepCount;
/* 105 */     return this.SP;
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
/* 136 */     return this.critCount / this.totalRepCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 143 */     return this.tObs;
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
/* 157 */     return this.totalRepCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 166 */     this.rand.setSeed(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStatisticListener(StatisticListener paramStatisticListener) {
/* 173 */     this.statisticListeners.add(paramStatisticListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeStatisticListener(StatisticListener paramStatisticListener) {
/* 180 */     this.statisticListeners.remove(paramStatisticListener);
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
/* 191 */     Iterator iterator = this.statisticListeners.iterator();
/* 192 */     while (iterator.hasNext()) {
/*     */ 
/*     */       
/* 195 */       StatisticListener statisticListener = iterator.next();
/* 196 */       statisticListener.statisticCreated(paramStatisticEvent);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/tests/ResampleTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */