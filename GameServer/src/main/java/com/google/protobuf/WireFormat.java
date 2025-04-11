package com.google.protobuf;

public final class WireFormat {

    public static final int WIRETYPE_VARINT = 0;
    public static final int WIRETYPE_FIXED64 = 1;
    public static final int WIRETYPE_LENGTH_DELIMITED = 2;
    public static final int WIRETYPE_START_GROUP = 3;
    public static final int WIRETYPE_END_GROUP = 4;
    public static final int WIRETYPE_FIXED32 = 5;
    static final int TAG_TYPE_BITS = 3;
    static final int TAG_TYPE_MASK = (1 << TAG_TYPE_BITS) - 1;
    static final int MESSAGE_SET_ITEM = 1;
    static final int MESSAGE_SET_TYPE_ID = 2;
    static final int MESSAGE_SET_MESSAGE = 3;
    static final int MESSAGE_SET_ITEM_TAG =
            makeTag(MESSAGE_SET_ITEM, WIRETYPE_START_GROUP);
    static final int MESSAGE_SET_ITEM_END_TAG =
            makeTag(MESSAGE_SET_ITEM, WIRETYPE_END_GROUP);
    static final int MESSAGE_SET_TYPE_ID_TAG =
            makeTag(MESSAGE_SET_TYPE_ID, WIRETYPE_VARINT);
    static final int MESSAGE_SET_MESSAGE_TAG =
            makeTag(MESSAGE_SET_MESSAGE, WIRETYPE_LENGTH_DELIMITED);
    private WireFormat() {
    }

    static int getTagWireType(final int tag) {
        return tag & TAG_TYPE_MASK;
    }

    public static int getTagFieldNumber(final int tag) {
        return tag >>> TAG_TYPE_BITS;
    }

    static int makeTag(final int fieldNumber, final int wireType) {
        return (fieldNumber << TAG_TYPE_BITS) | wireType;
    }
    public enum JavaType {
        INT(0),
        LONG(0L),
        FLOAT(0F),
        DOUBLE(0D),
        BOOLEAN(false),
        STRING(""),
        BYTE_STRING(ByteString.EMPTY),
        ENUM(null),
        MESSAGE(null);

        private final Object defaultDefault;

        JavaType(final Object defaultDefault) {
            this.defaultDefault = defaultDefault;
        }

        Object getDefaultDefault() {
            return defaultDefault;
        }
    }
    public enum FieldType {
        DOUBLE(JavaType.DOUBLE, WIRETYPE_FIXED64),
        FLOAT(JavaType.FLOAT, WIRETYPE_FIXED32),
        INT64(JavaType.LONG, WIRETYPE_VARINT),
        UINT64(JavaType.LONG, WIRETYPE_VARINT),
        INT32(JavaType.INT, WIRETYPE_VARINT),
        FIXED64(JavaType.LONG, WIRETYPE_FIXED64),
        FIXED32(JavaType.INT, WIRETYPE_FIXED32),
        BOOL(JavaType.BOOLEAN, WIRETYPE_VARINT),
        STRING(JavaType.STRING, WIRETYPE_LENGTH_DELIMITED) {
            public boolean isPackable() {
                return false;
            }
        },
        GROUP(JavaType.MESSAGE, WIRETYPE_START_GROUP) {
            public boolean isPackable() {
                return false;
            }
        },
        MESSAGE(JavaType.MESSAGE, WIRETYPE_LENGTH_DELIMITED) {
            public boolean isPackable() {
                return false;
            }
        },
        BYTES(JavaType.BYTE_STRING, WIRETYPE_LENGTH_DELIMITED) {
            public boolean isPackable() {
                return false;
            }
        },
        UINT32(JavaType.INT, WIRETYPE_VARINT),
        ENUM(JavaType.ENUM, WIRETYPE_VARINT),
        SFIXED32(JavaType.INT, WIRETYPE_FIXED32),
        SFIXED64(JavaType.LONG, WIRETYPE_FIXED64),
        SINT32(JavaType.INT, WIRETYPE_VARINT),
        SINT64(JavaType.LONG, WIRETYPE_VARINT);

        private final JavaType javaType;
        private final int wireType;
        FieldType(final JavaType javaType, final int wireType) {
            this.javaType = javaType;
            this.wireType = wireType;
        }

        public JavaType getJavaType() {
            return javaType;
        }

        public int getWireType() {
            return wireType;
        }

        public boolean isPackable() {
            return true;
        }
    }
}
