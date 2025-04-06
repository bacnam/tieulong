/*      */ package jsc.swt.datatable;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.datatransfer.DataFlavor;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.text.ParseException;
/*      */ import java.util.Vector;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.DefaultCellEditor;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JRadioButtonMenuItem;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.table.JTableHeader;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ import jsc.Utilities;
/*      */ import jsc.mathfunction.MathFunctionException;
/*      */ import jsc.mathfunction.StatisticalMathFunction;
/*      */ import jsc.swt.control.IllegalCharactersField;
/*      */ import jsc.swt.control.PosIntegerField;
/*      */ import jsc.swt.control.RealField;
/*      */ import jsc.swt.text.RealFormat;
/*      */ import jsc.util.Maths;
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
/*      */ public class DataTable
/*      */   extends Table
/*      */ {
/*      */   public static final String FSDS = "\"";
/*      */   public static final String DELIMITERS = " ,\"\t\n\r";
/*      */   public static final String ROW_DELIMITERS = "\n\r";
/*      */   public static final String COL_DELIMITERS = " ,\"\t";
/*      */   public static final int SQUARE = 1;
/*      */   public static final int SQRT = 2;
/*      */   public static final int LOGE = 3;
/*      */   public static final int LOG10 = 6;
/*      */   public static final int RECIP_SQRT = 4;
/*      */   public static final int RECIP = 5;
/*  106 */   JTextField columnNameEditor = new JTextField();
/*      */   
/*      */   JTableHeader tableHeader;
/*      */   
/*      */   TableColumnModel columnModel;
/*      */   
/*      */   JTable tableView;
/*      */   
/*      */   DataModel dataModel;
/*      */   
/*      */   int clickedColumn;
/*      */   
/*      */   int lastRowInserted;
/*  119 */   final IllegalCharactersField illegalCharsField = new IllegalCharactersField(0, "\"");
/*  120 */   final PosIntegerField integerField = new PosIntegerField(0, 5);
/*      */   
/*  122 */   final RealField realField = new RealField(0.0D, 5, (RealFormat)this.realFormat);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataTable(DataModel paramDataModel) {
/*  131 */     super(paramDataModel);
/*      */     
/*  133 */     this.dataModel = paramDataModel;
/*  134 */     this.tableView = this;
/*  135 */     this.tableHeader = getTableHeader();
/*  136 */     this.columnModel = getColumnModel();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  144 */     this.columnNameEditor.setVisible(false);
/*  145 */     this.tableHeader.add(this.columnNameEditor);
/*      */     
/*  147 */     this.columnNameEditor.addFocusListener(new ColumnNameFocusHandler(this));
/*  148 */     this.columnNameEditor.addKeyListener(new ColumnNameEditorListener(this));
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
/*  166 */     this.integerField.setHorizontalAlignment(4);
/*  167 */     setDefaultEditor(Integer.class, new IntegerCellEditor(this));
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
/*  184 */     this.realField.setHorizontalAlignment(4);
/*  185 */     setDefaultEditor(Double.class, new DoubleCellEditor(this));
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
/*  196 */     setDefaultEditor(String.class, new StringCellEditor(this));
/*      */     
/*  198 */     addMouseListenerToHeaderInTable();
/*  199 */     this.lastRowInserted = getRowCount() - 1;
/*  200 */     setAutoResizeMode(0);
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
/*      */   public int addColumn() {
/*  214 */     int i = convertColumnIndexToView(this.dataModel.addColumn());
/*      */     
/*  216 */     setColumnName(i, getColumnName(i));
/*  217 */     return i;
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
/*      */   void addMouseListenerToHeaderInTable() {
/*  229 */     MouseAdapter mouseAdapter = new MouseAdapter(this) {
/*      */         private final DataTable this$0;
/*      */         
/*      */         public void mouseClicked(MouseEvent param1MouseEvent) {
/*  233 */           TableColumnModel tableColumnModel = this.this$0.getColumnModel();
/*  234 */           int i = tableColumnModel.getColumnIndexAtX(param1MouseEvent.getX());
/*      */           
/*  236 */           this.this$0.clickedColumn = i;
/*      */           
/*  238 */           if (this.this$0.clickedColumn == -1) {
/*      */             return;
/*      */           }
/*      */ 
/*      */           
/*  243 */           if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown()) {
/*      */             
/*  245 */             this.this$0.changeColumnType(this.this$0.clickedColumn, param1MouseEvent.getX(), param1MouseEvent.getY());
/*  246 */           } else if (SwingUtilities.isLeftMouseButton(param1MouseEvent) && param1MouseEvent.getClickCount() == 2) {
/*  247 */             this.this$0.editColumnName(this.this$0.clickedColumn);
/*      */           }  }
/*      */       };
/*  250 */     this.tableHeader.addMouseListener(mouseAdapter);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addRow() {
/*  255 */     this.dataModel.addRow();
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
/*      */   public int calculate(StatisticalMathFunction paramStatisticalMathFunction, CalculatorVariables paramCalculatorVariables) {
/*  267 */     int i = addColumn();
/*  268 */     setColumnClass(i, Double.class);
/*      */     
/*  270 */     for (byte b = 0; b < getRowCount(); b++) {
/*      */       double d;
/*  272 */       paramCalculatorVariables.setRowIndex(b); try {
/*  273 */         d = paramStatisticalMathFunction.eval();
/*      */       }
/*  275 */       catch (MathFunctionException mathFunctionException) {}
/*  276 */       if (!Double.isNaN(d))
/*  277 */         setValueAt(new Double(d), b, i); 
/*      */     } 
/*  279 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void changeColumnType() {
/*  287 */     changeColumnType(Math.max(0, getSelectedColumn()));
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
/*      */   public void changeColumnType(int paramInt) {
/*  299 */     Rectangle rectangle = this.tableHeader.getHeaderRect(paramInt);
/*  300 */     changeColumnType(paramInt, rectangle.x, rectangle.y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void changeColumnType(int paramInt1, int paramInt2, int paramInt3) {
/*  309 */     JPopupMenu jPopupMenu = new JPopupMenu("Column type");
/*  310 */     ButtonGroup buttonGroup = new ButtonGroup();
/*      */ 
/*      */     
/*  313 */     JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Categorical");
/*  314 */     jRadioButtonMenuItem1.setMnemonic('C');
/*  315 */     JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Continuous");
/*  316 */     jRadioButtonMenuItem2.setMnemonic('O');
/*      */ 
/*      */     
/*  319 */     JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Integer");
/*  320 */     jRadioButtonMenuItem3.setMnemonic('I');
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  325 */     jRadioButtonMenuItem1.addActionListener(new CategoricalListener(this));
/*  326 */     jRadioButtonMenuItem2.addActionListener(new ContinuousListener(this));
/*      */ 
/*      */     
/*  329 */     jRadioButtonMenuItem3.addActionListener(new IntegerListener(this));
/*      */ 
/*      */ 
/*      */     
/*  333 */     buttonGroup.add(jRadioButtonMenuItem1);
/*  334 */     buttonGroup.add(jRadioButtonMenuItem2);
/*  335 */     buttonGroup.add(jRadioButtonMenuItem3);
/*      */     
/*  337 */     jPopupMenu.add(jRadioButtonMenuItem1);
/*  338 */     jPopupMenu.add(jRadioButtonMenuItem2);
/*  339 */     jPopupMenu.add(jRadioButtonMenuItem3);
/*      */ 
/*      */ 
/*      */     
/*  343 */     Class clazz = getColumnClass(paramInt1);
/*      */ 
/*      */     
/*  346 */     if (clazz == String.class) {
/*  347 */       jRadioButtonMenuItem1.setSelected(true);
/*  348 */     } else if (clazz == Double.class) {
/*  349 */       jRadioButtonMenuItem2.setSelected(true);
/*  350 */     } else if (clazz == Integer.class) {
/*  351 */       jRadioButtonMenuItem3.setSelected(true);
/*      */     } 
/*  353 */     jPopupMenu.show(this.tableView, paramInt2, paramInt3);
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
/*      */   public void clear() {
/*  374 */     if (getCellSelectionEnabled()) { clearSelectedCells(); }
/*  375 */     else if (getColumnSelectionAllowed()) { clearSelectedColumns(); }
/*  376 */     else if (getRowSelectionAllowed()) { clearSelectedRows(); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   void clearSelectedCells() {
/*  382 */     int i = getRowCount();
/*  383 */     int j = getColumnCount();
/*  384 */     for (int k = i - 1; k >= 0; k--) {
/*  385 */       for (int m = j - 1; m >= 0; m--) {
/*  386 */         if (isCellSelected(k, m))
/*  387 */           this.dataModel.removeCell(k, convertColumnIndexToModel(m)); 
/*      */       } 
/*  389 */     }  this.dataModel.fireTableDataChanged();
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
/*      */   void clearSelectedColumns() {
/*  411 */     int i = getSelectedColumnCount();
/*  412 */     int[] arrayOfInt = getSelectedColumns();
/*      */     
/*  414 */     String[] arrayOfString = new String[i];
/*  415 */     for (byte b1 = 0; b1 < i; b1++)
/*  416 */       arrayOfString[b1] = getColumnName(arrayOfInt[b1]); 
/*  417 */     for (byte b2 = 0; b2 < i; b2++) {
/*  418 */       this.dataModel.removeColumn(arrayOfString[b2]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  427 */     this.dataModel.fireTableDataChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void clearSelectedRows() {
/*  433 */     int[] arrayOfInt = getSelectedRows();
/*  434 */     int i = getSelectedRowCount();
/*  435 */     for (int j = i - 1; j >= 0; j--)
/*  436 */       this.dataModel.removeRow(arrayOfInt[j]); 
/*  437 */     this.dataModel.fireTableDataChanged();
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
/*      */   private void convertDoubleToInteger(int paramInt) {
/*  455 */     for (byte b = 0; b < getRowCount(); b++) {
/*      */       
/*  457 */       Object object = getValueAt(b, paramInt);
/*  458 */       if (object instanceof Double) {
/*      */         
/*  460 */         Double double_ = (Double)object;
/*  461 */         int i = double_.intValue();
/*  462 */         if (i < 0) i = 0; 
/*  463 */         setValueAt(new Integer(i), b, paramInt);
/*      */       } 
/*      */     } 
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
/*      */   private void convertDoubleToString(int paramInt) {
/*  477 */     for (byte b = 0; b < getRowCount(); b++) {
/*      */       
/*  479 */       Object object = getValueAt(b, paramInt);
/*  480 */       if (object instanceof Double) {
/*      */         
/*  482 */         Double double_ = (Double)object;
/*  483 */         String str = this.realFormat.format(double_.doubleValue());
/*  484 */         setValueAt(str, b, paramInt);
/*      */       } 
/*      */     } 
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
/*      */   private void convertIntegerToDouble(int paramInt) {
/*  504 */     for (byte b = 0; b < getRowCount(); b++) {
/*      */       
/*  506 */       Object object = getValueAt(b, paramInt);
/*  507 */       if (object instanceof Integer) {
/*      */         
/*  509 */         Integer integer = (Integer)object;
/*  510 */         double d = integer.doubleValue();
/*  511 */         setValueAt(new Double(d), b, paramInt);
/*      */       } 
/*      */     } 
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
/*      */   private void convertIntegerToString(int paramInt) {
/*  525 */     for (byte b = 0; b < getRowCount(); b++) {
/*      */       
/*  527 */       Object object = getValueAt(b, paramInt);
/*  528 */       if (object instanceof Integer) {
/*      */         
/*  530 */         Integer integer = (Integer)object;
/*  531 */         String str = this.integerFormat.format(integer.intValue());
/*  532 */         setValueAt(str, b, paramInt);
/*      */       } 
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void convertStringToDouble(int paramInt) {
/*  600 */     for (byte b = 0; b < getRowCount(); b++) {
/*      */ 
/*      */       
/*  603 */       Object object = getValueAt(b, paramInt);
/*  604 */       if (object != null) {
/*  605 */         Number number; String str = object.toString(); 
/*  606 */         try { number = this.realFormat.parse(str); }
/*  607 */         catch (ParseException parseException) { return; }
/*  608 */          double d = number.doubleValue();
/*  609 */         setValueAt(new Double(d), b, paramInt);
/*      */       } 
/*      */     } 
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
/*      */   private void convertStringToInteger(int paramInt) {
/*  626 */     for (byte b = 0; b < getRowCount(); b++) {
/*      */       
/*  628 */       Object object = getValueAt(b, paramInt);
/*  629 */       if (object != null) {
/*  630 */         Number number; String str = object.toString(); 
/*  631 */         try { number = this.integerFormat.parse(str); }
/*  632 */         catch (ParseException parseException) { return; }
/*  633 */          int i = number.intValue();
/*  634 */         setValueAt(new Integer(i), b, paramInt);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void editColumnName() {
/*  645 */     editColumnName(Math.max(0, getSelectedColumn()));
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
/*      */   public void editColumnName(int paramInt) {
/*  659 */     Rectangle rectangle = this.tableHeader.getHeaderRect(paramInt);
/*  660 */     this.columnNameEditor.setBounds(rectangle);
/*  661 */     this.columnNameEditor.setText(getColumnName(paramInt));
/*  662 */     this.columnNameEditor.selectAll();
/*  663 */     this.columnNameEditor.setVisible(true);
/*  664 */     this.columnNameEditor.requestFocus();
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
/*      */   public String[] getCategoricalData(String paramString) {
/*  678 */     int i = getColumnIndex(paramString);
/*  679 */     int j = getRowCount();
/*  680 */     Vector vector = new Vector(j);
/*      */     byte b;
/*  682 */     for (b = 0; b < j; b++) {
/*      */       
/*  684 */       Object object = getValueAt(b, i);
/*  685 */       if (object != null) vector.addElement(object.toString()); 
/*      */     } 
/*  687 */     int k = vector.size();
/*  688 */     String[] arrayOfString = new String[k];
/*  689 */     for (b = 0; b < k; b++)
/*  690 */       arrayOfString[b] = vector.elementAt(b); 
/*  691 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class getColumnClass(String paramString) {
/*  701 */     return getColumnClass(getColumnIndex(paramString));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnIndex(String paramString) {
/*  711 */     return this.columnModel.getColumnIndex(paramString);
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
/*  724 */     return this.dataModel.getColumnNames(paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class getDefaultColumnClass() {
/*  732 */     return this.dataModel.getDefaultColumnClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDefaultNamePrefix() {
/*  739 */     return this.dataModel.getDefaultNamePrefix();
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
/*      */   public double[] getNumericalData(String paramString) {
/*  751 */     int i = getColumnIndex(paramString);
/*      */     
/*  753 */     int j = getRowCount();
/*  754 */     Vector vector = new Vector(j);
/*      */     byte b;
/*  756 */     for (b = 0; b < j; b++) {
/*      */       
/*  758 */       Object object = getValueAt(b, i);
/*  759 */       if (object instanceof Double) {
/*  760 */         vector.addElement(object);
/*  761 */       } else if (object instanceof Integer) {
/*      */         
/*  763 */         Integer integer = (Integer)object;
/*  764 */         vector.addElement(new Double(integer.doubleValue()));
/*      */       } 
/*      */     } 
/*  767 */     int k = vector.size();
/*  768 */     double[] arrayOfDouble = new double[k];
/*  769 */     for (b = 0; b < k; b++)
/*  770 */       arrayOfDouble[b] = ((Double)vector.elementAt(b)).doubleValue(); 
/*  771 */     return arrayOfDouble;
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
/*      */   public double[][] getNumericalData(String[] paramArrayOfString) {
/*  784 */     int i = paramArrayOfString.length;
/*  785 */     int j = getRowCount();
/*      */ 
/*      */     
/*  788 */     Vector vector = new Vector(j);
/*      */     
/*      */     byte b;
/*  791 */     for (b = 0; b < j; b++) {
/*      */       
/*  793 */       boolean bool = true;
/*  794 */       double[] arrayOfDouble1 = new double[i];
/*  795 */       for (byte b1 = 0; b1 < i; b1++) {
/*      */         
/*  797 */         int m = getColumnIndex(paramArrayOfString[b1]);
/*  798 */         double d = getNumericalValueAt(b, m);
/*  799 */         if (Double.isNaN(d)) {
/*  800 */           bool = false; break;
/*      */         } 
/*  802 */         arrayOfDouble1[b1] = d;
/*      */       } 
/*  804 */       if (bool) vector.addElement(arrayOfDouble1); 
/*      */     } 
/*  806 */     int k = vector.size();
/*  807 */     double[][] arrayOfDouble = new double[k][];
/*  808 */     for (b = 0; b < k; b++)
/*  809 */       arrayOfDouble[b] = vector.elementAt(b); 
/*  810 */     return arrayOfDouble;
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
/*      */   public double getNumericalValueAt(int paramInt1, int paramInt2) {
/*  823 */     Object object = getValueAt(paramInt1, paramInt2);
/*  824 */     if (object instanceof Double) {
/*      */       
/*  826 */       Double double_ = (Double)object;
/*  827 */       return double_.doubleValue();
/*      */     } 
/*  829 */     if (object instanceof Integer) {
/*      */       
/*  831 */       Integer integer = (Integer)object;
/*  832 */       return integer.doubleValue();
/*      */     } 
/*      */     
/*  835 */     return Double.NaN;
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
/*      */   public void insertColumn() {
/*  856 */     int i = getSelectedColumn();
/*  857 */     if (i < 0)
/*  858 */       return;  this.dataModel.insertColumn(++i);
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
/*      */   public void insertRow() {
/*  878 */     int i = getSelectedRow();
/*  879 */     if (i < 0) i = this.lastRowInserted; 
/*  880 */     this.lastRowInserted = this.dataModel.insertRow(++i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isChanged() {
/*  888 */     return this.dataModel.isChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isColumnDataDouble(int paramInt) {
/*  897 */     return this.dataModel.isColumnDataDouble(convertColumnIndexToModel(paramInt));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isColumnDataInteger(int paramInt) {
/*  906 */     return this.dataModel.isColumnDataInteger(convertColumnIndexToModel(paramInt));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertValue(Object paramObject, int paramInt1, int paramInt2) {
/*  916 */     this.dataModel.insertValue(paramObject, paramInt1, convertColumnIndexToModel(paramInt2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNameInUse(int paramInt, String paramString) {
/*  927 */     for (byte b = 0; b < getColumnCount(); b++) {
/*  928 */       if (paramString.equals(getColumnName(b)) && b != paramInt) return true; 
/*  929 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void paste() {
/*  938 */     switch (Table.copiedData.getCopyMode()) {
/*      */       case 3:
/*  940 */         pasteCells(Table.copiedData); break;
/*  941 */       case 2: pasteColumns(); break;
/*  942 */       case 1: pasteRows();
/*      */         break;
/*      */     } 
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
/*      */   public void pasteCells(DataMatrix paramDataMatrix) {
/*  960 */     int i = getSelectedRow();
/*  961 */     int j = getSelectedColumn();
/*  962 */     if (i < 0 || j < 0) {
/*      */       return;
/*      */     }
/*  965 */     int k = paramDataMatrix.getRowCount();
/*  966 */     if (k < 1)
/*  967 */       return;  int m = paramDataMatrix.getColumnCount();
/*      */ 
/*      */     
/*  970 */     int n = getColumnCount();
/*  971 */     int i1 = j + m;
/*  972 */     if (i1 > n) {
/*  973 */       for (byte b = 1; b <= i1 - n; ) { addColumn(); b++; }
/*      */     
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     byte b1;
/*      */ 
/*      */ 
/*      */     
/*  983 */     for (b1 = 1; b1 <= k; ) { addRow(); b1++; }
/*      */     
/*  985 */     for (byte b2 = 0; b2 < m; b2++) {
/*      */       
/*  987 */       int i2 = j + b2;
/*      */ 
/*      */       
/*  990 */       for (b1 = 0; b1 < k; b1++) {
/*      */         
/*  992 */         int i3 = i + b1;
/*      */         
/*  994 */         insertValue(paramDataMatrix.getValueAt(b1, b2), i3, i2);
/*      */       } 
/*      */ 
/*      */       
/*  998 */       Class clazz = paramDataMatrix.getColumnClass(b2);
/*  999 */       if (clazz == Integer.class && isColumnDataInteger(i2)) {
/*      */         
/* 1001 */         convertStringToInteger(i2);
/* 1002 */         setColumnClass(i2, Integer.class);
/*      */       }
/* 1004 */       else if (clazz == Double.class && isColumnDataDouble(i2)) {
/*      */         
/* 1006 */         convertStringToDouble(i2);
/* 1007 */         setColumnClass(i2, Double.class);
/*      */       } else {
/*      */         
/* 1010 */         setColumnClass(i2, String.class);
/*      */       } 
/* 1012 */     }  this.dataModel.trimTableRows(k);
/* 1013 */     this.dataModel.fireTableDataChanged();
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
/*      */   public void pasteColumns() {
/* 1026 */     int i = Table.copiedData.getRowCount();
/* 1027 */     if (i < 1)
/* 1028 */       return;  for (byte b = 0; b < Table.copiedData.getColumnCount(); b++) {
/*      */ 
/*      */ 
/*      */       
/* 1032 */       int j = addColumn();
/* 1033 */       setColumnClass(j, Table.copiedData.getColumnClass(b));
/* 1034 */       setColumnName(j, Table.copiedData.getColumnName(b));
/* 1035 */       for (byte b1 = 0; b1 < i; b1++) {
/* 1036 */         setValueAt(Table.copiedData.getValueAt(b1, b), b1, j);
/*      */       }
/*      */     } 
/*      */     
/* 1040 */     this.dataModel.fireTableDataChanged();
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
/*      */   public void pasteFromSystemClipboard() {
/* 1052 */     Clipboard clipboard = getToolkit().getSystemClipboard();
/* 1053 */     Transferable transferable = clipboard.getContents(this);
/*      */     
/*      */     try {
/* 1056 */       String str = (String)transferable.getTransferData(DataFlavor.stringFlavor);
/* 1057 */       DataMatrix dataMatrix = new DataMatrix(str, false, "\n\r", " ,\"\t", "");
/* 1058 */       pasteCells(dataMatrix);
/*      */     } catch (Exception exception) {
/*      */       
/* 1061 */       getToolkit().beep();
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void pasteRows() {
/* 1073 */     int i = getSelectedRow();
/* 1074 */     if (i < 0)
/* 1075 */       return;  int j = Table.copiedData.getColumnCount();
/*      */ 
/*      */     
/* 1078 */     int k = Table.copiedData.getRowCount();
/* 1079 */     if (k < 1)
/* 1080 */       return;  for (byte b = 0; b < k; b++) {
/*      */       
/* 1082 */       this.dataModel.insertRow(i);
/* 1083 */       for (byte b1 = 0; b1 < j; b1++) {
/* 1084 */         setValueAt(Table.copiedData.getValueAt(b, b1), i, b1);
/*      */       }
/* 1086 */       i++;
/*      */     } 
/* 1088 */     this.dataModel.fireTableDataChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void promoteColumnClass(int paramInt) {
/* 1097 */     Class clazz = getColumnClass(paramInt);
/* 1098 */     if (clazz == String.class) {
/*      */       
/* 1100 */       if (isColumnDataInteger(paramInt)) {
/*      */         
/* 1102 */         convertStringToInteger(paramInt);
/* 1103 */         setColumnClass(paramInt, Integer.class);
/*      */         return;
/*      */       } 
/* 1106 */       if (isColumnDataDouble(paramInt)) {
/*      */         
/* 1108 */         convertStringToDouble(paramInt);
/* 1109 */         setColumnClass(paramInt, Double.class);
/*      */         return;
/*      */       } 
/*      */     } 
/* 1113 */     if (clazz == Integer.class && isColumnDataDouble(paramInt)) {
/*      */       
/* 1115 */       convertIntegerToDouble(paramInt);
/* 1116 */       setColumnClass(paramInt, Double.class);
/*      */     } 
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
/*      */   public int recode(String paramString, RecodeTable paramRecodeTable) {
/* 1133 */     int i = addColumn();
/* 1134 */     setColumnClass(i, String.class);
/* 1135 */     int j = getRowCount();
/* 1136 */     int k = paramRecodeTable.getRowCount();
/* 1137 */     int m = getColumnIndex(paramString);
/*      */     
/* 1139 */     for (byte b = 0; b < j; b++) {
/*      */       
/* 1141 */       Object object = getValueAt(b, m);
/* 1142 */       if (object != null) {
/*      */ 
/*      */ 
/*      */         
/* 1146 */         for (byte b1 = 0; b1 < k; b1++) {
/*      */           
/* 1148 */           String str = object.toString();
/* 1149 */           Object object1 = paramRecodeTable.getValueAt(b1, 1);
/* 1150 */           if (object1 != null)
/*      */           {
/* 1152 */             if (str.equals(paramRecodeTable.getValueAt(b1, 0))) {
/* 1153 */               object = object1; break;
/*      */             }  } 
/*      */         } 
/* 1156 */         setValueAt(object, b, i);
/*      */       } 
/* 1158 */     }  return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChanged(boolean paramBoolean) {
/* 1166 */     this.dataModel.setChanged(paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColumnClass(int paramInt, Class paramClass) {
/* 1175 */     this.dataModel.setColumnClass(convertColumnIndexToModel(paramInt), paramClass);
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
/*      */   public void setColumnName(int paramInt, String paramString) {
/* 1187 */     String str1 = Utilities.deleteChars("\"", paramString);
/*      */ 
/*      */     
/* 1190 */     byte b = 0;
/* 1191 */     String str2 = str1.trim();
/* 1192 */     while (isNameInUse(paramInt, str2)) {
/* 1193 */       str2 = str1 + "-" + ++b;
/*      */     }
/* 1195 */     this.dataModel.setColumnName(convertColumnIndexToModel(paramInt), str2);
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
/*      */   void setColumnNameEdited(boolean paramBoolean) {
/* 1209 */     String str = this.columnNameEditor.getText();
/* 1210 */     setColumnName(this.clickedColumn, str);
/* 1211 */     if (!paramBoolean || this.clickedColumn >= getColumnCount() - 1) {
/* 1212 */       this.columnNameEditor.setVisible(false);
/*      */     } else {
/*      */       
/* 1215 */       this.clickedColumn++;
/* 1216 */       editColumnName(this.clickedColumn);
/*      */     } 
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
/*      */   public void setData(double[] paramArrayOfdouble, int paramInt, String paramString) {
/* 1237 */     setColumnClass(paramInt, Double.class);
/*      */     
/* 1239 */     int i = getRowCount();
/* 1240 */     if (paramString == null) {
/*      */       
/* 1242 */       for (byte b = 0; b < paramArrayOfdouble.length; b++)
/*      */       {
/* 1244 */         if (b >= i) addRow(); 
/* 1245 */         setValueAt(new Double(paramArrayOfdouble[b]), b, paramInt);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1250 */       byte b2 = 0;
/* 1251 */       int j = getColumnIndex(paramString);
/* 1252 */       for (byte b1 = 0; b1 < i; b1++) {
/*      */         
/* 1254 */         if (getValueAt(b1, j) != null) {
/* 1255 */           setValueAt(new Double(paramArrayOfdouble[b2++]), b1, paramInt);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultColumnClass(Class paramClass) {
/* 1267 */     this.dataModel.setDefaultColumnClass(paramClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEditable(boolean paramBoolean) {
/* 1274 */     this.dataModel.setEditable(paramBoolean);
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
/*      */   private void sort(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
/* 1308 */     int i = paramInt2, j = paramInt3;
/* 1309 */     Object object = getValueAt((paramInt2 + paramInt3) / 2, paramInt1);
/*      */     while (true) {
/* 1311 */       if (paramBoolean) {
/*      */         
/* 1313 */         for (; i < paramInt3 && Sort.compare(object, getValueAt(i, paramInt1), true) > 0; i++);
/* 1314 */         for (; j > paramInt2 && Sort.compare(object, getValueAt(j, paramInt1), true) < 0; j--);
/*      */       }
/*      */       else {
/*      */         
/* 1318 */         for (; i < paramInt3 && Sort.compare(object, getValueAt(i, paramInt1), false) < 0; i++);
/* 1319 */         for (; j > paramInt2 && Sort.compare(object, getValueAt(j, paramInt1), false) > 0; j--);
/*      */       } 
/* 1321 */       if (i < j) this.dataModel.swapRow(i, j); 
/* 1322 */       if (i <= j) { i++; j--; }
/* 1323 */        if (i > j) {
/* 1324 */         if (paramInt2 < j) sort(paramInt1, paramInt2, j, paramBoolean); 
/* 1325 */         if (i < paramInt3) sort(paramInt1, i, paramInt3, paramBoolean);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sortRows(String paramString, boolean paramBoolean) {
/* 1337 */     int i = getColumnIndex(paramString);
/* 1338 */     boolean bool = false;
/* 1339 */     int j = getRowCount() - 1;
/* 1340 */     this.dataModel.addRow();
/* 1341 */     sort(i, bool, j, paramBoolean);
/* 1342 */     this.dataModel.removeRow(getRowCount() - 1);
/* 1343 */     this.dataModel.fireTableDataChanged();
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
/*      */   public void subset(String paramString, JList paramJList, boolean paramBoolean) {
/* 1356 */     int i = getRowCount();
/* 1357 */     int j = getColumnIndex(paramString);
/* 1358 */     Object[] arrayOfObject = paramJList.getSelectedValues();
/* 1359 */     int k = arrayOfObject.length;
/*      */ 
/*      */     
/* 1362 */     for (int m = i - 1; m >= 0; m--) {
/*      */       
/* 1364 */       Object object = getValueAt(m, j);
/* 1365 */       if (object != null) {
/*      */ 
/*      */         
/* 1368 */         boolean bool = false;
/* 1369 */         for (byte b = 0; b < k; b++) {
/*      */           
/* 1371 */           String str = object.toString();
/* 1372 */           if (str.equals(arrayOfObject[b].toString())) {
/* 1373 */             bool = true; break;
/*      */           } 
/* 1375 */         }  if (paramBoolean && !bool) { this.dataModel.removeRow(m); }
/* 1376 */         else if (!paramBoolean && bool) { this.dataModel.removeRow(m); }
/*      */       
/*      */       } 
/*      */     } 
/* 1380 */     this.dataModel.fireTableDataChanged();
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
/*      */   public int transform(String paramString, int paramInt) {
/* 1395 */     int i = addColumn();
/* 1396 */     setColumnClass(i, Double.class);
/* 1397 */     int j = getRowCount();
/* 1398 */     int k = getColumnIndex(paramString);
/*      */     
/* 1400 */     double d = 0.0D;
/* 1401 */     for (byte b = 0; b < j; b++) {
/*      */       double d1;
/* 1403 */       Object object = getValueAt(b, k);
/* 1404 */       if (object instanceof Double) {
/* 1405 */         d1 = ((Double)object).doubleValue();
/* 1406 */       } else if (object instanceof Integer) {
/*      */         
/* 1408 */         Integer integer = (Integer)object;
/* 1409 */         d1 = integer.doubleValue();
/*      */       } else {
/*      */         continue;
/* 1412 */       }  switch (paramInt) {
/*      */         
/*      */         case 1:
/* 1415 */           d = d1 * d1;
/*      */         
/*      */         case 2:
/* 1418 */           if (d1 < 0.0D)
/* 1419 */             break;  d = Math.sqrt(d1);
/*      */         
/*      */         case 3:
/* 1422 */           if (d1 <= 0.0D)
/* 1423 */             break;  d = Math.log(d1);
/*      */         
/*      */         case 6:
/* 1426 */           if (d1 <= 0.0D)
/* 1427 */             break;  d = Maths.log10(d1);
/*      */         
/*      */         case 4:
/* 1430 */           if (d1 <= 0.0D)
/* 1431 */             break;  d = Math.sqrt(1.0D / d1);
/*      */         
/*      */         case 5:
/* 1434 */           if (d1 == 0.0D)
/* 1435 */             break;  d = 1.0D / d1;
/*      */         
/*      */         default:
/* 1438 */           setValueAt(new Double(d), b, i); break;
/*      */       }  continue;
/* 1440 */     }  return i;
/*      */   }
/*      */   
/*      */   public void updateData() {
/* 1444 */     this.dataModel.fireTableDataChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class CategoricalListener
/*      */     implements ActionListener
/*      */   {
/*      */     private final DataTable this$0;
/*      */ 
/*      */     
/*      */     CategoricalListener(DataTable this$0) {
/* 1456 */       this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1462 */       if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Double == null) ? (DataTable.class$java$lang$Double = DataTable.class$("java.lang.Double")) : DataTable.class$java$lang$Double)) {
/* 1463 */         this.this$0.convertDoubleToString(this.this$0.clickedColumn);
/* 1464 */       } else if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Integer == null) ? (DataTable.class$java$lang$Integer = DataTable.class$("java.lang.Integer")) : DataTable.class$java$lang$Integer)) {
/* 1465 */         this.this$0.convertIntegerToString(this.this$0.clickedColumn);
/* 1466 */       }  this.this$0.setColumnClass(this.this$0.clickedColumn, (DataTable.class$java$lang$String == null) ? (DataTable.class$java$lang$String = DataTable.class$("java.lang.String")) : DataTable.class$java$lang$String);
/*      */     } }
/*      */   
/*      */   class ColumnNameEditorListener extends KeyAdapter { private final DataTable this$0;
/*      */     
/*      */     ColumnNameEditorListener(DataTable this$0) {
/* 1472 */       this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */     
/*      */     public void keyPressed(KeyEvent param1KeyEvent) {
/* 1477 */       int i = param1KeyEvent.getKeyCode();
/*      */ 
/*      */ 
/*      */       
/* 1481 */       if (i == 10) { this.this$0.setColumnNameEdited(true); }
/* 1482 */       else if (i == 27) { this.this$0.setColumnNameEdited(false); }
/* 1483 */       else if (i == 18) { this.this$0.columnNameEditor.setVisible(false); }
/*      */       else
/*      */       { return; }
/*      */     
/*      */     } }
/*      */ 
/*      */   
/*      */   class ColumnNameFocusHandler extends FocusAdapter {
/*      */     private final DataTable this$0;
/*      */     
/*      */     ColumnNameFocusHandler(DataTable this$0) {
/* 1494 */       this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */     
/*      */     public void focusLost(FocusEvent param1FocusEvent) {
/* 1499 */       this.this$0.columnNameEditor.setVisible(false);
/*      */     } }
/*      */   class ContinuousListener implements ActionListener { ContinuousListener(DataTable this$0) {
/* 1502 */       this.this$0 = this$0;
/*      */     }
/*      */     private final DataTable this$0;
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1506 */       if (!this.this$0.isColumnDataDouble(this.this$0.clickedColumn))
/* 1507 */         return;  if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$String == null) ? (DataTable.class$java$lang$String = DataTable.class$("java.lang.String")) : DataTable.class$java$lang$String)) {
/* 1508 */         this.this$0.convertStringToDouble(this.this$0.clickedColumn);
/* 1509 */       } else if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Integer == null) ? (DataTable.class$java$lang$Integer = DataTable.class$("java.lang.Integer")) : DataTable.class$java$lang$Integer)) {
/* 1510 */         this.this$0.convertIntegerToDouble(this.this$0.clickedColumn);
/* 1511 */       }  this.this$0.setColumnClass(this.this$0.clickedColumn, (DataTable.class$java$lang$Double == null) ? (DataTable.class$java$lang$Double = DataTable.class$("java.lang.Double")) : DataTable.class$java$lang$Double);
/*      */     } }
/*      */   
/*      */   class DoubleCellEditor extends DefaultCellEditor { private final DataTable this$0;
/*      */     
/*      */     public DoubleCellEditor(DataTable this$0) {
/* 1517 */       super((JTextField)this$0.realField); this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getCellEditorValue() {
/* 1523 */       String str = this.this$0.realField.getText();
/* 1524 */       if (str.length() == 0) return null; 
/* 1525 */       return new Double(this.this$0.realField.getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Component getTableCellEditorComponent(JTable param1JTable, Object param1Object, boolean param1Boolean, int param1Int1, int param1Int2) {
/*      */       String str;
/* 1533 */       if (param1Object instanceof Double) {
/*      */         
/* 1535 */         double d = ((Double)param1Object).doubleValue();
/*      */ 
/*      */         
/* 1538 */         str = this.this$0.realFormat.format(d);
/*      */       } else {
/*      */         
/* 1541 */         str = "";
/*      */       } 
/*      */       
/* 1544 */       return super.getTableCellEditorComponent(param1JTable, str, param1Boolean, param1Int1, param1Int2);
/*      */     } }
/*      */   
/*      */   class IntegerCellEditor extends DefaultCellEditor { private final DataTable this$0;
/*      */     
/*      */     public IntegerCellEditor(DataTable this$0) {
/* 1550 */       super((JTextField)this$0.integerField); this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getCellEditorValue() {
/* 1556 */       String str = this.this$0.integerField.getText();
/* 1557 */       if (str.length() == 0) return null; 
/* 1558 */       return new Integer(this.this$0.integerField.getValue());
/*      */     } }
/*      */   
/*      */   class IntegerListener implements ActionListener { IntegerListener(DataTable this$0) {
/* 1562 */       this.this$0 = this$0;
/*      */     }
/*      */     private final DataTable this$0;
/*      */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 1566 */       if (!this.this$0.isColumnDataDouble(this.this$0.clickedColumn))
/* 1567 */         return;  if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$String == null) ? (DataTable.class$java$lang$String = DataTable.class$("java.lang.String")) : DataTable.class$java$lang$String)) {
/* 1568 */         this.this$0.convertStringToInteger(this.this$0.clickedColumn);
/* 1569 */       } else if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Double == null) ? (DataTable.class$java$lang$Double = DataTable.class$("java.lang.Double")) : DataTable.class$java$lang$Double)) {
/* 1570 */         this.this$0.convertDoubleToInteger(this.this$0.clickedColumn);
/* 1571 */       }  this.this$0.setColumnClass(this.this$0.clickedColumn, (DataTable.class$java$lang$Integer == null) ? (DataTable.class$java$lang$Integer = DataTable.class$("java.lang.Integer")) : DataTable.class$java$lang$Integer);
/*      */     } }
/*      */   
/*      */   class StringCellEditor extends DefaultCellEditor { private final DataTable this$0;
/*      */     
/*      */     public StringCellEditor(DataTable this$0) {
/* 1577 */       super((JTextField)this$0.illegalCharsField); this.this$0 = this$0;
/*      */     }
/*      */     
/*      */     public Object getCellEditorValue() {
/* 1581 */       String str = this.this$0.illegalCharsField.getText();
/* 1582 */       if (str.length() == 0) return null; 
/* 1583 */       return str;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/DataTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */