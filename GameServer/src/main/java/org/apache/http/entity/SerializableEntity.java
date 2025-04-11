package org.apache.http.entity;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

import java.io.*;

@NotThreadSafe
public class SerializableEntity
        extends AbstractHttpEntity {
    private byte[] objSer;
    private Serializable objRef;

    public SerializableEntity(Serializable ser, boolean bufferize) throws IOException {
        Args.notNull(ser, "Source object");
        if (bufferize) {
            createBytes(ser);
        } else {
            this.objRef = ser;
        }
    }

    public SerializableEntity(Serializable ser) {
        Args.notNull(ser, "Source object");
        this.objRef = ser;
    }

    private void createBytes(Serializable ser) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(ser);
        out.flush();
        this.objSer = baos.toByteArray();
    }

    public InputStream getContent() throws IOException, IllegalStateException {
        if (this.objSer == null) {
            createBytes(this.objRef);
        }
        return new ByteArrayInputStream(this.objSer);
    }

    public long getContentLength() {
        if (this.objSer == null) {
            return -1L;
        }
        return this.objSer.length;
    }

    public boolean isRepeatable() {
        return true;
    }

    public boolean isStreaming() {
        return (this.objSer == null);
    }

    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        if (this.objSer == null) {
            ObjectOutputStream out = new ObjectOutputStream(outstream);
            out.writeObject(this.objRef);
            out.flush();
        } else {
            outstream.write(this.objSer);
            outstream.flush();
        }
    }
}

