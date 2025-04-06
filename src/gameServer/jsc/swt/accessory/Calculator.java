/*     */ package jsc.swt.accessory;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import jsc.mathfunction.MathFunctionException;
/*     */ import jsc.mathfunction.StandardMathFunction;
/*     */ import jsc.swt.control.LegalCharactersField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Calculator
/*     */ {
/*     */   StandardMathFunction smf;
/*     */   JOptionPane pane;
/*     */   JDialog dialog;
/*     */   Component parentComponent;
/*     */   LegalCharactersField expression;
/*     */   JComboBox expressionCombo;
/*     */   JTextField errorMessage;
/*     */   
/*     */   public Calculator(Component paramComponent) {
/*  41 */     this.parentComponent = paramComponent;
/*  42 */     this.smf = new StandardMathFunction();
/*  43 */     JPanel jPanel1 = new JPanel(new BorderLayout(0, 2));
/*  44 */     this.expression = new LegalCharactersField(0, this.smf.getLegalCharacters());
/*  45 */     this.expression.addActionListener(new CalcButtonListener(this));
/*     */     
/*  47 */     this.expressionCombo = new JComboBox();
/*  48 */     this.expressionCombo.setEditable(true);
/*  49 */     this.expressionCombo.setEditor(new ExpressionEditor(this));
/*  50 */     this.expressionCombo.setMaximumRowCount(10);
/*  51 */     this.errorMessage = new JTextField();
/*  52 */     this.errorMessage.setEditable(false);
/*  53 */     this.errorMessage.setFocusable(false);
/*     */ 
/*     */ 
/*     */     
/*  57 */     JPanel jPanel2 = new JPanel(new GridLayout(2, 1, 2, 2));
/*     */     
/*  59 */     jPanel2.add(this.expressionCombo);
/*  60 */     jPanel2.add(this.errorMessage);
/*     */     
/*  62 */     jPanel1.add(jPanel2, "North");
/*     */ 
/*     */ 
/*     */     
/*  66 */     JPanel jPanel3 = new JPanel(new GridBagLayout());
/*  67 */     GridBagConstraints gridBagConstraints = new GridBagConstraints();
/*  68 */     gridBagConstraints.fill = 1;
/*  69 */     gridBagConstraints.insets = new Insets(2, 2, 2, 2);
/*     */ 
/*     */     
/*  72 */     ButtonListener buttonListener = new ButtonListener(this);
/*     */ 
/*     */     
/*  75 */     Font font1 = new Font("Monospaced", 0, 14);
/*  76 */     JPanel jPanel4 = new JPanel(new GridLayout(4, 3, 2, 2));
/*  77 */     String[] arrayOfString1 = { "+", "-", "*", "/", "^", "%", "(", ")", " ", "pi", "e" }; byte b;
/*  78 */     for (b = 0; b < arrayOfString1.length; b++) {
/*     */       
/*  80 */       JButton jButton = new JButton(arrayOfString1[b]);
/*  81 */       jButton.setFont(font1);
/*  82 */       jButton.addActionListener(buttonListener);
/*  83 */       if (b == 8) jButton.setVisible(false); 
/*  84 */       jPanel4.add(jButton);
/*     */     } 
/*     */     
/*  87 */     gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 0; gridBagConstraints.gridwidth = 1; gridBagConstraints.gridheight = 1;
/*  88 */     jPanel3.add(jPanel4, gridBagConstraints);
/*     */ 
/*     */     
/*  91 */     Font font2 = new Font("SanSerif", 0, 12);
/*  92 */     JPanel jPanel5 = new JPanel(new GridLayout(6, 3, 2, 2));
/*  93 */     String[] arrayOfString2 = { "sqrt", "log", "exp", "int", "nint", "sign", "abs", "deg", "rad", "sin", "asin", "sinh", "cos", "acos", "cosh", "tan", "atan", "tanh" };
/*     */     
/*  95 */     for (b = 0; b < arrayOfString2.length; b++) {
/*     */       
/*  97 */       JButton jButton = new JButton(arrayOfString2[b].toUpperCase());
/*  98 */       jButton.setFont(font2);
/*  99 */       jButton.addActionListener(buttonListener);
/* 100 */       jPanel5.add(jButton);
/*     */     } 
/* 102 */     gridBagConstraints.gridx = 2; gridBagConstraints.gridy = 0; gridBagConstraints.gridwidth = 1; gridBagConstraints.gridheight = 2;
/* 103 */     jPanel3.add(jPanel5, gridBagConstraints);
/*     */ 
/*     */     
/* 106 */     JPanel jPanel6 = new JPanel(new GridLayout(4, 3, 2, 2));
/* 107 */     JButton[] arrayOfJButton = new JButton[10];
/* 108 */     for (b = 0; b <= 9; b++) {
/*     */       
/* 110 */       arrayOfJButton[b] = new JButton((new Integer(b)).toString());
/* 111 */       arrayOfJButton[b].addActionListener(buttonListener);
/* 112 */       arrayOfJButton[b].setFont(font2);
/*     */     } 
/* 114 */     jPanel6.add(arrayOfJButton[7]);
/* 115 */     jPanel6.add(arrayOfJButton[8]);
/* 116 */     jPanel6.add(arrayOfJButton[9]);
/* 117 */     jPanel6.add(arrayOfJButton[4]);
/* 118 */     jPanel6.add(arrayOfJButton[5]);
/* 119 */     jPanel6.add(arrayOfJButton[6]);
/* 120 */     jPanel6.add(arrayOfJButton[1]);
/* 121 */     jPanel6.add(arrayOfJButton[2]);
/* 122 */     jPanel6.add(arrayOfJButton[3]);
/* 123 */     jPanel6.add(arrayOfJButton[0]);
/* 124 */     JButton jButton1 = new JButton(".");
/* 125 */     jButton1.addActionListener(buttonListener);
/* 126 */     jPanel6.add(jButton1);
/* 127 */     JButton jButton2 = new JButton("E");
/* 128 */     jButton2.addActionListener(buttonListener);
/* 129 */     jPanel6.add(jButton2);
/* 130 */     gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 1; gridBagConstraints.gridwidth = 1; gridBagConstraints.gridheight = 1;
/* 131 */     jPanel3.add(jPanel6, gridBagConstraints);
/*     */     
/* 133 */     jPanel1.add(jPanel3, "Center");
/*     */     
/* 135 */     JButton jButton3 = new JButton("Calculate");
/* 136 */     jButton3.addActionListener(new CalcButtonListener(this));
/* 137 */     JButton jButton4 = new JButton("Clear");
/* 138 */     jButton4.addActionListener(new ClearButtonListener(this));
/* 139 */     JButton jButton5 = new JButton("Close");
/* 140 */     jButton5.addActionListener(new CloseButtonListener(this));
/* 141 */     jButton5.setDefaultCapable(false);
/*     */ 
/*     */     
/* 144 */     this.pane = new JOptionPane(jPanel1, -1, -1, null, new Object[] { jButton4, jButton3, jButton5 });
/*     */     
/* 146 */     this.dialog = this.pane.createDialog(paramComponent, "Calculator");
/* 147 */     this.dialog.setModal(false);
/* 148 */     this.dialog.setResizable(false);
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
/*     */   void insertToken(String paramString) {
/* 164 */     int i = this.expression.getCaretPosition();
/* 165 */     StringBuffer stringBuffer = new StringBuffer(this.expression.getText());
/* 166 */     stringBuffer.insert(i, paramString);
/* 167 */     this.expression.setText(stringBuffer.toString());
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
/*     */   public void setSize(int paramInt1, int paramInt2) {
/* 181 */     this.dialog.setSize(paramInt1, paramInt2);
/* 182 */     this.dialog.setLocationRelativeTo(this.parentComponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object show() {
/* 192 */     if (this.expressionCombo.getItemCount() > 1) this.expression.setText(""); 
/* 193 */     this.errorMessage.setText("");
/*     */     
/* 195 */     this.dialog.show();
/* 196 */     return null;
/*     */   } class ButtonListener implements ActionListener { private final Calculator this$0;
/*     */     ButtonListener(Calculator this$0) {
/* 199 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 203 */       String str = param1ActionEvent.getActionCommand();
/* 204 */       this.this$0.insertToken(str);
/*     */     } }
/*     */   class CalcButtonListener implements ActionListener { private final Calculator this$0;
/*     */     CalcButtonListener(Calculator this$0) {
/* 208 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 213 */       this.this$0.errorMessage.setText("");
/*     */ 
/*     */       
/*     */       try {
/* 217 */         String str = this.this$0.expression.getText();
/* 218 */         double d = this.this$0.smf.parse(str);
/* 219 */         this.this$0.expressionCombo.addItem(str);
/*     */         
/* 221 */         this.this$0.expressionCombo.setSelectedIndex(this.this$0.expressionCombo.getItemCount() - 1);
/* 222 */         if (!Double.isNaN(d)) {
/* 223 */           this.this$0.errorMessage.setText("Value of expression = " + d);
/*     */         }
/*     */       } catch (MathFunctionException mathFunctionException) {
/* 226 */         this.this$0.errorMessage.setText(mathFunctionException.getMessage());
/*     */       } 
/*     */     } }
/*     */   class ClearButtonListener implements ActionListener { ClearButtonListener(Calculator this$0) {
/* 230 */       this.this$0 = this$0;
/*     */     }
/*     */     private final Calculator this$0;
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 234 */       this.this$0.expression.setText("");
/* 235 */       this.this$0.errorMessage.setText("");
/*     */     } }
/*     */   class CloseButtonListener implements ActionListener { private final Calculator this$0;
/*     */     CloseButtonListener(Calculator this$0) {
/* 239 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 242 */       this.this$0.dialog.dispose();
/*     */     } }
/*     */   
/*     */   class ExpressionEditor implements ComboBoxEditor { private final Calculator this$0;
/*     */     
/*     */     ExpressionEditor(Calculator this$0) {
/* 248 */       this.this$0 = this$0;
/*     */     }
/*     */     
/* 251 */     public void addActionListener(ActionListener param1ActionListener) { this.this$0.expression.addActionListener(param1ActionListener); }
/* 252 */     public Component getEditorComponent() { return (Component)this.this$0.expression; } public Object getItem() {
/* 253 */       return this.this$0.expression.getText();
/*     */     }
/* 255 */     public void removeActionListener(ActionListener param1ActionListener) { this.this$0.expression.removeActionListener(param1ActionListener); } public void selectAll() {
/* 256 */       this.this$0.expression.selectAll();
/*     */     }
/*     */     public void setItem(Object param1Object) {
/* 259 */       if (param1Object != null) {
/*     */         
/* 261 */         this.this$0.expression.setText(param1Object.toString());
/* 262 */         this.this$0.errorMessage.setText("");
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/accessory/Calculator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */