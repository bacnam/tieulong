/*     */ package jsc.swt.datatable;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.net.URL;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JRadioButtonMenuItem;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.event.InternalFrameAdapter;
/*     */ import javax.swing.event.InternalFrameEvent;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import jsc.swt.menu.DigitsMenu;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TableWindow
/*     */   extends JInternalFrame
/*     */ {
/*     */   public JMenuBar menuBar;
/*     */   public Table table;
/*     */   CopyAction copyAction;
/*     */   JInternalFrame parent;
/*     */   DataWindow dataWindow;
/*     */   
/*     */   public TableWindow(String paramString, Table paramTable, DataWindow paramDataWindow) {
/*  46 */     super(paramString, true, true, true, true);
/*  47 */     this.parent = this;
/*  48 */     this.dataWindow = paramDataWindow;
/*  49 */     this.table = paramTable;
/*  50 */     paramTable.setColumnSelectionAllowed(false);
/*  51 */     paramTable.setRowSelectionAllowed(false);
/*  52 */     paramTable.setCellSelectionEnabled(true);
/*     */ 
/*     */ 
/*     */     
/*  56 */     ImageIcon imageIcon = null;
/*  57 */     Class clazz = getClass();
/*  58 */     URL uRL = clazz.getResource("images/table.gif");
/*  59 */     if (uRL != null) imageIcon = new ImageIcon(uRL); 
/*  60 */     setFrameIcon(imageIcon);
/*  61 */     setDefaultCloseOperation(2);
/*     */     
/*  63 */     addInternalFrameListener(new TableWindowListener(this));
/*     */ 
/*     */     
/*  66 */     this.menuBar = new JMenuBar();
/*  67 */     this.menuBar.setBorder(new BevelBorder(0));
/*     */ 
/*     */     
/*  70 */     JMenu jMenu1 = new JMenu("Edit");
/*  71 */     this.menuBar.add(jMenu1);
/*  72 */     this.copyAction = new CopyAction(this, "Copy", new ImageIcon("images/copy.gif"));
/*  73 */     SelectAllAction selectAllAction = new SelectAllAction(this, "Select all");
/*  74 */     SelectNoneAction selectNoneAction = new SelectNoneAction(this, "Select none");
/*  75 */     ResetColumnAction resetColumnAction = new ResetColumnAction(this, "Reset column order");
/*     */     
/*  77 */     JMenuItem jMenuItem = jMenu1.add(this.copyAction);
/*  78 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2, false));
/*  79 */     jMenu1.addSeparator();
/*  80 */     jMenuItem = jMenu1.add(selectAllAction);
/*  81 */     jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2, false));
/*  82 */     jMenu1.add(selectNoneAction);
/*  83 */     jMenu1.addSeparator();
/*  84 */     jMenu1.add(resetColumnAction);
/*     */ 
/*     */     
/*  87 */     JMenu jMenu2 = new JMenu("Options");
/*  88 */     this.menuBar.add(jMenu2);
/*     */ 
/*     */ 
/*     */     
/*  92 */     JMenu jMenu3 = new JMenu("Selection");
/*  93 */     jMenu2.add(jMenu3);
/*  94 */     ButtonGroup buttonGroup = new ButtonGroup();
/*  95 */     JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Columns");
/*  96 */     JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Rows");
/*  97 */     JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Any cells", true);
/*     */     
/*  99 */     jRadioButtonMenuItem1.addActionListener(new SelectionColumnsListener(this));
/* 100 */     jRadioButtonMenuItem2.addActionListener(new SelectionRowsListener(this));
/* 101 */     jRadioButtonMenuItem3.addActionListener(new SelectionCellsListener(this));
/*     */     
/* 103 */     buttonGroup.add(jRadioButtonMenuItem1); buttonGroup.add(jRadioButtonMenuItem2); buttonGroup.add(jRadioButtonMenuItem3);
/* 104 */     jMenu3.add(jRadioButtonMenuItem1);
/* 105 */     jMenu3.add(jRadioButtonMenuItem2);
/* 106 */     jMenu3.add(jRadioButtonMenuItem3);
/* 107 */     DigitsMenu digitsMenu = new DigitsMenu("Significant figures", 16, paramTable.getSignificantDigits(), new DigitsListener(this));
/*     */     
/* 109 */     jMenu2.add((JMenuItem)digitsMenu);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     this.copyAction.setEnabled(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     setJMenuBar(this.menuBar);
/*     */     
/* 125 */     Container container = getContentPane();
/* 126 */     container.setLayout(new BorderLayout(1, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     ListSelectionModel listSelectionModel = paramTable.getSelectionModel();
/* 132 */     listSelectionModel.addListSelectionListener(new SelectionDebugger(this, listSelectionModel));
/*     */     
/* 134 */     JScrollPane jScrollPane = new JScrollPane(paramTable);
/* 135 */     container.add(jScrollPane, "Center");
/*     */   }
/*     */   class CopyAction extends AbstractAction { private final TableWindow this$0;
/*     */     
/*     */     public CopyAction(TableWindow this$0, String param1String, Icon param1Icon) {
/* 140 */       super(param1String, param1Icon); this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 143 */       this.this$0.table.copy();
/* 144 */       this.this$0.dataWindow.pasteAction.setEnabled(true);
/* 145 */       this.this$0.dataWindow.pasteClipboardAction.setEnabled(true);
/*     */     } }
/*     */   class DigitsListener implements ActionListener { private final TableWindow this$0;
/*     */     DigitsListener(TableWindow this$0) {
/* 149 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 154 */       int i = Integer.parseInt(param1ActionEvent.getActionCommand());
/* 155 */       this.this$0.table.setSignificantDigits(i);
/*     */     } }
/*     */ 
/*     */   
/*     */   class ResetColumnAction extends AbstractAction { private final TableWindow this$0;
/*     */     
/* 161 */     public ResetColumnAction(TableWindow this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 162 */       this.this$0.table.resetColumnOrder();
/*     */     } }
/*     */   
/*     */   class SelectAllAction extends AbstractAction { private final TableWindow this$0;
/*     */     
/* 167 */     public SelectAllAction(TableWindow this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 168 */       this.this$0.table.selectAll();
/*     */     } }
/*     */   class SelectionCellsListener implements ActionListener { SelectionCellsListener(TableWindow this$0) {
/* 171 */       this.this$0 = this$0;
/*     */     }
/*     */     private final TableWindow this$0;
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 175 */       this.this$0.table.setColumnSelectionAllowed(false);
/* 176 */       this.this$0.table.setRowSelectionAllowed(false);
/* 177 */       this.this$0.table.setCellSelectionEnabled(true);
/* 178 */       this.this$0.table.clearSelection();
/*     */     } }
/*     */   class SelectionColumnsListener implements ActionListener { private final TableWindow this$0;
/*     */     SelectionColumnsListener(TableWindow this$0) {
/* 182 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 186 */       this.this$0.table.setColumnSelectionAllowed(true);
/* 187 */       this.this$0.table.setRowSelectionAllowed(false);
/*     */       
/* 189 */       this.this$0.table.clearSelection();
/*     */     } }
/*     */ 
/*     */   
/*     */   class SelectionDebugger implements ListSelectionListener {
/*     */     ListSelectionModel model;
/*     */     private final TableWindow this$0;
/*     */     
/*     */     public SelectionDebugger(TableWindow this$0, ListSelectionModel param1ListSelectionModel) {
/* 198 */       this.this$0 = this$0;
/* 199 */       this.model = param1ListSelectionModel;
/*     */     }
/*     */     
/*     */     public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
/* 203 */       boolean bool = !this.model.isSelectionEmpty() ? true : false;
/* 204 */       this.this$0.copyAction.setEnabled(bool);
/*     */     } }
/*     */   class SelectionRowsListener implements ActionListener { private final TableWindow this$0;
/*     */     SelectionRowsListener(TableWindow this$0) {
/* 208 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 212 */       this.this$0.table.setColumnSelectionAllowed(false);
/* 213 */       this.this$0.table.setRowSelectionAllowed(true);
/*     */       
/* 215 */       this.this$0.table.clearSelection();
/*     */     } }
/*     */ 
/*     */   
/*     */   class SelectNoneAction extends AbstractAction { private final TableWindow this$0;
/*     */     
/* 221 */     public SelectNoneAction(TableWindow this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
/* 222 */       this.this$0.table.clearSelection();
/*     */     } }
/*     */ 
/*     */   
/*     */   class TableWindowListener
/*     */     extends InternalFrameAdapter {
/*     */     private final TableWindow this$0;
/*     */     
/*     */     TableWindowListener(TableWindow this$0) {
/* 231 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void internalFrameClosing(InternalFrameEvent param1InternalFrameEvent) {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/TableWindow.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */