package org.junit.internal.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@Deprecated
public class MethodValidator
{
private final List<Throwable> errors = new ArrayList<Throwable>();

private TestClass testClass;

public MethodValidator(TestClass testClass) {
this.testClass = testClass;
}

public void validateInstanceMethods() {
validateTestMethods((Class)After.class, false);
validateTestMethods((Class)Before.class, false);
validateTestMethods((Class)Test.class, false);

List<Method> methods = this.testClass.getAnnotatedMethods((Class)Test.class);
if (methods.size() == 0) {
this.errors.add(new Exception("No runnable methods"));
}
}

public void validateStaticMethods() {
validateTestMethods((Class)BeforeClass.class, true);
validateTestMethods((Class)AfterClass.class, true);
}

public List<Throwable> validateMethodsForDefaultRunner() {
validateNoArgConstructor();
validateStaticMethods();
validateInstanceMethods();
return this.errors;
}

public void assertValid() throws InitializationError {
if (!this.errors.isEmpty()) {
throw new InitializationError(this.errors);
}
}

public void validateNoArgConstructor() {
try {
this.testClass.getConstructor();
} catch (Exception e) {
this.errors.add(new Exception("Test class should have public zero-argument constructor", e));
} 
}

private void validateTestMethods(Class<? extends Annotation> annotation, boolean isStatic) {
List<Method> methods = this.testClass.getAnnotatedMethods(annotation);

for (Method each : methods) {
if (Modifier.isStatic(each.getModifiers()) != isStatic) {
String state = isStatic ? "should" : "should not";
this.errors.add(new Exception("Method " + each.getName() + "() " + state + " be static"));
} 

if (!Modifier.isPublic(each.getDeclaringClass().getModifiers())) {
this.errors.add(new Exception("Class " + each.getDeclaringClass().getName() + " should be public"));
}

if (!Modifier.isPublic(each.getModifiers())) {
this.errors.add(new Exception("Method " + each.getName() + " should be public"));
}

if (each.getReturnType() != void.class) {
this.errors.add(new Exception("Method " + each.getName() + " should be void"));
}

if ((each.getParameterTypes()).length != 0)
this.errors.add(new Exception("Method " + each.getName() + " should have no parameters")); 
} 
}
}

