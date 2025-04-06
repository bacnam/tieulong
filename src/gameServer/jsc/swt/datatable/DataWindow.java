/*      */ package jsc.swt.datatable;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.GridLayout;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.io.File;
/*      */ import java.net.URL;
/*      */ import java.util.Vector;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.JCheckBoxMenuItem;
/*      */ import javax.swing.JInternalFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRadioButton;
/*      */ import javax.swing.JRadioButtonMenuItem;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListSelectionModel;
/*      */ import javax.swing.border.BevelBorder;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import jsc.swt.control.ToolBar;
/*      */ import jsc.swt.dialogue.Dialogue;
/*      */ import jsc.swt.dialogue.NameDialogue;
/*      */ import jsc.swt.help.HelpAction;
/*      */ import jsc.swt.menu.DigitsMenu;
/*      */ import jsc.util.Rank;
/*      */ import jsc.util.Sort;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DataWindow
/*      */   extends JInternalFrame
/*      */ {
/*      */   protected JMenuBar menuBar;
/*      */   protected JMenu helpMenu;
/*      */   protected ToolBar toolBar;
/*      */   protected DataTable dataTable;
/*   68 */   String initialHelpPage = "C:\\java\\jsc\\swt\\datatable\\help\\datatable.htm";
/*      */   
/*   70 */   String browserPath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
/*      */ 
/*      */   
/*      */   CopyAction copyAction;
/*      */   
/*      */   CutAction cutAction;
/*      */   
/*      */   ClearAction clearAction;
/*      */   
/*      */   PasteAction pasteAction;
/*      */   
/*      */   PasteClipboardAction pasteClipboardAction;
/*      */   
/*      */   JInternalFrame parent;
/*      */   
/*      */   DataCalculator calculator;
/*      */   
/*      */   ImageIcon windowIcon;
/*      */   
/*      */   HelpAction helpAction;
/*      */ 
/*      */   
/*      */   public DataWindow(DataMatrix paramDataMatrix) {
/*   93 */     super(" Data", true, true, true, true);
/*   94 */     this.parent = this;
/*      */ 
/*      */ 
/*      */     
/*   98 */     DataModel dataModel = new DataModel(paramDataMatrix);
/*      */     
/*  100 */     this.dataTable = new DataTable(dataModel);
/*      */     
/*  102 */     this.dataTable.setColumnSelectionAllowed(false);
/*  103 */     this.dataTable.setRowSelectionAllowed(false);
/*  104 */     this.dataTable.setCellSelectionEnabled(true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  119 */     ImageIcon imageIcon1 = null, imageIcon2 = null, imageIcon3 = null, imageIcon4 = null;
/*  120 */     ImageIcon imageIcon5 = null, imageIcon6 = null, imageIcon7 = null;
/*  121 */     Class clazz = getClass();
/*  122 */     URL uRL = clazz.getResource("images/table.gif");
/*  123 */     if (uRL != null) this.windowIcon = new ImageIcon(uRL); 
/*  124 */     uRL = clazz.getResource("images/cut.gif");
/*  125 */     if (uRL != null) imageIcon1 = new ImageIcon(uRL); 
/*  126 */     uRL = clazz.getResource("images/copy.gif");
/*  127 */     if (uRL != null) imageIcon2 = new ImageIcon(uRL); 
/*  128 */     uRL = clazz.getResource("images/paste.gif");
/*  129 */     if (uRL != null) imageIcon3 = new ImageIcon(uRL); 
/*  130 */     uRL = clazz.getResource("images/newrow.gif");
/*  131 */     if (uRL != null) imageIcon4 = new ImageIcon(uRL); 
/*  132 */     uRL = clazz.getResource("images/newcol.gif");
/*  133 */     if (uRL != null) imageIcon5 = new ImageIcon(uRL); 
/*  134 */     uRL = clazz.getResource("images/calculator.gif");
/*  135 */     if (uRL != null) imageIcon6 = new ImageIcon(uRL); 
/*  136 */     uRL = clazz.getResource("images/help.gif");
/*  137 */     if (uRL != null) imageIcon7 = new ImageIcon(uRL);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  146 */     setFrameIcon(this.windowIcon);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  153 */     setClosable(false);
/*      */ 
/*      */     
/*  156 */     this.menuBar = new JMenuBar();
/*  157 */     this.menuBar.setBorder(new BevelBorder(0));
/*      */ 
/*      */     
/*  160 */     JMenu jMenu1 = new JMenu("Edit");
/*  161 */     jMenu1.setMnemonic('E');
/*  162 */     this.menuBar.add(jMenu1);
/*  163 */     this.cutAction = new CutAction("Cut", imageIcon1);
/*  164 */     this.clearAction = new ClearAction("Delete");
/*  165 */     this.copyAction = new CopyAction("Copy", imageIcon2);
/*  166 */     this.pasteAction = new PasteAction("Paste", imageIcon3);
/*  167 */     this.pasteClipboardAction = new PasteClipboardAction("Paste from system");
/*  168 */     SelectAllAction selectAllAction = new SelectAllAction("Select all");
/*  169 */     SelectNoneAction selectNoneAction = new SelectNoneAction("Select none");
/*  170 */     InsertRowAction insertRowAction = new InsertRowAction("Insert row", imageIcon4);
/*  171 */     InsertColumnAction insertColumnAction = new InsertColumnAction("Add column", imageIcon5);
/*  172 */     ResetColumnAction resetColumnAction = new ResetColumnAction("Reset column order");
/*      */     
/*  174 */     JMenuItem jMenuItem = jMenu1.add(this.cutAction);
/*  175 */     jMenuItem.setMnemonic('T');
/*  176 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 2, false));
/*  177 */     jMenuItem = jMenu1.add(this.copyAction);
/*  178 */     jMenuItem.setMnemonic('C');
/*  179 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2, false));
/*  180 */     jMenuItem = jMenu1.add(this.pasteAction);
/*  181 */     jMenuItem.setMnemonic('P');
/*  182 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 2, false));
/*  183 */     jMenuItem = jMenu1.add(this.pasteClipboardAction);
/*  184 */     jMenuItem.setMnemonic('Y');
/*  185 */     jMenuItem = jMenu1.add(this.clearAction);
/*  186 */     jMenuItem.setMnemonic('L');
/*  187 */     jMenu1.addSeparator();
/*  188 */     jMenuItem = jMenu1.add(selectAllAction);
/*  189 */     jMenuItem.setMnemonic('S');
/*  190 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2, false));
/*  191 */     jMenuItem = jMenu1.add(selectNoneAction);
/*  192 */     jMenuItem.setMnemonic('N');
/*  193 */     jMenu1.addSeparator();
/*  194 */     jMenuItem = jMenu1.add(insertRowAction);
/*  195 */     jMenuItem.setMnemonic('I');
/*  196 */     jMenuItem = jMenu1.add(insertColumnAction);
/*  197 */     jMenuItem.setMnemonic('A');
/*  198 */     jMenuItem = jMenu1.add(resetColumnAction);
/*  199 */     jMenuItem.setMnemonic('O');
/*  200 */     jMenu1.addSeparator();
/*  201 */     RenameColumnAction renameColumnAction = new RenameColumnAction("Rename column");
/*  202 */     jMenuItem = jMenu1.add(renameColumnAction);
/*  203 */     jMenuItem.setMnemonic('R');
/*  204 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(82, 2, false));
/*  205 */     ChangeColumnTypeAction changeColumnTypeAction = new ChangeColumnTypeAction("Change column type");
/*  206 */     jMenuItem = jMenu1.add(changeColumnTypeAction);
/*  207 */     jMenuItem.setMnemonic('H');
/*  208 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(72, 2, false));
/*      */ 
/*      */     
/*  211 */     JMenu jMenu2 = new JMenu("Manipulate");
/*  212 */     jMenu2.setMnemonic('M');
/*  213 */     this.menuBar.add(jMenu2);
/*  214 */     SortRowsAction sortRowsAction = new SortRowsAction("Sort rows...");
/*  215 */     jMenuItem = jMenu2.add(sortRowsAction);
/*  216 */     jMenuItem.setMnemonic('S');
/*  217 */     RecodeAction recodeAction = new RecodeAction("Recode...");
/*  218 */     jMenuItem = jMenu2.add(recodeAction);
/*  219 */     jMenuItem.setMnemonic('R');
/*  220 */     SubsetAction subsetAction = new SubsetAction("Subset...");
/*  221 */     jMenuItem = jMenu2.add(subsetAction);
/*  222 */     jMenuItem.setMnemonic('U');
/*      */ 
/*      */     
/*  225 */     JMenu jMenu3 = new JMenu("Calculate");
/*  226 */     jMenu3.setMnemonic('C');
/*  227 */     this.menuBar.add(jMenu3);
/*      */ 
/*      */     
/*  230 */     CalculatorAction calculatorAction = new CalculatorAction("Calculator...", imageIcon6);
/*  231 */     jMenuItem = jMenu3.add(calculatorAction);
/*  232 */     jMenuItem.setMnemonic('C');
/*  233 */     jMenu3.addSeparator();
/*  234 */     CalculateListener calculateListener = new CalculateListener();
/*  235 */     jMenuItem = jMenu3.add("Square...");
/*  236 */     jMenuItem.setMnemonic('S');
/*  237 */     jMenuItem.addActionListener(calculateListener);
/*  238 */     jMenuItem = jMenu3.add("Square root...");
/*  239 */     jMenuItem.setMnemonic('Q');
/*  240 */     jMenuItem.addActionListener(calculateListener);
/*  241 */     jMenuItem = jMenu3.add("Log...");
/*  242 */     jMenuItem.setMnemonic('L');
/*  243 */     jMenuItem.addActionListener(calculateListener);
/*      */ 
/*      */ 
/*      */     
/*  247 */     jMenuItem = jMenu3.add("Reciprocal root...");
/*  248 */     jMenuItem.setMnemonic('R');
/*  249 */     jMenuItem.addActionListener(calculateListener);
/*  250 */     jMenuItem = jMenu3.add("Reciprocal...");
/*  251 */     jMenuItem.setMnemonic('E');
/*  252 */     jMenuItem.addActionListener(calculateListener);
/*  253 */     jMenu3.addSeparator();
/*  254 */     jMenuItem = jMenu3.add("Rank...");
/*  255 */     jMenuItem.setMnemonic('K');
/*  256 */     jMenuItem.addActionListener(calculateListener);
/*  257 */     jMenuItem = jMenu3.add("Normal scores...");
/*  258 */     jMenuItem.setMnemonic('N');
/*  259 */     jMenuItem.addActionListener(calculateListener);
/*      */ 
/*      */     
/*  262 */     JMenu jMenu4 = new JMenu("Options");
/*  263 */     jMenu4.setMnemonic('O');
/*  264 */     this.menuBar.add(jMenu4);
/*  265 */     JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Show toolbar", true);
/*  266 */     jCheckBoxMenuItem.setMnemonic('T');
/*  267 */     jCheckBoxMenuItem.addActionListener(new ShowToolBarListener());
/*  268 */     jMenu4.add(jCheckBoxMenuItem);
/*      */     
/*  270 */     JMenu jMenu5 = new JMenu("Selection");
/*  271 */     jMenu5.setMnemonic('S');
/*  272 */     jMenu4.add(jMenu5);
/*  273 */     ButtonGroup buttonGroup1 = new ButtonGroup();
/*  274 */     JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Columns");
/*  275 */     jRadioButtonMenuItem1.setMnemonic('C');
/*  276 */     JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Rows");
/*  277 */     jRadioButtonMenuItem2.setMnemonic('R');
/*  278 */     JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Any cells", true);
/*  279 */     jRadioButtonMenuItem3.setMnemonic('A');
/*      */     
/*  281 */     jRadioButtonMenuItem1.addActionListener(new SelectionColumnsListener());
/*  282 */     jRadioButtonMenuItem2.addActionListener(new SelectionRowsListener());
/*  283 */     jRadioButtonMenuItem3.addActionListener(new SelectionCellsListener());
/*      */     
/*  285 */     buttonGroup1.add(jRadioButtonMenuItem1); buttonGroup1.add(jRadioButtonMenuItem2); buttonGroup1.add(jRadioButtonMenuItem3);
/*  286 */     jMenu5.add(jRadioButtonMenuItem1);
/*  287 */     jMenu5.add(jRadioButtonMenuItem2);
/*  288 */     jMenu5.add(jRadioButtonMenuItem3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  294 */     DigitsMenu digitsMenu = new DigitsMenu("Significant figures", 16, this.dataTable.getSignificantDigits(), new DigitsListener());
/*      */     
/*  296 */     digitsMenu.setMnemonic('I');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  311 */     jMenu4.add((JMenuItem)digitsMenu);
/*      */     
/*  313 */     JMenu jMenu6 = new JMenu("Default column type");
/*  314 */     jMenu6.setMnemonic('D');
/*  315 */     ButtonGroup buttonGroup2 = new ButtonGroup();
/*  316 */     Class clazz1 = paramDataMatrix.getDefaultColumnClass();
/*  317 */     JRadioButtonMenuItem jRadioButtonMenuItem4 = new JRadioButtonMenuItem("Categorical", (clazz1 == String.class));
/*  318 */     jRadioButtonMenuItem4.setMnemonic('C');
/*  319 */     JRadioButtonMenuItem jRadioButtonMenuItem5 = new JRadioButtonMenuItem("Continuous", (clazz1 == Double.class));
/*  320 */     jRadioButtonMenuItem5.setMnemonic('O');
/*  321 */     JRadioButtonMenuItem jRadioButtonMenuItem6 = new JRadioButtonMenuItem("Integer", (clazz1 == Integer.class));
/*  322 */     jRadioButtonMenuItem6.setMnemonic('I');
/*  323 */     jRadioButtonMenuItem4.addActionListener(new CategoricalListener());
/*  324 */     jRadioButtonMenuItem5.addActionListener(new ContinuousListener());
/*  325 */     jRadioButtonMenuItem6.addActionListener(new IntegerListener());
/*  326 */     buttonGroup2.add(jRadioButtonMenuItem4);
/*  327 */     buttonGroup2.add(jRadioButtonMenuItem5);
/*  328 */     buttonGroup2.add(jRadioButtonMenuItem6);
/*  329 */     jMenu6.add(jRadioButtonMenuItem4);
/*  330 */     jMenu6.add(jRadioButtonMenuItem5);
/*  331 */     jMenu6.add(jRadioButtonMenuItem6);
/*  332 */     jMenu4.add(jMenu6);
/*      */ 
/*      */     
/*  335 */     this.helpMenu = new JMenu("Help");
/*  336 */     this.helpMenu.setMnemonic('H');
/*  337 */     this.menuBar.add(this.helpMenu);
/*  338 */     this.helpAction = new HelpAction(this.parent, "Help topics...", imageIcon7, this.browserPath, this.initialHelpPage);
/*      */     
/*  340 */     jMenuItem = this.helpMenu.add((Action)this.helpAction);
/*  341 */     jMenuItem.setMnemonic('H');
/*  342 */     AboutAction aboutAction = new AboutAction("About...");
/*  343 */     jMenuItem = this.helpMenu.add(aboutAction);
/*  344 */     jMenuItem.setMnemonic('A');
/*      */ 
/*      */     
/*  347 */     this.toolBar = new ToolBar("Data tool bar", new Dimension(30, 30));
/*      */     
/*  349 */     this.toolBar.setFloatable(true);
/*      */ 
/*      */     
/*  352 */     this.copyAction.setEnabled(false);
/*  353 */     this.cutAction.setEnabled(false);
/*  354 */     this.clearAction.setEnabled(false);
/*  355 */     this.pasteAction.setEnabled(DataTable.copiedData.hasData());
/*      */ 
/*      */     
/*  358 */     if (getToolkit().getSystemClipboard().getContents(this) == null) {
/*  359 */       this.pasteClipboardAction.setEnabled(false);
/*      */     }
/*      */     
/*  362 */     this.toolBar.add(this.cutAction, "Cut selected cells");
/*      */ 
/*      */     
/*  365 */     this.toolBar.add(this.copyAction, "Copy selected cells");
/*      */ 
/*      */     
/*  368 */     this.toolBar.add(this.pasteAction, "Paste data into cells");
/*      */ 
/*      */     
/*  371 */     this.toolBar.addSeparator();
/*  372 */     this.toolBar.add(insertRowAction, "Insert new row");
/*      */ 
/*      */     
/*  375 */     this.toolBar.add(insertColumnAction, "Add new column");
/*      */ 
/*      */     
/*  378 */     this.toolBar.addSeparator();
/*  379 */     this.toolBar.add(calculatorAction, "Calculate function of columns");
/*      */ 
/*      */ 
/*      */     
/*  383 */     this.toolBar.addSeparator();
/*  384 */     this.toolBar.add((Action)this.helpAction, "Help");
/*  385 */     setJMenuBar(this.menuBar);
/*      */     
/*  387 */     Container container = getContentPane();
/*  388 */     container.setLayout(new BorderLayout(1, 1));
/*  389 */     container.add((Component)this.toolBar, "North");
/*      */ 
/*      */ 
/*      */     
/*  393 */     ListSelectionModel listSelectionModel = this.dataTable.getSelectionModel();
/*  394 */     listSelectionModel.addListSelectionListener(new SelectionDebugger(listSelectionModel));
/*      */     
/*  396 */     JScrollPane jScrollPane = new JScrollPane(this.dataTable);
/*  397 */     container.add(jScrollPane, "Center");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  408 */     setCalculator(new StatisticalCalculator(this.parent, this.dataTable));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getCategoricalData(String paramString) {
/*  419 */     return this.dataTable.getCategoricalData(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnCount() {
/*  426 */     return this.dataTable.getColumnCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getColumnNames(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
/*  439 */     return this.dataTable.getColumnNames(paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer getDataAsStringBuffer(boolean paramBoolean, String paramString) {
/*  453 */     return this.dataTable.getDataAsStringBuffer(true, paramBoolean, paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataMatrix getDataMatrix() {
/*  460 */     return this.dataTable.dataModel.dataMatrix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataModel getDataModel() {
/*  469 */     return this.dataTable.dataModel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataTable getDataTable() {
/*  477 */     return this.dataTable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfPages() {
/*  484 */     return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double[] getNumericalData(String paramString) {
/*  494 */     return this.dataTable.getNumericalData(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double[][] getNumericalData(String[] paramArrayOfString) {
/*  505 */     return this.dataTable.getNumericalData(paramArrayOfString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRowCount() {
/*  517 */     return this.dataTable.getRowCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isChanged() {
/*  524 */     return this.dataTable.isChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DataMatrix read(File paramFile) {
/*  534 */     return DataFile.read(paramFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DataMatrix readTextFile(File paramFile, boolean paramBoolean, String paramString1, String paramString2) {
/*  560 */     return DataFile.readTextFile(paramFile, paramBoolean, paramString1, paramString2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBrowserPath(String paramString) {
/*  617 */     this.helpAction.setBrowserPath(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCalculator(DataCalculator paramDataCalculator) {
/*  628 */     this.calculator = paramDataCalculator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChanged(boolean paramBoolean) {
/*  635 */     this.dataTable.setChanged(paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitialHelpPage(String paramString) {
/*  645 */     this.helpAction.setInitialHelpPage(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean write(File paramFile) {
/*  692 */     return DataFile.write(paramFile, this.dataTable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean writeTextFile(File paramFile, boolean paramBoolean, String paramString) {
/*  707 */     return DataFile.writeTextFile(paramFile, this.dataTable, paramBoolean, paramString);
/*      */   }
/*      */   
/*      */   class AboutAction extends AbstractAction { public AboutAction(String param1String) {
/*  711 */       super(param1String);
/*      */     } private final DataWindow this$0;
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  714 */       JPanel jPanel = new JPanel(new GridLayout(2, 1));
/*  715 */       jPanel.add(new JLabel("Data Window V2.5", 0));
/*      */       
/*  717 */       jPanel.add(new JLabel("Copyright © 2004  A. J. Bertie", 0));
/*  718 */       JOptionPane.showMessageDialog(DataWindow.this.parent, jPanel, "About Data Window", -1, null);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class CalculateListener
/*      */     implements ActionListener
/*      */   {
/*      */     private final DataWindow this$0;
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  732 */       Vector vector = DataWindow.this.getColumnNames(true, true, false, 2);
/*  733 */       if (vector.isEmpty())
/*  734 */         return;  NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Calculation dialogue", "Select column to do calculation on", vector, 0);
/*      */       
/*  736 */       String[] arrayOfString = nameDialogue.showNames();
/*  737 */       if (arrayOfString == null)
/*  738 */         return;  String str = param1ActionEvent.getActionCommand();
/*  739 */       int i = -1;
/*  740 */       if (str.equals("Rank...")) {
/*      */         
/*  742 */         double[] arrayOfDouble = DataWindow.this.getNumericalData(arrayOfString[0]);
/*  743 */         Rank rank = new Rank(arrayOfDouble, 0.0D);
/*  744 */         i = DataWindow.this.dataTable.addColumn();
/*  745 */         DataWindow.this.dataTable.setData(rank.getRanks(), i, arrayOfString[0]);
/*      */       }
/*  747 */       else if (str.equals("Normal scores...")) {
/*      */         
/*  749 */         double[] arrayOfDouble1 = DataWindow.this.getNumericalData(arrayOfString[0]);
/*  750 */         Rank rank = new Rank(arrayOfDouble1, 0.0D);
/*  751 */         i = DataWindow.this.dataTable.addColumn();
/*  752 */         double[] arrayOfDouble2 = rank.getRanks();
/*  753 */         int j = rank.getN();
/*  754 */         double[] arrayOfDouble3 = new double[j];
/*  755 */         for (byte b = 0; b < j; b++) {
/*      */ 
/*      */           
/*  758 */           double d = (arrayOfDouble2[b] - 0.375D) / (j + 0.25D);
/*  759 */           arrayOfDouble3[b] = 4.91D * (Math.pow(d, 0.14D) - Math.pow(1.0D - d, 0.14D));
/*      */         } 
/*  761 */         DataWindow.this.dataTable.setData(arrayOfDouble3, i, arrayOfString[0]);
/*      */       }
/*  763 */       else if (str.equals("Square...")) {
/*  764 */         i = DataWindow.this.dataTable.transform(arrayOfString[0], 1);
/*  765 */       } else if (str.equals("Square root...")) {
/*  766 */         i = DataWindow.this.dataTable.transform(arrayOfString[0], 2);
/*  767 */       } else if (str.equals("Log...")) {
/*  768 */         i = DataWindow.this.dataTable.transform(arrayOfString[0], 3);
/*      */       
/*      */       }
/*  771 */       else if (str.equals("Reciprocal root...")) {
/*  772 */         i = DataWindow.this.dataTable.transform(arrayOfString[0], 4);
/*  773 */       } else if (str.equals("Reciprocal...")) {
/*  774 */         i = DataWindow.this.dataTable.transform(arrayOfString[0], 5);
/*      */       } 
/*      */       
/*  777 */       DataWindow.this.dataTable.setColumnName(i, str.substring(0, str.length() - 3) + " of  " + arrayOfString[0]);
/*  778 */       DataWindow.this.calculator.updateNames();
/*      */     } }
/*      */   
/*      */   class CalculatorAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*      */     public CalculatorAction(String param1String, Icon param1Icon) {
/*  784 */       super(param1String, param1Icon);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  789 */       DataWindow.this.calculator.updateNames();
/*  790 */       DataWindow.this.calculator.show();
/*      */     } }
/*      */ 
/*      */   
/*      */   class CategoricalListener implements ActionListener { private final DataWindow this$0;
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  797 */       DataWindow.this.dataTable.setDefaultColumnClass((DataWindow.class$java$lang$String == null) ? (DataWindow.class$java$lang$String = DataWindow.class$("java.lang.String")) : DataWindow.class$java$lang$String);
/*      */     } }
/*      */   class ChangeColumnTypeAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*      */     public ChangeColumnTypeAction(String param1String) {
/*  802 */       super(param1String);
/*      */     } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  804 */       DataWindow.this.dataTable.changeColumnType();
/*      */     } }
/*      */   
/*      */   class ClearAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*  809 */     public ClearAction(String param1String) { super(param1String); } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  810 */       DataWindow.this.dataTable.clear();
/*      */     } }
/*      */   
/*      */   class ContinuousListener implements ActionListener { private final DataWindow this$0;
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  816 */       DataWindow.this.dataTable.setDefaultColumnClass((DataWindow.class$java$lang$Double == null) ? (DataWindow.class$java$lang$Double = DataWindow.class$("java.lang.Double")) : DataWindow.class$java$lang$Double);
/*      */     } }
/*      */   class CopyAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*      */     public CopyAction(String param1String, Icon param1Icon) {
/*  821 */       super(param1String, param1Icon);
/*      */     }
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  824 */       DataWindow.this.dataTable.copy();
/*  825 */       DataWindow.this.pasteAction.setEnabled(true);
/*  826 */       DataWindow.this.pasteClipboardAction.setEnabled(true);
/*      */     } }
/*      */   
/*      */   class CutAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*      */     public CutAction(String param1String, Icon param1Icon) {
/*  832 */       super(param1String, param1Icon);
/*      */     }
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  835 */       DataWindow.this.dataTable.copy();
/*  836 */       DataWindow.this.dataTable.clear();
/*  837 */       DataWindow.this.pasteAction.setEnabled(true);
/*  838 */       DataWindow.this.pasteClipboardAction.setEnabled(true);
/*      */     } }
/*      */ 
/*      */   
/*      */   class DigitsListener
/*      */     implements ActionListener {
/*      */     private final DataWindow this$0;
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  847 */       int i = Integer.parseInt(param1ActionEvent.getActionCommand());
/*      */       
/*  849 */       DataWindow.this.dataTable.setSignificantDigits(i);
/*      */     }
/*      */   }
/*      */   
/*      */   class InsertColumnAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*  855 */     public InsertColumnAction(String param1String, Icon param1Icon) { super(param1String, param1Icon); } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  856 */       DataWindow.this.dataTable.addColumn();
/*      */     } }
/*      */   
/*      */   class InsertRowAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*  861 */     public InsertRowAction(String param1String, Icon param1Icon) { super(param1String, param1Icon); } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  862 */       DataWindow.this.dataTable.insertRow();
/*      */     } }
/*      */   
/*      */   class IntegerListener implements ActionListener { private final DataWindow this$0;
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  868 */       DataWindow.this.dataTable.setDefaultColumnClass((DataWindow.class$java$lang$Integer == null) ? (DataWindow.class$java$lang$Integer = DataWindow.class$("java.lang.Integer")) : DataWindow.class$java$lang$Integer);
/*      */     } }
/*      */   
/*      */   class PasteAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*  873 */     public PasteAction(String param1String, Icon param1Icon) { super(param1String, param1Icon); } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  874 */       DataWindow.this.dataTable.paste();
/*      */     } }
/*      */   
/*      */   class PasteClipboardAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*  879 */     public PasteClipboardAction(String param1String) { super(param1String); } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  880 */       DataWindow.this.dataTable.pasteFromSystemClipboard();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class RecodeAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private final DataWindow this$0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public RecodeAction(String param1String) {
/*  902 */       super(param1String);
/*      */     }
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  905 */       Vector vector1 = DataWindow.this.getColumnNames(true, true, true, 1);
/*  906 */       if (vector1.isEmpty())
/*  907 */         return;  NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Recode dialogue", "Select column to recode", vector1, 0);
/*      */       
/*  909 */       String[] arrayOfString1 = nameDialogue.showNames();
/*  910 */       if (arrayOfString1 == null)
/*      */         return; 
/*  912 */       String[] arrayOfString2 = DataWindow.this.getCategoricalData(arrayOfString1[0]);
/*  913 */       if (arrayOfString2.length < 1)
/*      */         return; 
/*  915 */       Vector vector2 = Sort.getLabels(arrayOfString2);
/*  916 */       RecodeModel recodeModel = new RecodeModel(vector2);
/*  917 */       RecodeTable recodeTable = new RecodeTable(recodeModel);
/*      */       
/*  919 */       JScrollPane jScrollPane = new JScrollPane(recodeTable);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  940 */       Dialogue dialogue = new Dialogue(DataWindow.this.parent, "Recode values of " + arrayOfString1[0], "Type new values into 2nd column. Select (drag down) rows to duplicate new values.", -1, 2);
/*      */ 
/*      */ 
/*      */       
/*  944 */       dialogue.add(jScrollPane, "Center");
/*  945 */       dialogue.setSize(300, 400);
/*  946 */       if (dialogue.show() == null) {
/*      */         return;
/*      */       }
/*  949 */       int i = DataWindow.this.dataTable.recode(arrayOfString1[0], recodeTable);
/*  950 */       DataWindow.this.dataTable.setColumnName(i, arrayOfString1[0] + " recoded");
/*  951 */       DataWindow.this.dataTable.promoteColumnClass(i);
/*      */     } }
/*      */   
/*      */   class RenameColumnAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*      */     public RenameColumnAction(String param1String) {
/*  957 */       super(param1String);
/*      */     } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  959 */       DataWindow.this.dataTable.editColumnName();
/*      */     } }
/*      */   
/*      */   class ResetColumnAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*  964 */     public ResetColumnAction(String param1String) { super(param1String); } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  965 */       DataWindow.this.dataTable.resetColumnOrder();
/*      */     } }
/*      */   
/*      */   class SelectAllAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*  970 */     public SelectAllAction(String param1String) { super(param1String); } public void actionPerformed(ActionEvent param1ActionEvent) {
/*  971 */       DataWindow.this.dataTable.selectAll();
/*      */     } }
/*      */   
/*      */   class SelectionCellsListener implements ActionListener {
/*      */     private final DataWindow this$0;
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  978 */       DataWindow.this.dataTable.setColumnSelectionAllowed(false);
/*  979 */       DataWindow.this.dataTable.setRowSelectionAllowed(false);
/*  980 */       DataWindow.this.dataTable.setCellSelectionEnabled(true);
/*  981 */       DataWindow.this.dataTable.clearSelection();
/*      */     }
/*      */   }
/*      */   
/*      */   class SelectionColumnsListener implements ActionListener {
/*      */     private final DataWindow this$0;
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/*  989 */       DataWindow.this.dataTable.setColumnSelectionAllowed(true);
/*  990 */       DataWindow.this.dataTable.setRowSelectionAllowed(false);
/*      */       
/*  992 */       DataWindow.this.dataTable.clearSelection();
/*      */     }
/*      */   }
/*      */   
/*      */   public class SelectionDebugger
/*      */     implements ListSelectionListener {
/*      */     ListSelectionModel model;
/*      */     private final DataWindow this$0;
/*      */     
/*      */     public SelectionDebugger(ListSelectionModel param1ListSelectionModel) {
/* 1002 */       this.model = param1ListSelectionModel;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
/* 1012 */       boolean bool = !this.model.isSelectionEmpty() ? true : false;
/* 1013 */       DataWindow.this.copyAction.setEnabled(bool);
/* 1014 */       DataWindow.this.cutAction.setEnabled(bool);
/* 1015 */       DataWindow.this.clearAction.setEnabled(bool);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class SelectionRowsListener
/*      */     implements ActionListener
/*      */   {
/*      */     private final DataWindow this$0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1035 */       DataWindow.this.dataTable.setColumnSelectionAllowed(false);
/* 1036 */       DataWindow.this.dataTable.setRowSelectionAllowed(true);
/*      */       
/* 1038 */       DataWindow.this.dataTable.clearSelection();
/*      */     }
/*      */   }
/*      */   
/*      */   class SelectNoneAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/* 1044 */     public SelectNoneAction(String param1String) { super(param1String); } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1045 */       DataWindow.this.dataTable.clearSelection();
/*      */     } }
/*      */   
/*      */   class ShowToolBarListener implements ActionListener { private final DataWindow this$0;
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1051 */       DataWindow.this.toolBar.setVisible(!DataWindow.this.toolBar.isVisible());
/*      */     } }
/*      */   class SortRowsAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*      */     public SortRowsAction(String param1String) {
/* 1056 */       super(param1String);
/*      */     }
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1059 */       Vector vector = DataWindow.this.getColumnNames(true, true, true, 2);
/* 1060 */       if (vector.isEmpty())
/* 1061 */         return;  NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Sort dialogue", "Select column to sort with", vector, 0);
/*      */ 
/*      */ 
/*      */       
/* 1065 */       ButtonGroup buttonGroup = new ButtonGroup();
/* 1066 */       JRadioButton jRadioButton1 = new JRadioButton("Ascending", true);
/* 1067 */       JRadioButton jRadioButton2 = new JRadioButton("Descending");
/* 1068 */       buttonGroup.add(jRadioButton1); buttonGroup.add(jRadioButton2);
/* 1069 */       JPanel jPanel = new JPanel();
/* 1070 */       jPanel.add(jRadioButton1); jPanel.add(jRadioButton2);
/* 1071 */       nameDialogue.add(jPanel, "South");
/*      */       
/* 1073 */       String[] arrayOfString = nameDialogue.showNames();
/* 1074 */       if (arrayOfString == null)
/* 1075 */         return;  DataWindow.this.dataTable.sortRows(arrayOfString[0], jRadioButton1.isSelected());
/*      */     } }
/*      */   
/*      */   class SubsetAction extends AbstractAction { private final DataWindow this$0;
/*      */     
/*      */     public SubsetAction(String param1String) {
/* 1081 */       super(param1String);
/*      */     }
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1084 */       Vector vector = DataWindow.this.getColumnNames(false, true, true, 2);
/* 1085 */       if (vector.isEmpty())
/* 1086 */         return;  NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Subset dialogue", "Select column to choose subset with", vector, 0);
/*      */       
/* 1088 */       String[] arrayOfString1 = nameDialogue.showNames();
/* 1089 */       if (arrayOfString1 == null)
/*      */         return; 
/* 1091 */       String[] arrayOfString2 = DataWindow.this.getCategoricalData(arrayOfString1[0]);
/* 1092 */       if (arrayOfString2.length < 1)
/*      */         return; 
/* 1094 */       Vector vector1 = Sort.getLabels(arrayOfString2);
/* 1095 */       JList jList = new JList(vector1);
/* 1096 */       JScrollPane jScrollPane = new JScrollPane(jList);
/*      */ 
/*      */       
/* 1099 */       Dialogue dialogue = new Dialogue(DataWindow.this.parent, "Subset using " + arrayOfString1[0], "Select values that define subset", -1, 2);
/*      */ 
/*      */ 
/*      */       
/* 1103 */       dialogue.add(jScrollPane, "Center");
/*      */       
/* 1105 */       ButtonGroup buttonGroup = new ButtonGroup();
/* 1106 */       JRadioButton jRadioButton1 = new JRadioButton("Include", true);
/* 1107 */       JRadioButton jRadioButton2 = new JRadioButton("Exclude");
/* 1108 */       buttonGroup.add(jRadioButton1); buttonGroup.add(jRadioButton2);
/* 1109 */       JPanel jPanel = new JPanel();
/* 1110 */       jPanel.add(jRadioButton1); jPanel.add(jRadioButton2);
/* 1111 */       dialogue.add(jPanel, "South");
/* 1112 */       if (dialogue.show() == null) {
/*      */         return;
/*      */       }
/* 1115 */       DataWindow.this.dataTable.subset(arrayOfString1[0], jList, jRadioButton1.isSelected());
/*      */     } }
/*      */ 
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/DataWindow.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */