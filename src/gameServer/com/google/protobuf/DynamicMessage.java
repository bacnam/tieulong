

package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;

import java.io.InputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public final class DynamicMessage extends AbstractMessage {
  private final Descriptor type;
  private final FieldSet<FieldDescriptor> fields;
  private final UnknownFieldSet unknownFields;
  private int memoizedSize = -1;

  private DynamicMessage(Descriptor type, FieldSet<FieldDescriptor> fields,
                         UnknownFieldSet unknownFields) {
    this.type = type;
    this.fields = fields;
    this.unknownFields = unknownFields;
  }

  public static DynamicMessage getDefaultInstance(Descriptor type) {
    return new DynamicMessage(type, FieldSet.<FieldDescriptor>emptySet(),
                              UnknownFieldSet.getDefaultInstance());
  }

  public static DynamicMessage parseFrom(Descriptor type,
                                         CodedInputStream input)
                                         throws IOException {
    return newBuilder(type).mergeFrom(input).buildParsed();
  }

  public static DynamicMessage parseFrom(
      Descriptor type,
      CodedInputStream input,
      ExtensionRegistry extensionRegistry)
      throws IOException {
    return newBuilder(type).mergeFrom(input, extensionRegistry).buildParsed();
  }

  public static DynamicMessage parseFrom(Descriptor type, ByteString data)
                                         throws InvalidProtocolBufferException {
    return newBuilder(type).mergeFrom(data).buildParsed();
  }

  public static DynamicMessage parseFrom(Descriptor type, ByteString data,
                                         ExtensionRegistry extensionRegistry)
                                         throws InvalidProtocolBufferException {
    return newBuilder(type).mergeFrom(data, extensionRegistry).buildParsed();
  }

  public static DynamicMessage parseFrom(Descriptor type, byte[] data)
                                         throws InvalidProtocolBufferException {
    return newBuilder(type).mergeFrom(data).buildParsed();
  }

  public static DynamicMessage parseFrom(Descriptor type, byte[] data,
                                         ExtensionRegistry extensionRegistry)
                                         throws InvalidProtocolBufferException {
    return newBuilder(type).mergeFrom(data, extensionRegistry).buildParsed();
  }

  public static DynamicMessage parseFrom(Descriptor type, InputStream input)
                                         throws IOException {
    return newBuilder(type).mergeFrom(input).buildParsed();
  }

  public static DynamicMessage parseFrom(Descriptor type, InputStream input,
                                         ExtensionRegistry extensionRegistry)
                                         throws IOException {
    return newBuilder(type).mergeFrom(input, extensionRegistry).buildParsed();
  }

  public static Builder newBuilder(Descriptor type) {
    return new Builder(type);
  }

  public static Builder newBuilder(Message prototype) {
    return new Builder(prototype.getDescriptorForType()).mergeFrom(prototype);
  }

  public Descriptor getDescriptorForType() {
    return type;
  }

  public DynamicMessage getDefaultInstanceForType() {
    return getDefaultInstance(type);
  }

  public Map<FieldDescriptor, Object> getAllFields() {
    return fields.getAllFields();
  }

  public boolean hasField(FieldDescriptor field) {
    verifyContainingType(field);
    return fields.hasField(field);
  }

  public Object getField(FieldDescriptor field) {
    verifyContainingType(field);
    Object result = fields.getField(field);
    if (result == null) {
      if (field.isRepeated()) {
        result = Collections.emptyList();
      } else if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
        result = getDefaultInstance(field.getMessageType());
      } else {
        result = field.getDefaultValue();
      }
    }
    return result;
  }

  public int getRepeatedFieldCount(FieldDescriptor field) {
    verifyContainingType(field);
    return fields.getRepeatedFieldCount(field);
  }

  public Object getRepeatedField(FieldDescriptor field, int index) {
    verifyContainingType(field);
    return fields.getRepeatedField(field, index);
  }

  public UnknownFieldSet getUnknownFields() {
    return unknownFields;
  }

  private static boolean isInitialized(Descriptor type,
                                       FieldSet<FieldDescriptor> fields) {

    for (final FieldDescriptor field : type.getFields()) {
      if (field.isRequired()) {
        if (!fields.hasField(field)) {
          return false;
        }
      }
    }

    return fields.isInitialized();
  }

  @Override
  public boolean isInitialized() {
    return isInitialized(type, fields);
  }

  @Override
  public void writeTo(CodedOutputStream output) throws IOException {
    if (type.getOptions().getMessageSetWireFormat()) {
      fields.writeMessageSetTo(output);
      unknownFields.writeAsMessageSetTo(output);
    } else {
      fields.writeTo(output);
      unknownFields.writeTo(output);
    }
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    if (type.getOptions().getMessageSetWireFormat()) {
      size = fields.getMessageSetSerializedSize();
      size += unknownFields.getSerializedSizeAsMessageSet();
    } else {
      size = fields.getSerializedSize();
      size += unknownFields.getSerializedSize();
    }

    memoizedSize = size;
    return size;
  }

  public Builder newBuilderForType() {
    return new Builder(type);
  }

  public Builder toBuilder() {
    return newBuilderForType().mergeFrom(this);
  }

  public Parser<DynamicMessage> getParserForType() {
    return new AbstractParser<DynamicMessage>() {
      public DynamicMessage parsePartialFrom(
          CodedInputStream input,
          ExtensionRegistryLite extensionRegistry)
          throws InvalidProtocolBufferException {
        Builder builder = newBuilder(type);
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (IOException e) {
          throw new InvalidProtocolBufferException(e.getMessage())
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };
  }

  private void verifyContainingType(FieldDescriptor field) {
    if (field.getContainingType() != type) {
      throw new IllegalArgumentException(
        "FieldDescriptor does not match message type.");
    }
  }

  public static final class Builder extends AbstractMessage.Builder<Builder> {
    private final Descriptor type;
    private FieldSet<FieldDescriptor> fields;
    private UnknownFieldSet unknownFields;

    private Builder(Descriptor type) {
      this.type = type;
      this.fields = FieldSet.newFieldSet();
      this.unknownFields = UnknownFieldSet.getDefaultInstance();
    }

    @Override
    public Builder clear() {
      if (fields.isImmutable()) {
        fields = FieldSet.newFieldSet();
      } else {
        fields.clear();
      }
      unknownFields = UnknownFieldSet.getDefaultInstance();
      return this;
    }

    @Override
    public Builder mergeFrom(Message other) {
      if (other instanceof DynamicMessage) {

        DynamicMessage otherDynamicMessage = (DynamicMessage) other;
        if (otherDynamicMessage.type != type) {
          throw new IllegalArgumentException(
            "mergeFrom(Message) can only merge messages of the same type.");
        }
        ensureIsMutable();
        fields.mergeFrom(otherDynamicMessage.fields);
        mergeUnknownFields(otherDynamicMessage.unknownFields);
        return this;
      } else {
        return super.mergeFrom(other);
      }
    }

    public DynamicMessage build() {
      if (!isInitialized()) {
        throw newUninitializedMessageException(
          new DynamicMessage(type, fields, unknownFields));
      }
      return buildPartial();
    }

    private DynamicMessage buildParsed() throws InvalidProtocolBufferException {
      if (!isInitialized()) {
        throw newUninitializedMessageException(
            new DynamicMessage(type, fields, unknownFields))
          .asInvalidProtocolBufferException();
      }
      return buildPartial();
    }

    public DynamicMessage buildPartial() {
      fields.makeImmutable();
      DynamicMessage result =
        new DynamicMessage(type, fields, unknownFields);
      return result;
    }

    @Override
    public Builder clone() {
      Builder result = new Builder(type);
      result.fields.mergeFrom(fields);
      result.mergeUnknownFields(unknownFields);
      return result;
    }

    public boolean isInitialized() {
      return DynamicMessage.isInitialized(type, fields);
    }

    public Descriptor getDescriptorForType() {
      return type;
    }

    public DynamicMessage getDefaultInstanceForType() {
      return getDefaultInstance(type);
    }

    public Map<FieldDescriptor, Object> getAllFields() {
      return fields.getAllFields();
    }

    public Builder newBuilderForField(FieldDescriptor field) {
      verifyContainingType(field);

      if (field.getJavaType() != FieldDescriptor.JavaType.MESSAGE) {
        throw new IllegalArgumentException(
          "newBuilderForField is only valid for fields with message type.");
      }

      return new Builder(field.getMessageType());
    }

    public boolean hasField(FieldDescriptor field) {
      verifyContainingType(field);
      return fields.hasField(field);
    }

    public Object getField(FieldDescriptor field) {
      verifyContainingType(field);
      Object result = fields.getField(field);
      if (result == null) {
        if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
          result = getDefaultInstance(field.getMessageType());
        } else {
          result = field.getDefaultValue();
        }
      }
      return result;
    }

    public Builder setField(FieldDescriptor field, Object value) {
      verifyContainingType(field);
      ensureIsMutable();
      fields.setField(field, value);
      return this;
    }

    public Builder clearField(FieldDescriptor field) {
      verifyContainingType(field);
      ensureIsMutable();
      fields.clearField(field);
      return this;
    }

    public int getRepeatedFieldCount(FieldDescriptor field) {
      verifyContainingType(field);
      return fields.getRepeatedFieldCount(field);
    }

    public Object getRepeatedField(FieldDescriptor field, int index) {
      verifyContainingType(field);
      return fields.getRepeatedField(field, index);
    }

    public Builder setRepeatedField(FieldDescriptor field,
                                    int index, Object value) {
      verifyContainingType(field);
      ensureIsMutable();
      fields.setRepeatedField(field, index, value);
      return this;
    }

    public Builder addRepeatedField(FieldDescriptor field, Object value) {
      verifyContainingType(field);
      ensureIsMutable();
      fields.addRepeatedField(field, value);
      return this;
    }

    public UnknownFieldSet getUnknownFields() {
      return unknownFields;
    }

    public Builder setUnknownFields(UnknownFieldSet unknownFields) {
      this.unknownFields = unknownFields;
      return this;
    }

    @Override
    public Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
      this.unknownFields =
        UnknownFieldSet.newBuilder(this.unknownFields)
                       .mergeFrom(unknownFields)
                       .build();
      return this;
    }

    private void verifyContainingType(FieldDescriptor field) {
      if (field.getContainingType() != type) {
        throw new IllegalArgumentException(
          "FieldDescriptor does not match message type.");
      }
    }

    private void ensureIsMutable() {
      if (fields.isImmutable()) {
        fields = fields.clone();
      }
    }

    @Override
    public com.google.protobuf.Message.Builder getFieldBuilder(FieldDescriptor field) {

      throw new UnsupportedOperationException(
        "getFieldBuilder() called on a dynamic message type.");
    }
  }
}
