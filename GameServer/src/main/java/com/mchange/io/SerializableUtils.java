package com.mchange.io;

import java.io.*;

public final class SerializableUtils {
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

