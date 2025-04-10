package jsc.mathfunction;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Stack;
import java.util.Vector;

public abstract class AbstractMathFunction
{
private static final int NUMBER = 6;
private static final int OPTI_CONST = 7;
private static final int DUMMYCODE = 8;
private static final int END = 9;
private static final int LABEL = -1;
static final int VARIABLE = 0;
public static final int UNARY_OP = 1;
public static final int BINARY_OP = 2;
public static final int CONSTANT = 3;
private static final int OPEN_BRACKET = 4;
private static final int CLOSE_BRACKET = 5;
static final String AUG_MISS = "Missing operand or argument after ";
static final String OP_MISS = "Missing operator or function";
static final String OPEN_MISS = "Missing open bracket";
static final String CLOSE_MISS = "Missing close bracket";
static final String NOCODELIST = "Invalid function";
static final String NOEXP = "Null expression";
static final String CODE_ERROR = "Implementation error: unrecognized code";
static final String TYPE_ERROR = "Implementation error: unrecognized code type";
static final String VARIABLE_NAN = "Value of variable is not a number";
private int nMax;
CodeList codeList;
private int nVarFound;
private boolean[] var_flag;
private long eval_count;
private Stack cs = new Stack();

private int count;

private MathFunctionVariables clv;

private String parsedExpression;

protected DecimalFormat decimalFormat;

protected DecimalFormat scientificFormat;

public AbstractMathFunction(MathFunctionVariables paramMathFunctionVariables) {
NumberFormat numberFormat1 = NumberFormat.getInstance();
if (numberFormat1 instanceof DecimalFormat) {
this.decimalFormat = (DecimalFormat)numberFormat1;
} else {
throw new IllegalArgumentException("DecimalFormat not available for locale.");
}  NumberFormat numberFormat2 = NumberFormat.getInstance();
this.scientificFormat = (DecimalFormat)numberFormat2;
this.scientificFormat.applyPattern("#.#E0");

this.var_flag = null;
this.codeList = null;
resetVariables(paramMathFunctionVariables);

initialize();
}

public AbstractMathFunction() {
this(null);
}

private void checkSubclass() throws MathFunctionException {
for (byte b = 0; b < getNumberOfTokens(); b++) {

if (getCode(b) < 10) {
throw new MathFunctionException("Implementation error: code must be > 9");
}

char c = getToken(b).charAt(0);
if (c == '.' || Character.isDigit(c)) {
throw new MathFunctionException("Implementation error: token must not start with \".\" or a digit");
}
} 
}

private void check_syntax(Vector paramVector) throws MathFunctionException {
Code code = paramVector.firstElement();
int i = code.type;
int j = code.code;

byte b1 = 0;
byte b2 = 0;

int k = paramVector.size();
if (i == 2 || (i == 1 && k == 1))
throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(j)); 
if (i == 5)
throw new MathFunctionException("Missing open bracket"); 
if (i == 4) {
b1++;
}
for (byte b3 = 1; b3 < k; b3++) {

code = paramVector.elementAt(b3);
int m = code.type;
boolean bool = (b3 == k - 1) ? true : false;

switch (m) {

case 4:
b1++;
case 1:
if (bool) throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(code.code)); 
case 0:
case 3:
if (i == 3 || i == 0 || i == 5)
{

throw new MathFunctionException("Missing operator or function"); } 
break;
case 2:
if (bool) throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(code.code)); 
case 5:
if (i != 3 && i != 0 && i != 5)
{

throw new MathFunctionException("Missing operand or argument after " + getTokenFromCode(j)); } 
if (m == 5)
b2++; 
if (b2 > b1)
throw new MathFunctionException("Missing open bracket"); 
break;
} 
i = m;
j = code.code;
} 
if (b1 > b2) throw new MathFunctionException("Missing close bracket"); 
if (b1 < b2) throw new MathFunctionException("Missing open bracket");

this.nMax = 1 + k - 2 * b1;
}

private void code_list_parse(Vector paramVector) throws MathFunctionException {
this.codeList = null;

this.codeList = new CodeList(this.nMax);

this.cs.clear();
this.cs.push(new Code(this, 8, 8));

for (byte b = 0; b < paramVector.size(); b++) {

Code code2, code1 = paramVector.elementAt(b);
switch (code1.type) {

case 0:
out_to_code_list(code1);
break;

case 3:
out_to_code_list(code1);
break;
case 2:
while (precedence(((Code)this.cs.peek()).code) >= precedence(code1.code))
{
out_to_code_list(this.cs.pop());
}

this.cs.push(new Code(this, CodeList.getLabelCode(this.codeList.size()), -1));

this.cs.push(code1);
break;
case 1:
case 4:
this.cs.push(code1);
break;
case 5:
while ((code2 = this.cs.pop()).code != 4)
{
out_to_code_list(code2); } 
break;
default:
throw new MathFunctionException("Implementation error: unrecognized code type");
} 
} 
Code code;
while ((code = this.cs.pop()).code != 8)
{
out_to_code_list(code);
}
}

private void codify(String paramString, Vector paramVector) throws MathFunctionException {
this.count = 0;
int i = 0;

StringBuffer stringBuffer = new StringBuffer(paramString);

stringBuffer.append(false);
String str = stringBuffer.toString();
char c;
while ((c = str.charAt(i)) != '\000') {

String str1 = str.substring(i);

if (c == '.' || Character.isDigit(c)) {

paramVector.add(new Code(this, 6, 3, parse_number(str1)));
i = i + this.count - 1;
}
else if (!Character.isWhitespace(c)) {

Code code = pattern_match(str1, -1);
paramVector.add(code);
i = i + this.count - 1;

if (code.type == 0)
{
this.var_flag[code.code] = true;
}
if (isAmbiguous(code.code) && paramVector.size() > 1) {

Code code1 = paramVector.elementAt(paramVector.size() - 2);
if ((code1.type == 0 || code1.type == 3 || code1.type == 5) && code.type == 1) {

paramVector.removeElementAt(paramVector.size() - 1);
paramVector.add(pattern_match(str1, code.code));
} 
} 
} 
i++;
} 
}

public double eval() throws MathFunctionException {
if (this.codeList == null) throw new MathFunctionException("Invalid function");

for (byte b = 1; b <= this.codeList.size(); b++) {
double d;
int i = this.codeList.getCode(b);
int j = this.codeList.getType(b);
switch (j) {
case 3:
break;

case 0:
d = this.clv.getVariableValue(i);
if (Double.isNaN(d))
throw new MathFunctionException("Value of variable is not a number"); 
this.codeList.setValue(d, b);
break;
case 1:
this.codeList.setValue(unaryOperation(i, this.codeList.getRightValue(b)), b);
break;
case 2:
this.codeList.setValue(binaryOperation(i, this.codeList.getLeftValue(b), this.codeList.getRightValue(b)), b);
break;
default:
throw new MathFunctionException("Implementation error: unrecognized code type");
} 

} 
this.eval_count++;
return this.codeList.getValue();
}

public long getEvalCount() {
return this.eval_count;
}

public String getLegalCharacters() {
StringBuffer stringBuffer = new StringBuffer("0123456789.,-+E()");

byte b;
for (b = 0; b < getNumberOfTokens(); ) { stringBuffer.append(getToken(b)); b++; }
for (b = 0; b < this.clv.getNumberOfVariables(); ) { stringBuffer.append(this.clv.getVariableName(b)); b++; }

Vector vector = new Vector();
for (b = 0; b < stringBuffer.length(); b++) {

Character character = new Character(Character.toLowerCase(stringBuffer.charAt(b)));
if (!vector.contains(character)) vector.addElement(character); 
} 
return vector.toString();
}

public int getNumberOfVariablesUsed() {
return this.nVarFound;
}

public String getParsedExpression() {
return this.parsedExpression;
}

public String getSummary() {
StringBuffer stringBuffer = new StringBuffer("\nUnary operators:"); byte b;
for (b = 0; b < getNumberOfTokens(); b++) {
if (getType(getCode(b)) == 1) stringBuffer.append(" " + getToken(b)); 
}  stringBuffer.append("\nBinary operators:");
for (b = 0; b < getNumberOfTokens(); b++) {
if (getType(getCode(b)) == 2) stringBuffer.append(" " + getToken(b)); 
}  stringBuffer.append("\nImplicit binary operator: ");
if (getImplicitCode() < 0) {
stringBuffer.append("none");
} else {
stringBuffer.append(getTokenFromCode(getImplicitCode()));
}  stringBuffer.append("\nConstants:");
for (b = 0; b < getNumberOfTokens(); b++) {
if (getType(getCode(b)) == 3) stringBuffer.append(" " + getToken(b)); 
}  stringBuffer.append("\nBrackets: ()");
stringBuffer.append("\nVariables:");
for (b = 0; b < this.clv.getNumberOfVariables(); ) { stringBuffer.append(" " + this.clv.getVariableName(b)); b++; }
return stringBuffer.toString();
}

protected String getTokenFromCode(int paramInt) {
if (paramInt == 4) return "("; 
if (paramInt == 5) return ")"; 
for (byte b = 0; b < getNumberOfTokens(); b++) {
if (paramInt == getCode(b)) return getToken(b); 
}  return null;
}

public boolean hasParsed() {
return (this.codeList != null);
}

private void initialize() {
if (this.codeList != null) this.codeList.setSize(0); 
this.eval_count = 0L;
this.nVarFound = 0;
for (byte b = 0; b < this.clv.getNumberOfVariables(); ) { this.var_flag[b] = false; b++; }

}

private void insert(Vector paramVector) {
Code code = paramVector.firstElement();
int i = code.type;

byte b = 0;
while (++b < paramVector.size()) {

code = paramVector.elementAt(b);
int j = code.type;
if ((operand(i) && operand(j)) || (operand(i) && j == 1) || (operand(i) && j == 4) || (i == 5 && j == 4) || (i == 5 && operand(j)) || (i == 5 && j == 1))
{

paramVector.insertElementAt(new Code(this, getImplicitCode(), 2), b++); } 
i = j;
} 
}

private boolean operand(int paramInt) {
return (paramInt == 3 || paramInt == 0);
}

private void out_to_code_list(Code paramCode) throws MathFunctionException {
int j, i = this.codeList.incrementSize();

switch (paramCode.type) {

case 0:
this.codeList.setType(paramCode.type, i);
this.codeList.setCode(paramCode.code, i);
return;

case 3:
this.codeList.setValue(paramCode.constant, i);
this.codeList.setType(paramCode.type, i);
this.codeList.setCode(paramCode.code, i);
return;

case 1:
if (this.codeList.getType(i - 1) == 3) {

this.codeList.setValue(unaryOperation(paramCode.code, this.codeList.getValue(i - 1)), i - 1);
this.codeList.setCode(7, i);
this.codeList.setType(3, i);
i = this.codeList.decrementSize();
}
else {

this.codeList.setRight(CodeList.getLabelCode(i - 1), i);
this.codeList.setCode(paramCode.code, i);
this.codeList.setType(paramCode.type, i);
} 
return;

case 2:
j = ((Code)this.cs.pop()).code;
if (this.codeList.getLabelLine(j) == i - 2 && this.codeList.getType(i - 2) == 3 && this.codeList.getType(i - 1) == 3) {

this.codeList.setValue(binaryOperation(paramCode.code, this.codeList.getValue(i - 2), this.codeList.getValue(i - 1)), i - 2);
this.codeList.setCode(7, i);
this.codeList.setType(3, i);
this.codeList.decrementSize();
i = this.codeList.decrementSize();
}
else {

this.codeList.setLeft(j, i);
this.codeList.setRight(CodeList.getLabelCode(i - 1), i);
this.codeList.setCode(paramCode.code, i);
this.codeList.setType(paramCode.type, i);
}  return;
} 
throw new MathFunctionException("Implementation error: unrecognized code type");
}

public double parse(String paramString) throws MathFunctionException {
this.parsedExpression = paramString;

int i = paramString.length();

initialize();
if (i < 1) throw new MathFunctionException("Null expression");

checkSubclass();

Vector vector = new Vector(i);

codify(paramString, vector);

for (byte b = 0; b < this.clv.getNumberOfVariables(); ) { if (this.var_flag[b]) this.nVarFound++;  b++; }

if (getImplicitCode() > 0) insert(vector);

check_syntax(vector);

code_list_parse(vector);

if (getNumberOfVariablesUsed() > 0) {
return Double.NaN;
}
return this.codeList.getValue();
}

private double parse_number(String paramString) throws MathFunctionException {
ParsePosition parsePosition = new ParsePosition(0);
Number number = this.scientificFormat.parse(paramString, parsePosition);
if (number == null) {

parsePosition.setIndex(0);
number = this.decimalFormat.parse(paramString, parsePosition);
if (number == null) {
throw new MathFunctionException("Error in number constant \"" + paramString.substring(0, parsePosition.getErrorIndex() + 1) + "\"");
}
} 
this.count = parsePosition.getIndex();
return number.doubleValue();
}

private Code pattern_match(String paramString, int paramInt) throws MathFunctionException {
if (paramString.charAt(0) == '(') { this.count = 1; return new Code(this, 4, 4); }
if (paramString.charAt(0) == ')') { this.count = 1; return new Code(this, 5, 5); }

int i = 0;

int j = paramString.length();

for (i = 0; i < getNumberOfTokens(); i++) {

String str = getToken(i);

this.count = str.length();
if (paramString.substring(0, Math.min(this.count, j)).equalsIgnoreCase(str)) {

int k = getCode(i);
int m = getType(k);
if (k != paramInt) {
return (m == 3) ? new Code(this, k, m, getConstant(k)) : new Code(this, k, m);
}
} 
} 

for (i = this.clv.getNumberOfVariables() - 1; i >= 0; i--) {

String str = this.clv.getVariableName(i);
this.count = str.length();
if (paramString.substring(0, Math.min(this.count, j)).equalsIgnoreCase(str))
{

return new Code(this, i, 0);
}
} 

this.count = 1;
throw new MathFunctionException("Unrecognized sequence: " + paramString.substring(0, Math.min(4, paramString.length() - 1)) + "...");
}

private int precedence(int paramInt) throws MathFunctionException {
if (paramInt == 8) return 0; 
if (paramInt == 4 || paramInt == 5) return 1; 
for (byte b = 0; b < getNumberOfTokens(); b++) {
if (paramInt == getCode(b)) {

int i = getPrecedence(b);
if (i < 2) {
throw new MathFunctionException("Implementation error: precedence must be > 1");
}
return i;
} 
}  throw new MathFunctionException("Implementation error: unrecognized code");
}

public void resetEvalCount() {
this.eval_count = 0L;
}

public void resetVariables(MathFunctionVariables paramMathFunctionVariables) {
if (paramMathFunctionVariables == null) {
this.clv = new ConstantMathFunction(this);
} else {
this.clv = paramMathFunctionVariables;
} 
int i = this.clv.getNumberOfVariables();
if (i == 0) {
this.var_flag = null;
} else {

this.var_flag = new boolean[i];
} 
}

public void setDecimalFormat(DecimalFormat paramDecimalFormat) {
this.decimalFormat = paramDecimalFormat;
}

public void setDecimalFormatPattern(String paramString) {
this.decimalFormat.applyPattern(paramString);
}

public void setScientificFormat(DecimalFormat paramDecimalFormat) {
this.scientificFormat = paramDecimalFormat;
}

public void setScientificFormatPattern(String paramString) {
this.scientificFormat.applyPattern(paramString);
}

private String tokenToString(int paramInt) {
if (paramInt < 0) return "L" + this.codeList.getLabelLine(paramInt); 
return getTokenFromCode(paramInt);
}

public String toString() {
StringBuffer stringBuffer = new StringBuffer(getSummary());
stringBuffer.append("\n\nCode list");
if (this.codeList.size() < 1) stringBuffer.append(" empty"); 
for (byte b = 1; b <= this.codeList.size(); b++) {

stringBuffer.append("\nL" + b + " ");
int i = this.codeList.getCode(b);
int j = this.codeList.getType(b);
switch (j) {
case 2:
stringBuffer.append(tokenToString(this.codeList.getLeft(b)) + " ");
case 1:
stringBuffer.append(tokenToString(i) + " ");
stringBuffer.append(tokenToString(this.codeList.getRight(b)));
break;
case 3:
stringBuffer.append(this.codeList.getValue(b));
break;

case 0:
stringBuffer.append(this.clv.getVariableName(i));
break;
default:
stringBuffer.append("??");
break;
} 
} 
stringBuffer.append("\n");
return stringBuffer.toString();
}

public boolean variableUsed(int paramInt) {
return this.var_flag[paramInt];
}

public boolean variableUsed(String paramString) {
for (byte b = 0; b < this.clv.getNumberOfVariables(); b++) {
if (this.var_flag[b] && paramString.equalsIgnoreCase(this.clv.getVariableName(b)))
return true; 
}  return false;
}

public abstract double binaryOperation(int paramInt, double paramDouble1, double paramDouble2) throws MathFunctionException;

public abstract int getCode(int paramInt);

public abstract double getConstant(int paramInt);

public abstract int getImplicitCode();

public abstract int getNumberOfTokens();

public abstract int getPrecedence(int paramInt);

public abstract String getToken(int paramInt);

public abstract int getType(int paramInt);

public abstract boolean isAmbiguous(int paramInt);

public abstract double unaryOperation(int paramInt, double paramDouble) throws MathFunctionException;

private class Code
{
int code;

int type;

double constant;

private final AbstractMathFunction this$0;

Code(AbstractMathFunction this$0, int param1Int1, int param1Int2) {
this.this$0 = this$0; this.code = param1Int1; this.type = param1Int2; this.constant = Double.NaN; } Code(AbstractMathFunction this$0, int param1Int1, int param1Int2, double param1Double) {
this.this$0 = this$0; this.code = param1Int1; this.type = param1Int2; this.constant = param1Double;
}
}

class ConstantMathFunction
implements MathFunctionVariables {
private final AbstractMathFunction this$0;

ConstantMathFunction(AbstractMathFunction this$0) {
this.this$0 = this$0;
}
public int getNumberOfVariables() { return 0; }
public String getVariableName(int param1Int) { return ""; } public double getVariableValue(int param1Int) {
return 0.0D;
}
}
}

