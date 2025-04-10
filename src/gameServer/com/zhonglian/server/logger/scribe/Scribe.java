package com.zhonglian.server.logger.scribe;

import com.facebook.fb303.FacebookService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TBase;
import org.apache.thrift.TBaseHelper;
import org.apache.thrift.TException;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClient;
import org.apache.thrift.async.TAsyncClientFactory;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.async.TAsyncMethodCall;
import org.apache.thrift.meta_data.EnumMetaData;
import org.apache.thrift.meta_data.FieldMetaData;
import org.apache.thrift.meta_data.FieldValueMetaData;
import org.apache.thrift.meta_data.ListMetaData;
import org.apache.thrift.meta_data.StructMetaData;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scribe
{
public static class Client
extends FacebookService.Client
implements TServiceClient, Iface
{
public static class Factory
implements TServiceClientFactory<Client>
{
public Scribe.Client getClient(TProtocol prot) {
return new Scribe.Client(prot);
}

public Scribe.Client getClient(TProtocol iprot, TProtocol oprot) {
return new Scribe.Client(iprot, oprot);
}
}

public Client(TProtocol prot) {
this(prot, prot);
}

public Client(TProtocol iprot, TProtocol oprot) {
super(iprot, oprot);
}

public ResultCode Log(List<LogEntry> messages) throws TException {
send_Log(messages);
return recv_Log();
}

public void send_Log(List<LogEntry> messages) throws TException {
this.oprot_.writeMessageBegin(new TMessage("Log", (byte)1, ++this.seqid_));
Scribe.Log_args args = new Scribe.Log_args();
args.setMessages(messages);
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public ResultCode recv_Log() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "Log failed: out of sequence response");
}
Scribe.Log_result result = new Scribe.Log_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "Log failed: unknown result");
}
}

public static class AsyncClient
extends FacebookService.AsyncClient implements AsyncIface {
public static class Factory implements TAsyncClientFactory<AsyncClient> {
private TAsyncClientManager clientManager;
private TProtocolFactory protocolFactory;

public Factory(TAsyncClientManager clientManager, TProtocolFactory protocolFactory) {
this.clientManager = clientManager;
this.protocolFactory = protocolFactory;
}

public Scribe.AsyncClient getAsyncClient(TNonblockingTransport transport) {
return new Scribe.AsyncClient(this.protocolFactory, this.clientManager, transport);
}
}

public AsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager clientManager, TNonblockingTransport transport) {
super(protocolFactory, clientManager, transport);
}

public void Log(List<LogEntry> messages, AsyncMethodCallback<Log_call> resultHandler) throws TException {
checkReady();
Log_call method_call = new Log_call(messages, resultHandler, (TAsyncClient)this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class Log_call
extends TAsyncMethodCall
{
private List<LogEntry> messages;

public Log_call(List<LogEntry> messages, AsyncMethodCallback<Log_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
this.messages = messages;
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("Log", (byte)1, 0));
Scribe.Log_args args = new Scribe.Log_args();
args.setMessages(this.messages);
args.write(prot);
prot.writeMessageEnd();
}

public ResultCode getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new Scribe.Client(prot)).recv_Log();
}
}
}

public static class Processor
extends FacebookService.Processor
implements TProcessor {
private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

public Processor(Scribe.Iface iface) {
super(iface);
this.iface_ = iface;
this.processMap_.put("Log", new Log(null));
}

private Scribe.Iface iface_;

public boolean process(TProtocol iprot, TProtocol oprot) throws TException {
TMessage msg = iprot.readMessageBegin();
FacebookService.Processor.ProcessFunction fn = (FacebookService.Processor.ProcessFunction)this.processMap_.get(msg.name);
if (fn == null) {
TProtocolUtil.skip(iprot, (byte)12);
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(1, "Invalid method name: '" + msg.name + "'");
oprot.writeMessageBegin(new TMessage(msg.name, (byte)3, msg.seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return true;
} 
fn.process(msg.seqid, iprot, oprot);
return true;
}

private class Log implements FacebookService.Processor.ProcessFunction {
public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
Scribe.Log_args args = new Scribe.Log_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("Log", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
Scribe.Log_result result = new Scribe.Log_result();
result.success = Scribe.Processor.this.iface_.Log(args.messages);
oprot.writeMessageBegin(new TMessage("Log", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}

private Log() {}
}
}

public static class Log_args
implements TBase<Log_args, Log_args._Fields>, Serializable, Cloneable
{
private static final long serialVersionUID = -4861100422505147853L;
private static final TStruct STRUCT_DESC = new TStruct("Log_args");

private static final TField MESSAGES_FIELD_DESC = new TField("messages", (byte)15, (short)1);
public List<LogEntry> messages;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
MESSAGES((short)1, "messages");

private static final Map<String, _Fields> byName = new ConcurrentHashMap<>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

private final short _thriftId;
private final String _fieldName;

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 1:
return MESSAGES;
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
tmpMap.put(_Fields.MESSAGES, new FieldMetaData("messages", (byte)3, 
(FieldValueMetaData)new ListMetaData((byte)15, (FieldValueMetaData)new StructMetaData((byte)12, LogEntry.class))));
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(Log_args.class, metaDataMap);
}

public Log_args() {}

public Log_args(List<LogEntry> messages) {
this();
this.messages = messages;
}

public Log_args(Log_args other) {
if (other.isSetMessages()) {
List<LogEntry> __this__messages = new ArrayList<>();
for (LogEntry other_element : other.messages) {
__this__messages.add(new LogEntry(other_element));
}
this.messages = __this__messages;
} 
}

public Log_args deepCopy() {
return new Log_args(this);
}

public void clear() {
this.messages = null;
}

public int getMessagesSize() {
return (this.messages == null) ? 0 : this.messages.size();
}

public Iterator<LogEntry> getMessagesIterator() {
return (this.messages == null) ? null : this.messages.iterator();
}

public void addToMessages(LogEntry elem) {
if (this.messages == null) {
this.messages = new ArrayList<>();
}
this.messages.add(elem);
}

public List<LogEntry> getMessages() {
return this.messages;
}

public Log_args setMessages(List<LogEntry> messages) {
this.messages = messages;
return this;
}

public void unsetMessages() {
this.messages = null;
}

public boolean isSetMessages() {
return (this.messages != null);
}

public void setMessagesIsSet(boolean value) {
if (!value) {
this.messages = null;
}
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetMessages(); break;
} 
setMessages((List<LogEntry>)value);
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return getMessages();
} 

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

switch (field) {
case null:
return isSetMessages();
} 
throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof Log_args)
return equals((Log_args)that); 
return false;
}

public boolean equals(Log_args that) {
if (that == null) {
return false;
}
boolean this_present_messages = isSetMessages();
boolean that_present_messages = that.isSetMessages();
if (this_present_messages || that_present_messages) {
if (!this_present_messages || !that_present_messages)
return false; 
if (!this.messages.equals(that.messages)) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(Log_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
Log_args typedOther = other;

lastComparison = Boolean.valueOf(isSetMessages()).compareTo(Boolean.valueOf(typedOther.isSetMessages()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetMessages()) {
lastComparison = TBaseHelper.compareTo(this.messages, typedOther.messages);
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
if (field.type == 15) {

TList _list0 = iprot.readListBegin();
this.messages = new ArrayList<>(_list0.size);
for (int _i1 = 0; _i1 < _list0.size; _i1++) {

LogEntry _elem2 = new LogEntry();
_elem2.read(iprot);
this.messages.add(_elem2);
} 
iprot.readListEnd();
break;
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
if (this.messages != null) {
oprot.writeFieldBegin(MESSAGES_FIELD_DESC);

oprot.writeListBegin(new TList((byte)12, this.messages.size()));
for (LogEntry _iter3 : this.messages) {
_iter3.write(oprot);
}
oprot.writeListEnd();

oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("Log_args(");

boolean first = true;
sb.append("messages:");
if (this.messages == null) {
sb.append("null");
} else {
sb.append(this.messages);
} 
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class Log_result
implements TBase<Log_result, Log_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("Log_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)8, (short)0);

public ResultCode success;

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
SUCCESS((short)

0, "success");

private static final Map<String, _Fields> byName = new ConcurrentHashMap<>(); private final short _thriftId;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

private final String _fieldName;

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
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
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new EnumMetaData((byte)16, ResultCode.class)));
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(Log_result.class, metaDataMap);
}

public Log_result() {}

public Log_result(ResultCode success) {
this();
this.success = success;
}

public Log_result(Log_result other) {
if (other.isSetSuccess()) {
this.success = other.success;
}
}

public Log_result deepCopy() {
return new Log_result(this);
}

public void clear() {
this.success = null;
}

public ResultCode getSuccess() {
return this.success;
}

public Log_result setSuccess(ResultCode success) {
this.success = success;
return this;
}

public void unsetSuccess() {
this.success = null;
}

public boolean isSetSuccess() {
return (this.success != null);
}

public void setSuccessIsSet(boolean value) {
if (!value) {
this.success = null;
}
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetSuccess(); break;
} 
setSuccess((ResultCode)value);
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return getSuccess();
} 

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

switch (field) {
case null:
return isSetSuccess();
} 
throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof Log_result)
return equals((Log_result)that); 
return false;
}

public boolean equals(Log_result that) {
if (that == null) {
return false;
}
boolean this_present_success = isSetSuccess();
boolean that_present_success = that.isSetSuccess();
if (this_present_success || that_present_success) {
if (!this_present_success || !that_present_success)
return false; 
if (!this.success.equals(that.success)) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(Log_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
Log_result typedOther = other;

lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetSuccess()) {
lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
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
case 0:
if (field.type == 8) {
this.success = ResultCode.findByValue(iprot.readI32()); break;
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
oprot.writeStructBegin(STRUCT_DESC);

if (isSetSuccess()) {
oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
oprot.writeI32(this.success.getValue());
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("Log_result(");

boolean first = true;

sb.append("success:");
if (this.success == null) {
sb.append("null");
} else {
sb.append(this.success);
} 
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static interface AsyncIface extends FacebookService.AsyncIface {
void Log(List<LogEntry> param1List, AsyncMethodCallback<Scribe.AsyncClient.Log_call> param1AsyncMethodCallback) throws TException;
}

public static interface Iface extends FacebookService.Iface {
ResultCode Log(List<LogEntry> param1List) throws TException;
}
}

