package bsh;

class BSHMethodDeclaration
extends SimpleNode
{
public String name;
BSHReturnType returnTypeNode;
BSHFormalParameters paramsNode;
BSHBlock blockNode;
int firstThrowsClause;
public Modifiers modifiers;
Class returnType;
int numThrows = 0;
BSHMethodDeclaration(int id) {
super(id);
}

synchronized void insureNodesParsed() {
if (this.paramsNode != null) {
return;
}
Object firstNode = jjtGetChild(0);
this.firstThrowsClause = 1;
if (firstNode instanceof BSHReturnType) {

this.returnTypeNode = (BSHReturnType)firstNode;
this.paramsNode = (BSHFormalParameters)jjtGetChild(1);
if (jjtGetNumChildren() > 2 + this.numThrows)
this.blockNode = (BSHBlock)jjtGetChild(2 + this.numThrows); 
this.firstThrowsClause++;
}
else {

this.paramsNode = (BSHFormalParameters)jjtGetChild(0);
this.blockNode = (BSHBlock)jjtGetChild(1 + this.numThrows);
} 
}

Class evalReturnType(CallStack callstack, Interpreter interpreter) throws EvalError {
insureNodesParsed();
if (this.returnTypeNode != null) {
return this.returnTypeNode.evalReturnType(callstack, interpreter);
}
return null;
}

String getReturnTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
insureNodesParsed();
if (this.returnTypeNode == null) {
return null;
}
return this.returnTypeNode.getTypeDescriptor(callstack, interpreter, defaultPackage);
}

BSHReturnType getReturnTypeNode() {
insureNodesParsed();
return this.returnTypeNode;
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
this.returnType = evalReturnType(callstack, interpreter);
evalNodes(callstack, interpreter);

NameSpace namespace = callstack.top();
BshMethod bshMethod = new BshMethod(this, namespace, this.modifiers);
try {
namespace.setMethod(this.name, bshMethod);
} catch (UtilEvalError e) {
throw e.toEvalError(this, callstack);
} 

return Primitive.VOID;
}

private void evalNodes(CallStack callstack, Interpreter interpreter) throws EvalError {
insureNodesParsed();

int i;
for (i = this.firstThrowsClause; i < this.numThrows + this.firstThrowsClause; i++) {
((BSHAmbiguousName)jjtGetChild(i)).toClass(callstack, interpreter);
}

this.paramsNode.eval(callstack, interpreter);

if (interpreter.getStrictJava()) {

for (i = 0; i < this.paramsNode.paramTypes.length; i++) {
if (this.paramsNode.paramTypes[i] == null)
{

throw new EvalError("(Strict Java Mode) Undeclared argument type, parameter: " + this.paramsNode.getParamNames()[i] + " in method: " + this.name, this, null);
}
} 

if (this.returnType == null)
{

throw new EvalError("(Strict Java Mode) Undeclared return type for method: " + this.name, this, null);
}
} 
}

public String toString() {
return "MethodDeclaration: " + this.name;
}
}

