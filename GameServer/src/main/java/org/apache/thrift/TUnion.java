
package org.apache.thrift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TStruct;

public abstract class TUnion<T extends TUnion, F extends TFieldIdEnum> implements TBase<T, F> {

  protected Object value_;
  protected F setField_;

  protected TUnion() {
    setField_ = null;
    value_ = null;
  }

  protected TUnion(F setField, Object value) {
    setFieldValue(setField, value);
  }

  protected TUnion(TUnion<T, F> other) {
    if (!other.getClass().equals(this.getClass())) {
      throw new ClassCastException();
    }
    setField_ = other.setField_;
    value_ = deepCopyObject(other.value_);
  }

  private static Object deepCopyObject(Object o) {
    if (o instanceof TBase) {
      return ((TBase)o).deepCopy();
    } else if (o instanceof byte[]) {
      byte[] other_val = (byte[])o;
      byte[] this_val = new byte[other_val.length];
      System.arraycopy(other_val, 0, this_val, 0, other_val.length);
      return this_val;
    } else if (o instanceof List) {
      return deepCopyList((List)o);
    } else if (o instanceof Set) {
      return deepCopySet((Set)o);
    } else if (o instanceof Map) {
      return deepCopyMap((Map)o);
    } else {
      return o;
    }
  }

  private static Map deepCopyMap(Map<Object, Object> map) {
    Map copy = new HashMap();
    for (Map.Entry<Object, Object> entry : map.entrySet()) {
      copy.put(deepCopyObject(entry.getKey()), deepCopyObject(entry.getValue()));
    }
    return copy;
  }

  private static Set deepCopySet(Set set) {
    Set copy = new HashSet();
    for (Object o : set) {
      copy.add(deepCopyObject(o));
    }
    return copy;
  }

  private static List deepCopyList(List list) {
    List copy = new ArrayList(list.size());
    for (Object o : list) {
      copy.add(deepCopyObject(o));
    }
    return copy;
  }

  public F getSetField() {
    return setField_;
  }

  public Object getFieldValue() {
    return value_;
  }

  public Object getFieldValue(F fieldId) {
    if (fieldId != setField_) {
      throw new IllegalArgumentException("Cannot get the value of field " + fieldId + " because union's set field is " + setField_);
    }

    return getFieldValue();
  }

  public Object getFieldValue(int fieldId) {
    return getFieldValue(enumForId((short)fieldId));
  }

  public boolean isSet() {
    return setField_ != null;
  }

  public boolean isSet(F fieldId) {
    return setField_ == fieldId;
  }

  public boolean isSet(int fieldId) {
    return isSet(enumForId((short)fieldId));
  }

  public void read(TProtocol iprot) throws TException {
    setField_ = null;
    value_ = null;

    iprot.readStructBegin();

    TField field = iprot.readFieldBegin();

    value_ = readValue(iprot, field);
    if (value_ != null) {
      setField_ = enumForId(field.id);
    }

    iprot.readFieldEnd();

    iprot.readFieldBegin();
    iprot.readStructEnd();
  }

  public void setFieldValue(F fieldId, Object value) {
    checkType(fieldId, value);
    setField_ = fieldId;
    value_ = value;
  }

  public void setFieldValue(int fieldId, Object value) {
    setFieldValue(enumForId((short)fieldId), value);
  }

  public void write(TProtocol oprot) throws TException {
    if (getSetField() == null || getFieldValue() == null) {
      throw new TProtocolException("Cannot write a TUnion with no set value!");
    }
    oprot.writeStructBegin(getStructDesc());
    oprot.writeFieldBegin(getFieldDesc(setField_));
    writeValue(oprot);
    oprot.writeFieldEnd();
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  protected abstract void checkType(F setField, Object value) throws ClassCastException;

  protected abstract Object readValue(TProtocol iprot, TField field) throws TException;

  protected abstract void writeValue(TProtocol oprot) throws TException;

  protected abstract TStruct getStructDesc();

  protected abstract TField getFieldDesc(F setField);

  protected abstract F enumForId(short id);

  @Override
  public String toString() {
    String result = "<" + this.getClass().getSimpleName() + " ";

    if (getSetField() != null) {
      Object v = getFieldValue();
      String vStr = null;
      if (v instanceof byte[]) {
        vStr = bytesToStr((byte[])v);
      } else {
        vStr = v.toString();
      }
      result += getFieldDesc(getSetField()).name + ":" + vStr;
    }

    return result + ">";
  }

  private static String bytesToStr(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    int size = Math.min(bytes.length, 128);
    for (int i = 0; i < size; i++) {
      if (i != 0) {
        sb.append(" ");
      }
      String digit = Integer.toHexString(bytes[i] & 0xFF);
      sb.append(digit.length() > 1 ? digit : "0" + digit);
    }
    if (bytes.length > 128) {
      sb.append(" ...");
    }
    return sb.toString();
  }

  public final void clear() {
    this.setField_ = null;
    this.value_ = null;
  }
}
