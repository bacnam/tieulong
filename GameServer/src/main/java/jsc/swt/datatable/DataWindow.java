package jsc.swt.datatable;

import jsc.swt.control.ToolBar;
import jsc.swt.dialogue.Dialogue;
import jsc.swt.dialogue.NameDialogue;
import jsc.swt.help.HelpAction;
import jsc.swt.menu.DigitsMenu;
import jsc.util.Rank;
import jsc.util.Sort;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Vector;

public class DataWindow
        extends JInternalFrame {
    protected JMenuBar menuBar;
    protected JMenu helpMenu;
    protected ToolBar toolBar;
    protected DataTable dataTable;
    String initialHelpPage = "C:\\java\\jsc\\swt\\datatable\\help\\datatable.htm";

    String browserPath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";

    CopyAction copyAction;

    CutAction cutAction;

    ClearAction clearAction;

    PasteAction pasteAction;

    PasteClipboardAction pasteClipboardAction;

    JInternalFrame parent;

    DataCalculator calculator;

    ImageIcon windowIcon;

    HelpAction helpAction;

    public DataWindow(DataMatrix paramDataMatrix) {
        super(" Data", true, true, true, true);
        this.parent = this;

        DataModel dataModel = new DataModel(paramDataMatrix);

        this.dataTable = new DataTable(dataModel);

        this.dataTable.setColumnSelectionAllowed(false);
        this.dataTable.setRowSelectionAllowed(false);
        this.dataTable.setCellSelectionEnabled(true);

        ImageIcon imageIcon1 = null, imageIcon2 = null, imageIcon3 = null, imageIcon4 = null;
        ImageIcon imageIcon5 = null, imageIcon6 = null, imageIcon7 = null;
        Class clazz = getClass();
        URL uRL = clazz.getResource("images/table.gif");
        if (uRL != null) this.windowIcon = new ImageIcon(uRL);
        uRL = clazz.getResource("images/cut.gif");
        if (uRL != null) imageIcon1 = new ImageIcon(uRL);
        uRL = clazz.getResource("images/copy.gif");
        if (uRL != null) imageIcon2 = new ImageIcon(uRL);
        uRL = clazz.getResource("images/paste.gif");
        if (uRL != null) imageIcon3 = new ImageIcon(uRL);
        uRL = clazz.getResource("images/newrow.gif");
        if (uRL != null) imageIcon4 = new ImageIcon(uRL);
        uRL = clazz.getResource("images/newcol.gif");
        if (uRL != null) imageIcon5 = new ImageIcon(uRL);
        uRL = clazz.getResource("images/calculator.gif");
        if (uRL != null) imageIcon6 = new ImageIcon(uRL);
        uRL = clazz.getResource("images/help.gif");
        if (uRL != null) imageIcon7 = new ImageIcon(uRL);

        setFrameIcon(this.windowIcon);

        setClosable(false);

        this.menuBar = new JMenuBar();
        this.menuBar.setBorder(new BevelBorder(0));

        JMenu jMenu1 = new JMenu("Edit");
        jMenu1.setMnemonic('E');
        this.menuBar.add(jMenu1);
        this.cutAction = new CutAction("Cut", imageIcon1);
        this.clearAction = new ClearAction("Delete");
        this.copyAction = new CopyAction("Copy", imageIcon2);
        this.pasteAction = new PasteAction("Paste", imageIcon3);
        this.pasteClipboardAction = new PasteClipboardAction("Paste from system");
        SelectAllAction selectAllAction = new SelectAllAction("Select all");
        SelectNoneAction selectNoneAction = new SelectNoneAction("Select none");
        InsertRowAction insertRowAction = new InsertRowAction("Insert row", imageIcon4);
        InsertColumnAction insertColumnAction = new InsertColumnAction("Add column", imageIcon5);
        ResetColumnAction resetColumnAction = new ResetColumnAction("Reset column order");

        JMenuItem jMenuItem = jMenu1.add(this.cutAction);
        jMenuItem.setMnemonic('T');
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 2, false));
        jMenuItem = jMenu1.add(this.copyAction);
        jMenuItem.setMnemonic('C');
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2, false));
        jMenuItem = jMenu1.add(this.pasteAction);
        jMenuItem.setMnemonic('P');
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 2, false));
        jMenuItem = jMenu1.add(this.pasteClipboardAction);
        jMenuItem.setMnemonic('Y');
        jMenuItem = jMenu1.add(this.clearAction);
        jMenuItem.setMnemonic('L');
        jMenu1.addSeparator();
        jMenuItem = jMenu1.add(selectAllAction);
        jMenuItem.setMnemonic('S');
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2, false));
        jMenuItem = jMenu1.add(selectNoneAction);
        jMenuItem.setMnemonic('N');
        jMenu1.addSeparator();
        jMenuItem = jMenu1.add(insertRowAction);
        jMenuItem.setMnemonic('I');
        jMenuItem = jMenu1.add(insertColumnAction);
        jMenuItem.setMnemonic('A');
        jMenuItem = jMenu1.add(resetColumnAction);
        jMenuItem.setMnemonic('O');
        jMenu1.addSeparator();
        RenameColumnAction renameColumnAction = new RenameColumnAction("Rename column");
        jMenuItem = jMenu1.add(renameColumnAction);
        jMenuItem.setMnemonic('R');
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(82, 2, false));
        ChangeColumnTypeAction changeColumnTypeAction = new ChangeColumnTypeAction("Change column type");
        jMenuItem = jMenu1.add(changeColumnTypeAction);
        jMenuItem.setMnemonic('H');
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(72, 2, false));

        JMenu jMenu2 = new JMenu("Manipulate");
        jMenu2.setMnemonic('M');
        this.menuBar.add(jMenu2);
        SortRowsAction sortRowsAction = new SortRowsAction("Sort rows...");
        jMenuItem = jMenu2.add(sortRowsAction);
        jMenuItem.setMnemonic('S');
        RecodeAction recodeAction = new RecodeAction("Recode...");
        jMenuItem = jMenu2.add(recodeAction);
        jMenuItem.setMnemonic('R');
        SubsetAction subsetAction = new SubsetAction("Subset...");
        jMenuItem = jMenu2.add(subsetAction);
        jMenuItem.setMnemonic('U');

        JMenu jMenu3 = new JMenu("Calculate");
        jMenu3.setMnemonic('C');
        this.menuBar.add(jMenu3);

        CalculatorAction calculatorAction = new CalculatorAction("Calculator...", imageIcon6);
        jMenuItem = jMenu3.add(calculatorAction);
        jMenuItem.setMnemonic('C');
        jMenu3.addSeparator();
        CalculateListener calculateListener = new CalculateListener();
        jMenuItem = jMenu3.add("Square...");
        jMenuItem.setMnemonic('S');
        jMenuItem.addActionListener(calculateListener);
        jMenuItem = jMenu3.add("Square root...");
        jMenuItem.setMnemonic('Q');
        jMenuItem.addActionListener(calculateListener);
        jMenuItem = jMenu3.add("Log...");
        jMenuItem.setMnemonic('L');
        jMenuItem.addActionListener(calculateListener);

        jMenuItem = jMenu3.add("Reciprocal root...");
        jMenuItem.setMnemonic('R');
        jMenuItem.addActionListener(calculateListener);
        jMenuItem = jMenu3.add("Reciprocal...");
        jMenuItem.setMnemonic('E');
        jMenuItem.addActionListener(calculateListener);
        jMenu3.addSeparator();
        jMenuItem = jMenu3.add("Rank...");
        jMenuItem.setMnemonic('K');
        jMenuItem.addActionListener(calculateListener);
        jMenuItem = jMenu3.add("Normal scores...");
        jMenuItem.setMnemonic('N');
        jMenuItem.addActionListener(calculateListener);

        JMenu jMenu4 = new JMenu("Options");
        jMenu4.setMnemonic('O');
        this.menuBar.add(jMenu4);
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Show toolbar", true);
        jCheckBoxMenuItem.setMnemonic('T');
        jCheckBoxMenuItem.addActionListener(new ShowToolBarListener());
        jMenu4.add(jCheckBoxMenuItem);

        JMenu jMenu5 = new JMenu("Selection");
        jMenu5.setMnemonic('S');
        jMenu4.add(jMenu5);
        ButtonGroup buttonGroup1 = new ButtonGroup();
        JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Columns");
        jRadioButtonMenuItem1.setMnemonic('C');
        JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Rows");
        jRadioButtonMenuItem2.setMnemonic('R');
        JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Any cells", true);
        jRadioButtonMenuItem3.setMnemonic('A');

        jRadioButtonMenuItem1.addActionListener(new SelectionColumnsListener());
        jRadioButtonMenuItem2.addActionListener(new SelectionRowsListener());
        jRadioButtonMenuItem3.addActionListener(new SelectionCellsListener());

        buttonGroup1.add(jRadioButtonMenuItem1);
        buttonGroup1.add(jRadioButtonMenuItem2);
        buttonGroup1.add(jRadioButtonMenuItem3);
        jMenu5.add(jRadioButtonMenuItem1);
        jMenu5.add(jRadioButtonMenuItem2);
        jMenu5.add(jRadioButtonMenuItem3);

        DigitsMenu digitsMenu = new DigitsMenu("Significant figures", 16, this.dataTable.getSignificantDigits(), new DigitsListener());

        digitsMenu.setMnemonic('I');

        jMenu4.add((JMenuItem) digitsMenu);

        JMenu jMenu6 = new JMenu("Default column type");
        jMenu6.setMnemonic('D');
        ButtonGroup buttonGroup2 = new ButtonGroup();
        Class clazz1 = paramDataMatrix.getDefaultColumnClass();
        JRadioButtonMenuItem jRadioButtonMenuItem4 = new JRadioButtonMenuItem("Categorical", (clazz1 == String.class));
        jRadioButtonMenuItem4.setMnemonic('C');
        JRadioButtonMenuItem jRadioButtonMenuItem5 = new JRadioButtonMenuItem("Continuous", (clazz1 == Double.class));
        jRadioButtonMenuItem5.setMnemonic('O');
        JRadioButtonMenuItem jRadioButtonMenuItem6 = new JRadioButtonMenuItem("Integer", (clazz1 == Integer.class));
        jRadioButtonMenuItem6.setMnemonic('I');
        jRadioButtonMenuItem4.addActionListener(new CategoricalListener());
        jRadioButtonMenuItem5.addActionListener(new ContinuousListener());
        jRadioButtonMenuItem6.addActionListener(new IntegerListener());
        buttonGroup2.add(jRadioButtonMenuItem4);
        buttonGroup2.add(jRadioButtonMenuItem5);
        buttonGroup2.add(jRadioButtonMenuItem6);
        jMenu6.add(jRadioButtonMenuItem4);
        jMenu6.add(jRadioButtonMenuItem5);
        jMenu6.add(jRadioButtonMenuItem6);
        jMenu4.add(jMenu6);

        this.helpMenu = new JMenu("Help");
        this.helpMenu.setMnemonic('H');
        this.menuBar.add(this.helpMenu);
        this.helpAction = new HelpAction(this.parent, "Help topics...", imageIcon7, this.browserPath, this.initialHelpPage);

        jMenuItem = this.helpMenu.add((Action) this.helpAction);
        jMenuItem.setMnemonic('H');
        AboutAction aboutAction = new AboutAction("About...");
        jMenuItem = this.helpMenu.add(aboutAction);
        jMenuItem.setMnemonic('A');

        this.toolBar = new ToolBar("Data tool bar", new Dimension(30, 30));

        this.toolBar.setFloatable(true);

        this.copyAction.setEnabled(false);
        this.cutAction.setEnabled(false);
        this.clearAction.setEnabled(false);
        this.pasteAction.setEnabled(DataTable.copiedData.hasData());

        if (getToolkit().getSystemClipboard().getContents(this) == null) {
            this.pasteClipboardAction.setEnabled(false);
        }

        this.toolBar.add(this.cutAction, "Cut selected cells");

        this.toolBar.add(this.copyAction, "Copy selected cells");

        this.toolBar.add(this.pasteAction, "Paste data into cells");

        this.toolBar.addSeparator();
        this.toolBar.add(insertRowAction, "Insert new row");

        this.toolBar.add(insertColumnAction, "Add new column");

        this.toolBar.addSeparator();
        this.toolBar.add(calculatorAction, "Calculate function of columns");

        this.toolBar.addSeparator();
        this.toolBar.add((Action) this.helpAction, "Help");
        setJMenuBar(this.menuBar);

        Container container = getContentPane();
        container.setLayout(new BorderLayout(1, 1));
        container.add((Component) this.toolBar, "North");

        ListSelectionModel listSelectionModel = this.dataTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SelectionDebugger(listSelectionModel));

        JScrollPane jScrollPane = new JScrollPane(this.dataTable);
        container.add(jScrollPane, "Center");

        setCalculator(new StatisticalCalculator(this.parent, this.dataTable));
    }

    public static DataMatrix read(File paramFile) {
        return DataFile.read(paramFile);
    }

    public static DataMatrix readTextFile(File paramFile, boolean paramBoolean, String paramString1, String paramString2) {
        return DataFile.readTextFile(paramFile, paramBoolean, paramString1, paramString2);
    }

    public String[] getCategoricalData(String paramString) {
        return this.dataTable.getCategoricalData(paramString);
    }

    public int getColumnCount() {
        return this.dataTable.getColumnCount();
    }

    public Vector getColumnNames(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
        return this.dataTable.getColumnNames(paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
    }

    public StringBuffer getDataAsStringBuffer(boolean paramBoolean, String paramString) {
        return this.dataTable.getDataAsStringBuffer(true, paramBoolean, paramString);
    }

    public DataMatrix getDataMatrix() {
        return this.dataTable.dataModel.dataMatrix;
    }

    public DataModel getDataModel() {
        return this.dataTable.dataModel;
    }

    public DataTable getDataTable() {
        return this.dataTable;
    }

    public int getNumberOfPages() {
        return 2;
    }

    public double[] getNumericalData(String paramString) {
        return this.dataTable.getNumericalData(paramString);
    }

    public double[][] getNumericalData(String[] paramArrayOfString) {
        return this.dataTable.getNumericalData(paramArrayOfString);
    }

    public int getRowCount() {
        return this.dataTable.getRowCount();
    }

    public boolean isChanged() {
        return this.dataTable.isChanged();
    }

    public void setChanged(boolean paramBoolean) {
        this.dataTable.setChanged(paramBoolean);
    }

    public void setBrowserPath(String paramString) {
        this.helpAction.setBrowserPath(paramString);
    }

    public void setCalculator(DataCalculator paramDataCalculator) {
        this.calculator = paramDataCalculator;
    }

    public void setInitialHelpPage(String paramString) {
        this.helpAction.setInitialHelpPage(paramString);
    }

    public boolean write(File paramFile) {
        return DataFile.write(paramFile, this.dataTable);
    }

    public boolean writeTextFile(File paramFile, boolean paramBoolean, String paramString) {
        return DataFile.writeTextFile(paramFile, this.dataTable, paramBoolean, paramString);
    }

    class AboutAction extends AbstractAction {
        private final DataWindow this$0;

        public AboutAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            JPanel jPanel = new JPanel(new GridLayout(2, 1));
            jPanel.add(new JLabel("Data Window V2.5", 0));

            jPanel.add(new JLabel("Copyright © 2004  A. J. Bertie", 0));
            JOptionPane.showMessageDialog(DataWindow.this.parent, jPanel, "About Data Window", -1, null);
        }
    }

    class CalculateListener
            implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            Vector vector = DataWindow.this.getColumnNames(true, true, false, 2);
            if (vector.isEmpty())
                return;
            NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Calculation dialogue", "Select column to do calculation on", vector, 0);

            String[] arrayOfString = nameDialogue.showNames();
            if (arrayOfString == null)
                return;
            String str = param1ActionEvent.getActionCommand();
            int i = -1;
            if (str.equals("Rank...")) {

                double[] arrayOfDouble = DataWindow.this.getNumericalData(arrayOfString[0]);
                Rank rank = new Rank(arrayOfDouble, 0.0D);
                i = DataWindow.this.dataTable.addColumn();
                DataWindow.this.dataTable.setData(rank.getRanks(), i, arrayOfString[0]);
            } else if (str.equals("Normal scores...")) {

                double[] arrayOfDouble1 = DataWindow.this.getNumericalData(arrayOfString[0]);
                Rank rank = new Rank(arrayOfDouble1, 0.0D);
                i = DataWindow.this.dataTable.addColumn();
                double[] arrayOfDouble2 = rank.getRanks();
                int j = rank.getN();
                double[] arrayOfDouble3 = new double[j];
                for (byte b = 0; b < j; b++) {

                    double d = (arrayOfDouble2[b] - 0.375D) / (j + 0.25D);
                    arrayOfDouble3[b] = 4.91D * (Math.pow(d, 0.14D) - Math.pow(1.0D - d, 0.14D));
                }
                DataWindow.this.dataTable.setData(arrayOfDouble3, i, arrayOfString[0]);
            } else if (str.equals("Square...")) {
                i = DataWindow.this.dataTable.transform(arrayOfString[0], 1);
            } else if (str.equals("Square root...")) {
                i = DataWindow.this.dataTable.transform(arrayOfString[0], 2);
            } else if (str.equals("Log...")) {
                i = DataWindow.this.dataTable.transform(arrayOfString[0], 3);

            } else if (str.equals("Reciprocal root...")) {
                i = DataWindow.this.dataTable.transform(arrayOfString[0], 4);
            } else if (str.equals("Reciprocal...")) {
                i = DataWindow.this.dataTable.transform(arrayOfString[0], 5);
            }

            DataWindow.this.dataTable.setColumnName(i, str.substring(0, str.length() - 3) + " of  " + arrayOfString[0]);
            DataWindow.this.calculator.updateNames();
        }
    }

    class CalculatorAction extends AbstractAction {
        private final DataWindow this$0;

        public CalculatorAction(String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.calculator.updateNames();
            DataWindow.this.calculator.show();
        }
    }

    class CategoricalListener implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.setDefaultColumnClass((DataWindow.class$java$lang$String == null) ? (DataWindow.class$java$lang$String = DataWindow.class$("java.lang.String")) : DataWindow.class$java$lang$String);
        }
    }

    class ChangeColumnTypeAction extends AbstractAction {
        private final DataWindow this$0;

        public ChangeColumnTypeAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.changeColumnType();
        }
    }

    class ClearAction extends AbstractAction {
        private final DataWindow this$0;

        public ClearAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.clear();
        }
    }

    class ContinuousListener implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.setDefaultColumnClass((DataWindow.class$java$lang$Double == null) ? (DataWindow.class$java$lang$Double = DataWindow.class$("java.lang.Double")) : DataWindow.class$java$lang$Double);
        }
    }

    class CopyAction extends AbstractAction {
        private final DataWindow this$0;

        public CopyAction(String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.copy();
            DataWindow.this.pasteAction.setEnabled(true);
            DataWindow.this.pasteClipboardAction.setEnabled(true);
        }
    }

    class CutAction extends AbstractAction {
        private final DataWindow this$0;

        public CutAction(String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.copy();
            DataWindow.this.dataTable.clear();
            DataWindow.this.pasteAction.setEnabled(true);
            DataWindow.this.pasteClipboardAction.setEnabled(true);
        }
    }

    class DigitsListener
            implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            int i = Integer.parseInt(param1ActionEvent.getActionCommand());

            DataWindow.this.dataTable.setSignificantDigits(i);
        }
    }

    class InsertColumnAction extends AbstractAction {
        private final DataWindow this$0;

        public InsertColumnAction(String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.addColumn();
        }
    }

    class InsertRowAction extends AbstractAction {
        private final DataWindow this$0;

        public InsertRowAction(String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.insertRow();
        }
    }

    class IntegerListener implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.setDefaultColumnClass((DataWindow.class$java$lang$Integer == null) ? (DataWindow.class$java$lang$Integer = DataWindow.class$("java.lang.Integer")) : DataWindow.class$java$lang$Integer);
        }
    }

    class PasteAction extends AbstractAction {
        private final DataWindow this$0;

        public PasteAction(String param1String, Icon param1Icon) {
            super(param1String, param1Icon);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.paste();
        }
    }

    class PasteClipboardAction extends AbstractAction {
        private final DataWindow this$0;

        public PasteClipboardAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.pasteFromSystemClipboard();
        }
    }

    class RecodeAction
            extends AbstractAction {
        private final DataWindow this$0;

        public RecodeAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            Vector vector1 = DataWindow.this.getColumnNames(true, true, true, 1);
            if (vector1.isEmpty())
                return;
            NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Recode dialogue", "Select column to recode", vector1, 0);

            String[] arrayOfString1 = nameDialogue.showNames();
            if (arrayOfString1 == null)
                return;
            String[] arrayOfString2 = DataWindow.this.getCategoricalData(arrayOfString1[0]);
            if (arrayOfString2.length < 1)
                return;
            Vector vector2 = Sort.getLabels(arrayOfString2);
            RecodeModel recodeModel = new RecodeModel(vector2);
            RecodeTable recodeTable = new RecodeTable(recodeModel);

            JScrollPane jScrollPane = new JScrollPane(recodeTable);

            Dialogue dialogue = new Dialogue(DataWindow.this.parent, "Recode values of " + arrayOfString1[0], "Type new values into 2nd column. Select (drag down) rows to duplicate new values.", -1, 2);

            dialogue.add(jScrollPane, "Center");
            dialogue.setSize(300, 400);
            if (dialogue.show() == null) {
                return;
            }
            int i = DataWindow.this.dataTable.recode(arrayOfString1[0], recodeTable);
            DataWindow.this.dataTable.setColumnName(i, arrayOfString1[0] + " recoded");
            DataWindow.this.dataTable.promoteColumnClass(i);
        }
    }

    class RenameColumnAction extends AbstractAction {
        private final DataWindow this$0;

        public RenameColumnAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.editColumnName();
        }
    }

    class ResetColumnAction extends AbstractAction {
        private final DataWindow this$0;

        public ResetColumnAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.resetColumnOrder();
        }
    }

    class SelectAllAction extends AbstractAction {
        private final DataWindow this$0;

        public SelectAllAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.selectAll();
        }
    }

    class SelectionCellsListener implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.setColumnSelectionAllowed(false);
            DataWindow.this.dataTable.setRowSelectionAllowed(false);
            DataWindow.this.dataTable.setCellSelectionEnabled(true);
            DataWindow.this.dataTable.clearSelection();
        }
    }

    class SelectionColumnsListener implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.setColumnSelectionAllowed(true);
            DataWindow.this.dataTable.setRowSelectionAllowed(false);

            DataWindow.this.dataTable.clearSelection();
        }
    }

    public class SelectionDebugger
            implements ListSelectionListener {
        private final DataWindow this$0;
        ListSelectionModel model;

        public SelectionDebugger(ListSelectionModel param1ListSelectionModel) {
            this.model = param1ListSelectionModel;
        }

        public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
            boolean bool = !this.model.isSelectionEmpty() ? true : false;
            DataWindow.this.copyAction.setEnabled(bool);
            DataWindow.this.cutAction.setEnabled(bool);
            DataWindow.this.clearAction.setEnabled(bool);
        }
    }

    class SelectionRowsListener
            implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.setColumnSelectionAllowed(false);
            DataWindow.this.dataTable.setRowSelectionAllowed(true);

            DataWindow.this.dataTable.clearSelection();
        }
    }

    class SelectNoneAction extends AbstractAction {
        private final DataWindow this$0;

        public SelectNoneAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.dataTable.clearSelection();
        }
    }

    class ShowToolBarListener implements ActionListener {
        private final DataWindow this$0;

        public void actionPerformed(ActionEvent param1ActionEvent) {
            DataWindow.this.toolBar.setVisible(!DataWindow.this.toolBar.isVisible());
        }
    }

    class SortRowsAction extends AbstractAction {
        private final DataWindow this$0;

        public SortRowsAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            Vector vector = DataWindow.this.getColumnNames(true, true, true, 2);
            if (vector.isEmpty())
                return;
            NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Sort dialogue", "Select column to sort with", vector, 0);

            ButtonGroup buttonGroup = new ButtonGroup();
            JRadioButton jRadioButton1 = new JRadioButton("Ascending", true);
            JRadioButton jRadioButton2 = new JRadioButton("Descending");
            buttonGroup.add(jRadioButton1);
            buttonGroup.add(jRadioButton2);
            JPanel jPanel = new JPanel();
            jPanel.add(jRadioButton1);
            jPanel.add(jRadioButton2);
            nameDialogue.add(jPanel, "South");

            String[] arrayOfString = nameDialogue.showNames();
            if (arrayOfString == null)
                return;
            DataWindow.this.dataTable.sortRows(arrayOfString[0], jRadioButton1.isSelected());
        }
    }

    class SubsetAction extends AbstractAction {
        private final DataWindow this$0;

        public SubsetAction(String param1String) {
            super(param1String);
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            Vector vector = DataWindow.this.getColumnNames(false, true, true, 2);
            if (vector.isEmpty())
                return;
            NameDialogue nameDialogue = new NameDialogue(DataWindow.this.parent, "Subset dialogue", "Select column to choose subset with", vector, 0);

            String[] arrayOfString1 = nameDialogue.showNames();
            if (arrayOfString1 == null)
                return;
            String[] arrayOfString2 = DataWindow.this.getCategoricalData(arrayOfString1[0]);
            if (arrayOfString2.length < 1)
                return;
            Vector vector1 = Sort.getLabels(arrayOfString2);
            JList jList = new JList(vector1);
            JScrollPane jScrollPane = new JScrollPane(jList);

            Dialogue dialogue = new Dialogue(DataWindow.this.parent, "Subset using " + arrayOfString1[0], "Select values that define subset", -1, 2);

            dialogue.add(jScrollPane, "Center");

            ButtonGroup buttonGroup = new ButtonGroup();
            JRadioButton jRadioButton1 = new JRadioButton("Include", true);
            JRadioButton jRadioButton2 = new JRadioButton("Exclude");
            buttonGroup.add(jRadioButton1);
            buttonGroup.add(jRadioButton2);
            JPanel jPanel = new JPanel();
            jPanel.add(jRadioButton1);
            jPanel.add(jRadioButton2);
            dialogue.add(jPanel, "South");
            if (dialogue.show() == null) {
                return;
            }
            DataWindow.this.dataTable.subset(arrayOfString1[0], jList, jRadioButton1.isSelected());
        }
    }

}

