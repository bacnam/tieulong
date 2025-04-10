package com.facebook.fb303;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
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
import org.apache.thrift.meta_data.MapMetaData;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TMap;
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

public class FacebookService
{
public static interface Iface
{
String getName() throws TException;

String getVersion() throws TException;

fb_status getStatus() throws TException;

String getStatusDetails() throws TException;

Map<String, Long> getCounters() throws TException;

long getCounter(String param1String) throws TException;

void setOption(String param1String1, String param1String2) throws TException;

String getOption(String param1String) throws TException;

Map<String, String> getOptions() throws TException;

String getCpuProfile(int param1Int) throws TException;

long aliveSince() throws TException;

void reinitialize() throws TException;

void shutdown() throws TException;
}

public static interface AsyncIface
{
void getName(AsyncMethodCallback<FacebookService.AsyncClient.getName_call> param1AsyncMethodCallback) throws TException;

void getVersion(AsyncMethodCallback<FacebookService.AsyncClient.getVersion_call> param1AsyncMethodCallback) throws TException;

void getStatus(AsyncMethodCallback<FacebookService.AsyncClient.getStatus_call> param1AsyncMethodCallback) throws TException;

void getStatusDetails(AsyncMethodCallback<FacebookService.AsyncClient.getStatusDetails_call> param1AsyncMethodCallback) throws TException;

void getCounters(AsyncMethodCallback<FacebookService.AsyncClient.getCounters_call> param1AsyncMethodCallback) throws TException;

void getCounter(String param1String, AsyncMethodCallback<FacebookService.AsyncClient.getCounter_call> param1AsyncMethodCallback) throws TException;

void setOption(String param1String1, String param1String2, AsyncMethodCallback<FacebookService.AsyncClient.setOption_call> param1AsyncMethodCallback) throws TException;

void getOption(String param1String, AsyncMethodCallback<FacebookService.AsyncClient.getOption_call> param1AsyncMethodCallback) throws TException;

void getOptions(AsyncMethodCallback<FacebookService.AsyncClient.getOptions_call> param1AsyncMethodCallback) throws TException;

void getCpuProfile(int param1Int, AsyncMethodCallback<FacebookService.AsyncClient.getCpuProfile_call> param1AsyncMethodCallback) throws TException;

void aliveSince(AsyncMethodCallback<FacebookService.AsyncClient.aliveSince_call> param1AsyncMethodCallback) throws TException;

void reinitialize(AsyncMethodCallback<FacebookService.AsyncClient.reinitialize_call> param1AsyncMethodCallback) throws TException;

void shutdown(AsyncMethodCallback<FacebookService.AsyncClient.shutdown_call> param1AsyncMethodCallback) throws TException;
}

public static class Client
implements TServiceClient, Iface
{
protected TProtocol iprot_;
protected TProtocol oprot_;
protected int seqid_;

public static class Factory
implements TServiceClientFactory<Client>
{
public FacebookService.Client getClient(TProtocol prot) {
return new FacebookService.Client(prot);
}
public FacebookService.Client getClient(TProtocol iprot, TProtocol oprot) {
return new FacebookService.Client(iprot, oprot);
}
}

public Client(TProtocol prot) {
this(prot, prot);
}

public Client(TProtocol iprot, TProtocol oprot) {
this.iprot_ = iprot;
this.oprot_ = oprot;
}

public TProtocol getInputProtocol() {
return this.iprot_;
}

public TProtocol getOutputProtocol() {
return this.oprot_;
}

public String getName() throws TException {
send_getName();
return recv_getName();
}

public void send_getName() throws TException {
this.oprot_.writeMessageBegin(new TMessage("getName", (byte)1, ++this.seqid_));
FacebookService.getName_args args = new FacebookService.getName_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public String recv_getName() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getName failed: out of sequence response");
}
FacebookService.getName_result result = new FacebookService.getName_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getName failed: unknown result");
}

public String getVersion() throws TException {
send_getVersion();
return recv_getVersion();
}

public void send_getVersion() throws TException {
this.oprot_.writeMessageBegin(new TMessage("getVersion", (byte)1, ++this.seqid_));
FacebookService.getVersion_args args = new FacebookService.getVersion_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public String recv_getVersion() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getVersion failed: out of sequence response");
}
FacebookService.getVersion_result result = new FacebookService.getVersion_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getVersion failed: unknown result");
}

public fb_status getStatus() throws TException {
send_getStatus();
return recv_getStatus();
}

public void send_getStatus() throws TException {
this.oprot_.writeMessageBegin(new TMessage("getStatus", (byte)1, ++this.seqid_));
FacebookService.getStatus_args args = new FacebookService.getStatus_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public fb_status recv_getStatus() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getStatus failed: out of sequence response");
}
FacebookService.getStatus_result result = new FacebookService.getStatus_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getStatus failed: unknown result");
}

public String getStatusDetails() throws TException {
send_getStatusDetails();
return recv_getStatusDetails();
}

public void send_getStatusDetails() throws TException {
this.oprot_.writeMessageBegin(new TMessage("getStatusDetails", (byte)1, ++this.seqid_));
FacebookService.getStatusDetails_args args = new FacebookService.getStatusDetails_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public String recv_getStatusDetails() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getStatusDetails failed: out of sequence response");
}
FacebookService.getStatusDetails_result result = new FacebookService.getStatusDetails_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getStatusDetails failed: unknown result");
}

public Map<String, Long> getCounters() throws TException {
send_getCounters();
return recv_getCounters();
}

public void send_getCounters() throws TException {
this.oprot_.writeMessageBegin(new TMessage("getCounters", (byte)1, ++this.seqid_));
FacebookService.getCounters_args args = new FacebookService.getCounters_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public Map<String, Long> recv_getCounters() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getCounters failed: out of sequence response");
}
FacebookService.getCounters_result result = new FacebookService.getCounters_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getCounters failed: unknown result");
}

public long getCounter(String key) throws TException {
send_getCounter(key);
return recv_getCounter();
}

public void send_getCounter(String key) throws TException {
this.oprot_.writeMessageBegin(new TMessage("getCounter", (byte)1, ++this.seqid_));
FacebookService.getCounter_args args = new FacebookService.getCounter_args();
args.setKey(key);
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public long recv_getCounter() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getCounter failed: out of sequence response");
}
FacebookService.getCounter_result result = new FacebookService.getCounter_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getCounter failed: unknown result");
}

public void setOption(String key, String value) throws TException {
send_setOption(key, value);
recv_setOption();
}

public void send_setOption(String key, String value) throws TException {
this.oprot_.writeMessageBegin(new TMessage("setOption", (byte)1, ++this.seqid_));
FacebookService.setOption_args args = new FacebookService.setOption_args();
args.setKey(key);
args.setValue(value);
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public void recv_setOption() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "setOption failed: out of sequence response");
}
FacebookService.setOption_result result = new FacebookService.setOption_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
}

public String getOption(String key) throws TException {
send_getOption(key);
return recv_getOption();
}

public void send_getOption(String key) throws TException {
this.oprot_.writeMessageBegin(new TMessage("getOption", (byte)1, ++this.seqid_));
FacebookService.getOption_args args = new FacebookService.getOption_args();
args.setKey(key);
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public String recv_getOption() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getOption failed: out of sequence response");
}
FacebookService.getOption_result result = new FacebookService.getOption_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getOption failed: unknown result");
}

public Map<String, String> getOptions() throws TException {
send_getOptions();
return recv_getOptions();
}

public void send_getOptions() throws TException {
this.oprot_.writeMessageBegin(new TMessage("getOptions", (byte)1, ++this.seqid_));
FacebookService.getOptions_args args = new FacebookService.getOptions_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public Map<String, String> recv_getOptions() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getOptions failed: out of sequence response");
}
FacebookService.getOptions_result result = new FacebookService.getOptions_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getOptions failed: unknown result");
}

public String getCpuProfile(int profileDurationInSec) throws TException {
send_getCpuProfile(profileDurationInSec);
return recv_getCpuProfile();
}

public void send_getCpuProfile(int profileDurationInSec) throws TException {
this.oprot_.writeMessageBegin(new TMessage("getCpuProfile", (byte)1, ++this.seqid_));
FacebookService.getCpuProfile_args args = new FacebookService.getCpuProfile_args();
args.setProfileDurationInSec(profileDurationInSec);
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public String recv_getCpuProfile() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "getCpuProfile failed: out of sequence response");
}
FacebookService.getCpuProfile_result result = new FacebookService.getCpuProfile_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "getCpuProfile failed: unknown result");
}

public long aliveSince() throws TException {
send_aliveSince();
return recv_aliveSince();
}

public void send_aliveSince() throws TException {
this.oprot_.writeMessageBegin(new TMessage("aliveSince", (byte)1, ++this.seqid_));
FacebookService.aliveSince_args args = new FacebookService.aliveSince_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public long recv_aliveSince() throws TException {
TMessage msg = this.iprot_.readMessageBegin();
if (msg.type == 3) {
TApplicationException x = TApplicationException.read(this.iprot_);
this.iprot_.readMessageEnd();
throw x;
} 
if (msg.seqid != this.seqid_) {
throw new TApplicationException(4, "aliveSince failed: out of sequence response");
}
FacebookService.aliveSince_result result = new FacebookService.aliveSince_result();
result.read(this.iprot_);
this.iprot_.readMessageEnd();
if (result.isSetSuccess()) {
return result.success;
}
throw new TApplicationException(5, "aliveSince failed: unknown result");
}

public void reinitialize() throws TException {
send_reinitialize();
}

public void send_reinitialize() throws TException {
this.oprot_.writeMessageBegin(new TMessage("reinitialize", (byte)1, ++this.seqid_));
FacebookService.reinitialize_args args = new FacebookService.reinitialize_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
}

public void shutdown() throws TException {
send_shutdown();
}

public void send_shutdown() throws TException {
this.oprot_.writeMessageBegin(new TMessage("shutdown", (byte)1, ++this.seqid_));
FacebookService.shutdown_args args = new FacebookService.shutdown_args();
args.write(this.oprot_);
this.oprot_.writeMessageEnd();
this.oprot_.getTransport().flush();
} }

public static class AsyncClient extends TAsyncClient implements AsyncIface {
public static class Factory implements TAsyncClientFactory<AsyncClient> {
private TAsyncClientManager clientManager;
private TProtocolFactory protocolFactory;

public Factory(TAsyncClientManager clientManager, TProtocolFactory protocolFactory) {
this.clientManager = clientManager;
this.protocolFactory = protocolFactory;
}
public FacebookService.AsyncClient getAsyncClient(TNonblockingTransport transport) {
return new FacebookService.AsyncClient(this.protocolFactory, this.clientManager, transport);
}
}

public AsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager clientManager, TNonblockingTransport transport) {
super(protocolFactory, clientManager, transport);
}

public void getName(AsyncMethodCallback<getName_call> resultHandler) throws TException {
checkReady();
getName_call method_call = new getName_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getName_call extends TAsyncMethodCall {
public getName_call(AsyncMethodCallback<getName_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getName", (byte)1, 0));
FacebookService.getName_args args = new FacebookService.getName_args();
args.write(prot);
prot.writeMessageEnd();
}

public String getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getName();
}
}

public void getVersion(AsyncMethodCallback<getVersion_call> resultHandler) throws TException {
checkReady();
getVersion_call method_call = new getVersion_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getVersion_call extends TAsyncMethodCall {
public getVersion_call(AsyncMethodCallback<getVersion_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getVersion", (byte)1, 0));
FacebookService.getVersion_args args = new FacebookService.getVersion_args();
args.write(prot);
prot.writeMessageEnd();
}

public String getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getVersion();
}
}

public void getStatus(AsyncMethodCallback<getStatus_call> resultHandler) throws TException {
checkReady();
getStatus_call method_call = new getStatus_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getStatus_call extends TAsyncMethodCall {
public getStatus_call(AsyncMethodCallback<getStatus_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getStatus", (byte)1, 0));
FacebookService.getStatus_args args = new FacebookService.getStatus_args();
args.write(prot);
prot.writeMessageEnd();
}

public fb_status getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getStatus();
}
}

public void getStatusDetails(AsyncMethodCallback<getStatusDetails_call> resultHandler) throws TException {
checkReady();
getStatusDetails_call method_call = new getStatusDetails_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getStatusDetails_call extends TAsyncMethodCall {
public getStatusDetails_call(AsyncMethodCallback<getStatusDetails_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getStatusDetails", (byte)1, 0));
FacebookService.getStatusDetails_args args = new FacebookService.getStatusDetails_args();
args.write(prot);
prot.writeMessageEnd();
}

public String getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getStatusDetails();
}
}

public void getCounters(AsyncMethodCallback<getCounters_call> resultHandler) throws TException {
checkReady();
getCounters_call method_call = new getCounters_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getCounters_call extends TAsyncMethodCall {
public getCounters_call(AsyncMethodCallback<getCounters_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getCounters", (byte)1, 0));
FacebookService.getCounters_args args = new FacebookService.getCounters_args();
args.write(prot);
prot.writeMessageEnd();
}

public Map<String, Long> getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getCounters();
}
}

public void getCounter(String key, AsyncMethodCallback<getCounter_call> resultHandler) throws TException {
checkReady();
getCounter_call method_call = new getCounter_call(key, resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getCounter_call extends TAsyncMethodCall { private String key;

public getCounter_call(String key, AsyncMethodCallback<getCounter_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
this.key = key;
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getCounter", (byte)1, 0));
FacebookService.getCounter_args args = new FacebookService.getCounter_args();
args.setKey(this.key);
args.write(prot);
prot.writeMessageEnd();
}

public long getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getCounter();
} }

public void setOption(String key, String value, AsyncMethodCallback<setOption_call> resultHandler) throws TException {
checkReady();
setOption_call method_call = new setOption_call(key, value, resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class setOption_call extends TAsyncMethodCall { private String key;
private String value;

public setOption_call(String key, String value, AsyncMethodCallback<setOption_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
this.key = key;
this.value = value;
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("setOption", (byte)1, 0));
FacebookService.setOption_args args = new FacebookService.setOption_args();
args.setKey(this.key);
args.setValue(this.value);
args.write(prot);
prot.writeMessageEnd();
}

public void getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
(new FacebookService.Client(prot)).recv_setOption();
} }

public void getOption(String key, AsyncMethodCallback<getOption_call> resultHandler) throws TException {
checkReady();
getOption_call method_call = new getOption_call(key, resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getOption_call extends TAsyncMethodCall { private String key;

public getOption_call(String key, AsyncMethodCallback<getOption_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
this.key = key;
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getOption", (byte)1, 0));
FacebookService.getOption_args args = new FacebookService.getOption_args();
args.setKey(this.key);
args.write(prot);
prot.writeMessageEnd();
}

public String getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getOption();
} }

public void getOptions(AsyncMethodCallback<getOptions_call> resultHandler) throws TException {
checkReady();
getOptions_call method_call = new getOptions_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getOptions_call extends TAsyncMethodCall {
public getOptions_call(AsyncMethodCallback<getOptions_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getOptions", (byte)1, 0));
FacebookService.getOptions_args args = new FacebookService.getOptions_args();
args.write(prot);
prot.writeMessageEnd();
}

public Map<String, String> getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getOptions();
}
}

public void getCpuProfile(int profileDurationInSec, AsyncMethodCallback<getCpuProfile_call> resultHandler) throws TException {
checkReady();
getCpuProfile_call method_call = new getCpuProfile_call(profileDurationInSec, resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class getCpuProfile_call extends TAsyncMethodCall { private int profileDurationInSec;

public getCpuProfile_call(int profileDurationInSec, AsyncMethodCallback<getCpuProfile_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
this.profileDurationInSec = profileDurationInSec;
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("getCpuProfile", (byte)1, 0));
FacebookService.getCpuProfile_args args = new FacebookService.getCpuProfile_args();
args.setProfileDurationInSec(this.profileDurationInSec);
args.write(prot);
prot.writeMessageEnd();
}

public String getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_getCpuProfile();
} }

public void aliveSince(AsyncMethodCallback<aliveSince_call> resultHandler) throws TException {
checkReady();
aliveSince_call method_call = new aliveSince_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class aliveSince_call extends TAsyncMethodCall {
public aliveSince_call(AsyncMethodCallback<aliveSince_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, false);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("aliveSince", (byte)1, 0));
FacebookService.aliveSince_args args = new FacebookService.aliveSince_args();
args.write(prot);
prot.writeMessageEnd();
}

public long getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
return (new FacebookService.Client(prot)).recv_aliveSince();
}
}

public void reinitialize(AsyncMethodCallback<reinitialize_call> resultHandler) throws TException {
checkReady();
reinitialize_call method_call = new reinitialize_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class reinitialize_call extends TAsyncMethodCall {
public reinitialize_call(AsyncMethodCallback<reinitialize_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, true);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("reinitialize", (byte)1, 0));
FacebookService.reinitialize_args args = new FacebookService.reinitialize_args();
args.write(prot);
prot.writeMessageEnd();
}

public void getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
}
}

public void shutdown(AsyncMethodCallback<shutdown_call> resultHandler) throws TException {
checkReady();
shutdown_call method_call = new shutdown_call(resultHandler, this, this.protocolFactory, this.transport);
this.manager.call(method_call);
}

public static class shutdown_call extends TAsyncMethodCall {
public shutdown_call(AsyncMethodCallback<shutdown_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
super(client, protocolFactory, transport, resultHandler, true);
}

public void write_args(TProtocol prot) throws TException {
prot.writeMessageBegin(new TMessage("shutdown", (byte)1, 0));
FacebookService.shutdown_args args = new FacebookService.shutdown_args();
args.write(prot);
prot.writeMessageEnd();
}

public void getResult() throws TException {
if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
throw new IllegalStateException("Method call not finished!");
}
TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
}
}
}

public static class Processor
implements TProcessor {
private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

private FacebookService.Iface iface_;

protected final HashMap<String, ProcessFunction> processMap_;

public Processor(FacebookService.Iface iface) {
this.processMap_ = new HashMap<String, ProcessFunction>(); this.iface_ = iface; this.processMap_.put("getName", new getName()); this.processMap_.put("getVersion", new getVersion()); this.processMap_.put("getStatus", new getStatus()); this.processMap_.put("getStatusDetails", new getStatusDetails()); this.processMap_.put("getCounters", new getCounters()); this.processMap_.put("getCounter", new getCounter()); this.processMap_.put("setOption", new setOption()); this.processMap_.put("getOption", new getOption()); this.processMap_.put("getOptions", new getOptions());
this.processMap_.put("getCpuProfile", new getCpuProfile());
this.processMap_.put("aliveSince", new aliveSince());
this.processMap_.put("reinitialize", new reinitialize());
this.processMap_.put("shutdown", new shutdown()); } public boolean process(TProtocol iprot, TProtocol oprot) throws TException { TMessage msg = iprot.readMessageBegin();
ProcessFunction fn = this.processMap_.get(msg.name);
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
return true; }

protected static interface ProcessFunction {
void process(int param2Int, TProtocol param2TProtocol1, TProtocol param2TProtocol2) throws TException; }
private class getName implements ProcessFunction { private getName() {}
public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getName_args args = new FacebookService.getName_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getName", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getName_result result = new FacebookService.getName_result();
result.success = FacebookService.Processor.this.iface_.getName();
oprot.writeMessageBegin(new TMessage("getName", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
} }

private class getVersion implements ProcessFunction {
private getVersion() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getVersion_args args = new FacebookService.getVersion_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getVersion", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getVersion_result result = new FacebookService.getVersion_result();
result.success = FacebookService.Processor.this.iface_.getVersion();
oprot.writeMessageBegin(new TMessage("getVersion", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class getStatus implements ProcessFunction {
private getStatus() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getStatus_args args = new FacebookService.getStatus_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getStatus", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getStatus_result result = new FacebookService.getStatus_result();
result.success = FacebookService.Processor.this.iface_.getStatus();
oprot.writeMessageBegin(new TMessage("getStatus", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class getStatusDetails implements ProcessFunction {
private getStatusDetails() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getStatusDetails_args args = new FacebookService.getStatusDetails_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getStatusDetails", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getStatusDetails_result result = new FacebookService.getStatusDetails_result();
result.success = FacebookService.Processor.this.iface_.getStatusDetails();
oprot.writeMessageBegin(new TMessage("getStatusDetails", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class getCounters implements ProcessFunction {
private getCounters() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getCounters_args args = new FacebookService.getCounters_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getCounters", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getCounters_result result = new FacebookService.getCounters_result();
result.success = FacebookService.Processor.this.iface_.getCounters();
oprot.writeMessageBegin(new TMessage("getCounters", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class getCounter implements ProcessFunction {
private getCounter() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getCounter_args args = new FacebookService.getCounter_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getCounter", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getCounter_result result = new FacebookService.getCounter_result();
result.success = FacebookService.Processor.this.iface_.getCounter(args.key);
result.setSuccessIsSet(true);
oprot.writeMessageBegin(new TMessage("getCounter", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class setOption implements ProcessFunction {
private setOption() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.setOption_args args = new FacebookService.setOption_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("setOption", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.setOption_result result = new FacebookService.setOption_result();
FacebookService.Processor.this.iface_.setOption(args.key, args.value);
oprot.writeMessageBegin(new TMessage("setOption", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class getOption implements ProcessFunction {
private getOption() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getOption_args args = new FacebookService.getOption_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getOption", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getOption_result result = new FacebookService.getOption_result();
result.success = FacebookService.Processor.this.iface_.getOption(args.key);
oprot.writeMessageBegin(new TMessage("getOption", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class getOptions implements ProcessFunction {
private getOptions() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getOptions_args args = new FacebookService.getOptions_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getOptions", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getOptions_result result = new FacebookService.getOptions_result();
result.success = FacebookService.Processor.this.iface_.getOptions();
oprot.writeMessageBegin(new TMessage("getOptions", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class getCpuProfile implements ProcessFunction {
private getCpuProfile() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.getCpuProfile_args args = new FacebookService.getCpuProfile_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("getCpuProfile", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.getCpuProfile_result result = new FacebookService.getCpuProfile_result();
result.success = FacebookService.Processor.this.iface_.getCpuProfile(args.profileDurationInSec);
oprot.writeMessageBegin(new TMessage("getCpuProfile", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class aliveSince implements ProcessFunction {
private aliveSince() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.aliveSince_args args = new FacebookService.aliveSince_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("aliveSince", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.aliveSince_result result = new FacebookService.aliveSince_result();
result.success = FacebookService.Processor.this.iface_.aliveSince();
result.setSuccessIsSet(true);
oprot.writeMessageBegin(new TMessage("aliveSince", (byte)2, seqid));
result.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
}
}

private class reinitialize implements ProcessFunction {
private reinitialize() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.reinitialize_args args = new FacebookService.reinitialize_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("reinitialize", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.Processor.this.iface_.reinitialize();
}
}

private class shutdown implements ProcessFunction {
private shutdown() {}

public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
FacebookService.shutdown_args args = new FacebookService.shutdown_args();
try {
args.read(iprot);
} catch (TProtocolException e) {
iprot.readMessageEnd();
TApplicationException x = new TApplicationException(7, e.getMessage());
oprot.writeMessageBegin(new TMessage("shutdown", (byte)3, seqid));
x.write(oprot);
oprot.writeMessageEnd();
oprot.getTransport().flush();
return;
} 
iprot.readMessageEnd();
FacebookService.Processor.this.iface_.shutdown();
}
}
}

public static class getName_args
implements TBase<getName_args, getName_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getName_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getName_args.class, metaDataMap);
}

public getName_args() {}

public getName_args(getName_args other) {}

public getName_args deepCopy() {
return new getName_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getName_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getName_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getName_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getName_args)
return equals((getName_args)that); 
return false;
}

public boolean equals(getName_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getName_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getName_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getName_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getName_result
implements TBase<getName_result, getName_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getName_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
public String success;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getName_result.class, metaDataMap);
}

public getName_result() {}

public getName_result(String success) {
this();
this.success = success;
}

public getName_result(getName_result other) {
if (other.isSetSuccess()) {
this.success = other.success;
}
}

public getName_result deepCopy() {
return new getName_result(this);
}

public void clear() {
this.success = null;
}

public String getSuccess() {
return this.success;
}

public getName_result setSuccess(String success) {
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
setSuccess((String)value);
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
if (that instanceof getName_result)
return equals((getName_result)that); 
return false;
}

public boolean equals(getName_result that) {
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

public int compareTo(getName_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getName_result typedOther = other;

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
if (field.type == 11) {
this.success = iprot.readString(); break;
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
oprot.writeString(this.success);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getName_result(");
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

public static class getVersion_args
implements TBase<getVersion_args, getVersion_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getVersion_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getVersion_args.class, metaDataMap);
}

public getVersion_args() {}

public getVersion_args(getVersion_args other) {}

public getVersion_args deepCopy() {
return new getVersion_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getVersion_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getVersion_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getVersion_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getVersion_args)
return equals((getVersion_args)that); 
return false;
}

public boolean equals(getVersion_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getVersion_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getVersion_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getVersion_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getVersion_result
implements TBase<getVersion_result, getVersion_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getVersion_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
public String success;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getVersion_result.class, metaDataMap);
}

public getVersion_result() {}

public getVersion_result(String success) {
this();
this.success = success;
}

public getVersion_result(getVersion_result other) {
if (other.isSetSuccess()) {
this.success = other.success;
}
}

public getVersion_result deepCopy() {
return new getVersion_result(this);
}

public void clear() {
this.success = null;
}

public String getSuccess() {
return this.success;
}

public getVersion_result setSuccess(String success) {
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
setSuccess((String)value);
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
if (that instanceof getVersion_result)
return equals((getVersion_result)that); 
return false;
}

public boolean equals(getVersion_result that) {
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

public int compareTo(getVersion_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getVersion_result typedOther = other;

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
if (field.type == 11) {
this.success = iprot.readString(); break;
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
oprot.writeString(this.success);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getVersion_result(");
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

public static class getStatus_args
implements TBase<getStatus_args, getStatus_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getStatus_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getStatus_args.class, metaDataMap);
}

public getStatus_args() {}

public getStatus_args(getStatus_args other) {}

public getStatus_args deepCopy() {
return new getStatus_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatus_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatus_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatus_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getStatus_args)
return equals((getStatus_args)that); 
return false;
}

public boolean equals(getStatus_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getStatus_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getStatus_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getStatus_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getStatus_result
implements TBase<getStatus_result, getStatus_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getStatus_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)8, (short)0);

public fb_status success;

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new EnumMetaData((byte)16, fb_status.class)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getStatus_result.class, metaDataMap);
}

public getStatus_result() {}

public getStatus_result(fb_status success) {
this();
this.success = success;
}

public getStatus_result(getStatus_result other) {
if (other.isSetSuccess()) {
this.success = other.success;
}
}

public getStatus_result deepCopy() {
return new getStatus_result(this);
}

public void clear() {
this.success = null;
}

public fb_status getSuccess() {
return this.success;
}

public getStatus_result setSuccess(fb_status success) {
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
setSuccess((fb_status)value);
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
if (that instanceof getStatus_result)
return equals((getStatus_result)that); 
return false;
}

public boolean equals(getStatus_result that) {
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

public int compareTo(getStatus_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getStatus_result typedOther = other;

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
this.success = fb_status.findByValue(iprot.readI32()); break;
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
StringBuilder sb = new StringBuilder("getStatus_result(");
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

public static class getStatusDetails_args
implements TBase<getStatusDetails_args, getStatusDetails_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getStatusDetails_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getStatusDetails_args.class, metaDataMap);
}

public getStatusDetails_args() {}

public getStatusDetails_args(getStatusDetails_args other) {}

public getStatusDetails_args deepCopy() {
return new getStatusDetails_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatusDetails_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatusDetails_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatusDetails_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getStatusDetails_args)
return equals((getStatusDetails_args)that); 
return false;
}

public boolean equals(getStatusDetails_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getStatusDetails_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getStatusDetails_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getStatusDetails_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getStatusDetails_result
implements TBase<getStatusDetails_result, getStatusDetails_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getStatusDetails_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
public String success;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getStatusDetails_result.class, metaDataMap);
}

public getStatusDetails_result() {}

public getStatusDetails_result(String success) {
this();
this.success = success;
}

public getStatusDetails_result(getStatusDetails_result other) {
if (other.isSetSuccess()) {
this.success = other.success;
}
}

public getStatusDetails_result deepCopy() {
return new getStatusDetails_result(this);
}

public void clear() {
this.success = null;
}

public String getSuccess() {
return this.success;
}

public getStatusDetails_result setSuccess(String success) {
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
setSuccess((String)value);
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
if (that instanceof getStatusDetails_result)
return equals((getStatusDetails_result)that); 
return false;
}

public boolean equals(getStatusDetails_result that) {
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

public int compareTo(getStatusDetails_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getStatusDetails_result typedOther = other;

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
if (field.type == 11) {
this.success = iprot.readString(); break;
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
oprot.writeString(this.success);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getStatusDetails_result(");
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

public static class getCounters_args
implements TBase<getCounters_args, getCounters_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getCounters_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getCounters_args.class, metaDataMap);
}

public getCounters_args() {}

public getCounters_args(getCounters_args other) {}

public getCounters_args deepCopy() {
return new getCounters_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getCounters_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getCounters_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getCounters_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getCounters_args)
return equals((getCounters_args)that); 
return false;
}

public boolean equals(getCounters_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getCounters_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getCounters_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getCounters_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getCounters_result
implements TBase<getCounters_result, getCounters_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getCounters_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)13, (short)0);
public Map<String, Long> success;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new MapMetaData((byte)13, new FieldValueMetaData((byte)11), new FieldValueMetaData((byte)10))));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getCounters_result.class, metaDataMap);
}

public getCounters_result() {}

public getCounters_result(Map<String, Long> success) {
this();
this.success = success;
}

public getCounters_result(getCounters_result other) {
if (other.isSetSuccess()) {
Map<String, Long> __this__success = new HashMap<String, Long>();
for (Map.Entry<String, Long> other_element : other.success.entrySet()) {

String other_element_key = other_element.getKey();
Long other_element_value = other_element.getValue();

String __this__success_copy_key = other_element_key;

Long __this__success_copy_value = other_element_value;

__this__success.put(__this__success_copy_key, __this__success_copy_value);
} 
this.success = __this__success;
} 
}

public getCounters_result deepCopy() {
return new getCounters_result(this);
}

public void clear() {
this.success = null;
}

public int getSuccessSize() {
return (this.success == null) ? 0 : this.success.size();
}

public void putToSuccess(String key, long val) {
if (this.success == null) {
this.success = new HashMap<String, Long>();
}
this.success.put(key, Long.valueOf(val));
}

public Map<String, Long> getSuccess() {
return this.success;
}

public getCounters_result setSuccess(Map<String, Long> success) {
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
setSuccess((Map<String, Long>)value);
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
if (that instanceof getCounters_result)
return equals((getCounters_result)that); 
return false;
}

public boolean equals(getCounters_result that) {
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

public int compareTo(getCounters_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getCounters_result typedOther = other;

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
if (field.type == 13) {

TMap _map0 = iprot.readMapBegin();
this.success = new HashMap<String, Long>(2 * _map0.size);
for (int _i1 = 0; _i1 < _map0.size; _i1++) {

String _key2 = iprot.readString();
long _val3 = iprot.readI64();
this.success.put(_key2, Long.valueOf(_val3));
} 
iprot.readMapEnd();
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
oprot.writeStructBegin(STRUCT_DESC);

if (isSetSuccess()) {
oprot.writeFieldBegin(SUCCESS_FIELD_DESC);

oprot.writeMapBegin(new TMap((byte)11, (byte)10, this.success.size()));
for (Map.Entry<String, Long> _iter4 : this.success.entrySet()) {

oprot.writeString(_iter4.getKey());
oprot.writeI64(((Long)_iter4.getValue()).longValue());
} 
oprot.writeMapEnd();

oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getCounters_result(");
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

public static class getCounter_args
implements TBase<getCounter_args, getCounter_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getCounter_args");

private static final TField KEY_FIELD_DESC = new TField("key", (byte)11, (short)1);
public String key;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
KEY((short)1, "key"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 1:
return KEY;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.KEY, new FieldMetaData("key", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getCounter_args.class, metaDataMap);
}

public getCounter_args() {}

public getCounter_args(String key) {
this();
this.key = key;
}

public getCounter_args(getCounter_args other) {
if (other.isSetKey()) {
this.key = other.key;
}
}

public getCounter_args deepCopy() {
return new getCounter_args(this);
}

public void clear() {
this.key = null;
}

public String getKey() {
return this.key;
}

public getCounter_args setKey(String key) {
this.key = key;
return this;
}

public void unsetKey() {
this.key = null;
}

public boolean isSetKey() {
return (this.key != null);
}

public void setKeyIsSet(boolean value) {
if (!value) {
this.key = null;
}
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetKey(); break;
} 
setKey((String)value);
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return getKey();
} 

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

switch (field) {
case null:
return isSetKey();
} 
throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getCounter_args)
return equals((getCounter_args)that); 
return false;
}

public boolean equals(getCounter_args that) {
if (that == null) {
return false;
}
boolean this_present_key = isSetKey();
boolean that_present_key = that.isSetKey();
if (this_present_key || that_present_key) {
if (!this_present_key || !that_present_key)
return false; 
if (!this.key.equals(that.key)) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getCounter_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getCounter_args typedOther = other;

lastComparison = Boolean.valueOf(isSetKey()).compareTo(Boolean.valueOf(typedOther.isSetKey()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetKey()) {
lastComparison = TBaseHelper.compareTo(this.key, typedOther.key);
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
this.key = iprot.readString(); break;
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
if (this.key != null) {
oprot.writeFieldBegin(KEY_FIELD_DESC);
oprot.writeString(this.key);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getCounter_args(");
boolean first = true;

sb.append("key:");
if (this.key == null) {
sb.append("null");
} else {
sb.append(this.key);
} 
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getCounter_result
implements TBase<getCounter_result, getCounter_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getCounter_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)10, (short)0);
public long success;
private static final int __SUCCESS_ISSET_ID = 0;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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

private BitSet __isset_bit_vector = new BitSet(1);
public static final Map<_Fields, FieldMetaData> metaDataMap;

static {
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)10)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getCounter_result.class, metaDataMap);
}

public getCounter_result(long success) {
this();
this.success = success;
setSuccessIsSet(true);
}

public getCounter_result(getCounter_result other) {
this.__isset_bit_vector.clear();
this.__isset_bit_vector.or(other.__isset_bit_vector);
this.success = other.success;
}

public getCounter_result deepCopy() {
return new getCounter_result(this);
}

public void clear() {
setSuccessIsSet(false);
this.success = 0L;
}

public long getSuccess() {
return this.success;
}

public getCounter_result setSuccess(long success) {
this.success = success;
setSuccessIsSet(true);
return this;
}

public void unsetSuccess() {
this.__isset_bit_vector.clear(0);
}

public boolean isSetSuccess() {
return this.__isset_bit_vector.get(0);
}

public void setSuccessIsSet(boolean value) {
this.__isset_bit_vector.set(0, value);
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetSuccess(); break;
} 
setSuccess(((Long)value).longValue());
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return new Long(getSuccess());
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
if (that instanceof getCounter_result)
return equals((getCounter_result)that); 
return false;
}

public boolean equals(getCounter_result that) {
if (that == null) {
return false;
}
boolean this_present_success = true;
boolean that_present_success = true;
if (this_present_success || that_present_success) {
if (!this_present_success || !that_present_success)
return false; 
if (this.success != that.success) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getCounter_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getCounter_result typedOther = other;

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
if (field.type == 10) {
this.success = iprot.readI64();
setSuccessIsSet(true); break;
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
oprot.writeI64(this.success);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getCounter_result(");
boolean first = true;

sb.append("success:");
sb.append(this.success);
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}

public getCounter_result() {}
}

public static class setOption_args
implements TBase<setOption_args, setOption_args._Fields>, Serializable, Cloneable {
private static final TStruct STRUCT_DESC = new TStruct("setOption_args");

private static final TField KEY_FIELD_DESC = new TField("key", (byte)11, (short)1);
private static final TField VALUE_FIELD_DESC = new TField("value", (byte)11, (short)2);
public String key;
public String value;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
KEY((short)1, "key"),
VALUE((short)2, "value");

private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 1:
return KEY;
case 2:
return VALUE;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.KEY, new FieldMetaData("key", (byte)3, new FieldValueMetaData((byte)11)));

tmpMap.put(_Fields.VALUE, new FieldMetaData("value", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(setOption_args.class, metaDataMap);
}

public setOption_args() {}

public setOption_args(String key, String value) {
this();
this.key = key;
this.value = value;
}

public setOption_args(setOption_args other) {
if (other.isSetKey()) {
this.key = other.key;
}
if (other.isSetValue()) {
this.value = other.value;
}
}

public setOption_args deepCopy() {
return new setOption_args(this);
}

public void clear() {
this.key = null;
this.value = null;
}

public String getKey() {
return this.key;
}

public setOption_args setKey(String key) {
this.key = key;
return this;
}

public void unsetKey() {
this.key = null;
}

public boolean isSetKey() {
return (this.key != null);
}

public void setKeyIsSet(boolean value) {
if (!value) {
this.key = null;
}
}

public String getValue() {
return this.value;
}

public setOption_args setValue(String value) {
this.value = value;
return this;
}

public void unsetValue() {
this.value = null;
}

public boolean isSetValue() {
return (this.value != null);
}

public void setValueIsSet(boolean value) {
if (!value) {
this.value = null;
}
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetKey(); break;
} 
setKey((String)value);
break;

case null:
if (value == null) {
unsetValue(); break;
} 
setValue((String)value);
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return getKey();

case null:
return getValue();
} 

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

switch (field) {
case null:
return isSetKey();
case null:
return isSetValue();
} 
throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof setOption_args)
return equals((setOption_args)that); 
return false;
}

public boolean equals(setOption_args that) {
if (that == null) {
return false;
}
boolean this_present_key = isSetKey();
boolean that_present_key = that.isSetKey();
if (this_present_key || that_present_key) {
if (!this_present_key || !that_present_key)
return false; 
if (!this.key.equals(that.key)) {
return false;
}
} 
boolean this_present_value = isSetValue();
boolean that_present_value = that.isSetValue();
if (this_present_value || that_present_value) {
if (!this_present_value || !that_present_value)
return false; 
if (!this.value.equals(that.value)) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(setOption_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
setOption_args typedOther = other;

lastComparison = Boolean.valueOf(isSetKey()).compareTo(Boolean.valueOf(typedOther.isSetKey()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetKey()) {
lastComparison = TBaseHelper.compareTo(this.key, typedOther.key);
if (lastComparison != 0) {
return lastComparison;
}
} 
lastComparison = Boolean.valueOf(isSetValue()).compareTo(Boolean.valueOf(typedOther.isSetValue()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetValue()) {
lastComparison = TBaseHelper.compareTo(this.value, typedOther.value);
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
this.key = iprot.readString(); break;
} 
TProtocolUtil.skip(iprot, field.type);
break;

case 2:
if (field.type == 11) {
this.value = iprot.readString(); break;
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
if (this.key != null) {
oprot.writeFieldBegin(KEY_FIELD_DESC);
oprot.writeString(this.key);
oprot.writeFieldEnd();
} 
if (this.value != null) {
oprot.writeFieldBegin(VALUE_FIELD_DESC);
oprot.writeString(this.value);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("setOption_args(");
boolean first = true;

sb.append("key:");
if (this.key == null) {
sb.append("null");
} else {
sb.append(this.key);
} 
first = false;
if (!first) sb.append(", "); 
sb.append("value:");
if (this.value == null) {
sb.append("null");
} else {
sb.append(this.value);
} 
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class setOption_result
implements TBase<setOption_result, setOption_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("setOption_result");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(setOption_result.class, metaDataMap);
}

public setOption_result() {}

public setOption_result(setOption_result other) {}

public setOption_result deepCopy() {
return new setOption_result(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$setOption_result$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$setOption_result$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$setOption_result$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof setOption_result)
return equals((setOption_result)that); 
return false;
}

public boolean equals(setOption_result that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(setOption_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
setOption_result typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
oprot.writeStructBegin(STRUCT_DESC);

oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("setOption_result(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getOption_args
implements TBase<getOption_args, getOption_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getOption_args");

private static final TField KEY_FIELD_DESC = new TField("key", (byte)11, (short)1);
public String key;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
KEY((short)1, "key"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 1:
return KEY;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.KEY, new FieldMetaData("key", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getOption_args.class, metaDataMap);
}

public getOption_args() {}

public getOption_args(String key) {
this();
this.key = key;
}

public getOption_args(getOption_args other) {
if (other.isSetKey()) {
this.key = other.key;
}
}

public getOption_args deepCopy() {
return new getOption_args(this);
}

public void clear() {
this.key = null;
}

public String getKey() {
return this.key;
}

public getOption_args setKey(String key) {
this.key = key;
return this;
}

public void unsetKey() {
this.key = null;
}

public boolean isSetKey() {
return (this.key != null);
}

public void setKeyIsSet(boolean value) {
if (!value) {
this.key = null;
}
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetKey(); break;
} 
setKey((String)value);
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return getKey();
} 

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

switch (field) {
case null:
return isSetKey();
} 
throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getOption_args)
return equals((getOption_args)that); 
return false;
}

public boolean equals(getOption_args that) {
if (that == null) {
return false;
}
boolean this_present_key = isSetKey();
boolean that_present_key = that.isSetKey();
if (this_present_key || that_present_key) {
if (!this_present_key || !that_present_key)
return false; 
if (!this.key.equals(that.key)) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getOption_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getOption_args typedOther = other;

lastComparison = Boolean.valueOf(isSetKey()).compareTo(Boolean.valueOf(typedOther.isSetKey()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetKey()) {
lastComparison = TBaseHelper.compareTo(this.key, typedOther.key);
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
this.key = iprot.readString(); break;
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
if (this.key != null) {
oprot.writeFieldBegin(KEY_FIELD_DESC);
oprot.writeString(this.key);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getOption_args(");
boolean first = true;

sb.append("key:");
if (this.key == null) {
sb.append("null");
} else {
sb.append(this.key);
} 
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getOption_result
implements TBase<getOption_result, getOption_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getOption_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
public String success;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getOption_result.class, metaDataMap);
}

public getOption_result() {}

public getOption_result(String success) {
this();
this.success = success;
}

public getOption_result(getOption_result other) {
if (other.isSetSuccess()) {
this.success = other.success;
}
}

public getOption_result deepCopy() {
return new getOption_result(this);
}

public void clear() {
this.success = null;
}

public String getSuccess() {
return this.success;
}

public getOption_result setSuccess(String success) {
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
setSuccess((String)value);
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
if (that instanceof getOption_result)
return equals((getOption_result)that); 
return false;
}

public boolean equals(getOption_result that) {
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

public int compareTo(getOption_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getOption_result typedOther = other;

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
if (field.type == 11) {
this.success = iprot.readString(); break;
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
oprot.writeString(this.success);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getOption_result(");
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

public static class getOptions_args
implements TBase<getOptions_args, getOptions_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getOptions_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getOptions_args.class, metaDataMap);
}

public getOptions_args() {}

public getOptions_args(getOptions_args other) {}

public getOptions_args deepCopy() {
return new getOptions_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getOptions_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getOptions_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getOptions_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getOptions_args)
return equals((getOptions_args)that); 
return false;
}

public boolean equals(getOptions_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getOptions_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getOptions_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getOptions_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class getOptions_result
implements TBase<getOptions_result, getOptions_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getOptions_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)13, (short)0);
public Map<String, String> success;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new MapMetaData((byte)13, new FieldValueMetaData((byte)11), new FieldValueMetaData((byte)11))));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getOptions_result.class, metaDataMap);
}

public getOptions_result() {}

public getOptions_result(Map<String, String> success) {
this();
this.success = success;
}

public getOptions_result(getOptions_result other) {
if (other.isSetSuccess()) {
Map<String, String> __this__success = new HashMap<String, String>();
for (Map.Entry<String, String> other_element : other.success.entrySet()) {

String other_element_key = other_element.getKey();
String other_element_value = other_element.getValue();

String __this__success_copy_key = other_element_key;

String __this__success_copy_value = other_element_value;

__this__success.put(__this__success_copy_key, __this__success_copy_value);
} 
this.success = __this__success;
} 
}

public getOptions_result deepCopy() {
return new getOptions_result(this);
}

public void clear() {
this.success = null;
}

public int getSuccessSize() {
return (this.success == null) ? 0 : this.success.size();
}

public void putToSuccess(String key, String val) {
if (this.success == null) {
this.success = new HashMap<String, String>();
}
this.success.put(key, val);
}

public Map<String, String> getSuccess() {
return this.success;
}

public getOptions_result setSuccess(Map<String, String> success) {
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
setSuccess((Map<String, String>)value);
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
if (that instanceof getOptions_result)
return equals((getOptions_result)that); 
return false;
}

public boolean equals(getOptions_result that) {
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

public int compareTo(getOptions_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getOptions_result typedOther = other;

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
if (field.type == 13) {

TMap _map5 = iprot.readMapBegin();
this.success = new HashMap<String, String>(2 * _map5.size);
for (int _i6 = 0; _i6 < _map5.size; _i6++) {

String _key7 = iprot.readString();
String _val8 = iprot.readString();
this.success.put(_key7, _val8);
} 
iprot.readMapEnd();
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
oprot.writeStructBegin(STRUCT_DESC);

if (isSetSuccess()) {
oprot.writeFieldBegin(SUCCESS_FIELD_DESC);

oprot.writeMapBegin(new TMap((byte)11, (byte)11, this.success.size()));
for (Map.Entry<String, String> _iter9 : this.success.entrySet()) {

oprot.writeString(_iter9.getKey());
oprot.writeString(_iter9.getValue());
} 
oprot.writeMapEnd();

oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getOptions_result(");
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

public static class getCpuProfile_args
implements TBase<getCpuProfile_args, getCpuProfile_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("getCpuProfile_args");

private static final TField PROFILE_DURATION_IN_SEC_FIELD_DESC = new TField("profileDurationInSec", (byte)8, (short)1);
public int profileDurationInSec;
private static final int __PROFILEDURATIONINSEC_ISSET_ID = 0;

public enum _Fields
implements TFieldIdEnum {
PROFILE_DURATION_IN_SEC((short)1, "profileDurationInSec"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 1:
return PROFILE_DURATION_IN_SEC;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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

private BitSet __isset_bit_vector = new BitSet(1);
public static final Map<_Fields, FieldMetaData> metaDataMap;

static {
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.PROFILE_DURATION_IN_SEC, new FieldMetaData("profileDurationInSec", (byte)3, new FieldValueMetaData((byte)8)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getCpuProfile_args.class, metaDataMap);
}

public getCpuProfile_args(int profileDurationInSec) {
this();
this.profileDurationInSec = profileDurationInSec;
setProfileDurationInSecIsSet(true);
}

public getCpuProfile_args(getCpuProfile_args other) {
this.__isset_bit_vector.clear();
this.__isset_bit_vector.or(other.__isset_bit_vector);
this.profileDurationInSec = other.profileDurationInSec;
}

public getCpuProfile_args deepCopy() {
return new getCpuProfile_args(this);
}

public void clear() {
setProfileDurationInSecIsSet(false);
this.profileDurationInSec = 0;
}

public int getProfileDurationInSec() {
return this.profileDurationInSec;
}

public getCpuProfile_args setProfileDurationInSec(int profileDurationInSec) {
this.profileDurationInSec = profileDurationInSec;
setProfileDurationInSecIsSet(true);
return this;
}

public void unsetProfileDurationInSec() {
this.__isset_bit_vector.clear(0);
}

public boolean isSetProfileDurationInSec() {
return this.__isset_bit_vector.get(0);
}

public void setProfileDurationInSecIsSet(boolean value) {
this.__isset_bit_vector.set(0, value);
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetProfileDurationInSec(); break;
} 
setProfileDurationInSec(((Integer)value).intValue());
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return new Integer(getProfileDurationInSec());
} 

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

switch (field) {
case null:
return isSetProfileDurationInSec();
} 
throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof getCpuProfile_args)
return equals((getCpuProfile_args)that); 
return false;
}

public boolean equals(getCpuProfile_args that) {
if (that == null) {
return false;
}
boolean this_present_profileDurationInSec = true;
boolean that_present_profileDurationInSec = true;
if (this_present_profileDurationInSec || that_present_profileDurationInSec) {
if (!this_present_profileDurationInSec || !that_present_profileDurationInSec)
return false; 
if (this.profileDurationInSec != that.profileDurationInSec) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(getCpuProfile_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getCpuProfile_args typedOther = other;

lastComparison = Boolean.valueOf(isSetProfileDurationInSec()).compareTo(Boolean.valueOf(typedOther.isSetProfileDurationInSec()));
if (lastComparison != 0) {
return lastComparison;
}
if (isSetProfileDurationInSec()) {
lastComparison = TBaseHelper.compareTo(this.profileDurationInSec, typedOther.profileDurationInSec);
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
if (field.type == 8) {
this.profileDurationInSec = iprot.readI32();
setProfileDurationInSecIsSet(true); break;
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
oprot.writeFieldBegin(PROFILE_DURATION_IN_SEC_FIELD_DESC);
oprot.writeI32(this.profileDurationInSec);
oprot.writeFieldEnd();
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getCpuProfile_args(");
boolean first = true;

sb.append("profileDurationInSec:");
sb.append(this.profileDurationInSec);
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}

public getCpuProfile_args() {}
}

public static class getCpuProfile_result
implements TBase<getCpuProfile_result, getCpuProfile_result._Fields>, Serializable, Cloneable {
private static final TStruct STRUCT_DESC = new TStruct("getCpuProfile_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
public String success;
public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(getCpuProfile_result.class, metaDataMap);
}

public getCpuProfile_result() {}

public getCpuProfile_result(String success) {
this();
this.success = success;
}

public getCpuProfile_result(getCpuProfile_result other) {
if (other.isSetSuccess()) {
this.success = other.success;
}
}

public getCpuProfile_result deepCopy() {
return new getCpuProfile_result(this);
}

public void clear() {
this.success = null;
}

public String getSuccess() {
return this.success;
}

public getCpuProfile_result setSuccess(String success) {
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
setSuccess((String)value);
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
if (that instanceof getCpuProfile_result)
return equals((getCpuProfile_result)that); 
return false;
}

public boolean equals(getCpuProfile_result that) {
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

public int compareTo(getCpuProfile_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
getCpuProfile_result typedOther = other;

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
if (field.type == 11) {
this.success = iprot.readString(); break;
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
oprot.writeString(this.success);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("getCpuProfile_result(");
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

public static class aliveSince_args
implements TBase<aliveSince_args, aliveSince_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("aliveSince_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(aliveSince_args.class, metaDataMap);
}

public aliveSince_args() {}

public aliveSince_args(aliveSince_args other) {}

public aliveSince_args deepCopy() {
return new aliveSince_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$aliveSince_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$aliveSince_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$aliveSince_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof aliveSince_args)
return equals((aliveSince_args)that); 
return false;
}

public boolean equals(aliveSince_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(aliveSince_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
aliveSince_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("aliveSince_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class aliveSince_result
implements TBase<aliveSince_result, aliveSince_result._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("aliveSince_result");

private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)10, (short)0);
public long success;
private static final int __SUCCESS_ISSET_ID = 0;

public enum _Fields
implements TFieldIdEnum {
SUCCESS((short)0, "success"); private final String _fieldName;
private final short _thriftId;
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {
case 0:
return SUCCESS;
} 
return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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

private BitSet __isset_bit_vector = new BitSet(1);
public static final Map<_Fields, FieldMetaData> metaDataMap;

static {
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)10)));

metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(aliveSince_result.class, metaDataMap);
}

public aliveSince_result(long success) {
this();
this.success = success;
setSuccessIsSet(true);
}

public aliveSince_result(aliveSince_result other) {
this.__isset_bit_vector.clear();
this.__isset_bit_vector.or(other.__isset_bit_vector);
this.success = other.success;
}

public aliveSince_result deepCopy() {
return new aliveSince_result(this);
}

public void clear() {
setSuccessIsSet(false);
this.success = 0L;
}

public long getSuccess() {
return this.success;
}

public aliveSince_result setSuccess(long success) {
this.success = success;
setSuccessIsSet(true);
return this;
}

public void unsetSuccess() {
this.__isset_bit_vector.clear(0);
}

public boolean isSetSuccess() {
return this.__isset_bit_vector.get(0);
}

public void setSuccessIsSet(boolean value) {
this.__isset_bit_vector.set(0, value);
}

public void setFieldValue(_Fields field, Object value) {
switch (field) {
case null:
if (value == null) {
unsetSuccess(); break;
} 
setSuccess(((Long)value).longValue());
break;
} 
}

public Object getFieldValue(_Fields field) {
switch (field) {
case null:
return new Long(getSuccess());
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
if (that instanceof aliveSince_result)
return equals((aliveSince_result)that); 
return false;
}

public boolean equals(aliveSince_result that) {
if (that == null) {
return false;
}
boolean this_present_success = true;
boolean that_present_success = true;
if (this_present_success || that_present_success) {
if (!this_present_success || !that_present_success)
return false; 
if (this.success != that.success) {
return false;
}
} 
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(aliveSince_result other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
aliveSince_result typedOther = other;

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
if (field.type == 10) {
this.success = iprot.readI64();
setSuccessIsSet(true); break;
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
oprot.writeI64(this.success);
oprot.writeFieldEnd();
} 
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("aliveSince_result(");
boolean first = true;

sb.append("success:");
sb.append(this.success);
first = false;
sb.append(")");
return sb.toString();
}

public void validate() throws TException {}

public aliveSince_result() {}
}

public static class reinitialize_args
implements TBase<reinitialize_args, reinitialize_args._Fields>, Serializable, Cloneable {
private static final TStruct STRUCT_DESC = new TStruct("reinitialize_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(reinitialize_args.class, metaDataMap);
}

public reinitialize_args() {}

public reinitialize_args(reinitialize_args other) {}

public reinitialize_args deepCopy() {
return new reinitialize_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$reinitialize_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$reinitialize_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$reinitialize_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof reinitialize_args)
return equals((reinitialize_args)that); 
return false;
}

public boolean equals(reinitialize_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(reinitialize_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
reinitialize_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("reinitialize_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}

public static class shutdown_args
implements TBase<shutdown_args, shutdown_args._Fields>, Serializable, Cloneable
{
private static final TStruct STRUCT_DESC = new TStruct("shutdown_args");

public static final Map<_Fields, FieldMetaData> metaDataMap;

public enum _Fields
implements TFieldIdEnum
{
private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;

static {
for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
byName.put(field.getFieldName(), field);
}
}

public static _Fields findByThriftId(int fieldId) {
switch (fieldId) {

}  return null;
}

public static _Fields findByThriftIdOrThrow(int fieldId) {
_Fields fields = findByThriftId(fieldId);
if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
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
Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
metaDataMap = Collections.unmodifiableMap(tmpMap);
FieldMetaData.addStructMetaDataMap(shutdown_args.class, metaDataMap);
}

public shutdown_args() {}

public shutdown_args(shutdown_args other) {}

public shutdown_args deepCopy() {
return new shutdown_args(this);
}

public void clear() {}

public void setFieldValue(_Fields field, Object value) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$shutdown_args$_Fields[field.ordinal()];
}

public Object getFieldValue(_Fields field) {
FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$shutdown_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean isSet(_Fields field) {
if (field == null) {
throw new IllegalArgumentException();
}

FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$shutdown_args$_Fields[field.ordinal()];

throw new IllegalStateException();
}

public boolean equals(Object that) {
if (that == null)
return false; 
if (that instanceof shutdown_args)
return equals((shutdown_args)that); 
return false;
}

public boolean equals(shutdown_args that) {
if (that == null) {
return false;
}
return true;
}

public int hashCode() {
return 0;
}

public int compareTo(shutdown_args other) {
if (!getClass().equals(other.getClass())) {
return getClass().getName().compareTo(other.getClass().getName());
}

int lastComparison = 0;
shutdown_args typedOther = other;

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

}  TProtocolUtil.skip(iprot, field.type);

iprot.readFieldEnd();
} 
iprot.readStructEnd();

validate();
}

public void write(TProtocol oprot) throws TException {
validate();

oprot.writeStructBegin(STRUCT_DESC);
oprot.writeFieldStop();
oprot.writeStructEnd();
}

public String toString() {
StringBuilder sb = new StringBuilder("shutdown_args(");
boolean first = true;

sb.append(")");
return sb.toString();
}

public void validate() throws TException {}
}
}

