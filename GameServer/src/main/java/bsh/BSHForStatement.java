package bsh;

class BSHForStatement
extends SimpleNode
implements ParserConstants
{
public boolean hasForInit;
public boolean hasExpression;
public boolean hasForUpdate;
private SimpleNode forInit;
private SimpleNode expression;
private SimpleNode forUpdate;
private SimpleNode statement;
private boolean parsed;

BSHForStatement(int id) {
super(id);
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
int i = 0;
if (this.hasForInit)
this.forInit = (SimpleNode)jjtGetChild(i++); 
if (this.hasExpression)
this.expression = (SimpleNode)jjtGetChild(i++); 
if (this.hasForUpdate)
this.forUpdate = (SimpleNode)jjtGetChild(i++); 
if (i < jjtGetNumChildren()) {
this.statement = (SimpleNode)jjtGetChild(i);
}
NameSpace enclosingNameSpace = callstack.top();
BlockNameSpace forNameSpace = new BlockNameSpace(enclosingNameSpace);

callstack.swap(forNameSpace);

if (this.hasForInit) {
this.forInit.eval(callstack, interpreter);
}
Object returnControl = Primitive.VOID;

while (true) {
if (this.hasExpression) {

boolean cond = BSHIfStatement.evaluateCondition(this.expression, callstack, interpreter);

if (!cond) {
break;
}
} 
boolean breakout = false;
if (this.statement != null) {

Object ret = this.statement.eval(callstack, interpreter);

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
if (this.hasForUpdate) {
this.forUpdate.eval(callstack, interpreter);
}
} 
callstack.swap(enclosingNameSpace);
return returnControl;
}
}

