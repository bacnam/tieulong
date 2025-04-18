package org.apache.http.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharacterCodingException;
import org.apache.http.util.CharArrayBuffer;

public interface SessionOutputBuffer {
  boolean hasData();

  int length();

  int flush(WritableByteChannel paramWritableByteChannel) throws IOException;

  void write(ByteBuffer paramByteBuffer);

  void write(ReadableByteChannel paramReadableByteChannel) throws IOException;

  void writeLine(CharArrayBuffer paramCharArrayBuffer) throws CharacterCodingException;

  void writeLine(String paramString) throws IOException;
}

