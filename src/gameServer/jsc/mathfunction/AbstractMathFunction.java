/*      */ package jsc.mathfunction;
/*      */ 
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractMathFunction
/*      */ {
/*      */   private static final int NUMBER = 6;
/*      */   private static final int OPTI_CONST = 7;
/*      */   private static final int DUMMYCODE = 8;
/*      */   private static final int END = 9;
/*      */   private static final int LABEL = -1;
/*      */   static final int VARIABLE = 0;
/*      */   public static final int UNARY_OP = 1;
/*      */   public static final int BINARY_OP = 2;
/*      */   public static final int CONSTANT = 3;
/*      */   private static final int OPEN_BRACKET = 4;
/*      */   private static final int CLOSE_BRACKET = 5;
/*      */   static final String AUG_MISS = "Missing operand or argument after ";
/*      */   static final String OP_MISS = "Missing operator or function";
/*      */   static final String OPEN_MISS = "Missing open bracket";
/*      */   static final String CLOSE_MISS = "Missing close bracket";
/*      */   static final String NOCODELIST = "Invalid function";
/*      */   static final String NOEXP = "Null expression";
/*      */   static final String CODE_ERROR = "Implementation error: unrecognized code";
/*      */   static final String TYPE_ERROR = "Implementation error: unrecognized code type";
/*      */   static final String VARIABLE_NAN = "Value of variable is not a number";
/*      */   private int nMax;
/*      */   CodeList codeList;
/*      */   private int nVarFound;
/*      */   private boolean[] var_flag;
/*      */   private long eval_count;
/*  103 */   private Stack cs = new Stack();
/*      */ 
/*      */ 
/*      */   
/*      */   private int count;
/*      */ 
/*      */   
/*      */   private MathFunctionVariables clv;
/*      */ 
/*      */   
/*      */   private String parsedExpression;
/*      */ 
/*      */   
/*      */   protected DecimalFormat decimalFormat;
/*      */ 
/*      */   
/*      */   protected DecimalFormat scientificFormat;
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractMathFunction(MathFunctionVariables paramMathFunctionVariables) {
/*  124 */     NumberFormat numberFormat1 = NumberFormat.getInstance();
/*  125 */     if (numberFormat1 instanceof DecimalFormat) {
/*  126 */       this.decimalFormat = (DecimalFormat)numberFormat1;
/*      */     } else {
/*  128 */       throw new IllegalArgumentException("DecimalFormat not available for locale.");
/*  129 */     }  NumberFormat numberFormat2 = NumberFormat.getInstance();
/*  130 */     this.scientificFormat = (DecimalFormat)numberFormat2;
/*  131 */     this.scientificFormat.applyPattern("#.#E0");
/*      */     
/*  133 */     this.var_flag = null;
/*  134 */     this.codeList = null;
/*  135 */     resetVariables(paramMathFunctionVariables);
/*      */     
/*  137 */     initialize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractMathFunction() {
/*  145 */     this(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkSubclass() throws MathFunctionException {
/*  153 */     for (byte b = 0; b < getNumberOfTokens(); b++) {
/*      */ 
/*      */ 
/*      */       
/*  157 */       if (getCode(b) < 10) {
/*  158 */         throw new MathFunctionException("Implementation error: code must be > 9");
/*      */       }
/*      */       
/*  161 */       char c = getToken(b).charAt(0);
/*  162 */       if (c == '.' || Character.isDigit(c)) {
/*  163 */         throw new MathFunctionException("Implementation error: token must not start with \".\" or a digit");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void check_syntax(Vector paramVector) throws MathFunctionException {
/*  170 */     Code code = paramVector.firstElement();
/*  171 */     int i = code.type;
/*  172 */     int j = code.code;
/*      */     
/*  174 */     byte b1 = 0;
/*  175 */     byte b2 = 0;
/*      */     
/*  177 */     int k = paramVector.size();
/*  178 */     if (i == 2 || (i == 1 && k == 1))
/*  179 */       throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(j)); 
/*  180 */     if (i == 5)
/*  181 */       throw new MathFunctionException("Missing open bracket"); 
/*  182 */     if (i == 4) {
/*  183 */       b1++;
/*      */     }
/*  185 */     for (byte b3 = 1; b3 < k; b3++) {
/*      */       
/*  187 */       code = paramVector.elementAt(b3);
/*  188 */       int m = code.type;
/*  189 */       boolean bool = (b3 == k - 1) ? true : false;
/*      */       
/*  191 */       switch (m) {
/*      */         
/*      */         case 4:
/*  194 */           b1++;
/*      */         case 1:
/*  196 */           if (bool) throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(code.code)); 
/*      */         case 0:
/*      */         case 3:
/*  199 */           if (i == 3 || i == 0 || i == 5)
/*      */           {
/*      */             
/*  202 */             throw new MathFunctionException("Missing operator or function"); } 
/*      */           break;
/*      */         case 2:
/*  205 */           if (bool) throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(code.code)); 
/*      */         case 5:
/*  207 */           if (i != 3 && i != 0 && i != 5)
/*      */           {
/*      */             
/*  210 */             throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(j)); } 
/*  211 */           if (m == 5)
/*  212 */             b2++; 
/*  213 */           if (b2 > b1)
/*  214 */             throw new MathFunctionException("Missing open bracket"); 
/*      */           break;
/*      */       } 
/*  217 */       i = m;
/*  218 */       j = code.code;
/*      */     } 
/*  220 */     if (b1 > b2) throw new MathFunctionException("Missing close bracket"); 
/*  221 */     if (b1 < b2) throw new MathFunctionException("Missing open bracket");
/*      */ 
/*      */     
/*  224 */     this.nMax = 1 + k - 2 * b1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void code_list_parse(Vector paramVector) throws MathFunctionException {
/*  235 */     this.codeList = null;
/*      */ 
/*      */     
/*  238 */     this.codeList = new CodeList(this.nMax);
/*      */ 
/*      */ 
/*      */     
/*  242 */     this.cs.clear();
/*  243 */     this.cs.push(new Code(this, 8, 8));
/*      */     
/*  245 */     for (byte b = 0; b < paramVector.size(); b++) {
/*      */       
/*  247 */       Code code2, code1 = paramVector.elementAt(b);
/*  248 */       switch (code1.type) {
/*      */         
/*      */         case 0:
/*  251 */           out_to_code_list(code1);
/*      */           break;
/*      */         
/*      */         case 3:
/*  255 */           out_to_code_list(code1);
/*      */           break;
/*      */         case 2:
/*  258 */           while (precedence(((Code)this.cs.peek()).code) >= precedence(code1.code))
/*      */           {
/*  260 */             out_to_code_list(this.cs.pop());
/*      */           }
/*      */           
/*  263 */           this.cs.push(new Code(this, CodeList.getLabelCode(this.codeList.size()), -1));
/*      */           
/*  265 */           this.cs.push(code1);
/*      */           break;
/*      */         case 1:
/*      */         case 4:
/*  269 */           this.cs.push(code1);
/*      */           break;
/*      */         case 5:
/*  272 */           while ((code2 = this.cs.pop()).code != 4)
/*      */           {
/*  274 */             out_to_code_list(code2); } 
/*      */           break;
/*      */         default:
/*  277 */           throw new MathFunctionException("Implementation error: unrecognized code type");
/*      */       } 
/*      */     } 
/*      */     Code code;
/*  281 */     while ((code = this.cs.pop()).code != 8)
/*      */     {
/*  283 */       out_to_code_list(code);
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
/*      */   private void codify(String paramString, Vector paramVector) throws MathFunctionException {
/*  299 */     this.count = 0;
/*  300 */     int i = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  310 */     StringBuffer stringBuffer = new StringBuffer(paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  318 */     stringBuffer.append(false);
/*  319 */     String str = stringBuffer.toString();
/*      */     char c;
/*  321 */     while ((c = str.charAt(i)) != '\000') {
/*      */       
/*  323 */       String str1 = str.substring(i);
/*      */       
/*  325 */       if (c == '.' || Character.isDigit(c)) {
/*      */ 
/*      */         
/*  328 */         paramVector.add(new Code(this, 6, 3, parse_number(str1)));
/*  329 */         i = i + this.count - 1;
/*      */       }
/*  331 */       else if (!Character.isWhitespace(c)) {
/*      */ 
/*      */         
/*  334 */         Code code = pattern_match(str1, -1);
/*  335 */         paramVector.add(code);
/*  336 */         i = i + this.count - 1;
/*      */ 
/*      */         
/*  339 */         if (code.type == 0)
/*      */         {
/*  341 */           this.var_flag[code.code] = true;
/*      */         }
/*  343 */         if (isAmbiguous(code.code) && paramVector.size() > 1) {
/*      */           
/*  345 */           Code code1 = paramVector.elementAt(paramVector.size() - 2);
/*  346 */           if ((code1.type == 0 || code1.type == 3 || code1.type == 5) && code.type == 1) {
/*      */ 
/*      */             
/*  349 */             paramVector.removeElementAt(paramVector.size() - 1);
/*  350 */             paramVector.add(pattern_match(str1, code.code));
/*      */           } 
/*      */         } 
/*      */       } 
/*  354 */       i++;
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
/*      */   public double eval() throws MathFunctionException {
/*  381 */     if (this.codeList == null) throw new MathFunctionException("Invalid function");
/*      */     
/*  383 */     for (byte b = 1; b <= this.codeList.size(); b++) {
/*      */       double d;
/*  385 */       int i = this.codeList.getCode(b);
/*  386 */       int j = this.codeList.getType(b);
/*  387 */       switch (j) {
/*      */         case 3:
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 0:
/*  398 */           d = this.clv.getVariableValue(i);
/*  399 */           if (Double.isNaN(d))
/*  400 */             throw new MathFunctionException("Value of variable is not a number"); 
/*  401 */           this.codeList.setValue(d, b);
/*      */           break;
/*      */         case 1:
/*  404 */           this.codeList.setValue(unaryOperation(i, this.codeList.getRightValue(b)), b);
/*      */           break;
/*      */         case 2:
/*  407 */           this.codeList.setValue(binaryOperation(i, this.codeList.getLeftValue(b), this.codeList.getRightValue(b)), b);
/*      */           break;
/*      */         default:
/*  410 */           throw new MathFunctionException("Implementation error: unrecognized code type");
/*      */       } 
/*      */     
/*      */     } 
/*  414 */     this.eval_count++;
/*  415 */     return this.codeList.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getEvalCount() {
/*  424 */     return this.eval_count;
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
/*      */   public String getLegalCharacters() {
/*  440 */     StringBuffer stringBuffer = new StringBuffer("0123456789.,-+E()");
/*      */     
/*      */     byte b;
/*  443 */     for (b = 0; b < getNumberOfTokens(); ) { stringBuffer.append(getToken(b)); b++; }
/*  444 */      for (b = 0; b < this.clv.getNumberOfVariables(); ) { stringBuffer.append(this.clv.getVariableName(b)); b++; }
/*      */ 
/*      */     
/*  447 */     Vector vector = new Vector();
/*  448 */     for (b = 0; b < stringBuffer.length(); b++) {
/*      */       
/*  450 */       Character character = new Character(Character.toLowerCase(stringBuffer.charAt(b)));
/*  451 */       if (!vector.contains(character)) vector.addElement(character); 
/*      */     } 
/*  453 */     return vector.toString();
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
/*      */   public int getNumberOfVariablesUsed() {
/*  468 */     return this.nVarFound;
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
/*      */   public String getParsedExpression() {
/*  493 */     return this.parsedExpression;
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
/*      */   public String getSummary() {
/*  506 */     StringBuffer stringBuffer = new StringBuffer("\nUnary operators:"); byte b;
/*  507 */     for (b = 0; b < getNumberOfTokens(); b++) {
/*  508 */       if (getType(getCode(b)) == 1) stringBuffer.append(" " + getToken(b)); 
/*  509 */     }  stringBuffer.append("\nBinary operators:");
/*  510 */     for (b = 0; b < getNumberOfTokens(); b++) {
/*  511 */       if (getType(getCode(b)) == 2) stringBuffer.append(" " + getToken(b)); 
/*  512 */     }  stringBuffer.append("\nImplicit binary operator: ");
/*  513 */     if (getImplicitCode() < 0) {
/*  514 */       stringBuffer.append("none");
/*      */     } else {
/*  516 */       stringBuffer.append(getTokenFromCode(getImplicitCode()));
/*  517 */     }  stringBuffer.append("\nConstants:");
/*  518 */     for (b = 0; b < getNumberOfTokens(); b++) {
/*  519 */       if (getType(getCode(b)) == 3) stringBuffer.append(" " + getToken(b)); 
/*  520 */     }  stringBuffer.append("\nBrackets: ()");
/*  521 */     stringBuffer.append("\nVariables:");
/*  522 */     for (b = 0; b < this.clv.getNumberOfVariables(); ) { stringBuffer.append(" " + this.clv.getVariableName(b)); b++; }
/*  523 */      return stringBuffer.toString();
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
/*      */   protected String getTokenFromCode(int paramInt) {
/*  535 */     if (paramInt == 4) return "("; 
/*  536 */     if (paramInt == 5) return ")"; 
/*  537 */     for (byte b = 0; b < getNumberOfTokens(); b++) {
/*  538 */       if (paramInt == getCode(b)) return getToken(b); 
/*  539 */     }  return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasParsed() {
/*  549 */     return (this.codeList != null);
/*      */   }
/*      */   
/*      */   private void initialize() {
/*  553 */     if (this.codeList != null) this.codeList.setSize(0); 
/*  554 */     this.eval_count = 0L;
/*  555 */     this.nVarFound = 0;
/*  556 */     for (byte b = 0; b < this.clv.getNumberOfVariables(); ) { this.var_flag[b] = false; b++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void insert(Vector paramVector) {
/*  564 */     Code code = paramVector.firstElement();
/*  565 */     int i = code.type;
/*      */ 
/*      */     
/*  568 */     byte b = 0;
/*  569 */     while (++b < paramVector.size()) {
/*      */       
/*  571 */       code = paramVector.elementAt(b);
/*  572 */       int j = code.type;
/*  573 */       if ((operand(i) && operand(j)) || (operand(i) && j == 1) || (operand(i) && j == 4) || (i == 5 && j == 4) || (i == 5 && operand(j)) || (i == 5 && j == 1))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  580 */         paramVector.insertElementAt(new Code(this, getImplicitCode(), 2), b++); } 
/*  581 */       i = j;
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
/*      */   private boolean operand(int paramInt) {
/*  598 */     return (paramInt == 3 || paramInt == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void out_to_code_list(Code paramCode) throws MathFunctionException {
/*  605 */     int j, i = this.codeList.incrementSize();
/*      */     
/*  607 */     switch (paramCode.type) {
/*      */       
/*      */       case 0:
/*  610 */         this.codeList.setType(paramCode.type, i);
/*  611 */         this.codeList.setCode(paramCode.code, i);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 3:
/*  616 */         this.codeList.setValue(paramCode.constant, i);
/*  617 */         this.codeList.setType(paramCode.type, i);
/*  618 */         this.codeList.setCode(paramCode.code, i);
/*      */         return;
/*      */       
/*      */       case 1:
/*  622 */         if (this.codeList.getType(i - 1) == 3) {
/*      */ 
/*      */           
/*  625 */           this.codeList.setValue(unaryOperation(paramCode.code, this.codeList.getValue(i - 1)), i - 1);
/*  626 */           this.codeList.setCode(7, i);
/*  627 */           this.codeList.setType(3, i);
/*  628 */           i = this.codeList.decrementSize();
/*      */         }
/*      */         else {
/*      */           
/*  632 */           this.codeList.setRight(CodeList.getLabelCode(i - 1), i);
/*  633 */           this.codeList.setCode(paramCode.code, i);
/*  634 */           this.codeList.setType(paramCode.type, i);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*  641 */         j = ((Code)this.cs.pop()).code;
/*  642 */         if (this.codeList.getLabelLine(j) == i - 2 && this.codeList.getType(i - 2) == 3 && this.codeList.getType(i - 1) == 3) {
/*      */ 
/*      */ 
/*      */           
/*  646 */           this.codeList.setValue(binaryOperation(paramCode.code, this.codeList.getValue(i - 2), this.codeList.getValue(i - 1)), i - 2);
/*  647 */           this.codeList.setCode(7, i);
/*  648 */           this.codeList.setType(3, i);
/*  649 */           this.codeList.decrementSize();
/*  650 */           i = this.codeList.decrementSize();
/*      */         }
/*      */         else {
/*      */           
/*  654 */           this.codeList.setLeft(j, i);
/*  655 */           this.codeList.setRight(CodeList.getLabelCode(i - 1), i);
/*  656 */           this.codeList.setCode(paramCode.code, i);
/*  657 */           this.codeList.setType(paramCode.type, i);
/*      */         }  return;
/*      */     } 
/*  660 */     throw new MathFunctionException("Implementation error: unrecognized code type");
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
/*      */   public double parse(String paramString) throws MathFunctionException {
/*  705 */     this.parsedExpression = paramString;
/*      */ 
/*      */ 
/*      */     
/*  709 */     int i = paramString.length();
/*      */     
/*  711 */     initialize();
/*  712 */     if (i < 1) throw new MathFunctionException("Null expression");
/*      */     
/*  714 */     checkSubclass();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  722 */     Vector vector = new Vector(i);
/*      */ 
/*      */     
/*  725 */     codify(paramString, vector);
/*      */ 
/*      */     
/*  728 */     for (byte b = 0; b < this.clv.getNumberOfVariables(); ) { if (this.var_flag[b]) this.nVarFound++;  b++; }
/*      */ 
/*      */     
/*  731 */     if (getImplicitCode() > 0) insert(vector);
/*      */ 
/*      */ 
/*      */     
/*  735 */     check_syntax(vector);
/*      */ 
/*      */     
/*  738 */     code_list_parse(vector);
/*      */     
/*  740 */     if (getNumberOfVariablesUsed() > 0) {
/*  741 */       return Double.NaN;
/*      */     }
/*  743 */     return this.codeList.getValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private double parse_number(String paramString) throws MathFunctionException {
/*  829 */     ParsePosition parsePosition = new ParsePosition(0);
/*  830 */     Number number = this.scientificFormat.parse(paramString, parsePosition);
/*  831 */     if (number == null) {
/*      */       
/*  833 */       parsePosition.setIndex(0);
/*  834 */       number = this.decimalFormat.parse(paramString, parsePosition);
/*  835 */       if (number == null) {
/*  836 */         throw new MathFunctionException("Error in number constant \"" + paramString.substring(0, parsePosition.getErrorIndex() + 1) + "\"");
/*      */       }
/*      */     } 
/*  839 */     this.count = parsePosition.getIndex();
/*  840 */     return number.doubleValue();
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
/*      */   private Code pattern_match(String paramString, int paramInt) throws MathFunctionException {
/*  854 */     if (paramString.charAt(0) == '(') { this.count = 1; return new Code(this, 4, 4); }
/*  855 */      if (paramString.charAt(0) == ')') { this.count = 1; return new Code(this, 5, 5); }
/*      */     
/*  857 */     int i = 0;
/*      */ 
/*      */     
/*  860 */     int j = paramString.length();
/*      */ 
/*      */     
/*  863 */     for (i = 0; i < getNumberOfTokens(); i++) {
/*      */       
/*  865 */       String str = getToken(i);
/*      */       
/*  867 */       this.count = str.length();
/*  868 */       if (paramString.substring(0, Math.min(this.count, j)).equalsIgnoreCase(str)) {
/*      */         
/*  870 */         int k = getCode(i);
/*  871 */         int m = getType(k);
/*  872 */         if (k != paramInt) {
/*  873 */           return (m == 3) ? new Code(this, k, m, getConstant(k)) : new Code(this, k, m);
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  879 */     for (i = this.clv.getNumberOfVariables() - 1; i >= 0; i--) {
/*      */       
/*  881 */       String str = this.clv.getVariableName(i);
/*  882 */       this.count = str.length();
/*  883 */       if (paramString.substring(0, Math.min(this.count, j)).equalsIgnoreCase(str))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  888 */         return new Code(this, i, 0);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  893 */     this.count = 1;
/*  894 */     throw new MathFunctionException("Unrecognized sequence: " + paramString.substring(0, Math.min(4, paramString.length() - 1)) + "...");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int precedence(int paramInt) throws MathFunctionException {
/*  900 */     if (paramInt == 8) return 0; 
/*  901 */     if (paramInt == 4 || paramInt == 5) return 1; 
/*  902 */     for (byte b = 0; b < getNumberOfTokens(); b++) {
/*  903 */       if (paramInt == getCode(b)) {
/*      */         
/*  905 */         int i = getPrecedence(b);
/*  906 */         if (i < 2) {
/*  907 */           throw new MathFunctionException("Implementation error: precedence must be > 1");
/*      */         }
/*  909 */         return i;
/*      */       } 
/*  911 */     }  throw new MathFunctionException("Implementation error: unrecognized code");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetEvalCount() {
/*  917 */     this.eval_count = 0L;
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
/*      */   public void resetVariables(MathFunctionVariables paramMathFunctionVariables) {
/*  929 */     if (paramMathFunctionVariables == null) {
/*  930 */       this.clv = new ConstantMathFunction(this);
/*      */     } else {
/*  932 */       this.clv = paramMathFunctionVariables;
/*      */     } 
/*  934 */     int i = this.clv.getNumberOfVariables();
/*  935 */     if (i == 0) {
/*  936 */       this.var_flag = null;
/*      */     } else {
/*      */       
/*  939 */       this.var_flag = new boolean[i];
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
/*      */   public void setDecimalFormat(DecimalFormat paramDecimalFormat) {
/*  952 */     this.decimalFormat = paramDecimalFormat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDecimalFormatPattern(String paramString) {
/*  962 */     this.decimalFormat.applyPattern(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScientificFormat(DecimalFormat paramDecimalFormat) {
/*  973 */     this.scientificFormat = paramDecimalFormat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScientificFormatPattern(String paramString) {
/*  984 */     this.scientificFormat.applyPattern(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   private String tokenToString(int paramInt) {
/*  989 */     if (paramInt < 0) return "L" + this.codeList.getLabelLine(paramInt); 
/*  990 */     return getTokenFromCode(paramInt);
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
/*      */   public String toString() {
/* 1002 */     StringBuffer stringBuffer = new StringBuffer(getSummary());
/* 1003 */     stringBuffer.append("\n\nCode list");
/* 1004 */     if (this.codeList.size() < 1) stringBuffer.append(" empty"); 
/* 1005 */     for (byte b = 1; b <= this.codeList.size(); b++) {
/*      */       
/* 1007 */       stringBuffer.append("\nL" + b + " ");
/* 1008 */       int i = this.codeList.getCode(b);
/* 1009 */       int j = this.codeList.getType(b);
/* 1010 */       switch (j) {
/*      */         case 2:
/* 1012 */           stringBuffer.append(tokenToString(this.codeList.getLeft(b)) + " ");
/*      */         case 1:
/* 1014 */           stringBuffer.append(tokenToString(i) + " ");
/* 1015 */           stringBuffer.append(tokenToString(this.codeList.getRight(b)));
/*      */           break;
/*      */         case 3:
/* 1018 */           stringBuffer.append(this.codeList.getValue(b));
/*      */           break;
/*      */         
/*      */         case 0:
/* 1022 */           stringBuffer.append(this.clv.getVariableName(i));
/*      */           break;
/*      */         default:
/* 1025 */           stringBuffer.append("??");
/*      */           break;
/*      */       } 
/*      */     } 
/* 1029 */     stringBuffer.append("\n");
/* 1030 */     return stringBuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean variableUsed(int paramInt) {
/* 1038 */     return this.var_flag[paramInt];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean variableUsed(String paramString) {
/* 1049 */     for (byte b = 0; b < this.clv.getNumberOfVariables(); b++) {
/* 1050 */       if (this.var_flag[b] && paramString.equalsIgnoreCase(this.clv.getVariableName(b)))
/* 1051 */         return true; 
/* 1052 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract double binaryOperation(int paramInt, double paramDouble1, double paramDouble2) throws MathFunctionException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int getCode(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract double getConstant(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int getImplicitCode();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int getNumberOfTokens();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int getPrecedence(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String getToken(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int getType(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean isAmbiguous(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract double unaryOperation(int paramInt, double paramDouble) throws MathFunctionException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class Code
/*      */   {
/*      */     int code;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int type;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     double constant;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final AbstractMathFunction this$0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Code(AbstractMathFunction this$0, int param1Int1, int param1Int2) {
/* 1175 */       this.this$0 = this$0; this.code = param1Int1; this.type = param1Int2; this.constant = Double.NaN; } Code(AbstractMathFunction this$0, int param1Int1, int param1Int2, double param1Double) {
/* 1176 */       this.this$0 = this$0; this.code = param1Int1; this.type = param1Int2; this.constant = param1Double;
/*      */     }
/*      */   }
/*      */   
/*      */   class ConstantMathFunction
/*      */     implements MathFunctionVariables {
/*      */     private final AbstractMathFunction this$0;
/*      */     
/*      */     ConstantMathFunction(AbstractMathFunction this$0) {
/* 1185 */       this.this$0 = this$0;
/*      */     }
/* 1187 */     public int getNumberOfVariables() { return 0; }
/* 1188 */     public String getVariableName(int param1Int) { return ""; } public double getVariableValue(int param1Int) {
/* 1189 */       return 0.0D;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/AbstractMathFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */