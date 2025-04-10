package bsh;

class BSHEnhancedForStatement
extends SimpleNode
implements ParserConstants
{
String varName;

BSHEnhancedForStatement(int id) {
super(id);
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
SimpleNode expression;
Class elementType = null;
SimpleNode statement = null;

NameSpace enclosingNameSpace = callstack.top();
SimpleNode firstNode = (SimpleNode)jjtGetChild(0);
int nodeCount = jjtGetNumChildren();

if (firstNode instanceof BSHType) {

elementType = ((BSHType)firstNode).getType(callstack, interpreter);
expression = (SimpleNode)jjtGetChild(1);
if (nodeCount > 2) {
statement = (SimpleNode)jjtGetChild(2);
}
} else {
expression = firstNode;
if (nodeCount > 1) {
statement = (SimpleNode)jjtGetChild(1);
}
} 
BlockNameSpace eachNameSpace = new BlockNameSpace(enclosingNameSpace);
callstack.swap(eachNameSpace);

Object iteratee = expression.eval(callstack, interpreter);

if (iteratee == Primitive.NULL) {
throw new EvalError("The collection, array, map, iterator, or enumeration portion of a for statement cannot be null.", this, callstack);
}

CollectionManager cm = CollectionManager.getCollectionManager();
if (!cm.isBshIterable(iteratee)) {
throw new EvalError("Can't iterate over type: " + iteratee.getClass(), this, callstack);
}
BshIterator iterator = cm.getBshIterator(iteratee);

Object returnControl = Primitive.VOID;
while (iterator.hasNext()) {

try {
if (elementType != null)
{ eachNameSpace.setTypedVariable(this.varName, elementType, iterator.next(), new Modifiers()); }

else

{ eachNameSpace.setVariable(this.varName, iterator.next(), false); } 
} catch (UtilEvalError e) {
throw e.toEvalError("for loop iterator variable:" + this.varName, this, callstack);
} 

boolean breakout = false;
if (statement != null) {

Object ret = statement.eval(callstack, interpreter);

if (ret instanceof ReturnControl)
{
switch (((ReturnControl)ret).kind) {

case 46:
returnControl = ret;
breakout = true;
break;

case 12:
breakout = true;
break;
} 

}
} 
if (breakout) {
break;
}
} 
callstack.swap(enclosingNameSpace);
return returnControl;
}
}

