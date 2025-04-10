package bsh;

public class EvalError
extends Exception
{
SimpleNode node;
String message;
CallStack callstack;

public EvalError(String s, SimpleNode node, CallStack callstack) {
setMessage(s);
this.node = node;

if (callstack != null) {
this.callstack = callstack.copy();
}
}

public String toString() {
String trace;
if (this.node != null) {
trace = " : at Line: " + this.node.getLineNumber() + " : in file: " + this.node.getSourceFile() + " : " + this.node.getText();

}
else {

trace = ": <at unknown location>";
} 
if (this.callstack != null) {
trace = trace + "\n" + getScriptStackTrace();
}
return getMessage() + trace;
}

public void reThrow(String msg) throws EvalError {
prependMessage(msg);
throw this;
}

SimpleNode getNode() {
return this.node;
}

void setNode(SimpleNode node) {
this.node = node;
}

public String getErrorText() {
if (this.node != null) {
return this.node.getText();
}
return "<unknown error>";
}

public int getErrorLineNumber() {
if (this.node != null) {
return this.node.getLineNumber();
}
return -1;
}

public String getErrorSourceFile() {
if (this.node != null) {
return this.node.getSourceFile();
}
return "<unknown file>";
}

public String getScriptStackTrace() {
if (this.callstack == null) {
return "<Unknown>";
}
String trace = "";
CallStack stack = this.callstack.copy();
while (stack.depth() > 0) {

NameSpace ns = stack.pop();
SimpleNode node = ns.getNode();
if (ns.isMethod) {

trace = trace + "\nCalled from method: " + ns.getName();
if (node != null) {
trace = trace + " : at Line: " + node.getLineNumber() + " : in file: " + node.getSourceFile() + " : " + node.getText();
}
} 
} 

return trace;
}

public String getMessage() {
return this.message;
} public void setMessage(String s) {
this.message = s;
}

protected void prependMessage(String s) {
if (s == null) {
return;
}
if (this.message == null) {
this.message = s;
} else {
this.message = s + " : " + this.message;
} 
}
}

