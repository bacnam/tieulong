package ch.qos.logback.core.pattern.parser;

import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.FormatInfo;
import ch.qos.logback.core.pattern.IdentityCompositeConverter;
import ch.qos.logback.core.pattern.ReplacingCompositeConverter;
import ch.qos.logback.core.pattern.util.IEscapeUtil;
import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.ScanException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser<E>
extends ContextAwareBase
{
public static final String MISSING_RIGHT_PARENTHESIS = "http:
public static final Map<String, String> DEFAULT_COMPOSITE_CONVERTER_MAP = new HashMap<String, String>();

static {
DEFAULT_COMPOSITE_CONVERTER_MAP.put(Token.BARE_COMPOSITE_KEYWORD_TOKEN.getValue().toString(), IdentityCompositeConverter.class.getName());

DEFAULT_COMPOSITE_CONVERTER_MAP.put("replace", ReplacingCompositeConverter.class.getName());
}

public static final String REPLACE_CONVERTER_WORD = "replace";
final List tokenList;
int pointer = 0;

Parser(TokenStream ts) throws ScanException {
this.tokenList = ts.tokenize();
}

public Parser(String pattern) throws ScanException {
this(pattern, (IEscapeUtil)new RegularEscapeUtil());
}

public Parser(String pattern, IEscapeUtil escapeUtil) throws ScanException {
try {
TokenStream ts = new TokenStream(pattern, escapeUtil);
this.tokenList = ts.tokenize();
} catch (IllegalArgumentException npe) {
throw new ScanException("Failed to initialize Parser", npe);
} 
}

public Converter<E> compile(Node top, Map converterMap) {
Compiler<E> compiler = new Compiler<E>(top, converterMap);
compiler.setContext(this.context);

return compiler.compile();
}

public Node parse() throws ScanException {
return E();
}

Node E() throws ScanException {
Node t = T();
if (t == null) {
return null;
}
Node eOpt = Eopt();
if (eOpt != null) {
t.setNext(eOpt);
}
return t;
}

Node Eopt() throws ScanException {
Token next = getCurentToken();

if (next == null) {
return null;
}
return E();
}

Node T() throws ScanException {
Token u;
FormattingNode c;
Token t = getCurentToken();
expectNotNull(t, "a LITERAL or '%'");

switch (t.getType()) {
case 1000:
advanceTokenPointer();
return new Node(0, t.getValue());
case 37:
advanceTokenPointer();

u = getCurentToken();

expectNotNull(u, "a FORMAT_MODIFIER, SIMPLE_KEYWORD or COMPOUND_KEYWORD");
if (u.getType() == 1002) {
FormatInfo fi = FormatInfo.valueOf((String)u.getValue());
advanceTokenPointer();
c = C();
c.setFormatInfo(fi);
} else {
c = C();
} 
return c;
} 

return null;
}

FormattingNode C() throws ScanException {
Token t = getCurentToken();

expectNotNull(t, "a LEFT_PARENTHESIS or KEYWORD");
int type = t.getType();
switch (type) {
case 1004:
return SINGLE();
case 1005:
advanceTokenPointer();
return COMPOSITE(t.getValue().toString());
} 
throw new IllegalStateException("Unexpected token " + t);
}

FormattingNode SINGLE() throws ScanException {
Token t = getNextToken();

SimpleKeywordNode keywordNode = new SimpleKeywordNode(t.getValue());

Token ot = getCurentToken();
if (ot != null && ot.getType() == 1006) {
List<String> optionList = (List<String>)ot.getValue();
keywordNode.setOptions(optionList);
advanceTokenPointer();
} 
return keywordNode;
}

FormattingNode COMPOSITE(String keyword) throws ScanException {
CompositeNode compositeNode = new CompositeNode(keyword);

Node childNode = E();
compositeNode.setChildNode(childNode);

Token t = getNextToken();

if (t == null || t.getType() != 41) {
String msg = "Expecting RIGHT_PARENTHESIS token but got " + t;
addError(msg);
addError("See also http:
throw new ScanException(msg);
} 
Token ot = getCurentToken();
if (ot != null && ot.getType() == 1006) {
List<String> optionList = (List<String>)ot.getValue();
compositeNode.setOptions(optionList);
advanceTokenPointer();
} 
return compositeNode;
}

Token getNextToken() {
if (this.pointer < this.tokenList.size()) {
return this.tokenList.get(this.pointer++);
}
return null;
}

Token getCurentToken() {
if (this.pointer < this.tokenList.size()) {
return this.tokenList.get(this.pointer);
}
return null;
}

void advanceTokenPointer() {
this.pointer++;
}

void expectNotNull(Token t, String expected) {
if (t == null)
throw new IllegalStateException("All tokens consumed but was expecting " + expected); 
}
}

