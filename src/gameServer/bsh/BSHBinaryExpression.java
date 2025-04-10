package bsh;

class BSHBinaryExpression
extends SimpleNode
implements ParserConstants
{
public int kind;

BSHBinaryExpression(int id) {
super(id);
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
Object lhs = ((SimpleNode)jjtGetChild(0)).eval(callstack, interpreter);

if (this.kind == 35) {

if (lhs == Primitive.NULL) {
return new Primitive(false);
}
Class<Primitive> clazz = ((BSHType)jjtGetChild(1)).getType(callstack, interpreter);

if (lhs instanceof Primitive) {
if (clazz == Primitive.class) {
return new Primitive(true);
}
return new Primitive(false);
} 

boolean ret = Types.isJavaBaseAssignable(clazz, lhs.getClass());
return new Primitive(ret);
} 

if (this.kind == 98 || this.kind == 99) {
Object obj = lhs;
if (isPrimitiveValue(lhs))
obj = ((Primitive)lhs).getValue(); 
if (obj instanceof Boolean && !((Boolean)obj).booleanValue())
{
return new Primitive(false);
}
} 

if (this.kind == 96 || this.kind == 97) {
Object obj = lhs;
if (isPrimitiveValue(lhs))
obj = ((Primitive)lhs).getValue(); 
if (obj instanceof Boolean && ((Boolean)obj).booleanValue() == true)
{
return new Primitive(true);
}
} 

boolean isLhsWrapper = isWrapper(lhs);
Object rhs = ((SimpleNode)jjtGetChild(1)).eval(callstack, interpreter);
boolean isRhsWrapper = isWrapper(rhs);
if ((isLhsWrapper || isPrimitiveValue(lhs)) && (isRhsWrapper || isPrimitiveValue(rhs)))
{

if (!isLhsWrapper || !isRhsWrapper || this.kind != 90) {

try {

return Primitive.binaryOperation(lhs, rhs, this.kind);
} catch (UtilEvalError e) {
throw e.toEvalError(this, callstack);
} 
}
}

switch (this.kind) {

case 90:
return new Primitive((lhs == rhs));

case 95:
return new Primitive((lhs != rhs));

case 102:
if (lhs instanceof String || rhs instanceof String) {
return lhs.toString() + rhs.toString();
}
break;
} 

if (lhs instanceof Primitive || rhs instanceof Primitive) {
if (lhs == Primitive.VOID || rhs == Primitive.VOID) {
throw new EvalError("illegal use of undefined variable, class, or 'void' literal", this, callstack);
}

if (lhs == Primitive.NULL || rhs == Primitive.NULL) {
throw new EvalError("illegal use of null value or 'null' literal", this, callstack);
}
} 
throw new EvalError("Operator: '" + tokenImage[this.kind] + "' inappropriate for objects", this, callstack);
}

private boolean isPrimitiveValue(Object obj) {
return (obj instanceof Primitive && obj != Primitive.VOID && obj != Primitive.NULL);
}

private boolean isWrapper(Object obj) {
return (obj instanceof Boolean || obj instanceof Character || obj instanceof Number);
}
}

