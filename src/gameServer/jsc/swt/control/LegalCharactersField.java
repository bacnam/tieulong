/*    */ package jsc.swt.control;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Toolkit;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.text.AttributeSet;
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.Document;
/*    */ import javax.swing.text.PlainDocument;
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
/*    */ public class LegalCharactersField
/*    */   extends JTextField
/*    */ {
/*    */   private Toolkit toolkit;
/*    */   private String legalCharacters;
/*    */   
/*    */   public LegalCharactersField(int paramInt, String paramString) {
/* 36 */     super(paramInt);
/* 37 */     this.legalCharacters = paramString;
/* 38 */     this.toolkit = Toolkit.getDefaultToolkit();
/* 39 */     setBackground(Color.white);
/*    */   }
/*    */   
/*    */   protected Document createDefaultModel() {
/* 43 */     return new LegalCharactersDocument(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLegalCharacters(String paramString) {
/* 51 */     this.legalCharacters = paramString;
/*    */   } protected class LegalCharactersDocument extends PlainDocument { protected LegalCharactersDocument(LegalCharactersField this$0) {
/* 53 */       this.this$0 = this$0;
/*    */     }
/*    */     private final LegalCharactersField this$0;
/*    */     public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
/* 57 */       char[] arrayOfChar1 = param1String.toCharArray();
/* 58 */       char[] arrayOfChar2 = new char[arrayOfChar1.length];
/* 59 */       byte b1 = 0;
/*    */       
/* 61 */       for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {
/*    */         
/* 63 */         if (this.this$0.legalCharacters.indexOf(Character.toLowerCase(arrayOfChar1[b2])) >= 0) {
/* 64 */           arrayOfChar2[b1++] = arrayOfChar1[b2];
/*    */         } else {
/*    */           
/* 67 */           this.this$0.toolkit.beep();
/*    */         } 
/*    */       } 
/*    */       
/* 71 */       super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/LegalCharactersField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */