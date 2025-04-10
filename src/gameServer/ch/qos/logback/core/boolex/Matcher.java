package ch.qos.logback.core.boolex;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Matcher
extends ContextAwareBase
implements LifeCycle
{
private String regex;
private String name;
private boolean caseSensitive = true;
private boolean canonEq = false;
private boolean unicodeCase = false;
private boolean start = false;
private Pattern pattern;

public String getRegex() {
return this.regex;
}

public void setRegex(String regex) {
this.regex = regex;
}

public void start() {
if (this.name == null) {
addError("All Matcher objects must be named");
return;
} 
try {
int code = 0;
if (!this.caseSensitive) {
code |= 0x2;
}
if (this.canonEq) {
code |= 0x80;
}
if (this.unicodeCase) {
code |= 0x40;
}

this.pattern = Pattern.compile(this.regex, code);
this.start = true;
} catch (PatternSyntaxException pse) {
addError("Failed to compile regex [" + this.regex + "]", pse);
} 
}

public void stop() {
this.start = false;
}

public boolean isStarted() {
return this.start;
}

public boolean matches(String input) throws EvaluationException {
if (this.start) {
java.util.regex.Matcher matcher = this.pattern.matcher(input);
return matcher.find();
} 
throw new EvaluationException("Matcher [" + this.regex + "] not started");
}

public boolean isCanonEq() {
return this.canonEq;
}

public void setCanonEq(boolean canonEq) {
this.canonEq = canonEq;
}

public boolean isCaseSensitive() {
return this.caseSensitive;
}

public void setCaseSensitive(boolean caseSensitive) {
this.caseSensitive = caseSensitive;
}

public boolean isUnicodeCase() {
return this.unicodeCase;
}

public void setUnicodeCase(boolean unicodeCase) {
this.unicodeCase = unicodeCase;
}

public String getName() {
return this.name;
}

public void setName(String name) {
this.name = name;
}
}

