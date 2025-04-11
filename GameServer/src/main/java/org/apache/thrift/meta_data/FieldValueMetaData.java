package org.apache.thrift.meta_data;

import org.apache.thrift.protocol.TType;

public class FieldValueMetaData implements java.io.Serializable {
    public final byte type;

    private final boolean isTypedefType;
    private final String typedefName;

    public FieldValueMetaData(byte type) {
        this.type = type;
        this.isTypedefType = false;
        this.typedefName = null;
    }

    public FieldValueMetaData(byte type, String typedefName) {
        this.type = type;
        this.isTypedefType = true;
        this.typedefName = typedefName;
    }

    public boolean isTypedef() {
        return isTypedefType;
    }

    public String getTypedefName() {
        return typedefName;
    }

    public boolean isStruct() {
        return type == TType.STRUCT;
    }

    public boolean isContainer() {
        return type == TType.LIST || type == TType.MAP || type == TType.SET;
    }
}
