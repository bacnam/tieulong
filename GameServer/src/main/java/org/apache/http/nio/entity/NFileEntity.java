package org.apache.http.nio.entity;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.ContentEncoderChannel;
import org.apache.http.nio.FileContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.util.Args;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

@NotThreadSafe
public class NFileEntity
        extends AbstractHttpEntity
        implements HttpAsyncContentProducer, ProducingNHttpEntity {
    private final File file;
    private RandomAccessFile accessfile;
    private FileChannel fileChannel;
    private long idx = -1L;

    private boolean useFileChannels;

    public NFileEntity(File file, ContentType contentType, boolean useFileChannels) {
        Args.notNull(file, "File");
        this.file = file;
        this.useFileChannels = useFileChannels;
        if (contentType != null) {
            setContentType(contentType.toString());
        }
    }

    public NFileEntity(File file) {
        Args.notNull(file, "File");
        this.file = file;
    }

    public NFileEntity(File file, ContentType contentType) {
        this(file, contentType, true);
    }

    @Deprecated
    public NFileEntity(File file, String contentType, boolean useFileChannels) {
        Args.notNull(file, "File");
        this.file = file;
        this.useFileChannels = useFileChannels;
        setContentType(contentType);
    }

    @Deprecated
    public NFileEntity(File file, String contentType) {
        this(file, contentType, true);
    }

    public void close() throws IOException {
        if (this.accessfile != null) {
            this.accessfile.close();
        }
        this.accessfile = null;
        this.fileChannel = null;
    }

    @Deprecated
    public void finish() throws IOException {
        close();
    }

    public long getContentLength() {
        return this.file.length();
    }

    public boolean isRepeatable() {
        return true;
    }

    public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
        long transferred;
        if (this.accessfile == null) {
            this.accessfile = new RandomAccessFile(this.file, "r");
        }
        if (this.fileChannel == null) {
            this.fileChannel = this.accessfile.getChannel();
            this.idx = 0L;
        }

        if (this.useFileChannels && encoder instanceof FileContentEncoder) {
            transferred = ((FileContentEncoder) encoder).transfer(this.fileChannel, this.idx, Long.MAX_VALUE);
        } else {

            transferred = this.fileChannel.transferTo(this.idx, Long.MAX_VALUE, (WritableByteChannel) new ContentEncoderChannel(encoder));
        }

        if (transferred > 0L) {
            this.idx += transferred;
        }
        if (this.idx >= this.fileChannel.size()) {
            encoder.complete();
            close();
        }
    }

    public boolean isStreaming() {
        return false;
    }

    public InputStream getContent() throws IOException {
        return new FileInputStream(this.file);
    }

    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        InputStream instream = new FileInputStream(this.file);
        try {
            byte[] tmp = new byte[4096];
            int l;
            while ((l = instream.read(tmp)) != -1) {
                outstream.write(tmp, 0, l);
            }
            outstream.flush();
        } finally {
            instream.close();
        }
    }
}

