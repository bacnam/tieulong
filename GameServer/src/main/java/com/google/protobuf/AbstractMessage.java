package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage.ExtendableBuilder;
import com.google.protobuf.Internal.EnumLite;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractMessage extends AbstractMessageLite
        implements Message {
    private int memoizedSize = -1;

    private static String delimitWithCommas(List<String> parts) {
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(part);
        }
        return result.toString();
    }

    protected static int hashLong(long n) {
        return (int) (n ^ (n >>> 32));
    }

    protected static int hashBoolean(boolean b) {
        return b ? 1231 : 1237;
    }

    protected static int hashEnum(EnumLite e) {
        return e.getNumber();
    }

    protected static int hashEnumList(List<? extends EnumLite> list) {
        int hash = 1;
        for (EnumLite e : list) {
            hash = 31 * hash + hashEnum(e);
        }
        return hash;
    }

    @SuppressWarnings("unchecked")
    public boolean isInitialized() {

        for (final FieldDescriptor field : getDescriptorForType().getFields()) {
            if (field.isRequired()) {
                if (!hasField(field)) {
                    return false;
                }
            }
        }

        for (final Map.Entry<FieldDescriptor, Object> entry :
                getAllFields().entrySet()) {
            final FieldDescriptor field = entry.getKey();
            if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
                if (field.isRepeated()) {
                    for (final Message element : (List<Message>) entry.getValue()) {
                        if (!element.isInitialized()) {
                            return false;
                        }
                    }
                } else {
                    if (!((Message) entry.getValue()).isInitialized()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public List<String> findInitializationErrors() {
        return Builder.findMissingFields(this);
    }

    public String getInitializationErrorString() {
        return delimitWithCommas(findInitializationErrors());
    }

    @Override
    public final String toString() {
        return TextFormat.printToString(this);
    }

    public void writeTo(final CodedOutputStream output) throws IOException {
        final boolean isMessageSet =
                getDescriptorForType().getOptions().getMessageSetWireFormat();

        for (final Map.Entry<FieldDescriptor, Object> entry :
                getAllFields().entrySet()) {
            final FieldDescriptor field = entry.getKey();
            final Object value = entry.getValue();
            if (isMessageSet && field.isExtension() &&
                    field.getType() == FieldDescriptor.Type.MESSAGE &&
                    !field.isRepeated()) {
                output.writeMessageSetExtension(field.getNumber(), (Message) value);
            } else {
                FieldSet.writeField(field, value, output);
            }
        }

        final UnknownFieldSet unknownFields = getUnknownFields();
        if (isMessageSet) {
            unknownFields.writeAsMessageSetTo(output);
        } else {
            unknownFields.writeTo(output);
        }
    }

    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) {
            return size;
        }

        size = 0;
        final boolean isMessageSet =
                getDescriptorForType().getOptions().getMessageSetWireFormat();

        for (final Map.Entry<FieldDescriptor, Object> entry :
                getAllFields().entrySet()) {
            final FieldDescriptor field = entry.getKey();
            final Object value = entry.getValue();
            if (isMessageSet && field.isExtension() &&
                    field.getType() == FieldDescriptor.Type.MESSAGE &&
                    !field.isRepeated()) {
                size += CodedOutputStream.computeMessageSetExtensionSize(
                        field.getNumber(), (Message) value);
            } else {
                size += FieldSet.computeFieldSize(field, value);
            }
        }

        final UnknownFieldSet unknownFields = getUnknownFields();
        if (isMessageSet) {
            size += unknownFields.getSerializedSizeAsMessageSet();
        } else {
            size += unknownFields.getSerializedSize();
        }

        memoizedSize = size;
        return size;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Message)) {
            return false;
        }
        final Message otherMessage = (Message) other;
        if (getDescriptorForType() != otherMessage.getDescriptorForType()) {
            return false;
        }
        return getAllFields().equals(otherMessage.getAllFields()) &&
                getUnknownFields().equals(otherMessage.getUnknownFields());
    }

    @Override
    public int hashCode() {
        int hash = 41;
        hash = (19 * hash) + getDescriptorForType().hashCode();
        hash = hashFields(hash, getAllFields());
        hash = (29 * hash) + getUnknownFields().hashCode();
        return hash;
    }

    @SuppressWarnings("unchecked")
    protected int hashFields(int hash, Map<FieldDescriptor, Object> map) {
        for (Map.Entry<FieldDescriptor, Object> entry : map.entrySet()) {
            FieldDescriptor field = entry.getKey();
            Object value = entry.getValue();
            hash = (37 * hash) + field.getNumber();
            if (field.getType() != FieldDescriptor.Type.ENUM) {
                hash = (53 * hash) + value.hashCode();
            } else if (field.isRepeated()) {
                List<? extends EnumLite> list = (List<? extends EnumLite>) value;
                hash = (53 * hash) + hashEnumList(list);
            } else {
                hash = (53 * hash) + hashEnum((EnumLite) value);
            }
        }
        return hash;
    }

    @Override
    UninitializedMessageException newUninitializedMessageException() {
        return Builder.newUninitializedMessageException(this);
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<BuilderType extends Builder>
            extends AbstractMessageLite.Builder<BuilderType>
            implements Message.Builder {

        private static void addRepeatedField(
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions,
                FieldDescriptor field,
                Object value) {
            if (builder != null) {
                builder.addRepeatedField(field, value);
            } else {
                extensions.addRepeatedField(field, value);
            }
        }

        private static void setField(
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions,
                FieldDescriptor field,
                Object value) {
            if (builder != null) {
                builder.setField(field, value);
            } else {
                extensions.setField(field, value);
            }
        }

        private static boolean hasOriginalMessage(
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions,
                FieldDescriptor field) {
            if (builder != null) {
                return builder.hasField(field);
            } else {
                return extensions.hasField(field);
            }
        }

        private static Message getOriginalMessage(
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions,
                FieldDescriptor field) {
            if (builder != null) {
                return (Message) builder.getField(field);
            } else {
                return (Message) extensions.getField(field);
            }
        }

        private static void mergeOriginalMessage(
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions,
                FieldDescriptor field,
                Message.Builder subBuilder) {
            Message originalMessage = getOriginalMessage(builder, extensions, field);
            if (originalMessage != null) {
                subBuilder.mergeFrom(originalMessage);
            }
        }

        static boolean mergeFieldFrom(
                CodedInputStream input,
                UnknownFieldSet.Builder unknownFields,
                ExtensionRegistryLite extensionRegistry,
                Descriptor type,
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions,
                int tag) throws IOException {
            if (type.getOptions().getMessageSetWireFormat() &&
                    tag == WireFormat.MESSAGE_SET_ITEM_TAG) {
                mergeMessageSetExtensionFromCodedStream(
                        input, unknownFields, extensionRegistry, type, builder, extensions);
                return true;
            }

            final int wireType = WireFormat.getTagWireType(tag);
            final int fieldNumber = WireFormat.getTagFieldNumber(tag);

            final FieldDescriptor field;
            Message defaultInstance = null;

            if (type.isExtensionNumber(fieldNumber)) {

                if (extensionRegistry instanceof ExtensionRegistry) {
                    final ExtensionRegistry.ExtensionInfo extension =
                            ((ExtensionRegistry) extensionRegistry)
                                    .findExtensionByNumber(type, fieldNumber);
                    if (extension == null) {
                        field = null;
                    } else {
                        field = extension.descriptor;
                        defaultInstance = extension.defaultInstance;
                        if (defaultInstance == null &&
                                field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
                            throw new IllegalStateException(
                                    "Message-typed extension lacked default instance: " +
                                            field.getFullName());
                        }
                    }
                } else {
                    field = null;
                }
            } else if (builder != null) {
                field = type.findFieldByNumber(fieldNumber);
            } else {
                field = null;
            }

            boolean unknown = false;
            boolean packed = false;
            if (field == null) {
                unknown = true;
            } else if (wireType == FieldSet.getWireFormatForFieldType(
                    field.getLiteType(),
                    false)) {
                packed = false;
            } else if (field.isPackable() &&
                    wireType == FieldSet.getWireFormatForFieldType(
                            field.getLiteType(),
                            true)) {
                packed = true;
            } else {
                unknown = true;
            }

            if (unknown) {
                return unknownFields.mergeFieldFrom(tag, input);
            }

            if (packed) {
                final int length = input.readRawVarint32();
                final int limit = input.pushLimit(length);
                if (field.getLiteType() == WireFormat.FieldType.ENUM) {
                    while (input.getBytesUntilLimit() > 0) {
                        final int rawValue = input.readEnum();
                        final Object value = field.getEnumType().findValueByNumber(rawValue);
                        if (value == null) {

                            return true;
                        }
                        addRepeatedField(builder, extensions, field, value);
                    }
                } else {
                    while (input.getBytesUntilLimit() > 0) {
                        final Object value =
                                FieldSet.readPrimitiveField(input, field.getLiteType());
                        addRepeatedField(builder, extensions, field, value);
                    }
                }
                input.popLimit(limit);
            } else {
                final Object value;
                switch (field.getType()) {
                    case GROUP: {
                        final Message.Builder subBuilder;
                        if (defaultInstance != null) {
                            subBuilder = defaultInstance.newBuilderForType();
                        } else {
                            subBuilder = builder.newBuilderForField(field);
                        }
                        if (!field.isRepeated()) {
                            mergeOriginalMessage(builder, extensions, field, subBuilder);
                        }
                        input.readGroup(field.getNumber(), subBuilder, extensionRegistry);
                        value = subBuilder.buildPartial();
                        break;
                    }
                    case MESSAGE: {
                        final Message.Builder subBuilder;
                        if (defaultInstance != null) {
                            subBuilder = defaultInstance.newBuilderForType();
                        } else {
                            subBuilder = builder.newBuilderForField(field);
                        }
                        if (!field.isRepeated()) {
                            mergeOriginalMessage(builder, extensions, field, subBuilder);
                        }
                        input.readMessage(subBuilder, extensionRegistry);
                        value = subBuilder.buildPartial();
                        break;
                    }
                    case ENUM:
                        final int rawValue = input.readEnum();
                        value = field.getEnumType().findValueByNumber(rawValue);

                        if (value == null) {
                            unknownFields.mergeVarintField(fieldNumber, rawValue);
                            return true;
                        }
                        break;
                    default:
                        value = FieldSet.readPrimitiveField(input, field.getLiteType());
                        break;
                }

                if (field.isRepeated()) {
                    addRepeatedField(builder, extensions, field, value);
                } else {
                    setField(builder, extensions, field, value);
                }
            }

            return true;
        }

        private static void mergeMessageSetExtensionFromCodedStream(
                CodedInputStream input,
                UnknownFieldSet.Builder unknownFields,
                ExtensionRegistryLite extensionRegistry,
                Descriptor type,
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions) throws IOException {

            int typeId = 0;
            ByteString rawBytes = null;
            ExtensionRegistry.ExtensionInfo extension = null;

            while (true) {
                final int tag = input.readTag();
                if (tag == 0) {
                    break;
                }

                if (tag == WireFormat.MESSAGE_SET_TYPE_ID_TAG) {
                    typeId = input.readUInt32();
                    if (typeId != 0) {

                        if (extensionRegistry instanceof ExtensionRegistry) {
                            extension = ((ExtensionRegistry) extensionRegistry)
                                    .findExtensionByNumber(type, typeId);
                        }
                    }

                } else if (tag == WireFormat.MESSAGE_SET_MESSAGE_TAG) {
                    if (typeId != 0) {
                        if (extension != null && ExtensionRegistryLite.isEagerlyParseMessageSets()) {

                            eagerlyMergeMessageSetExtension(
                                    input, extension, extensionRegistry, builder, extensions);
                            rawBytes = null;
                            continue;
                        }
                    }

                    rawBytes = input.readBytes();

                } else {
                    if (!input.skipField(tag)) {
                        break;
                    }
                }
            }
            input.checkLastTagWas(WireFormat.MESSAGE_SET_ITEM_END_TAG);

            if (rawBytes != null && typeId != 0) {
                if (extension != null) {
                    mergeMessageSetExtensionFromBytes(
                            rawBytes, extension, extensionRegistry, builder, extensions);
                } else {
                    if (rawBytes != null) {
                        unknownFields.mergeField(typeId, UnknownFieldSet.Field.newBuilder()
                                .addLengthDelimited(rawBytes).build());
                    }
                }
            }
        }

        private static void eagerlyMergeMessageSetExtension(
                CodedInputStream input,
                ExtensionRegistry.ExtensionInfo extension,
                ExtensionRegistryLite extensionRegistry,
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions) throws IOException {

            FieldDescriptor field = extension.descriptor;
            Message value = null;
            if (hasOriginalMessage(builder, extensions, field)) {
                Message originalMessage =
                        getOriginalMessage(builder, extensions, field);
                Message.Builder subBuilder = originalMessage.toBuilder();
                input.readMessage(subBuilder, extensionRegistry);
                value = subBuilder.buildPartial();
            } else {
                value = input.readMessage(extension.defaultInstance.getParserForType(),
                        extensionRegistry);
            }

            if (builder != null) {
                builder.setField(field, value);
            } else {
                extensions.setField(field, value);
            }
        }

        private static void mergeMessageSetExtensionFromBytes(
                ByteString rawBytes,
                ExtensionRegistry.ExtensionInfo extension,
                ExtensionRegistryLite extensionRegistry,
                Message.Builder builder,
                FieldSet<FieldDescriptor> extensions) throws IOException {

            FieldDescriptor field = extension.descriptor;
            boolean hasOriginalValue = hasOriginalMessage(builder, extensions, field);

            if (hasOriginalValue || ExtensionRegistryLite.isEagerlyParseMessageSets()) {

                Message value = null;
                if (hasOriginalValue) {
                    Message originalMessage =
                            getOriginalMessage(builder, extensions, field);
                    Message.Builder subBuilder = originalMessage.toBuilder();
                    subBuilder.mergeFrom(rawBytes, extensionRegistry);
                    value = subBuilder.buildPartial();
                } else {
                    value = extension.defaultInstance.getParserForType()
                            .parsePartialFrom(rawBytes, extensionRegistry);
                }
                setField(builder, extensions, field, value);
            } else {

                LazyField lazyField = new LazyField(
                        extension.defaultInstance, extensionRegistry, rawBytes);
                if (builder != null) {

                    if (builder instanceof ExtendableBuilder) {
                        builder.setField(field, lazyField);
                    } else {
                        builder.setField(field, lazyField.getValue());
                    }
                } else {
                    extensions.setField(field, lazyField);
                }
            }
        }

        protected static UninitializedMessageException
        newUninitializedMessageException(Message message) {
            return new UninitializedMessageException(findMissingFields(message));
        }

        private static List<String> findMissingFields(
                final MessageOrBuilder message) {
            final List<String> results = new ArrayList<String>();
            findMissingFields(message, "", results);
            return results;
        }

        private static void findMissingFields(final MessageOrBuilder message,
                                              final String prefix,
                                              final List<String> results) {
            for (final FieldDescriptor field :
                    message.getDescriptorForType().getFields()) {
                if (field.isRequired() && !message.hasField(field)) {
                    results.add(prefix + field.getName());
                }
            }

            for (final Map.Entry<FieldDescriptor, Object> entry :
                    message.getAllFields().entrySet()) {
                final FieldDescriptor field = entry.getKey();
                final Object value = entry.getValue();

                if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
                    if (field.isRepeated()) {
                        int i = 0;
                        for (final Object element : (List) value) {
                            findMissingFields((MessageOrBuilder) element,
                                    subMessagePrefix(prefix, field, i++),
                                    results);
                        }
                    } else {
                        if (message.hasField(field)) {
                            findMissingFields((MessageOrBuilder) value,
                                    subMessagePrefix(prefix, field, -1),
                                    results);
                        }
                    }
                }
            }
        }

        private static String subMessagePrefix(final String prefix,
                                               final FieldDescriptor field,
                                               final int index) {
            final StringBuilder result = new StringBuilder(prefix);
            if (field.isExtension()) {
                result.append('(')
                        .append(field.getFullName())
                        .append(')');
            } else {
                result.append(field.getName());
            }
            if (index != -1) {
                result.append('[')
                        .append(index)
                        .append(']');
            }
            result.append('.');
            return result.toString();
        }

        @Override
        public abstract BuilderType clone();

        public BuilderType clear() {
            for (final Map.Entry<FieldDescriptor, Object> entry :
                    getAllFields().entrySet()) {
                clearField(entry.getKey());
            }
            return (BuilderType) this;
        }

        public List<String> findInitializationErrors() {
            return findMissingFields(this);
        }

        public String getInitializationErrorString() {
            return delimitWithCommas(findInitializationErrors());
        }

        public BuilderType mergeFrom(final Message other) {
            if (other.getDescriptorForType() != getDescriptorForType()) {
                throw new IllegalArgumentException(
                        "mergeFrom(Message) can only merge messages of the same type.");
            }

            for (final Map.Entry<FieldDescriptor, Object> entry :
                    other.getAllFields().entrySet()) {
                final FieldDescriptor field = entry.getKey();
                if (field.isRepeated()) {
                    for (final Object element : (List) entry.getValue()) {
                        addRepeatedField(field, element);
                    }
                } else if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
                    final Message existingValue = (Message) getField(field);
                    if (existingValue == existingValue.getDefaultInstanceForType()) {
                        setField(field, entry.getValue());
                    } else {
                        setField(field,
                                existingValue.newBuilderForType()
                                        .mergeFrom(existingValue)
                                        .mergeFrom((Message) entry.getValue())
                                        .build());
                    }
                } else {
                    setField(field, entry.getValue());
                }
            }

            mergeUnknownFields(other.getUnknownFields());

            return (BuilderType) this;
        }

        @Override
        public BuilderType mergeFrom(final CodedInputStream input)
                throws IOException {
            return mergeFrom(input, ExtensionRegistry.getEmptyRegistry());
        }

        @Override
        public BuilderType mergeFrom(
                final CodedInputStream input,
                final ExtensionRegistryLite extensionRegistry)
                throws IOException {
            final UnknownFieldSet.Builder unknownFields =
                    UnknownFieldSet.newBuilder(getUnknownFields());
            while (true) {
                final int tag = input.readTag();
                if (tag == 0) {
                    break;
                }

                if (!mergeFieldFrom(input, unknownFields, extensionRegistry,
                        getDescriptorForType(), this, null, tag)) {

                    break;
                }
            }
            setUnknownFields(unknownFields.build());
            return (BuilderType) this;
        }

        public BuilderType mergeUnknownFields(final UnknownFieldSet unknownFields) {
            setUnknownFields(
                    UnknownFieldSet.newBuilder(getUnknownFields())
                            .mergeFrom(unknownFields)
                            .build());
            return (BuilderType) this;
        }

        public Message.Builder getFieldBuilder(final FieldDescriptor field) {
            throw new UnsupportedOperationException(
                    "getFieldBuilder() called on an unsupported message type.");
        }

        @Override
        public BuilderType mergeFrom(final ByteString data)
                throws InvalidProtocolBufferException {
            return super.mergeFrom(data);
        }

        @Override
        public BuilderType mergeFrom(
                final ByteString data,
                final ExtensionRegistryLite extensionRegistry)
                throws InvalidProtocolBufferException {
            return super.mergeFrom(data, extensionRegistry);
        }

        @Override
        public BuilderType mergeFrom(final byte[] data)
                throws InvalidProtocolBufferException {
            return super.mergeFrom(data);
        }

        @Override
        public BuilderType mergeFrom(
                final byte[] data, final int off, final int len)
                throws InvalidProtocolBufferException {
            return super.mergeFrom(data, off, len);
        }

        @Override
        public BuilderType mergeFrom(
                final byte[] data,
                final ExtensionRegistryLite extensionRegistry)
                throws InvalidProtocolBufferException {
            return super.mergeFrom(data, extensionRegistry);
        }

        @Override
        public BuilderType mergeFrom(
                final byte[] data, final int off, final int len,
                final ExtensionRegistryLite extensionRegistry)
                throws InvalidProtocolBufferException {
            return super.mergeFrom(data, off, len, extensionRegistry);
        }

        @Override
        public BuilderType mergeFrom(final InputStream input)
                throws IOException {
            return super.mergeFrom(input);
        }

        @Override
        public BuilderType mergeFrom(
                final InputStream input,
                final ExtensionRegistryLite extensionRegistry)
                throws IOException {
            return super.mergeFrom(input, extensionRegistry);
        }

        @Override
        public boolean mergeDelimitedFrom(final InputStream input)
                throws IOException {
            return super.mergeDelimitedFrom(input);
        }

        @Override
        public boolean mergeDelimitedFrom(
                final InputStream input,
                final ExtensionRegistryLite extensionRegistry)
                throws IOException {
            return super.mergeDelimitedFrom(input, extensionRegistry);
        }

    }
}
