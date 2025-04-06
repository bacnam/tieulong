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
/*     */ public class IntegerField
/*     */   extends JTextField
/*     */ {
/*     */   private Toolkit toolkit;
/*     */   private NumberFormat integerFormatter;
/*     */   
/*     */   public IntegerField(int paramInt1, int paramInt2) {
/*  39 */     super(paramInt2);
/*  40 */     this.toolkit = Toolkit.getDefaultToolkit();
/*  41 */     this.integerFormatter = NumberFormat.getNumberInstance(Locale.UK);
/*  42 */     this.integerFormatter.setParseIntegerOnly(true);
/*  43 */     this.integerFormatter.setGroupingUsed(false);
/*  44 */     setBackground(Color.white);
/*  45 */     setValue(paramInt1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*     */     try {
/*  57 */       return this.integerFormatter.parse(getText()).intValue();
/*     */     
/*     */     }
/*     */     catch (ParseException parseException) {
/*     */       
/*  62 */       this.toolkit.beep();
/*  63 */       return 0;
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
/*     */   protected boolean isNegative() throws BadLocationException {
/*  75 */     return (getText(0, 1).charAt(0) == '-');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int paramInt) {
/*  83 */     setText(this.integerFormatter.format(paramInt));
/*     */   }
/*     */   protected Document createDefaultModel() {
/*  86 */     return new WholeNumberDocument(this);
/*     */   } protected class WholeNumberDocument extends PlainDocument { protected WholeNumberDocument(IntegerField this$0) {
/*  88 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     private final IntegerField this$0;
/*     */     
/*     */     public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
/*  94 */       char[] arrayOfChar1 = param1String.toCharArray();
/*  95 */       char[] arrayOfChar2 = new char[arrayOfChar1.length];
/*     */       
/*  97 */       if (getLength() + param1String.length() > ((this.this$0.isNegative() || (param1Int == 0 && arrayOfChar1[0] == '-')) ? 10 : 9)) {
/*  98 */         this.this$0.toolkit.beep(); return;
/*     */       } 
/* 100 */       byte b1 = 0;
/* 101 */       for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {
/*     */         
/* 103 */         if (Character.isDigit(arrayOfChar1[b2])) {
/* 104 */           arrayOfChar2[b1++] = arrayOfChar1[b2];
/* 105 */         } else if (arrayOfChar1[b2] == '-' && param1Int == 0 && !this.this$0.isNegative()) {
/* 106 */           arrayOfChar2[b1++] = arrayOfChar1[b2];
/*     */         } else {
/*     */           
/* 109 */           this.this$0.toolkit.beep();
/*     */         } 
/*     */       } 
/*     */       
/* 113 */       super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/IntegerField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */