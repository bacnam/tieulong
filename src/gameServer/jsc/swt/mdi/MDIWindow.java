/*     */ package jsc.swt.mdi;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.event.InternalFrameAdapter;
/*     */ import javax.swing.event.InternalFrameEvent;
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
/*     */ public abstract class MDIWindow
/*     */   extends JInternalFrame
/*     */ {
/*     */   boolean changed;
/*     */   boolean selection;
/*  23 */   File currentFile = null;
/*     */ 
/*     */ 
/*     */   
/*     */   MDIWindow frame;
/*     */ 
/*     */ 
/*     */   
/*     */   MDIApplication parentApp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MDIWindow(File paramFile) {
/*  37 */     super("", true, true, true, true);
/*  38 */     this.frame = this;
/*  39 */     this.currentFile = paramFile;
/*  40 */     if (paramFile != null) setTitle(paramFile.getName());
/*     */ 
/*     */ 
/*     */     
/*  44 */     addInternalFrameListener(new MyWindowListener(this));
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
/*     */   public abstract void clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void copy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void cut();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/*  85 */     return this.currentFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MDIApplication getParentApp() {
/*  92 */     return this.parentApp;
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
/*     */   public boolean hasSelection() {
/* 104 */     return this.selection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChanged() {
/* 111 */     return this.changed;
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
/*     */   public abstract void paste();
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
/*     */   public abstract void selectAll();
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
/*     */   public abstract void selectNone();
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
/*     */   void setApp(MDIApplication paramMDIApplication) {
/* 182 */     this.parentApp = paramMDIApplication;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged(boolean paramBoolean) {
/* 189 */     this.changed = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File paramFile) {
/* 197 */     this.currentFile = paramFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelection(boolean paramBoolean) {
/* 204 */     this.selection = paramBoolean;
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
/*     */   public abstract boolean write(File paramFile);
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
/*     */   class MyWindowListener
/*     */     extends InternalFrameAdapter
/*     */   {
/*     */     private final MDIWindow this$0;
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
/*     */     MyWindowListener(MDIWindow this$0) {
/* 244 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void internalFrameClosing(InternalFrameEvent param1InternalFrameEvent) {
/* 250 */       if (this.this$0.isChanged()) {
/*     */         
/* 252 */         String str = this.this$0.getTitle();
/* 253 */         int i = JOptionPane.showConfirmDialog(this.this$0.parentApp, str + " has changed." + "\nDo you want to save it?", "Closing " + str, 0);
/*     */ 
/*     */         
/* 256 */         if (i == 0)
/* 257 */           this.this$0.parentApp.save(false, this.this$0.frame); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/mdi/MDIWindow.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */