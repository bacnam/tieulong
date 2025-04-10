

package com.google.protobuf;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public abstract class AbstractMessageLite implements MessageLite {
  public ByteString toByteString() {
    try {
      final ByteString.CodedBuilder out =
        ByteString.newCodedBuilder(getSerializedSize());
      writeTo(out.getCodedOutput());
      return out.build();
    } catch (IOException e) {
      throw new RuntimeException(
        "Serializing to a ByteString threw an IOException (should " +
        "never happen).", e);
    }
  }

  public byte[] toByteArray() {
    try {
      final byte[] result = new byte[getSerializedSize()];
      final CodedOutputStream output = CodedOutputStream.newInstance(result);
      writeTo(output);
      output.checkNoSpaceLeft();
      return result;
    } catch (IOException e) {
      throw new RuntimeException(
        "Serializing to a byte array threw an IOException " +
        "(should never happen).", e);
    }
  }

  public void writeTo(final OutputStream output) throws IOException {
    final int bufferSize =
        CodedOutputStream.computePreferredBufferSize(getSerializedSize());
    final CodedOutputStream codedOutput =
        CodedOutputStream.newInstance(output, bufferSize);
    writeTo(codedOutput);
    codedOutput.flush();
  }

  public void writeDelimitedTo(final OutputStream output) throws IOException {
    final int serialized = getSerializedSize();
    final int bufferSize = CodedOutputStream.computePreferredBufferSize(
        CodedOutputStream.computeRawVarint32Size(serialized) + serialized);
    final CodedOutputStream codedOutput =
        CodedOutputStream.newInstance(output, bufferSize);
    codedOutput.writeRawVarint32(serialized);
    writeTo(codedOutput);
    codedOutput.flush();
  }

  UninitializedMessageException newUninitializedMessageException() {
    return new UninitializedMessageException(this);
  }

  @SuppressWarnings("unchecked")
  public static abstract class Builder<BuilderType extends Builder>
      implements MessageLite.Builder {

    @Override
    public abstract BuilderType clone();

    public BuilderType mergeFrom(final CodedInputStream input)
                                 throws IOException {
      return mergeFrom(input, ExtensionRegistryLite.getEmptyRegistry());
    }

    public abstract BuilderType mergeFrom(
        final CodedInputStream input,
        final ExtensionRegistryLite extensionRegistry)
        throws IOException;

    public BuilderType mergeFrom(final ByteString data)
        throws InvalidProtocolBufferException {
      try {
        final CodedInputStream input = data.newCodedInput();
        mergeFrom(input);
        input.checkLastTagWas(0);
        return (BuilderType) this;
      } catch (InvalidProtocolBufferException e) {
        throw e;
      } catch (IOException e) {
        throw new RuntimeException(
          "Reading from a ByteString threw an IOException (should " +
          "never happen).", e);
      }
    }

    public BuilderType mergeFrom(
        final ByteString data,
        final ExtensionRegistryLite extensionRegistry)
        throws InvalidProtocolBufferException {
      try {
        final CodedInputStream input = data.newCodedInput();
        mergeFrom(input, extensionRegistry);
        input.checkLastTagWas(0);
        return (BuilderType) this;
      } catch (InvalidProtocolBufferException e) {
        throw e;
      } catch (IOException e) {
        throw new RuntimeException(
          "Reading from a ByteString threw an IOException (should " +
          "never happen).", e);
      }
    }

    public BuilderType mergeFrom(final byte[] data)
        throws InvalidProtocolBufferException {
      return mergeFrom(data, 0, data.length);
    }

    public BuilderType mergeFrom(final byte[] data, final int off,
                                 final int len)
                                 throws InvalidProtocolBufferException {
      try {
        final CodedInputStream input =
            CodedInputStream.newInstance(data, off, len);
        mergeFrom(input);
        input.checkLastTagWas(0);
        return (BuilderType) this;
      } catch (InvalidProtocolBufferException e) {
        throw e;
      } catch (IOException e) {
        throw new RuntimeException(
          "Reading from a byte array threw an IOException (should " +
          "never happen).", e);
      }
    }

    public BuilderType mergeFrom(
        final byte[] data,
        final ExtensionRegistryLite extensionRegistry)
        throws InvalidProtocolBufferException {
      return mergeFrom(data, 0, data.length, extensionRegistry);
    }

    public BuilderType mergeFrom(
        final byte[] data, final int off, final int len,
        final ExtensionRegistryLite extensionRegistry)
        throws InvalidProtocolBufferException {
      try {
        final CodedInputStream input =
            CodedInputStream.newInstance(data, off, len);
        mergeFrom(input, extensionRegistry);
        input.checkLastTagWas(0);
        return (BuilderType) this;
      } catch (InvalidProtocolBufferException e) {
        throw e;
      } catch (IOException e) {
        throw new RuntimeException(
          "Reading from a byte array threw an IOException (should " +
          "never happen).", e);
      }
    }

    public BuilderType mergeFrom(final InputStream input) throws IOException {
      final CodedInputStream codedInput = CodedInputStream.newInstance(input);
      mergeFrom(codedInput);
      codedInput.checkLastTagWas(0);
      return (BuilderType) this;
    }

    public BuilderType mergeFrom(
        final InputStream input,
        final ExtensionRegistryLite extensionRegistry)
        throws IOException {
      final CodedInputStream codedInput = CodedInputStream.newInstance(input);
      mergeFrom(codedInput, extensionRegistry);
      codedInput.checkLastTagWas(0);
      return (BuilderType) this;
    }

    static final class LimitedInputStream extends FilterInputStream {
      private int limit;

      LimitedInputStream(InputStream in, int limit) {
        super(in);
        this.limit = limit;
      }

      @Override
      public int available() throws IOException {
        return Math.min(super.available(), limit);
      }

      @Override
      public int read() throws IOException {
        if (limit <= 0) {
          return -1;
        }
        final int result = super.read();
        if (result >= 0) {
          --limit;
        }
        return result;
      }

      @Override
      public int read(final byte[] b, final int off, int len)
                      throws IOException {
        if (limit <= 0) {
          return -1;
        }
        len = Math.min(len, limit);
        final int result = super.read(b, off, len);
        if (result >= 0) {
          limit -= result;
        }
        return result;
      }

      @Override
      public long skip(final long n) throws IOException {
        final long result = super.skip(Math.min(n, limit));
        if (result >= 0) {
          limit -= result;
        }
        return result;
      }
    }

    public boolean mergeDelimitedFrom(
        final InputStream input,
        final ExtensionRegistryLite extensionRegistry)
        throws IOException {
      final int firstByte = input.read();
      if (firstByte == -1) {
        return false;
      }
      final int size = CodedInputStream.readRawVarint32(firstByte, input);
      final InputStream limitedInput = new LimitedInputStream(input, size);
      mergeFrom(limitedInput, extensionRegistry);
      return true;
    }

    public boolean mergeDelimitedFrom(final InputStream input)
        throws IOException {
      return mergeDelimitedFrom(input,
          ExtensionRegistryLite.getEmptyRegistry());
    }

    protected static UninitializedMessageException
        newUninitializedMessageException(MessageLite message) {
      return new UninitializedMessageException(message);
    }

    protected static <T> void addAll(final Iterable<T> values,
                                     final Collection<? super T> list) {
      if (values instanceof LazyStringList) {

        checkForNullValues(((LazyStringList) values).getUnderlyingElements());
      } else {
        checkForNullValues(values);
      }
      if (values instanceof Collection) {
        final Collection<T> collection = (Collection<T>) values;
        list.addAll(collection);
      } else {
        for (final T value : values) {
          list.add(value);
        }
      }
    }

    private static void checkForNullValues(final Iterable<?> values) {
      for (final Object value : values) {
        if (value == null) {
          throw new NullPointerException();
        }
      }
    }
  }
}
