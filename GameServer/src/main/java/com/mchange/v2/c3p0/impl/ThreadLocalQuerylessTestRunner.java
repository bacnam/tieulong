package com.mchange.v2.c3p0.impl;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLogger;
import java.sql.Connection;
import java.util.Map;
import java.util.WeakHashMap;

class ThreadLocalQuerylessTestRunner
implements DefaultConnectionTester.QuerylessTestRunner
{
static final MLogger logger = DefaultConnectionTester.logger;

private static final ThreadLocal classToTestRunnerThreadLocal = new ThreadLocal() {
protected Object initialValue() {
return new WeakHashMap<Object, Object>();
}
};
private static final Class[] ARG_ARRAY = new Class[] { int.class };

private static Map classToTestRunner() {
return classToTestRunnerThreadLocal.get();
}

private static DefaultConnectionTester.QuerylessTestRunner findTestRunner(Class cClass) {
try {
cClass.getDeclaredMethod("isValid", ARG_ARRAY);
return DefaultConnectionTester.IS_VALID;
}
catch (NoSuchMethodException e) {
return DefaultConnectionTester.METADATA_TABLESEARCH;
} catch (SecurityException e) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Huh? SecurityException while reflectively checking for " + cClass.getName() + ".isValid(). Defaulting to traditional (slow) queryless test."); 
return DefaultConnectionTester.METADATA_TABLESEARCH;
} 
}

public int activeCheckConnectionNoQuery(Connection c, Throwable[] rootCauseOutParamHolder) {
Map<Class<?>, DefaultConnectionTester.QuerylessTestRunner> map = classToTestRunner();
Class<?> cClass = c.getClass();
DefaultConnectionTester.QuerylessTestRunner qtl = (DefaultConnectionTester.QuerylessTestRunner)map.get(cClass);
if (qtl == null) {

qtl = findTestRunner(cClass);
map.put(cClass, qtl);
} 
return qtl.activeCheckConnectionNoQuery(c, rootCauseOutParamHolder);
}
}

