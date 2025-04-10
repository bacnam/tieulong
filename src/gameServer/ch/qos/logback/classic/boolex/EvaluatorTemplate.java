package ch.qos.logback.classic.boolex;

import ch.qos.logback.classic.spi.ILoggingEvent;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class EvaluatorTemplate
implements IEvaluator, GroovyObject
{
public EvaluatorTemplate() {
CallSite[] arrayOfCallSite = $getCallSiteArray();
MetaClass metaClass = $getStaticMetaClass();
this.metaClass = metaClass;
}

public boolean doEvaluate(ILoggingEvent event) {
CallSite[] arrayOfCallSite = $getCallSiteArray(); ILoggingEvent e = event; return DefaultTypeTransformation.booleanUnbox(e);
}
}

