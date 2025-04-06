/*     */ package jsc.swt.menu;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Pageable;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import java.net.URL;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.KeyStroke;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrintMenu
/*     */   extends JMenu
/*     */ {
/*     */   public JMenuItem pageSetup;
/*     */   public JMenuItem print;
/*     */   public PageSetupAction pageSetupAction;
/*     */   public PrintAction printAction;
/*     */   Component parentComponent;
/*     */   PageFormat pageFormat;
/*     */   Printable printer;
/*     */   
/*     */   public PrintMenu(Component paramComponent, PageFormat paramPageFormat, Printable paramPrintable) {
/*  50 */     super("Print");
/*  51 */     this.parentComponent = paramComponent;
/*  52 */     this.pageFormat = paramPageFormat;
/*  53 */     this.printer = paramPrintable;
/*  54 */     setMnemonic('P');
/*     */ 
/*     */     
/*  57 */     this.pageSetupAction = new PageSetupAction(this, "Page Setup...");
/*  58 */     this.pageSetup = add(this.pageSetupAction);
/*  59 */     this.pageSetup.setMnemonic('U');
/*     */     
/*  61 */     ImageIcon imageIcon = null;
/*  62 */     URL uRL = getClass().getResource("images/print24.gif"); if (uRL != null) imageIcon = new ImageIcon(uRL); 
/*  63 */     this.printAction = new PrintAction(this, "Print...", imageIcon);
/*  64 */     this.print = add(this.printAction);
/*  65 */     this.print.setMnemonic('P');
/*  66 */     this.print.setAccelerator(KeyStroke.getKeyStroke(80, 2, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabled(boolean paramBoolean) {
/*  76 */     super.setEnabled(paramBoolean);
/*  77 */     this.pageSetupAction.setEnabled(paramBoolean);
/*  78 */     this.printAction.setEnabled(paramBoolean);
/*     */   }
/*     */   class Pager implements Pageable {
/*     */     int pageCount;
/*     */     private final PrintMenu this$0;
/*     */     
/*  84 */     public Pager(PrintMenu this$0, int param1Int) { this.this$0 = this$0; this.pageCount = param1Int; } public int getNumberOfPages() {
/*  85 */       return this.pageCount;
/*     */     }
/*  87 */     public PageFormat getPageFormat(int param1Int) { return this.this$0.pageFormat; } public Printable getPrintable(int param1Int) {
/*  88 */       return this.this$0.printer;
/*     */     } }
/*     */   class PageSetupAction extends AbstractAction { private final PrintMenu this$0;
/*     */     
/*     */     public PageSetupAction(PrintMenu this$0, String param1String) {
/*  93 */       super(param1String); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  96 */       PrinterJob printerJob = PrinterJob.getPrinterJob();
/*  97 */       this.this$0.pageFormat = printerJob.pageDialog(this.this$0.pageFormat);
/*     */     } }
/*     */   
/*     */   class PrintAction extends AbstractAction { private final PrintMenu this$0;
/*     */     
/*     */     public PrintAction(PrintMenu this$0, String param1String, Icon param1Icon) {
/* 103 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 106 */       PrinterJob printerJob = PrinterJob.getPrinterJob();
/*     */       
/* 108 */       printerJob.setPrintable(this.this$0.printer, this.this$0.pageFormat);
/* 109 */       printerJob.setPageable(new PrintMenu.Pager(this.this$0, 1));
/* 110 */       if (printerJob.printDialog())
/*     */         try {
/* 112 */           printerJob.print();
/*     */         } catch (PrinterException printerException) {
/*     */           
/* 115 */           JOptionPane.showMessageDialog(this.this$0.parentComponent, "Cannot print: " + printerException.getMessage(), "System error", 0);
/*     */         }  
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/menu/PrintMenu.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */