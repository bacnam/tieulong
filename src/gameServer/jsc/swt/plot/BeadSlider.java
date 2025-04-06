/*     */ package jsc.swt.plot;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import jsc.swt.control.RealSlider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeadSlider
/*     */   extends RealSlider
/*     */ {
/*     */   int index;
/*     */   
/*     */   public BeadSlider(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, Color paramColor) {
/*  54 */     super(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble4, paramDouble5, 0.0D, "");
/*  55 */     setOrientation(1);
/*  56 */     setOpaque(false);
/*  57 */     setPaintTrack(false);
/*     */     
/*  59 */     this.index = paramInt;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     setBeadColour(paramColor);
/*  65 */     setFocusColour(paramColor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/*  76 */     return this.index;
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
/*     */   public void rescale(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/*  92 */     rescale(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble4, paramDouble5, 0.0D, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeadColour(Color paramColor) {
/* 101 */     BeadSliderUI beadSliderUI = (BeadSliderUI)getUI();
/* 102 */     beadSliderUI.setBeadColour(paramColor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocusColour(Color paramColor) {
/* 112 */     BeadSliderUI beadSliderUI = (BeadSliderUI)getUI();
/* 113 */     beadSliderUI.setFocusColour(paramColor);
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
/*     */   public void updateUI() {
/* 134 */     BeadSliderUI beadSliderUI = new BeadSliderUI();
/* 135 */     setUI(beadSliderUI);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot/BeadSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */