package bsh;

class BSHSwitchLabel
extends SimpleNode
{
boolean isDefault;

public BSHSwitchLabel(int id) {
super(id);
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
if (this.isDefault)
return null; 
SimpleNode label = (SimpleNode)jjtGetChild(0);
return label.eval(callstack, interpreter);
}
}

