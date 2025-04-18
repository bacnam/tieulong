package ch.qos.logback.core.pattern.parser;

import ch.qos.logback.core.pattern.util.AsIsEscapeUtil;
import ch.qos.logback.core.pattern.util.IEscapeUtil;
import ch.qos.logback.core.spi.ScanException;
import java.util.ArrayList;
import java.util.List;

public class OptionTokenizer
{
private static final int EXPECTING_STATE = 0;
private static final int RAW_COLLECTING_STATE = 1;
private static final int QUOTED_COLLECTING_STATE = 2;
final IEscapeUtil escapeUtil;
final TokenStream tokenStream;
final String pattern;
final int patternLength;
char quoteChar;
int state = 0;

OptionTokenizer(TokenStream tokenStream) {
this(tokenStream, (IEscapeUtil)new AsIsEscapeUtil());
}

OptionTokenizer(TokenStream tokenStream, IEscapeUtil escapeUtil) {
this.tokenStream = tokenStream;
this.pattern = tokenStream.pattern;
this.patternLength = tokenStream.patternLength;
this.escapeUtil = escapeUtil;
}

void tokenize(char firstChar, List<Token> tokenList) throws ScanException {
StringBuffer buf = new StringBuffer();
List<String> optionList = new ArrayList<String>();
char c = firstChar;

while (this.tokenStream.pointer < this.patternLength) {
switch (this.state) {
case 0:
switch (c) {
case '\t':
case '\n':
case '\r':
case ' ':
case ',':
break;
case '"':
case '\'':
this.state = 2;
this.quoteChar = c;
break;
case '}':
emitOptionToken(tokenList, optionList);
return;
} 
buf.append(c);
this.state = 1;
break;

case 1:
switch (c) {
case ',':
optionList.add(buf.toString().trim());
buf.setLength(0);
this.state = 0;
break;
case '}':
optionList.add(buf.toString().trim());
emitOptionToken(tokenList, optionList);
return;
} 
buf.append(c);
break;

case 2:
if (c == this.quoteChar) {
optionList.add(buf.toString());
buf.setLength(0);
this.state = 0; break;
}  if (c == '\\') {
escape(String.valueOf(this.quoteChar), buf); break;
} 
buf.append(c);
break;
} 

c = this.pattern.charAt(this.tokenStream.pointer);
this.tokenStream.pointer++;
} 

if (c == '}') {
if (this.state == 0) {
emitOptionToken(tokenList, optionList);
} else if (this.state == 1) {
optionList.add(buf.toString().trim());
emitOptionToken(tokenList, optionList);
} else {
throw new ScanException("Unexpected end of pattern string in OptionTokenizer");
} 
} else {
throw new ScanException("Unexpected end of pattern string in OptionTokenizer");
} 
}

void emitOptionToken(List<Token> tokenList, List<String> optionList) {
tokenList.add(new Token(1006, optionList));
this.tokenStream.state = TokenStream.TokenizerState.LITERAL_STATE;
}

void escape(String escapeChars, StringBuffer buf) {
if (this.tokenStream.pointer < this.patternLength) {
char next = this.pattern.charAt(this.tokenStream.pointer++);
this.escapeUtil.escape(escapeChars, buf, next, this.tokenStream.pointer);
} 
}
}

