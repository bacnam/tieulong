/*     */ package jsc.swt.plot;
/*     */ 
/*     */ import jsc.swt.text.PatternFormat;
/*     */ import jsc.swt.text.RealFormat;
/*     */ import jsc.util.Scale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinearAxisModel
/*     */   implements AxisModel
/*     */ {
/*     */   String label;
/*     */   private Scale scale;
/*     */   private RealFormat tickLabelFormatter;
/*     */   
/*     */   public LinearAxisModel(String paramString1, Scale paramScale, String paramString2) {
/*  45 */     this(paramString1, paramScale, (RealFormat)new PatternFormat(paramString2));
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
/*     */   public LinearAxisModel(String paramString, Scale paramScale, RealFormat paramRealFormat) {
/*  58 */     this.label = paramString;
/*  59 */     this.scale = paramScale;
/*     */     
/*  61 */     this.tickLabelFormatter = paramRealFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinearAxisModel() {
/*  68 */     this("", new Scale(), "");
/*     */   }
/*     */   public Object clone() {
/*  71 */     return copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public LinearAxisModel copy() {
/*  76 */     return new LinearAxisModel(this.label, this.scale, this.tickLabelFormatter);
/*     */   }
/*     */   
/*     */   public double getLength() {
/*  80 */     return this.scale.getLength();
/*  81 */   } public double getMin() { return this.scale.getMin(); }
/*  82 */   public double getMax() { return this.scale.getMax(); }
/*  83 */   public int getTickCount() { return this.scale.getNumberOfTicks(); }
/*  84 */   public double getFirstTickValue() { return this.scale.getFirstTickValue(); } public double getLastTickValue() {
/*  85 */     return this.scale.getLastTickValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTickLabel(int paramInt) {
/*  93 */     double d = this.scale.getTickValue(paramInt);
/*     */     
/*  95 */     if (d == 0.0D) return "0";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     return this.tickLabelFormatter.format(d);
/*     */   }
/*     */   
/*     */   public double getTickValue(int paramInt) {
/* 106 */     return this.scale.getTickValue(paramInt);
/*     */   }
/* 108 */   public String getLabel() { return this.label; } public void setLabel(String paramString) {
/* 109 */     this.label = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStep() {
/* 116 */     return this.scale.getStep();
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
/*     */ 
/*     */   
/*     */   public void setTickLabelFormat(RealFormat paramRealFormat) {
/* 149 */     this.tickLabelFormatter = paramRealFormat;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot/LinearAxisModel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */