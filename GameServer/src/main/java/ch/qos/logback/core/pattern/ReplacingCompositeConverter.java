package ch.qos.logback.core.pattern;

import java.util.List;
import java.util.regex.Pattern;

public class ReplacingCompositeConverter<E>
extends CompositeConverter<E>
{
Pattern pattern;
String regex;
String replacement;

public void start() {
List<String> optionList = getOptionList();
if (optionList == null) {
addError("at least two options are expected whereas you have declared none");

return;
} 
int numOpts = optionList.size();

if (numOpts < 2) {
addError("at least two options are expected whereas you have declared only " + numOpts + "as [" + optionList + "]");
return;
} 
this.regex = optionList.get(0);
this.pattern = Pattern.compile(this.regex);
this.replacement = optionList.get(1);
super.start();
}

protected String transform(E event, String in) {
if (!this.started)
return in; 
return this.pattern.matcher(in).replaceAll(this.replacement);
}
}

