package com.mchange.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class SerializableUtils
{
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

