package bsh;

class BSHFormalParameters
extends SimpleNode
{
private String[] paramNames;
Class[] paramTypes;
int numArgs;
String[] typeDescriptors;

BSHFormalParameters(int id) {
super(id);
}

void insureParsed() {
if (this.paramNames != null) {
return;
}
this.numArgs = jjtGetNumChildren();
String[] paramNames = new String[this.numArgs];

for (int i = 0; i < this.numArgs; i++) {

BSHFormalParameter param = (BSHFormalParameter)jjtGetChild(i);
paramNames[i] = param.name;
} 

this.paramNames = paramNames;
}

public String[] getParamNames() {
insureParsed();
return this.paramNames;
}

public String[] getTypeDescriptors(CallStack callstack, Interpreter interpreter, String defaultPackage) {
if (this.typeDescriptors != null) {
return this.typeDescriptors;
}
insureParsed();
String[] typeDesc = new String[this.numArgs];

for (int i = 0; i < this.numArgs; i++) {

BSHFormalParameter param = (BSHFormalParameter)jjtGetChild(i);
typeDesc[i] = param.getTypeDescriptor(callstack, interpreter, defaultPackage);
} 

this.typeDescriptors = typeDesc;
return typeDesc;
}

public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
if (this.paramTypes != null) {
return this.paramTypes;
}
insureParsed();
Class[] paramTypes = new Class[this.numArgs];

for (int i = 0; i < this.numArgs; i++) {

BSHFormalParameter param = (BSHFormalParameter)jjtGetChild(i);
paramTypes[i] = (Class)param.eval(callstack, interpreter);
} 

this.paramTypes = paramTypes;

return paramTypes;
}
}

