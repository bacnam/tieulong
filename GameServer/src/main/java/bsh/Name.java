package bsh;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

class Name
implements Serializable
{
public NameSpace namespace;
String value = null;

private String evalName;

private String lastEvalName;

private static String FINISHED = null;

private Object evalBaseObject;

private int callstackDepth;

Class asClass;

Class classOfStaticMethod;

private void reset() {
this.evalName = this.value;
this.evalBaseObject = null;
this.callstackDepth = 0;
}

Name(NameSpace namespace, String s) {
this.namespace = namespace;
this.value = s;
}

public Object toObject(CallStack callstack, Interpreter interpreter) throws UtilEvalError {
return toObject(callstack, interpreter, false);
}

public synchronized Object toObject(CallStack callstack, Interpreter interpreter, boolean forceClass) throws UtilEvalError {
reset();

Object obj = null;
while (this.evalName != null) {
obj = consumeNextObjectField(callstack, interpreter, forceClass, false);
}

if (obj == null) {
throw new InterpreterError("null value in toObject()");
}
return obj;
}

private Object completeRound(String lastEvalName, String nextEvalName, Object returnObject) {
if (returnObject == null)
throw new InterpreterError("lastEvalName = " + lastEvalName); 
this.lastEvalName = lastEvalName;
this.evalName = nextEvalName;
this.evalBaseObject = returnObject;
return returnObject;
}

private Object consumeNextObjectField(CallStack callstack, Interpreter interpreter, boolean forceClass, boolean autoAllocateThis) throws UtilEvalError {
if (this.evalBaseObject == null && !isCompound(this.evalName) && !forceClass) {

Object obj = resolveThisFieldReference(callstack, this.namespace, interpreter, this.evalName, false);

if (obj != Primitive.VOID) {
return completeRound(this.evalName, FINISHED, obj);
}
} 

String varName = prefix(this.evalName, 1);
if ((this.evalBaseObject == null || this.evalBaseObject instanceof This) && !forceClass) {
Object obj;

if (Interpreter.DEBUG) {
Interpreter.debug("trying to resolve variable: " + varName);
}

if (this.evalBaseObject == null) {
obj = resolveThisFieldReference(callstack, this.namespace, interpreter, varName, false);
} else {

obj = resolveThisFieldReference(callstack, ((This)this.evalBaseObject).namespace, interpreter, varName, true);
} 

if (obj != Primitive.VOID) {

if (Interpreter.DEBUG) {
Interpreter.debug("resolved variable: " + varName + " in namespace: " + this.namespace);
}

return completeRound(varName, suffix(this.evalName), obj);
} 
} 

if (this.evalBaseObject == null) {

if (Interpreter.DEBUG) {
Interpreter.debug("trying class: " + this.evalName);
}

Class clas = null;
int i = 1;
String className = null;
for (; i <= countParts(this.evalName); i++) {

className = prefix(this.evalName, i);
if ((clas = this.namespace.getClass(className)) != null) {
break;
}
} 
if (clas != null) {
return completeRound(className, suffix(this.evalName, countParts(this.evalName) - i), new ClassIdentifier(clas));
}

if (Interpreter.DEBUG) {
Interpreter.debug("not a class, trying var prefix " + this.evalName);
}
} 

if ((this.evalBaseObject == null || this.evalBaseObject instanceof This) && !forceClass && autoAllocateThis) {

NameSpace targetNameSpace = (this.evalBaseObject == null) ? this.namespace : ((This)this.evalBaseObject).namespace;

Object obj = (new NameSpace(targetNameSpace, "auto: " + varName)).getThis(interpreter);

targetNameSpace.setVariable(varName, obj, false);
return completeRound(varName, suffix(this.evalName), obj);
} 

if (this.evalBaseObject == null) {
if (!isCompound(this.evalName)) {
return completeRound(this.evalName, FINISHED, Primitive.VOID);
}
throw new UtilEvalError("Class or variable not found: " + this.evalName);
} 

if (this.evalBaseObject == Primitive.NULL) {
throw new UtilTargetError(new NullPointerException("Null Pointer while evaluating: " + this.value));
}

if (this.evalBaseObject == Primitive.VOID) {
throw new UtilEvalError("Undefined variable or class name while evaluating: " + this.value);
}

if (this.evalBaseObject instanceof Primitive) {
throw new UtilEvalError("Can't treat primitive like an object. Error while evaluating: " + this.value);
}

if (this.evalBaseObject instanceof ClassIdentifier) {

Class<?> clas = ((ClassIdentifier)this.evalBaseObject).getTargetClass();
String str = prefix(this.evalName, 1);

if (str.equals("this")) {

NameSpace ns = this.namespace;
while (ns != null) {

if (ns.classInstance != null && ns.classInstance.getClass() == clas)
{

return completeRound(str, suffix(this.evalName), ns.classInstance);
}
ns = ns.getParent();
} 
throw new UtilEvalError("Can't find enclosing 'this' instance of class: " + clas);
} 

Object obj = null;

try {
if (Interpreter.DEBUG) {
Interpreter.debug("Name call to getStaticFieldValue, class: " + clas + ", field:" + str);
}
obj = Reflect.getStaticFieldValue(clas, str);
} catch (ReflectError e) {
if (Interpreter.DEBUG) {
Interpreter.debug("field reflect error: " + e);
}
} 

if (obj == null) {
String iclass = clas.getName() + "$" + str;
Class c = this.namespace.getClass(iclass);
if (c != null) {
obj = new ClassIdentifier(c);
}
} 
if (obj == null) {
throw new UtilEvalError("No static field or inner class: " + str + " of " + clas);
}

return completeRound(str, suffix(this.evalName), obj);
} 

if (forceClass) {
throw new UtilEvalError(this.value + " does not resolve to a class name.");
}

String field = prefix(this.evalName, 1);

if (field.equals("length") && this.evalBaseObject.getClass().isArray()) {

Object obj = new Primitive(Array.getLength(this.evalBaseObject));
return completeRound(field, suffix(this.evalName), obj);
} 

try {
Object obj = Reflect.getObjectFieldValue(this.evalBaseObject, field);
return completeRound(field, suffix(this.evalName), obj);
} catch (ReflectError e) {

throw new UtilEvalError("Cannot access field: " + field + ", on object: " + this.evalBaseObject);
} 
}

Object resolveThisFieldReference(CallStack callstack, NameSpace thisNameSpace, Interpreter interpreter, String varName, boolean specialFieldsVisible) throws UtilEvalError {
if (varName.equals("this")) {

if (specialFieldsVisible) {
throw new UtilEvalError("Redundant to call .this on This type");
}

This ths = thisNameSpace.getThis(interpreter);
thisNameSpace = ths.getNameSpace();
Object result = ths;

NameSpace classNameSpace = getClassNameSpace(thisNameSpace);
if (classNameSpace != null)
{
if (isCompound(this.evalName)) {
result = classNameSpace.getThis(interpreter);
} else {
result = classNameSpace.getClassInstance();
} 
}
return result;
} 

if (varName.equals("super")) {

This ths = thisNameSpace.getSuper(interpreter);
thisNameSpace = ths.getNameSpace();

if (thisNameSpace.getParent() != null && (thisNameSpace.getParent()).isClass)
{

ths = thisNameSpace.getParent().getThis(interpreter);
}
return ths;
} 

Object obj = null;

if (varName.equals("global")) {
obj = thisNameSpace.getGlobal(interpreter);
}
if (obj == null && specialFieldsVisible)
{
if (varName.equals("namespace")) {
obj = thisNameSpace;
} else if (varName.equals("variables")) {
obj = thisNameSpace.getVariableNames();
} else if (varName.equals("methods")) {
obj = thisNameSpace.getMethodNames();
} else if (varName.equals("interpreter")) {
if (this.lastEvalName.equals("this")) {
obj = interpreter;
} else {
throw new UtilEvalError("Can only call .interpreter on literal 'this'");
} 
} 
}
if (obj == null && specialFieldsVisible && varName.equals("caller")) {

if (this.lastEvalName.equals("this") || this.lastEvalName.equals("caller")) {

if (callstack == null)
throw new InterpreterError("no callstack"); 
obj = callstack.get(++this.callstackDepth).getThis(interpreter);
}
else {

throw new UtilEvalError("Can only call .caller on literal 'this' or literal '.caller'");
} 

return obj;
} 

if (obj == null && specialFieldsVisible && varName.equals("callstack"))
{

if (this.lastEvalName.equals("this")) {

if (callstack == null)
throw new InterpreterError("no callstack"); 
obj = callstack;
} else {

throw new UtilEvalError("Can only call .callstack on literal 'this'");
} 
}

if (obj == null) {
obj = thisNameSpace.getVariable(varName);
}
if (obj == null) {
throw new InterpreterError("null this field ref:" + varName);
}
return obj;
}

static NameSpace getClassNameSpace(NameSpace thisNameSpace) {
if (thisNameSpace.isClass) {
return thisNameSpace;
}
if (thisNameSpace.isMethod && thisNameSpace.getParent() != null && (thisNameSpace.getParent()).isClass)
{

return thisNameSpace.getParent();
}
return null;
}

public synchronized Class toClass() throws ClassNotFoundException, UtilEvalError {
if (this.asClass != null) {
return this.asClass;
}
reset();

if (this.evalName.equals("var")) {
return this.asClass = null;
}

Class clas = this.namespace.getClass(this.evalName);

if (clas == null) {

Object obj = null;

try {
obj = toObject(null, null, true);
} catch (UtilEvalError e) {}

if (obj instanceof ClassIdentifier) {
clas = ((ClassIdentifier)obj).getTargetClass();
}
} 
if (clas == null) {
throw new ClassNotFoundException("Class: " + this.value + " not found in namespace");
}

this.asClass = clas;
return this.asClass;
}

public synchronized LHS toLHS(CallStack callstack, Interpreter interpreter) throws UtilEvalError {
reset();

if (!isCompound(this.evalName)) {

if (this.evalName.equals("this")) {
throw new UtilEvalError("Can't assign to 'this'.");
}

LHS lhs = new LHS(this.namespace, this.evalName, false);
return lhs;
} 

Object obj = null;
try {
while (this.evalName != null && isCompound(this.evalName))
{
obj = consumeNextObjectField(callstack, interpreter, false, true);

}
}
catch (UtilEvalError e) {
throw new UtilEvalError("LHS evaluation: " + e.getMessage());
} 

if (this.evalName == null && obj instanceof ClassIdentifier) {
throw new UtilEvalError("Can't assign to class: " + this.value);
}
if (obj == null) {
throw new UtilEvalError("Error in LHS: " + this.value);
}

if (obj instanceof This) {

if (this.evalName.equals("namespace") || this.evalName.equals("variables") || this.evalName.equals("methods") || this.evalName.equals("caller"))
{

throw new UtilEvalError("Can't assign to special variable: " + this.evalName);
}

Interpreter.debug("found This reference evaluating LHS");

boolean localVar = !this.lastEvalName.equals("super");
return new LHS(((This)obj).namespace, this.evalName, localVar);
} 

if (this.evalName != null) {

try {
if (obj instanceof ClassIdentifier) {

Class clas = ((ClassIdentifier)obj).getTargetClass();
LHS lHS = Reflect.getLHSStaticField(clas, this.evalName);
return lHS;
} 
LHS lhs = Reflect.getLHSObjectField(obj, this.evalName);
return lhs;
}
catch (ReflectError e) {
throw new UtilEvalError("Field access: " + e);
} 
}

throw new InterpreterError("Internal error in lhs...");
}

public Object invokeMethod(Interpreter interpreter, Object[] args, CallStack callstack, SimpleNode callerInfo) throws UtilEvalError, EvalError, ReflectError, InvocationTargetException {
String methodName = suffix(this.value, 1);
BshClassManager bcm = interpreter.getClassManager();
NameSpace namespace = callstack.top();

if (this.classOfStaticMethod != null)
{
return Reflect.invokeStaticMethod(bcm, this.classOfStaticMethod, methodName, args);
}

if (!isCompound(this.value)) {
return invokeLocalMethod(interpreter, args, callstack, callerInfo);
}

String prefix = prefix(this.value);

if (prefix.equals("super") && countParts(this.value) == 2) {

This ths = namespace.getThis(interpreter);
NameSpace thisNameSpace = ths.getNameSpace();
NameSpace classNameSpace = getClassNameSpace(thisNameSpace);
if (classNameSpace != null) {

Object instance = classNameSpace.getClassInstance();
return ClassGenerator.getClassGenerator().invokeSuperclassMethod(bcm, instance, methodName, args);
} 
} 

Name targetName = namespace.getNameResolver(prefix);
Object obj = targetName.toObject(callstack, interpreter);

if (obj == Primitive.VOID) {
throw new UtilEvalError("Attempt to resolve method: " + methodName + "() on undefined variable or class name: " + targetName);
}

if (!(obj instanceof ClassIdentifier)) {

if (obj instanceof Primitive) {

if (obj == Primitive.NULL) {
throw new UtilTargetError(new NullPointerException("Null Pointer in Method Invocation"));
}

if (Interpreter.DEBUG) {
Interpreter.debug("Attempt to access method on primitive... allowing bsh.Primitive to peek through for debugging");
}
} 

return Reflect.invokeObjectMethod(obj, methodName, args, interpreter, callstack, callerInfo);
} 

if (Interpreter.DEBUG) {
Interpreter.debug("invokeMethod: trying static - " + targetName);
}
Class clas = ((ClassIdentifier)obj).getTargetClass();

this.classOfStaticMethod = clas;

if (clas != null) {
return Reflect.invokeStaticMethod(bcm, clas, methodName, args);
}

throw new UtilEvalError("invokeMethod: unknown target: " + targetName);
}

private Object invokeLocalMethod(Interpreter interpreter, Object[] args, CallStack callstack, SimpleNode callerInfo) throws EvalError {
Object commandObject;
if (Interpreter.DEBUG)
Interpreter.debug("invokeLocalMethod: " + this.value); 
if (interpreter == null) {
throw new InterpreterError("invokeLocalMethod: interpreter = null");
}

String commandName = this.value;
Class[] argTypes = Types.getTypes(args);

BshMethod meth = null;
try {
meth = this.namespace.getMethod(commandName, argTypes);
} catch (UtilEvalError e) {
throw e.toEvalError("Local method invocation", callerInfo, callstack);
} 

if (meth != null) {
return meth.invoke(args, interpreter, callstack, callerInfo);
}
BshClassManager bcm = interpreter.getClassManager();

try {
commandObject = this.namespace.getCommand(commandName, argTypes, interpreter);
}
catch (UtilEvalError e) {
throw e.toEvalError("Error loading command: ", callerInfo, callstack);
} 

if (commandObject == null) {

BshMethod invokeMethod = null;
try {
invokeMethod = this.namespace.getMethod("invoke", new Class[] { null, null });
}
catch (UtilEvalError e) {
throw e.toEvalError("Local method invocation", callerInfo, callstack);
} 

if (invokeMethod != null) {
return invokeMethod.invoke(new Object[] { commandName, args }, interpreter, callstack, callerInfo);
}

throw new EvalError("Command not found: " + StringUtil.methodString(commandName, argTypes), callerInfo, callstack);
} 

if (commandObject instanceof BshMethod) {
return ((BshMethod)commandObject).invoke(args, interpreter, callstack, callerInfo);
}

if (commandObject instanceof Class) {
try {
return Reflect.invokeCompiledCommand((Class)commandObject, args, interpreter, callstack);
}
catch (UtilEvalError e) {
throw e.toEvalError("Error invoking compiled command: ", callerInfo, callstack);
} 
}

throw new InterpreterError("invalid command type");
}

public static boolean isCompound(String value) {
return (value.indexOf('.') != -1);
}

static int countParts(String value) {
if (value == null) {
return 0;
}
int count = 0;
int index = -1;
while ((index = value.indexOf('.', index + 1)) != -1)
count++; 
return count + 1;
}

static String prefix(String value) {
if (!isCompound(value)) {
return null;
}
return prefix(value, countParts(value) - 1);
}

static String prefix(String value, int parts) {
if (parts < 1) {
return null;
}
int count = 0;
int index = -1;

while ((index = value.indexOf('.', index + 1)) != -1 && ++count < parts);

return (index == -1) ? value : value.substring(0, index);
}

static String suffix(String name) {
if (!isCompound(name)) {
return null;
}
return suffix(name, countParts(name) - 1);
}

public static String suffix(String value, int parts) {
if (parts < 1) {
return null;
}
int count = 0;
int index = value.length() + 1;

while ((index = value.lastIndexOf('.', index - 1)) != -1 && ++count < parts);

return (index == -1) ? value : value.substring(index + 1);
}

public String toString() {
return this.value;
}
}

