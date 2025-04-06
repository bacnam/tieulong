/*     */ package jsc.swt.control;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Toolkit;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Locale;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.PlainDocument;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PosIntegerField
/*     */   extends JTextField
/*     */ {
/*     */   private Toolkit toolkit;
/*     */   private NumberFormat integerFormatter;
/*     */   int maxDigits;
/*     */   int maxValue;
/*     */   
/*     */   public PosIntegerField(int paramInt1, int paramInt2) {
/*  43 */     this(paramInt1, paramInt2, 9, 999999999);
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
/*     */   public PosIntegerField(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  61 */     super(paramInt2);
/*  62 */     this.maxDigits = paramInt3;
/*  63 */     this.maxValue = paramInt4;
/*  64 */     this.toolkit = Toolkit.getDefaultToolkit();
/*  65 */     this.integerFormatter = NumberFormat.getNumberInstance(Locale.UK);
/*  66 */     this.integerFormatter.setParseIntegerOnly(true);
/*  67 */     this.integerFormatter.setGroupingUsed(false);
/*  68 */     setBackground(Color.white);
/*  69 */     setValue(paramInt1);
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
/*     */   public int getValue() {
/*  82 */     int i = 0;
/*     */     
/*     */     try {
/*  85 */       i = this.integerFormatter.parse(getText()).intValue();
/*     */     
/*     */     }
/*     */     catch (ParseException parseException) {
/*     */ 
/*     */       
/*  91 */       this.toolkit.beep();
/*     */     } 
/*  93 */     if (i > this.maxValue || i < 0) {
/*  94 */       i = this.maxValue; setText((new Integer(this.maxValue)).toString());
/*     */     } 
/*  96 */     return i;
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
/*     */   public void setValue(int paramInt) {
/* 108 */     setText(this.integerFormatter.format(paramInt));
/*     */   }
/*     */   
/*     */   protected Document createDefaultModel() {
/* 112 */     return new WholeNumberDocument(this);
/*     */   } protected class WholeNumberDocument extends PlainDocument { protected WholeNumberDocument(PosIntegerField this$0) {
/* 114 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     private final PosIntegerField this$0;
/*     */     
/*     */     public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
/* 121 */       if (getLength() + param1String.length() > this.this$0.maxDigits) { this.this$0.toolkit.beep(); return; }
/*     */       
/* 123 */       char[] arrayOfChar1 = param1String.toCharArray();
/* 124 */       char[] arrayOfChar2 = new char[arrayOfChar1.length];
/* 125 */       byte b1 = 0;
/*     */       
/* 127 */       for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {
/*     */         
/* 129 */         if (Character.isDigit(arrayOfChar1[b2])) {
/* 130 */           arrayOfChar2[b1++] = arrayOfChar1[b2];
/*     */         } else {
/*     */           
/* 133 */           this.this$0.toolkit.beep();
/*     */         } 
/*     */       } 
/*     */       
/* 137 */       super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/PosIntegerField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */