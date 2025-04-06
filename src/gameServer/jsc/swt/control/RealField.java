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
/*     */ public class RealField
/*     */   extends JTextField
/*     */ {
/*     */   private Toolkit toolkit;
/*     */   private RealFormat realFormatter;
/*     */   private char decimalSeparator;
/*     */   JTextField field;
/*     */   
/*     */   public RealField(double paramDouble, int paramInt, RealFormat paramRealFormat) {
/*  47 */     super(paramInt);
/*  48 */     this.toolkit = Toolkit.getDefaultToolkit();
/*     */ 
/*     */     
/*  51 */     this.realFormatter = paramRealFormat;
/*  52 */     this.field = this;
/*  53 */     setBackground(Color.white);
/*     */     
/*  55 */     this.decimalSeparator = paramRealFormat.getDecimalSeparator();
/*  56 */     setValue(paramDouble);
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
/*     */   public RealField(double paramDouble, int paramInt) {
/*  69 */     this(paramDouble, paramInt, (RealFormat)new MaxWidthFormat(7));
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
/*  80 */       return this.realFormatter.parse(getText()).doubleValue();
/*     */     }
/*     */     catch (ParseException parseException) {
/*     */       
/*  84 */       this.toolkit.beep();
/*     */       
/*  86 */       return 0.0D;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRealFormat(RealFormat paramRealFormat) {
/*  97 */     this.realFormatter = paramRealFormat;
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
/*     */   public void setValue(double paramDouble) {
/* 133 */     setText(this.realFormatter.format(paramDouble));
/*     */   }
/*     */   protected Document createDefaultModel() {
/* 136 */     return new RealNumberDocument(this);
/*     */   } protected class RealNumberDocument extends PlainDocument { protected RealNumberDocument(RealField this$0) {
/* 138 */       this.this$0 = this$0;
/*     */     }
/*     */     private final RealField this$0;
/*     */     public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
/* 142 */       char[] arrayOfChar1 = param1String.toCharArray();
/* 143 */       char[] arrayOfChar2 = new char[arrayOfChar1.length];
/* 144 */       byte b1 = 0;
/*     */       
/* 146 */       for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {
/*     */         
/* 148 */         char c = Character.toUpperCase(arrayOfChar1[b2]);
/* 149 */         if (Character.isDigit(c)) {
/* 150 */           arrayOfChar2[b1++] = c;
/* 151 */         } else if (c == this.this$0.decimalSeparator) {
/*     */           
/* 153 */           String str = this.this$0.field.getText();
/* 154 */           if (str.indexOf(this.this$0.decimalSeparator) < 0) {
/* 155 */             arrayOfChar2[b1++] = c;
/*     */           }
/*     */         }
/* 158 */         else if (c == 'E') {
/*     */           
/* 160 */           String str = this.this$0.field.getText().toUpperCase();
/* 161 */           if (str.indexOf('E') < 0) {
/* 162 */             arrayOfChar2[b1++] = c;
/*     */           }
/*     */         }
/* 165 */         else if (c == '-' && param1Int == 0 && getText(0, 1).charAt(0) != '-') {
/* 166 */           arrayOfChar2[b1++] = c;
/*     */         }
/* 168 */         else if (c == '-' && getText(param1Int - 1, 1).toUpperCase().charAt(0) == 'E' && getText(param1Int, 1).charAt(0) != '-') {
/* 169 */           arrayOfChar2[b1++] = c;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 176 */       super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/RealField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */