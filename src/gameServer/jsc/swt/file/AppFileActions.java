/*     */ package jsc.swt.file;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import jsc.Utilities;
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
/*     */ public class AppFileActions
/*     */ {
/*  30 */   static String writeFileErrorMessage = "\nCheck the following.\nIs the file name valid for your system?\nIs there sufficient free space on your disk?\nAre you allowed to write to the disk?";
/*     */ 
/*     */ 
/*     */   
/*  34 */   String confirmCloseMessage = "Save changes to file?";
/*     */   
/*     */   boolean changed;
/*     */   
/*     */   Component app;
/*     */   AppFile appFile;
/*  40 */   File currentFile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FileFilter filter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String defaultFileName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JFileChooser openFileChooser;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JFileChooser saveFileChooser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppFileActions(Component paramComponent, AppFile paramAppFile, FileFilter paramFileFilter, String paramString) {
/*  70 */     this.app = paramComponent;
/*  71 */     this.appFile = paramAppFile;
/*     */ 
/*     */ 
/*     */     
/*  75 */     this.filter = paramFileFilter;
/*  76 */     this.defaultFileName = paramString;
/*     */     
/*  78 */     this.openFileChooser = new JFileChooser();
/*  79 */     this.openFileChooser.addChoosableFileFilter(paramFileFilter);
/*  80 */     this.openFileChooser.setApproveButtonToolTipText("Open file");
/*     */     
/*  82 */     this.saveFileChooser = new JFileChooser();
/*  83 */     this.saveFileChooser.addChoosableFileFilter(paramFileFilter);
/*  84 */     this.saveFileChooser.setApproveButtonToolTipText("Save file");
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
/*     */   public AppFileActions(Component paramComponent, AppFile paramAppFile, String paramString1, String paramString2) {
/*  97 */     this(paramComponent, paramAppFile, new ExampleFileFilter(paramString2, paramString1 + " file"), new String("*." + paramString2));
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
/*     */   public boolean confirmClose() {
/* 109 */     if (this.changed) {
/*     */       
/* 111 */       int i = JOptionPane.showConfirmDialog(this.app, this.confirmCloseMessage, " Confirm", 1);
/* 112 */       if (i == 0)
/* 113 */       { if (!save(false)) return false;  }
/* 114 */       else if (i == 2) { return false; }
/*     */     
/* 116 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 124 */     return this.currentFile;
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
/*     */   public AbstractAction getOpenAction(String paramString, Icon paramIcon) {
/* 138 */     return new OpenAction(this, paramString, paramIcon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 145 */     return this.currentFile.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractAction getSaveAction(String paramString, Icon paramIcon) {
/* 155 */     return new SaveAction(this, paramString, paramIcon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractAction getSaveAsAction(String paramString) {
/* 164 */     return new SaveAsAction(this, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChanged() {
/* 171 */     return this.changed;
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
/*     */   public boolean save(boolean paramBoolean) {
/* 185 */     File file = this.currentFile;
/* 186 */     if (paramBoolean || file == null || file.isDirectory() || !file.isFile()) {
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
/* 197 */       if (file != null && file.isDirectory()) {
/* 198 */         this.saveFileChooser.setCurrentDirectory(file);
/*     */       }
/*     */       
/* 201 */       if (!this.saveFileChooser.isTraversable(file))
/*     */       {
/*     */         
/* 204 */         this.saveFileChooser.setCurrentDirectory(Utilities.getUserDirectory());
/*     */       }
/*     */ 
/*     */       
/* 208 */       if (file == null || file.isDirectory()) {
/*     */         
/* 210 */         this.saveFileChooser.setSelectedFile(new File(this.defaultFileName));
/*     */       } else {
/* 212 */         this.saveFileChooser.setSelectedFile(file);
/*     */       } 
/*     */       
/* 215 */       int i = this.saveFileChooser.showSaveDialog(this.app);
/* 216 */       if (i == 0) {
/*     */         
/* 218 */         file = this.saveFileChooser.getSelectedFile();
/* 219 */         if (file == null) return false;
/*     */       
/*     */       } else {
/* 222 */         return false;
/*     */       } 
/*     */     } 
/* 225 */     if (this.appFile.write(file)) {
/*     */       
/* 227 */       this.appFile.setFile(file);
/*     */       
/* 229 */       this.currentFile = file;
/* 230 */       setChanged(false);
/* 231 */       return true;
/*     */     } 
/*     */     
/* 234 */     showFileWriteErrorMessage(file); return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged(boolean paramBoolean) {
/* 243 */     this.changed = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfirmCloseMessage(String paramString) {
/* 253 */     this.confirmCloseMessage = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultFileName(String paramString) {
/* 260 */     this.defaultFileName = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File paramFile) {
/* 268 */     this.currentFile = paramFile;
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
/*     */   public void setWriteFileErrorMessage(String paramString) {
/* 285 */     this; writeFileErrorMessage = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void showFileWriteErrorMessage(File paramFile) {
/* 294 */     JOptionPane.showMessageDialog(this.app, "Cannot save to file " + paramFile.getName() + writeFileErrorMessage, "Error", 0);
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
/*     */   class OpenAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private final AppFileActions this$0;
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
/*     */     public OpenAction(AppFileActions this$0, String param1String, Icon param1Icon) {
/* 322 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 327 */       if (!this.this$0.confirmClose()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 333 */       if (this.this$0.currentFile == null) {
/*     */ 
/*     */         
/* 336 */         this.this$0.openFileChooser.setCurrentDirectory(Utilities.getUserDirectory());
/*     */       } else {
/*     */         
/* 339 */         this.this$0.openFileChooser.setCurrentDirectory(this.this$0.currentFile);
/*     */       } 
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
/* 357 */       int i = this.this$0.openFileChooser.showOpenDialog(this.this$0.app);
/* 358 */       if (i == 0) {
/*     */         
/* 360 */         File file = this.this$0.openFileChooser.getSelectedFile();
/* 361 */         if (file != null)
/*     */         {
/* 363 */           if (this.this$0.appFile.read(file)) {
/*     */             
/* 365 */             this.this$0.currentFile = file;
/*     */             
/* 367 */             this.this$0.appFile.setFile(file);
/*     */           }
/*     */           else {
/*     */             
/* 371 */             JOptionPane.showMessageDialog(this.this$0.app, "Cannot read file " + file.getName() + "\nThe file must exist and be a file previously saved by this program.", "Error", 0);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class SaveAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private final AppFileActions this$0;
/*     */     
/* 383 */     public SaveAction(AppFileActions this$0, String param1String, Icon param1Icon) { super(param1String, param1Icon); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 384 */       this.this$0.save(false);
/*     */     }
/*     */   }
/*     */   class SaveAsAction extends AbstractAction { private final AppFileActions this$0;
/*     */     
/* 389 */     public SaveAsAction(AppFileActions this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 390 */       this.this$0.save(true);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/file/AppFileActions.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */