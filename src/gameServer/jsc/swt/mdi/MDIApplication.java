/*     */ package jsc.swt.mdi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.io.File;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.DefaultDesktopManager;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JCheckBoxMenuItem;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import jsc.swt.accessory.Calculator;
/*     */ import jsc.swt.control.ToolBar;
/*     */ import jsc.swt.file.ExampleFileFilter;
/*     */ import jsc.swt.help.HelpAction;
/*     */ import jsc.swt.menu.LookAndFeelMenu;
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
/*     */ public abstract class MDIApplication
/*     */   extends JFrame
/*     */ {
/*     */   public static final int NO_WINDOW = 0;
/*     */   public static final int CLOSE_CANCELLED = 1;
/*     */   public static final int CLOSE_CONFIRMED = 2;
/*     */   protected JMenuBar menuBar;
/*     */   protected JMenu fileMenu;
/*     */   protected JMenu editMenu;
/*     */   protected JMenu windowMenu;
/*     */   protected JMenu optionsMenu;
/*     */   protected JMenu helpMenu;
/*     */   protected ToolBar toolBar;
/*     */   protected JDesktopPane desktop;
/*     */   protected JFrame parent;
/*  71 */   Hashtable windowHashtable = new Hashtable(10);
/*     */   
/*     */   CopyAction copyAction;
/*     */   
/*     */   CutAction cutAction;
/*     */   
/*     */   ClearAction clearAction;
/*     */   
/*     */   PasteAction pasteAction;
/*     */   Object aboutMessage;
/*     */   String initialHelpPage;
/*  82 */   String browserPath = "iexporer.exe";
/*     */   String appTitle;
/*  84 */   File currentFile = null;
/*     */   
/*     */   String appFileExtension;
/*     */   
/*  88 */   int untitledWindowCount = 0;
/*     */   
/*     */   Calculator calculator;
/*     */   
/*  92 */   ImageIcon windowIcon = null;
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
/*     */   public MDIApplication(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Object paramObject) {
/* 114 */     super(" " + paramString1);
/* 115 */     this.appTitle = paramString1;
/* 116 */     this.appFileExtension = paramString4;
/* 117 */     this.aboutMessage = paramObject;
/* 118 */     this.initialHelpPage = paramString5;
/*     */     
/* 120 */     this.parent = this;
/* 121 */     addWindowListener(new WL(this));
/*     */     
/* 123 */     if (paramString2 != null) {
/*     */       
/* 125 */       Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 126 */       Image image = toolkit.getImage(paramString2);
/* 127 */       setIconImage(image);
/*     */     } 
/*     */     
/* 130 */     if (paramString3 != null) {
/* 131 */       this.windowIcon = new ImageIcon(paramString3);
/*     */     }
/*     */     
/* 134 */     this.menuBar = new JMenuBar();
/* 135 */     this.menuBar.setBorder(new BevelBorder(0));
/*     */ 
/*     */     
/* 138 */     this.fileMenu = new JMenu("File");
/* 139 */     this.menuBar.add(this.fileMenu);
/* 140 */     NewAction newAction = new NewAction(this, "New...", new ImageIcon("images/new.gif"));
/* 141 */     OpenAction openAction = new OpenAction(this, "Open...", new ImageIcon("images/open.gif"));
/* 142 */     SaveAction saveAction = new SaveAction(this, "Save", new ImageIcon("images/save.gif"));
/* 143 */     SaveAsAction saveAsAction = new SaveAsAction(this, "Save as...");
/* 144 */     ExitAction exitAction = new ExitAction(this, "Exit");
/*     */     
/* 146 */     JMenuItem jMenuItem = this.fileMenu.add(newAction);
/* 147 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(78, 2, false));
/* 148 */     jMenuItem = this.fileMenu.add(openAction);
/* 149 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, 2, false));
/* 150 */     jMenuItem = this.fileMenu.add(saveAction);
/* 151 */     this.fileMenu.add(saveAsAction);
/* 152 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(83, 2, false));
/* 153 */     this.fileMenu.addSeparator();
/* 154 */     this.fileMenu.add(exitAction);
/*     */ 
/*     */     
/* 157 */     this.editMenu = new JMenu("Edit");
/* 158 */     this.menuBar.add(this.editMenu);
/* 159 */     this.cutAction = new CutAction(this, "Cut", new ImageIcon("images/cut.gif"));
/* 160 */     this.clearAction = new ClearAction(this, "Delete");
/* 161 */     this.copyAction = new CopyAction(this, "Copy", new ImageIcon("images/copy.gif"));
/* 162 */     this.pasteAction = new PasteAction(this, "Paste", new ImageIcon("images/paste.gif"));
/* 163 */     SelectAllAction selectAllAction = new SelectAllAction(this, "Select all");
/* 164 */     SelectNoneAction selectNoneAction = new SelectNoneAction(this, "Select none");
/* 165 */     jMenuItem = this.editMenu.add(this.cutAction);
/* 166 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 2, false));
/* 167 */     jMenuItem = this.editMenu.add(this.copyAction);
/* 168 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2, false));
/* 169 */     jMenuItem = this.editMenu.add(this.pasteAction);
/* 170 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 2, false));
/* 171 */     this.editMenu.add(this.clearAction);
/* 172 */     this.editMenu.addSeparator();
/* 173 */     jMenuItem = this.editMenu.add(selectAllAction);
/* 174 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2, false));
/* 175 */     this.editMenu.add(selectNoneAction);
/*     */ 
/*     */     
/* 178 */     this.optionsMenu = new JMenu("Options");
/* 179 */     JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Show toolbar", true);
/* 180 */     jCheckBoxMenuItem.addActionListener(new ShowToolBarListener(this));
/* 181 */     this.optionsMenu.add(jCheckBoxMenuItem);
/*     */     
/* 183 */     LookAndFeelMenu lookAndFeelMenu = new LookAndFeelMenu(this.parent);
/* 184 */     this.optionsMenu.add((JMenuItem)lookAndFeelMenu);
/* 185 */     this.menuBar.add(this.optionsMenu);
/*     */ 
/*     */     
/* 188 */     this.windowMenu = new JMenu("Window");
/* 189 */     this.menuBar.add(this.windowMenu);
/* 190 */     CascadeAction cascadeAction = new CascadeAction(this, "Cascade");
/* 191 */     this.windowMenu.add(cascadeAction);
/* 192 */     TileAction tileAction = new TileAction(this, "Tile");
/* 193 */     this.windowMenu.add(tileAction);
/* 194 */     CloseAllAction closeAllAction = new CloseAllAction(this, "Close all");
/* 195 */     this.windowMenu.add(closeAllAction);
/* 196 */     this.windowMenu.addSeparator();
/*     */ 
/*     */     
/* 199 */     this.helpMenu = new JMenu("Help");
/* 200 */     this.menuBar.add(this.helpMenu);
/* 201 */     CalculatorAction calculatorAction = new CalculatorAction(this, "Calculator...", new ImageIcon("images/calculator.gif"));
/* 202 */     this.helpMenu.add(calculatorAction);
/*     */     
/* 204 */     HelpAction helpAction = new HelpAction(this.parent, "Help topics...", new ImageIcon("images/help.gif"), this.browserPath, paramString5);
/*     */     
/* 206 */     this.helpMenu.add((Action)helpAction);
/* 207 */     AboutAction aboutAction = new AboutAction(this, "About...");
/* 208 */     this.helpMenu.add(aboutAction);
/*     */ 
/*     */     
/* 211 */     this.toolBar = new ToolBar(this.appTitle + " tool bar", new Dimension(30, 30));
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
/* 223 */     this.toolBar.add(newAction, "New");
/*     */ 
/*     */ 
/*     */     
/* 227 */     this.toolBar.add(openAction, "Open file");
/*     */ 
/*     */ 
/*     */     
/* 231 */     this.toolBar.add(saveAction, "Save file");
/*     */ 
/*     */ 
/*     */     
/* 235 */     this.toolBar.addSeparator();
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
/* 257 */     setEditEnabled(false);
/*     */     
/* 259 */     if (getToolkit().getSystemClipboard().getContents(this) == null) {
/* 260 */       this.pasteAction.setEnabled(false);
/*     */     }
/* 262 */     this.toolBar.add(this.cutAction, "Cut");
/*     */ 
/*     */ 
/*     */     
/* 266 */     this.toolBar.add(this.copyAction, "Copy");
/*     */ 
/*     */ 
/*     */     
/* 270 */     this.toolBar.add(this.pasteAction, "Paste");
/*     */ 
/*     */ 
/*     */     
/* 274 */     this.toolBar.addSeparator();
/* 275 */     this.toolBar.add((Action)helpAction, "Help");
/* 276 */     this.toolBar.add(calculatorAction, "Calculator");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     setJMenuBar(this.menuBar);
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
/* 299 */     this.desktop = new JDesktopPane();
/* 300 */     setContentPane(this.desktop);
/* 301 */     this.desktop.setDesktopManager(new WindowManager(this));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     this.toolBar.addToDesktop(this.desktop);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     this.calculator = new Calculator(this.parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeAllWindows() {
/* 323 */     JInternalFrame[] arrayOfJInternalFrame = this.desktop.getAllFrames();
/* 324 */     for (byte b = 0; b < arrayOfJInternalFrame.length; b++) { 
/* 325 */       try { arrayOfJInternalFrame[b].setClosed(true); } catch (Exception exception) {} }
/*     */   
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
/*     */   public abstract MDIWindow createWindow(File paramFile);
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
/*     */   public void fileWriteErrorMessage(File paramFile) {
/* 351 */     JOptionPane.showMessageDialog(this.parent, "Cannot save window to file " + paramFile.getName() + "\nCheck the following." + "\nIs the file name valid for your system?" + "\nIs there sufficient free space on your disk?" + "\nAre you allowed to write to the disk?", "Error", 0);
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
/*     */   public MDIWindow getActiveWindow() {
/* 367 */     return (MDIWindow)this.desktop.getSelectedFrame();
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
/*     */   void newWindow(File paramFile) {
/* 383 */     MDIWindow mDIWindow = createWindow(paramFile);
/* 384 */     if (mDIWindow == null)
/* 385 */       return;  if (paramFile == null) mDIWindow.setTitle("Untitled " + ++this.untitledWindowCount); 
/* 386 */     mDIWindow.setApp(this);
/* 387 */     if (this.windowIcon != null) mDIWindow.setFrameIcon(this.windowIcon); 
/* 388 */     mDIWindow.setBounds(0, this.toolBar.getVisibleHeight(), 400, 300);
/* 389 */     this.desktop.add(mDIWindow, JLayeredPane.DEFAULT_LAYER);
/* 390 */     mDIWindow.setVisible(true);
/* 391 */     JMenuItem jMenuItem = this.windowMenu.add(new WindowAction(this, mDIWindow.getTitle(), mDIWindow));
/* 392 */     this.windowHashtable.put(mDIWindow, jMenuItem);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processWindowEvent(WindowEvent paramWindowEvent) {
/* 401 */     if (paramWindowEvent.getID() == 201 && 
/* 402 */       saveQuery() == 1)
/* 403 */       return;  super.processWindowEvent(paramWindowEvent);
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
/*     */   public boolean save(boolean paramBoolean, MDIWindow paramMDIWindow) {
/* 486 */     if (paramMDIWindow == null) return false; 
/* 487 */     File file = paramMDIWindow.getFile();
/* 488 */     if (paramBoolean || file == null) {
/*     */       
/* 490 */       JFileChooser jFileChooser = new JFileChooser(this.currentFile);
/* 491 */       String[] arrayOfString = { this.appFileExtension };
/* 492 */       ExampleFileFilter exampleFileFilter = new ExampleFileFilter(arrayOfString, this.appTitle + " file");
/* 493 */       jFileChooser.addChoosableFileFilter((FileFilter)exampleFileFilter);
/* 494 */       jFileChooser.setApproveButtonToolTipText("Save window to file");
/* 495 */       if (file == null) {
/* 496 */         jFileChooser.setSelectedFile(new File("*." + this.appFileExtension));
/*     */       } else {
/* 498 */         jFileChooser.setSelectedFile(file);
/* 499 */       }  int i = jFileChooser.showSaveDialog(this.parent);
/* 500 */       if (i == 0) {
/*     */         
/* 502 */         file = jFileChooser.getSelectedFile();
/* 503 */         if (file == null) return false;
/*     */       
/*     */       } else {
/* 506 */         return false;
/*     */       } 
/* 508 */     }  if (paramMDIWindow.write(file)) {
/*     */       
/* 510 */       paramMDIWindow.setFile(file);
/* 511 */       setTitle(file.getName());
/* 512 */       paramMDIWindow.setChanged(false);
/* 513 */       this.currentFile = file;
/* 514 */       return true;
/*     */     } 
/*     */     
/* 517 */     fileWriteErrorMessage(file); return false;
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
/*     */   protected int saveQuery() {
/* 530 */     JInternalFrame[] arrayOfJInternalFrame = this.desktop.getAllFrames();
/* 531 */     for (int i = arrayOfJInternalFrame.length - 1; i >= 0; i--) {
/*     */       
/* 533 */       MDIWindow mDIWindow = (MDIWindow)arrayOfJInternalFrame[i];
/* 534 */       if (mDIWindow.isChanged()) {
/*     */         
/* 536 */         int j = JOptionPane.showConfirmDialog(this.parent, mDIWindow.getTitle() + " has changed." + "\nDo you want to save it?", "Closing " + this.appTitle, 1);
/*     */ 
/*     */         
/* 539 */         if (j == 0)
/* 540 */         { if (!save(false, mDIWindow)) return 1;  }
/* 541 */         else if (j == 2) { return 1; }
/*     */       
/*     */       } 
/* 544 */     }  return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClearEnabled(boolean paramBoolean) {
/* 552 */     this.clearAction.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCopyEnabled(boolean paramBoolean) {
/* 559 */     this.copyAction.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCutEnabled(boolean paramBoolean) {
/* 566 */     this.cutAction.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEditEnabled(boolean paramBoolean) {
/* 576 */     this.copyAction.setEnabled(paramBoolean);
/* 577 */     this.cutAction.setEnabled(paramBoolean);
/* 578 */     this.clearAction.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPasteEnabled(boolean paramBoolean) {
/* 586 */     this.pasteAction.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(String paramString) {
/* 593 */     super.setTitle(this.appTitle + " - " + paramString);
/*     */   }
/*     */   
/*     */   class AboutAction extends AbstractAction { public AboutAction(MDIApplication this$0, String param1String) {
/* 597 */       super(param1String); this.this$0 = this$0;
/*     */     } private final MDIApplication this$0; public void actionPerformed(ActionEvent param1ActionEvent) {
/* 599 */       JOptionPane.showMessageDialog(this.this$0.parent, this.this$0.aboutMessage, "About " + this.this$0.appTitle, -1);
/*     */     } }
/*     */   class CalculatorAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public CalculatorAction(MDIApplication this$0, String param1String, Icon param1Icon) {
/* 604 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 608 */       this.this$0.calculator.show();
/*     */     } }
/*     */   
/*     */   class CascadeAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public CascadeAction(MDIApplication this$0, String param1String) {
/* 614 */       super(param1String); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 617 */       JInternalFrame[] arrayOfJInternalFrame = this.this$0.desktop.getAllFrames();
/* 618 */       boolean bool = false;
/* 619 */       int i = this.this$0.toolBar.getVisibleHeight();
/* 620 */       for (int j = arrayOfJInternalFrame.length - 1; j >= 0; j--) {
/*     */         
/* 622 */         Rectangle rectangle = arrayOfJInternalFrame[j].getNormalBounds();
/* 623 */         rectangle.setLocation(bool, i);
/* 624 */         arrayOfJInternalFrame[j].setBounds(rectangle);
/* 625 */         bool += true; i += 25; 
/* 626 */         try { arrayOfJInternalFrame[j].setIcon(false); } catch (Exception exception) {}
/* 627 */         arrayOfJInternalFrame[j].show();
/*     */       } 
/*     */     } }
/*     */   
/*     */   class ClearAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public ClearAction(MDIApplication this$0, String param1String) {
/* 634 */       super(param1String); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 637 */       MDIWindow mDIWindow = this.this$0.getActiveWindow();
/* 638 */       if (mDIWindow == null)
/* 639 */         return;  mDIWindow.clear();
/* 640 */       mDIWindow.setChanged(true);
/*     */     } }
/*     */ 
/*     */   
/*     */   class CloseAllAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/* 646 */     public CloseAllAction(MDIApplication this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 647 */       this.this$0.closeAllWindows();
/*     */     } }
/*     */   class CopyAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public CopyAction(MDIApplication this$0, String param1String, Icon param1Icon) {
/* 652 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 655 */       MDIWindow mDIWindow = this.this$0.getActiveWindow();
/* 656 */       if (mDIWindow == null)
/* 657 */         return;  mDIWindow.copy();
/* 658 */       this.this$0.pasteAction.setEnabled(true);
/*     */     } }
/*     */   
/*     */   class CutAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public CutAction(MDIApplication this$0, String param1String, Icon param1Icon) {
/* 664 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 667 */       MDIWindow mDIWindow = this.this$0.getActiveWindow();
/* 668 */       if (mDIWindow == null)
/* 669 */         return;  mDIWindow.copy();
/* 670 */       mDIWindow.clear();
/* 671 */       mDIWindow.setChanged(true);
/* 672 */       this.this$0.pasteAction.setEnabled(true);
/*     */     } }
/*     */   
/*     */   class ExitAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public ExitAction(MDIApplication this$0, String param1String) {
/* 678 */       super(param1String); this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 682 */       this.this$0.dispatchEvent(new WindowEvent(this.this$0, 201));
/*     */     } }
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
/*     */   class NewAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private final MDIApplication this$0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NewAction(MDIApplication this$0, String param1String, Icon param1Icon) {
/* 709 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 716 */       this.this$0.newWindow((File)null);
/*     */     } }
/*     */   
/*     */   class OpenAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public OpenAction(MDIApplication this$0, String param1String, Icon param1Icon) {
/* 722 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 727 */       JFileChooser jFileChooser = new JFileChooser(this.this$0.currentFile);
/* 728 */       String[] arrayOfString = { this.this$0.appFileExtension };
/* 729 */       ExampleFileFilter exampleFileFilter = new ExampleFileFilter(arrayOfString, this.this$0.appTitle + " files");
/* 730 */       jFileChooser.addChoosableFileFilter((FileFilter)exampleFileFilter);
/* 731 */       jFileChooser.setApproveButtonToolTipText("Open file");
/* 732 */       int i = jFileChooser.showOpenDialog(this.this$0.parent);
/* 733 */       if (i == 0) {
/*     */         
/* 735 */         File file = jFileChooser.getSelectedFile();
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
/* 755 */         this.this$0.newWindow(file);
/* 756 */         this.this$0.currentFile = file;
/*     */       } 
/*     */     } }
/*     */   
/*     */   class PasteAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public PasteAction(MDIApplication this$0, String param1String, Icon param1Icon) {
/* 763 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 766 */       MDIWindow mDIWindow = this.this$0.getActiveWindow();
/* 767 */       if (mDIWindow == null)
/* 768 */         return;  mDIWindow.paste();
/* 769 */       mDIWindow.setChanged(true);
/*     */     } }
/*     */ 
/*     */   
/*     */   class SaveAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/* 775 */     public SaveAction(MDIApplication this$0, String param1String, Icon param1Icon) { super(param1String, param1Icon); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 776 */       this.this$0.save(false, this.this$0.getActiveWindow());
/*     */     } }
/*     */   
/*     */   class SaveAsAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/* 781 */     public SaveAsAction(MDIApplication this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 782 */       this.this$0.save(true, this.this$0.getActiveWindow());
/*     */     } }
/*     */   class SelectAllAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public SelectAllAction(MDIApplication this$0, String param1String) {
/* 787 */       super(param1String); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 790 */       MDIWindow mDIWindow = this.this$0.getActiveWindow();
/* 791 */       if (mDIWindow == null)
/* 792 */         return;  mDIWindow.selectAll();
/* 793 */       mDIWindow.setSelection(true);
/* 794 */       this.this$0.setEditEnabled(true);
/*     */     } }
/*     */   
/*     */   class SelectNoneAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public SelectNoneAction(MDIApplication this$0, String param1String) {
/* 800 */       super(param1String); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 803 */       MDIWindow mDIWindow = this.this$0.getActiveWindow();
/* 804 */       if (mDIWindow == null)
/* 805 */         return;  mDIWindow.selectNone();
/* 806 */       mDIWindow.setSelection(false);
/* 807 */       this.this$0.setEditEnabled(false);
/*     */     } }
/*     */   
/*     */   class ShowToolBarListener implements ActionListener { ShowToolBarListener(MDIApplication this$0) {
/* 811 */       this.this$0 = this$0;
/*     */     } private final MDIApplication this$0;
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 814 */       this.this$0.toolBar.setVisible(!this.this$0.toolBar.isVisible());
/*     */     } }
/*     */   class TileAction extends AbstractAction { private final MDIApplication this$0;
/*     */     
/*     */     public TileAction(MDIApplication this$0, String param1String) {
/* 819 */       super(param1String); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 822 */       JInternalFrame[] arrayOfJInternalFrame = this.this$0.desktop.getAllFrames();
/* 823 */       int i = arrayOfJInternalFrame.length;
/* 824 */       if (i == 0)
/* 825 */         return;  int j = (int)Math.sqrt(i);
/* 826 */       int k = j;
/* 827 */       int m = j;
/* 828 */       if (k * m < i) {
/*     */         
/* 830 */         m++;
/* 831 */         if (k * m < i) k++; 
/*     */       } 
/* 833 */       Dimension dimension = this.this$0.desktop.getSize();
/* 834 */       int n = dimension.width / m;
/* 835 */       int i1 = dimension.height / k - this.this$0.toolBar.getVisibleHeight();
/* 836 */       int i2 = 0;
/* 837 */       int i3 = this.this$0.toolBar.getVisibleHeight();
/* 838 */       for (byte b = 0; b < k; b++) {
/*     */         
/* 840 */         for (byte b1 = 0; b1 < m && b * m + b1 < i; b1++) {
/*     */           
/* 842 */           JInternalFrame jInternalFrame = arrayOfJInternalFrame[b * m + b1]; 
/* 843 */           try { jInternalFrame.setIcon(false); } catch (Exception exception) {}
/* 844 */           jInternalFrame.setBounds(new Rectangle(i2, i3, n, i1));
/* 845 */           i2 += n;
/*     */         } 
/* 847 */         i3 += i1;
/* 848 */         i2 = 0;
/*     */       } 
/*     */     } }
/*     */   
/*     */   class WindowAction extends AbstractAction {
/*     */     JInternalFrame f;
/*     */     private final MDIApplication this$0;
/*     */     
/*     */     public WindowAction(MDIApplication this$0, String param1String, JInternalFrame param1JInternalFrame) {
/* 857 */       super(param1String); this.this$0 = this$0; this.f = param1JInternalFrame;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*     */       
/* 862 */       try { this.f.setIcon(false); } catch (Exception exception) {}
/*     */ 
/*     */       
/* 865 */       this.f.toFront(); try {
/* 866 */         this.f.setSelected(true);
/* 867 */       } catch (PropertyVetoException propertyVetoException) {}
/*     */     } public JInternalFrame getWindow() {
/* 869 */       return this.f;
/*     */     }
/*     */   }
/*     */   
/*     */   class WindowManager extends DefaultDesktopManager { private final MDIApplication this$0;
/*     */     
/*     */     WindowManager(MDIApplication this$0) {
/* 876 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateFrame(JInternalFrame param1JInternalFrame) {
/* 881 */       super.activateFrame(param1JInternalFrame);
/*     */       
/* 883 */       this.this$0.setTitle(param1JInternalFrame.getTitle());
/* 884 */       param1JInternalFrame.requestFocus();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void closeFrame(JInternalFrame param1JInternalFrame) {
/* 891 */       System.out.println("MDI Window closing");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 900 */       Object object = this.this$0.windowHashtable.get(param1JInternalFrame);
/* 901 */       if (object != null) {
/*     */         
/* 903 */         this.this$0.windowMenu.remove((JMenuItem)object);
/* 904 */         this.this$0.windowHashtable.remove(param1JInternalFrame);
/*     */         
/* 906 */         System.out.println("Removing window menu item");
/*     */       } 
/* 908 */       super.closeFrame(param1JInternalFrame);
/* 909 */       this.this$0.desktop.remove(param1JInternalFrame);
/*     */       
/* 911 */       param1JInternalFrame = null;
/*     */ 
/*     */       
/* 914 */       if (this.this$0.getActiveWindow() == null) this.this$0.setTitle(""); 
/*     */     } }
/*     */   class WL extends WindowAdapter { private final MDIApplication this$0;
/*     */     
/*     */     WL(MDIApplication this$0) {
/* 919 */       this.this$0 = this$0;
/*     */     } public void windowClosing(WindowEvent param1WindowEvent) {
/* 921 */       System.exit(0);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/mdi/MDIApplication.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */