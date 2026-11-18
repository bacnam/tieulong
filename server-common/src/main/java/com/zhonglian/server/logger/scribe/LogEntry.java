package com.zhonglian.server.logger.scribe;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.thrift.TBase;
import org.apache.thrift.TBaseHelper;
import org.apache.thrift.TException;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.meta_data.FieldMetaData;
import org.apache.thrift.meta_data.FieldValueMetaData;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TStruct;

public class LogEntry implements TBase<LogEntry, LogEntry._Fields>, Serializable, Cloneable {
    private static final long serialVersionUID = 3121543719267680503L;
    private static final TStruct STRUCT_DESC = new TStruct("LogEntry");
    private static final TField CATEGORY_FIELD_DESC = new TField("category", (byte)11, (short)1);
    private static final TField MESSAGE_FIELD_DESC = new TField("message", (byte)11, (short)2);
    public String category;
    public String message;
    public static final Map<LogEntry._Fields, FieldMetaData> metaDataMap;

    static {
        Map<LogEntry._Fields, FieldMetaData> tmpMap = new EnumMap<>(LogEntry._Fields.class);
        tmpMap.put(LogEntry._Fields.CATEGORY, new FieldMetaData("category", (byte)3, new FieldValueMetaData((byte)11)));
        tmpMap.put(LogEntry._Fields.MESSAGE, new FieldMetaData("message", (byte)3, new FieldValueMetaData((byte)11)));
        metaDataMap = Collections.unmodifiableMap(tmpMap);
        FieldMetaData.addStructMetaDataMap(LogEntry.class, metaDataMap);
    }

    public LogEntry() {
    }

    public LogEntry(String category, String message) {
        this();
        this.category = category;
        this.message = message;
    }

    public LogEntry(LogEntry other) {
        if (other.isSetCategory()) {
            this.category = other.category;
        }

        if (other.isSetMessage()) {
            this.message = other.message;
        }
    }

    public LogEntry deepCopy() {
        return new LogEntry(this);
    }

    @Override
    public void clear() {
        this.category = null;
        this.message = null;
    }

    public String getCategory() {
        return this.category;
    }

    public LogEntry setCategory(String category) {
        this.category = category;
        return this;
    }

    public void unsetCategory() {
        this.category = null;
    }

    public boolean isSetCategory() {
        return this.category != null;
    }

    public void setCategoryIsSet(boolean value) {
        if (!value) {
            this.category = null;
        }
    }

    public String getMessage() {
        return this.message;
    }

    public LogEntry setMessage(String message) {
        this.message = message;
        return this;
    }

    public void unsetMessage() {
        this.message = null;
    }

    public boolean isSetMessage() {
        return this.message != null;
    }

    public void setMessageIsSet(boolean value) {
        if (!value) {
            this.message = null;
        }
    }

    public void setFieldValue(LogEntry._Fields field, Object value) {
        switch (field) {
            case CATEGORY:
                if (value == null) {
                    this.unsetCategory();
                } else {
                    this.setCategory((String)value);
                }
                break;
            case MESSAGE:
                if (value == null) {
                    this.unsetMessage();
                } else {
                    this.setMessage((String)value);
                }
        }
    }

    public Object getFieldValue(LogEntry._Fields field) {
        switch (field) {
            case CATEGORY:
                return this.getCategory();
            case MESSAGE:
                return this.getMessage();
            default:
                throw new IllegalStateException();
        }
    }

    public boolean isSet(LogEntry._Fields field) {
        if (field == null) {
            throw new IllegalArgumentException();
        } else {
            switch (field) {
                case CATEGORY:
                    return this.isSetCategory();
                case MESSAGE:
                    return this.isSetMessage();
                default:
                    throw new IllegalStateException();
            }
        }
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        } else {
            return that instanceof LogEntry ? this.equals((LogEntry)that) : false;
        }
    }

    public boolean equals(LogEntry that) {
        if (that == null) {
            return false;
        } else {
            boolean this_present_category = this.isSetCategory();
            boolean that_present_category = that.isSetCategory();
            if (this_present_category || that_present_category) {
                if (!this_present_category || !that_present_category) {
                    return false;
                }

                if (!this.category.equals(that.category)) {
                    return false;
                }
            }

            boolean this_present_message = this.isSetMessage();
            boolean that_present_message = that.isSetMessage();
            if (this_present_message || that_present_message) {
                if (!this_present_message || !that_present_message) {
                    return false;
                }

                if (!this.message.equals(that.message)) {
                    return false;
                }
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public int compareTo(LogEntry other) {
        if (!this.getClass().equals(other.getClass())) {
            return this.getClass().getName().compareTo(other.getClass().getName());
        } else {
            int lastComparison = 0;
            lastComparison = Boolean.valueOf(this.isSetCategory()).compareTo(other.isSetCategory());
            if (lastComparison != 0) {
                return lastComparison;
            } else {
                if (this.isSetCategory()) {
                    lastComparison = TBaseHelper.compareTo(this.category, other.category);
                    if (lastComparison != 0) {
                        return lastComparison;
                    }
                }

                lastComparison = Boolean.valueOf(this.isSetMessage()).compareTo(other.isSetMessage());
                if (lastComparison != 0) {
                    return lastComparison;
                } else {
                    if (this.isSetMessage()) {
                        lastComparison = TBaseHelper.compareTo(this.message, other.message);
                        if (lastComparison != 0) {
                            return lastComparison;
                        }
                    }

                    return 0;
                }
            }
        }
    }

    public LogEntry._Fields fieldForId(int fieldId) {
        return LogEntry._Fields.findByThriftId(fieldId);
    }

    @Override
    public void read(TProtocol iprot) throws TException {
        iprot.readStructBegin();

        while (true) {
            TField field = iprot.readFieldBegin();
            if (field.type == 0) {
                iprot.readStructEnd();
                this.validate();
                return;
            }

            switch (field.id) {
                case 1:
                    if (field.type == 11) {
                        this.category = iprot.readString();
                    } else {
                        TProtocolUtil.skip(iprot, field.type);
                    }
                    break;
                case 2:
                    if (field.type == 11) {
                        this.message = iprot.readString();
                    } else {
                        TProtocolUtil.skip(iprot, field.type);
                    }
                    break;
                default:
                    TProtocolUtil.skip(iprot, field.type);
            }

            iprot.readFieldEnd();
        }
    }

    @Override
    public void write(TProtocol oprot) throws TException {
        this.validate();
        oprot.writeStructBegin(STRUCT_DESC);
        if (this.category != null) {
            oprot.writeFieldBegin(CATEGORY_FIELD_DESC);
            oprot.writeString(this.category);
            oprot.writeFieldEnd();
        }

        if (this.message != null) {
            oprot.writeFieldBegin(MESSAGE_FIELD_DESC);
            oprot.writeString(this.message);
            oprot.writeFieldEnd();
        }

        oprot.writeFieldStop();
        oprot.writeStructEnd();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LogEntry(");
        boolean first = true;
        sb.append("category:");
        if (this.category == null) {
            sb.append("null");
        } else {
            sb.append(this.category);
        }

        first = false;
        if (!first) {
            sb.append(", ");
        }

        sb.append("message:");
        if (this.message == null) {
            sb.append("null");
        } else {
            sb.append(this.message);
        }

        first = false;
        sb.append(")");
        return sb.toString();
    }

    public void validate() throws TException {
    }

    public static enum _Fields implements TFieldIdEnum {
        CATEGORY((short)1, "category"),
        MESSAGE((short)2, "message");

        private static final Map<String, LogEntry._Fields> byName = new ConcurrentHashMap<>();
        private final short _thriftId;
        private final String _fieldName;

        static {
            for (LogEntry._Fields field : EnumSet.allOf(LogEntry._Fields.class)) {
                byName.put(field.getFieldName(), field);
            }
        }

        public static LogEntry._Fields findByThriftId(int fieldId) {
            switch (fieldId) {
                case 1:
                    return CATEGORY;
                case 2:
                    return MESSAGE;
                default:
                    return null;
            }
        }

        public static LogEntry._Fields findByThriftIdOrThrow(int fieldId) {
            LogEntry._Fields fields = findByThriftId(fieldId);
            if (fields == null) {
                throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
            } else {
                return fields;
            }
        }

        public static LogEntry._Fields findByName(String name) {
            return byName.get(name);
        }

        private _Fields(short thriftId, String fieldName) {
            this._thriftId = thriftId;
            this._fieldName = fieldName;
        }

        @Override
        public short getThriftFieldId() {
            return this._thriftId;
        }

        @Override
        public String getFieldName() {
            return this._fieldName;
        }
    }
}
