package bsh;

class BSHBlock
extends SimpleNode
{
public boolean isSynchronized = false;

BSHBlock(int id) {
super(id);
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
return eval(callstack, interpreter, false);
}

public Object eval(CallStack callstack, Interpreter interpreter, boolean overrideNamespace) throws EvalError {
Object ret, syncValue = null;
if (this.isSynchronized) {

SimpleNode exp = (SimpleNode)jjtGetChild(0);
syncValue = exp.eval(callstack, interpreter);
} 

if (this.isSynchronized) {
synchronized (syncValue) {

ret = evalBlock(callstack, interpreter, overrideNamespace, (NodeFilter)null);
} 
} else {

ret = evalBlock(callstack, interpreter, overrideNamespace, (NodeFilter)null);
} 

return ret;
}

Object evalBlock(CallStack callstack, Interpreter interpreter, boolean overrideNamespace, NodeFilter nodeFilter) throws EvalError {
Object ret = Primitive.VOID;
NameSpace enclosingNameSpace = null;
if (!overrideNamespace) {

enclosingNameSpace = callstack.top();
BlockNameSpace bodyNameSpace = new BlockNameSpace(enclosingNameSpace);

callstack.swap(bodyNameSpace);
} 

int startChild = this.isSynchronized ? 1 : 0;
int numChildren = jjtGetNumChildren();

try {
int i;

for (i = startChild; i < numChildren; i++) {

SimpleNode node = (SimpleNode)jjtGetChild(i);

if (nodeFilter == null || nodeFilter.isVisible(node))
{

if (node instanceof BSHClassDeclaration)
node.eval(callstack, interpreter);  } 
} 
for (i = startChild; i < numChildren; i++) {

SimpleNode node = (SimpleNode)jjtGetChild(i);
if (!(node instanceof BSHClassDeclaration))
{

if (nodeFilter == null || nodeFilter.isVisible(node)) {

ret = node.eval(callstack, interpreter);

if (ret instanceof ReturnControl)
break; 
}  } 
} 
} finally {
if (!overrideNamespace)
callstack.swap(enclosingNameSpace); 
} 
return ret;
}

public static interface NodeFilter {
boolean isVisible(SimpleNode param1SimpleNode);
}
}

