

package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface Message extends MessageLite, MessageOrBuilder {

  Parser<? extends Message> getParserForType();

  @Override
  boolean equals(Object other);

  @Override
  int hashCode();

  @Override
  String toString();

  Builder newBuilderForType();
  Builder toBuilder();

  interface Builder extends MessageLite.Builder, MessageOrBuilder {

    Builder clear();

    Builder mergeFrom(Message other);

    Message build();
    Message buildPartial();
    Builder clone();
    Builder mergeFrom(CodedInputStream input) throws IOException;
    Builder mergeFrom(CodedInputStream input,
                      ExtensionRegistryLite extensionRegistry)
                      throws IOException;

    Descriptors.Descriptor getDescriptorForType();

    Builder newBuilderForField(Descriptors.FieldDescriptor field);

    Builder getFieldBuilder(Descriptors.FieldDescriptor field);

    Builder setField(Descriptors.FieldDescriptor field, Object value);

    Builder clearField(Descriptors.FieldDescriptor field);

    Builder setRepeatedField(Descriptors.FieldDescriptor field,
                             int index, Object value);

    Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value);

    Builder setUnknownFields(UnknownFieldSet unknownFields);

    Builder mergeUnknownFields(UnknownFieldSet unknownFields);

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
