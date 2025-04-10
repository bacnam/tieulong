package org.apache.http.nio.client.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentDecoderChannel;
import org.apache.http.nio.FileContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

public abstract class ZeroCopyConsumer<T>
extends AbstractAsyncResponseConsumer<T>
{
private final File file;
private final RandomAccessFile accessfile;
private HttpResponse response;
private ContentType contentType;
private FileChannel fileChannel;
private long idx = -1L;

public ZeroCopyConsumer(File file) throws FileNotFoundException {
if (file == null) {
throw new IllegalArgumentException("File may nor be null");
}
this.file = file;
this.accessfile = new RandomAccessFile(this.file, "rw");
}

protected void onResponseReceived(HttpResponse response) {
this.response = response;
}

protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
this.contentType = contentType;
this.fileChannel = this.accessfile.getChannel();
this.idx = 0L;
}

protected void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
long transferred;
Asserts.notNull(this.fileChannel, "File channel");

if (decoder instanceof FileContentDecoder) {
transferred = ((FileContentDecoder)decoder).transfer(this.fileChannel, this.idx, 2147483647L);
} else {

transferred = this.fileChannel.transferFrom((ReadableByteChannel)new ContentDecoderChannel(decoder), this.idx, 2147483647L);
} 

if (transferred > 0L) {
this.idx += transferred;
}
if (decoder.isCompleted()) {
this.fileChannel.close();
}
}

protected abstract T process(HttpResponse paramHttpResponse, File paramFile, ContentType paramContentType) throws Exception;

protected T buildResult(HttpContext context) throws Exception {
this.response.setEntity((HttpEntity)new FileEntity(this.file, this.contentType));
return process(this.response, this.file, this.contentType);
}

protected void releaseResources() {
try {
this.accessfile.close();
} catch (IOException ignore) {}
}
}

