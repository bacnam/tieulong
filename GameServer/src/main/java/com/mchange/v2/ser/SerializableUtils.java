package com.mchange.v2.ser;

import com.mchange.v1.io.InputStreamUtils;
import com.mchange.v1.io.OutputStreamUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class SerializableUtils
{
static final MLogger logger = MLog.getLogger(SerializableUtils.class);

public static byte[] toByteArray(Object paramObject) throws NotSerializableException {
return serializeToByteArray(paramObject);
}

public static byte[] toByteArray(Object paramObject, Indirector paramIndirector, IndirectPolicy paramIndirectPolicy) throws NotSerializableException {
try {
if (paramIndirectPolicy == IndirectPolicy.DEFINITELY_INDIRECT) {

if (paramIndirector == null) {
throw new IllegalArgumentException("null indirector is not consistent with " + paramIndirectPolicy);
}
IndirectlySerialized indirectlySerialized = paramIndirector.indirectForm(paramObject);
return toByteArray(indirectlySerialized);
} 
if (paramIndirectPolicy == IndirectPolicy.INDIRECT_ON_EXCEPTION) {

if (paramIndirector == null)
throw new IllegalArgumentException("null indirector is not consistent with " + paramIndirectPolicy); 
try {
return toByteArray(paramObject);
} catch (NotSerializableException notSerializableException) {
return toByteArray(paramObject, paramIndirector, IndirectPolicy.DEFINITELY_INDIRECT);
} 
}  if (paramIndirectPolicy == IndirectPolicy.DEFINITELY_DIRECT) {
return toByteArray(paramObject);
}
throw new InternalError("unknown indirecting policy: " + paramIndirectPolicy);
}
catch (NotSerializableException notSerializableException) {
throw notSerializableException;
} catch (Exception exception) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "An Exception occurred while serializing an Object to a byte[] with an Indirector.", exception); 
throw new NotSerializableException(exception.toString());
} 
}

public static byte[] serializeToByteArray(Object paramObject) throws NotSerializableException {
try {
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
objectOutputStream.writeObject(paramObject);
return byteArrayOutputStream.toByteArray();
}
catch (NotSerializableException notSerializableException) {

notSerializableException.fillInStackTrace();
throw notSerializableException;
}
catch (IOException iOException) {

if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, "An IOException occurred while writing into a ByteArrayOutputStream?!?", iOException); 
throw new Error("IOException writing to a byte array!");
} 
}

public static Object fromByteArray(byte[] paramArrayOfbyte) throws IOException, ClassNotFoundException {
Object object = deserializeFromByteArray(paramArrayOfbyte);
if (object instanceof IndirectlySerialized) {
return ((IndirectlySerialized)object).getObject();
}
return object;
}

public static Object fromByteArray(byte[] paramArrayOfbyte, boolean paramBoolean) throws IOException, ClassNotFoundException {
if (paramBoolean) {
return deserializeFromByteArray(paramArrayOfbyte);
}
return fromByteArray(paramArrayOfbyte);
}

public static Object deserializeFromByteArray(byte[] paramArrayOfbyte) throws IOException, ClassNotFoundException {
ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(paramArrayOfbyte));
return objectInputStream.readObject();
}

public static Object testSerializeDeserialize(Object paramObject) throws IOException, ClassNotFoundException {
return deepCopy(paramObject);
}

public static Object deepCopy(Object paramObject) throws IOException, ClassNotFoundException {
byte[] arrayOfByte = serializeToByteArray(paramObject);
return deserializeFromByteArray(arrayOfByte);
}

public static final Object unmarshallObjectFromFile(File paramFile) throws IOException, ClassNotFoundException {
ObjectInputStream objectInputStream = null;

try {
objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(paramFile)));
return objectInputStream.readObject();
} finally {

InputStreamUtils.attemptClose(objectInputStream);
} 
}

public static final void marshallObjectToFile(Object paramObject, File paramFile) throws IOException {
ObjectOutputStream objectOutputStream = null;

try {
objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(paramFile)));
objectOutputStream.writeObject(paramObject);
} finally {

OutputStreamUtils.attemptClose(objectOutputStream);
} 
}
}

