/*    */ package jsc.swt.plot2d;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import jsc.swt.plot.AxisModel;
/*    */ import jsc.swt.plot.LinearAxisModel;
/*    */ import jsc.swt.virtualgraphics.VPoint;
/*    */ import jsc.util.Scale;
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
/*    */ public class LabelledXaxisPlot
/*    */   extends AxesPlot
/*    */ {
/*    */   int labelCount;
/*    */   
/*    */   public LabelledXaxisPlot(String paramString1, String paramString2, String[] paramArrayOfString, Color paramColor, Font paramFont, AxisModel paramAxisModel) {
/* 38 */     super((AxisModel)new LinearAxisModel(), paramAxisModel, paramString1);
/*    */ 
/*    */     
/* 41 */     setPaintXaxis(false);
/*    */ 
/*    */     
/* 44 */     this.labelCount = paramArrayOfString.length;
/*    */ 
/*    */ 
/*    */     
/* 48 */     double d = paramAxisModel.getTickValue(0);
/* 49 */     for (byte b = 0; b < this.labelCount; b++) {
/*    */       
/* 51 */       PlotText plotText = new PlotText(paramArrayOfString[b], new VPoint((b + 1), d), 2, 3, paramColor, paramFont);
/* 52 */       addObject(plotText);
/*    */     } 
/*    */     
/* 55 */     rescaleHorizontal((AxisModel)new LinearAxisModel(paramString2, new Scale(0.0D, (this.labelCount + 1), this.labelCount + 2, false), ""));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLabelCount() {
/* 63 */     return this.labelCount;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/LabelledXaxisPlot.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */