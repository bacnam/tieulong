package bsh;

public class ParseException
extends EvalError
{
String sourceFile = "<unknown>"; protected boolean specialConstructor; public Token currentToken;
public int[][] expectedTokenSequences;
public String[] tokenImage;
protected String eol;

public void setErrorSourceFile(String file) {
this.sourceFile = file;
}

public String getErrorSourceFile() {
return this.sourceFile;
}

public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
this();

this.specialConstructor = true;
this.currentToken = currentTokenVal;
this.expectedTokenSequences = expectedTokenSequencesVal;
this.tokenImage = tokenImageVal;
}

public ParseException() {
this("");

this.specialConstructor = false;
}

public ParseException(String message) {
super(message, null, null);

this.eol = System.getProperty("line.separator", "\n");
this.specialConstructor = false;
}

public String getMessage() {
return getMessage(false);
}

protected String add_escapes(String str) { StringBuffer retval = new StringBuffer();

for (int i = 0; i < str.length(); i++) {
char ch; switch (str.charAt(i)) {
case '\000':
break;

case '\b':
retval.append("\\b");
break;
case '\t':
retval.append("\\t");
break;
case '\n':
retval.append("\\n");
break;
case '\f':
retval.append("\\f");
break;
case '\r':
retval.append("\\r");
break;
case '"':
retval.append("\\\"");
break;
case '\'':
retval.append("\\'");
break;
case '\\':
retval.append("\\\\");
break;
default:
if ((ch = str.charAt(i)) < ' ' || ch > '~') {
String s = "0000" + Integer.toString(ch, 16);
retval.append("\\u" + s.substring(s.length() - 4, s.length())); break;
} 
retval.append(ch);
break;
} 

} 
return retval.toString(); }

public int getErrorLineNumber() {
return this.currentToken.next.beginLine;
} public String getMessage(boolean debug) { if (!this.specialConstructor) return super.getMessage();  String expected = ""; int maxSize = 0; for (int i = 0; i < this.expectedTokenSequences.length; i++) { if (maxSize < (this.expectedTokenSequences[i]).length)
maxSize = (this.expectedTokenSequences[i]).length;  for (int k = 0; k < (this.expectedTokenSequences[i]).length; k++)
expected = expected + this.tokenImage[this.expectedTokenSequences[i][k]] + " ";  if (this.expectedTokenSequences[i][(this.expectedTokenSequences[i]).length - 1] != 0)
expected = expected + "...";  expected = expected + this.eol + "    "; }  String retval = "In file: " + this.sourceFile + " Encountered \""; Token tok = this.currentToken.next; for (int j = 0; j < maxSize; j++) { if (j != 0)
retval = retval + " ";  if (tok.kind == 0) { retval = retval + this.tokenImage[0]; break; }  retval = retval + add_escapes(tok.image); tok = tok.next; }  retval = retval + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn + "." + this.eol; if (debug) { if (this.expectedTokenSequences.length == 1) { retval = retval + "Was expecting:" + this.eol + "    "; } else { retval = retval + "Was expecting one of:" + this.eol + "    "; }  retval = retval + expected; }  return retval; } public String getErrorText() { int maxSize = 0;
for (int i = 0; i < this.expectedTokenSequences.length; i++) {
if (maxSize < (this.expectedTokenSequences[i]).length) {
maxSize = (this.expectedTokenSequences[i]).length;
}
} 
String retval = "";
Token tok = this.currentToken.next;
for (int j = 0; j < maxSize; j++) {

if (j != 0) retval = retval + " "; 
if (tok.kind == 0) {
retval = retval + this.tokenImage[0];
break;
} 
retval = retval + add_escapes(tok.image);
tok = tok.next;
} 

return retval; }

public String toString() {
return getMessage();
}
}

