/*    */ package jsc.independentsamples;
/*    */ 
/*    */ import jsc.distributions.Tail;
/*    */ import jsc.event.StatisticListener;
/*    */ import jsc.tests.BootstrapTest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TwoSampleBootstrapTest
/*    */   extends BootstrapTest
/*    */ {
/*    */   protected final int N;
/*    */   protected final int nA;
/*    */   protected final int nB;
/*    */   protected TwoSampleStatistic t;
/*    */   
/*    */   public TwoSampleBootstrapTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail) {
/* 47 */     this(paramTwoSampleStatistic, paramTail, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TwoSampleBootstrapTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail, StatisticListener paramStatisticListener) {
/* 58 */     super(paramTwoSampleStatistic.getStatistic(), paramTail, paramStatisticListener);
/*    */ 
/*    */     
/* 61 */     this.nA = paramTwoSampleStatistic.sizeA();
/* 62 */     this.nB = paramTwoSampleStatistic.sizeB();
/* 63 */     this.N = this.nA + this.nB;
/* 64 */     this.t = paramTwoSampleStatistic;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getN() {
/* 72 */     return this.N;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/TwoSampleBootstrapTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */