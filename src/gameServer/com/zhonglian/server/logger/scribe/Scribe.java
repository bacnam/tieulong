/*     */ package com.zhonglian.server.logger.scribe;
/*     */ 
/*     */ import com.facebook.fb303.FacebookService;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.thrift.TApplicationException;
/*     */ import org.apache.thrift.TBase;
/*     */ import org.apache.thrift.TBaseHelper;
/*     */ import org.apache.thrift.TException;
/*     */ import org.apache.thrift.TFieldIdEnum;
/*     */ import org.apache.thrift.TProcessor;
/*     */ import org.apache.thrift.TServiceClient;
/*     */ import org.apache.thrift.TServiceClientFactory;
/*     */ import org.apache.thrift.async.AsyncMethodCallback;
/*     */ import org.apache.thrift.async.TAsyncClient;
/*     */ import org.apache.thrift.async.TAsyncClientFactory;
/*     */ import org.apache.thrift.async.TAsyncClientManager;
/*     */ import org.apache.thrift.async.TAsyncMethodCall;
/*     */ import org.apache.thrift.meta_data.EnumMetaData;
/*     */ import org.apache.thrift.meta_data.FieldMetaData;
/*     */ import org.apache.thrift.meta_data.FieldValueMetaData;
/*     */ import org.apache.thrift.meta_data.ListMetaData;
/*     */ import org.apache.thrift.meta_data.StructMetaData;
/*     */ import org.apache.thrift.protocol.TField;
/*     */ import org.apache.thrift.protocol.TList;
/*     */ import org.apache.thrift.protocol.TMessage;
/*     */ import org.apache.thrift.protocol.TProtocol;
/*     */ import org.apache.thrift.protocol.TProtocolException;
/*     */ import org.apache.thrift.protocol.TProtocolFactory;
/*     */ import org.apache.thrift.protocol.TProtocolUtil;
/*     */ import org.apache.thrift.protocol.TStruct;
/*     */ import org.apache.thrift.transport.TMemoryInputTransport;
/*     */ import org.apache.thrift.transport.TNonblockingTransport;
/*     */ import org.apache.thrift.transport.TTransport;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Scribe
/*     */ {
/*     */   public static class Client
/*     */     extends FacebookService.Client
/*     */     implements TServiceClient, Iface
/*     */   {
/*     */     public static class Factory
/*     */       implements TServiceClientFactory<Client>
/*     */     {
/*     */       public Scribe.Client getClient(TProtocol prot) {
/*  69 */         return new Scribe.Client(prot);
/*     */       }
/*     */       
/*     */       public Scribe.Client getClient(TProtocol iprot, TProtocol oprot) {
/*  73 */         return new Scribe.Client(iprot, oprot);
/*     */       }
/*     */     }
/*     */     
/*     */     public Client(TProtocol prot) {
/*  78 */       this(prot, prot);
/*     */     }
/*     */     
/*     */     public Client(TProtocol iprot, TProtocol oprot) {
/*  82 */       super(iprot, oprot);
/*     */     }
/*     */     
/*     */     public ResultCode Log(List<LogEntry> messages) throws TException {
/*  86 */       send_Log(messages);
/*  87 */       return recv_Log();
/*     */     }
/*     */     
/*     */     public void send_Log(List<LogEntry> messages) throws TException {
/*  91 */       this.oprot_.writeMessageBegin(new TMessage("Log", (byte)1, ++this.seqid_));
/*  92 */       Scribe.Log_args args = new Scribe.Log_args();
/*  93 */       args.setMessages(messages);
/*  94 */       args.write(this.oprot_);
/*  95 */       this.oprot_.writeMessageEnd();
/*  96 */       this.oprot_.getTransport().flush();
/*     */     }
/*     */     
/*     */     public ResultCode recv_Log() throws TException {
/* 100 */       TMessage msg = this.iprot_.readMessageBegin();
/* 101 */       if (msg.type == 3) {
/* 102 */         TApplicationException x = TApplicationException.read(this.iprot_);
/* 103 */         this.iprot_.readMessageEnd();
/* 104 */         throw x;
/*     */       } 
/* 106 */       if (msg.seqid != this.seqid_) {
/* 107 */         throw new TApplicationException(4, "Log failed: out of sequence response");
/*     */       }
/* 109 */       Scribe.Log_result result = new Scribe.Log_result();
/* 110 */       result.read(this.iprot_);
/* 111 */       this.iprot_.readMessageEnd();
/* 112 */       if (result.isSetSuccess()) {
/* 113 */         return result.success;
/*     */       }
/* 115 */       throw new TApplicationException(5, "Log failed: unknown result");
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AsyncClient
/*     */     extends FacebookService.AsyncClient implements AsyncIface {
/*     */     public static class Factory implements TAsyncClientFactory<AsyncClient> {
/*     */       private TAsyncClientManager clientManager;
/*     */       private TProtocolFactory protocolFactory;
/*     */       
/*     */       public Factory(TAsyncClientManager clientManager, TProtocolFactory protocolFactory) {
/* 126 */         this.clientManager = clientManager;
/* 127 */         this.protocolFactory = protocolFactory;
/*     */       }
/*     */       
/*     */       public Scribe.AsyncClient getAsyncClient(TNonblockingTransport transport) {
/* 131 */         return new Scribe.AsyncClient(this.protocolFactory, this.clientManager, transport);
/*     */       }
/*     */     }
/*     */     
/*     */     public AsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager clientManager, TNonblockingTransport transport) {
/* 136 */       super(protocolFactory, clientManager, transport);
/*     */     }
/*     */     
/*     */     public void Log(List<LogEntry> messages, AsyncMethodCallback<Log_call> resultHandler) throws TException {
/* 140 */       checkReady();
/* 141 */       Log_call method_call = new Log_call(messages, resultHandler, (TAsyncClient)this, this.protocolFactory, this.transport);
/* 142 */       this.manager.call(method_call);
/*     */     }
/*     */     
/*     */     public static class Log_call
/*     */       extends TAsyncMethodCall
/*     */     {
/*     */       private List<LogEntry> messages;
/*     */       
/*     */       public Log_call(List<LogEntry> messages, AsyncMethodCallback<Log_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/* 151 */         super(client, protocolFactory, transport, resultHandler, false);
/* 152 */         this.messages = messages;
/*     */       }
/*     */       
/*     */       public void write_args(TProtocol prot) throws TException {
/* 156 */         prot.writeMessageBegin(new TMessage("Log", (byte)1, 0));
/* 157 */         Scribe.Log_args args = new Scribe.Log_args();
/* 158 */         args.setMessages(this.messages);
/* 159 */         args.write(prot);
/* 160 */         prot.writeMessageEnd();
/*     */       }
/*     */       
/*     */       public ResultCode getResult() throws TException {
/* 164 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/* 165 */           throw new IllegalStateException("Method call not finished!");
/*     */         }
/* 167 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/* 168 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/* 169 */         return (new Scribe.Client(prot)).recv_Log();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Processor
/*     */     extends FacebookService.Processor
/*     */     implements TProcessor {
/* 177 */     private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
/*     */     
/*     */     public Processor(Scribe.Iface iface) {
/* 180 */       super(iface);
/* 181 */       this.iface_ = iface;
/* 182 */       this.processMap_.put("Log", new Log(null));
/*     */     }
/*     */     
/*     */     private Scribe.Iface iface_;
/*     */     
/*     */     public boolean process(TProtocol iprot, TProtocol oprot) throws TException {
/* 188 */       TMessage msg = iprot.readMessageBegin();
/* 189 */       FacebookService.Processor.ProcessFunction fn = (FacebookService.Processor.ProcessFunction)this.processMap_.get(msg.name);
/* 190 */       if (fn == null) {
/* 191 */         TProtocolUtil.skip(iprot, (byte)12);
/* 192 */         iprot.readMessageEnd();
/* 193 */         TApplicationException x = new TApplicationException(1, "Invalid method name: '" + msg.name + "'");
/* 194 */         oprot.writeMessageBegin(new TMessage(msg.name, (byte)3, msg.seqid));
/* 195 */         x.write(oprot);
/* 196 */         oprot.writeMessageEnd();
/* 197 */         oprot.getTransport().flush();
/* 198 */         return true;
/*     */       } 
/* 200 */       fn.process(msg.seqid, iprot, oprot);
/* 201 */       return true;
/*     */     }
/*     */     
/*     */     private class Log implements FacebookService.Processor.ProcessFunction {
/*     */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 206 */         Scribe.Log_args args = new Scribe.Log_args();
/*     */         try {
/* 208 */           args.read(iprot);
/* 209 */         } catch (TProtocolException e) {
/* 210 */           iprot.readMessageEnd();
/* 211 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 212 */           oprot.writeMessageBegin(new TMessage("Log", (byte)3, seqid));
/* 213 */           x.write(oprot);
/* 214 */           oprot.writeMessageEnd();
/* 215 */           oprot.getTransport().flush();
/*     */           return;
/*     */         } 
/* 218 */         iprot.readMessageEnd();
/* 219 */         Scribe.Log_result result = new Scribe.Log_result();
/* 220 */         result.success = Scribe.Processor.this.iface_.Log(args.messages);
/* 221 */         oprot.writeMessageBegin(new TMessage("Log", (byte)2, seqid));
/* 222 */         result.write(oprot);
/* 223 */         oprot.writeMessageEnd();
/* 224 */         oprot.getTransport().flush();
/*     */       }
/*     */ 
/*     */       
/*     */       private Log() {}
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Log_args
/*     */     implements TBase<Log_args, Log_args._Fields>, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -4861100422505147853L;
/* 237 */     private static final TStruct STRUCT_DESC = new TStruct("Log_args");
/*     */     
/* 239 */     private static final TField MESSAGES_FIELD_DESC = new TField("messages", (byte)15, (short)1);
/*     */     public List<LogEntry> messages;
/*     */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*     */     
/*     */     public enum _Fields
/*     */       implements TFieldIdEnum {
/* 245 */       MESSAGES((short)1, "messages");
/*     */       
/* 247 */       private static final Map<String, _Fields> byName = new ConcurrentHashMap<>();
/*     */       
/*     */       static {
/* 250 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 251 */           byName.put(field.getFieldName(), field);
/*     */         }
/*     */       }
/*     */       
/*     */       private final short _thriftId;
/*     */       private final String _fieldName;
/*     */       
/*     */       public static _Fields findByThriftId(int fieldId) {
/* 259 */         switch (fieldId) {
/*     */           case 1:
/* 261 */             return MESSAGES;
/*     */         } 
/* 263 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 272 */         _Fields fields = findByThriftId(fieldId);
/* 273 */         if (fields == null)
/* 274 */           throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 275 */         return fields;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public static _Fields findByName(String name) {
/* 282 */         return byName.get(name);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       _Fields(short thriftId, String fieldName) {
/* 289 */         this._thriftId = thriftId;
/* 290 */         this._fieldName = fieldName;
/*     */       }
/*     */       
/*     */       public short getThriftFieldId() {
/* 294 */         return this._thriftId;
/*     */       }
/*     */       
/*     */       public String getFieldName() {
/* 298 */         return this._fieldName;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 306 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<>(_Fields.class);
/* 307 */       tmpMap.put(_Fields.MESSAGES, new FieldMetaData("messages", (byte)3, 
/* 308 */             (FieldValueMetaData)new ListMetaData((byte)15, (FieldValueMetaData)new StructMetaData((byte)12, LogEntry.class))));
/* 309 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 310 */       FieldMetaData.addStructMetaDataMap(Log_args.class, metaDataMap);
/*     */     }
/*     */ 
/*     */     
/*     */     public Log_args() {}
/*     */     
/*     */     public Log_args(List<LogEntry> messages) {
/* 317 */       this();
/* 318 */       this.messages = messages;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Log_args(Log_args other) {
/* 325 */       if (other.isSetMessages()) {
/* 326 */         List<LogEntry> __this__messages = new ArrayList<>();
/* 327 */         for (LogEntry other_element : other.messages) {
/* 328 */           __this__messages.add(new LogEntry(other_element));
/*     */         }
/* 330 */         this.messages = __this__messages;
/*     */       } 
/*     */     }
/*     */     
/*     */     public Log_args deepCopy() {
/* 335 */       return new Log_args(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 340 */       this.messages = null;
/*     */     }
/*     */     
/*     */     public int getMessagesSize() {
/* 344 */       return (this.messages == null) ? 0 : this.messages.size();
/*     */     }
/*     */     
/*     */     public Iterator<LogEntry> getMessagesIterator() {
/* 348 */       return (this.messages == null) ? null : this.messages.iterator();
/*     */     }
/*     */     
/*     */     public void addToMessages(LogEntry elem) {
/* 352 */       if (this.messages == null) {
/* 353 */         this.messages = new ArrayList<>();
/*     */       }
/* 355 */       this.messages.add(elem);
/*     */     }
/*     */     
/*     */     public List<LogEntry> getMessages() {
/* 359 */       return this.messages;
/*     */     }
/*     */     
/*     */     public Log_args setMessages(List<LogEntry> messages) {
/* 363 */       this.messages = messages;
/* 364 */       return this;
/*     */     }
/*     */     
/*     */     public void unsetMessages() {
/* 368 */       this.messages = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSetMessages() {
/* 373 */       return (this.messages != null);
/*     */     }
/*     */     
/*     */     public void setMessagesIsSet(boolean value) {
/* 377 */       if (!value) {
/* 378 */         this.messages = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFieldValue(_Fields field, Object value) {
/* 384 */       switch (field) {
/*     */         case null:
/* 386 */           if (value == null) {
/* 387 */             unsetMessages(); break;
/*     */           } 
/* 389 */           setMessages((List<LogEntry>)value);
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getFieldValue(_Fields field) {
/* 397 */       switch (field) {
/*     */         case null:
/* 399 */           return getMessages();
/*     */       } 
/*     */       
/* 402 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSet(_Fields field) {
/* 407 */       if (field == null) {
/* 408 */         throw new IllegalArgumentException();
/*     */       }
/*     */       
/* 411 */       switch (field) {
/*     */         case null:
/* 413 */           return isSetMessages();
/*     */       } 
/* 415 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object that) {
/* 420 */       if (that == null)
/* 421 */         return false; 
/* 422 */       if (that instanceof Log_args)
/* 423 */         return equals((Log_args)that); 
/* 424 */       return false;
/*     */     }
/*     */     
/*     */     public boolean equals(Log_args that) {
/* 428 */       if (that == null) {
/* 429 */         return false;
/*     */       }
/* 431 */       boolean this_present_messages = isSetMessages();
/* 432 */       boolean that_present_messages = that.isSetMessages();
/* 433 */       if (this_present_messages || that_present_messages) {
/* 434 */         if (!this_present_messages || !that_present_messages)
/* 435 */           return false; 
/* 436 */         if (!this.messages.equals(that.messages)) {
/* 437 */           return false;
/*     */         }
/*     */       } 
/* 440 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 445 */       return 0;
/*     */     }
/*     */     
/*     */     public int compareTo(Log_args other) {
/* 449 */       if (!getClass().equals(other.getClass())) {
/* 450 */         return getClass().getName().compareTo(other.getClass().getName());
/*     */       }
/*     */       
/* 453 */       int lastComparison = 0;
/* 454 */       Log_args typedOther = other;
/*     */       
/* 456 */       lastComparison = Boolean.valueOf(isSetMessages()).compareTo(Boolean.valueOf(typedOther.isSetMessages()));
/* 457 */       if (lastComparison != 0) {
/* 458 */         return lastComparison;
/*     */       }
/* 460 */       if (isSetMessages()) {
/* 461 */         lastComparison = TBaseHelper.compareTo(this.messages, typedOther.messages);
/* 462 */         if (lastComparison != 0) {
/* 463 */           return lastComparison;
/*     */         }
/*     */       } 
/* 466 */       return 0;
/*     */     }
/*     */     
/*     */     public _Fields fieldForId(int fieldId) {
/* 470 */       return _Fields.findByThriftId(fieldId);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(TProtocol iprot) throws TException {
/* 475 */       iprot.readStructBegin();
/*     */       while (true) {
/* 477 */         TField field = iprot.readFieldBegin();
/* 478 */         if (field.type == 0) {
/*     */           break;
/*     */         }
/* 481 */         switch (field.id) {
/*     */           case 1:
/* 483 */             if (field.type == 15) {
/*     */               
/* 485 */               TList _list0 = iprot.readListBegin();
/* 486 */               this.messages = new ArrayList<>(_list0.size);
/* 487 */               for (int _i1 = 0; _i1 < _list0.size; _i1++) {
/*     */                 
/* 489 */                 LogEntry _elem2 = new LogEntry();
/* 490 */                 _elem2.read(iprot);
/* 491 */                 this.messages.add(_elem2);
/*     */               } 
/* 493 */               iprot.readListEnd();
/*     */               break;
/*     */             } 
/* 496 */             TProtocolUtil.skip(iprot, field.type);
/*     */             break;
/*     */           
/*     */           default:
/* 500 */             TProtocolUtil.skip(iprot, field.type); break;
/*     */         } 
/* 502 */         iprot.readFieldEnd();
/*     */       } 
/* 504 */       iprot.readStructEnd();
/*     */ 
/*     */       
/* 507 */       validate();
/*     */     }
/*     */     
/*     */     public void write(TProtocol oprot) throws TException {
/* 511 */       validate();
/*     */       
/* 513 */       oprot.writeStructBegin(STRUCT_DESC);
/* 514 */       if (this.messages != null) {
/* 515 */         oprot.writeFieldBegin(MESSAGES_FIELD_DESC);
/*     */         
/* 517 */         oprot.writeListBegin(new TList((byte)12, this.messages.size()));
/* 518 */         for (LogEntry _iter3 : this.messages) {
/* 519 */           _iter3.write(oprot);
/*     */         }
/* 521 */         oprot.writeListEnd();
/*     */         
/* 523 */         oprot.writeFieldEnd();
/*     */       } 
/* 525 */       oprot.writeFieldStop();
/* 526 */       oprot.writeStructEnd();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 531 */       StringBuilder sb = new StringBuilder("Log_args(");
/*     */       
/* 533 */       boolean first = true;
/* 534 */       sb.append("messages:");
/* 535 */       if (this.messages == null) {
/* 536 */         sb.append("null");
/*     */       } else {
/* 538 */         sb.append(this.messages);
/*     */       } 
/* 540 */       first = false;
/* 541 */       sb.append(")");
/* 542 */       return sb.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate() throws TException {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Log_result
/*     */     implements TBase<Log_result, Log_result._Fields>, Serializable, Cloneable
/*     */   {
/* 553 */     private static final TStruct STRUCT_DESC = new TStruct("Log_result");
/*     */     
/* 555 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)8, (short)0);
/*     */     
/*     */     public ResultCode success;
/*     */     
/*     */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*     */ 
/*     */     
/*     */     public enum _Fields
/*     */       implements TFieldIdEnum
/*     */     {
/* 565 */       SUCCESS((short)
/*     */ 
/*     */ 
/*     */         
/* 569 */         0, "success");
/*     */       
/* 571 */       private static final Map<String, _Fields> byName = new ConcurrentHashMap<>(); private final short _thriftId;
/*     */       
/*     */       static {
/* 574 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 575 */           byName.put(field.getFieldName(), field);
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       private final String _fieldName;
/*     */       
/*     */       public static _Fields findByThriftId(int fieldId) {
/* 583 */         switch (fieldId) {
/*     */           case 0:
/* 585 */             return SUCCESS;
/*     */         } 
/* 587 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 596 */         _Fields fields = findByThriftId(fieldId);
/* 597 */         if (fields == null)
/* 598 */           throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 599 */         return fields;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public static _Fields findByName(String name) {
/* 606 */         return byName.get(name);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       _Fields(short thriftId, String fieldName) {
/* 613 */         this._thriftId = thriftId;
/* 614 */         this._fieldName = fieldName;
/*     */       }
/*     */       
/*     */       public short getThriftFieldId() {
/* 618 */         return this._thriftId;
/*     */       }
/*     */       
/*     */       public String getFieldName() {
/* 622 */         return this._fieldName;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 630 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<>(_Fields.class);
/* 631 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new EnumMetaData((byte)16, ResultCode.class)));
/* 632 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 633 */       FieldMetaData.addStructMetaDataMap(Log_result.class, metaDataMap);
/*     */     }
/*     */ 
/*     */     
/*     */     public Log_result() {}
/*     */     
/*     */     public Log_result(ResultCode success) {
/* 640 */       this();
/* 641 */       this.success = success;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Log_result(Log_result other) {
/* 648 */       if (other.isSetSuccess()) {
/* 649 */         this.success = other.success;
/*     */       }
/*     */     }
/*     */     
/*     */     public Log_result deepCopy() {
/* 654 */       return new Log_result(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 659 */       this.success = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ResultCode getSuccess() {
/* 667 */       return this.success;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Log_result setSuccess(ResultCode success) {
/* 675 */       this.success = success;
/* 676 */       return this;
/*     */     }
/*     */     
/*     */     public void unsetSuccess() {
/* 680 */       this.success = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSetSuccess() {
/* 685 */       return (this.success != null);
/*     */     }
/*     */     
/*     */     public void setSuccessIsSet(boolean value) {
/* 689 */       if (!value) {
/* 690 */         this.success = null;
/*     */       }
/*     */     }
/*     */     
/*     */     public void setFieldValue(_Fields field, Object value) {
/* 695 */       switch (field) {
/*     */         case null:
/* 697 */           if (value == null) {
/* 698 */             unsetSuccess(); break;
/*     */           } 
/* 700 */           setSuccess((ResultCode)value);
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getFieldValue(_Fields field) {
/* 708 */       switch (field) {
/*     */         case null:
/* 710 */           return getSuccess();
/*     */       } 
/*     */       
/* 713 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSet(_Fields field) {
/* 718 */       if (field == null) {
/* 719 */         throw new IllegalArgumentException();
/*     */       }
/*     */       
/* 722 */       switch (field) {
/*     */         case null:
/* 724 */           return isSetSuccess();
/*     */       } 
/* 726 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object that) {
/* 731 */       if (that == null)
/* 732 */         return false; 
/* 733 */       if (that instanceof Log_result)
/* 734 */         return equals((Log_result)that); 
/* 735 */       return false;
/*     */     }
/*     */     
/*     */     public boolean equals(Log_result that) {
/* 739 */       if (that == null) {
/* 740 */         return false;
/*     */       }
/* 742 */       boolean this_present_success = isSetSuccess();
/* 743 */       boolean that_present_success = that.isSetSuccess();
/* 744 */       if (this_present_success || that_present_success) {
/* 745 */         if (!this_present_success || !that_present_success)
/* 746 */           return false; 
/* 747 */         if (!this.success.equals(that.success)) {
/* 748 */           return false;
/*     */         }
/*     */       } 
/* 751 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 756 */       return 0;
/*     */     }
/*     */     
/*     */     public int compareTo(Log_result other) {
/* 760 */       if (!getClass().equals(other.getClass())) {
/* 761 */         return getClass().getName().compareTo(other.getClass().getName());
/*     */       }
/*     */       
/* 764 */       int lastComparison = 0;
/* 765 */       Log_result typedOther = other;
/*     */       
/* 767 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 768 */       if (lastComparison != 0) {
/* 769 */         return lastComparison;
/*     */       }
/* 771 */       if (isSetSuccess()) {
/* 772 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 773 */         if (lastComparison != 0) {
/* 774 */           return lastComparison;
/*     */         }
/*     */       } 
/* 777 */       return 0;
/*     */     }
/*     */     
/*     */     public _Fields fieldForId(int fieldId) {
/* 781 */       return _Fields.findByThriftId(fieldId);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(TProtocol iprot) throws TException {
/* 786 */       iprot.readStructBegin();
/*     */       while (true) {
/* 788 */         TField field = iprot.readFieldBegin();
/* 789 */         if (field.type == 0) {
/*     */           break;
/*     */         }
/* 792 */         switch (field.id) {
/*     */           case 0:
/* 794 */             if (field.type == 8) {
/* 795 */               this.success = ResultCode.findByValue(iprot.readI32()); break;
/*     */             } 
/* 797 */             TProtocolUtil.skip(iprot, field.type);
/*     */             break;
/*     */           
/*     */           default:
/* 801 */             TProtocolUtil.skip(iprot, field.type); break;
/*     */         } 
/* 803 */         iprot.readFieldEnd();
/*     */       } 
/* 805 */       iprot.readStructEnd();
/*     */ 
/*     */       
/* 808 */       validate();
/*     */     }
/*     */     
/*     */     public void write(TProtocol oprot) throws TException {
/* 812 */       oprot.writeStructBegin(STRUCT_DESC);
/*     */       
/* 814 */       if (isSetSuccess()) {
/* 815 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 816 */         oprot.writeI32(this.success.getValue());
/* 817 */         oprot.writeFieldEnd();
/*     */       } 
/* 819 */       oprot.writeFieldStop();
/* 820 */       oprot.writeStructEnd();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 825 */       StringBuilder sb = new StringBuilder("Log_result(");
/*     */       
/* 827 */       boolean first = true;
/*     */       
/* 829 */       sb.append("success:");
/* 830 */       if (this.success == null) {
/* 831 */         sb.append("null");
/*     */       } else {
/* 833 */         sb.append(this.success);
/*     */       } 
/* 835 */       first = false;
/* 836 */       sb.append(")");
/* 837 */       return sb.toString();
/*     */     }
/*     */     
/*     */     public void validate() throws TException {}
/*     */   }
/*     */   
/*     */   public static interface AsyncIface extends FacebookService.AsyncIface {
/*     */     void Log(List<LogEntry> param1List, AsyncMethodCallback<Scribe.AsyncClient.Log_call> param1AsyncMethodCallback) throws TException;
/*     */   }
/*     */   
/*     */   public static interface Iface extends FacebookService.Iface {
/*     */     ResultCode Log(List<LogEntry> param1List) throws TException;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/scribe/Scribe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */