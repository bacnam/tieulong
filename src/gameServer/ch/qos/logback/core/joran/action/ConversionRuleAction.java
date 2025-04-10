package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.util.OptionHelper;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;

public class ConversionRuleAction
extends Action
{
boolean inError = false;

public void begin(InterpretationContext ec, String localName, Attributes attributes) {
this.inError = false;

String conversionWord = attributes.getValue("conversionWord");

String converterClass = attributes.getValue("converterClass");

if (OptionHelper.isEmpty(conversionWord)) {
this.inError = true;
String errorMsg = "No 'conversionWord' attribute in <conversionRule>";
addError(errorMsg);

return;
} 

if (OptionHelper.isEmpty(converterClass)) {
this.inError = true;
String errorMsg = "No 'converterClass' attribute in <conversionRule>";
ec.addError(errorMsg);

return;
} 

try {
Map<String, String> ruleRegistry = (Map<String, String>)this.context.getObject("PATTERN_RULE_REGISTRY");
if (ruleRegistry == null) {
ruleRegistry = new HashMap<String, String>();
this.context.putObject("PATTERN_RULE_REGISTRY", ruleRegistry);
} 

addInfo("registering conversion word " + conversionWord + " with class [" + converterClass + "]");
ruleRegistry.put(conversionWord, converterClass);
} catch (Exception oops) {
this.inError = true;
String errorMsg = "Could not add conversion rule to PatternLayout.";
addError(errorMsg);
} 
}

public void end(InterpretationContext ec, String n) {}

public void finish(InterpretationContext ec) {}
}

