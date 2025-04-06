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
/*    */ 
/*    */ 
/*    */ public class IllegalCharactersField
/*    */   extends JTextField
/*    */ {
/*    */   private Toolkit toolkit;
/*    */   private String illegalCharacters;
/*    */   
/*    */   public IllegalCharactersField(int paramInt, String paramString) {
/* 38 */     super(paramInt);
/* 39 */     this.illegalCharacters = paramString;
/* 40 */     this.toolkit = Toolkit.getDefaultToolkit();
/* 41 */     setBackground(Color.white);
/*    */   }
/*    */   
/*    */   protected Document createDefaultModel() {
/* 45 */     return new IllegalCharactersDocument(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setIllegalCharacters(String paramString) {
/* 53 */     this.illegalCharacters = paramString;
/*    */   } protected class IllegalCharactersDocument extends PlainDocument { protected IllegalCharactersDocument(IllegalCharactersField this$0) {
/* 55 */       this.this$0 = this$0;
/*    */     }
/*    */     private final IllegalCharactersField this$0;
/*    */     public void insertString(int param1Int, String param1String, AttributeSet param1AttributeSet) throws BadLocationException {
/* 59 */       char[] arrayOfChar1 = param1String.toCharArray();
/* 60 */       char[] arrayOfChar2 = new char[arrayOfChar1.length];
/* 61 */       byte b1 = 0;
/*    */       
/* 63 */       for (byte b2 = 0; b2 < arrayOfChar2.length; b2++) {
/*    */         
/* 65 */         if (this.this$0.illegalCharacters.indexOf(Character.toLowerCase(arrayOfChar1[b2])) >= 0) {
/*    */           
/* 67 */           this.this$0.toolkit.beep();
/*    */         }
/*    */         else {
/*    */           
/* 71 */           arrayOfChar2[b1++] = arrayOfChar1[b2];
/*    */         } 
/* 73 */       }  super.insertString(param1Int, new String(arrayOfChar2, 0, b1), param1AttributeSet);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/IllegalCharactersField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */