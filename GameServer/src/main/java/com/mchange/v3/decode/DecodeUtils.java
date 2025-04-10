package com.mchange.v3.decode;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class DecodeUtils
{
public static final String DECODER_CLASS_DOT_KEY = ".decoderClass";
public static final String DECODER_CLASS_NO_DOT_KEY = "decoderClass";
private static final Object[] DECODER_CLASS_DOT_KEY_OBJ_ARRAY = new Object[] { ".decoderClass" };
private static final Object[] DECODER_CLASS_NO_DOT_KEY_OBJ_ARRAY = new Object[] { "decoderClass" };

private static final MLogger logger = MLog.getLogger(DecodeUtils.class);

private static final List<DecoderFinder> finders;

private static final String[] finderClassNames = new String[] { "com.mchange.sc.v1.decode.ScalaMapDecoderFinder" };

static {
LinkedList<JavaMapDecoderFinder> linkedList = new LinkedList();
linkedList.add(new JavaMapDecoderFinder()); byte b; int i;
for (b = 0, i = finderClassNames.length; b < i; b++) {
try {
linkedList.add((DecoderFinder)Class.forName(finderClassNames[b]).newInstance());
} catch (Exception exception) {

if (logger.isLoggable(MLevel.INFO))
logger.log(MLevel.INFO, "Could not load DecoderFinder '" + finderClassNames[b] + "'", exception); 
} 
} 
finders = Collections.unmodifiableList((List)linkedList);
}

static class JavaMapDecoderFinder
implements DecoderFinder
{
public String decoderClassName(Object param1Object) throws CannotDecodeException {
if (param1Object instanceof Map) {

String str = null;
Map map = (Map)param1Object;
str = (String)map.get(".decoderClass");
if (str == null)
str = (String)map.get("decoderClass"); 
if (str == null) {
throw new CannotDecodeException("Could not find the decoder class for java.util.Map: " + param1Object);
}
return str;
} 

return null;
}
}

static final String findDecoderClassName(Object paramObject) throws CannotDecodeException {
for (DecoderFinder decoderFinder : finders) {

String str = decoderFinder.decoderClassName(paramObject);
if (str != null) return str; 
} 
throw new CannotDecodeException("Could not find a decoder class name for object: " + paramObject);
}

public static Object decode(String paramString, Object paramObject) throws CannotDecodeException {
try {
Class<?> clazz = Class.forName(paramString);
Decoder decoder = (Decoder)clazz.newInstance();
return decoder.decode(paramObject);
}
catch (Exception exception) {
throw new CannotDecodeException("An exception occurred while attempting to decode " + paramObject, exception);
} 
}
public static Object decode(Object paramObject) throws CannotDecodeException {
return decode(findDecoderClassName(paramObject), paramObject);
}
}

