package ch.qos.logback.core.subst;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ScanException;
import java.util.List;

public class Parser
{
final List<Token> tokenList;
int pointer = 0;

public Parser(List<Token> tokenList) {
this.tokenList = tokenList;
}

public Node parse() throws ScanException {
if (this.tokenList == null || this.tokenList.isEmpty())
return null; 
return E();
}

private Node E() throws ScanException {
Node t = T();
if (t == null) {
return null;
}
Node eOpt = Eopt();
if (eOpt != null) {
t.append(eOpt);
}
return t;
}

private Node Eopt() throws ScanException {
Token next = peekAtCurentToken();
if (next == null) {
return null;
}
return E();
}
private Node T() throws ScanException {
Node innerNode;
Token right;
Node curlyLeft, v;
Token w, t = peekAtCurentToken();

switch (t.type) {
case LITERAL:
advanceTokenPointer();
return makeNewLiteralNode(t.payload);
case CURLY_LEFT:
advanceTokenPointer();
innerNode = C();
right = peekAtCurentToken();
expectCurlyRight(right);
advanceTokenPointer();
curlyLeft = makeNewLiteralNode(CoreConstants.LEFT_ACCOLADE);
curlyLeft.append(innerNode);
curlyLeft.append(makeNewLiteralNode(CoreConstants.RIGHT_ACCOLADE));
return curlyLeft;
case START:
advanceTokenPointer();
v = V();
w = peekAtCurentToken();
expectCurlyRight(w);
advanceTokenPointer();
return v;
} 
return null;
}

private Node makeNewLiteralNode(String s) {
return new Node(Node.Type.LITERAL, s);
}

private Node V() throws ScanException {
Node e = E();
Node variable = new Node(Node.Type.VARIABLE, e);
Token t = peekAtCurentToken();
if (isDefaultToken(t)) {
advanceTokenPointer();
Node def = E();
variable.defaultPart = def;
} 
return variable;
}

private Node C() throws ScanException {
Node e0 = E();
Token t = peekAtCurentToken();
if (isDefaultToken(t)) {
advanceTokenPointer();
Node literal = makeNewLiteralNode(":-");
e0.append(literal);
Node e1 = E();
e0.append(e1);
} 
return e0;
}

private boolean isDefaultToken(Token t) {
return (t != null && t.type == Token.Type.DEFAULT);
}

void advanceTokenPointer() {
this.pointer++;
}

void expectNotNull(Token t, String expected) {
if (t == null) {
throw new IllegalArgumentException("All tokens consumed but was expecting \"" + expected + "\"");
}
}

void expectCurlyRight(Token t) throws ScanException {
expectNotNull(t, "}");
if (t.type != Token.Type.CURLY_RIGHT) {
throw new ScanException("Expecting }");
}
}

Token peekAtCurentToken() {
if (this.pointer < this.tokenList.size()) {
return this.tokenList.get(this.pointer);
}
return null;
}
}

