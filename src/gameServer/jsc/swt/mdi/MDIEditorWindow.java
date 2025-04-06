/*     */ package jsc.swt.mdi;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Container;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
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
/*     */ public class MDIEditorWindow
/*     */   extends MDIWindow
/*     */ {
/*     */   protected JTextArea ta;
/*     */   
/*     */   public MDIEditorWindow(File paramFile, String paramString) {
/*  37 */     super(paramFile);
/*     */     
/*  39 */     this.ta = new JTextArea(paramString);
/*  40 */     this.ta.setEditable(true);
/*  41 */     this.ta.setLineWrap(true);
/*     */ 
/*     */ 
/*     */     
/*  45 */     this.ta.getDocument().addDocumentListener(new TextListener(this));
/*  46 */     this.ta.addCaretListener(new SelectionListener(this));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     Container container = getContentPane();
/*  53 */     container.setLayout(new BorderLayout(1, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     JScrollPane jScrollPane = new JScrollPane(this.ta);
/*  60 */     container.add(jScrollPane, "Center");
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
/*     */   public void clear() {
/*  80 */     this.ta.replaceRange("", this.ta.getSelectionStart(), this.ta.getSelectionEnd());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void copy() {
/*  86 */     this.ta.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cut() {
/*  97 */     this.ta.cut();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paste() {
/* 103 */     this.ta.paste();
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
/*     */   public void selectAll() {
/* 124 */     setSelection(true);
/* 125 */     this.ta.selectAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectNone() {
/* 132 */     setSelection(false);
/* 133 */     this.ta.select(0, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean write(File paramFile) {
/* 144 */     FileWriter fileWriter = null;
/*     */ 
/*     */     
/* 147 */     try { fileWriter = new FileWriter(paramFile);
/* 148 */       String str = this.ta.getText();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 153 */       fileWriter.write(str, 0, str.length());
/*     */       
/* 155 */       return true; }
/*     */     
/* 157 */     catch (IOException iOException) {  }
/* 158 */     finally { try { if (fileWriter != null) fileWriter.close();  } catch (IOException iOException) {} }
/* 159 */      return false;
/*     */   }
/*     */   
/*     */   class SelectionListener implements CaretListener {
/*     */     private final MDIEditorWindow this$0;
/*     */     
/*     */     SelectionListener(MDIEditorWindow this$0) {
/* 166 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void caretUpdate(CaretEvent param1CaretEvent) {
/* 170 */       this.this$0.getParentApp().setEditEnabled((param1CaretEvent.getDot() != param1CaretEvent.getMark()));
/*     */     } }
/*     */   class TextListener implements DocumentListener { private final MDIEditorWindow this$0;
/*     */     
/*     */     TextListener(MDIEditorWindow this$0) {
/* 175 */       this.this$0 = this$0;
/*     */     }
/* 177 */     public void insertUpdate(DocumentEvent param1DocumentEvent) { this.this$0.setChanged(true); }
/* 178 */     public void removeUpdate(DocumentEvent param1DocumentEvent) { this.this$0.setChanged(true); } public void changedUpdate(DocumentEvent param1DocumentEvent) {
/* 179 */       this.this$0.setChanged(true);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/mdi/MDIEditorWindow.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */