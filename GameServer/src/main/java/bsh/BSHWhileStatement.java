package bsh;

class BSHWhileStatement
extends SimpleNode
implements ParserConstants
{
public boolean isDoStatement;

BSHWhileStatement(int id) {
super(id);
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
SimpleNode condExp;
int numChild = jjtGetNumChildren();

SimpleNode body = null;

if (this.isDoStatement) {
condExp = (SimpleNode)jjtGetChild(1);
body = (SimpleNode)jjtGetChild(0);
} else {
condExp = (SimpleNode)jjtGetChild(0);
if (numChild > 1) {
body = (SimpleNode)jjtGetChild(1);
}
} 
boolean doOnceFlag = this.isDoStatement;

while (doOnceFlag || BSHIfStatement.evaluateCondition(condExp, callstack, interpreter)) {

if (body == null) {
continue;
}
Object ret = body.eval(callstack, interpreter);

boolean breakout = false;
if (ret instanceof ReturnControl)
{
switch (((ReturnControl)ret).kind) {

case 46:
return ret;

case 19:
continue;

case 12:
breakout = true;
break;
} 
}
if (breakout) {
break;
}
doOnceFlag = false;
} 

return Primitive.VOID;
}
}

