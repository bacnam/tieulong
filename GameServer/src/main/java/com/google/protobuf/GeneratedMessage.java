

package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class GeneratedMessage extends AbstractMessage
    implements Serializable {
  private static final long serialVersionUID = 1L;

  protected static boolean alwaysUseFieldBuilders = false;

  protected GeneratedMessage() {
  }

  protected GeneratedMessage(Builder<?> builder) {
  }

  public Parser<? extends Message> getParserForType() {
    throw new UnsupportedOperationException(
        "This is supposed to be overridden by subclasses.");
  }

  static void enableAlwaysUseFieldBuildersForTesting() {
    alwaysUseFieldBuilders = true;
  }

  protected abstract FieldAccessorTable internalGetFieldAccessorTable();

  public Descriptor getDescriptorForType() {
    return internalGetFieldAccessorTable().descriptor;
  }

  private Map<FieldDescriptor, Object> getAllFieldsMutable() {
    final TreeMap<FieldDescriptor, Object> result =
      new TreeMap<FieldDescriptor, Object>();
    final Descriptor descriptor = internalGetFieldAccessorTable().descriptor;
    for (final FieldDescriptor field : descriptor.getFields()) {
      if (field.isRepeated()) {
        final List<?> value = (List<?>) getField(field);
        if (!value.isEmpty()) {
          result.put(field, value);
        }
      } else {
        if (hasField(field)) {
          result.put(field, getField(field));
        }
      }
    }
    return result;
  }

  @Override
  public boolean isInitialized() {
    for (final FieldDescriptor field : getDescriptorForType().getFields()) {

      if (field.isRequired()) {
        if (!hasField(field)) {
          return false;
        }
      }

      if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
        if (field.isRepeated()) {
          @SuppressWarnings("unchecked") final
          List<Message> messageList = (List<Message>) getField(field);
          for (final Message element : messageList) {
            if (!element.isInitialized()) {
              return false;
            }
          }
        } else {
          if (hasField(field) && !((Message) getField(field)).isInitialized()) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public Map<FieldDescriptor, Object> getAllFields() {
    return Collections.unmodifiableMap(getAllFieldsMutable());
  }

  public boolean hasField(final FieldDescriptor field) {
    return internalGetFieldAccessorTable().getField(field).has(this);
  }

  public Object getField(final FieldDescriptor field) {
    return internalGetFieldAccessorTable().getField(field).get(this);
  }

  public int getRepeatedFieldCount(final FieldDescriptor field) {
    return internalGetFieldAccessorTable().getField(field)
      .getRepeatedCount(this);
  }

  public Object getRepeatedField(final FieldDescriptor field, final int index) {
    return internalGetFieldAccessorTable().getField(field)
      .getRepeated(this, index);
  }

  public UnknownFieldSet getUnknownFields() {
    throw new UnsupportedOperationException(
        "This is supposed to be overridden by subclasses.");
  }

  protected boolean parseUnknownField(
      CodedInputStream input,
      UnknownFieldSet.Builder unknownFields,
      ExtensionRegistryLite extensionRegistry,
      int tag) throws IOException {
    return unknownFields.mergeFieldFrom(tag, input);
  }

  protected void makeExtensionsImmutable() {

  }

  protected abstract Message.Builder newBuilderForType(BuilderParent parent);

  protected interface BuilderParent {

    void markDirty();
  }

  @SuppressWarnings("unchecked")
  public abstract static class Builder <BuilderType extends Builder>
      extends AbstractMessage.Builder<BuilderType> {

    private BuilderParent builderParent;

    private BuilderParentImpl meAsParent;

    private boolean isClean;

    private UnknownFieldSet unknownFields =
        UnknownFieldSet.getDefaultInstance();

    protected Builder() {
      this(null);
    }

    protected Builder(BuilderParent builderParent) {
      this.builderParent = builderParent;
    }

    void dispose() {
      builderParent = null;
    }

    protected void onBuilt() {
      if (builderParent != null) {
        markClean();
      }
    }

    protected void markClean() {
      this.isClean = true;
    }

    protected boolean isClean() {
      return isClean;
    }

    @Override
    public BuilderType clone() {
      throw new UnsupportedOperationException(
          "This is supposed to be overridden by subclasses.");
    }

    public BuilderType clear() {
      unknownFields = UnknownFieldSet.getDefaultInstance();
      onChanged();
      return (BuilderType) this;
    }

    protected abstract FieldAccessorTable internalGetFieldAccessorTable();

    public Descriptor getDescriptorForType() {
      return internalGetFieldAccessorTable().descriptor;
    }

    public Map<FieldDescriptor, Object> getAllFields() {
      return Collections.unmodifiableMap(getAllFieldsMutable());
    }

    private Map<FieldDescriptor, Object> getAllFieldsMutable() {
      final TreeMap<FieldDescriptor, Object> result =
        new TreeMap<FieldDescriptor, Object>();
      final Descriptor descriptor = internalGetFieldAccessorTable().descriptor;
      for (final FieldDescriptor field : descriptor.getFields()) {
        if (field.isRepeated()) {
          final List value = (List) getField(field);
          if (!value.isEmpty()) {
            result.put(field, value);
          }
        } else {
          if (hasField(field)) {
            result.put(field, getField(field));
          }
        }
      }
      return result;
    }

    public Message.Builder newBuilderForField(
        final FieldDescriptor field) {
      return internalGetFieldAccessorTable().getField(field).newBuilder();
    }

    public Message.Builder getFieldBuilder(final FieldDescriptor field) {
      return internalGetFieldAccessorTable().getField(field).getBuilder(this);
    }

    public boolean hasField(final FieldDescriptor field) {
      return internalGetFieldAccessorTable().getField(field).has(this);
    }

    public Object getField(final FieldDescriptor field) {
      Object object = internalGetFieldAccessorTable().getField(field).get(this);
      if (field.isRepeated()) {

        return Collections.unmodifiableList((List) object);
      } else {
        return object;
      }
    }

    public BuilderType setField(final FieldDescriptor field,
                                final Object value) {
      internalGetFieldAccessorTable().getField(field).set(this, value);
      return (BuilderType) this;
    }

    public BuilderType clearField(final FieldDescriptor field) {
      internalGetFieldAccessorTable().getField(field).clear(this);
      return (BuilderType) this;
    }

    public int getRepeatedFieldCount(final FieldDescriptor field) {
      return internalGetFieldAccessorTable().getField(field)
          .getRepeatedCount(this);
    }

    public Object getRepeatedField(final FieldDescriptor field,
                                   final int index) {
      return internalGetFieldAccessorTable().getField(field)
          .getRepeated(this, index);
    }

    public BuilderType setRepeatedField(final FieldDescriptor field,
                                        final int index, final Object value) {
      internalGetFieldAccessorTable().getField(field)
        .setRepeated(this, index, value);
      return (BuilderType) this;
    }

    public BuilderType addRepeatedField(final FieldDescriptor field,
                                        final Object value) {
      internalGetFieldAccessorTable().getField(field).addRepeated(this, value);
      return (BuilderType) this;
    }

    public final BuilderType setUnknownFields(
        final UnknownFieldSet unknownFields) {
      this.unknownFields = unknownFields;
      onChanged();
      return (BuilderType) this;
    }

    @Override
    public final BuilderType mergeUnknownFields(
        final UnknownFieldSet unknownFields) {
      this.unknownFields =
        UnknownFieldSet.newBuilder(this.unknownFields)
                       .mergeFrom(unknownFields)
                       .build();
      onChanged();
      return (BuilderType) this;
    }

    public boolean isInitialized() {
      for (final FieldDescriptor field : getDescriptorForType().getFields()) {

        if (field.isRequired()) {
          if (!hasField(field)) {
            return false;
          }
        }

        if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
          if (field.isRepeated()) {
            @SuppressWarnings("unchecked") final
            List<Message> messageList = (List<Message>) getField(field);
            for (final Message element : messageList) {
              if (!element.isInitialized()) {
                return false;
              }
            }
          } else {
            if (hasField(field) &&
                !((Message) getField(field)).isInitialized()) {
              return false;
            }
          }
        }
      }
      return true;
    }

    public final UnknownFieldSet getUnknownFields() {
      return unknownFields;
    }

    protected boolean parseUnknownField(
        final CodedInputStream input,
        final UnknownFieldSet.Builder unknownFields,
        final ExtensionRegistryLite extensionRegistry,
        final int tag) throws IOException {
      return unknownFields.mergeFieldFrom(tag, input);
    }

    private class BuilderParentImpl implements BuilderParent {

      public void markDirty() {
        onChanged();
      }
    }

    protected BuilderParent getParentForChildren() {
      if (meAsParent == null) {
        meAsParent = new BuilderParentImpl();
      }
      return meAsParent;
    }

    protected final void onChanged() {
      if (isClean && builderParent != null) {
        builderParent.markDirty();

        isClean = false;
      }
    }
  }

  public interface ExtendableMessageOrBuilder<
      MessageType extends ExtendableMessage> extends MessageOrBuilder {

    <Type> boolean hasExtension(
        GeneratedExtension<MessageType, Type> extension);

    <Type> int getExtensionCount(
        GeneratedExtension<MessageType, List<Type>> extension);

    <Type> Type getExtension(GeneratedExtension<MessageType, Type> extension);

    <Type> Type getExtension(
        GeneratedExtension<MessageType, List<Type>> extension,
        int index);
  }

  public abstract static class ExtendableMessage<
        MessageType extends ExtendableMessage>
      extends GeneratedMessage
      implements ExtendableMessageOrBuilder<MessageType> {

    private final FieldSet<FieldDescriptor> extensions;

    protected ExtendableMessage() {
      this.extensions = FieldSet.newFieldSet();
    }

    protected ExtendableMessage(
        ExtendableBuilder<MessageType, ?> builder) {
      super(builder);
      this.extensions = builder.buildExtensions();
    }

    private void verifyExtensionContainingType(
        final GeneratedExtension<MessageType, ?> extension) {
      if (extension.getDescriptor().getContainingType() !=
          getDescriptorForType()) {

        throw new IllegalArgumentException(
          "Extension is for type \"" +
          extension.getDescriptor().getContainingType().getFullName() +
          "\" which does not match message type \"" +
          getDescriptorForType().getFullName() + "\".");
      }
    }

    public final <Type> boolean hasExtension(
        final GeneratedExtension<MessageType, Type> extension) {
      verifyExtensionContainingType(extension);
      return extensions.hasField(extension.getDescriptor());
    }

    public final <Type> int getExtensionCount(
        final GeneratedExtension<MessageType, List<Type>> extension) {
      verifyExtensionContainingType(extension);
      final FieldDescriptor descriptor = extension.getDescriptor();
      return extensions.getRepeatedFieldCount(descriptor);
    }

    @SuppressWarnings("unchecked")
    public final <Type> Type getExtension(
        final GeneratedExtension<MessageType, Type> extension) {
      verifyExtensionContainingType(extension);
      FieldDescriptor descriptor = extension.getDescriptor();
      final Object value = extensions.getField(descriptor);
      if (value == null) {
        if (descriptor.isRepeated()) {
          return (Type) Collections.emptyList();
        } else if (descriptor.getJavaType() ==
                   FieldDescriptor.JavaType.MESSAGE) {
          return (Type) extension.getMessageDefaultInstance();
        } else {
          return (Type) extension.fromReflectionType(
              descriptor.getDefaultValue());
        }
      } else {
        return (Type) extension.fromReflectionType(value);
      }
    }

    @SuppressWarnings("unchecked")
    public final <Type> Type getExtension(
        final GeneratedExtension<MessageType, List<Type>> extension,
        final int index) {
      verifyExtensionContainingType(extension);
      FieldDescriptor descriptor = extension.getDescriptor();
      return (Type) extension.singularFromReflectionType(
          extensions.getRepeatedField(descriptor, index));
    }

    protected boolean extensionsAreInitialized() {
      return extensions.isInitialized();
    }

    @Override
    public boolean isInitialized() {
      return super.isInitialized() && extensionsAreInitialized();
    }

    @Override
    protected boolean parseUnknownField(
        CodedInputStream input,
        UnknownFieldSet.Builder unknownFields,
        ExtensionRegistryLite extensionRegistry,
        int tag) throws IOException {
      return AbstractMessage.Builder.mergeFieldFrom(
        input, unknownFields, extensionRegistry, getDescriptorForType(),
        null, extensions, tag);
    }

    @Override
    protected void makeExtensionsImmutable() {
      extensions.makeImmutable();
    }

    protected class ExtensionWriter {

      private final Iterator<Map.Entry<FieldDescriptor, Object>> iter =
        extensions.iterator();
      private Map.Entry<FieldDescriptor, Object> next;
      private final boolean messageSetWireFormat;

      private ExtensionWriter(final boolean messageSetWireFormat) {
        if (iter.hasNext()) {
          next = iter.next();
        }
        this.messageSetWireFormat = messageSetWireFormat;
      }

      public void writeUntil(final int end, final CodedOutputStream output)
                             throws IOException {
        while (next != null && next.getKey().getNumber() < end) {
          FieldDescriptor descriptor = next.getKey();
          if (messageSetWireFormat && descriptor.getLiteJavaType() ==
                  WireFormat.JavaType.MESSAGE &&
              !descriptor.isRepeated()) {
            if (next instanceof LazyField.LazyEntry<?>) {
              output.writeRawMessageSetExtension(descriptor.getNumber(),
                  ((LazyField.LazyEntry<?>) next).getField().toByteString());
            } else {
              output.writeMessageSetExtension(descriptor.getNumber(),
                                              (Message) next.getValue());
            }
          } else {

            FieldSet.writeField(descriptor, next.getValue(), output);
          }
          if (iter.hasNext()) {
            next = iter.next();
          } else {
            next = null;
          }
        }
      }
    }

    protected ExtensionWriter newExtensionWriter() {
      return new ExtensionWriter(false);
    }
    protected ExtensionWriter newMessageSetExtensionWriter() {
      return new ExtensionWriter(true);
    }

    protected int extensionsSerializedSize() {
      return extensions.getSerializedSize();
    }
    protected int extensionsSerializedSizeAsMessageSet() {
      return extensions.getMessageSetSerializedSize();
    }

    protected Map<FieldDescriptor, Object> getExtensionFields() {
      return extensions.getAllFields();
    }

    @Override
    public Map<FieldDescriptor, Object> getAllFields() {
      final Map<FieldDescriptor, Object> result = super.getAllFieldsMutable();
      result.putAll(getExtensionFields());
      return Collections.unmodifiableMap(result);
    }

    @Override
    public boolean hasField(final FieldDescriptor field) {
      if (field.isExtension()) {
        verifyContainingType(field);
        return extensions.hasField(field);
      } else {
        return super.hasField(field);
      }
    }

    @Override
    public Object getField(final FieldDescriptor field) {
      if (field.isExtension()) {
        verifyContainingType(field);
        final Object value = extensions.getField(field);
        if (value == null) {
          if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {

            return DynamicMessage.getDefaultInstance(field.getMessageType());
          } else {
            return field.getDefaultValue();
          }
        } else {
          return value;
        }
      } else {
        return super.getField(field);
      }
    }

    @Override
    public int getRepeatedFieldCount(final FieldDescriptor field) {
      if (field.isExtension()) {
        verifyContainingType(field);
        return extensions.getRepeatedFieldCount(field);
      } else {
        return super.getRepeatedFieldCount(field);
      }
    }

    @Override
    public Object getRepeatedField(final FieldDescriptor field,
                                   final int index) {
      if (field.isExtension()) {
        verifyContainingType(field);
        return extensions.getRepeatedField(field, index);
      } else {
        return super.getRepeatedField(field, index);
      }
    }

    private void verifyContainingType(final FieldDescriptor field) {
      if (field.getContainingType() != getDescriptorForType()) {
        throw new IllegalArgumentException(
          "FieldDescriptor does not match message type.");
      }
    }
  }

  @SuppressWarnings("unchecked")
  public abstract static class ExtendableBuilder<
        MessageType extends ExtendableMessage,
        BuilderType extends ExtendableBuilder>
      extends Builder<BuilderType>
      implements ExtendableMessageOrBuilder<MessageType> {

    private FieldSet<FieldDescriptor> extensions = FieldSet.emptySet();

    protected ExtendableBuilder() {}

    protected ExtendableBuilder(
        BuilderParent parent) {
      super(parent);
    }

    @Override
    public BuilderType clear() {
      extensions = FieldSet.emptySet();
      return super.clear();
    }

    @Override
    public BuilderType clone() {
      throw new UnsupportedOperationException(
          "This is supposed to be overridden by subclasses.");
    }

    private void ensureExtensionsIsMutable() {
      if (extensions.isImmutable()) {
        extensions = extensions.clone();
      }
    }

    private void verifyExtensionContainingType(
        final GeneratedExtension<MessageType, ?> extension) {
      if (extension.getDescriptor().getContainingType() !=
          getDescriptorForType()) {

        throw new IllegalArgumentException(
          "Extension is for type \"" +
          extension.getDescriptor().getContainingType().getFullName() +
          "\" which does not match message type \"" +
          getDescriptorForType().getFullName() + "\".");
      }
    }

    public final <Type> boolean hasExtension(
        final GeneratedExtension<MessageType, Type> extension) {
      verifyExtensionContainingType(extension);
      return extensions.hasField(extension.getDescriptor());
    }

    public final <Type> int getExtensionCount(
        final GeneratedExtension<MessageType, List<Type>> extension) {
      verifyExtensionContainingType(extension);
      final FieldDescriptor descriptor = extension.getDescriptor();
      return extensions.getRepeatedFieldCount(descriptor);
    }

    public final <Type> Type getExtension(
        final GeneratedExtension<MessageType, Type> extension) {
      verifyExtensionContainingType(extension);
      FieldDescriptor descriptor = extension.getDescriptor();
      final Object value = extensions.getField(descriptor);
      if (value == null) {
        if (descriptor.isRepeated()) {
          return (Type) Collections.emptyList();
        } else if (descriptor.getJavaType() ==
                   FieldDescriptor.JavaType.MESSAGE) {
          return (Type) extension.getMessageDefaultInstance();
        } else {
          return (Type) extension.fromReflectionType(
              descriptor.getDefaultValue());
        }
      } else {
        return (Type) extension.fromReflectionType(value);
      }
    }

    public final <Type> Type getExtension(
        final GeneratedExtension<MessageType, List<Type>> extension,
        final int index) {
      verifyExtensionContainingType(extension);
      FieldDescriptor descriptor = extension.getDescriptor();
      return (Type) extension.singularFromReflectionType(
          extensions.getRepeatedField(descriptor, index));
    }

    public final <Type> BuilderType setExtension(
        final GeneratedExtension<MessageType, Type> extension,
        final Type value) {
      verifyExtensionContainingType(extension);
      ensureExtensionsIsMutable();
      final FieldDescriptor descriptor = extension.getDescriptor();
      extensions.setField(descriptor, extension.toReflectionType(value));
      onChanged();
      return (BuilderType) this;
    }

    public final <Type> BuilderType setExtension(
        final GeneratedExtension<MessageType, List<Type>> extension,
        final int index, final Type value) {
      verifyExtensionContainingType(extension);
      ensureExtensionsIsMutable();
      final FieldDescriptor descriptor = extension.getDescriptor();
      extensions.setRepeatedField(
        descriptor, index,
        extension.singularToReflectionType(value));
      onChanged();
      return (BuilderType) this;
    }

    public final <Type> BuilderType addExtension(
        final GeneratedExtension<MessageType, List<Type>> extension,
        final Type value) {
      verifyExtensionContainingType(extension);
      ensureExtensionsIsMutable();
      final FieldDescriptor descriptor = extension.getDescriptor();
      extensions.addRepeatedField(
          descriptor, extension.singularToReflectionType(value));
      onChanged();
      return (BuilderType) this;
    }

    public final <Type> BuilderType clearExtension(
        final GeneratedExtension<MessageType, ?> extension) {
      verifyExtensionContainingType(extension);
      ensureExtensionsIsMutable();
      extensions.clearField(extension.getDescriptor());
      onChanged();
      return (BuilderType) this;
    }

    protected boolean extensionsAreInitialized() {
      return extensions.isInitialized();
    }

    private FieldSet<FieldDescriptor> buildExtensions() {
      extensions.makeImmutable();
      return extensions;
    }

    @Override
    public boolean isInitialized() {
      return super.isInitialized() && extensionsAreInitialized();
    }

    @Override
    protected boolean parseUnknownField(
        final CodedInputStream input,
        final UnknownFieldSet.Builder unknownFields,
        final ExtensionRegistryLite extensionRegistry,
        final int tag) throws IOException {
      return AbstractMessage.Builder.mergeFieldFrom(
        input, unknownFields, extensionRegistry, getDescriptorForType(),
        this, null, tag);
    }

    @Override
    public Map<FieldDescriptor, Object> getAllFields() {
      final Map<FieldDescriptor, Object> result = super.getAllFieldsMutable();
      result.putAll(extensions.getAllFields());
      return Collections.unmodifiableMap(result);
    }

    @Override
    public Object getField(final FieldDescriptor field) {
      if (field.isExtension()) {
        verifyContainingType(field);
        final Object value = extensions.getField(field);
        if (value == null) {
          if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {

            return DynamicMessage.getDefaultInstance(field.getMessageType());
          } else {
            return field.getDefaultValue();
          }
        } else {
          return value;
        }
      } else {
        return super.getField(field);
      }
    }

    @Override
    public int getRepeatedFieldCount(final FieldDescriptor field) {
      if (field.isExtension()) {
        verifyContainingType(field);
        return extensions.getRepeatedFieldCount(field);
      } else {
        return super.getRepeatedFieldCount(field);
      }
    }

    @Override
    public Object getRepeatedField(final FieldDescriptor field,
                                   final int index) {
      if (field.isExtension()) {
        verifyContainingType(field);
        return extensions.getRepeatedField(field, index);
      } else {
        return super.getRepeatedField(field, index);
      }
    }

    @Override
    public boolean hasField(final FieldDescriptor field) {
      if (field.isExtension()) {
        verifyContainingType(field);
        return extensions.hasField(field);
      } else {
        return super.hasField(field);
      }
    }

    @Override
    public BuilderType setField(final FieldDescriptor field,
                                final Object value) {
      if (field.isExtension()) {
        verifyContainingType(field);
        ensureExtensionsIsMutable();
        extensions.setField(field, value);
        onChanged();
        return (BuilderType) this;
      } else {
        return super.setField(field, value);
      }
    }

    @Override
    public BuilderType clearField(final FieldDescriptor field) {
      if (field.isExtension()) {
        verifyContainingType(field);
        ensureExtensionsIsMutable();
        extensions.clearField(field);
        onChanged();
        return (BuilderType) this;
      } else {
        return super.clearField(field);
      }
    }

    @Override
    public BuilderType setRepeatedField(final FieldDescriptor field,
                                        final int index, final Object value) {
      if (field.isExtension()) {
        verifyContainingType(field);
        ensureExtensionsIsMutable();
        extensions.setRepeatedField(field, index, value);
        onChanged();
        return (BuilderType) this;
      } else {
        return super.setRepeatedField(field, index, value);
      }
    }

    @Override
    public BuilderType addRepeatedField(final FieldDescriptor field,
                                        final Object value) {
      if (field.isExtension()) {
        verifyContainingType(field);
        ensureExtensionsIsMutable();
        extensions.addRepeatedField(field, value);
        onChanged();
        return (BuilderType) this;
      } else {
        return super.addRepeatedField(field, value);
      }
    }

    protected final void mergeExtensionFields(final ExtendableMessage other) {
      ensureExtensionsIsMutable();
      extensions.mergeFrom(other.extensions);
      onChanged();
    }

    private void verifyContainingType(final FieldDescriptor field) {
      if (field.getContainingType() != getDescriptorForType()) {
        throw new IllegalArgumentException(
          "FieldDescriptor does not match message type.");
      }
    }
  }

  private static interface ExtensionDescriptorRetriever {
    FieldDescriptor getDescriptor();
  }

  public static <ContainingType extends Message, Type>
      GeneratedExtension<ContainingType, Type>
      newMessageScopedGeneratedExtension(final Message scope,
                                         final int descriptorIndex,
                                         final Class singularType,
                                         final Message defaultInstance) {

    return new GeneratedExtension<ContainingType, Type>(
        new ExtensionDescriptorRetriever() {

          public FieldDescriptor getDescriptor() {
            return scope.getDescriptorForType().getExtensions()
                .get(descriptorIndex);
          }
        },
        singularType,
        defaultInstance);
  }

  public static <ContainingType extends Message, Type>
     GeneratedExtension<ContainingType, Type>
     newFileScopedGeneratedExtension(final Class singularType,
                                     final Message defaultInstance) {

    return new GeneratedExtension<ContainingType, Type>(
        null,  
        singularType,
        defaultInstance);
  }

  public static final class GeneratedExtension<
      ContainingType extends Message, Type> {

    private GeneratedExtension(ExtensionDescriptorRetriever descriptorRetriever,
                               Class singularType,
                               Message messageDefaultInstance) {
      if (Message.class.isAssignableFrom(singularType) &&
          !singularType.isInstance(messageDefaultInstance)) {
        throw new IllegalArgumentException(
            "Bad messageDefaultInstance for " + singularType.getName());
      }
      this.descriptorRetriever = descriptorRetriever;
      this.singularType = singularType;
      this.messageDefaultInstance = messageDefaultInstance;

      if (ProtocolMessageEnum.class.isAssignableFrom(singularType)) {
        this.enumValueOf = getMethodOrDie(singularType, "valueOf",
                                          EnumValueDescriptor.class);
        this.enumGetValueDescriptor =
            getMethodOrDie(singularType, "getValueDescriptor");
      } else {
        this.enumValueOf = null;
        this.enumGetValueDescriptor = null;
      }
    }

    public void internalInit(final FieldDescriptor descriptor) {
      if (descriptorRetriever != null) {
        throw new IllegalStateException("Already initialized.");
      }
      descriptorRetriever = new ExtensionDescriptorRetriever() {

          public FieldDescriptor getDescriptor() {
            return descriptor;
          }
        };
    }

    private ExtensionDescriptorRetriever descriptorRetriever;
    private final Class singularType;
    private final Message messageDefaultInstance;
    private final Method enumValueOf;
    private final Method enumGetValueDescriptor;

    public FieldDescriptor getDescriptor() {
      if (descriptorRetriever == null) {
        throw new IllegalStateException(
            "getDescriptor() called before internalInit()");
      }
      return descriptorRetriever.getDescriptor();
    }

    public Message getMessageDefaultInstance() {
      return messageDefaultInstance;
    }

    @SuppressWarnings("unchecked")
    private Object fromReflectionType(final Object value) {
      FieldDescriptor descriptor = getDescriptor();
      if (descriptor.isRepeated()) {
        if (descriptor.getJavaType() == FieldDescriptor.JavaType.MESSAGE ||
            descriptor.getJavaType() == FieldDescriptor.JavaType.ENUM) {

          final List result = new ArrayList();
          for (final Object element : (List) value) {
            result.add(singularFromReflectionType(element));
          }
          return result;
        } else {
          return value;
        }
      } else {
        return singularFromReflectionType(value);
      }
    }

    private Object singularFromReflectionType(final Object value) {
      FieldDescriptor descriptor = getDescriptor();
      switch (descriptor.getJavaType()) {
        case MESSAGE:
          if (singularType.isInstance(value)) {
            return value;
          } else {

            return messageDefaultInstance.newBuilderForType()
                           .mergeFrom((Message) value).build();
          }
        case ENUM:
          return invokeOrDie(enumValueOf, null, (EnumValueDescriptor) value);
        default:
          return value;
      }
    }

    @SuppressWarnings("unchecked")
    private Object toReflectionType(final Object value) {
      FieldDescriptor descriptor = getDescriptor();
      if (descriptor.isRepeated()) {
        if (descriptor.getJavaType() == FieldDescriptor.JavaType.ENUM) {

          final List result = new ArrayList();
          for (final Object element : (List) value) {
            result.add(singularToReflectionType(element));
          }
          return result;
        } else {
          return value;
        }
      } else {
        return singularToReflectionType(value);
      }
    }

    private Object singularToReflectionType(final Object value) {
      FieldDescriptor descriptor = getDescriptor();
      switch (descriptor.getJavaType()) {
        case ENUM:
          return invokeOrDie(enumGetValueDescriptor, value);
        default:
          return value;
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static Method getMethodOrDie(
      final Class clazz, final String name, final Class... params) {
    try {
      return clazz.getMethod(name, params);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(
        "Generated message class \"" + clazz.getName() +
        "\" missing method \"" + name + "\".", e);
    }
  }

  private static Object invokeOrDie(
      final Method method, final Object object, final Object... params) {
    try {
      return method.invoke(object, params);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(
        "Couldn't use Java reflection to implement protocol message " +
        "reflection.", e);
    } catch (InvocationTargetException e) {
      final Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      } else if (cause instanceof Error) {
        throw (Error) cause;
      } else {
        throw new RuntimeException(
          "Unexpected exception thrown by generated accessor method.", cause);
      }
    }
  }

  public static final class FieldAccessorTable {

    public FieldAccessorTable(
        final Descriptor descriptor,
        final String[] camelCaseNames,
        final Class<? extends GeneratedMessage> messageClass,
        final Class<? extends Builder> builderClass) {
      this(descriptor, camelCaseNames);
      ensureFieldAccessorsInitialized(messageClass, builderClass);
    }

    public FieldAccessorTable(
        final Descriptor descriptor,
        final String[] camelCaseNames) {
      this.descriptor = descriptor;
      this.camelCaseNames = camelCaseNames;
      fields = new FieldAccessor[descriptor.getFields().size()];
      initialized = false;
    }

    public FieldAccessorTable ensureFieldAccessorsInitialized(
        Class<? extends GeneratedMessage> messageClass,
        Class<? extends Builder> builderClass) {
      if (initialized) { return this; }
      synchronized (this) {
        if (initialized) { return this; }
        for (int i = 0; i < fields.length; i++) {
          FieldDescriptor field = descriptor.getFields().get(i);
          if (field.isRepeated()) {
            if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
              fields[i] = new RepeatedMessageFieldAccessor(
                  field, camelCaseNames[i], messageClass, builderClass);
            } else if (field.getJavaType() == FieldDescriptor.JavaType.ENUM) {
              fields[i] = new RepeatedEnumFieldAccessor(
                  field, camelCaseNames[i], messageClass, builderClass);
            } else {
              fields[i] = new RepeatedFieldAccessor(
                  field, camelCaseNames[i], messageClass, builderClass);
            }
          } else {
            if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
              fields[i] = new SingularMessageFieldAccessor(
                  field, camelCaseNames[i], messageClass, builderClass);
            } else if (field.getJavaType() == FieldDescriptor.JavaType.ENUM) {
              fields[i] = new SingularEnumFieldAccessor(
                  field, camelCaseNames[i], messageClass, builderClass);
            } else {
              fields[i] = new SingularFieldAccessor(
                  field, camelCaseNames[i], messageClass, builderClass);
            }
          }
        }
        initialized = true;
        camelCaseNames = null;
        return this;
      }
    }

    private final Descriptor descriptor;
    private final FieldAccessor[] fields;
    private String[] camelCaseNames;
    private volatile boolean initialized;

    private FieldAccessor getField(final FieldDescriptor field) {
      if (field.getContainingType() != descriptor) {
        throw new IllegalArgumentException(
          "FieldDescriptor does not match message type.");
      } else if (field.isExtension()) {

        throw new IllegalArgumentException(
          "This type does not have extensions.");
      }
      return fields[field.getIndex()];
    }

    private interface FieldAccessor {
      Object get(GeneratedMessage message);
      Object get(GeneratedMessage.Builder builder);
      void set(Builder builder, Object value);
      Object getRepeated(GeneratedMessage message, int index);
      Object getRepeated(GeneratedMessage.Builder builder, int index);
      void setRepeated(Builder builder,
                       int index, Object value);
      void addRepeated(Builder builder, Object value);
      boolean has(GeneratedMessage message);
      boolean has(GeneratedMessage.Builder builder);
      int getRepeatedCount(GeneratedMessage message);
      int getRepeatedCount(GeneratedMessage.Builder builder);
      void clear(Builder builder);
      Message.Builder newBuilder();
      Message.Builder getBuilder(GeneratedMessage.Builder builder);
    }

    private static class SingularFieldAccessor implements FieldAccessor {
      SingularFieldAccessor(
          final FieldDescriptor descriptor, final String camelCaseName,
          final Class<? extends GeneratedMessage> messageClass,
          final Class<? extends Builder> builderClass) {
        getMethod = getMethodOrDie(messageClass, "get" + camelCaseName);
        getMethodBuilder = getMethodOrDie(builderClass, "get" + camelCaseName);
        type = getMethod.getReturnType();
        setMethod = getMethodOrDie(builderClass, "set" + camelCaseName, type);
        hasMethod =
            getMethodOrDie(messageClass, "has" + camelCaseName);
        hasMethodBuilder =
            getMethodOrDie(builderClass, "has" + camelCaseName);
        clearMethod = getMethodOrDie(builderClass, "clear" + camelCaseName);
      }

      protected final Class<?> type;
      protected final Method getMethod;
      protected final Method getMethodBuilder;
      protected final Method setMethod;
      protected final Method hasMethod;
      protected final Method hasMethodBuilder;
      protected final Method clearMethod;

      public Object get(final GeneratedMessage message) {
        return invokeOrDie(getMethod, message);
      }
      public Object get(GeneratedMessage.Builder builder) {
        return invokeOrDie(getMethodBuilder, builder);
      }
      public void set(final Builder builder, final Object value) {
        invokeOrDie(setMethod, builder, value);
      }
      public Object getRepeated(final GeneratedMessage message,
                                final int index) {
        throw new UnsupportedOperationException(
          "getRepeatedField() called on a singular field.");
      }
      public Object getRepeated(GeneratedMessage.Builder builder, int index) {
        throw new UnsupportedOperationException(
          "getRepeatedField() called on a singular field.");
      }
      public void setRepeated(final Builder builder,
                              final int index, final Object value) {
        throw new UnsupportedOperationException(
          "setRepeatedField() called on a singular field.");
      }
      public void addRepeated(final Builder builder, final Object value) {
        throw new UnsupportedOperationException(
          "addRepeatedField() called on a singular field.");
      }
      public boolean has(final GeneratedMessage message) {
        return (Boolean) invokeOrDie(hasMethod, message);
      }
      public boolean has(GeneratedMessage.Builder builder) {
        return (Boolean) invokeOrDie(hasMethodBuilder, builder);
      }
      public int getRepeatedCount(final GeneratedMessage message) {
        throw new UnsupportedOperationException(
          "getRepeatedFieldSize() called on a singular field.");
      }
      public int getRepeatedCount(GeneratedMessage.Builder builder) {
        throw new UnsupportedOperationException(
          "getRepeatedFieldSize() called on a singular field.");
      }
      public void clear(final Builder builder) {
        invokeOrDie(clearMethod, builder);
      }
      public Message.Builder newBuilder() {
        throw new UnsupportedOperationException(
          "newBuilderForField() called on a non-Message type.");
      }
      public Message.Builder getBuilder(GeneratedMessage.Builder builder) {
        throw new UnsupportedOperationException(
          "getFieldBuilder() called on a non-Message type.");
      }
    }

    private static class RepeatedFieldAccessor implements FieldAccessor {
      protected final Class type;
      protected final Method getMethod;
      protected final Method getMethodBuilder;
      protected final Method getRepeatedMethod;
      protected final Method getRepeatedMethodBuilder;
      protected final Method setRepeatedMethod;
      protected final Method addRepeatedMethod;
      protected final Method getCountMethod;
      protected final Method getCountMethodBuilder;
      protected final Method clearMethod;

      RepeatedFieldAccessor(
          final FieldDescriptor descriptor, final String camelCaseName,
          final Class<? extends GeneratedMessage> messageClass,
          final Class<? extends Builder> builderClass) {
        getMethod = getMethodOrDie(messageClass,
                                   "get" + camelCaseName + "List");
        getMethodBuilder = getMethodOrDie(builderClass,
                                   "get" + camelCaseName + "List");
        getRepeatedMethod =
            getMethodOrDie(messageClass, "get" + camelCaseName, Integer.TYPE);
        getRepeatedMethodBuilder =
            getMethodOrDie(builderClass, "get" + camelCaseName, Integer.TYPE);
        type = getRepeatedMethod.getReturnType();
        setRepeatedMethod =
            getMethodOrDie(builderClass, "set" + camelCaseName,
                           Integer.TYPE, type);
        addRepeatedMethod =
            getMethodOrDie(builderClass, "add" + camelCaseName, type);
        getCountMethod =
            getMethodOrDie(messageClass, "get" + camelCaseName + "Count");
        getCountMethodBuilder =
            getMethodOrDie(builderClass, "get" + camelCaseName + "Count");

        clearMethod = getMethodOrDie(builderClass, "clear" + camelCaseName);
      }

      public Object get(final GeneratedMessage message) {
        return invokeOrDie(getMethod, message);
      }
      public Object get(GeneratedMessage.Builder builder) {
        return invokeOrDie(getMethodBuilder, builder);
      }
      public void set(final Builder builder, final Object value) {

        clear(builder);
        for (final Object element : (List<?>) value) {
          addRepeated(builder, element);
        }
      }
      public Object getRepeated(final GeneratedMessage message,
                                final int index) {
        return invokeOrDie(getRepeatedMethod, message, index);
      }
      public Object getRepeated(GeneratedMessage.Builder builder, int index) {
        return invokeOrDie(getRepeatedMethodBuilder, builder, index);
      }
      public void setRepeated(final Builder builder,
                              final int index, final Object value) {
        invokeOrDie(setRepeatedMethod, builder, index, value);
      }
      public void addRepeated(final Builder builder, final Object value) {
        invokeOrDie(addRepeatedMethod, builder, value);
      }
      public boolean has(final GeneratedMessage message) {
        throw new UnsupportedOperationException(
          "hasField() called on a repeated field.");
      }
      public boolean has(GeneratedMessage.Builder builder) {
        throw new UnsupportedOperationException(
          "hasField() called on a repeated field.");
      }
      public int getRepeatedCount(final GeneratedMessage message) {
        return (Integer) invokeOrDie(getCountMethod, message);
      }
      public int getRepeatedCount(GeneratedMessage.Builder builder) {
        return (Integer) invokeOrDie(getCountMethodBuilder, builder);
      }
      public void clear(final Builder builder) {
        invokeOrDie(clearMethod, builder);
      }
      public Message.Builder newBuilder() {
        throw new UnsupportedOperationException(
          "newBuilderForField() called on a non-Message type.");
      }
      public Message.Builder getBuilder(GeneratedMessage.Builder builder) {
        throw new UnsupportedOperationException(
          "getFieldBuilder() called on a non-Message type.");
      }
    }

    private static final class SingularEnumFieldAccessor
        extends SingularFieldAccessor {
      SingularEnumFieldAccessor(
          final FieldDescriptor descriptor, final String camelCaseName,
          final Class<? extends GeneratedMessage> messageClass,
          final Class<? extends Builder> builderClass) {
        super(descriptor, camelCaseName, messageClass, builderClass);

        valueOfMethod = getMethodOrDie(type, "valueOf",
                                       EnumValueDescriptor.class);
        getValueDescriptorMethod =
          getMethodOrDie(type, "getValueDescriptor");
      }

      private Method valueOfMethod;
      private Method getValueDescriptorMethod;

      @Override
      public Object get(final GeneratedMessage message) {
        return invokeOrDie(getValueDescriptorMethod, super.get(message));
      }

      @Override
      public Object get(final GeneratedMessage.Builder builder) {
        return invokeOrDie(getValueDescriptorMethod, super.get(builder));
      }

      @Override
      public void set(final Builder builder, final Object value) {
        super.set(builder, invokeOrDie(valueOfMethod, null, value));
      }
    }

    private static final class RepeatedEnumFieldAccessor
        extends RepeatedFieldAccessor {
      RepeatedEnumFieldAccessor(
          final FieldDescriptor descriptor, final String camelCaseName,
          final Class<? extends GeneratedMessage> messageClass,
          final Class<? extends Builder> builderClass) {
        super(descriptor, camelCaseName, messageClass, builderClass);

        valueOfMethod = getMethodOrDie(type, "valueOf",
                                       EnumValueDescriptor.class);
        getValueDescriptorMethod =
          getMethodOrDie(type, "getValueDescriptor");
      }

      private final Method valueOfMethod;
      private final Method getValueDescriptorMethod;

      @Override
      @SuppressWarnings("unchecked")
      public Object get(final GeneratedMessage message) {
        final List newList = new ArrayList();
        for (final Object element : (List) super.get(message)) {
          newList.add(invokeOrDie(getValueDescriptorMethod, element));
        }
        return Collections.unmodifiableList(newList);
      }

      @Override
      @SuppressWarnings("unchecked")
      public Object get(final GeneratedMessage.Builder builder) {
        final List newList = new ArrayList();
        for (final Object element : (List) super.get(builder)) {
          newList.add(invokeOrDie(getValueDescriptorMethod, element));
        }
        return Collections.unmodifiableList(newList);
      }

      @Override
      public Object getRepeated(final GeneratedMessage message,
                                final int index) {
        return invokeOrDie(getValueDescriptorMethod,
          super.getRepeated(message, index));
      }
      @Override
      public Object getRepeated(final GeneratedMessage.Builder builder,
                                final int index) {
        return invokeOrDie(getValueDescriptorMethod,
          super.getRepeated(builder, index));
      }
      @Override
      public void setRepeated(final Builder builder,
                              final int index, final Object value) {
        super.setRepeated(builder, index, invokeOrDie(valueOfMethod, null,
                          value));
      }
      @Override
      public void addRepeated(final Builder builder, final Object value) {
        super.addRepeated(builder, invokeOrDie(valueOfMethod, null, value));
      }
    }

    private static final class SingularMessageFieldAccessor
        extends SingularFieldAccessor {
      SingularMessageFieldAccessor(
          final FieldDescriptor descriptor, final String camelCaseName,
          final Class<? extends GeneratedMessage> messageClass,
          final Class<? extends Builder> builderClass) {
        super(descriptor, camelCaseName, messageClass, builderClass);

        newBuilderMethod = getMethodOrDie(type, "newBuilder");
        getBuilderMethodBuilder =
            getMethodOrDie(builderClass, "get" + camelCaseName + "Builder");
      }

      private final Method newBuilderMethod;
      private final Method getBuilderMethodBuilder;

      private Object coerceType(final Object value) {
        if (type.isInstance(value)) {
          return value;
        } else {

          return ((Message.Builder) invokeOrDie(newBuilderMethod, null))
                  .mergeFrom((Message) value).buildPartial();
        }
      }

      @Override
      public void set(final Builder builder, final Object value) {
        super.set(builder, coerceType(value));
      }
      @Override
      public Message.Builder newBuilder() {
        return (Message.Builder) invokeOrDie(newBuilderMethod, null);
      }
      @Override
      public Message.Builder getBuilder(GeneratedMessage.Builder builder) {
        return (Message.Builder) invokeOrDie(getBuilderMethodBuilder, builder);
      }
    }

    private static final class RepeatedMessageFieldAccessor
        extends RepeatedFieldAccessor {
      RepeatedMessageFieldAccessor(
          final FieldDescriptor descriptor, final String camelCaseName,
          final Class<? extends GeneratedMessage> messageClass,
          final Class<? extends Builder> builderClass) {
        super(descriptor, camelCaseName, messageClass, builderClass);

        newBuilderMethod = getMethodOrDie(type, "newBuilder");
      }

      private final Method newBuilderMethod;

      private Object coerceType(final Object value) {
        if (type.isInstance(value)) {
          return value;
        } else {

          return ((Message.Builder) invokeOrDie(newBuilderMethod, null))
                  .mergeFrom((Message) value).build();
        }
      }

      @Override
      public void setRepeated(final Builder builder,
                              final int index, final Object value) {
        super.setRepeated(builder, index, coerceType(value));
      }
      @Override
      public void addRepeated(final Builder builder, final Object value) {
        super.addRepeated(builder, coerceType(value));
      }
      @Override
      public Message.Builder newBuilder() {
        return (Message.Builder) invokeOrDie(newBuilderMethod, null);
      }
    }
  }

  protected Object writeReplace() throws ObjectStreamException {
    return new GeneratedMessageLite.SerializedForm(this);
  }
}
