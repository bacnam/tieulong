package jsc.swt.datatable;

import jsc.mathfunction.MathFunctionException;
import jsc.mathfunction.StatisticalMathFunction;
import jsc.swt.control.LegalCharactersField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class StatisticalCalculator
        extends DataCalculator {
    StatisticalMathFunction smf;
    CalculatorVariables cv;
    JOptionPane pane;
    JDialog dialog;
    Vector names;
    JList nameList;
    LegalCharactersField expression;
    JComboBox expressionCombo;
    JTextField errorMessage;

    public StatisticalCalculator(Component paramComponent, DataTable paramDataTable) {
        super(paramComponent, paramDataTable);

        this.nameList = new JList();
        this.expression = new LegalCharactersField(0, "");
        updateNames();

        JPanel jPanel1 = new JPanel(new BorderLayout(0, 2));

        this.expression.addActionListener(new CalcButtonListener(this));

        this.expressionCombo = new JComboBox();
        this.expressionCombo.setEditable(true);
        this.expressionCombo.setEditor(new ExpressionEditor(this));
        this.expressionCombo.setMaximumRowCount(10);
        this.errorMessage = new JTextField();
        this.errorMessage.setEditable(false);
        this.errorMessage.setFocusable(false);

        JPanel jPanel2 = new JPanel(new GridLayout(2, 1, 2, 2));

        jPanel2.add(this.expressionCombo);
        jPanel2.add(this.errorMessage);

        jPanel1.add(jPanel2, "North");

        JPanel jPanel3 = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);

        this.nameList.setSelectionMode(0);

        this.nameList.addMouseListener(new NameListMouseListener(this));
        this.nameList.addKeyListener(new NameListKeyListener(this));
        this.nameList.addFocusListener(new NameListFocusListener(this));

        JScrollPane jScrollPane = new JScrollPane(this.nameList);
        jScrollPane.setPreferredSize(new Dimension(150, 200));

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 2;
        jPanel3.add(jScrollPane, gridBagConstraints);

        ButtonListener buttonListener = new ButtonListener(this);

        Font font1 = new Font("Monospaced", 0, 14);
        JPanel jPanel4 = new JPanel(new GridLayout(7, 3, 2, 2));
        String[] arrayOfString1 = {"+", "-", "*", "/", "^", "%", "<", "<=", "=", ">", ">=", "~=", "|", "&", "~", "(", ")", " ", "pi", "e"};
        byte b;
        for (b = 0; b < arrayOfString1.length; b++) {

            JButton jButton = new JButton(arrayOfString1[b]);
            jButton.setFont(font1);
            jButton.addActionListener(buttonListener);
            if (b == 17) jButton.setVisible(false);
            jPanel4.add(jButton);
        }

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        jPanel3.add(jPanel4, gridBagConstraints);

        Font font2 = new Font("SanSerif", 0, 12);
        JPanel jPanel5 = new JPanel(new GridLayout(10, 2, 2, 2));
        String[] arrayOfString2 = {"log", "exp", "sqrt", "abs", "int", "nint", "sin", "asin", "cos", "acos", "tan", "atan", "deg", "rad", "sinh", "cosh", "tanh", "sign", "gam", "lgam"};

        for (b = 0; b < arrayOfString2.length; b++) {

            JButton jButton = new JButton(arrayOfString2[b].toUpperCase());
            jButton.setFont(font2);
            jButton.addActionListener(buttonListener);
            jPanel5.add(jButton);
        }
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 2;
        jPanel3.add(jPanel5, gridBagConstraints);

        JPanel jPanel6 = new JPanel(new GridLayout(4, 3, 2, 2));
        JButton[] arrayOfJButton = new JButton[10];
        for (b = 0; b <= 9; b++) {

            arrayOfJButton[b] = new JButton((new Integer(b)).toString());
            arrayOfJButton[b].addActionListener(buttonListener);
            arrayOfJButton[b].setFont(font2);
        }
        jPanel6.add(arrayOfJButton[7]);
        jPanel6.add(arrayOfJButton[8]);
        jPanel6.add(arrayOfJButton[9]);
        jPanel6.add(arrayOfJButton[4]);
        jPanel6.add(arrayOfJButton[5]);
        jPanel6.add(arrayOfJButton[6]);
        jPanel6.add(arrayOfJButton[1]);
        jPanel6.add(arrayOfJButton[2]);
        jPanel6.add(arrayOfJButton[3]);
        jPanel6.add(arrayOfJButton[0]);
        JButton jButton1 = new JButton(".");
        jButton1.addActionListener(buttonListener);
        jPanel6.add(jButton1);
        JButton jButton2 = new JButton("E");
        jButton2.addActionListener(buttonListener);
        jPanel6.add(jButton2);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        jPanel3.add(jPanel6, gridBagConstraints);

        jPanel1.add(jPanel3, "Center");

        JButton jButton3 = new JButton("Calculate");
        jButton3.addActionListener(new CalcButtonListener(this));
        JButton jButton4 = new JButton("Clear");
        jButton4.addActionListener(new ClearButtonListener(this));
        JButton jButton5 = new JButton("Close");
        jButton5.addActionListener(new CloseButtonListener(this));
        jButton5.setDefaultCapable(false);

        this.pane = new JOptionPane(jPanel1, -1, -1, null, new Object[]{jButton4, jButton3, jButton5});

        this.dialog = this.pane.createDialog(paramComponent, "Calculations on variables dialogue");
        this.dialog.setModal(false);
        this.dialog.setResizable(false);
    }

    void insertToken(String paramString) {
        int i = this.expression.getCaretPosition();
        StringBuffer stringBuffer = new StringBuffer(this.expression.getText());
        stringBuffer.insert(i, paramString);
        this.expression.setText(stringBuffer.toString());
    }

    public void setSize(int paramInt1, int paramInt2) {
        this.dialog.setSize(paramInt1, paramInt2);
        this.dialog.setLocationRelativeTo(this.parentComponent);
    }

    public void show() {
        if (this.expressionCombo.getItemCount() > 1) this.expression.setText("");
        this.errorMessage.setText("");
        this.dialog.show();
    }

    public void updateNames() {
        this.cv = new CalculatorVariables(this.dataTable);
        this.smf = new StatisticalMathFunction(this.cv);
        this.expression.setLegalCharacters(this.smf.getLegalCharacters());
        this.names = this.cv.getNames();
        this.nameList.setListData(this.names);
    }

    class ButtonListener implements ActionListener {
        private final StatisticalCalculator this$0;

        ButtonListener(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            String str = param1ActionEvent.getActionCommand();
            this.this$0.insertToken(str);
        }
    }

    class CalcButtonListener implements ActionListener {
        private final StatisticalCalculator this$0;

        CalcButtonListener(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            this.this$0.errorMessage.setText("");

            try {
                String str = this.this$0.expression.getText();
                double d = this.this$0.smf.parse(str);
                this.this$0.expressionCombo.addItem(str);

                this.this$0.expressionCombo.setSelectedIndex(this.this$0.expressionCombo.getItemCount() - 1);
                if (!Double.isNaN(d)) {
                    this.this$0.errorMessage.setText("Value of expression = " + d);
                } else {

                    int i = this.this$0.dataTable.calculate(this.this$0.smf, this.this$0.cv);

                    String str1 = this.this$0.smf.getParsedExpression();
                    this.this$0.dataTable.setColumnName(i, str1.replace('"', ' '));

                    this.this$0.errorMessage.setText(this.this$0.smf.getEvalCount() + " row calculations stored in new column " + (i + 1));

                    this.this$0.updateNames();
                }
            } catch (MathFunctionException mathFunctionException) {

                this.this$0.errorMessage.setText(mathFunctionException.getMessage());
            }
        }
    }

    class ClearButtonListener implements ActionListener {
        private final StatisticalCalculator this$0;

        ClearButtonListener(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            this.this$0.expression.setText("");
            this.this$0.errorMessage.setText("");
        }
    }

    class CloseButtonListener implements ActionListener {
        private final StatisticalCalculator this$0;

        CloseButtonListener(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            this.this$0.dialog.dispose();
        }
    }

    class ExpressionEditor implements ComboBoxEditor {
        private final StatisticalCalculator this$0;

        ExpressionEditor(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void addActionListener(ActionListener param1ActionListener) {
            this.this$0.expression.addActionListener(param1ActionListener);
        }

        public Component getEditorComponent() {
            return (Component) this.this$0.expression;
        }

        public Object getItem() {
            return this.this$0.expression.getText();
        }

        public void setItem(Object param1Object) {
            if (param1Object != null) {

                this.this$0.expression.setText(param1Object.toString());
                this.this$0.errorMessage.setText("");
            }
        }

        public void removeActionListener(ActionListener param1ActionListener) {
            this.this$0.expression.removeActionListener(param1ActionListener);
        }

        public void selectAll() {
            this.this$0.expression.selectAll();
        }
    }

    class NameListFocusListener extends FocusAdapter {
        private final StatisticalCalculator this$0;

        NameListFocusListener(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void focusGained(FocusEvent param1FocusEvent) {
            this.this$0.nameList.setSelectedIndex(0);
        }
    }

    class NameListKeyListener extends KeyAdapter {
        private final StatisticalCalculator this$0;

        NameListKeyListener(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void keyTyped(KeyEvent param1KeyEvent) {
            int i = this.this$0.nameList.getSelectedIndex();
            if (i < 0 || i >= this.this$0.names.size())
                return;
            Object object = this.this$0.names.elementAt(i);
            String str = "\"" + object.toString() + "\"";
            this.this$0.insertToken(str);
        }
    }

    class NameListMouseListener extends MouseAdapter {
        private final StatisticalCalculator this$0;

        NameListMouseListener(StatisticalCalculator this$0) {
            this.this$0 = this$0;
        }

        public void mouseClicked(MouseEvent param1MouseEvent) {
            if (param1MouseEvent.getClickCount() == 1) {

                int i = this.this$0.nameList.locationToIndex(param1MouseEvent.getPoint());
                if (i < 0 || i >= this.this$0.names.size())
                    return;
                Object object = this.this$0.names.elementAt(i);
                String str = "\"" + object.toString() + "\"";
                this.this$0.insertToken(str);
            }
        }
    }

}

