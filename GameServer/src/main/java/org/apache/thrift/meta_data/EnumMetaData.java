package org.apache.thrift.meta_data;

import org.apache.thrift.TEnum;

public class EnumMetaData extends FieldValueMetaData {
    public final Class<? extends TEnum> enumClass;

    public EnumMetaData(byte type, Class<? extends TEnum> sClass) {
        super(type);
        this.enumClass = sClass;
    }
}
