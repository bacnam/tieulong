package ch.qos.logback.core.subst;

import ch.qos.logback.core.spi.PropertyContainer;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.util.OptionHelper;
import java.util.List;
import java.util.Stack;

public class NodeToStringTransformer
{
final Node node;
final PropertyContainer propertyContainer0;
final PropertyContainer propertyContainer1;

public NodeToStringTransformer(Node node, PropertyContainer propertyContainer0, PropertyContainer propertyContainer1) {
this.node = node;
this.propertyContainer0 = propertyContainer0;
this.propertyContainer1 = propertyContainer1;
}

public NodeToStringTransformer(Node node, PropertyContainer propertyContainer0) {
this(node, propertyContainer0, null);
}

public static String substituteVariable(String input, PropertyContainer pc0, PropertyContainer pc1) throws ScanException {
Node node = tokenizeAndParseString(input);
NodeToStringTransformer nodeToStringTransformer = new NodeToStringTransformer(node, pc0, pc1);
return nodeToStringTransformer.transform();
}

private static Node tokenizeAndParseString(String value) throws ScanException {
Tokenizer tokenizer = new Tokenizer(value);
List<Token> tokens = tokenizer.tokenize();
Parser parser = new Parser(tokens);
return parser.parse();
}

public String transform() throws ScanException {
StringBuilder stringBuilder = new StringBuilder();
compileNode(this.node, stringBuilder, new Stack<Node>());
return stringBuilder.toString();
}

private void compileNode(Node inputNode, StringBuilder stringBuilder, Stack<Node> cycleCheckStack) throws ScanException {
Node n = inputNode;
while (n != null) {
switch (n.type) {
case LITERAL:
handleLiteral(n, stringBuilder);
break;
case VARIABLE:
handleVariable(n, stringBuilder, cycleCheckStack);
break;
} 
n = n.next;
} 
}

private void handleVariable(Node n, StringBuilder stringBuilder, Stack<Node> cycleCheckStack) throws ScanException {
if (haveVisitedNodeAlready(n, cycleCheckStack)) {
cycleCheckStack.push(n);
String error = constructRecursionErrorMessage(cycleCheckStack);
throw new IllegalArgumentException(error);
} 
cycleCheckStack.push(n);

StringBuilder keyBuffer = new StringBuilder();
Node payload = (Node)n.payload;
compileNode(payload, keyBuffer, cycleCheckStack);
String key = keyBuffer.toString();
String value = lookupKey(key);

if (value != null) {
Node innerNode = tokenizeAndParseString(value);
compileNode(innerNode, stringBuilder, cycleCheckStack);
cycleCheckStack.pop();

return;
} 
if (n.defaultPart == null) {
stringBuilder.append(key + "_IS_UNDEFINED");
cycleCheckStack.pop();

return;
} 
Node defaultPart = (Node)n.defaultPart;
StringBuilder defaultPartBuffer = new StringBuilder();
compileNode(defaultPart, defaultPartBuffer, cycleCheckStack);
cycleCheckStack.pop();
String defaultVal = defaultPartBuffer.toString();
stringBuilder.append(defaultVal);
}

private String lookupKey(String key) {
String value = this.propertyContainer0.getProperty(key);
if (value != null) {
return value;
}
if (this.propertyContainer1 != null) {
value = this.propertyContainer1.getProperty(key);
if (value != null) {
return value;
}
} 
value = OptionHelper.getSystemProperty(key, null);
if (value != null) {
return value;
}
value = OptionHelper.getEnv(key);
if (value != null) {
return value;
}

return null;
}

private void handleLiteral(Node n, StringBuilder stringBuilder) {
stringBuilder.append((String)n.payload);
}

private String variableNodeValue(Node variableNode) {
Node literalPayload = (Node)variableNode.payload;
return (String)literalPayload.payload;
}

private String constructRecursionErrorMessage(Stack<Node> recursionNodes) {
StringBuilder errorBuilder = new StringBuilder("Circular variable reference detected while parsing input [");

for (Node stackNode : recursionNodes) {
errorBuilder.append("${").append(variableNodeValue(stackNode)).append("}");
if (recursionNodes.lastElement() != stackNode) {
errorBuilder.append(" --> ");
}
} 
errorBuilder.append("]");
return errorBuilder.toString();
}

private boolean haveVisitedNodeAlready(Node node, Stack<Node> cycleDetectionStack) {
for (Node cycleNode : cycleDetectionStack) {
if (equalNodes(node, cycleNode)) {
return true;
}
} 
return false;
}
private boolean equalNodes(Node node1, Node node2) {
if (node1.type != null && !node1.type.equals(node2.type)) return false; 
if (node1.payload != null && !node1.payload.equals(node2.payload)) return false; 
if (node1.defaultPart != null && !node1.defaultPart.equals(node2.defaultPart)) return false;

return true;
}
}

