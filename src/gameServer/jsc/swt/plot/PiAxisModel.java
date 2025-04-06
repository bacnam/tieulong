/*    */ package jsc.swt.plot;
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
/*    */ public class PiAxisModel
/*    */   implements AxisModel
/*    */ {
/*    */   String label;
/*    */   int minNumerator;
/*    */   int maxNumerator;
/*    */   int denominator;
/*    */   
/*    */   public PiAxisModel(String paramString, int paramInt1, int paramInt2, int paramInt3) {
/* 39 */     if (paramInt3 < 1)
/* 40 */       throw new IllegalArgumentException("Denominator must be > 0."); 
/* 41 */     if (paramInt1 >= paramInt2)
/* 42 */       throw new IllegalArgumentException("minNumerator must be < maxNumerator."); 
/* 43 */     this.label = paramString;
/* 44 */     this.minNumerator = paramInt1;
/* 45 */     this.maxNumerator = paramInt2;
/* 46 */     this.denominator = paramInt3;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PiAxisModel() {
/* 52 */     this("", -1, 1, 1);
/*    */   }
/*    */   public Object clone() {
/* 55 */     return copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public PiAxisModel copy() {
/* 60 */     return new PiAxisModel(this.label, this.minNumerator, this.maxNumerator, this.denominator);
/*    */   }
/*    */   
/*    */   public double getLength() {
/* 64 */     return (this.maxNumerator - this.minNumerator) * Math.PI / this.denominator;
/* 65 */   } public double getMin() { return this.minNumerator * Math.PI / this.denominator; }
/* 66 */   public double getMax() { return this.maxNumerator * Math.PI / this.denominator; }
/* 67 */   public int getTickCount() { return 1 + this.maxNumerator - this.minNumerator; }
/* 68 */   public double getFirstTickValue() { return getMin(); } public double getLastTickValue() {
/* 69 */     return getMax();
/*    */   }
/*    */   
/*    */   public String getTickLabel(int paramInt) {
/* 73 */     int i = this.minNumerator + paramInt;
/* 74 */     if (i == 0) return "0"; 
/* 75 */     if (this.denominator == 1) return (i == 1) ? "π" : (i + "π");
/*    */     
/* 77 */     if (i % this.denominator == 0) {
/*    */       
/* 79 */       i /= this.denominator;
/* 80 */       if (i == 1) return "π"; 
/* 81 */       if (i == -1) return "-π"; 
/* 82 */       return i + "π";
/*    */     } 
/*    */     
/* 85 */     if (i == 1) return "π/" + this.denominator; 
/* 86 */     if (i == -1) return "-π/" + this.denominator; 
/* 87 */     return i + "π" + "/" + this.denominator;
/*    */   }
/*    */   public double getTickValue(int paramInt) {
/* 90 */     return (this.minNumerator + paramInt) * Math.PI / this.denominator;
/*    */   }
/* 92 */   public String getLabel() { return this.label; } public void setLabel(String paramString) {
/* 93 */     this.label = paramString;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot/PiAxisModel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */