/*     */ package jsc.swt.datatable;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Vector;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ import jsc.mathfunction.MathFunctionException;
/*     */ import jsc.mathfunction.StatisticalMathFunction;
/*     */ import jsc.swt.control.LegalCharactersField;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatisticalCalculator
/*     */   extends DataCalculator
/*     */ {
/*     */   StatisticalMathFunction smf;
/*     */   CalculatorVariables cv;
/*     */   JOptionPane pane;
/*     */   JDialog dialog;
/*     */   Vector names;
/*     */   JList nameList;
/*     */   LegalCharactersField expression;
/*     */   JComboBox expressionCombo;
/*     */   JTextField errorMessage;
/*     */   
/*     */   public StatisticalCalculator(Component paramComponent, DataTable paramDataTable) {
/*  49 */     super(paramComponent, paramDataTable);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     this.nameList = new JList();
/*  55 */     this.expression = new LegalCharactersField(0, "");
/*  56 */     updateNames();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     JPanel jPanel1 = new JPanel(new BorderLayout(0, 2));
/*     */ 
/*     */     
/*  64 */     this.expression.addActionListener(new CalcButtonListener(this));
/*     */     
/*  66 */     this.expressionCombo = new JComboBox();
/*  67 */     this.expressionCombo.setEditable(true);
/*  68 */     this.expressionCombo.setEditor(new ExpressionEditor(this));
/*  69 */     this.expressionCombo.setMaximumRowCount(10);
/*  70 */     this.errorMessage = new JTextField();
/*  71 */     this.errorMessage.setEditable(false);
/*  72 */     this.errorMessage.setFocusable(false);
/*     */ 
/*     */ 
/*     */     
/*  76 */     JPanel jPanel2 = new JPanel(new GridLayout(2, 1, 2, 2));
/*     */     
/*  78 */     jPanel2.add(this.expressionCombo);
/*  79 */     jPanel2.add(this.errorMessage);
/*     */     
/*  81 */     jPanel1.add(jPanel2, "North");
/*     */ 
/*     */ 
/*     */     
/*  85 */     JPanel jPanel3 = new JPanel(new GridBagLayout());
/*  86 */     GridBagConstraints gridBagConstraints = new GridBagConstraints();
/*  87 */     gridBagConstraints.fill = 1;
/*  88 */     gridBagConstraints.insets = new Insets(2, 2, 2, 2);
/*     */ 
/*     */ 
/*     */     
/*  92 */     this.nameList.setSelectionMode(0);
/*     */     
/*  94 */     this.nameList.addMouseListener(new NameListMouseListener(this));
/*  95 */     this.nameList.addKeyListener(new NameListKeyListener(this));
/*  96 */     this.nameList.addFocusListener(new NameListFocusListener(this));
/*     */     
/*  98 */     JScrollPane jScrollPane = new JScrollPane(this.nameList);
/*  99 */     jScrollPane.setPreferredSize(new Dimension(150, 200));
/*     */ 
/*     */     
/* 102 */     gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0; gridBagConstraints.gridwidth = 1; gridBagConstraints.gridheight = 2;
/* 103 */     jPanel3.add(jScrollPane, gridBagConstraints);
/*     */ 
/*     */     
/* 106 */     ButtonListener buttonListener = new ButtonListener(this);
/*     */ 
/*     */     
/* 109 */     Font font1 = new Font("Monospaced", 0, 14);
/* 110 */     JPanel jPanel4 = new JPanel(new GridLayout(7, 3, 2, 2));
/* 111 */     String[] arrayOfString1 = { "+", "-", "*", "/", "^", "%", "<", "<=", "=", ">", ">=", "~=", "|", "&", "~", "(", ")", " ", "pi", "e" }; byte b;
/* 112 */     for (b = 0; b < arrayOfString1.length; b++) {
/*     */       
/* 114 */       JButton jButton = new JButton(arrayOfString1[b]);
/* 115 */       jButton.setFont(font1);
/* 116 */       jButton.addActionListener(buttonListener);
/* 117 */       if (b == 17) jButton.setVisible(false); 
/* 118 */       jPanel4.add(jButton);
/*     */     } 
/*     */     
/* 121 */     gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 0; gridBagConstraints.gridwidth = 1; gridBagConstraints.gridheight = 1;
/* 122 */     jPanel3.add(jPanel4, gridBagConstraints);
/*     */ 
/*     */     
/* 125 */     Font font2 = new Font("SanSerif", 0, 12);
/* 126 */     JPanel jPanel5 = new JPanel(new GridLayout(10, 2, 2, 2));
/* 127 */     String[] arrayOfString2 = { "log", "exp", "sqrt", "abs", "int", "nint", "sin", "asin", "cos", "acos", "tan", "atan", "deg", "rad", "sinh", "cosh", "tanh", "sign", "gam", "lgam" };
/*     */     
/* 129 */     for (b = 0; b < arrayOfString2.length; b++) {
/*     */       
/* 131 */       JButton jButton = new JButton(arrayOfString2[b].toUpperCase());
/* 132 */       jButton.setFont(font2);
/* 133 */       jButton.addActionListener(buttonListener);
/* 134 */       jPanel5.add(jButton);
/*     */     } 
/* 136 */     gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 0; gridBagConstraints.gridwidth = 1; gridBagConstraints.gridheight = 2;
/* 137 */     jPanel3.add(jPanel5, gridBagConstraints);
/*     */ 
/*     */     
/* 140 */     JPanel jPanel6 = new JPanel(new GridLayout(4, 3, 2, 2));
/* 141 */     JButton[] arrayOfJButton = new JButton[10];
/* 142 */     for (b = 0; b <= 9; b++) {
/*     */       
/* 144 */       arrayOfJButton[b] = new JButton((new Integer(b)).toString());
/* 145 */       arrayOfJButton[b].addActionListener(buttonListener);
/* 146 */       arrayOfJButton[b].setFont(font2);
/*     */     } 
/* 148 */     jPanel6.add(arrayOfJButton[7]);
/* 149 */     jPanel6.add(arrayOfJButton[8]);
/* 150 */     jPanel6.add(arrayOfJButton[9]);
/* 151 */     jPanel6.add(arrayOfJButton[4]);
/* 152 */     jPanel6.add(arrayOfJButton[5]);
/* 153 */     jPanel6.add(arrayOfJButton[6]);
/* 154 */     jPanel6.add(arrayOfJButton[1]);
/* 155 */     jPanel6.add(arrayOfJButton[2]);
/* 156 */     jPanel6.add(arrayOfJButton[3]);
/* 157 */     jPanel6.add(arrayOfJButton[0]);
/* 158 */     JButton jButton1 = new JButton(".");
/* 159 */     jButton1.addActionListener(buttonListener);
/* 160 */     jPanel6.add(jButton1);
/* 161 */     JButton jButton2 = new JButton("E");
/* 162 */     jButton2.addActionListener(buttonListener);
/* 163 */     jPanel6.add(jButton2);
/* 164 */     gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 1; gridBagConstraints.gridwidth = 1; gridBagConstraints.gridheight = 1;
/* 165 */     jPanel3.add(jPanel6, gridBagConstraints);
/*     */     
/* 167 */     jPanel1.add(jPanel3, "Center");
/*     */     
/* 169 */     JButton jButton3 = new JButton("Calculate");
/* 170 */     jButton3.addActionListener(new CalcButtonListener(this));
/* 171 */     JButton jButton4 = new JButton("Clear");
/* 172 */     jButton4.addActionListener(new ClearButtonListener(this));
/* 173 */     JButton jButton5 = new JButton("Close");
/* 174 */     jButton5.addActionListener(new CloseButtonListener(this));
/* 175 */     jButton5.setDefaultCapable(false);
/*     */ 
/*     */     
/* 178 */     this.pane = new JOptionPane(jPanel1, -1, -1, null, new Object[] { jButton4, jButton3, jButton5 });
/*     */     
/* 180 */     this.dialog = this.pane.createDialog(paramComponent, "Calculations on variables dialogue");
/* 181 */     this.dialog.setModal(false);
/* 182 */     this.dialog.setResizable(false);
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
/*     */   void insertToken(String paramString) {
/* 201 */     int i = this.expression.getCaretPosition();
/* 202 */     StringBuffer stringBuffer = new StringBuffer(this.expression.getText());
/* 203 */     stringBuffer.insert(i, paramString);
/* 204 */     this.expression.setText(stringBuffer.toString());
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
/*     */   public void setSize(int paramInt1, int paramInt2) {
/* 258 */     this.dialog.setSize(paramInt1, paramInt2);
/* 259 */     this.dialog.setLocationRelativeTo(this.parentComponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void show() {
/* 267 */     if (this.expressionCombo.getItemCount() > 1) this.expression.setText(""); 
/* 268 */     this.errorMessage.setText("");
/* 269 */     this.dialog.show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNames() {
/* 279 */     this.cv = new CalculatorVariables(this.dataTable);
/* 280 */     this.smf = new StatisticalMathFunction(this.cv);
/* 281 */     this.expression.setLegalCharacters(this.smf.getLegalCharacters());
/* 282 */     this.names = this.cv.getNames();
/* 283 */     this.nameList.setListData(this.names);
/*     */   } class ButtonListener implements ActionListener { private final StatisticalCalculator this$0;
/*     */     ButtonListener(StatisticalCalculator this$0) {
/* 286 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 290 */       String str = param1ActionEvent.getActionCommand();
/* 291 */       this.this$0.insertToken(str);
/*     */     } }
/*     */   class CalcButtonListener implements ActionListener { private final StatisticalCalculator this$0;
/*     */     CalcButtonListener(StatisticalCalculator this$0) {
/* 295 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 300 */       this.this$0.errorMessage.setText("");
/*     */ 
/*     */       
/*     */       try {
/* 304 */         String str = this.this$0.expression.getText();
/* 305 */         double d = this.this$0.smf.parse(str);
/* 306 */         this.this$0.expressionCombo.addItem(str);
/*     */         
/* 308 */         this.this$0.expressionCombo.setSelectedIndex(this.this$0.expressionCombo.getItemCount() - 1);
/* 309 */         if (!Double.isNaN(d)) {
/* 310 */           this.this$0.errorMessage.setText("Value of expression = " + d);
/*     */         } else {
/*     */           
/* 313 */           int i = this.this$0.dataTable.calculate(this.this$0.smf, this.this$0.cv);
/*     */           
/* 315 */           String str1 = this.this$0.smf.getParsedExpression();
/* 316 */           this.this$0.dataTable.setColumnName(i, str1.replace('"', ' '));
/*     */ 
/*     */           
/* 319 */           this.this$0.errorMessage.setText(this.this$0.smf.getEvalCount() + " row calculations stored in new column " + (i + 1));
/*     */           
/* 321 */           this.this$0.updateNames();
/*     */         } 
/*     */       } catch (MathFunctionException mathFunctionException) {
/*     */         
/* 325 */         this.this$0.errorMessage.setText(mathFunctionException.getMessage());
/*     */       } 
/*     */     } }
/*     */   class ClearButtonListener implements ActionListener { ClearButtonListener(StatisticalCalculator this$0) {
/* 329 */       this.this$0 = this$0;
/*     */     }
/*     */     private final StatisticalCalculator this$0;
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 333 */       this.this$0.expression.setText("");
/* 334 */       this.this$0.errorMessage.setText("");
/*     */     } }
/*     */   class CloseButtonListener implements ActionListener { private final StatisticalCalculator this$0;
/*     */     CloseButtonListener(StatisticalCalculator this$0) {
/* 338 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 341 */       this.this$0.dialog.dispose();
/*     */     } }
/*     */   
/*     */   class ExpressionEditor implements ComboBoxEditor { private final StatisticalCalculator this$0;
/*     */     
/*     */     ExpressionEditor(StatisticalCalculator this$0) {
/* 347 */       this.this$0 = this$0;
/*     */     }
/*     */     
/* 350 */     public void addActionListener(ActionListener param1ActionListener) { this.this$0.expression.addActionListener(param1ActionListener); }
/* 351 */     public Component getEditorComponent() { return (Component)this.this$0.expression; } public Object getItem() {
/* 352 */       return this.this$0.expression.getText();
/*     */     }
/* 354 */     public void removeActionListener(ActionListener param1ActionListener) { this.this$0.expression.removeActionListener(param1ActionListener); } public void selectAll() {
/* 355 */       this.this$0.expression.selectAll();
/*     */     }
/*     */     public void setItem(Object param1Object) {
/* 358 */       if (param1Object != null) {
/*     */         
/* 360 */         this.this$0.expression.setText(param1Object.toString());
/* 361 */         this.this$0.errorMessage.setText("");
/*     */       } 
/*     */     } }
/*     */   class NameListFocusListener extends FocusAdapter { private final StatisticalCalculator this$0;
/*     */     NameListFocusListener(StatisticalCalculator this$0) {
/* 366 */       this.this$0 = this$0;
/*     */     }
/*     */     public void focusGained(FocusEvent param1FocusEvent) {
/* 369 */       this.this$0.nameList.setSelectedIndex(0);
/*     */     } } class NameListKeyListener extends KeyAdapter { private final StatisticalCalculator this$0;
/*     */     NameListKeyListener(StatisticalCalculator this$0) {
/* 372 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void keyTyped(KeyEvent param1KeyEvent) {
/* 378 */       int i = this.this$0.nameList.getSelectedIndex();
/* 379 */       if (i < 0 || i >= this.this$0.names.size())
/*     */         return; 
/* 381 */       Object object = this.this$0.names.elementAt(i);
/* 382 */       String str = "\"" + object.toString() + "\"";
/* 383 */       this.this$0.insertToken(str);
/*     */     } }
/*     */   class NameListMouseListener extends MouseAdapter { private final StatisticalCalculator this$0;
/*     */     
/*     */     NameListMouseListener(StatisticalCalculator this$0) {
/* 388 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent param1MouseEvent) {
/* 393 */       if (param1MouseEvent.getClickCount() == 1) {
/*     */         
/* 395 */         int i = this.this$0.nameList.locationToIndex(param1MouseEvent.getPoint());
/* 396 */         if (i < 0 || i >= this.this$0.names.size())
/*     */           return; 
/* 398 */         Object object = this.this$0.names.elementAt(i);
/* 399 */         String str = "\"" + object.toString() + "\"";
/* 400 */         this.this$0.insertToken(str);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/StatisticalCalculator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */