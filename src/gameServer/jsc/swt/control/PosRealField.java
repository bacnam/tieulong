/*     */ package jsc.swt.control;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Toolkit;
/*     */ import java.text.ParseException;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.PlainDocument;
/*     */ import jsc.swt.text.MaxWidthFormat;
/*     */ import jsc.swt.text.RealFormat;
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
/*     */ public class PosRealField
/*     */   extends JTextField
/*     */ {
/*     */   private Toolkit toolkit;
/*     */   private RealFormat realFormatter;
/*     */   private char decimalSeparator;
/*     */   JTextField field;
/*     */   
/*     */   public PosRealField(double paramDouble, int paramInt, RealFormat paramRealFormat) {
/*  49 */     super(paramInt);
/*  50 */     this.toolkit = Toolkit.getDefaultToolkit();
/*     */ 
/*     */     
/*  53 */     this.realFormatter = paramRealFormat;
/*  54 */     this.field = this;
/*  55 */     setBackground(Color.white);
/*  56 */     this.decimalSeparator = paramRealFormat.getDecimalSeparator();
/*  57 */     setValue(paramDouble);
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
/*     */   public PosRealField(double paramDouble, int paramInt) {
/*  70 */     this(paramDouble, paramInt, (RealFormat)new MaxWidthFormat(7));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue() {
/*     */     try {
/*  81 */       return this.realFormatter.parse(getText()).doubleValue();
/*     */     
/*     */     }
/*     */     catch (ParseException parseException) {
/*     */ 
/*     */       
/*  87 */       this.toolkit.beep();
/*     */ 
/*     */       
/*  90 */       return 0.0D;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRealFormat(RealFormat paramRealFormat) {
/* 129 */     this.realFormatter = paramRealFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(double paramDouble) {
/* 137 */     setText(this.realFormatter.format(paramDouble));
/*     */   }
/*     */   protected Document createDefaultModel() {
/* 140 */     return new RealNumberDocument(this);
/*     */   } protected class RealNumberDocument extends PlainDocument { protected RealNumberDocument(PosRealField this$0) {
/* 142 */       this.this$0 = this$0;
/*     */     }
/*     */     private final PosRealField this$0;
/*     */     public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
/* 146 */       char[] arrayOfChar1 = param1String.toCharArray();
/* 147 */       char[] arrayOfChar2 = new char[arrayOfChar1.length];
/* 148 */       byte b1 = 0;
/*     */       
/* 150 */       for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {
/*     */         
/* 152 */         char c = Character.toUpperCase(arrayOfChar1[b2]);
/* 153 */         if (Character.isDigit(c)) {
/* 154 */           arrayOfChar2[b1++] = c;
/* 155 */         } else if (c == this.this$0.decimalSeparator) {
/*     */           
/* 157 */           String str = this.this$0.field.getText();
/* 158 */           if (str.indexOf(this.this$0.decimalSeparator) < 0) {
/* 159 */             arrayOfChar2[b1++] = c;
/*     */           }
/*     */         }
/* 162 */         else if (c == 'E') {
/*     */           
/* 164 */           String str = this.this$0.field.getText().toUpperCase();
/* 165 */           if (str.indexOf('E') < 0) {
/* 166 */             arrayOfChar2[b1++] = c;
/*     */           }
/*     */         }
/* 169 */         else if (c == '-' && param1Int > 0) {
/*     */           
/* 171 */           String str = this.this$0.field.getText().toUpperCase();
/* 172 */           if (str.indexOf('-') < 0 && str.indexOf('E') == param1Int - 1) {
/* 173 */             arrayOfChar2[b1++] = c;
/*     */           }
/*     */         } else {
/*     */           
/* 177 */           this.this$0.toolkit.beep();
/*     */         } 
/*     */       } 
/*     */       
/* 181 */       super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/PosRealField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */