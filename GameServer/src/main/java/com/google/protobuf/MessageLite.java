

package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface MessageLite extends MessageLiteOrBuilder {

  void writeTo(CodedOutputStream output) throws IOException;

  int getSerializedSize();

  Parser<? extends MessageLite> getParserForType();

  ByteString toByteString();

  byte[] toByteArray();

  void writeTo(OutputStream output) throws IOException;

  void writeDelimitedTo(OutputStream output) throws IOException;

  Builder newBuilderForType();

  Builder toBuilder();

  interface Builder extends MessageLiteOrBuilder, Cloneable {

    Builder clear();

    MessageLite build();

    MessageLite buildPartial();

    Builder clone();

    Builder mergeFrom(CodedInputStream input) throws IOException;

    Builder mergeFrom(CodedInputStream input,
                      ExtensionRegistryLite extensionRegistry)
                      throws IOException;

    Builder mergeFrom(ByteString data) throws InvalidProtocolBufferException;

    Builder mergeFrom(ByteString data,
                      ExtensionRegistryLite extensionRegistry)
                      throws InvalidProtocolBufferException;

    Builder mergeFrom(byte[] data) throws InvalidProtocolBufferException;

    Builder mergeFrom(byte[] data, int off, int len)
                      throws InvalidProtocolBufferException;

    Builder mergeFrom(byte[] data,
                      ExtensionRegistryLite extensionRegistry)
                      throws InvalidProtocolBufferException;

    Builder mergeFrom(byte[] data, int off, int len,
                      ExtensionRegistryLite extensionRegistry)
                      throws InvalidProtocolBufferException;

    Builder mergeFrom(InputStream input) throws IOException;

    Builder mergeFrom(InputStream input,
                      ExtensionRegistryLite extensionRegistry)
                      throws IOException;

    boolean mergeDelimitedFrom(InputStream input)
                               throws IOException;

    boolean mergeDelimitedFrom(InputStream input,
                               ExtensionRegistryLite extensionRegistry)
                               throws IOException;
  }
}
