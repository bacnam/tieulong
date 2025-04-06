/*     */ package jsc.swt.help;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JOptionPane;
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
/*     */ public class HelpAction
/*     */   extends AbstractAction
/*     */ {
/*     */   String browserPath;
/*     */   String initialHelpPage;
/*     */   Component parent;
/*     */   
/*     */   public HelpAction(Component paramComponent, String paramString1, Icon paramIcon, String paramString2, String paramString3) {
/*  43 */     super(paramString1, paramIcon);
/*  44 */     this.browserPath = paramString2;
/*  45 */     this.initialHelpPage = paramString3;
/*  46 */     this.parent = paramComponent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent paramActionEvent) {
/*  55 */     showHelp(this.parent, this.browserPath, this.initialHelpPage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBrowserPath(String paramString) {
/*  66 */     this.browserPath = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialHelpPage(String paramString) {
/*  76 */     this.initialHelpPage = paramString;
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
/*     */   public static void showHelp(Component paramComponent, String paramString) {
/*  89 */     showHelp(paramComponent, "explorer", paramString);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showHelp(Component paramComponent, String paramString1, String paramString2) {
/* 135 */     Runtime runtime = Runtime.getRuntime();
/*     */     
/* 137 */     Process process = null;
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
/* 149 */     String str = paramString1 + " " + paramString2;
/*     */     try {
/* 151 */       process = runtime.exec(str);
/*     */     
/*     */     }
/*     */     catch (IOException iOException) {
/*     */       
/* 156 */       showHelpWindow(paramComponent, paramString2);
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
/*     */   public static void showHelpWindow(Component paramComponent, String paramString) {
/*     */     try {
/* 175 */       File file = new File(paramString);
/* 176 */       URL uRL = file.toURL();
/* 177 */       HelpWindow helpWindow = new HelpWindow(uRL);
/* 178 */       helpWindow.show();
/*     */     }
/*     */     catch (IOException iOException) {
/*     */       
/* 182 */       JOptionPane.showMessageDialog(paramComponent, "Cannot find Help page " + paramString, "Error", 0);
/*     */     
/*     */     }
/*     */     catch (Exception exception) {
/*     */       
/* 187 */       JOptionPane.showMessageDialog(paramComponent, "Cannot display Help page " + paramString, "Error", 0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/help/HelpAction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */