package com.mysql.jdbc;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

public class Util
{
protected static final Method systemNanoTimeMethod;
private static Method CAST_METHOD;

static {
Method method;
try {
method = System.class.getMethod("nanoTime", (Class[])null);
} catch (SecurityException e) {
method = null;
} catch (NoSuchMethodException e) {
method = null;
} 

systemNanoTimeMethod = method;
}

public static boolean nanoTimeAvailable() {
return (systemNanoTimeMethod != null);
}

private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

static final TimeZone getDefaultTimeZone() {
return (TimeZone)DEFAULT_TIMEZONE.clone();
}

class RandStructcture
{
long maxValue;

double maxValueDbl;

long seed1;
long seed2;
}
private static Util enclosingInstance = new Util();

private static boolean isJdbc4 = false;

private static boolean isColdFusion = false;

static {
try {
CAST_METHOD = Class.class.getMethod("cast", new Class[] { Object.class });
}
catch (Throwable t) {}

try {
Class.forName("java.sql.NClob");
isJdbc4 = true;
} catch (Throwable t) {
isJdbc4 = false;
} 

String loadedFrom = stackTraceToString(new Throwable());

if (loadedFrom != null) {
isColdFusion = (loadedFrom.indexOf("coldfusion") != -1);
} else {
isColdFusion = false;
} 
}

public static boolean isJdbc4() {
return isJdbc4;
}

public static boolean isColdFusion() {
return isColdFusion;
}

public static String newCrypt(String password, String seed) {
if (password == null || password.length() == 0) {
return password;
}

long[] pw = newHash(seed);
long[] msg = newHash(password);
long max = 1073741823L;
long seed1 = (pw[0] ^ msg[0]) % max;
long seed2 = (pw[1] ^ msg[1]) % max;
char[] chars = new char[seed.length()];
int i;
for (i = 0; i < seed.length(); i++) {
seed1 = (seed1 * 3L + seed2) % max;
seed2 = (seed1 + seed2 + 33L) % max;
double d1 = seed1 / max;
byte b1 = (byte)(int)Math.floor(d1 * 31.0D + 64.0D);
chars[i] = (char)b1;
} 

seed1 = (seed1 * 3L + seed2) % max;
seed2 = (seed1 + seed2 + 33L) % max;
double d = seed1 / max;
byte b = (byte)(int)Math.floor(d * 31.0D);

for (i = 0; i < seed.length(); i++) {
chars[i] = (char)(chars[i] ^ (char)b);
}

return new String(chars);
}

static long[] newHash(String password) {
long nr = 1345345333L;
long add = 7L;
long nr2 = 305419889L;

for (int i = 0; i < password.length(); i++) {
if (password.charAt(i) != ' ' && password.charAt(i) != '\t') {

long tmp = (0xFF & password.charAt(i));
nr ^= ((nr & 0x3FL) + add) * tmp + (nr << 8L);
nr2 += nr2 << 8L ^ nr;
add += tmp;
} 
} 
long[] result = new long[2];
result[0] = nr & 0x7FFFFFFFL;
result[1] = nr2 & 0x7FFFFFFFL;

return result;
}

public static String oldCrypt(String password, String seed) {
long max = 33554431L;

if (password == null || password.length() == 0) {
return password;
}

long hp = oldHash(seed);
long hm = oldHash(password);

long nr = hp ^ hm;
nr %= max;
long s1 = nr;
long s2 = nr / 2L;

char[] chars = new char[seed.length()];

for (int i = 0; i < seed.length(); i++) {
s1 = (s1 * 3L + s2) % max;
s2 = (s1 + s2 + 33L) % max;
double d = s1 / max;
byte b = (byte)(int)Math.floor(d * 31.0D + 64.0D);
chars[i] = (char)b;
} 

return new String(chars);
}

static long oldHash(String password) {
long nr = 1345345333L;
long nr2 = 7L;

for (int i = 0; i < password.length(); i++) {
if (password.charAt(i) != ' ' && password.charAt(i) != '\t') {

long tmp = password.charAt(i);
nr ^= ((nr & 0x3FL) + nr2) * tmp + (nr << 8L);
nr2 += tmp;
} 
} 
return nr & 0x7FFFFFFFL;
}

private static RandStructcture randomInit(long seed1, long seed2) {
enclosingInstance.getClass(); RandStructcture randStruct = new RandStructcture();

randStruct.maxValue = 1073741823L;
randStruct.maxValueDbl = randStruct.maxValue;
randStruct.seed1 = seed1 % randStruct.maxValue;
randStruct.seed2 = seed2 % randStruct.maxValue;

return randStruct;
}

public static Object readObject(ResultSet resultSet, int index) throws Exception {
ObjectInputStream objIn = new ObjectInputStream(resultSet.getBinaryStream(index));

Object obj = objIn.readObject();
objIn.close();

return obj;
}

private static double rnd(RandStructcture randStruct) {
randStruct.seed1 = (randStruct.seed1 * 3L + randStruct.seed2) % randStruct.maxValue;

randStruct.seed2 = (randStruct.seed1 + randStruct.seed2 + 33L) % randStruct.maxValue;

return randStruct.seed1 / randStruct.maxValueDbl;
}

public static String scramble(String message, String password) {
byte[] to = new byte[8];
String val = "";

message = message.substring(0, 8);

if (password != null && password.length() > 0) {
long[] hashPass = newHash(password);
long[] hashMessage = newHash(message);

RandStructcture randStruct = randomInit(hashPass[0] ^ hashMessage[0], hashPass[1] ^ hashMessage[1]);

int msgPos = 0;
int msgLength = message.length();
int toPos = 0;

while (msgPos++ < msgLength) {
to[toPos++] = (byte)(int)(Math.floor(rnd(randStruct) * 31.0D) + 64.0D);
}

byte extra = (byte)(int)Math.floor(rnd(randStruct) * 31.0D);

for (int i = 0; i < to.length; i++) {
to[i] = (byte)(to[i] ^ extra);
}

val = StringUtils.toString(to);
} 

return val;
}

public static String stackTraceToString(Throwable ex) {
StringBuffer traceBuf = new StringBuffer();
traceBuf.append(Messages.getString("Util.1"));

if (ex != null) {
traceBuf.append(ex.getClass().getName());

String message = ex.getMessage();

if (message != null) {
traceBuf.append(Messages.getString("Util.2"));
traceBuf.append(message);
} 

StringWriter out = new StringWriter();

PrintWriter printOut = new PrintWriter(out);

ex.printStackTrace(printOut);

traceBuf.append(Messages.getString("Util.3"));
traceBuf.append(out.toString());
} 

traceBuf.append(Messages.getString("Util.4"));

return traceBuf.toString();
}

public static Object getInstance(String className, Class<?>[] argTypes, Object[] args, ExceptionInterceptor exceptionInterceptor) throws SQLException {
try {
return handleNewInstance(Class.forName(className).getConstructor(argTypes), args, exceptionInterceptor);
}
catch (SecurityException e) {
throw SQLError.createSQLException("Can't instantiate required class", "S1000", e, exceptionInterceptor);

}
catch (NoSuchMethodException e) {
throw SQLError.createSQLException("Can't instantiate required class", "S1000", e, exceptionInterceptor);

}
catch (ClassNotFoundException e) {
throw SQLError.createSQLException("Can't instantiate required class", "S1000", e, exceptionInterceptor);
} 
}

public static final Object handleNewInstance(Constructor<?> ctor, Object[] args, ExceptionInterceptor exceptionInterceptor) throws SQLException {
try {
return ctor.newInstance(args);
} catch (IllegalArgumentException e) {
throw SQLError.createSQLException("Can't instantiate required class", "S1000", e, exceptionInterceptor);

}
catch (InstantiationException e) {
throw SQLError.createSQLException("Can't instantiate required class", "S1000", e, exceptionInterceptor);

}
catch (IllegalAccessException e) {
throw SQLError.createSQLException("Can't instantiate required class", "S1000", e, exceptionInterceptor);

}
catch (InvocationTargetException e) {
Throwable target = e.getTargetException();

if (target instanceof SQLException) {
throw (SQLException)target;
}

if (target instanceof ExceptionInInitializerError) {
target = ((ExceptionInInitializerError)target).getException();
}

throw SQLError.createSQLException(target.toString(), "S1000", exceptionInterceptor);
} 
}

public static boolean interfaceExists(String hostname) {
try {
Class<?> networkInterfaceClass = Class.forName("java.net.NetworkInterface"); return 

(networkInterfaceClass.getMethod("getByName", (Class[])null).invoke(networkInterfaceClass, new Object[] { hostname }) != null);
}
catch (Throwable t) {
return false;
} 
}

public static Object cast(Object invokeOn, Object toCast) {
if (CAST_METHOD != null) {
try {
return CAST_METHOD.invoke(invokeOn, new Object[] { toCast });
} catch (Throwable t) {
return null;
} 
}

return null;
}

public static long getCurrentTimeNanosOrMillis() {
if (systemNanoTimeMethod != null) {
try {
return ((Long)systemNanoTimeMethod.invoke(null, (Object[])null)).longValue();
}
catch (IllegalArgumentException e) {

} catch (IllegalAccessException e) {

} catch (InvocationTargetException e) {}
}

return System.currentTimeMillis();
}

public static void resultSetToMap(Map<Object, Object> mappedValues, ResultSet rs) throws SQLException {
while (rs.next()) {
mappedValues.put(rs.getObject(1), rs.getObject(2));
}
}

public static void resultSetToMap(Map<Object, Object> mappedValues, ResultSet rs, int key, int value) throws SQLException {
while (rs.next()) {
mappedValues.put(rs.getObject(key), rs.getObject(value));
}
}

public static void resultSetToMap(Map<Object, Object> mappedValues, ResultSet rs, String key, String value) throws SQLException {
while (rs.next()) {
mappedValues.put(rs.getObject(key), rs.getObject(value));
}
}

public static Map<Object, Object> calculateDifferences(Map<?, ?> map1, Map<?, ?> map2) {
Map<Object, Object> diffMap = new HashMap<Object, Object>();

for (Map.Entry<?, ?> entry : map1.entrySet()) {
Object key = entry.getKey();

Number value1 = null;
Number value2 = null;

if (entry.getValue() instanceof Number) {

value1 = (Number)entry.getValue();
value2 = (Number)map2.get(key);
} else {
try {
value1 = new Double(entry.getValue().toString());
value2 = new Double(map2.get(key).toString());
} catch (NumberFormatException nfe) {
continue;
} 
} 

if (value1.equals(value2)) {
continue;
}

if (value1 instanceof Byte) {
diffMap.put(key, Byte.valueOf((byte)(((Byte)value2).byteValue() - ((Byte)value1).byteValue())));
continue;
} 
if (value1 instanceof Short) {
diffMap.put(key, Short.valueOf((short)(((Short)value2).shortValue() - ((Short)value1).shortValue()))); continue;
} 
if (value1 instanceof Integer) {
diffMap.put(key, Integer.valueOf(((Integer)value2).intValue() - ((Integer)value1).intValue()));
continue;
} 
if (value1 instanceof Long) {
diffMap.put(key, Long.valueOf(((Long)value2).longValue() - ((Long)value1).longValue()));
continue;
} 
if (value1 instanceof Float) {
diffMap.put(key, Float.valueOf(((Float)value2).floatValue() - ((Float)value1).floatValue())); continue;
} 
if (value1 instanceof Double) {
diffMap.put(key, Double.valueOf((((Double)value2).shortValue() - ((Double)value1).shortValue())));
continue;
} 
if (value1 instanceof BigDecimal) {
diffMap.put(key, ((BigDecimal)value2).subtract((BigDecimal)value1)); continue;
} 
if (value1 instanceof BigInteger) {
diffMap.put(key, ((BigInteger)value2).subtract((BigInteger)value1));
}
} 

return diffMap;
}

public static List<Extension> loadExtensions(Connection conn, Properties props, String extensionClassNames, String errorMessageKey, ExceptionInterceptor exceptionInterceptor) throws SQLException {
List<Extension> extensionList = new LinkedList<Extension>();

List<String> interceptorsToCreate = StringUtils.split(extensionClassNames, ",", true);

Iterator<String> iter = interceptorsToCreate.iterator();

String className = null;

try {
while (iter.hasNext()) {
className = ((String)iter.next()).toString();
Extension extensionInstance = (Extension)Class.forName(className).newInstance();

extensionInstance.init(conn, props);

extensionList.add(extensionInstance);
} 
} catch (Throwable t) {
SQLException sqlEx = SQLError.createSQLException(Messages.getString(errorMessageKey, new Object[] { className }), exceptionInterceptor);

sqlEx.initCause(t);

throw sqlEx;
} 

return extensionList;
}
}

