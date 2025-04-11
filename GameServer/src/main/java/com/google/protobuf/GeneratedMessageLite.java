package com.google.protobuf;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class GeneratedMessageLite extends AbstractMessageLite
        implements Serializable {
    private static final long serialVersionUID = 1L;

    protected GeneratedMessageLite() {
    }

    protected GeneratedMessageLite(Builder builder) {
    }

    private static <MessageType extends MessageLite>
    boolean parseUnknownField(
            FieldSet<ExtensionDescriptor> extensions,
            MessageType defaultInstance,
            CodedInputStream input,
            ExtensionRegistryLite extensionRegistry,
            int tag) throws IOException {
        int wireType = WireFormat.getTagWireType(tag);
        int fieldNumber = WireFormat.getTagFieldNumber(tag);

        GeneratedExtension<MessageType, ?> extension =
                extensionRegistry.findLiteExtensionByNumber(
                        defaultInstance, fieldNumber);

        boolean unknown = false;
        boolean packed = false;
        if (extension == null) {
            unknown = true;
        } else if (wireType == FieldSet.getWireFormatForFieldType(
                extension.descriptor.getLiteType(),
                false)) {
            packed = false;
        } else if (extension.descriptor.isRepeated &&
                extension.descriptor.type.isPackable() &&
                wireType == FieldSet.getWireFormatForFieldType(
                        extension.descriptor.getLiteType(),
                        true)) {
            packed = true;
        } else {
            unknown = true;
        }

        if (unknown) {
            return input.skipField(tag);
        }

        if (packed) {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            if (extension.descriptor.getLiteType() == WireFormat.FieldType.ENUM) {
                while (input.getBytesUntilLimit() > 0) {
                    int rawValue = input.readEnum();
                    Object value =
                            extension.descriptor.getEnumType().findValueByNumber(rawValue);
                    if (value == null) {

                        return true;
                    }
                    extensions.addRepeatedField(extension.descriptor, value);
                }
            } else {
                while (input.getBytesUntilLimit() > 0) {
                    Object value =
                            FieldSet.readPrimitiveField(input,
                                    extension.descriptor.getLiteType());
                    extensions.addRepeatedField(extension.descriptor, value);
                }
            }
            input.popLimit(limit);
        } else {
            Object value;
            switch (extension.descriptor.getLiteJavaType()) {
                case MESSAGE: {
                    MessageLite.Builder subBuilder = null;
                    if (!extension.descriptor.isRepeated()) {
                        MessageLite existingValue =
                                (MessageLite) extensions.getField(extension.descriptor);
                        if (existingValue != null) {
                            subBuilder = existingValue.toBuilder();
                        }
                    }
                    if (subBuilder == null) {
                        subBuilder = extension.messageDefaultInstance.newBuilderForType();
                    }
                    if (extension.descriptor.getLiteType() ==
                            WireFormat.FieldType.GROUP) {
                        input.readGroup(extension.getNumber(),
                                subBuilder, extensionRegistry);
                    } else {
                        input.readMessage(subBuilder, extensionRegistry);
                    }
                    value = subBuilder.build();
                    break;
                }
                case ENUM:
                    int rawValue = input.readEnum();
                    value = extension.descriptor.getEnumType()
                            .findValueByNumber(rawValue);

                    if (value == null) {
                        return true;
                    }
                    break;
                default:
                    value = FieldSet.readPrimitiveField(input,
                            extension.descriptor.getLiteType());
                    break;
            }

            if (extension.descriptor.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, value);
            } else {
                extensions.setField(extension.descriptor, value);
            }
        }

        return true;
    }

    public static <ContainingType extends MessageLite, Type>
    GeneratedExtension<ContainingType, Type>
    newSingularGeneratedExtension(
            final ContainingType containingTypeDefaultInstance,
            final Type defaultValue,
            final MessageLite messageDefaultInstance,
            final Internal.EnumLiteMap<?> enumTypeMap,
            final int number,
            final WireFormat.FieldType type) {
        return new GeneratedExtension<ContainingType, Type>(
                containingTypeDefaultInstance,
                defaultValue,
                messageDefaultInstance,
                new ExtensionDescriptor(enumTypeMap, number, type,
                        false,
                        false));
    }

    public static <ContainingType extends MessageLite, Type>
    GeneratedExtension<ContainingType, Type>
    newRepeatedGeneratedExtension(
            final ContainingType containingTypeDefaultInstance,
            final MessageLite messageDefaultInstance,
            final Internal.EnumLiteMap<?> enumTypeMap,
            final int number,
            final WireFormat.FieldType type,
            final boolean isPacked) {
        @SuppressWarnings("unchecked")
        Type emptyList = (Type) Collections.emptyList();
        return new GeneratedExtension<ContainingType, Type>(
                containingTypeDefaultInstance,
                emptyList,
                messageDefaultInstance,
                new ExtensionDescriptor(
                        enumTypeMap, number, type, true, isPacked));
    }

    public Parser<? extends MessageLite> getParserForType() {
        throw new UnsupportedOperationException(
                "This is supposed to be overridden by subclasses.");
    }

    protected boolean parseUnknownField(
            CodedInputStream input,
            ExtensionRegistryLite extensionRegistry,
            int tag) throws IOException {
        return input.skipField(tag);
    }

    protected void makeExtensionsImmutable() {

    }

    protected Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }

    public interface ExtendableMessageOrBuilder<
            MessageType extends ExtendableMessage> extends MessageLiteOrBuilder {

        <Type> boolean hasExtension(
                GeneratedExtension<MessageType, Type> extension);

        <Type> int getExtensionCount(
                GeneratedExtension<MessageType, List<Type>> extension);

        <Type> Type getExtension(GeneratedExtension<MessageType, Type> extension);

        <Type> Type getExtension(
                GeneratedExtension<MessageType, List<Type>> extension,
                int index);
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<MessageType extends GeneratedMessageLite,
            BuilderType extends Builder>
            extends AbstractMessageLite.Builder<BuilderType> {
        protected Builder() {
        }

        public BuilderType clear() {
            return (BuilderType) this;
        }

        @Override
        public BuilderType clone() {
            throw new UnsupportedOperationException(
                    "This is supposed to be overridden by subclasses.");
        }

        public abstract BuilderType mergeFrom(MessageType message);

        public abstract MessageType getDefaultInstanceForType();

        protected boolean parseUnknownField(
                CodedInputStream input,
                ExtensionRegistryLite extensionRegistry,
                int tag) throws IOException {
            return input.skipField(tag);
        }
    }

    public abstract static class ExtendableMessage<
            MessageType extends ExtendableMessage<MessageType>>
            extends GeneratedMessageLite
            implements ExtendableMessageOrBuilder<MessageType> {

        private final FieldSet<ExtensionDescriptor> extensions;

        protected ExtendableMessage() {
            this.extensions = FieldSet.newFieldSet();
        }

        protected ExtendableMessage(ExtendableBuilder<MessageType, ?> builder) {
            this.extensions = builder.buildExtensions();
        }

        private void verifyExtensionContainingType(
                final GeneratedExtension<MessageType, ?> extension) {
            if (extension.getContainingTypeDefaultInstance() !=
                    getDefaultInstanceForType()) {

                throw new IllegalArgumentException(
                        "This extension is for a different message type.  Please make " +
                                "sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(
                final GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            return extensions.hasField(extension.descriptor);
        }

        public final <Type> int getExtensionCount(
                final GeneratedExtension<MessageType, List<Type>> extension) {
            verifyExtensionContainingType(extension);
            return extensions.getRepeatedFieldCount(extension.descriptor);
        }

        @SuppressWarnings("unchecked")
        public final <Type> Type getExtension(
                final GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            final Object value = extensions.getField(extension.descriptor);
            if (value == null) {
                return extension.defaultValue;
            } else {
                return (Type) value;
            }
        }

        @SuppressWarnings("unchecked")
        public final <Type> Type getExtension(
                final GeneratedExtension<MessageType, List<Type>> extension,
                final int index) {
            verifyExtensionContainingType(extension);
            return (Type) extensions.getRepeatedField(extension.descriptor, index);
        }

        protected boolean extensionsAreInitialized() {
            return extensions.isInitialized();
        }

        @Override
        protected boolean parseUnknownField(
                CodedInputStream input,
                ExtensionRegistryLite extensionRegistry,
                int tag) throws IOException {
            return GeneratedMessageLite.parseUnknownField(
                    extensions,
                    getDefaultInstanceForType(),
                    input,
                    extensionRegistry,
                    tag);
        }

        @Override
        protected void makeExtensionsImmutable() {
            extensions.makeImmutable();
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

        protected class ExtensionWriter {

            private final Iterator<Map.Entry<ExtensionDescriptor, Object>> iter =
                    extensions.iterator();
            private final boolean messageSetWireFormat;
            private Map.Entry<ExtensionDescriptor, Object> next;

            private ExtensionWriter(boolean messageSetWireFormat) {
                if (iter.hasNext()) {
                    next = iter.next();
                }
                this.messageSetWireFormat = messageSetWireFormat;
            }

            public void writeUntil(final int end, final CodedOutputStream output)
                    throws IOException {
                while (next != null && next.getKey().getNumber() < end) {
                    ExtensionDescriptor extension = next.getKey();
                    if (messageSetWireFormat && extension.getLiteJavaType() ==
                            WireFormat.JavaType.MESSAGE &&
                            !extension.isRepeated()) {
                        output.writeMessageSetExtension(extension.getNumber(),
                                (MessageLite) next.getValue());
                    } else {
                        FieldSet.writeField(extension, next.getValue(), output);
                    }
                    if (iter.hasNext()) {
                        next = iter.next();
                    } else {
                        next = null;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class ExtendableBuilder<
            MessageType extends ExtendableMessage<MessageType>,
            BuilderType extends ExtendableBuilder<MessageType, BuilderType>>
            extends Builder<MessageType, BuilderType>
            implements ExtendableMessageOrBuilder<MessageType> {
        private FieldSet<ExtensionDescriptor> extensions = FieldSet.emptySet();
        private boolean extensionsIsMutable;
        protected ExtendableBuilder() {
        }

        @Override
        public BuilderType clear() {
            extensions.clear();
            extensionsIsMutable = false;
            return super.clear();
        }

        private void ensureExtensionsIsMutable() {
            if (!extensionsIsMutable) {
                extensions = extensions.clone();
                extensionsIsMutable = true;
            }
        }

        private FieldSet<ExtensionDescriptor> buildExtensions() {
            extensions.makeImmutable();
            extensionsIsMutable = false;
            return extensions;
        }

        private void verifyExtensionContainingType(
                final GeneratedExtension<MessageType, ?> extension) {
            if (extension.getContainingTypeDefaultInstance() !=
                    getDefaultInstanceForType()) {

                throw new IllegalArgumentException(
                        "This extension is for a different message type.  Please make " +
                                "sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(
                final GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            return extensions.hasField(extension.descriptor);
        }

        public final <Type> int getExtensionCount(
                final GeneratedExtension<MessageType, List<Type>> extension) {
            verifyExtensionContainingType(extension);
            return extensions.getRepeatedFieldCount(extension.descriptor);
        }

        @SuppressWarnings("unchecked")
        public final <Type> Type getExtension(
                final GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            final Object value = extensions.getField(extension.descriptor);
            if (value == null) {
                return extension.defaultValue;
            } else {
                return (Type) value;
            }
        }

        @SuppressWarnings("unchecked")

        public final <Type> Type getExtension(
                final GeneratedExtension<MessageType, List<Type>> extension,
                final int index) {
            verifyExtensionContainingType(extension);
            return (Type) extensions.getRepeatedField(extension.descriptor, index);
        }

        @Override
        public BuilderType clone() {
            throw new UnsupportedOperationException(
                    "This is supposed to be overridden by subclasses.");
        }

        public final <Type> BuilderType setExtension(
                final GeneratedExtension<MessageType, Type> extension,
                final Type value) {
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            extensions.setField(extension.descriptor, value);
            return (BuilderType) this;
        }

        public final <Type> BuilderType setExtension(
                final GeneratedExtension<MessageType, List<Type>> extension,
                final int index, final Type value) {
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            extensions.setRepeatedField(extension.descriptor, index, value);
            return (BuilderType) this;
        }

        public final <Type> BuilderType addExtension(
                final GeneratedExtension<MessageType, List<Type>> extension,
                final Type value) {
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            extensions.addRepeatedField(extension.descriptor, value);
            return (BuilderType) this;
        }

        public final <Type> BuilderType clearExtension(
                final GeneratedExtension<MessageType, ?> extension) {
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            extensions.clearField(extension.descriptor);
            return (BuilderType) this;
        }

        protected boolean extensionsAreInitialized() {
            return extensions.isInitialized();
        }

        @Override
        protected boolean parseUnknownField(
                CodedInputStream input,
                ExtensionRegistryLite extensionRegistry,
                int tag) throws IOException {
            ensureExtensionsIsMutable();
            return GeneratedMessageLite.parseUnknownField(
                    extensions,
                    getDefaultInstanceForType(),
                    input,
                    extensionRegistry,
                    tag);
        }

        protected final void mergeExtensionFields(final MessageType other) {
            ensureExtensionsIsMutable();
            extensions.mergeFrom(((ExtendableMessage) other).extensions);
        }
    }

    private static final class ExtensionDescriptor
            implements FieldSet.FieldDescriptorLite<
            ExtensionDescriptor> {
        private final Internal.EnumLiteMap<?> enumTypeMap;
        private final int number;
        private final WireFormat.FieldType type;
        private final boolean isRepeated;
        private final boolean isPacked;
        private ExtensionDescriptor(
                final Internal.EnumLiteMap<?> enumTypeMap,
                final int number,
                final WireFormat.FieldType type,
                final boolean isRepeated,
                final boolean isPacked) {
            this.enumTypeMap = enumTypeMap;
            this.number = number;
            this.type = type;
            this.isRepeated = isRepeated;
            this.isPacked = isPacked;
        }

        public int getNumber() {
            return number;
        }

        public WireFormat.FieldType getLiteType() {
            return type;
        }

        public WireFormat.JavaType getLiteJavaType() {
            return type.getJavaType();
        }

        public boolean isRepeated() {
            return isRepeated;
        }

        public boolean isPacked() {
            return isPacked;
        }

        public Internal.EnumLiteMap<?> getEnumType() {
            return enumTypeMap;
        }

        @SuppressWarnings("unchecked")
        public MessageLite.Builder internalMergeFrom(
                MessageLite.Builder to, MessageLite from) {
            return ((Builder) to).mergeFrom((GeneratedMessageLite) from);
        }

        public int compareTo(ExtensionDescriptor other) {
            return number - other.number;
        }
    }

    public static final class GeneratedExtension<
            ContainingType extends MessageLite, Type> {

        private final ContainingType containingTypeDefaultInstance;
        private final Type defaultValue;
        private final MessageLite messageDefaultInstance;
        private final ExtensionDescriptor descriptor;
        private GeneratedExtension(
                final ContainingType containingTypeDefaultInstance,
                final Type defaultValue,
                final MessageLite messageDefaultInstance,
                final ExtensionDescriptor descriptor) {

            if (containingTypeDefaultInstance == null) {
                throw new IllegalArgumentException(
                        "Null containingTypeDefaultInstance");
            }
            if (descriptor.getLiteType() == WireFormat.FieldType.MESSAGE &&
                    messageDefaultInstance == null) {
                throw new IllegalArgumentException(
                        "Null messageDefaultInstance");
            }
            this.containingTypeDefaultInstance = containingTypeDefaultInstance;
            this.defaultValue = defaultValue;
            this.messageDefaultInstance = messageDefaultInstance;
            this.descriptor = descriptor;
        }

        public ContainingType getContainingTypeDefaultInstance() {
            return containingTypeDefaultInstance;
        }

        public int getNumber() {
            return descriptor.getNumber();
        }

        public MessageLite getMessageDefaultInstance() {
            return messageDefaultInstance;
        }
    }

    static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0L;

        private String messageClassName;
        private byte[] asBytes;

        SerializedForm(MessageLite regularForm) {
            messageClassName = regularForm.getClass().getName();
            asBytes = regularForm.toByteArray();
        }

        @SuppressWarnings("unchecked")
        protected Object readResolve() throws ObjectStreamException {
            try {
                Class messageClass = Class.forName(messageClassName);
                Method newBuilder = messageClass.getMethod("newBuilder");
                MessageLite.Builder builder =
                        (MessageLite.Builder) newBuilder.invoke(null);
                builder.mergeFrom(asBytes);
                return builder.buildPartial();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find proto buffer class", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Unable to find newBuilder method", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to call newBuilder method", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Error calling newBuilder", e.getCause());
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException("Unable to understand proto buffer", e);
            }
        }
    }
}
