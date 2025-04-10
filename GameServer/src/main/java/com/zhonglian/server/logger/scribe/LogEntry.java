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

public class LogEntry
implements TBase<LogEntry, LogEntry._Fields>, Serializable, Cloneable
{
private static final long serialVersionUID = 3121543719267680503L;
private static final TStruct STRUCT_DESC = new TStruct("LogEntry");

private static final TField CATEGORY_FIELD_DESC = new TField("category", (byte)11, (short)1);
private static final TField MESSAGE_FIELD_DESC = new TField("message", (byte)11, (short)2);
public String category;
public String message;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
CATEGORY((short)1, "category"), MESSAGE((short)2, "message");

private static final Map<String, _Fields> byName = new ConcurrentHashMap<>(); private final short _thriftId;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

private final String _fieldName;

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 1:
return CATEGORY;
case 2:
return MESSAGE;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null)
throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
return fields;
}

public static _Fields findByName(String name) {
return byName.get(name);
}

_Fields(short thriftId, String fieldName) {
this._thriftId = thriftId;
this._fieldName = fieldName;
}

public short getThriftFieldId() {
return this._thriftId;
}

public String getFieldName() {
return this._fieldName;
}
}

static {
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<>(_Fields.class);
tmpMap.put(_Fields.CATEGORY, new FieldMetaData("category", (byte)3, new FieldValueMetaData((byte)11)));
tmpMap.put(_Fields.MESSAGE, new FieldMetaData("message", (byte)3, new FieldValueMetaData((byte)11)));
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(LogEntry.class, metaDataMap);
}

public LogEntry() {}

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
return (this.category != null);
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
return (this.message != null);
}

public void setMessageIsSet(boolean value) {
if (!value) {
this.message = null;
}
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetCategory(); break;
} 
setCategory((String)value);
break;

case MESSAGE:
if (value == null) {
unsetMessage(); break;
} 
setMessage((String)value);
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return getCategory();

case MESSAGE:
return getMessage();
} 

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

switch (field) {
case null:
return isSetCategory();
case MESSAGE:
return isSetMessage();
} 
throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof LogEntry)
return equals((LogEntry)that); 
return false;
}

public boolean equals(LogEntry that) {
if (that == null) {
return false;
}
boolean this_present_category = isSetCategory();
boolean that_present_category = that.isSetCategory();
if (this_present_category || that_present_category) {
if (!this_present_category || !that_present_category)
return false; 
if (!this.category.equals(that.category)) {
return false;
}
} 
boolean this_present_message = isSetMessage();
boolean that_present_message = that.isSetMessage();
if (this_present_message || that_present_message) {
if (!this_present_message || !that_present_message)
return false; 
if (!this.message.equals(that.message)) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(LogEntry other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
LogEntry typedOther = other;

lastComparison = Boolean.valueOf(isSetCategory()).compareTo(Boolean.valueOf(typedOther.isSetCategory()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetCategory()) {
lastComparison = TBaseHelper.compareTo(this.category, typedOther.category);
if (lastComparison != 0) {
return lastComparison;
}
} 
lastComparison = Boolean.valueOf(isSetMessage()).compareTo(Boolean.valueOf(typedOther.isSetMessage()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetMessage()) {
lastComparison = TBaseHelper.compareTo(this.message, typedOther.message);
if (lastComparison != 0) {
return lastComparison;
}
} 
return 0;
}

public _Fields fieldForId(int fieldId) {
return _Fields.findByThriftId(fieldId);
}

public void read(TProtocol iprot) throws TException {
iprot.readStructBegin();
while (true) {
TField field = iprot.readFieldBegin();
if (field.type == 0) {
break;
}
switch (field.id) {
case 1:
if (field.type == 11) {
this.category = iprot.readString(); break;
} 
TProtocolUtil.skip(iprot, field.type);
break;

case 2:
if (field.type == 11) {
this.message = iprot.readString(); break;
} 
TProtocolUtil.skip(iprot, field.type);
break;

default:
TProtocolUtil.skip(iprot, field.type); break;
} 
iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

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
if (!first)
sb.append(", "); 
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

public void validate() throws TException {}
}

