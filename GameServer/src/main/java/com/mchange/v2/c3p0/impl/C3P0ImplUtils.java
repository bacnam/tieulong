package com.mchange.v2.c3p0.impl;

import com.mchange.lang.ByteUtils;
import com.mchange.v1.identicator.Identicator;
import com.mchange.v1.identicator.IdentityHashCodeIdenticator;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.encounter.EncounterCounter;
import com.mchange.v2.encounter.EncounterUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.log.jdk14logging.ForwardingLogger;
import com.mchange.v2.ser.SerializableUtils;
import com.mchange.v2.sql.SqlUtils;
import com.mchange.v2.uid.UidUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public final class C3P0ImplUtils
{
private static final boolean CONDITIONAL_LONG_TOKENS = false;
static final MLogger logger = MLog.getLogger(C3P0ImplUtils.class);

public static final DbAuth NULL_AUTH = new DbAuth(null, null);

public static final Object[] NOARGS = new Object[0];

public static final Logger PARENT_LOGGER = (Logger)new ForwardingLogger(MLog.getLogger("com.mchange.v2.c3p0"), null);

private static final EncounterCounter ID_TOKEN_COUNTER = createEncounterCounter();

public static final String VMID_PROPKEY = "com.mchange.v2.c3p0.VMID";

private static final String VMID_PFX;

private static EncounterCounter createEncounterCounter() {
return EncounterUtils.syncWrap(EncounterUtils.createWeak((Identicator)IdentityHashCodeIdenticator.INSTANCE));
}

static {
String vmid = C3P0Config.getPropsFileConfigProperty("com.mchange.v2.c3p0.VMID");
if (vmid == null || (vmid = vmid.trim()).equals("") || vmid.equals("AUTO")) {
VMID_PFX = UidUtils.VM_ID + '|';
} else if (vmid.equals("NONE")) {
VMID_PFX = "";
} else {
VMID_PFX = vmid + "|";
} 
}

static String connectionTesterClassName = null;

private static final String HASM_HEADER = "HexAsciiSerializedMap";

public static String allocateIdentityToken(Object o) {
if (o == null) {
return null;
}

String shortIdToken = Integer.toString(System.identityHashCode(o), 16);

StringBuffer sb = new StringBuffer(128);
sb.append(VMID_PFX); long count;
if (ID_TOKEN_COUNTER != null && (count = ID_TOKEN_COUNTER.encounter(shortIdToken)) > 0L) {

sb.append(shortIdToken);
sb.append('#');
sb.append(count);
} else {

sb.append(shortIdToken);
} 
String out = sb.toString().intern();

return out;
}

public static DbAuth findAuth(Object o) throws SQLException {
if (o == null) {
return NULL_AUTH;
}
String user = null;
String password = null;

String overrideDefaultUser = null;
String overrideDefaultPassword = null;

try {
BeanInfo bi = Introspector.getBeanInfo(o.getClass());
PropertyDescriptor[] pds = bi.getPropertyDescriptors();
for (int i = 0, len = pds.length; i < len; i++) {

PropertyDescriptor pd = pds[i];
Class<?> propCl = pd.getPropertyType();
String propName = pd.getName();
if (propCl == String.class) {

Method readMethod = pd.getReadMethod();
if (readMethod != null) {

Object propVal = readMethod.invoke(o, NOARGS);
String value = (String)propVal;
if ("user".equals(propName)) {
user = value;
} else if ("password".equals(propName)) {
password = value;
} else if ("overrideDefaultUser".equals(propName)) {
overrideDefaultUser = value;
} else if ("overrideDefaultPassword".equals(propName)) {
overrideDefaultPassword = value;
} 
} 
} 
}  if (overrideDefaultUser != null)
return new DbAuth(overrideDefaultUser, overrideDefaultPassword); 
if (user != null) {
return new DbAuth(user, password);
}
return NULL_AUTH;
}
catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "An exception occurred while trying to extract the default authentification info from a bean.", e); 
throw SqlUtils.toSQLException(e);
} 
}

static void resetTxnState(Connection pCon, boolean forceIgnoreUnresolvedTransactions, boolean autoCommitOnClose, boolean txnKnownResolved) throws SQLException {
if (!forceIgnoreUnresolvedTransactions && !pCon.getAutoCommit()) {

if (!autoCommitOnClose && !txnKnownResolved)
{

pCon.rollback();
}
pCon.setAutoCommit(true);
} 
}

public static boolean supportsMethod(Object target, String mname, Class[] argTypes) {
try {
return (target.getClass().getMethod(mname, argTypes) != null);
} catch (NoSuchMethodException e) {
return false;
} catch (SecurityException e) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "We were denied access in a check of whether " + target + " supports method " + mname + ". Prob means external clients have no access, returning false.", e);
}

return false;
} 
}

public static String createUserOverridesAsString(Map userOverrides) throws IOException {
StringBuffer sb = new StringBuffer();
sb.append("HexAsciiSerializedMap");
sb.append('[');
sb.append(ByteUtils.toHexAscii(SerializableUtils.toByteArray(userOverrides)));
sb.append(']');
return sb.toString();
}

public static Map parseUserOverridesAsString(String userOverridesAsString) throws IOException, ClassNotFoundException {
if (userOverridesAsString != null) {

String hexAscii = userOverridesAsString.substring("HexAsciiSerializedMap".length() + 1, userOverridesAsString.length() - 1);
byte[] serBytes = ByteUtils.fromHexAscii(hexAscii);
return Collections.unmodifiableMap((Map<?, ?>)SerializableUtils.fromByteArray(serBytes));
} 

return Collections.EMPTY_MAP;
}

public static void assertCompileTimePresenceOfJdbc4_Jdk17Api(NewProxyConnection npc) throws SQLException {
npc.getNetworkTimeout();
}
}

