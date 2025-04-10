package ch.qos.logback.core.pattern.parser;

import ch.qos.logback.core.pattern.util.IEscapeUtil;
import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import ch.qos.logback.core.spi.ScanException;
import java.util.ArrayList;
import java.util.List;

class TokenStream
{
final String pattern;
final int patternLength;
final IEscapeUtil escapeUtil;

enum TokenizerState
{
LITERAL_STATE, FORMAT_MODIFIER_STATE, KEYWORD_STATE, OPTION_STATE, RIGHT_PARENTHESIS_STATE;
}

final IEscapeUtil optionEscapeUtil = (IEscapeUtil)new RestrictedEscapeUtil();

TokenizerState state = TokenizerState.LITERAL_STATE;
int pointer = 0;

TokenStream(String pattern) {
this(pattern, (IEscapeUtil)new RegularEscapeUtil());
}

TokenStream(String pattern, IEscapeUtil escapeUtil) {
if (pattern == null || pattern.length() == 0) {
throw new IllegalArgumentException("null or empty pattern string not allowed");
}

this.pattern = pattern;
this.patternLength = pattern.length();
this.escapeUtil = escapeUtil;
}

List tokenize() throws ScanException {
List<Token> tokenList = new ArrayList<Token>();
StringBuffer buf = new StringBuffer();

while (this.pointer < this.patternLength) {
char c = this.pattern.charAt(this.pointer);
this.pointer++;

switch (this.state) {
case LITERAL_STATE:
handleLiteralState(c, tokenList, buf);

case FORMAT_MODIFIER_STATE:
handleFormatModifierState(c, tokenList, buf);

case OPTION_STATE:
processOption(c, tokenList, buf);

case KEYWORD_STATE:
handleKeywordState(c, tokenList, buf);

case RIGHT_PARENTHESIS_STATE:
handleRightParenthesisState(c, tokenList, buf);
} 

} 
switch (this.state) {
case LITERAL_STATE:
addValuedToken(1000, buf, tokenList);
break;
case KEYWORD_STATE:
tokenList.add(new Token(1004, buf.toString()));
break;
case RIGHT_PARENTHESIS_STATE:
tokenList.add(Token.RIGHT_PARENTHESIS_TOKEN);
break;

case FORMAT_MODIFIER_STATE:
case OPTION_STATE:
throw new ScanException("Unexpected end of pattern string");
} 

return tokenList;
}

private void handleRightParenthesisState(char c, List<Token> tokenList, StringBuffer buf) {
tokenList.add(Token.RIGHT_PARENTHESIS_TOKEN);
switch (c) {
case ')':
return;
case '{':
this.state = TokenizerState.OPTION_STATE;

case '\\':
escape("%{}", buf);
this.state = TokenizerState.LITERAL_STATE;
} 

buf.append(c);
this.state = TokenizerState.LITERAL_STATE;
}

private void processOption(char c, List<Token> tokenList, StringBuffer buf) throws ScanException {
OptionTokenizer ot = new OptionTokenizer(this);
ot.tokenize(c, tokenList);
}

private void handleFormatModifierState(char c, List<Token> tokenList, StringBuffer buf) {
if (c == '(') {
addValuedToken(1002, buf, tokenList);
tokenList.add(Token.BARE_COMPOSITE_KEYWORD_TOKEN);
this.state = TokenizerState.LITERAL_STATE;
} else if (Character.isJavaIdentifierStart(c)) {
addValuedToken(1002, buf, tokenList);
this.state = TokenizerState.KEYWORD_STATE;
buf.append(c);
} else {
buf.append(c);
} 
}

private void handleLiteralState(char c, List<Token> tokenList, StringBuffer buf) {
switch (c) {
case '\\':
escape("%()", buf);
return;

case '%':
addValuedToken(1000, buf, tokenList);
tokenList.add(Token.PERCENT_TOKEN);
this.state = TokenizerState.FORMAT_MODIFIER_STATE;
return;

case ')':
addValuedToken(1000, buf, tokenList);
this.state = TokenizerState.RIGHT_PARENTHESIS_STATE;
return;
} 

buf.append(c);
}

private void handleKeywordState(char c, List<Token> tokenList, StringBuffer buf) {
if (Character.isJavaIdentifierPart(c)) {
buf.append(c);
} else if (c == '{') {
addValuedToken(1004, buf, tokenList);
this.state = TokenizerState.OPTION_STATE;
} else if (c == '(') {
addValuedToken(1005, buf, tokenList);
this.state = TokenizerState.LITERAL_STATE;
} else if (c == '%') {
addValuedToken(1004, buf, tokenList);
tokenList.add(Token.PERCENT_TOKEN);
this.state = TokenizerState.FORMAT_MODIFIER_STATE;
} else if (c == ')') {
addValuedToken(1004, buf, tokenList);
this.state = TokenizerState.RIGHT_PARENTHESIS_STATE;
} else {
addValuedToken(1004, buf, tokenList);
if (c == '\\') {
if (this.pointer < this.patternLength) {
char next = this.pattern.charAt(this.pointer++);
this.escapeUtil.escape("%()", buf, next, this.pointer);
} 
} else {
buf.append(c);
} 
this.state = TokenizerState.LITERAL_STATE;
} 
}

void escape(String escapeChars, StringBuffer buf) {
if (this.pointer < this.patternLength) {
char next = this.pattern.charAt(this.pointer++);
this.escapeUtil.escape(escapeChars, buf, next, this.pointer);
} 
}

void optionEscape(String escapeChars, StringBuffer buf) {
if (this.pointer < this.patternLength) {
char next = this.pattern.charAt(this.pointer++);
this.optionEscapeUtil.escape(escapeChars, buf, next, this.pointer);
} 
}

private void addValuedToken(int type, StringBuffer buf, List<Token> tokenList) {
if (buf.length() > 0) {
tokenList.add(new Token(type, buf.toString()));
buf.setLength(0);
} 
}
}

