package com.mchange.v2.naming;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;

public final class ReferenceableUtils
{
static final MLogger logger = MLog.getLogger(ReferenceableUtils.class);

static final String REFADDR_VERSION = "version";

static final String REFADDR_CLASSNAME = "classname";

static final String REFADDR_FACTORY = "factory";

static final String REFADDR_FACTORY_CLASS_LOCATION = "factoryClassLocation";

static final String REFADDR_SIZE = "size";

static final int CURRENT_REF_VERSION = 1;

public static String literalNullToNull(String paramString) {
if (paramString == null || "null".equals(paramString)) {
return null;
}
return paramString;
}

public static Object referenceToObject(Reference paramReference, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable) throws NamingException {
try {
ClassLoader classLoader;
String str1 = paramReference.getFactoryClassName();
String str2 = paramReference.getFactoryClassLocation();

if (str2 == null) {
classLoader = ClassLoader.getSystemClassLoader();
} else {

URL uRL = new URL(str2);
classLoader = new URLClassLoader(new URL[] { uRL }, ClassLoader.getSystemClassLoader());
} 

Class<?> clazz = Class.forName(str1, true, classLoader);
ObjectFactory objectFactory = (ObjectFactory)clazz.newInstance();
return objectFactory.getObjectInstance(paramReference, paramName, paramContext, paramHashtable);
}
catch (Exception exception) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "Could not resolve Reference to Object!", exception);
}
NamingException namingException = new NamingException("Could not resolve Reference to Object!");
namingException.setRootCause(exception);
throw namingException;
} 
}

public static void appendToReference(Reference paramReference1, Reference paramReference2) throws NamingException {
int i = paramReference2.size();
paramReference1.add(new StringRefAddr("version", String.valueOf(1)));
paramReference1.add(new StringRefAddr("classname", paramReference2.getClassName()));
paramReference1.add(new StringRefAddr("factory", paramReference2.getFactoryClassName()));
paramReference1.add(new StringRefAddr("factoryClassLocation", paramReference2.getFactoryClassLocation()));

paramReference1.add(new StringRefAddr("size", String.valueOf(i)));
for (byte b = 0; b < i; b++) {
paramReference1.add(paramReference2.get(b));
}
}

public static ExtractRec extractNestedReference(Reference paramReference, int paramInt) throws NamingException {
try {
int i = Integer.parseInt((String)paramReference.get(paramInt++).getContent());
if (i == 1) {

String str1 = (String)paramReference.get(paramInt++).getContent();
String str2 = (String)paramReference.get(paramInt++).getContent();
String str3 = (String)paramReference.get(paramInt++).getContent();

Reference reference = new Reference(str1, str2, str3);

int j = Integer.parseInt((String)paramReference.get(paramInt++).getContent());
for (byte b = 0; b < j; b++)
reference.add(paramReference.get(paramInt++)); 
return new ExtractRec(reference, paramInt);
} 

throw new NamingException("Bad version of nested reference!!!");
}
catch (NumberFormatException numberFormatException) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "Version or size nested reference was not a number!!!", numberFormatException);
}
throw new NamingException("Version or size nested reference was not a number!!!");
} 
}

public static class ExtractRec
{
public Reference ref;

public int index;

private ExtractRec(Reference param1Reference, int param1Int) {
this.ref = param1Reference;
this.index = param1Int;
}
}
}

