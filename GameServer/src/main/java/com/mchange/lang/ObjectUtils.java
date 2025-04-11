package com.mchange.lang;

import java.io.*;

public final class ObjectUtils {
    public static final Object DUMMY_OBJECT = new Object();

    public static byte[] objectToByteArray(Object paramObject) throws NotSerializableException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(paramObject);
            return byteArrayOutputStream.toByteArray();
        } catch (NotSerializableException notSerializableException) {

            notSerializableException.fillInStackTrace();
            throw notSerializableException;
        } catch (IOException iOException) {

            iOException.printStackTrace();
            throw new Error("IOException writing to a byte array!");
        }
    }

    public static Object objectFromByteArray(byte[] paramArrayOfbyte) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(paramArrayOfbyte));
        return objectInputStream.readObject();
    }
}

