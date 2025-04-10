package bsh;

class BSHTernaryExpression
extends SimpleNode
{
BSHTernaryExpression(int id) {
super(id);
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
SimpleNode cond = (SimpleNode)jjtGetChild(0);
SimpleNode evalTrue = (SimpleNode)jjtGetChild(1);
SimpleNode evalFalse = (SimpleNode)jjtGetChild(2);

if (BSHIfStatement.evaluateCondition(cond, callstack, interpreter)) {
return evalTrue.eval(callstack, interpreter);
}
return evalFalse.eval(callstack, interpreter);
}
}

