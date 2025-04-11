package org.apache.thrift;

import org.apache.thrift.protocol.TProtocol;

import java.io.Serializable;

public interface TBase<T extends TBase, F extends TFieldIdEnum> extends Comparable<T>, Serializable {

    public void read(TProtocol iprot) throws TException;

    public void write(TProtocol oprot) throws TException;

    public F fieldForId(int fieldId);

    public boolean isSet(F field);

    public Object getFieldValue(F field);

    public void setFieldValue(F field, Object value);

    public TBase<T, F> deepCopy();

    public void clear();
}
