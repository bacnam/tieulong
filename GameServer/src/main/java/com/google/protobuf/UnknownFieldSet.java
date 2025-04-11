package com.google.protobuf;

import com.google.protobuf.AbstractMessageLite.Builder.LimitedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public final class UnknownFieldSet implements MessageLite {
    private static final UnknownFieldSet defaultInstance =
            new UnknownFieldSet(Collections.<Integer, Field>emptyMap());
    private static final Parser PARSER = new Parser();
    private Map<Integer, Field> fields;

    private UnknownFieldSet() {
    }

    private UnknownFieldSet(final Map<Integer, Field> fields) {
        this.fields = fields;
    }

    public static Builder newBuilder() {
        return Builder.create();
    }

    public static Builder newBuilder(final UnknownFieldSet copyFrom) {
        return newBuilder().mergeFrom(copyFrom);
    }

    public static UnknownFieldSet getDefaultInstance() {
        return defaultInstance;
    }

    public static UnknownFieldSet parseFrom(final CodedInputStream input)
            throws IOException {
        return newBuilder().mergeFrom(input).build();
    }

    public static UnknownFieldSet parseFrom(final ByteString data)
            throws InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).build();
    }

    public static UnknownFieldSet parseFrom(final byte[] data)
            throws InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).build();
    }

    public static UnknownFieldSet parseFrom(final InputStream input)
            throws IOException {
        return newBuilder().mergeFrom(input).build();
    }

    public UnknownFieldSet getDefaultInstanceForType() {
        return defaultInstance;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof UnknownFieldSet) &&
                fields.equals(((UnknownFieldSet) other).fields);
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    public Map<Integer, Field> asMap() {
        return fields;
    }

    public boolean hasField(final int number) {
        return fields.containsKey(number);
    }

    public Field getField(final int number) {
        final Field result = fields.get(number);
        return (result == null) ? Field.getDefaultInstance() : result;
    }

    public void writeTo(final CodedOutputStream output) throws IOException {
        for (final Map.Entry<Integer, Field> entry : fields.entrySet()) {
            entry.getValue().writeTo(entry.getKey(), output);
        }
    }

    @Override
    public String toString() {
        return TextFormat.printToString(this);
    }

    public ByteString toByteString() {
        try {
            final ByteString.CodedBuilder out =
                    ByteString.newCodedBuilder(getSerializedSize());
            writeTo(out.getCodedOutput());
            return out.build();
        } catch (final IOException e) {
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
        } catch (final IOException e) {
            throw new RuntimeException(
                    "Serializing to a byte array threw an IOException " +
                            "(should never happen).", e);
        }
    }

    public void writeTo(final OutputStream output) throws IOException {
        final CodedOutputStream codedOutput = CodedOutputStream.newInstance(output);
        writeTo(codedOutput);
        codedOutput.flush();
    }

    public void writeDelimitedTo(OutputStream output) throws IOException {
        final CodedOutputStream codedOutput = CodedOutputStream.newInstance(output);
        codedOutput.writeRawVarint32(getSerializedSize());
        writeTo(codedOutput);
        codedOutput.flush();
    }

    public int getSerializedSize() {
        int result = 0;
        for (final Map.Entry<Integer, Field> entry : fields.entrySet()) {
            result += entry.getValue().getSerializedSize(entry.getKey());
        }
        return result;
    }

    public void writeAsMessageSetTo(final CodedOutputStream output)
            throws IOException {
        for (final Map.Entry<Integer, Field> entry : fields.entrySet()) {
            entry.getValue().writeAsMessageSetExtensionTo(
                    entry.getKey(), output);
        }
    }

    public int getSerializedSizeAsMessageSet() {
        int result = 0;
        for (final Map.Entry<Integer, Field> entry : fields.entrySet()) {
            result += entry.getValue().getSerializedSizeAsMessageSetExtension(
                    entry.getKey());
        }
        return result;
    }

    public boolean isInitialized() {

        return true;
    }

    public Builder newBuilderForType() {
        return newBuilder();
    }

    public Builder toBuilder() {
        return newBuilder().mergeFrom(this);
    }

    public final Parser getParserForType() {
        return PARSER;
    }

    public static final class Builder implements MessageLite.Builder {

        private Map<Integer, Field> fields;
        private int lastFieldNumber;
        private Field.Builder lastField;
        private Builder() {
        }

        private static Builder create() {
            Builder builder = new Builder();
            builder.reinitialize();
            return builder;
        }

        private Field.Builder getFieldBuilder(final int number) {
            if (lastField != null) {
                if (number == lastFieldNumber) {
                    return lastField;
                }

                addField(lastFieldNumber, lastField.build());
            }
            if (number == 0) {
                return null;
            } else {
                final Field existing = fields.get(number);
                lastFieldNumber = number;
                lastField = Field.newBuilder();
                if (existing != null) {
                    lastField.mergeFrom(existing);
                }
                return lastField;
            }
        }

        public UnknownFieldSet build() {
            getFieldBuilder(0);
            final UnknownFieldSet result;
            if (fields.isEmpty()) {
                result = getDefaultInstance();
            } else {
                result = new UnknownFieldSet(Collections.unmodifiableMap(fields));
            }
            fields = null;
            return result;
        }

        public UnknownFieldSet buildPartial() {

            return build();
        }

        @Override
        public Builder clone() {
            getFieldBuilder(0);
            return UnknownFieldSet.newBuilder().mergeFrom(
                    new UnknownFieldSet(fields));
        }

        public UnknownFieldSet getDefaultInstanceForType() {
            return UnknownFieldSet.getDefaultInstance();
        }

        private void reinitialize() {
            fields = Collections.emptyMap();
            lastFieldNumber = 0;
            lastField = null;
        }

        public Builder clear() {
            reinitialize();
            return this;
        }

        public Builder mergeFrom(final UnknownFieldSet other) {
            if (other != getDefaultInstance()) {
                for (final Map.Entry<Integer, Field> entry : other.fields.entrySet()) {
                    mergeField(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Builder mergeField(final int number, final Field field) {
            if (number == 0) {
                throw new IllegalArgumentException("Zero is not a valid field number.");
            }
            if (hasField(number)) {
                getFieldBuilder(number).mergeFrom(field);
            } else {

                addField(number, field);
            }
            return this;
        }

        public Builder mergeVarintField(final int number, final int value) {
            if (number == 0) {
                throw new IllegalArgumentException("Zero is not a valid field number.");
            }
            getFieldBuilder(number).addVarint(value);
            return this;
        }

        public boolean hasField(final int number) {
            if (number == 0) {
                throw new IllegalArgumentException("Zero is not a valid field number.");
            }
            return number == lastFieldNumber || fields.containsKey(number);
        }

        public Builder addField(final int number, final Field field) {
            if (number == 0) {
                throw new IllegalArgumentException("Zero is not a valid field number.");
            }
            if (lastField != null && lastFieldNumber == number) {

                lastField = null;
                lastFieldNumber = 0;
            }
            if (fields.isEmpty()) {
                fields = new TreeMap<Integer, Field>();
            }
            fields.put(number, field);
            return this;
        }

        public Map<Integer, Field> asMap() {
            getFieldBuilder(0);
            return Collections.unmodifiableMap(fields);
        }

        public Builder mergeFrom(final CodedInputStream input) throws IOException {
            while (true) {
                final int tag = input.readTag();
                if (tag == 0 || !mergeFieldFrom(tag, input)) {
                    break;
                }
            }
            return this;
        }

        public boolean mergeFieldFrom(final int tag, final CodedInputStream input)
                throws IOException {
            final int number = WireFormat.getTagFieldNumber(tag);
            switch (WireFormat.getTagWireType(tag)) {
                case WireFormat.WIRETYPE_VARINT:
                    getFieldBuilder(number).addVarint(input.readInt64());
                    return true;
                case WireFormat.WIRETYPE_FIXED64:
                    getFieldBuilder(number).addFixed64(input.readFixed64());
                    return true;
                case WireFormat.WIRETYPE_LENGTH_DELIMITED:
                    getFieldBuilder(number).addLengthDelimited(input.readBytes());
                    return true;
                case WireFormat.WIRETYPE_START_GROUP:
                    final Builder subBuilder = newBuilder();
                    input.readGroup(number, subBuilder,
                            ExtensionRegistry.getEmptyRegistry());
                    getFieldBuilder(number).addGroup(subBuilder.build());
                    return true;
                case WireFormat.WIRETYPE_END_GROUP:
                    return false;
                case WireFormat.WIRETYPE_FIXED32:
                    getFieldBuilder(number).addFixed32(input.readFixed32());
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        public Builder mergeFrom(final ByteString data)
                throws InvalidProtocolBufferException {
            try {
                final CodedInputStream input = data.newCodedInput();
                mergeFrom(input);
                input.checkLastTagWas(0);
                return this;
            } catch (final InvalidProtocolBufferException e) {
                throw e;
            } catch (final IOException e) {
                throw new RuntimeException(
                        "Reading from a ByteString threw an IOException (should " +
                                "never happen).", e);
            }
        }

        public Builder mergeFrom(final byte[] data)
                throws InvalidProtocolBufferException {
            try {
                final CodedInputStream input = CodedInputStream.newInstance(data);
                mergeFrom(input);
                input.checkLastTagWas(0);
                return this;
            } catch (final InvalidProtocolBufferException e) {
                throw e;
            } catch (final IOException e) {
                throw new RuntimeException(
                        "Reading from a byte array threw an IOException (should " +
                                "never happen).", e);
            }
        }

        public Builder mergeFrom(final InputStream input) throws IOException {
            final CodedInputStream codedInput = CodedInputStream.newInstance(input);
            mergeFrom(codedInput);
            codedInput.checkLastTagWas(0);
            return this;
        }

        public boolean mergeDelimitedFrom(InputStream input)
                throws IOException {
            final int firstByte = input.read();
            if (firstByte == -1) {
                return false;
            }
            final int size = CodedInputStream.readRawVarint32(firstByte, input);
            final InputStream limitedInput = new LimitedInputStream(input, size);
            mergeFrom(limitedInput);
            return true;
        }

        public boolean mergeDelimitedFrom(
                InputStream input,
                ExtensionRegistryLite extensionRegistry) throws IOException {

            return mergeDelimitedFrom(input);
        }

        public Builder mergeFrom(
                CodedInputStream input,
                ExtensionRegistryLite extensionRegistry) throws IOException {

            return mergeFrom(input);
        }

        public Builder mergeFrom(
                ByteString data,
                ExtensionRegistryLite extensionRegistry)
                throws InvalidProtocolBufferException {

            return mergeFrom(data);
        }

        public Builder mergeFrom(byte[] data, int off, int len)
                throws InvalidProtocolBufferException {
            try {
                final CodedInputStream input =
                        CodedInputStream.newInstance(data, off, len);
                mergeFrom(input);
                input.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (IOException e) {
                throw new RuntimeException(
                        "Reading from a byte array threw an IOException (should " +
                                "never happen).", e);
            }
        }

        public Builder mergeFrom(
                byte[] data,
                ExtensionRegistryLite extensionRegistry)
                throws InvalidProtocolBufferException {

            return mergeFrom(data);
        }

        public Builder mergeFrom(
                byte[] data, int off, int len,
                ExtensionRegistryLite extensionRegistry)
                throws InvalidProtocolBufferException {

            return mergeFrom(data, off, len);
        }

        public Builder mergeFrom(
                InputStream input,
                ExtensionRegistryLite extensionRegistry) throws IOException {

            return mergeFrom(input);
        }

        public boolean isInitialized() {

            return true;
        }
    }

    public static final class Field {
        private static final Field fieldDefaultInstance = newBuilder().build();
        private List<Long> varint;
        private List<Integer> fixed32;
        private List<Long> fixed64;
        private List<ByteString> lengthDelimited;
        private List<UnknownFieldSet> group;

        private Field() {
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(final Field copyFrom) {
            return newBuilder().mergeFrom(copyFrom);
        }

        public static Field getDefaultInstance() {
            return fieldDefaultInstance;
        }

        public List<Long> getVarintList() {
            return varint;
        }

        public List<Integer> getFixed32List() {
            return fixed32;
        }

        public List<Long> getFixed64List() {
            return fixed64;
        }

        public List<ByteString> getLengthDelimitedList() {
            return lengthDelimited;
        }

        public List<UnknownFieldSet> getGroupList() {
            return group;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Field)) {
                return false;
            }
            return Arrays.equals(getIdentityArray(),
                    ((Field) other).getIdentityArray());
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(getIdentityArray());
        }

        private Object[] getIdentityArray() {
            return new Object[]{
                    varint,
                    fixed32,
                    fixed64,
                    lengthDelimited,
                    group};
        }

        public void writeTo(final int fieldNumber, final CodedOutputStream output)
                throws IOException {
            for (final long value : varint) {
                output.writeUInt64(fieldNumber, value);
            }
            for (final int value : fixed32) {
                output.writeFixed32(fieldNumber, value);
            }
            for (final long value : fixed64) {
                output.writeFixed64(fieldNumber, value);
            }
            for (final ByteString value : lengthDelimited) {
                output.writeBytes(fieldNumber, value);
            }
            for (final UnknownFieldSet value : group) {
                output.writeGroup(fieldNumber, value);
            }
        }

        public int getSerializedSize(final int fieldNumber) {
            int result = 0;
            for (final long value : varint) {
                result += CodedOutputStream.computeUInt64Size(fieldNumber, value);
            }
            for (final int value : fixed32) {
                result += CodedOutputStream.computeFixed32Size(fieldNumber, value);
            }
            for (final long value : fixed64) {
                result += CodedOutputStream.computeFixed64Size(fieldNumber, value);
            }
            for (final ByteString value : lengthDelimited) {
                result += CodedOutputStream.computeBytesSize(fieldNumber, value);
            }
            for (final UnknownFieldSet value : group) {
                result += CodedOutputStream.computeGroupSize(fieldNumber, value);
            }
            return result;
        }

        public void writeAsMessageSetExtensionTo(
                final int fieldNumber,
                final CodedOutputStream output)
                throws IOException {
            for (final ByteString value : lengthDelimited) {
                output.writeRawMessageSetExtension(fieldNumber, value);
            }
        }

        public int getSerializedSizeAsMessageSetExtension(final int fieldNumber) {
            int result = 0;
            for (final ByteString value : lengthDelimited) {
                result += CodedOutputStream.computeRawMessageSetExtensionSize(
                        fieldNumber, value);
            }
            return result;
        }

        public static final class Builder {

            private Field result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new Field();
                return builder;
            }

            public Field build() {
                if (result.varint == null) {
                    result.varint = Collections.emptyList();
                } else {
                    result.varint = Collections.unmodifiableList(result.varint);
                }
                if (result.fixed32 == null) {
                    result.fixed32 = Collections.emptyList();
                } else {
                    result.fixed32 = Collections.unmodifiableList(result.fixed32);
                }
                if (result.fixed64 == null) {
                    result.fixed64 = Collections.emptyList();
                } else {
                    result.fixed64 = Collections.unmodifiableList(result.fixed64);
                }
                if (result.lengthDelimited == null) {
                    result.lengthDelimited = Collections.emptyList();
                } else {
                    result.lengthDelimited =
                            Collections.unmodifiableList(result.lengthDelimited);
                }
                if (result.group == null) {
                    result.group = Collections.emptyList();
                } else {
                    result.group = Collections.unmodifiableList(result.group);
                }

                final Field returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder clear() {
                result = new Field();
                return this;
            }

            public Builder mergeFrom(final Field other) {
                if (!other.varint.isEmpty()) {
                    if (result.varint == null) {
                        result.varint = new ArrayList<Long>();
                    }
                    result.varint.addAll(other.varint);
                }
                if (!other.fixed32.isEmpty()) {
                    if (result.fixed32 == null) {
                        result.fixed32 = new ArrayList<Integer>();
                    }
                    result.fixed32.addAll(other.fixed32);
                }
                if (!other.fixed64.isEmpty()) {
                    if (result.fixed64 == null) {
                        result.fixed64 = new ArrayList<Long>();
                    }
                    result.fixed64.addAll(other.fixed64);
                }
                if (!other.lengthDelimited.isEmpty()) {
                    if (result.lengthDelimited == null) {
                        result.lengthDelimited = new ArrayList<ByteString>();
                    }
                    result.lengthDelimited.addAll(other.lengthDelimited);
                }
                if (!other.group.isEmpty()) {
                    if (result.group == null) {
                        result.group = new ArrayList<UnknownFieldSet>();
                    }
                    result.group.addAll(other.group);
                }
                return this;
            }

            public Builder addVarint(final long value) {
                if (result.varint == null) {
                    result.varint = new ArrayList<Long>();
                }
                result.varint.add(value);
                return this;
            }

            public Builder addFixed32(final int value) {
                if (result.fixed32 == null) {
                    result.fixed32 = new ArrayList<Integer>();
                }
                result.fixed32.add(value);
                return this;
            }

            public Builder addFixed64(final long value) {
                if (result.fixed64 == null) {
                    result.fixed64 = new ArrayList<Long>();
                }
                result.fixed64.add(value);
                return this;
            }

            public Builder addLengthDelimited(final ByteString value) {
                if (result.lengthDelimited == null) {
                    result.lengthDelimited = new ArrayList<ByteString>();
                }
                result.lengthDelimited.add(value);
                return this;
            }

            public Builder addGroup(final UnknownFieldSet value) {
                if (result.group == null) {
                    result.group = new ArrayList<UnknownFieldSet>();
                }
                result.group.add(value);
                return this;
            }
        }
    }

    public static final class Parser extends AbstractParser<UnknownFieldSet> {
        public UnknownFieldSet parsePartialFrom(
                CodedInputStream input, ExtensionRegistryLite extensionRegistry)
                throws InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
                builder.mergeFrom(input);
            } catch (InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (IOException e) {
                throw new InvalidProtocolBufferException(e.getMessage())
                        .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
        }
    }
}
