package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.PropertyContainer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ClassBodyEvaluator;

public class PropertyEvalScriptBuilder
extends ContextAwareBase
{
private static String SCRIPT_PREFIX = "public boolean evaluate() { return ";

private static String SCRIPT_SUFFIX = "; }";

final PropertyContainer localPropContainer;

Map<String, String> map;

PropertyEvalScriptBuilder(PropertyContainer localPropContainer) {
this.map = new HashMap<String, String>();
this.localPropContainer = localPropContainer;
}

public Condition build(String script) throws IllegalAccessException, CompileException, InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
ClassBodyEvaluator cbe = new ClassBodyEvaluator();
cbe.setImplementedInterfaces(new Class[] { Condition.class });
cbe.setExtendedClass(PropertyWrapperForScripts.class);
cbe.setParentClassLoader(ClassBodyEvaluator.class.getClassLoader());
cbe.cook(SCRIPT_PREFIX + script + SCRIPT_SUFFIX);

Class<?> clazz = cbe.getClazz();
Condition instance = (Condition)clazz.newInstance();
Method setMapMethod = clazz.getMethod("setPropertyContainers", new Class[] { PropertyContainer.class, PropertyContainer.class });
setMapMethod.invoke(instance, new Object[] { this.localPropContainer, this.context });

return instance;
}
}

