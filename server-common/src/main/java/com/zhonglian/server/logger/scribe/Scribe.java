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
import org.apache.thrift.async.TAsyncMethodCall.State;
import org.apache.thrift.meta_data.EnumMetaData;
import org.apache.thrift.meta_data.FieldMetaData;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scribe {
    public static class AsyncClient extends FacebookService.AsyncClient implements Scribe.AsyncIface {
        public AsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager clientManager, TNonblockingTransport transport) {
            super(protocolFactory, clientManager, transport);
        }

        @Override
        public void Log(List<LogEntry> messages, AsyncMethodCallback<Scribe.AsyncClient.Log_call> resultHandler) throws TException {
            this.checkReady();
            Scribe.AsyncClient.Log_call method_call = new Scribe.AsyncClient.Log_call(messages, resultHandler, this, this.protocolFactory, this.transport);
            this.manager.call(method_call);
        }

        public static class Factory implements TAsyncClientFactory<Scribe.AsyncClient> {
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

        public static class Log_call extends TAsyncMethodCall {
            private List<LogEntry> messages;

            public Log_call(
                List<LogEntry> messages,
                AsyncMethodCallback<Scribe.AsyncClient.Log_call> resultHandler,
                TAsyncClient client,
                TProtocolFactory protocolFactory,
                TNonblockingTransport transport
            ) throws TException {
                super(client, protocolFactory, transport, resultHandler, false);
                this.messages = messages;
            }

            @Override
            public void write_args(TProtocol prot) throws TException {
                prot.writeMessageBegin(new TMessage("Log", (byte)1, 0));
                Scribe.Log_args args = new Scribe.Log_args();
                args.setMessages(this.messages);
                args.write(prot);
                prot.writeMessageEnd();
            }

            public ResultCode getResult() throws TException {
                if (this.getState() != State.RESPONSE_READ) {
                    throw new IllegalStateException("Method call not finished!");
                } else {
                    TMemoryInputTransport memoryTransport = new TMemoryInputTransport(this.getFrameBuffer().array());
                    TProtocol prot = this.client.getProtocolFactory().getProtocol(memoryTransport);
                    return new Scribe.Client(prot).recv_Log();
                }
            }
        }
    }

    public interface AsyncIface extends FacebookService.AsyncIface {
        void Log(List<LogEntry> var1, AsyncMethodCallback<Scribe.AsyncClient.Log_call> var2) throws TException;
    }

    public static class Client extends FacebookService.Client implements TServiceClient, Scribe.Iface {
        public Client(TProtocol prot) {
            this(prot, prot);
        }

        public Client(TProtocol iprot, TProtocol oprot) {
            super(iprot, oprot);
        }

        @Override
        public ResultCode Log(List<LogEntry> messages) throws TException {
            this.send_Log(messages);
            return this.recv_Log();
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
            } else if (msg.seqid != this.seqid_) {
                throw new TApplicationException(4, "Log failed: out of sequence response");
            } else {
                Scribe.Log_result result = new Scribe.Log_result();
                result.read(this.iprot_);
                this.iprot_.readMessageEnd();
                if (result.isSetSuccess()) {
                    return result.success;
                } else {
                    throw new TApplicationException(5, "Log failed: unknown result");
                }
            }
        }

        public static class Factory implements TServiceClientFactory<Scribe.Client> {
            public Scribe.Client getClient(TProtocol prot) {
                return new Scribe.Client(prot);
            }

            public Scribe.Client getClient(TProtocol iprot, TProtocol oprot) {
                return new Scribe.Client(iprot, oprot);
            }
        }
    }

    public interface Iface extends FacebookService.Iface {
        ResultCode Log(List<LogEntry> var1) throws TException;
    }

    public static class Log_args implements TBase<Scribe.Log_args, Scribe.Log_args._Fields>, Serializable, Cloneable {
        private static final long serialVersionUID = -4861100422505147853L;
        private static final TStruct STRUCT_DESC = new TStruct("Log_args");
        private static final TField MESSAGES_FIELD_DESC = new TField("messages", (byte)15, (short)1);
        public List<LogEntry> messages;
        public static final Map<Scribe.Log_args._Fields, FieldMetaData> metaDataMap;

        static {
            Map<Scribe.Log_args._Fields, FieldMetaData> tmpMap = new EnumMap<>(Scribe.Log_args._Fields.class);
            tmpMap.put(
                Scribe.Log_args._Fields.MESSAGES,
                new FieldMetaData("messages", (byte)3, new ListMetaData((byte)15, new StructMetaData((byte)12, LogEntry.class)))
            );
            metaDataMap = Collections.unmodifiableMap(tmpMap);
            FieldMetaData.addStructMetaDataMap(Scribe.Log_args.class, metaDataMap);
        }

        public Log_args() {
        }

        public Log_args(List<LogEntry> messages) {
            this();
            this.messages = messages;
        }

        public Log_args(Scribe.Log_args other) {
            if (other.isSetMessages()) {
                List<LogEntry> __this__messages = new ArrayList<>();

                for (LogEntry other_element : other.messages) {
                    __this__messages.add(new LogEntry(other_element));
                }

                this.messages = __this__messages;
            }
        }

        public Scribe.Log_args deepCopy() {
            return new Scribe.Log_args(this);
        }

        @Override
        public void clear() {
            this.messages = null;
        }

        public int getMessagesSize() {
            return this.messages == null ? 0 : this.messages.size();
        }

        public Iterator<LogEntry> getMessagesIterator() {
            return this.messages == null ? null : this.messages.iterator();
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

        public Scribe.Log_args setMessages(List<LogEntry> messages) {
            this.messages = messages;
            return this;
        }

        public void unsetMessages() {
            this.messages = null;
        }

        public boolean isSetMessages() {
            return this.messages != null;
        }

        public void setMessagesIsSet(boolean value) {
            if (!value) {
                this.messages = null;
            }
        }

        public void setFieldValue(Scribe.Log_args._Fields field, Object value) {
            switch (field) {
                case MESSAGES:
                    if (value == null) {
                        this.unsetMessages();
                    } else {
                        this.setMessages((List<LogEntry>)value);
                    }
            }
        }

        public Object getFieldValue(Scribe.Log_args._Fields field) {
            switch (field) {
                case MESSAGES:
                    return this.getMessages();
                default:
                    throw new IllegalStateException();
            }
        }

        public boolean isSet(Scribe.Log_args._Fields field) {
            if (field == null) {
                throw new IllegalArgumentException();
            } else {
                switch (field) {
                    case MESSAGES:
                        return this.isSetMessages();
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
                return that instanceof Scribe.Log_args ? this.equals((Scribe.Log_args)that) : false;
            }
        }

        public boolean equals(Scribe.Log_args that) {
            if (that == null) {
                return false;
            } else {
                boolean this_present_messages = this.isSetMessages();
                boolean that_present_messages = that.isSetMessages();
                if (this_present_messages || that_present_messages) {
                    if (!this_present_messages || !that_present_messages) {
                        return false;
                    }

                    if (!this.messages.equals(that.messages)) {
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

        public int compareTo(Scribe.Log_args other) {
            if (!this.getClass().equals(other.getClass())) {
                return this.getClass().getName().compareTo(other.getClass().getName());
            } else {
                int lastComparison = 0;
                lastComparison = Boolean.valueOf(this.isSetMessages()).compareTo(other.isSetMessages());
                if (lastComparison != 0) {
                    return lastComparison;
                } else {
                    if (this.isSetMessages()) {
                        lastComparison = TBaseHelper.compareTo(this.messages, other.messages);
                        if (lastComparison != 0) {
                            return lastComparison;
                        }
                    }

                    return 0;
                }
            }
        }

        public Scribe.Log_args._Fields fieldForId(int fieldId) {
            return Scribe.Log_args._Fields.findByThriftId(fieldId);
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
                        if (field.type != 15) {
                            TProtocolUtil.skip(iprot, field.type);
                            break;
                        }

                        TList _list0 = iprot.readListBegin();
                        this.messages = new ArrayList<>(_list0.size);

                        for (int _i1 = 0; _i1 < _list0.size; _i1++) {
                            LogEntry _elem2 = new LogEntry();
                            _elem2.read(iprot);
                            this.messages.add(_elem2);
                        }

                        iprot.readListEnd();
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

        @Override
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

        public void validate() throws TException {
        }

        public static enum _Fields implements TFieldIdEnum {
            MESSAGES((short)1, "messages");

            private static final Map<String, Scribe.Log_args._Fields> byName = new ConcurrentHashMap<>();
            private final short _thriftId;
            private final String _fieldName;

            static {
                for (Scribe.Log_args._Fields field : EnumSet.allOf(Scribe.Log_args._Fields.class)) {
                    byName.put(field.getFieldName(), field);
                }
            }

            public static Scribe.Log_args._Fields findByThriftId(int fieldId) {
                switch (fieldId) {
                    case 1:
                        return MESSAGES;
                    default:
                        return null;
                }
            }

            public static Scribe.Log_args._Fields findByThriftIdOrThrow(int fieldId) {
                Scribe.Log_args._Fields fields = findByThriftId(fieldId);
                if (fields == null) {
                    throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
                } else {
                    return fields;
                }
            }

            public static Scribe.Log_args._Fields findByName(String name) {
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

    public static class Log_result implements TBase<Scribe.Log_result, Scribe.Log_result._Fields>, Serializable, Cloneable {
        private static final TStruct STRUCT_DESC = new TStruct("Log_result");
        private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)8, (short)0);
        public ResultCode success;
        public static final Map<Scribe.Log_result._Fields, FieldMetaData> metaDataMap;

        static {
            Map<Scribe.Log_result._Fields, FieldMetaData> tmpMap = new EnumMap<>(Scribe.Log_result._Fields.class);
            tmpMap.put(Scribe.Log_result._Fields.SUCCESS, new FieldMetaData("success", (byte)3, new EnumMetaData((byte)16, ResultCode.class)));
            metaDataMap = Collections.unmodifiableMap(tmpMap);
            FieldMetaData.addStructMetaDataMap(Scribe.Log_result.class, metaDataMap);
        }

        public Log_result() {
        }

        public Log_result(ResultCode success) {
            this();
            this.success = success;
        }

        public Log_result(Scribe.Log_result other) {
            if (other.isSetSuccess()) {
                this.success = other.success;
            }
        }

        public Scribe.Log_result deepCopy() {
            return new Scribe.Log_result(this);
        }

        @Override
        public void clear() {
            this.success = null;
        }

        public ResultCode getSuccess() {
            return this.success;
        }

        public Scribe.Log_result setSuccess(ResultCode success) {
            this.success = success;
            return this;
        }

        public void unsetSuccess() {
            this.success = null;
        }

        public boolean isSetSuccess() {
            return this.success != null;
        }

        public void setSuccessIsSet(boolean value) {
            if (!value) {
                this.success = null;
            }
        }

        public void setFieldValue(Scribe.Log_result._Fields field, Object value) {
            switch (field) {
                case SUCCESS:
                    if (value == null) {
                        this.unsetSuccess();
                    } else {
                        this.setSuccess((ResultCode)value);
                    }
            }
        }

        public Object getFieldValue(Scribe.Log_result._Fields field) {
            switch (field) {
                case SUCCESS:
                    return this.getSuccess();
                default:
                    throw new IllegalStateException();
            }
        }

        public boolean isSet(Scribe.Log_result._Fields field) {
            if (field == null) {
                throw new IllegalArgumentException();
            } else {
                switch (field) {
                    case SUCCESS:
                        return this.isSetSuccess();
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
                return that instanceof Scribe.Log_result ? this.equals((Scribe.Log_result)that) : false;
            }
        }

        public boolean equals(Scribe.Log_result that) {
            if (that == null) {
                return false;
            } else {
                boolean this_present_success = this.isSetSuccess();
                boolean that_present_success = that.isSetSuccess();
                if (this_present_success || that_present_success) {
                    if (!this_present_success || !that_present_success) {
                        return false;
                    }

                    if (!this.success.equals(that.success)) {
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

        public int compareTo(Scribe.Log_result other) {
            if (!this.getClass().equals(other.getClass())) {
                return this.getClass().getName().compareTo(other.getClass().getName());
            } else {
                int lastComparison = 0;
                lastComparison = Boolean.valueOf(this.isSetSuccess()).compareTo(other.isSetSuccess());
                if (lastComparison != 0) {
                    return lastComparison;
                } else {
                    if (this.isSetSuccess()) {
                        lastComparison = TBaseHelper.compareTo((Comparable)this.success, (Comparable)other.success);
                        if (lastComparison != 0) {
                            return lastComparison;
                        }
                    }

                    return 0;
                }
            }
        }

        public Scribe.Log_result._Fields fieldForId(int fieldId) {
            return Scribe.Log_result._Fields.findByThriftId(fieldId);
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
                    case 0:
                        if (field.type == 8) {
                            this.success = ResultCode.findByValue(iprot.readI32());
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
            oprot.writeStructBegin(STRUCT_DESC);
            if (this.isSetSuccess()) {
                oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
                oprot.writeI32(this.success.getValue());
                oprot.writeFieldEnd();
            }

            oprot.writeFieldStop();
            oprot.writeStructEnd();
        }

        @Override
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

        public void validate() throws TException {
        }

        public static enum _Fields implements TFieldIdEnum {
            SUCCESS((short)0, "success");

            private static final Map<String, Scribe.Log_result._Fields> byName = new ConcurrentHashMap<>();
            private final short _thriftId;
            private final String _fieldName;

            static {
                for (Scribe.Log_result._Fields field : EnumSet.allOf(Scribe.Log_result._Fields.class)) {
                    byName.put(field.getFieldName(), field);
                }
            }

            public static Scribe.Log_result._Fields findByThriftId(int fieldId) {
                switch (fieldId) {
                    case 0:
                        return SUCCESS;
                    default:
                        return null;
                }
            }

            public static Scribe.Log_result._Fields findByThriftIdOrThrow(int fieldId) {
                Scribe.Log_result._Fields fields = findByThriftId(fieldId);
                if (fields == null) {
                    throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
                } else {
                    return fields;
                }
            }

            public static Scribe.Log_result._Fields findByName(String name) {
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

    public static class Processor extends FacebookService.Processor implements TProcessor {
        private static final Logger LOGGER = LoggerFactory.getLogger(Scribe.Processor.class.getName());
        private Scribe.Iface iface_;

        public Processor(Scribe.Iface iface) {
            super(iface);
            this.iface_ = iface;
            this.processMap_.put("Log", new Log());
        }

        @Override
        public boolean process(TProtocol iprot, TProtocol oprot) throws TException {
            TMessage msg = iprot.readMessageBegin();
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
            } else {
                fn.process(msg.seqid, iprot, oprot);
                return true;
            }
        }

        private class Log implements ProcessFunction {
            private Log() {
            }

            @Override
            public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
                Scribe.Log_args args = new Scribe.Log_args();

                try {
                    args.read(iprot);
                } catch (TProtocolException var7) {
                    iprot.readMessageEnd();
                    TApplicationException x = new TApplicationException(7, var7.getMessage());
                    oprot.writeMessageBegin(new TMessage("Log", (byte)3, seqid));
                    x.write(oprot);
                    oprot.writeMessageEnd();
                    oprot.getTransport().flush();
                    return;
                }

                iprot.readMessageEnd();
                Scribe.Log_result result = new Scribe.Log_result();
                result.success = Processor.this.iface_.Log(args.messages);
                oprot.writeMessageBegin(new TMessage("Log", (byte)2, seqid));
                result.write(oprot);
                oprot.writeMessageEnd();
                oprot.getTransport().flush();
            }
        }
    }
}
