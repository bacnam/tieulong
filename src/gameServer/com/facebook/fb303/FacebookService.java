/*      */ package com.facebook.fb303;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.apache.thrift.TApplicationException;
/*      */ import org.apache.thrift.TBase;
/*      */ import org.apache.thrift.TBaseHelper;
/*      */ import org.apache.thrift.TException;
/*      */ import org.apache.thrift.TFieldIdEnum;
/*      */ import org.apache.thrift.TProcessor;
/*      */ import org.apache.thrift.TServiceClient;
/*      */ import org.apache.thrift.TServiceClientFactory;
/*      */ import org.apache.thrift.async.AsyncMethodCallback;
/*      */ import org.apache.thrift.async.TAsyncClient;
/*      */ import org.apache.thrift.async.TAsyncClientFactory;
/*      */ import org.apache.thrift.async.TAsyncClientManager;
/*      */ import org.apache.thrift.async.TAsyncMethodCall;
/*      */ import org.apache.thrift.meta_data.EnumMetaData;
/*      */ import org.apache.thrift.meta_data.FieldMetaData;
/*      */ import org.apache.thrift.meta_data.FieldValueMetaData;
/*      */ import org.apache.thrift.meta_data.MapMetaData;
/*      */ import org.apache.thrift.protocol.TField;
/*      */ import org.apache.thrift.protocol.TMap;
/*      */ import org.apache.thrift.protocol.TMessage;
/*      */ import org.apache.thrift.protocol.TProtocol;
/*      */ import org.apache.thrift.protocol.TProtocolException;
/*      */ import org.apache.thrift.protocol.TProtocolFactory;
/*      */ import org.apache.thrift.protocol.TProtocolUtil;
/*      */ import org.apache.thrift.protocol.TStruct;
/*      */ import org.apache.thrift.transport.TMemoryInputTransport;
/*      */ import org.apache.thrift.transport.TNonblockingTransport;
/*      */ import org.apache.thrift.transport.TTransport;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FacebookService
/*      */ {
/*      */   public static interface Iface
/*      */   {
/*      */     String getName() throws TException;
/*      */     
/*      */     String getVersion() throws TException;
/*      */     
/*      */     fb_status getStatus() throws TException;
/*      */     
/*      */     String getStatusDetails() throws TException;
/*      */     
/*      */     Map<String, Long> getCounters() throws TException;
/*      */     
/*      */     long getCounter(String param1String) throws TException;
/*      */     
/*      */     void setOption(String param1String1, String param1String2) throws TException;
/*      */     
/*      */     String getOption(String param1String) throws TException;
/*      */     
/*      */     Map<String, String> getOptions() throws TException;
/*      */     
/*      */     String getCpuProfile(int param1Int) throws TException;
/*      */     
/*      */     long aliveSince() throws TException;
/*      */     
/*      */     void reinitialize() throws TException;
/*      */     
/*      */     void shutdown() throws TException;
/*      */   }
/*      */   
/*      */   public static interface AsyncIface
/*      */   {
/*      */     void getName(AsyncMethodCallback<FacebookService.AsyncClient.getName_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getVersion(AsyncMethodCallback<FacebookService.AsyncClient.getVersion_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getStatus(AsyncMethodCallback<FacebookService.AsyncClient.getStatus_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getStatusDetails(AsyncMethodCallback<FacebookService.AsyncClient.getStatusDetails_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getCounters(AsyncMethodCallback<FacebookService.AsyncClient.getCounters_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getCounter(String param1String, AsyncMethodCallback<FacebookService.AsyncClient.getCounter_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void setOption(String param1String1, String param1String2, AsyncMethodCallback<FacebookService.AsyncClient.setOption_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getOption(String param1String, AsyncMethodCallback<FacebookService.AsyncClient.getOption_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getOptions(AsyncMethodCallback<FacebookService.AsyncClient.getOptions_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void getCpuProfile(int param1Int, AsyncMethodCallback<FacebookService.AsyncClient.getCpuProfile_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void aliveSince(AsyncMethodCallback<FacebookService.AsyncClient.aliveSince_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void reinitialize(AsyncMethodCallback<FacebookService.AsyncClient.reinitialize_call> param1AsyncMethodCallback) throws TException;
/*      */     
/*      */     void shutdown(AsyncMethodCallback<FacebookService.AsyncClient.shutdown_call> param1AsyncMethodCallback) throws TException;
/*      */   }
/*      */   
/*      */   public static class Client
/*      */     implements TServiceClient, Iface
/*      */   {
/*      */     protected TProtocol iprot_;
/*      */     protected TProtocol oprot_;
/*      */     protected int seqid_;
/*      */     
/*      */     public static class Factory
/*      */       implements TServiceClientFactory<Client>
/*      */     {
/*      */       public FacebookService.Client getClient(TProtocol prot) {
/*  148 */         return new FacebookService.Client(prot);
/*      */       }
/*      */       public FacebookService.Client getClient(TProtocol iprot, TProtocol oprot) {
/*  151 */         return new FacebookService.Client(iprot, oprot);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public Client(TProtocol prot) {
/*  157 */       this(prot, prot);
/*      */     }
/*      */ 
/*      */     
/*      */     public Client(TProtocol iprot, TProtocol oprot) {
/*  162 */       this.iprot_ = iprot;
/*  163 */       this.oprot_ = oprot;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TProtocol getInputProtocol() {
/*  173 */       return this.iprot_;
/*      */     }
/*      */ 
/*      */     
/*      */     public TProtocol getOutputProtocol() {
/*  178 */       return this.oprot_;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() throws TException {
/*  183 */       send_getName();
/*  184 */       return recv_getName();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getName() throws TException {
/*  189 */       this.oprot_.writeMessageBegin(new TMessage("getName", (byte)1, ++this.seqid_));
/*  190 */       FacebookService.getName_args args = new FacebookService.getName_args();
/*  191 */       args.write(this.oprot_);
/*  192 */       this.oprot_.writeMessageEnd();
/*  193 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public String recv_getName() throws TException {
/*  198 */       TMessage msg = this.iprot_.readMessageBegin();
/*  199 */       if (msg.type == 3) {
/*  200 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  201 */         this.iprot_.readMessageEnd();
/*  202 */         throw x;
/*      */       } 
/*  204 */       if (msg.seqid != this.seqid_) {
/*  205 */         throw new TApplicationException(4, "getName failed: out of sequence response");
/*      */       }
/*  207 */       FacebookService.getName_result result = new FacebookService.getName_result();
/*  208 */       result.read(this.iprot_);
/*  209 */       this.iprot_.readMessageEnd();
/*  210 */       if (result.isSetSuccess()) {
/*  211 */         return result.success;
/*      */       }
/*  213 */       throw new TApplicationException(5, "getName failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public String getVersion() throws TException {
/*  218 */       send_getVersion();
/*  219 */       return recv_getVersion();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getVersion() throws TException {
/*  224 */       this.oprot_.writeMessageBegin(new TMessage("getVersion", (byte)1, ++this.seqid_));
/*  225 */       FacebookService.getVersion_args args = new FacebookService.getVersion_args();
/*  226 */       args.write(this.oprot_);
/*  227 */       this.oprot_.writeMessageEnd();
/*  228 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public String recv_getVersion() throws TException {
/*  233 */       TMessage msg = this.iprot_.readMessageBegin();
/*  234 */       if (msg.type == 3) {
/*  235 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  236 */         this.iprot_.readMessageEnd();
/*  237 */         throw x;
/*      */       } 
/*  239 */       if (msg.seqid != this.seqid_) {
/*  240 */         throw new TApplicationException(4, "getVersion failed: out of sequence response");
/*      */       }
/*  242 */       FacebookService.getVersion_result result = new FacebookService.getVersion_result();
/*  243 */       result.read(this.iprot_);
/*  244 */       this.iprot_.readMessageEnd();
/*  245 */       if (result.isSetSuccess()) {
/*  246 */         return result.success;
/*      */       }
/*  248 */       throw new TApplicationException(5, "getVersion failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public fb_status getStatus() throws TException {
/*  253 */       send_getStatus();
/*  254 */       return recv_getStatus();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getStatus() throws TException {
/*  259 */       this.oprot_.writeMessageBegin(new TMessage("getStatus", (byte)1, ++this.seqid_));
/*  260 */       FacebookService.getStatus_args args = new FacebookService.getStatus_args();
/*  261 */       args.write(this.oprot_);
/*  262 */       this.oprot_.writeMessageEnd();
/*  263 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public fb_status recv_getStatus() throws TException {
/*  268 */       TMessage msg = this.iprot_.readMessageBegin();
/*  269 */       if (msg.type == 3) {
/*  270 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  271 */         this.iprot_.readMessageEnd();
/*  272 */         throw x;
/*      */       } 
/*  274 */       if (msg.seqid != this.seqid_) {
/*  275 */         throw new TApplicationException(4, "getStatus failed: out of sequence response");
/*      */       }
/*  277 */       FacebookService.getStatus_result result = new FacebookService.getStatus_result();
/*  278 */       result.read(this.iprot_);
/*  279 */       this.iprot_.readMessageEnd();
/*  280 */       if (result.isSetSuccess()) {
/*  281 */         return result.success;
/*      */       }
/*  283 */       throw new TApplicationException(5, "getStatus failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public String getStatusDetails() throws TException {
/*  288 */       send_getStatusDetails();
/*  289 */       return recv_getStatusDetails();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getStatusDetails() throws TException {
/*  294 */       this.oprot_.writeMessageBegin(new TMessage("getStatusDetails", (byte)1, ++this.seqid_));
/*  295 */       FacebookService.getStatusDetails_args args = new FacebookService.getStatusDetails_args();
/*  296 */       args.write(this.oprot_);
/*  297 */       this.oprot_.writeMessageEnd();
/*  298 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public String recv_getStatusDetails() throws TException {
/*  303 */       TMessage msg = this.iprot_.readMessageBegin();
/*  304 */       if (msg.type == 3) {
/*  305 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  306 */         this.iprot_.readMessageEnd();
/*  307 */         throw x;
/*      */       } 
/*  309 */       if (msg.seqid != this.seqid_) {
/*  310 */         throw new TApplicationException(4, "getStatusDetails failed: out of sequence response");
/*      */       }
/*  312 */       FacebookService.getStatusDetails_result result = new FacebookService.getStatusDetails_result();
/*  313 */       result.read(this.iprot_);
/*  314 */       this.iprot_.readMessageEnd();
/*  315 */       if (result.isSetSuccess()) {
/*  316 */         return result.success;
/*      */       }
/*  318 */       throw new TApplicationException(5, "getStatusDetails failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<String, Long> getCounters() throws TException {
/*  323 */       send_getCounters();
/*  324 */       return recv_getCounters();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getCounters() throws TException {
/*  329 */       this.oprot_.writeMessageBegin(new TMessage("getCounters", (byte)1, ++this.seqid_));
/*  330 */       FacebookService.getCounters_args args = new FacebookService.getCounters_args();
/*  331 */       args.write(this.oprot_);
/*  332 */       this.oprot_.writeMessageEnd();
/*  333 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<String, Long> recv_getCounters() throws TException {
/*  338 */       TMessage msg = this.iprot_.readMessageBegin();
/*  339 */       if (msg.type == 3) {
/*  340 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  341 */         this.iprot_.readMessageEnd();
/*  342 */         throw x;
/*      */       } 
/*  344 */       if (msg.seqid != this.seqid_) {
/*  345 */         throw new TApplicationException(4, "getCounters failed: out of sequence response");
/*      */       }
/*  347 */       FacebookService.getCounters_result result = new FacebookService.getCounters_result();
/*  348 */       result.read(this.iprot_);
/*  349 */       this.iprot_.readMessageEnd();
/*  350 */       if (result.isSetSuccess()) {
/*  351 */         return result.success;
/*      */       }
/*  353 */       throw new TApplicationException(5, "getCounters failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public long getCounter(String key) throws TException {
/*  358 */       send_getCounter(key);
/*  359 */       return recv_getCounter();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getCounter(String key) throws TException {
/*  364 */       this.oprot_.writeMessageBegin(new TMessage("getCounter", (byte)1, ++this.seqid_));
/*  365 */       FacebookService.getCounter_args args = new FacebookService.getCounter_args();
/*  366 */       args.setKey(key);
/*  367 */       args.write(this.oprot_);
/*  368 */       this.oprot_.writeMessageEnd();
/*  369 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public long recv_getCounter() throws TException {
/*  374 */       TMessage msg = this.iprot_.readMessageBegin();
/*  375 */       if (msg.type == 3) {
/*  376 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  377 */         this.iprot_.readMessageEnd();
/*  378 */         throw x;
/*      */       } 
/*  380 */       if (msg.seqid != this.seqid_) {
/*  381 */         throw new TApplicationException(4, "getCounter failed: out of sequence response");
/*      */       }
/*  383 */       FacebookService.getCounter_result result = new FacebookService.getCounter_result();
/*  384 */       result.read(this.iprot_);
/*  385 */       this.iprot_.readMessageEnd();
/*  386 */       if (result.isSetSuccess()) {
/*  387 */         return result.success;
/*      */       }
/*  389 */       throw new TApplicationException(5, "getCounter failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public void setOption(String key, String value) throws TException {
/*  394 */       send_setOption(key, value);
/*  395 */       recv_setOption();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_setOption(String key, String value) throws TException {
/*  400 */       this.oprot_.writeMessageBegin(new TMessage("setOption", (byte)1, ++this.seqid_));
/*  401 */       FacebookService.setOption_args args = new FacebookService.setOption_args();
/*  402 */       args.setKey(key);
/*  403 */       args.setValue(value);
/*  404 */       args.write(this.oprot_);
/*  405 */       this.oprot_.writeMessageEnd();
/*  406 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public void recv_setOption() throws TException {
/*  411 */       TMessage msg = this.iprot_.readMessageBegin();
/*  412 */       if (msg.type == 3) {
/*  413 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  414 */         this.iprot_.readMessageEnd();
/*  415 */         throw x;
/*      */       } 
/*  417 */       if (msg.seqid != this.seqid_) {
/*  418 */         throw new TApplicationException(4, "setOption failed: out of sequence response");
/*      */       }
/*  420 */       FacebookService.setOption_result result = new FacebookService.setOption_result();
/*  421 */       result.read(this.iprot_);
/*  422 */       this.iprot_.readMessageEnd();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getOption(String key) throws TException {
/*  428 */       send_getOption(key);
/*  429 */       return recv_getOption();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getOption(String key) throws TException {
/*  434 */       this.oprot_.writeMessageBegin(new TMessage("getOption", (byte)1, ++this.seqid_));
/*  435 */       FacebookService.getOption_args args = new FacebookService.getOption_args();
/*  436 */       args.setKey(key);
/*  437 */       args.write(this.oprot_);
/*  438 */       this.oprot_.writeMessageEnd();
/*  439 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public String recv_getOption() throws TException {
/*  444 */       TMessage msg = this.iprot_.readMessageBegin();
/*  445 */       if (msg.type == 3) {
/*  446 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  447 */         this.iprot_.readMessageEnd();
/*  448 */         throw x;
/*      */       } 
/*  450 */       if (msg.seqid != this.seqid_) {
/*  451 */         throw new TApplicationException(4, "getOption failed: out of sequence response");
/*      */       }
/*  453 */       FacebookService.getOption_result result = new FacebookService.getOption_result();
/*  454 */       result.read(this.iprot_);
/*  455 */       this.iprot_.readMessageEnd();
/*  456 */       if (result.isSetSuccess()) {
/*  457 */         return result.success;
/*      */       }
/*  459 */       throw new TApplicationException(5, "getOption failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<String, String> getOptions() throws TException {
/*  464 */       send_getOptions();
/*  465 */       return recv_getOptions();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getOptions() throws TException {
/*  470 */       this.oprot_.writeMessageBegin(new TMessage("getOptions", (byte)1, ++this.seqid_));
/*  471 */       FacebookService.getOptions_args args = new FacebookService.getOptions_args();
/*  472 */       args.write(this.oprot_);
/*  473 */       this.oprot_.writeMessageEnd();
/*  474 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<String, String> recv_getOptions() throws TException {
/*  479 */       TMessage msg = this.iprot_.readMessageBegin();
/*  480 */       if (msg.type == 3) {
/*  481 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  482 */         this.iprot_.readMessageEnd();
/*  483 */         throw x;
/*      */       } 
/*  485 */       if (msg.seqid != this.seqid_) {
/*  486 */         throw new TApplicationException(4, "getOptions failed: out of sequence response");
/*      */       }
/*  488 */       FacebookService.getOptions_result result = new FacebookService.getOptions_result();
/*  489 */       result.read(this.iprot_);
/*  490 */       this.iprot_.readMessageEnd();
/*  491 */       if (result.isSetSuccess()) {
/*  492 */         return result.success;
/*      */       }
/*  494 */       throw new TApplicationException(5, "getOptions failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public String getCpuProfile(int profileDurationInSec) throws TException {
/*  499 */       send_getCpuProfile(profileDurationInSec);
/*  500 */       return recv_getCpuProfile();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_getCpuProfile(int profileDurationInSec) throws TException {
/*  505 */       this.oprot_.writeMessageBegin(new TMessage("getCpuProfile", (byte)1, ++this.seqid_));
/*  506 */       FacebookService.getCpuProfile_args args = new FacebookService.getCpuProfile_args();
/*  507 */       args.setProfileDurationInSec(profileDurationInSec);
/*  508 */       args.write(this.oprot_);
/*  509 */       this.oprot_.writeMessageEnd();
/*  510 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public String recv_getCpuProfile() throws TException {
/*  515 */       TMessage msg = this.iprot_.readMessageBegin();
/*  516 */       if (msg.type == 3) {
/*  517 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  518 */         this.iprot_.readMessageEnd();
/*  519 */         throw x;
/*      */       } 
/*  521 */       if (msg.seqid != this.seqid_) {
/*  522 */         throw new TApplicationException(4, "getCpuProfile failed: out of sequence response");
/*      */       }
/*  524 */       FacebookService.getCpuProfile_result result = new FacebookService.getCpuProfile_result();
/*  525 */       result.read(this.iprot_);
/*  526 */       this.iprot_.readMessageEnd();
/*  527 */       if (result.isSetSuccess()) {
/*  528 */         return result.success;
/*      */       }
/*  530 */       throw new TApplicationException(5, "getCpuProfile failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public long aliveSince() throws TException {
/*  535 */       send_aliveSince();
/*  536 */       return recv_aliveSince();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_aliveSince() throws TException {
/*  541 */       this.oprot_.writeMessageBegin(new TMessage("aliveSince", (byte)1, ++this.seqid_));
/*  542 */       FacebookService.aliveSince_args args = new FacebookService.aliveSince_args();
/*  543 */       args.write(this.oprot_);
/*  544 */       this.oprot_.writeMessageEnd();
/*  545 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public long recv_aliveSince() throws TException {
/*  550 */       TMessage msg = this.iprot_.readMessageBegin();
/*  551 */       if (msg.type == 3) {
/*  552 */         TApplicationException x = TApplicationException.read(this.iprot_);
/*  553 */         this.iprot_.readMessageEnd();
/*  554 */         throw x;
/*      */       } 
/*  556 */       if (msg.seqid != this.seqid_) {
/*  557 */         throw new TApplicationException(4, "aliveSince failed: out of sequence response");
/*      */       }
/*  559 */       FacebookService.aliveSince_result result = new FacebookService.aliveSince_result();
/*  560 */       result.read(this.iprot_);
/*  561 */       this.iprot_.readMessageEnd();
/*  562 */       if (result.isSetSuccess()) {
/*  563 */         return result.success;
/*      */       }
/*  565 */       throw new TApplicationException(5, "aliveSince failed: unknown result");
/*      */     }
/*      */ 
/*      */     
/*      */     public void reinitialize() throws TException {
/*  570 */       send_reinitialize();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_reinitialize() throws TException {
/*  575 */       this.oprot_.writeMessageBegin(new TMessage("reinitialize", (byte)1, ++this.seqid_));
/*  576 */       FacebookService.reinitialize_args args = new FacebookService.reinitialize_args();
/*  577 */       args.write(this.oprot_);
/*  578 */       this.oprot_.writeMessageEnd();
/*  579 */       this.oprot_.getTransport().flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public void shutdown() throws TException {
/*  584 */       send_shutdown();
/*      */     }
/*      */ 
/*      */     
/*      */     public void send_shutdown() throws TException {
/*  589 */       this.oprot_.writeMessageBegin(new TMessage("shutdown", (byte)1, ++this.seqid_));
/*  590 */       FacebookService.shutdown_args args = new FacebookService.shutdown_args();
/*  591 */       args.write(this.oprot_);
/*  592 */       this.oprot_.writeMessageEnd();
/*  593 */       this.oprot_.getTransport().flush();
/*      */     } }
/*      */   
/*      */   public static class AsyncClient extends TAsyncClient implements AsyncIface {
/*      */     public static class Factory implements TAsyncClientFactory<AsyncClient> {
/*      */       private TAsyncClientManager clientManager;
/*      */       private TProtocolFactory protocolFactory;
/*      */       
/*      */       public Factory(TAsyncClientManager clientManager, TProtocolFactory protocolFactory) {
/*  602 */         this.clientManager = clientManager;
/*  603 */         this.protocolFactory = protocolFactory;
/*      */       }
/*      */       public FacebookService.AsyncClient getAsyncClient(TNonblockingTransport transport) {
/*  606 */         return new FacebookService.AsyncClient(this.protocolFactory, this.clientManager, transport);
/*      */       }
/*      */     }
/*      */     
/*      */     public AsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager clientManager, TNonblockingTransport transport) {
/*  611 */       super(protocolFactory, clientManager, transport);
/*      */     }
/*      */     
/*      */     public void getName(AsyncMethodCallback<getName_call> resultHandler) throws TException {
/*  615 */       checkReady();
/*  616 */       getName_call method_call = new getName_call(resultHandler, this, this.protocolFactory, this.transport);
/*  617 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getName_call extends TAsyncMethodCall {
/*      */       public getName_call(AsyncMethodCallback<getName_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  622 */         super(client, protocolFactory, transport, resultHandler, false);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  626 */         prot.writeMessageBegin(new TMessage("getName", (byte)1, 0));
/*  627 */         FacebookService.getName_args args = new FacebookService.getName_args();
/*  628 */         args.write(prot);
/*  629 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public String getResult() throws TException {
/*  633 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  634 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  636 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  637 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  638 */         return (new FacebookService.Client(prot)).recv_getName();
/*      */       }
/*      */     }
/*      */     
/*      */     public void getVersion(AsyncMethodCallback<getVersion_call> resultHandler) throws TException {
/*  643 */       checkReady();
/*  644 */       getVersion_call method_call = new getVersion_call(resultHandler, this, this.protocolFactory, this.transport);
/*  645 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getVersion_call extends TAsyncMethodCall {
/*      */       public getVersion_call(AsyncMethodCallback<getVersion_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  650 */         super(client, protocolFactory, transport, resultHandler, false);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  654 */         prot.writeMessageBegin(new TMessage("getVersion", (byte)1, 0));
/*  655 */         FacebookService.getVersion_args args = new FacebookService.getVersion_args();
/*  656 */         args.write(prot);
/*  657 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public String getResult() throws TException {
/*  661 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  662 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  664 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  665 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  666 */         return (new FacebookService.Client(prot)).recv_getVersion();
/*      */       }
/*      */     }
/*      */     
/*      */     public void getStatus(AsyncMethodCallback<getStatus_call> resultHandler) throws TException {
/*  671 */       checkReady();
/*  672 */       getStatus_call method_call = new getStatus_call(resultHandler, this, this.protocolFactory, this.transport);
/*  673 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getStatus_call extends TAsyncMethodCall {
/*      */       public getStatus_call(AsyncMethodCallback<getStatus_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  678 */         super(client, protocolFactory, transport, resultHandler, false);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  682 */         prot.writeMessageBegin(new TMessage("getStatus", (byte)1, 0));
/*  683 */         FacebookService.getStatus_args args = new FacebookService.getStatus_args();
/*  684 */         args.write(prot);
/*  685 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public fb_status getResult() throws TException {
/*  689 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  690 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  692 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  693 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  694 */         return (new FacebookService.Client(prot)).recv_getStatus();
/*      */       }
/*      */     }
/*      */     
/*      */     public void getStatusDetails(AsyncMethodCallback<getStatusDetails_call> resultHandler) throws TException {
/*  699 */       checkReady();
/*  700 */       getStatusDetails_call method_call = new getStatusDetails_call(resultHandler, this, this.protocolFactory, this.transport);
/*  701 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getStatusDetails_call extends TAsyncMethodCall {
/*      */       public getStatusDetails_call(AsyncMethodCallback<getStatusDetails_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  706 */         super(client, protocolFactory, transport, resultHandler, false);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  710 */         prot.writeMessageBegin(new TMessage("getStatusDetails", (byte)1, 0));
/*  711 */         FacebookService.getStatusDetails_args args = new FacebookService.getStatusDetails_args();
/*  712 */         args.write(prot);
/*  713 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public String getResult() throws TException {
/*  717 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  718 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  720 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  721 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  722 */         return (new FacebookService.Client(prot)).recv_getStatusDetails();
/*      */       }
/*      */     }
/*      */     
/*      */     public void getCounters(AsyncMethodCallback<getCounters_call> resultHandler) throws TException {
/*  727 */       checkReady();
/*  728 */       getCounters_call method_call = new getCounters_call(resultHandler, this, this.protocolFactory, this.transport);
/*  729 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getCounters_call extends TAsyncMethodCall {
/*      */       public getCounters_call(AsyncMethodCallback<getCounters_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  734 */         super(client, protocolFactory, transport, resultHandler, false);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  738 */         prot.writeMessageBegin(new TMessage("getCounters", (byte)1, 0));
/*  739 */         FacebookService.getCounters_args args = new FacebookService.getCounters_args();
/*  740 */         args.write(prot);
/*  741 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public Map<String, Long> getResult() throws TException {
/*  745 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  746 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  748 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  749 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  750 */         return (new FacebookService.Client(prot)).recv_getCounters();
/*      */       }
/*      */     }
/*      */     
/*      */     public void getCounter(String key, AsyncMethodCallback<getCounter_call> resultHandler) throws TException {
/*  755 */       checkReady();
/*  756 */       getCounter_call method_call = new getCounter_call(key, resultHandler, this, this.protocolFactory, this.transport);
/*  757 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getCounter_call extends TAsyncMethodCall { private String key;
/*      */       
/*      */       public getCounter_call(String key, AsyncMethodCallback<getCounter_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  763 */         super(client, protocolFactory, transport, resultHandler, false);
/*  764 */         this.key = key;
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  768 */         prot.writeMessageBegin(new TMessage("getCounter", (byte)1, 0));
/*  769 */         FacebookService.getCounter_args args = new FacebookService.getCounter_args();
/*  770 */         args.setKey(this.key);
/*  771 */         args.write(prot);
/*  772 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public long getResult() throws TException {
/*  776 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  777 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  779 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  780 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  781 */         return (new FacebookService.Client(prot)).recv_getCounter();
/*      */       } }
/*      */ 
/*      */     
/*      */     public void setOption(String key, String value, AsyncMethodCallback<setOption_call> resultHandler) throws TException {
/*  786 */       checkReady();
/*  787 */       setOption_call method_call = new setOption_call(key, value, resultHandler, this, this.protocolFactory, this.transport);
/*  788 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class setOption_call extends TAsyncMethodCall { private String key;
/*      */       private String value;
/*      */       
/*      */       public setOption_call(String key, String value, AsyncMethodCallback<setOption_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  795 */         super(client, protocolFactory, transport, resultHandler, false);
/*  796 */         this.key = key;
/*  797 */         this.value = value;
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  801 */         prot.writeMessageBegin(new TMessage("setOption", (byte)1, 0));
/*  802 */         FacebookService.setOption_args args = new FacebookService.setOption_args();
/*  803 */         args.setKey(this.key);
/*  804 */         args.setValue(this.value);
/*  805 */         args.write(prot);
/*  806 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public void getResult() throws TException {
/*  810 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  811 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  813 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  814 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  815 */         (new FacebookService.Client(prot)).recv_setOption();
/*      */       } }
/*      */ 
/*      */     
/*      */     public void getOption(String key, AsyncMethodCallback<getOption_call> resultHandler) throws TException {
/*  820 */       checkReady();
/*  821 */       getOption_call method_call = new getOption_call(key, resultHandler, this, this.protocolFactory, this.transport);
/*  822 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getOption_call extends TAsyncMethodCall { private String key;
/*      */       
/*      */       public getOption_call(String key, AsyncMethodCallback<getOption_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  828 */         super(client, protocolFactory, transport, resultHandler, false);
/*  829 */         this.key = key;
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  833 */         prot.writeMessageBegin(new TMessage("getOption", (byte)1, 0));
/*  834 */         FacebookService.getOption_args args = new FacebookService.getOption_args();
/*  835 */         args.setKey(this.key);
/*  836 */         args.write(prot);
/*  837 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public String getResult() throws TException {
/*  841 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  842 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  844 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  845 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  846 */         return (new FacebookService.Client(prot)).recv_getOption();
/*      */       } }
/*      */ 
/*      */     
/*      */     public void getOptions(AsyncMethodCallback<getOptions_call> resultHandler) throws TException {
/*  851 */       checkReady();
/*  852 */       getOptions_call method_call = new getOptions_call(resultHandler, this, this.protocolFactory, this.transport);
/*  853 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getOptions_call extends TAsyncMethodCall {
/*      */       public getOptions_call(AsyncMethodCallback<getOptions_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  858 */         super(client, protocolFactory, transport, resultHandler, false);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  862 */         prot.writeMessageBegin(new TMessage("getOptions", (byte)1, 0));
/*  863 */         FacebookService.getOptions_args args = new FacebookService.getOptions_args();
/*  864 */         args.write(prot);
/*  865 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public Map<String, String> getResult() throws TException {
/*  869 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  870 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  872 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  873 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  874 */         return (new FacebookService.Client(prot)).recv_getOptions();
/*      */       }
/*      */     }
/*      */     
/*      */     public void getCpuProfile(int profileDurationInSec, AsyncMethodCallback<getCpuProfile_call> resultHandler) throws TException {
/*  879 */       checkReady();
/*  880 */       getCpuProfile_call method_call = new getCpuProfile_call(profileDurationInSec, resultHandler, this, this.protocolFactory, this.transport);
/*  881 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class getCpuProfile_call extends TAsyncMethodCall { private int profileDurationInSec;
/*      */       
/*      */       public getCpuProfile_call(int profileDurationInSec, AsyncMethodCallback<getCpuProfile_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  887 */         super(client, protocolFactory, transport, resultHandler, false);
/*  888 */         this.profileDurationInSec = profileDurationInSec;
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  892 */         prot.writeMessageBegin(new TMessage("getCpuProfile", (byte)1, 0));
/*  893 */         FacebookService.getCpuProfile_args args = new FacebookService.getCpuProfile_args();
/*  894 */         args.setProfileDurationInSec(this.profileDurationInSec);
/*  895 */         args.write(prot);
/*  896 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public String getResult() throws TException {
/*  900 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  901 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  903 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  904 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  905 */         return (new FacebookService.Client(prot)).recv_getCpuProfile();
/*      */       } }
/*      */ 
/*      */     
/*      */     public void aliveSince(AsyncMethodCallback<aliveSince_call> resultHandler) throws TException {
/*  910 */       checkReady();
/*  911 */       aliveSince_call method_call = new aliveSince_call(resultHandler, this, this.protocolFactory, this.transport);
/*  912 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class aliveSince_call extends TAsyncMethodCall {
/*      */       public aliveSince_call(AsyncMethodCallback<aliveSince_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  917 */         super(client, protocolFactory, transport, resultHandler, false);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  921 */         prot.writeMessageBegin(new TMessage("aliveSince", (byte)1, 0));
/*  922 */         FacebookService.aliveSince_args args = new FacebookService.aliveSince_args();
/*  923 */         args.write(prot);
/*  924 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public long getResult() throws TException {
/*  928 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  929 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  931 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  932 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*  933 */         return (new FacebookService.Client(prot)).recv_aliveSince();
/*      */       }
/*      */     }
/*      */     
/*      */     public void reinitialize(AsyncMethodCallback<reinitialize_call> resultHandler) throws TException {
/*  938 */       checkReady();
/*  939 */       reinitialize_call method_call = new reinitialize_call(resultHandler, this, this.protocolFactory, this.transport);
/*  940 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class reinitialize_call extends TAsyncMethodCall {
/*      */       public reinitialize_call(AsyncMethodCallback<reinitialize_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  945 */         super(client, protocolFactory, transport, resultHandler, true);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  949 */         prot.writeMessageBegin(new TMessage("reinitialize", (byte)1, 0));
/*  950 */         FacebookService.reinitialize_args args = new FacebookService.reinitialize_args();
/*  951 */         args.write(prot);
/*  952 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public void getResult() throws TException {
/*  956 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  957 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  959 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  960 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*      */       }
/*      */     }
/*      */     
/*      */     public void shutdown(AsyncMethodCallback<shutdown_call> resultHandler) throws TException {
/*  965 */       checkReady();
/*  966 */       shutdown_call method_call = new shutdown_call(resultHandler, this, this.protocolFactory, this.transport);
/*  967 */       this.manager.call(method_call);
/*      */     }
/*      */     
/*      */     public static class shutdown_call extends TAsyncMethodCall {
/*      */       public shutdown_call(AsyncMethodCallback<shutdown_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
/*  972 */         super(client, protocolFactory, transport, resultHandler, true);
/*      */       }
/*      */       
/*      */       public void write_args(TProtocol prot) throws TException {
/*  976 */         prot.writeMessageBegin(new TMessage("shutdown", (byte)1, 0));
/*  977 */         FacebookService.shutdown_args args = new FacebookService.shutdown_args();
/*  978 */         args.write(prot);
/*  979 */         prot.writeMessageEnd();
/*      */       }
/*      */       
/*      */       public void getResult() throws TException {
/*  983 */         if (getState() != TAsyncMethodCall.State.RESPONSE_READ) {
/*  984 */           throw new IllegalStateException("Method call not finished!");
/*      */         }
/*  986 */         TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
/*  987 */         TProtocol prot = this.client.getProtocolFactory().getProtocol((TTransport)memoryTransport);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Processor
/*      */     implements TProcessor {
/*  994 */     private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private FacebookService.Iface iface_;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final HashMap<String, ProcessFunction> processMap_;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Processor(FacebookService.Iface iface) {
/* 1018 */       this.processMap_ = new HashMap<String, ProcessFunction>(); this.iface_ = iface; this.processMap_.put("getName", new getName()); this.processMap_.put("getVersion", new getVersion()); this.processMap_.put("getStatus", new getStatus()); this.processMap_.put("getStatusDetails", new getStatusDetails()); this.processMap_.put("getCounters", new getCounters()); this.processMap_.put("getCounter", new getCounter()); this.processMap_.put("setOption", new setOption()); this.processMap_.put("getOption", new getOption()); this.processMap_.put("getOptions", new getOptions());
/*      */       this.processMap_.put("getCpuProfile", new getCpuProfile());
/*      */       this.processMap_.put("aliveSince", new aliveSince());
/*      */       this.processMap_.put("reinitialize", new reinitialize());
/* 1022 */       this.processMap_.put("shutdown", new shutdown()); } public boolean process(TProtocol iprot, TProtocol oprot) throws TException { TMessage msg = iprot.readMessageBegin();
/* 1023 */       ProcessFunction fn = this.processMap_.get(msg.name);
/* 1024 */       if (fn == null) {
/* 1025 */         TProtocolUtil.skip(iprot, (byte)12);
/* 1026 */         iprot.readMessageEnd();
/* 1027 */         TApplicationException x = new TApplicationException(1, "Invalid method name: '" + msg.name + "'");
/* 1028 */         oprot.writeMessageBegin(new TMessage(msg.name, (byte)3, msg.seqid));
/* 1029 */         x.write(oprot);
/* 1030 */         oprot.writeMessageEnd();
/* 1031 */         oprot.getTransport().flush();
/* 1032 */         return true;
/*      */       } 
/* 1034 */       fn.process(msg.seqid, iprot, oprot);
/* 1035 */       return true; }
/*      */     
/*      */     protected static interface ProcessFunction {
/*      */       void process(int param2Int, TProtocol param2TProtocol1, TProtocol param2TProtocol2) throws TException; }
/*      */     private class getName implements ProcessFunction { private getName() {}
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1041 */         FacebookService.getName_args args = new FacebookService.getName_args();
/*      */         try {
/* 1043 */           args.read(iprot);
/* 1044 */         } catch (TProtocolException e) {
/* 1045 */           iprot.readMessageEnd();
/* 1046 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1047 */           oprot.writeMessageBegin(new TMessage("getName", (byte)3, seqid));
/* 1048 */           x.write(oprot);
/* 1049 */           oprot.writeMessageEnd();
/* 1050 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1053 */         iprot.readMessageEnd();
/* 1054 */         FacebookService.getName_result result = new FacebookService.getName_result();
/* 1055 */         result.success = FacebookService.Processor.this.iface_.getName();
/* 1056 */         oprot.writeMessageBegin(new TMessage("getName", (byte)2, seqid));
/* 1057 */         result.write(oprot);
/* 1058 */         oprot.writeMessageEnd();
/* 1059 */         oprot.getTransport().flush();
/*      */       } }
/*      */ 
/*      */     
/*      */     private class getVersion implements ProcessFunction {
/*      */       private getVersion() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1067 */         FacebookService.getVersion_args args = new FacebookService.getVersion_args();
/*      */         try {
/* 1069 */           args.read(iprot);
/* 1070 */         } catch (TProtocolException e) {
/* 1071 */           iprot.readMessageEnd();
/* 1072 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1073 */           oprot.writeMessageBegin(new TMessage("getVersion", (byte)3, seqid));
/* 1074 */           x.write(oprot);
/* 1075 */           oprot.writeMessageEnd();
/* 1076 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1079 */         iprot.readMessageEnd();
/* 1080 */         FacebookService.getVersion_result result = new FacebookService.getVersion_result();
/* 1081 */         result.success = FacebookService.Processor.this.iface_.getVersion();
/* 1082 */         oprot.writeMessageBegin(new TMessage("getVersion", (byte)2, seqid));
/* 1083 */         result.write(oprot);
/* 1084 */         oprot.writeMessageEnd();
/* 1085 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class getStatus implements ProcessFunction {
/*      */       private getStatus() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1093 */         FacebookService.getStatus_args args = new FacebookService.getStatus_args();
/*      */         try {
/* 1095 */           args.read(iprot);
/* 1096 */         } catch (TProtocolException e) {
/* 1097 */           iprot.readMessageEnd();
/* 1098 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1099 */           oprot.writeMessageBegin(new TMessage("getStatus", (byte)3, seqid));
/* 1100 */           x.write(oprot);
/* 1101 */           oprot.writeMessageEnd();
/* 1102 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1105 */         iprot.readMessageEnd();
/* 1106 */         FacebookService.getStatus_result result = new FacebookService.getStatus_result();
/* 1107 */         result.success = FacebookService.Processor.this.iface_.getStatus();
/* 1108 */         oprot.writeMessageBegin(new TMessage("getStatus", (byte)2, seqid));
/* 1109 */         result.write(oprot);
/* 1110 */         oprot.writeMessageEnd();
/* 1111 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class getStatusDetails implements ProcessFunction {
/*      */       private getStatusDetails() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1119 */         FacebookService.getStatusDetails_args args = new FacebookService.getStatusDetails_args();
/*      */         try {
/* 1121 */           args.read(iprot);
/* 1122 */         } catch (TProtocolException e) {
/* 1123 */           iprot.readMessageEnd();
/* 1124 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1125 */           oprot.writeMessageBegin(new TMessage("getStatusDetails", (byte)3, seqid));
/* 1126 */           x.write(oprot);
/* 1127 */           oprot.writeMessageEnd();
/* 1128 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1131 */         iprot.readMessageEnd();
/* 1132 */         FacebookService.getStatusDetails_result result = new FacebookService.getStatusDetails_result();
/* 1133 */         result.success = FacebookService.Processor.this.iface_.getStatusDetails();
/* 1134 */         oprot.writeMessageBegin(new TMessage("getStatusDetails", (byte)2, seqid));
/* 1135 */         result.write(oprot);
/* 1136 */         oprot.writeMessageEnd();
/* 1137 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class getCounters implements ProcessFunction {
/*      */       private getCounters() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1145 */         FacebookService.getCounters_args args = new FacebookService.getCounters_args();
/*      */         try {
/* 1147 */           args.read(iprot);
/* 1148 */         } catch (TProtocolException e) {
/* 1149 */           iprot.readMessageEnd();
/* 1150 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1151 */           oprot.writeMessageBegin(new TMessage("getCounters", (byte)3, seqid));
/* 1152 */           x.write(oprot);
/* 1153 */           oprot.writeMessageEnd();
/* 1154 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1157 */         iprot.readMessageEnd();
/* 1158 */         FacebookService.getCounters_result result = new FacebookService.getCounters_result();
/* 1159 */         result.success = FacebookService.Processor.this.iface_.getCounters();
/* 1160 */         oprot.writeMessageBegin(new TMessage("getCounters", (byte)2, seqid));
/* 1161 */         result.write(oprot);
/* 1162 */         oprot.writeMessageEnd();
/* 1163 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class getCounter implements ProcessFunction {
/*      */       private getCounter() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1171 */         FacebookService.getCounter_args args = new FacebookService.getCounter_args();
/*      */         try {
/* 1173 */           args.read(iprot);
/* 1174 */         } catch (TProtocolException e) {
/* 1175 */           iprot.readMessageEnd();
/* 1176 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1177 */           oprot.writeMessageBegin(new TMessage("getCounter", (byte)3, seqid));
/* 1178 */           x.write(oprot);
/* 1179 */           oprot.writeMessageEnd();
/* 1180 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1183 */         iprot.readMessageEnd();
/* 1184 */         FacebookService.getCounter_result result = new FacebookService.getCounter_result();
/* 1185 */         result.success = FacebookService.Processor.this.iface_.getCounter(args.key);
/* 1186 */         result.setSuccessIsSet(true);
/* 1187 */         oprot.writeMessageBegin(new TMessage("getCounter", (byte)2, seqid));
/* 1188 */         result.write(oprot);
/* 1189 */         oprot.writeMessageEnd();
/* 1190 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class setOption implements ProcessFunction {
/*      */       private setOption() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1198 */         FacebookService.setOption_args args = new FacebookService.setOption_args();
/*      */         try {
/* 1200 */           args.read(iprot);
/* 1201 */         } catch (TProtocolException e) {
/* 1202 */           iprot.readMessageEnd();
/* 1203 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1204 */           oprot.writeMessageBegin(new TMessage("setOption", (byte)3, seqid));
/* 1205 */           x.write(oprot);
/* 1206 */           oprot.writeMessageEnd();
/* 1207 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1210 */         iprot.readMessageEnd();
/* 1211 */         FacebookService.setOption_result result = new FacebookService.setOption_result();
/* 1212 */         FacebookService.Processor.this.iface_.setOption(args.key, args.value);
/* 1213 */         oprot.writeMessageBegin(new TMessage("setOption", (byte)2, seqid));
/* 1214 */         result.write(oprot);
/* 1215 */         oprot.writeMessageEnd();
/* 1216 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class getOption implements ProcessFunction {
/*      */       private getOption() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1224 */         FacebookService.getOption_args args = new FacebookService.getOption_args();
/*      */         try {
/* 1226 */           args.read(iprot);
/* 1227 */         } catch (TProtocolException e) {
/* 1228 */           iprot.readMessageEnd();
/* 1229 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1230 */           oprot.writeMessageBegin(new TMessage("getOption", (byte)3, seqid));
/* 1231 */           x.write(oprot);
/* 1232 */           oprot.writeMessageEnd();
/* 1233 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1236 */         iprot.readMessageEnd();
/* 1237 */         FacebookService.getOption_result result = new FacebookService.getOption_result();
/* 1238 */         result.success = FacebookService.Processor.this.iface_.getOption(args.key);
/* 1239 */         oprot.writeMessageBegin(new TMessage("getOption", (byte)2, seqid));
/* 1240 */         result.write(oprot);
/* 1241 */         oprot.writeMessageEnd();
/* 1242 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class getOptions implements ProcessFunction {
/*      */       private getOptions() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1250 */         FacebookService.getOptions_args args = new FacebookService.getOptions_args();
/*      */         try {
/* 1252 */           args.read(iprot);
/* 1253 */         } catch (TProtocolException e) {
/* 1254 */           iprot.readMessageEnd();
/* 1255 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1256 */           oprot.writeMessageBegin(new TMessage("getOptions", (byte)3, seqid));
/* 1257 */           x.write(oprot);
/* 1258 */           oprot.writeMessageEnd();
/* 1259 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1262 */         iprot.readMessageEnd();
/* 1263 */         FacebookService.getOptions_result result = new FacebookService.getOptions_result();
/* 1264 */         result.success = FacebookService.Processor.this.iface_.getOptions();
/* 1265 */         oprot.writeMessageBegin(new TMessage("getOptions", (byte)2, seqid));
/* 1266 */         result.write(oprot);
/* 1267 */         oprot.writeMessageEnd();
/* 1268 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class getCpuProfile implements ProcessFunction {
/*      */       private getCpuProfile() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1276 */         FacebookService.getCpuProfile_args args = new FacebookService.getCpuProfile_args();
/*      */         try {
/* 1278 */           args.read(iprot);
/* 1279 */         } catch (TProtocolException e) {
/* 1280 */           iprot.readMessageEnd();
/* 1281 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1282 */           oprot.writeMessageBegin(new TMessage("getCpuProfile", (byte)3, seqid));
/* 1283 */           x.write(oprot);
/* 1284 */           oprot.writeMessageEnd();
/* 1285 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1288 */         iprot.readMessageEnd();
/* 1289 */         FacebookService.getCpuProfile_result result = new FacebookService.getCpuProfile_result();
/* 1290 */         result.success = FacebookService.Processor.this.iface_.getCpuProfile(args.profileDurationInSec);
/* 1291 */         oprot.writeMessageBegin(new TMessage("getCpuProfile", (byte)2, seqid));
/* 1292 */         result.write(oprot);
/* 1293 */         oprot.writeMessageEnd();
/* 1294 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class aliveSince implements ProcessFunction {
/*      */       private aliveSince() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1302 */         FacebookService.aliveSince_args args = new FacebookService.aliveSince_args();
/*      */         try {
/* 1304 */           args.read(iprot);
/* 1305 */         } catch (TProtocolException e) {
/* 1306 */           iprot.readMessageEnd();
/* 1307 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1308 */           oprot.writeMessageBegin(new TMessage("aliveSince", (byte)3, seqid));
/* 1309 */           x.write(oprot);
/* 1310 */           oprot.writeMessageEnd();
/* 1311 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1314 */         iprot.readMessageEnd();
/* 1315 */         FacebookService.aliveSince_result result = new FacebookService.aliveSince_result();
/* 1316 */         result.success = FacebookService.Processor.this.iface_.aliveSince();
/* 1317 */         result.setSuccessIsSet(true);
/* 1318 */         oprot.writeMessageBegin(new TMessage("aliveSince", (byte)2, seqid));
/* 1319 */         result.write(oprot);
/* 1320 */         oprot.writeMessageEnd();
/* 1321 */         oprot.getTransport().flush();
/*      */       }
/*      */     }
/*      */     
/*      */     private class reinitialize implements ProcessFunction {
/*      */       private reinitialize() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1329 */         FacebookService.reinitialize_args args = new FacebookService.reinitialize_args();
/*      */         try {
/* 1331 */           args.read(iprot);
/* 1332 */         } catch (TProtocolException e) {
/* 1333 */           iprot.readMessageEnd();
/* 1334 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1335 */           oprot.writeMessageBegin(new TMessage("reinitialize", (byte)3, seqid));
/* 1336 */           x.write(oprot);
/* 1337 */           oprot.writeMessageEnd();
/* 1338 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1341 */         iprot.readMessageEnd();
/* 1342 */         FacebookService.Processor.this.iface_.reinitialize();
/*      */       }
/*      */     }
/*      */     
/*      */     private class shutdown implements ProcessFunction {
/*      */       private shutdown() {}
/*      */       
/*      */       public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException {
/* 1350 */         FacebookService.shutdown_args args = new FacebookService.shutdown_args();
/*      */         try {
/* 1352 */           args.read(iprot);
/* 1353 */         } catch (TProtocolException e) {
/* 1354 */           iprot.readMessageEnd();
/* 1355 */           TApplicationException x = new TApplicationException(7, e.getMessage());
/* 1356 */           oprot.writeMessageBegin(new TMessage("shutdown", (byte)3, seqid));
/* 1357 */           x.write(oprot);
/* 1358 */           oprot.writeMessageEnd();
/* 1359 */           oprot.getTransport().flush();
/*      */           return;
/*      */         } 
/* 1362 */         iprot.readMessageEnd();
/* 1363 */         FacebookService.Processor.this.iface_.shutdown();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class getName_args
/*      */     implements TBase<getName_args, getName_args._Fields>, Serializable, Cloneable
/*      */   {
/* 1371 */     private static final TStruct STRUCT_DESC = new TStruct("getName_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 1379 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 1382 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 1383 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 1391 */         switch (fieldId) {
/*      */         
/* 1393 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 1402 */         _Fields fields = findByThriftId(fieldId);
/* 1403 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 1404 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 1411 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 1418 */         this._thriftId = thriftId;
/* 1419 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 1423 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 1427 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 1432 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 1433 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 1434 */       FieldMetaData.addStructMetaDataMap(getName_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getName_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public getName_args(getName_args other) {}
/*      */ 
/*      */     
/*      */     public getName_args deepCopy() {
/* 1447 */       return new getName_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 1455 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getName_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 1460 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getName_args$_Fields[field.ordinal()];
/*      */       
/* 1462 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 1467 */       if (field == null) {
/* 1468 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 1471 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getName_args$_Fields[field.ordinal()];
/*      */       
/* 1473 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 1478 */       if (that == null)
/* 1479 */         return false; 
/* 1480 */       if (that instanceof getName_args)
/* 1481 */         return equals((getName_args)that); 
/* 1482 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getName_args that) {
/* 1486 */       if (that == null) {
/* 1487 */         return false;
/*      */       }
/* 1489 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1494 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getName_args other) {
/* 1498 */       if (!getClass().equals(other.getClass())) {
/* 1499 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 1502 */       int lastComparison = 0;
/* 1503 */       getName_args typedOther = other;
/*      */       
/* 1505 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 1509 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 1514 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 1517 */         TField field = iprot.readFieldBegin();
/* 1518 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 1521 */         switch (field.id) {
/*      */         
/* 1523 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 1525 */         iprot.readFieldEnd();
/*      */       } 
/* 1527 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 1530 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 1534 */       validate();
/*      */       
/* 1536 */       oprot.writeStructBegin(STRUCT_DESC);
/* 1537 */       oprot.writeFieldStop();
/* 1538 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1543 */       StringBuilder sb = new StringBuilder("getName_args(");
/* 1544 */       boolean first = true;
/*      */       
/* 1546 */       sb.append(")");
/* 1547 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getName_result
/*      */     implements TBase<getName_result, getName_result._Fields>, Serializable, Cloneable
/*      */   {
/* 1557 */     private static final TStruct STRUCT_DESC = new TStruct("getName_result");
/*      */     
/* 1559 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
/*      */     public String success;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 1565 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 1567 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 1570 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 1571 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 1579 */         switch (fieldId) {
/*      */           case 0:
/* 1581 */             return SUCCESS;
/*      */         } 
/* 1583 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 1592 */         _Fields fields = findByThriftId(fieldId);
/* 1593 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 1594 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 1601 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 1608 */         this._thriftId = thriftId;
/* 1609 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 1613 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 1617 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 1625 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 1626 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 1628 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 1629 */       FieldMetaData.addStructMetaDataMap(getName_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getName_result() {}
/*      */ 
/*      */     
/*      */     public getName_result(String success) {
/* 1638 */       this();
/* 1639 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getName_result(getName_result other) {
/* 1646 */       if (other.isSetSuccess()) {
/* 1647 */         this.success = other.success;
/*      */       }
/*      */     }
/*      */     
/*      */     public getName_result deepCopy() {
/* 1652 */       return new getName_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1657 */       this.success = null;
/*      */     }
/*      */     
/*      */     public String getSuccess() {
/* 1661 */       return this.success;
/*      */     }
/*      */     
/*      */     public getName_result setSuccess(String success) {
/* 1665 */       this.success = success;
/* 1666 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 1670 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 1675 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 1679 */       if (!value) {
/* 1680 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 1685 */       switch (field) {
/*      */         case null:
/* 1687 */           if (value == null) {
/* 1688 */             unsetSuccess(); break;
/*      */           } 
/* 1690 */           setSuccess((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 1698 */       switch (field) {
/*      */         case null:
/* 1700 */           return getSuccess();
/*      */       } 
/*      */       
/* 1703 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 1708 */       if (field == null) {
/* 1709 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 1712 */       switch (field) {
/*      */         case null:
/* 1714 */           return isSetSuccess();
/*      */       } 
/* 1716 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 1721 */       if (that == null)
/* 1722 */         return false; 
/* 1723 */       if (that instanceof getName_result)
/* 1724 */         return equals((getName_result)that); 
/* 1725 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getName_result that) {
/* 1729 */       if (that == null) {
/* 1730 */         return false;
/*      */       }
/* 1732 */       boolean this_present_success = isSetSuccess();
/* 1733 */       boolean that_present_success = that.isSetSuccess();
/* 1734 */       if (this_present_success || that_present_success) {
/* 1735 */         if (!this_present_success || !that_present_success)
/* 1736 */           return false; 
/* 1737 */         if (!this.success.equals(that.success)) {
/* 1738 */           return false;
/*      */         }
/*      */       } 
/* 1741 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1746 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getName_result other) {
/* 1750 */       if (!getClass().equals(other.getClass())) {
/* 1751 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 1754 */       int lastComparison = 0;
/* 1755 */       getName_result typedOther = other;
/*      */       
/* 1757 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 1758 */       if (lastComparison != 0) {
/* 1759 */         return lastComparison;
/*      */       }
/* 1761 */       if (isSetSuccess()) {
/* 1762 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 1763 */         if (lastComparison != 0) {
/* 1764 */           return lastComparison;
/*      */         }
/*      */       } 
/* 1767 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 1771 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 1776 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 1779 */         TField field = iprot.readFieldBegin();
/* 1780 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 1783 */         switch (field.id) {
/*      */           case 0:
/* 1785 */             if (field.type == 11) {
/* 1786 */               this.success = iprot.readString(); break;
/*      */             } 
/* 1788 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 1792 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 1794 */         iprot.readFieldEnd();
/*      */       } 
/* 1796 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 1799 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 1803 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 1805 */       if (isSetSuccess()) {
/* 1806 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 1807 */         oprot.writeString(this.success);
/* 1808 */         oprot.writeFieldEnd();
/*      */       } 
/* 1810 */       oprot.writeFieldStop();
/* 1811 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1816 */       StringBuilder sb = new StringBuilder("getName_result(");
/* 1817 */       boolean first = true;
/*      */       
/* 1819 */       sb.append("success:");
/* 1820 */       if (this.success == null) {
/* 1821 */         sb.append("null");
/*      */       } else {
/* 1823 */         sb.append(this.success);
/*      */       } 
/* 1825 */       first = false;
/* 1826 */       sb.append(")");
/* 1827 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getVersion_args
/*      */     implements TBase<getVersion_args, getVersion_args._Fields>, Serializable, Cloneable
/*      */   {
/* 1837 */     private static final TStruct STRUCT_DESC = new TStruct("getVersion_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 1845 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 1848 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 1849 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 1857 */         switch (fieldId) {
/*      */         
/* 1859 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 1868 */         _Fields fields = findByThriftId(fieldId);
/* 1869 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 1870 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 1877 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 1884 */         this._thriftId = thriftId;
/* 1885 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 1889 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 1893 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 1898 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 1899 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 1900 */       FieldMetaData.addStructMetaDataMap(getVersion_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getVersion_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public getVersion_args(getVersion_args other) {}
/*      */ 
/*      */     
/*      */     public getVersion_args deepCopy() {
/* 1913 */       return new getVersion_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 1921 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getVersion_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 1926 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getVersion_args$_Fields[field.ordinal()];
/*      */       
/* 1928 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 1933 */       if (field == null) {
/* 1934 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 1937 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getVersion_args$_Fields[field.ordinal()];
/*      */       
/* 1939 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 1944 */       if (that == null)
/* 1945 */         return false; 
/* 1946 */       if (that instanceof getVersion_args)
/* 1947 */         return equals((getVersion_args)that); 
/* 1948 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getVersion_args that) {
/* 1952 */       if (that == null) {
/* 1953 */         return false;
/*      */       }
/* 1955 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1960 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getVersion_args other) {
/* 1964 */       if (!getClass().equals(other.getClass())) {
/* 1965 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 1968 */       int lastComparison = 0;
/* 1969 */       getVersion_args typedOther = other;
/*      */       
/* 1971 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 1975 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 1980 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 1983 */         TField field = iprot.readFieldBegin();
/* 1984 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 1987 */         switch (field.id) {
/*      */         
/* 1989 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 1991 */         iprot.readFieldEnd();
/*      */       } 
/* 1993 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 1996 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 2000 */       validate();
/*      */       
/* 2002 */       oprot.writeStructBegin(STRUCT_DESC);
/* 2003 */       oprot.writeFieldStop();
/* 2004 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2009 */       StringBuilder sb = new StringBuilder("getVersion_args(");
/* 2010 */       boolean first = true;
/*      */       
/* 2012 */       sb.append(")");
/* 2013 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getVersion_result
/*      */     implements TBase<getVersion_result, getVersion_result._Fields>, Serializable, Cloneable
/*      */   {
/* 2023 */     private static final TStruct STRUCT_DESC = new TStruct("getVersion_result");
/*      */     
/* 2025 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
/*      */     public String success;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 2031 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 2033 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 2036 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 2037 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 2045 */         switch (fieldId) {
/*      */           case 0:
/* 2047 */             return SUCCESS;
/*      */         } 
/* 2049 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 2058 */         _Fields fields = findByThriftId(fieldId);
/* 2059 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 2060 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 2067 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 2074 */         this._thriftId = thriftId;
/* 2075 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 2079 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 2083 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 2091 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 2092 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 2094 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 2095 */       FieldMetaData.addStructMetaDataMap(getVersion_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getVersion_result() {}
/*      */ 
/*      */     
/*      */     public getVersion_result(String success) {
/* 2104 */       this();
/* 2105 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getVersion_result(getVersion_result other) {
/* 2112 */       if (other.isSetSuccess()) {
/* 2113 */         this.success = other.success;
/*      */       }
/*      */     }
/*      */     
/*      */     public getVersion_result deepCopy() {
/* 2118 */       return new getVersion_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2123 */       this.success = null;
/*      */     }
/*      */     
/*      */     public String getSuccess() {
/* 2127 */       return this.success;
/*      */     }
/*      */     
/*      */     public getVersion_result setSuccess(String success) {
/* 2131 */       this.success = success;
/* 2132 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 2136 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 2141 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 2145 */       if (!value) {
/* 2146 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 2151 */       switch (field) {
/*      */         case null:
/* 2153 */           if (value == null) {
/* 2154 */             unsetSuccess(); break;
/*      */           } 
/* 2156 */           setSuccess((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 2164 */       switch (field) {
/*      */         case null:
/* 2166 */           return getSuccess();
/*      */       } 
/*      */       
/* 2169 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 2174 */       if (field == null) {
/* 2175 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 2178 */       switch (field) {
/*      */         case null:
/* 2180 */           return isSetSuccess();
/*      */       } 
/* 2182 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 2187 */       if (that == null)
/* 2188 */         return false; 
/* 2189 */       if (that instanceof getVersion_result)
/* 2190 */         return equals((getVersion_result)that); 
/* 2191 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getVersion_result that) {
/* 2195 */       if (that == null) {
/* 2196 */         return false;
/*      */       }
/* 2198 */       boolean this_present_success = isSetSuccess();
/* 2199 */       boolean that_present_success = that.isSetSuccess();
/* 2200 */       if (this_present_success || that_present_success) {
/* 2201 */         if (!this_present_success || !that_present_success)
/* 2202 */           return false; 
/* 2203 */         if (!this.success.equals(that.success)) {
/* 2204 */           return false;
/*      */         }
/*      */       } 
/* 2207 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2212 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getVersion_result other) {
/* 2216 */       if (!getClass().equals(other.getClass())) {
/* 2217 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 2220 */       int lastComparison = 0;
/* 2221 */       getVersion_result typedOther = other;
/*      */       
/* 2223 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 2224 */       if (lastComparison != 0) {
/* 2225 */         return lastComparison;
/*      */       }
/* 2227 */       if (isSetSuccess()) {
/* 2228 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 2229 */         if (lastComparison != 0) {
/* 2230 */           return lastComparison;
/*      */         }
/*      */       } 
/* 2233 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 2237 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 2242 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 2245 */         TField field = iprot.readFieldBegin();
/* 2246 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 2249 */         switch (field.id) {
/*      */           case 0:
/* 2251 */             if (field.type == 11) {
/* 2252 */               this.success = iprot.readString(); break;
/*      */             } 
/* 2254 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 2258 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 2260 */         iprot.readFieldEnd();
/*      */       } 
/* 2262 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 2265 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 2269 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 2271 */       if (isSetSuccess()) {
/* 2272 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 2273 */         oprot.writeString(this.success);
/* 2274 */         oprot.writeFieldEnd();
/*      */       } 
/* 2276 */       oprot.writeFieldStop();
/* 2277 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2282 */       StringBuilder sb = new StringBuilder("getVersion_result(");
/* 2283 */       boolean first = true;
/*      */       
/* 2285 */       sb.append("success:");
/* 2286 */       if (this.success == null) {
/* 2287 */         sb.append("null");
/*      */       } else {
/* 2289 */         sb.append(this.success);
/*      */       } 
/* 2291 */       first = false;
/* 2292 */       sb.append(")");
/* 2293 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getStatus_args
/*      */     implements TBase<getStatus_args, getStatus_args._Fields>, Serializable, Cloneable
/*      */   {
/* 2303 */     private static final TStruct STRUCT_DESC = new TStruct("getStatus_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 2311 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 2314 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 2315 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 2323 */         switch (fieldId) {
/*      */         
/* 2325 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 2334 */         _Fields fields = findByThriftId(fieldId);
/* 2335 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 2336 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 2343 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 2350 */         this._thriftId = thriftId;
/* 2351 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 2355 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 2359 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 2364 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 2365 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 2366 */       FieldMetaData.addStructMetaDataMap(getStatus_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatus_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatus_args(getStatus_args other) {}
/*      */ 
/*      */     
/*      */     public getStatus_args deepCopy() {
/* 2379 */       return new getStatus_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 2387 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatus_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 2392 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatus_args$_Fields[field.ordinal()];
/*      */       
/* 2394 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 2399 */       if (field == null) {
/* 2400 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 2403 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatus_args$_Fields[field.ordinal()];
/*      */       
/* 2405 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 2410 */       if (that == null)
/* 2411 */         return false; 
/* 2412 */       if (that instanceof getStatus_args)
/* 2413 */         return equals((getStatus_args)that); 
/* 2414 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getStatus_args that) {
/* 2418 */       if (that == null) {
/* 2419 */         return false;
/*      */       }
/* 2421 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2426 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getStatus_args other) {
/* 2430 */       if (!getClass().equals(other.getClass())) {
/* 2431 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 2434 */       int lastComparison = 0;
/* 2435 */       getStatus_args typedOther = other;
/*      */       
/* 2437 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 2441 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 2446 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 2449 */         TField field = iprot.readFieldBegin();
/* 2450 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 2453 */         switch (field.id) {
/*      */         
/* 2455 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 2457 */         iprot.readFieldEnd();
/*      */       } 
/* 2459 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 2462 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 2466 */       validate();
/*      */       
/* 2468 */       oprot.writeStructBegin(STRUCT_DESC);
/* 2469 */       oprot.writeFieldStop();
/* 2470 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2475 */       StringBuilder sb = new StringBuilder("getStatus_args(");
/* 2476 */       boolean first = true;
/*      */       
/* 2478 */       sb.append(")");
/* 2479 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getStatus_result
/*      */     implements TBase<getStatus_result, getStatus_result._Fields>, Serializable, Cloneable
/*      */   {
/* 2489 */     private static final TStruct STRUCT_DESC = new TStruct("getStatus_result");
/*      */     
/* 2491 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)8, (short)0);
/*      */ 
/*      */ 
/*      */     
/*      */     public fb_status success;
/*      */ 
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 2505 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 2507 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 2510 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 2511 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 2519 */         switch (fieldId) {
/*      */           case 0:
/* 2521 */             return SUCCESS;
/*      */         } 
/* 2523 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 2532 */         _Fields fields = findByThriftId(fieldId);
/* 2533 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 2534 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 2541 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 2548 */         this._thriftId = thriftId;
/* 2549 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 2553 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 2557 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 2565 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 2566 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new EnumMetaData((byte)16, fb_status.class)));
/*      */       
/* 2568 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 2569 */       FieldMetaData.addStructMetaDataMap(getStatus_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatus_result() {}
/*      */ 
/*      */     
/*      */     public getStatus_result(fb_status success) {
/* 2578 */       this();
/* 2579 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatus_result(getStatus_result other) {
/* 2586 */       if (other.isSetSuccess()) {
/* 2587 */         this.success = other.success;
/*      */       }
/*      */     }
/*      */     
/*      */     public getStatus_result deepCopy() {
/* 2592 */       return new getStatus_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2597 */       this.success = null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public fb_status getSuccess() {
/* 2605 */       return this.success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatus_result setSuccess(fb_status success) {
/* 2613 */       this.success = success;
/* 2614 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 2618 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 2623 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 2627 */       if (!value) {
/* 2628 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 2633 */       switch (field) {
/*      */         case null:
/* 2635 */           if (value == null) {
/* 2636 */             unsetSuccess(); break;
/*      */           } 
/* 2638 */           setSuccess((fb_status)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 2646 */       switch (field) {
/*      */         case null:
/* 2648 */           return getSuccess();
/*      */       } 
/*      */       
/* 2651 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 2656 */       if (field == null) {
/* 2657 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 2660 */       switch (field) {
/*      */         case null:
/* 2662 */           return isSetSuccess();
/*      */       } 
/* 2664 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 2669 */       if (that == null)
/* 2670 */         return false; 
/* 2671 */       if (that instanceof getStatus_result)
/* 2672 */         return equals((getStatus_result)that); 
/* 2673 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getStatus_result that) {
/* 2677 */       if (that == null) {
/* 2678 */         return false;
/*      */       }
/* 2680 */       boolean this_present_success = isSetSuccess();
/* 2681 */       boolean that_present_success = that.isSetSuccess();
/* 2682 */       if (this_present_success || that_present_success) {
/* 2683 */         if (!this_present_success || !that_present_success)
/* 2684 */           return false; 
/* 2685 */         if (!this.success.equals(that.success)) {
/* 2686 */           return false;
/*      */         }
/*      */       } 
/* 2689 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2694 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getStatus_result other) {
/* 2698 */       if (!getClass().equals(other.getClass())) {
/* 2699 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 2702 */       int lastComparison = 0;
/* 2703 */       getStatus_result typedOther = other;
/*      */       
/* 2705 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 2706 */       if (lastComparison != 0) {
/* 2707 */         return lastComparison;
/*      */       }
/* 2709 */       if (isSetSuccess()) {
/* 2710 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 2711 */         if (lastComparison != 0) {
/* 2712 */           return lastComparison;
/*      */         }
/*      */       } 
/* 2715 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 2719 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 2724 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 2727 */         TField field = iprot.readFieldBegin();
/* 2728 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 2731 */         switch (field.id) {
/*      */           case 0:
/* 2733 */             if (field.type == 8) {
/* 2734 */               this.success = fb_status.findByValue(iprot.readI32()); break;
/*      */             } 
/* 2736 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 2740 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 2742 */         iprot.readFieldEnd();
/*      */       } 
/* 2744 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 2747 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 2751 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 2753 */       if (isSetSuccess()) {
/* 2754 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 2755 */         oprot.writeI32(this.success.getValue());
/* 2756 */         oprot.writeFieldEnd();
/*      */       } 
/* 2758 */       oprot.writeFieldStop();
/* 2759 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2764 */       StringBuilder sb = new StringBuilder("getStatus_result(");
/* 2765 */       boolean first = true;
/*      */       
/* 2767 */       sb.append("success:");
/* 2768 */       if (this.success == null) {
/* 2769 */         sb.append("null");
/*      */       } else {
/* 2771 */         sb.append(this.success);
/*      */       } 
/* 2773 */       first = false;
/* 2774 */       sb.append(")");
/* 2775 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getStatusDetails_args
/*      */     implements TBase<getStatusDetails_args, getStatusDetails_args._Fields>, Serializable, Cloneable
/*      */   {
/* 2785 */     private static final TStruct STRUCT_DESC = new TStruct("getStatusDetails_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 2793 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 2796 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 2797 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 2805 */         switch (fieldId) {
/*      */         
/* 2807 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 2816 */         _Fields fields = findByThriftId(fieldId);
/* 2817 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 2818 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 2825 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 2832 */         this._thriftId = thriftId;
/* 2833 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 2837 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 2841 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 2846 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 2847 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 2848 */       FieldMetaData.addStructMetaDataMap(getStatusDetails_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatusDetails_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatusDetails_args(getStatusDetails_args other) {}
/*      */ 
/*      */     
/*      */     public getStatusDetails_args deepCopy() {
/* 2861 */       return new getStatusDetails_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 2869 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatusDetails_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 2874 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatusDetails_args$_Fields[field.ordinal()];
/*      */       
/* 2876 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 2881 */       if (field == null) {
/* 2882 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 2885 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getStatusDetails_args$_Fields[field.ordinal()];
/*      */       
/* 2887 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 2892 */       if (that == null)
/* 2893 */         return false; 
/* 2894 */       if (that instanceof getStatusDetails_args)
/* 2895 */         return equals((getStatusDetails_args)that); 
/* 2896 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getStatusDetails_args that) {
/* 2900 */       if (that == null) {
/* 2901 */         return false;
/*      */       }
/* 2903 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2908 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getStatusDetails_args other) {
/* 2912 */       if (!getClass().equals(other.getClass())) {
/* 2913 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 2916 */       int lastComparison = 0;
/* 2917 */       getStatusDetails_args typedOther = other;
/*      */       
/* 2919 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 2923 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 2928 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 2931 */         TField field = iprot.readFieldBegin();
/* 2932 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 2935 */         switch (field.id) {
/*      */         
/* 2937 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 2939 */         iprot.readFieldEnd();
/*      */       } 
/* 2941 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 2944 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 2948 */       validate();
/*      */       
/* 2950 */       oprot.writeStructBegin(STRUCT_DESC);
/* 2951 */       oprot.writeFieldStop();
/* 2952 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2957 */       StringBuilder sb = new StringBuilder("getStatusDetails_args(");
/* 2958 */       boolean first = true;
/*      */       
/* 2960 */       sb.append(")");
/* 2961 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getStatusDetails_result
/*      */     implements TBase<getStatusDetails_result, getStatusDetails_result._Fields>, Serializable, Cloneable
/*      */   {
/* 2971 */     private static final TStruct STRUCT_DESC = new TStruct("getStatusDetails_result");
/*      */     
/* 2973 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
/*      */     public String success;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 2979 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 2981 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 2984 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 2985 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 2993 */         switch (fieldId) {
/*      */           case 0:
/* 2995 */             return SUCCESS;
/*      */         } 
/* 2997 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 3006 */         _Fields fields = findByThriftId(fieldId);
/* 3007 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 3008 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 3015 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 3022 */         this._thriftId = thriftId;
/* 3023 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 3027 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 3031 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 3039 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 3040 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 3042 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 3043 */       FieldMetaData.addStructMetaDataMap(getStatusDetails_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatusDetails_result() {}
/*      */ 
/*      */     
/*      */     public getStatusDetails_result(String success) {
/* 3052 */       this();
/* 3053 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getStatusDetails_result(getStatusDetails_result other) {
/* 3060 */       if (other.isSetSuccess()) {
/* 3061 */         this.success = other.success;
/*      */       }
/*      */     }
/*      */     
/*      */     public getStatusDetails_result deepCopy() {
/* 3066 */       return new getStatusDetails_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3071 */       this.success = null;
/*      */     }
/*      */     
/*      */     public String getSuccess() {
/* 3075 */       return this.success;
/*      */     }
/*      */     
/*      */     public getStatusDetails_result setSuccess(String success) {
/* 3079 */       this.success = success;
/* 3080 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 3084 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 3089 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 3093 */       if (!value) {
/* 3094 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 3099 */       switch (field) {
/*      */         case null:
/* 3101 */           if (value == null) {
/* 3102 */             unsetSuccess(); break;
/*      */           } 
/* 3104 */           setSuccess((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 3112 */       switch (field) {
/*      */         case null:
/* 3114 */           return getSuccess();
/*      */       } 
/*      */       
/* 3117 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 3122 */       if (field == null) {
/* 3123 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 3126 */       switch (field) {
/*      */         case null:
/* 3128 */           return isSetSuccess();
/*      */       } 
/* 3130 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 3135 */       if (that == null)
/* 3136 */         return false; 
/* 3137 */       if (that instanceof getStatusDetails_result)
/* 3138 */         return equals((getStatusDetails_result)that); 
/* 3139 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getStatusDetails_result that) {
/* 3143 */       if (that == null) {
/* 3144 */         return false;
/*      */       }
/* 3146 */       boolean this_present_success = isSetSuccess();
/* 3147 */       boolean that_present_success = that.isSetSuccess();
/* 3148 */       if (this_present_success || that_present_success) {
/* 3149 */         if (!this_present_success || !that_present_success)
/* 3150 */           return false; 
/* 3151 */         if (!this.success.equals(that.success)) {
/* 3152 */           return false;
/*      */         }
/*      */       } 
/* 3155 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 3160 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getStatusDetails_result other) {
/* 3164 */       if (!getClass().equals(other.getClass())) {
/* 3165 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 3168 */       int lastComparison = 0;
/* 3169 */       getStatusDetails_result typedOther = other;
/*      */       
/* 3171 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 3172 */       if (lastComparison != 0) {
/* 3173 */         return lastComparison;
/*      */       }
/* 3175 */       if (isSetSuccess()) {
/* 3176 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 3177 */         if (lastComparison != 0) {
/* 3178 */           return lastComparison;
/*      */         }
/*      */       } 
/* 3181 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 3185 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 3190 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 3193 */         TField field = iprot.readFieldBegin();
/* 3194 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 3197 */         switch (field.id) {
/*      */           case 0:
/* 3199 */             if (field.type == 11) {
/* 3200 */               this.success = iprot.readString(); break;
/*      */             } 
/* 3202 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 3206 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 3208 */         iprot.readFieldEnd();
/*      */       } 
/* 3210 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 3213 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 3217 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 3219 */       if (isSetSuccess()) {
/* 3220 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 3221 */         oprot.writeString(this.success);
/* 3222 */         oprot.writeFieldEnd();
/*      */       } 
/* 3224 */       oprot.writeFieldStop();
/* 3225 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 3230 */       StringBuilder sb = new StringBuilder("getStatusDetails_result(");
/* 3231 */       boolean first = true;
/*      */       
/* 3233 */       sb.append("success:");
/* 3234 */       if (this.success == null) {
/* 3235 */         sb.append("null");
/*      */       } else {
/* 3237 */         sb.append(this.success);
/*      */       } 
/* 3239 */       first = false;
/* 3240 */       sb.append(")");
/* 3241 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getCounters_args
/*      */     implements TBase<getCounters_args, getCounters_args._Fields>, Serializable, Cloneable
/*      */   {
/* 3251 */     private static final TStruct STRUCT_DESC = new TStruct("getCounters_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 3259 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 3262 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 3263 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 3271 */         switch (fieldId) {
/*      */         
/* 3273 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 3282 */         _Fields fields = findByThriftId(fieldId);
/* 3283 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 3284 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 3291 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 3298 */         this._thriftId = thriftId;
/* 3299 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 3303 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 3307 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 3312 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 3313 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 3314 */       FieldMetaData.addStructMetaDataMap(getCounters_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounters_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounters_args(getCounters_args other) {}
/*      */ 
/*      */     
/*      */     public getCounters_args deepCopy() {
/* 3327 */       return new getCounters_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 3335 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getCounters_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 3340 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getCounters_args$_Fields[field.ordinal()];
/*      */       
/* 3342 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 3347 */       if (field == null) {
/* 3348 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 3351 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getCounters_args$_Fields[field.ordinal()];
/*      */       
/* 3353 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 3358 */       if (that == null)
/* 3359 */         return false; 
/* 3360 */       if (that instanceof getCounters_args)
/* 3361 */         return equals((getCounters_args)that); 
/* 3362 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getCounters_args that) {
/* 3366 */       if (that == null) {
/* 3367 */         return false;
/*      */       }
/* 3369 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 3374 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getCounters_args other) {
/* 3378 */       if (!getClass().equals(other.getClass())) {
/* 3379 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 3382 */       int lastComparison = 0;
/* 3383 */       getCounters_args typedOther = other;
/*      */       
/* 3385 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 3389 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 3394 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 3397 */         TField field = iprot.readFieldBegin();
/* 3398 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 3401 */         switch (field.id) {
/*      */         
/* 3403 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 3405 */         iprot.readFieldEnd();
/*      */       } 
/* 3407 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 3410 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 3414 */       validate();
/*      */       
/* 3416 */       oprot.writeStructBegin(STRUCT_DESC);
/* 3417 */       oprot.writeFieldStop();
/* 3418 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 3423 */       StringBuilder sb = new StringBuilder("getCounters_args(");
/* 3424 */       boolean first = true;
/*      */       
/* 3426 */       sb.append(")");
/* 3427 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getCounters_result
/*      */     implements TBase<getCounters_result, getCounters_result._Fields>, Serializable, Cloneable
/*      */   {
/* 3437 */     private static final TStruct STRUCT_DESC = new TStruct("getCounters_result");
/*      */     
/* 3439 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)13, (short)0);
/*      */     public Map<String, Long> success;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 3445 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 3447 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 3450 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 3451 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 3459 */         switch (fieldId) {
/*      */           case 0:
/* 3461 */             return SUCCESS;
/*      */         } 
/* 3463 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 3472 */         _Fields fields = findByThriftId(fieldId);
/* 3473 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 3474 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 3481 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 3488 */         this._thriftId = thriftId;
/* 3489 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 3493 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 3497 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 3505 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 3506 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new MapMetaData((byte)13, new FieldValueMetaData((byte)11), new FieldValueMetaData((byte)10))));
/*      */ 
/*      */ 
/*      */       
/* 3510 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 3511 */       FieldMetaData.addStructMetaDataMap(getCounters_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounters_result() {}
/*      */ 
/*      */     
/*      */     public getCounters_result(Map<String, Long> success) {
/* 3520 */       this();
/* 3521 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounters_result(getCounters_result other) {
/* 3528 */       if (other.isSetSuccess()) {
/* 3529 */         Map<String, Long> __this__success = new HashMap<String, Long>();
/* 3530 */         for (Map.Entry<String, Long> other_element : other.success.entrySet()) {
/*      */           
/* 3532 */           String other_element_key = other_element.getKey();
/* 3533 */           Long other_element_value = other_element.getValue();
/*      */           
/* 3535 */           String __this__success_copy_key = other_element_key;
/*      */           
/* 3537 */           Long __this__success_copy_value = other_element_value;
/*      */           
/* 3539 */           __this__success.put(__this__success_copy_key, __this__success_copy_value);
/*      */         } 
/* 3541 */         this.success = __this__success;
/*      */       } 
/*      */     }
/*      */     
/*      */     public getCounters_result deepCopy() {
/* 3546 */       return new getCounters_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3551 */       this.success = null;
/*      */     }
/*      */     
/*      */     public int getSuccessSize() {
/* 3555 */       return (this.success == null) ? 0 : this.success.size();
/*      */     }
/*      */     
/*      */     public void putToSuccess(String key, long val) {
/* 3559 */       if (this.success == null) {
/* 3560 */         this.success = new HashMap<String, Long>();
/*      */       }
/* 3562 */       this.success.put(key, Long.valueOf(val));
/*      */     }
/*      */     
/*      */     public Map<String, Long> getSuccess() {
/* 3566 */       return this.success;
/*      */     }
/*      */     
/*      */     public getCounters_result setSuccess(Map<String, Long> success) {
/* 3570 */       this.success = success;
/* 3571 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 3575 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 3580 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 3584 */       if (!value) {
/* 3585 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 3590 */       switch (field) {
/*      */         case null:
/* 3592 */           if (value == null) {
/* 3593 */             unsetSuccess(); break;
/*      */           } 
/* 3595 */           setSuccess((Map<String, Long>)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 3603 */       switch (field) {
/*      */         case null:
/* 3605 */           return getSuccess();
/*      */       } 
/*      */       
/* 3608 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 3613 */       if (field == null) {
/* 3614 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 3617 */       switch (field) {
/*      */         case null:
/* 3619 */           return isSetSuccess();
/*      */       } 
/* 3621 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 3626 */       if (that == null)
/* 3627 */         return false; 
/* 3628 */       if (that instanceof getCounters_result)
/* 3629 */         return equals((getCounters_result)that); 
/* 3630 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getCounters_result that) {
/* 3634 */       if (that == null) {
/* 3635 */         return false;
/*      */       }
/* 3637 */       boolean this_present_success = isSetSuccess();
/* 3638 */       boolean that_present_success = that.isSetSuccess();
/* 3639 */       if (this_present_success || that_present_success) {
/* 3640 */         if (!this_present_success || !that_present_success)
/* 3641 */           return false; 
/* 3642 */         if (!this.success.equals(that.success)) {
/* 3643 */           return false;
/*      */         }
/*      */       } 
/* 3646 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 3651 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getCounters_result other) {
/* 3655 */       if (!getClass().equals(other.getClass())) {
/* 3656 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 3659 */       int lastComparison = 0;
/* 3660 */       getCounters_result typedOther = other;
/*      */       
/* 3662 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 3663 */       if (lastComparison != 0) {
/* 3664 */         return lastComparison;
/*      */       }
/* 3666 */       if (isSetSuccess()) {
/* 3667 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 3668 */         if (lastComparison != 0) {
/* 3669 */           return lastComparison;
/*      */         }
/*      */       } 
/* 3672 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 3676 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 3681 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 3684 */         TField field = iprot.readFieldBegin();
/* 3685 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 3688 */         switch (field.id) {
/*      */           case 0:
/* 3690 */             if (field.type == 13) {
/*      */               
/* 3692 */               TMap _map0 = iprot.readMapBegin();
/* 3693 */               this.success = new HashMap<String, Long>(2 * _map0.size);
/* 3694 */               for (int _i1 = 0; _i1 < _map0.size; _i1++) {
/*      */ 
/*      */ 
/*      */                 
/* 3698 */                 String _key2 = iprot.readString();
/* 3699 */                 long _val3 = iprot.readI64();
/* 3700 */                 this.success.put(_key2, Long.valueOf(_val3));
/*      */               } 
/* 3702 */               iprot.readMapEnd();
/*      */               break;
/*      */             } 
/* 3705 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 3709 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 3711 */         iprot.readFieldEnd();
/*      */       } 
/* 3713 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 3716 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 3720 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 3722 */       if (isSetSuccess()) {
/* 3723 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/*      */         
/* 3725 */         oprot.writeMapBegin(new TMap((byte)11, (byte)10, this.success.size()));
/* 3726 */         for (Map.Entry<String, Long> _iter4 : this.success.entrySet()) {
/*      */           
/* 3728 */           oprot.writeString(_iter4.getKey());
/* 3729 */           oprot.writeI64(((Long)_iter4.getValue()).longValue());
/*      */         } 
/* 3731 */         oprot.writeMapEnd();
/*      */         
/* 3733 */         oprot.writeFieldEnd();
/*      */       } 
/* 3735 */       oprot.writeFieldStop();
/* 3736 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 3741 */       StringBuilder sb = new StringBuilder("getCounters_result(");
/* 3742 */       boolean first = true;
/*      */       
/* 3744 */       sb.append("success:");
/* 3745 */       if (this.success == null) {
/* 3746 */         sb.append("null");
/*      */       } else {
/* 3748 */         sb.append(this.success);
/*      */       } 
/* 3750 */       first = false;
/* 3751 */       sb.append(")");
/* 3752 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getCounter_args
/*      */     implements TBase<getCounter_args, getCounter_args._Fields>, Serializable, Cloneable
/*      */   {
/* 3762 */     private static final TStruct STRUCT_DESC = new TStruct("getCounter_args");
/*      */     
/* 3764 */     private static final TField KEY_FIELD_DESC = new TField("key", (byte)11, (short)1);
/*      */     public String key;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 3770 */       KEY((short)1, "key"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 3772 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 3775 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 3776 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 3784 */         switch (fieldId) {
/*      */           case 1:
/* 3786 */             return KEY;
/*      */         } 
/* 3788 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 3797 */         _Fields fields = findByThriftId(fieldId);
/* 3798 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 3799 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 3806 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 3813 */         this._thriftId = thriftId;
/* 3814 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 3818 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 3822 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 3830 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 3831 */       tmpMap.put(_Fields.KEY, new FieldMetaData("key", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 3833 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 3834 */       FieldMetaData.addStructMetaDataMap(getCounter_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounter_args() {}
/*      */ 
/*      */     
/*      */     public getCounter_args(String key) {
/* 3843 */       this();
/* 3844 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounter_args(getCounter_args other) {
/* 3851 */       if (other.isSetKey()) {
/* 3852 */         this.key = other.key;
/*      */       }
/*      */     }
/*      */     
/*      */     public getCounter_args deepCopy() {
/* 3857 */       return new getCounter_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3862 */       this.key = null;
/*      */     }
/*      */     
/*      */     public String getKey() {
/* 3866 */       return this.key;
/*      */     }
/*      */     
/*      */     public getCounter_args setKey(String key) {
/* 3870 */       this.key = key;
/* 3871 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetKey() {
/* 3875 */       this.key = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetKey() {
/* 3880 */       return (this.key != null);
/*      */     }
/*      */     
/*      */     public void setKeyIsSet(boolean value) {
/* 3884 */       if (!value) {
/* 3885 */         this.key = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 3890 */       switch (field) {
/*      */         case null:
/* 3892 */           if (value == null) {
/* 3893 */             unsetKey(); break;
/*      */           } 
/* 3895 */           setKey((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 3903 */       switch (field) {
/*      */         case null:
/* 3905 */           return getKey();
/*      */       } 
/*      */       
/* 3908 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 3913 */       if (field == null) {
/* 3914 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 3917 */       switch (field) {
/*      */         case null:
/* 3919 */           return isSetKey();
/*      */       } 
/* 3921 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 3926 */       if (that == null)
/* 3927 */         return false; 
/* 3928 */       if (that instanceof getCounter_args)
/* 3929 */         return equals((getCounter_args)that); 
/* 3930 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getCounter_args that) {
/* 3934 */       if (that == null) {
/* 3935 */         return false;
/*      */       }
/* 3937 */       boolean this_present_key = isSetKey();
/* 3938 */       boolean that_present_key = that.isSetKey();
/* 3939 */       if (this_present_key || that_present_key) {
/* 3940 */         if (!this_present_key || !that_present_key)
/* 3941 */           return false; 
/* 3942 */         if (!this.key.equals(that.key)) {
/* 3943 */           return false;
/*      */         }
/*      */       } 
/* 3946 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 3951 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getCounter_args other) {
/* 3955 */       if (!getClass().equals(other.getClass())) {
/* 3956 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 3959 */       int lastComparison = 0;
/* 3960 */       getCounter_args typedOther = other;
/*      */       
/* 3962 */       lastComparison = Boolean.valueOf(isSetKey()).compareTo(Boolean.valueOf(typedOther.isSetKey()));
/* 3963 */       if (lastComparison != 0) {
/* 3964 */         return lastComparison;
/*      */       }
/* 3966 */       if (isSetKey()) {
/* 3967 */         lastComparison = TBaseHelper.compareTo(this.key, typedOther.key);
/* 3968 */         if (lastComparison != 0) {
/* 3969 */           return lastComparison;
/*      */         }
/*      */       } 
/* 3972 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 3976 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 3981 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 3984 */         TField field = iprot.readFieldBegin();
/* 3985 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 3988 */         switch (field.id) {
/*      */           case 1:
/* 3990 */             if (field.type == 11) {
/* 3991 */               this.key = iprot.readString(); break;
/*      */             } 
/* 3993 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 3997 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 3999 */         iprot.readFieldEnd();
/*      */       } 
/* 4001 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 4004 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 4008 */       validate();
/*      */       
/* 4010 */       oprot.writeStructBegin(STRUCT_DESC);
/* 4011 */       if (this.key != null) {
/* 4012 */         oprot.writeFieldBegin(KEY_FIELD_DESC);
/* 4013 */         oprot.writeString(this.key);
/* 4014 */         oprot.writeFieldEnd();
/*      */       } 
/* 4016 */       oprot.writeFieldStop();
/* 4017 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4022 */       StringBuilder sb = new StringBuilder("getCounter_args(");
/* 4023 */       boolean first = true;
/*      */       
/* 4025 */       sb.append("key:");
/* 4026 */       if (this.key == null) {
/* 4027 */         sb.append("null");
/*      */       } else {
/* 4029 */         sb.append(this.key);
/*      */       } 
/* 4031 */       first = false;
/* 4032 */       sb.append(")");
/* 4033 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getCounter_result
/*      */     implements TBase<getCounter_result, getCounter_result._Fields>, Serializable, Cloneable
/*      */   {
/* 4043 */     private static final TStruct STRUCT_DESC = new TStruct("getCounter_result");
/*      */     
/* 4045 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)10, (short)0);
/*      */     public long success;
/*      */     private static final int __SUCCESS_ISSET_ID = 0;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 4051 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 4053 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 4056 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 4057 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 4065 */         switch (fieldId) {
/*      */           case 0:
/* 4067 */             return SUCCESS;
/*      */         } 
/* 4069 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 4078 */         _Fields fields = findByThriftId(fieldId);
/* 4079 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 4080 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 4087 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 4094 */         this._thriftId = thriftId;
/* 4095 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 4099 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 4103 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4109 */     private BitSet __isset_bit_vector = new BitSet(1);
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     static {
/* 4113 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 4114 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)10)));
/*      */       
/* 4116 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 4117 */       FieldMetaData.addStructMetaDataMap(getCounter_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounter_result(long success) {
/* 4126 */       this();
/* 4127 */       this.success = success;
/* 4128 */       setSuccessIsSet(true);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getCounter_result(getCounter_result other) {
/* 4135 */       this.__isset_bit_vector.clear();
/* 4136 */       this.__isset_bit_vector.or(other.__isset_bit_vector);
/* 4137 */       this.success = other.success;
/*      */     }
/*      */     
/*      */     public getCounter_result deepCopy() {
/* 4141 */       return new getCounter_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4146 */       setSuccessIsSet(false);
/* 4147 */       this.success = 0L;
/*      */     }
/*      */     
/*      */     public long getSuccess() {
/* 4151 */       return this.success;
/*      */     }
/*      */     
/*      */     public getCounter_result setSuccess(long success) {
/* 4155 */       this.success = success;
/* 4156 */       setSuccessIsSet(true);
/* 4157 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 4161 */       this.__isset_bit_vector.clear(0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 4166 */       return this.__isset_bit_vector.get(0);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 4170 */       this.__isset_bit_vector.set(0, value);
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 4174 */       switch (field) {
/*      */         case null:
/* 4176 */           if (value == null) {
/* 4177 */             unsetSuccess(); break;
/*      */           } 
/* 4179 */           setSuccess(((Long)value).longValue());
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 4187 */       switch (field) {
/*      */         case null:
/* 4189 */           return new Long(getSuccess());
/*      */       } 
/*      */       
/* 4192 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 4197 */       if (field == null) {
/* 4198 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 4201 */       switch (field) {
/*      */         case null:
/* 4203 */           return isSetSuccess();
/*      */       } 
/* 4205 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 4210 */       if (that == null)
/* 4211 */         return false; 
/* 4212 */       if (that instanceof getCounter_result)
/* 4213 */         return equals((getCounter_result)that); 
/* 4214 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getCounter_result that) {
/* 4218 */       if (that == null) {
/* 4219 */         return false;
/*      */       }
/* 4221 */       boolean this_present_success = true;
/* 4222 */       boolean that_present_success = true;
/* 4223 */       if (this_present_success || that_present_success) {
/* 4224 */         if (!this_present_success || !that_present_success)
/* 4225 */           return false; 
/* 4226 */         if (this.success != that.success) {
/* 4227 */           return false;
/*      */         }
/*      */       } 
/* 4230 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 4235 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getCounter_result other) {
/* 4239 */       if (!getClass().equals(other.getClass())) {
/* 4240 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 4243 */       int lastComparison = 0;
/* 4244 */       getCounter_result typedOther = other;
/*      */       
/* 4246 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 4247 */       if (lastComparison != 0) {
/* 4248 */         return lastComparison;
/*      */       }
/* 4250 */       if (isSetSuccess()) {
/* 4251 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 4252 */         if (lastComparison != 0) {
/* 4253 */           return lastComparison;
/*      */         }
/*      */       } 
/* 4256 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 4260 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 4265 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 4268 */         TField field = iprot.readFieldBegin();
/* 4269 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 4272 */         switch (field.id) {
/*      */           case 0:
/* 4274 */             if (field.type == 10) {
/* 4275 */               this.success = iprot.readI64();
/* 4276 */               setSuccessIsSet(true); break;
/*      */             } 
/* 4278 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 4282 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 4284 */         iprot.readFieldEnd();
/*      */       } 
/* 4286 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 4289 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 4293 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 4295 */       if (isSetSuccess()) {
/* 4296 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 4297 */         oprot.writeI64(this.success);
/* 4298 */         oprot.writeFieldEnd();
/*      */       } 
/* 4300 */       oprot.writeFieldStop();
/* 4301 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4306 */       StringBuilder sb = new StringBuilder("getCounter_result(");
/* 4307 */       boolean first = true;
/*      */       
/* 4309 */       sb.append("success:");
/* 4310 */       sb.append(this.success);
/* 4311 */       first = false;
/* 4312 */       sb.append(")");
/* 4313 */       return sb.toString();
/*      */     }
/*      */     
/*      */     public void validate() throws TException {}
/*      */     
/*      */     public getCounter_result() {}
/*      */   }
/*      */   
/*      */   public static class setOption_args
/*      */     implements TBase<setOption_args, setOption_args._Fields>, Serializable, Cloneable {
/* 4323 */     private static final TStruct STRUCT_DESC = new TStruct("setOption_args");
/*      */     
/* 4325 */     private static final TField KEY_FIELD_DESC = new TField("key", (byte)11, (short)1);
/* 4326 */     private static final TField VALUE_FIELD_DESC = new TField("value", (byte)11, (short)2);
/*      */     public String key;
/*      */     public String value;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 4333 */       KEY((short)1, "key"),
/* 4334 */       VALUE((short)2, "value");
/*      */       
/* 4336 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 4339 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 4340 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 4348 */         switch (fieldId) {
/*      */           case 1:
/* 4350 */             return KEY;
/*      */           case 2:
/* 4352 */             return VALUE;
/*      */         } 
/* 4354 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 4363 */         _Fields fields = findByThriftId(fieldId);
/* 4364 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 4365 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 4372 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 4379 */         this._thriftId = thriftId;
/* 4380 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 4384 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 4388 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 4396 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 4397 */       tmpMap.put(_Fields.KEY, new FieldMetaData("key", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 4399 */       tmpMap.put(_Fields.VALUE, new FieldMetaData("value", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 4401 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 4402 */       FieldMetaData.addStructMetaDataMap(setOption_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public setOption_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public setOption_args(String key, String value) {
/* 4412 */       this();
/* 4413 */       this.key = key;
/* 4414 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public setOption_args(setOption_args other) {
/* 4421 */       if (other.isSetKey()) {
/* 4422 */         this.key = other.key;
/*      */       }
/* 4424 */       if (other.isSetValue()) {
/* 4425 */         this.value = other.value;
/*      */       }
/*      */     }
/*      */     
/*      */     public setOption_args deepCopy() {
/* 4430 */       return new setOption_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4435 */       this.key = null;
/* 4436 */       this.value = null;
/*      */     }
/*      */     
/*      */     public String getKey() {
/* 4440 */       return this.key;
/*      */     }
/*      */     
/*      */     public setOption_args setKey(String key) {
/* 4444 */       this.key = key;
/* 4445 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetKey() {
/* 4449 */       this.key = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetKey() {
/* 4454 */       return (this.key != null);
/*      */     }
/*      */     
/*      */     public void setKeyIsSet(boolean value) {
/* 4458 */       if (!value) {
/* 4459 */         this.key = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public String getValue() {
/* 4464 */       return this.value;
/*      */     }
/*      */     
/*      */     public setOption_args setValue(String value) {
/* 4468 */       this.value = value;
/* 4469 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetValue() {
/* 4473 */       this.value = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetValue() {
/* 4478 */       return (this.value != null);
/*      */     }
/*      */     
/*      */     public void setValueIsSet(boolean value) {
/* 4482 */       if (!value) {
/* 4483 */         this.value = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 4488 */       switch (field) {
/*      */         case null:
/* 4490 */           if (value == null) {
/* 4491 */             unsetKey(); break;
/*      */           } 
/* 4493 */           setKey((String)value);
/*      */           break;
/*      */ 
/*      */         
/*      */         case null:
/* 4498 */           if (value == null) {
/* 4499 */             unsetValue(); break;
/*      */           } 
/* 4501 */           setValue((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 4509 */       switch (field) {
/*      */         case null:
/* 4511 */           return getKey();
/*      */         
/*      */         case null:
/* 4514 */           return getValue();
/*      */       } 
/*      */       
/* 4517 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 4522 */       if (field == null) {
/* 4523 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 4526 */       switch (field) {
/*      */         case null:
/* 4528 */           return isSetKey();
/*      */         case null:
/* 4530 */           return isSetValue();
/*      */       } 
/* 4532 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 4537 */       if (that == null)
/* 4538 */         return false; 
/* 4539 */       if (that instanceof setOption_args)
/* 4540 */         return equals((setOption_args)that); 
/* 4541 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(setOption_args that) {
/* 4545 */       if (that == null) {
/* 4546 */         return false;
/*      */       }
/* 4548 */       boolean this_present_key = isSetKey();
/* 4549 */       boolean that_present_key = that.isSetKey();
/* 4550 */       if (this_present_key || that_present_key) {
/* 4551 */         if (!this_present_key || !that_present_key)
/* 4552 */           return false; 
/* 4553 */         if (!this.key.equals(that.key)) {
/* 4554 */           return false;
/*      */         }
/*      */       } 
/* 4557 */       boolean this_present_value = isSetValue();
/* 4558 */       boolean that_present_value = that.isSetValue();
/* 4559 */       if (this_present_value || that_present_value) {
/* 4560 */         if (!this_present_value || !that_present_value)
/* 4561 */           return false; 
/* 4562 */         if (!this.value.equals(that.value)) {
/* 4563 */           return false;
/*      */         }
/*      */       } 
/* 4566 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 4571 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(setOption_args other) {
/* 4575 */       if (!getClass().equals(other.getClass())) {
/* 4576 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 4579 */       int lastComparison = 0;
/* 4580 */       setOption_args typedOther = other;
/*      */       
/* 4582 */       lastComparison = Boolean.valueOf(isSetKey()).compareTo(Boolean.valueOf(typedOther.isSetKey()));
/* 4583 */       if (lastComparison != 0) {
/* 4584 */         return lastComparison;
/*      */       }
/* 4586 */       if (isSetKey()) {
/* 4587 */         lastComparison = TBaseHelper.compareTo(this.key, typedOther.key);
/* 4588 */         if (lastComparison != 0) {
/* 4589 */           return lastComparison;
/*      */         }
/*      */       } 
/* 4592 */       lastComparison = Boolean.valueOf(isSetValue()).compareTo(Boolean.valueOf(typedOther.isSetValue()));
/* 4593 */       if (lastComparison != 0) {
/* 4594 */         return lastComparison;
/*      */       }
/* 4596 */       if (isSetValue()) {
/* 4597 */         lastComparison = TBaseHelper.compareTo(this.value, typedOther.value);
/* 4598 */         if (lastComparison != 0) {
/* 4599 */           return lastComparison;
/*      */         }
/*      */       } 
/* 4602 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 4606 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 4611 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 4614 */         TField field = iprot.readFieldBegin();
/* 4615 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 4618 */         switch (field.id) {
/*      */           case 1:
/* 4620 */             if (field.type == 11) {
/* 4621 */               this.key = iprot.readString(); break;
/*      */             } 
/* 4623 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           case 2:
/* 4627 */             if (field.type == 11) {
/* 4628 */               this.value = iprot.readString(); break;
/*      */             } 
/* 4630 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 4634 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 4636 */         iprot.readFieldEnd();
/*      */       } 
/* 4638 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 4641 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 4645 */       validate();
/*      */       
/* 4647 */       oprot.writeStructBegin(STRUCT_DESC);
/* 4648 */       if (this.key != null) {
/* 4649 */         oprot.writeFieldBegin(KEY_FIELD_DESC);
/* 4650 */         oprot.writeString(this.key);
/* 4651 */         oprot.writeFieldEnd();
/*      */       } 
/* 4653 */       if (this.value != null) {
/* 4654 */         oprot.writeFieldBegin(VALUE_FIELD_DESC);
/* 4655 */         oprot.writeString(this.value);
/* 4656 */         oprot.writeFieldEnd();
/*      */       } 
/* 4658 */       oprot.writeFieldStop();
/* 4659 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4664 */       StringBuilder sb = new StringBuilder("setOption_args(");
/* 4665 */       boolean first = true;
/*      */       
/* 4667 */       sb.append("key:");
/* 4668 */       if (this.key == null) {
/* 4669 */         sb.append("null");
/*      */       } else {
/* 4671 */         sb.append(this.key);
/*      */       } 
/* 4673 */       first = false;
/* 4674 */       if (!first) sb.append(", "); 
/* 4675 */       sb.append("value:");
/* 4676 */       if (this.value == null) {
/* 4677 */         sb.append("null");
/*      */       } else {
/* 4679 */         sb.append(this.value);
/*      */       } 
/* 4681 */       first = false;
/* 4682 */       sb.append(")");
/* 4683 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class setOption_result
/*      */     implements TBase<setOption_result, setOption_result._Fields>, Serializable, Cloneable
/*      */   {
/* 4693 */     private static final TStruct STRUCT_DESC = new TStruct("setOption_result");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 4701 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 4704 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 4705 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 4713 */         switch (fieldId) {
/*      */         
/* 4715 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 4724 */         _Fields fields = findByThriftId(fieldId);
/* 4725 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 4726 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 4733 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 4740 */         this._thriftId = thriftId;
/* 4741 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 4745 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 4749 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 4754 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 4755 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 4756 */       FieldMetaData.addStructMetaDataMap(setOption_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public setOption_result() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public setOption_result(setOption_result other) {}
/*      */ 
/*      */     
/*      */     public setOption_result deepCopy() {
/* 4769 */       return new setOption_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 4777 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$setOption_result$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 4782 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$setOption_result$_Fields[field.ordinal()];
/*      */       
/* 4784 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 4789 */       if (field == null) {
/* 4790 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 4793 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$setOption_result$_Fields[field.ordinal()];
/*      */       
/* 4795 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 4800 */       if (that == null)
/* 4801 */         return false; 
/* 4802 */       if (that instanceof setOption_result)
/* 4803 */         return equals((setOption_result)that); 
/* 4804 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(setOption_result that) {
/* 4808 */       if (that == null) {
/* 4809 */         return false;
/*      */       }
/* 4811 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 4816 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(setOption_result other) {
/* 4820 */       if (!getClass().equals(other.getClass())) {
/* 4821 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 4824 */       int lastComparison = 0;
/* 4825 */       setOption_result typedOther = other;
/*      */       
/* 4827 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 4831 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 4836 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 4839 */         TField field = iprot.readFieldBegin();
/* 4840 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 4843 */         switch (field.id) {
/*      */         
/* 4845 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 4847 */         iprot.readFieldEnd();
/*      */       } 
/* 4849 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 4852 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 4856 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 4858 */       oprot.writeFieldStop();
/* 4859 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4864 */       StringBuilder sb = new StringBuilder("setOption_result(");
/* 4865 */       boolean first = true;
/*      */       
/* 4867 */       sb.append(")");
/* 4868 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getOption_args
/*      */     implements TBase<getOption_args, getOption_args._Fields>, Serializable, Cloneable
/*      */   {
/* 4878 */     private static final TStruct STRUCT_DESC = new TStruct("getOption_args");
/*      */     
/* 4880 */     private static final TField KEY_FIELD_DESC = new TField("key", (byte)11, (short)1);
/*      */     public String key;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 4886 */       KEY((short)1, "key"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 4888 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 4891 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 4892 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 4900 */         switch (fieldId) {
/*      */           case 1:
/* 4902 */             return KEY;
/*      */         } 
/* 4904 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 4913 */         _Fields fields = findByThriftId(fieldId);
/* 4914 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 4915 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 4922 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 4929 */         this._thriftId = thriftId;
/* 4930 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 4934 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 4938 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 4946 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 4947 */       tmpMap.put(_Fields.KEY, new FieldMetaData("key", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 4949 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 4950 */       FieldMetaData.addStructMetaDataMap(getOption_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getOption_args() {}
/*      */ 
/*      */     
/*      */     public getOption_args(String key) {
/* 4959 */       this();
/* 4960 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getOption_args(getOption_args other) {
/* 4967 */       if (other.isSetKey()) {
/* 4968 */         this.key = other.key;
/*      */       }
/*      */     }
/*      */     
/*      */     public getOption_args deepCopy() {
/* 4973 */       return new getOption_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4978 */       this.key = null;
/*      */     }
/*      */     
/*      */     public String getKey() {
/* 4982 */       return this.key;
/*      */     }
/*      */     
/*      */     public getOption_args setKey(String key) {
/* 4986 */       this.key = key;
/* 4987 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetKey() {
/* 4991 */       this.key = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetKey() {
/* 4996 */       return (this.key != null);
/*      */     }
/*      */     
/*      */     public void setKeyIsSet(boolean value) {
/* 5000 */       if (!value) {
/* 5001 */         this.key = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 5006 */       switch (field) {
/*      */         case null:
/* 5008 */           if (value == null) {
/* 5009 */             unsetKey(); break;
/*      */           } 
/* 5011 */           setKey((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 5019 */       switch (field) {
/*      */         case null:
/* 5021 */           return getKey();
/*      */       } 
/*      */       
/* 5024 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 5029 */       if (field == null) {
/* 5030 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 5033 */       switch (field) {
/*      */         case null:
/* 5035 */           return isSetKey();
/*      */       } 
/* 5037 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 5042 */       if (that == null)
/* 5043 */         return false; 
/* 5044 */       if (that instanceof getOption_args)
/* 5045 */         return equals((getOption_args)that); 
/* 5046 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getOption_args that) {
/* 5050 */       if (that == null) {
/* 5051 */         return false;
/*      */       }
/* 5053 */       boolean this_present_key = isSetKey();
/* 5054 */       boolean that_present_key = that.isSetKey();
/* 5055 */       if (this_present_key || that_present_key) {
/* 5056 */         if (!this_present_key || !that_present_key)
/* 5057 */           return false; 
/* 5058 */         if (!this.key.equals(that.key)) {
/* 5059 */           return false;
/*      */         }
/*      */       } 
/* 5062 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 5067 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getOption_args other) {
/* 5071 */       if (!getClass().equals(other.getClass())) {
/* 5072 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 5075 */       int lastComparison = 0;
/* 5076 */       getOption_args typedOther = other;
/*      */       
/* 5078 */       lastComparison = Boolean.valueOf(isSetKey()).compareTo(Boolean.valueOf(typedOther.isSetKey()));
/* 5079 */       if (lastComparison != 0) {
/* 5080 */         return lastComparison;
/*      */       }
/* 5082 */       if (isSetKey()) {
/* 5083 */         lastComparison = TBaseHelper.compareTo(this.key, typedOther.key);
/* 5084 */         if (lastComparison != 0) {
/* 5085 */           return lastComparison;
/*      */         }
/*      */       } 
/* 5088 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 5092 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 5097 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 5100 */         TField field = iprot.readFieldBegin();
/* 5101 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 5104 */         switch (field.id) {
/*      */           case 1:
/* 5106 */             if (field.type == 11) {
/* 5107 */               this.key = iprot.readString(); break;
/*      */             } 
/* 5109 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 5113 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 5115 */         iprot.readFieldEnd();
/*      */       } 
/* 5117 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 5120 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 5124 */       validate();
/*      */       
/* 5126 */       oprot.writeStructBegin(STRUCT_DESC);
/* 5127 */       if (this.key != null) {
/* 5128 */         oprot.writeFieldBegin(KEY_FIELD_DESC);
/* 5129 */         oprot.writeString(this.key);
/* 5130 */         oprot.writeFieldEnd();
/*      */       } 
/* 5132 */       oprot.writeFieldStop();
/* 5133 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 5138 */       StringBuilder sb = new StringBuilder("getOption_args(");
/* 5139 */       boolean first = true;
/*      */       
/* 5141 */       sb.append("key:");
/* 5142 */       if (this.key == null) {
/* 5143 */         sb.append("null");
/*      */       } else {
/* 5145 */         sb.append(this.key);
/*      */       } 
/* 5147 */       first = false;
/* 5148 */       sb.append(")");
/* 5149 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getOption_result
/*      */     implements TBase<getOption_result, getOption_result._Fields>, Serializable, Cloneable
/*      */   {
/* 5159 */     private static final TStruct STRUCT_DESC = new TStruct("getOption_result");
/*      */     
/* 5161 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
/*      */     public String success;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 5167 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 5169 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 5172 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 5173 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 5181 */         switch (fieldId) {
/*      */           case 0:
/* 5183 */             return SUCCESS;
/*      */         } 
/* 5185 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 5194 */         _Fields fields = findByThriftId(fieldId);
/* 5195 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 5196 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 5203 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 5210 */         this._thriftId = thriftId;
/* 5211 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 5215 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 5219 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 5227 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 5228 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 5230 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 5231 */       FieldMetaData.addStructMetaDataMap(getOption_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getOption_result() {}
/*      */ 
/*      */     
/*      */     public getOption_result(String success) {
/* 5240 */       this();
/* 5241 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getOption_result(getOption_result other) {
/* 5248 */       if (other.isSetSuccess()) {
/* 5249 */         this.success = other.success;
/*      */       }
/*      */     }
/*      */     
/*      */     public getOption_result deepCopy() {
/* 5254 */       return new getOption_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 5259 */       this.success = null;
/*      */     }
/*      */     
/*      */     public String getSuccess() {
/* 5263 */       return this.success;
/*      */     }
/*      */     
/*      */     public getOption_result setSuccess(String success) {
/* 5267 */       this.success = success;
/* 5268 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 5272 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 5277 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 5281 */       if (!value) {
/* 5282 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 5287 */       switch (field) {
/*      */         case null:
/* 5289 */           if (value == null) {
/* 5290 */             unsetSuccess(); break;
/*      */           } 
/* 5292 */           setSuccess((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 5300 */       switch (field) {
/*      */         case null:
/* 5302 */           return getSuccess();
/*      */       } 
/*      */       
/* 5305 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 5310 */       if (field == null) {
/* 5311 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 5314 */       switch (field) {
/*      */         case null:
/* 5316 */           return isSetSuccess();
/*      */       } 
/* 5318 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 5323 */       if (that == null)
/* 5324 */         return false; 
/* 5325 */       if (that instanceof getOption_result)
/* 5326 */         return equals((getOption_result)that); 
/* 5327 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getOption_result that) {
/* 5331 */       if (that == null) {
/* 5332 */         return false;
/*      */       }
/* 5334 */       boolean this_present_success = isSetSuccess();
/* 5335 */       boolean that_present_success = that.isSetSuccess();
/* 5336 */       if (this_present_success || that_present_success) {
/* 5337 */         if (!this_present_success || !that_present_success)
/* 5338 */           return false; 
/* 5339 */         if (!this.success.equals(that.success)) {
/* 5340 */           return false;
/*      */         }
/*      */       } 
/* 5343 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 5348 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getOption_result other) {
/* 5352 */       if (!getClass().equals(other.getClass())) {
/* 5353 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 5356 */       int lastComparison = 0;
/* 5357 */       getOption_result typedOther = other;
/*      */       
/* 5359 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 5360 */       if (lastComparison != 0) {
/* 5361 */         return lastComparison;
/*      */       }
/* 5363 */       if (isSetSuccess()) {
/* 5364 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 5365 */         if (lastComparison != 0) {
/* 5366 */           return lastComparison;
/*      */         }
/*      */       } 
/* 5369 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 5373 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 5378 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 5381 */         TField field = iprot.readFieldBegin();
/* 5382 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 5385 */         switch (field.id) {
/*      */           case 0:
/* 5387 */             if (field.type == 11) {
/* 5388 */               this.success = iprot.readString(); break;
/*      */             } 
/* 5390 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 5394 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 5396 */         iprot.readFieldEnd();
/*      */       } 
/* 5398 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 5401 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 5405 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 5407 */       if (isSetSuccess()) {
/* 5408 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 5409 */         oprot.writeString(this.success);
/* 5410 */         oprot.writeFieldEnd();
/*      */       } 
/* 5412 */       oprot.writeFieldStop();
/* 5413 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 5418 */       StringBuilder sb = new StringBuilder("getOption_result(");
/* 5419 */       boolean first = true;
/*      */       
/* 5421 */       sb.append("success:");
/* 5422 */       if (this.success == null) {
/* 5423 */         sb.append("null");
/*      */       } else {
/* 5425 */         sb.append(this.success);
/*      */       } 
/* 5427 */       first = false;
/* 5428 */       sb.append(")");
/* 5429 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getOptions_args
/*      */     implements TBase<getOptions_args, getOptions_args._Fields>, Serializable, Cloneable
/*      */   {
/* 5439 */     private static final TStruct STRUCT_DESC = new TStruct("getOptions_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 5447 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 5450 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 5451 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 5459 */         switch (fieldId) {
/*      */         
/* 5461 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 5470 */         _Fields fields = findByThriftId(fieldId);
/* 5471 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 5472 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 5479 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 5486 */         this._thriftId = thriftId;
/* 5487 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 5491 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 5495 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 5500 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 5501 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 5502 */       FieldMetaData.addStructMetaDataMap(getOptions_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getOptions_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public getOptions_args(getOptions_args other) {}
/*      */ 
/*      */     
/*      */     public getOptions_args deepCopy() {
/* 5515 */       return new getOptions_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 5523 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getOptions_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 5528 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getOptions_args$_Fields[field.ordinal()];
/*      */       
/* 5530 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 5535 */       if (field == null) {
/* 5536 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 5539 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$getOptions_args$_Fields[field.ordinal()];
/*      */       
/* 5541 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 5546 */       if (that == null)
/* 5547 */         return false; 
/* 5548 */       if (that instanceof getOptions_args)
/* 5549 */         return equals((getOptions_args)that); 
/* 5550 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getOptions_args that) {
/* 5554 */       if (that == null) {
/* 5555 */         return false;
/*      */       }
/* 5557 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 5562 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getOptions_args other) {
/* 5566 */       if (!getClass().equals(other.getClass())) {
/* 5567 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 5570 */       int lastComparison = 0;
/* 5571 */       getOptions_args typedOther = other;
/*      */       
/* 5573 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 5577 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 5582 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 5585 */         TField field = iprot.readFieldBegin();
/* 5586 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 5589 */         switch (field.id) {
/*      */         
/* 5591 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 5593 */         iprot.readFieldEnd();
/*      */       } 
/* 5595 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 5598 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 5602 */       validate();
/*      */       
/* 5604 */       oprot.writeStructBegin(STRUCT_DESC);
/* 5605 */       oprot.writeFieldStop();
/* 5606 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 5611 */       StringBuilder sb = new StringBuilder("getOptions_args(");
/* 5612 */       boolean first = true;
/*      */       
/* 5614 */       sb.append(")");
/* 5615 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getOptions_result
/*      */     implements TBase<getOptions_result, getOptions_result._Fields>, Serializable, Cloneable
/*      */   {
/* 5625 */     private static final TStruct STRUCT_DESC = new TStruct("getOptions_result");
/*      */     
/* 5627 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)13, (short)0);
/*      */     public Map<String, String> success;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 5633 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 5635 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 5638 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 5639 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 5647 */         switch (fieldId) {
/*      */           case 0:
/* 5649 */             return SUCCESS;
/*      */         } 
/* 5651 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 5660 */         _Fields fields = findByThriftId(fieldId);
/* 5661 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 5662 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 5669 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 5676 */         this._thriftId = thriftId;
/* 5677 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 5681 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 5685 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 5693 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 5694 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, (FieldValueMetaData)new MapMetaData((byte)13, new FieldValueMetaData((byte)11), new FieldValueMetaData((byte)11))));
/*      */ 
/*      */ 
/*      */       
/* 5698 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 5699 */       FieldMetaData.addStructMetaDataMap(getOptions_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getOptions_result() {}
/*      */ 
/*      */     
/*      */     public getOptions_result(Map<String, String> success) {
/* 5708 */       this();
/* 5709 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getOptions_result(getOptions_result other) {
/* 5716 */       if (other.isSetSuccess()) {
/* 5717 */         Map<String, String> __this__success = new HashMap<String, String>();
/* 5718 */         for (Map.Entry<String, String> other_element : other.success.entrySet()) {
/*      */           
/* 5720 */           String other_element_key = other_element.getKey();
/* 5721 */           String other_element_value = other_element.getValue();
/*      */           
/* 5723 */           String __this__success_copy_key = other_element_key;
/*      */           
/* 5725 */           String __this__success_copy_value = other_element_value;
/*      */           
/* 5727 */           __this__success.put(__this__success_copy_key, __this__success_copy_value);
/*      */         } 
/* 5729 */         this.success = __this__success;
/*      */       } 
/*      */     }
/*      */     
/*      */     public getOptions_result deepCopy() {
/* 5734 */       return new getOptions_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 5739 */       this.success = null;
/*      */     }
/*      */     
/*      */     public int getSuccessSize() {
/* 5743 */       return (this.success == null) ? 0 : this.success.size();
/*      */     }
/*      */     
/*      */     public void putToSuccess(String key, String val) {
/* 5747 */       if (this.success == null) {
/* 5748 */         this.success = new HashMap<String, String>();
/*      */       }
/* 5750 */       this.success.put(key, val);
/*      */     }
/*      */     
/*      */     public Map<String, String> getSuccess() {
/* 5754 */       return this.success;
/*      */     }
/*      */     
/*      */     public getOptions_result setSuccess(Map<String, String> success) {
/* 5758 */       this.success = success;
/* 5759 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 5763 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 5768 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 5772 */       if (!value) {
/* 5773 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 5778 */       switch (field) {
/*      */         case null:
/* 5780 */           if (value == null) {
/* 5781 */             unsetSuccess(); break;
/*      */           } 
/* 5783 */           setSuccess((Map<String, String>)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 5791 */       switch (field) {
/*      */         case null:
/* 5793 */           return getSuccess();
/*      */       } 
/*      */       
/* 5796 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 5801 */       if (field == null) {
/* 5802 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 5805 */       switch (field) {
/*      */         case null:
/* 5807 */           return isSetSuccess();
/*      */       } 
/* 5809 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 5814 */       if (that == null)
/* 5815 */         return false; 
/* 5816 */       if (that instanceof getOptions_result)
/* 5817 */         return equals((getOptions_result)that); 
/* 5818 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getOptions_result that) {
/* 5822 */       if (that == null) {
/* 5823 */         return false;
/*      */       }
/* 5825 */       boolean this_present_success = isSetSuccess();
/* 5826 */       boolean that_present_success = that.isSetSuccess();
/* 5827 */       if (this_present_success || that_present_success) {
/* 5828 */         if (!this_present_success || !that_present_success)
/* 5829 */           return false; 
/* 5830 */         if (!this.success.equals(that.success)) {
/* 5831 */           return false;
/*      */         }
/*      */       } 
/* 5834 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 5839 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getOptions_result other) {
/* 5843 */       if (!getClass().equals(other.getClass())) {
/* 5844 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 5847 */       int lastComparison = 0;
/* 5848 */       getOptions_result typedOther = other;
/*      */       
/* 5850 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 5851 */       if (lastComparison != 0) {
/* 5852 */         return lastComparison;
/*      */       }
/* 5854 */       if (isSetSuccess()) {
/* 5855 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 5856 */         if (lastComparison != 0) {
/* 5857 */           return lastComparison;
/*      */         }
/*      */       } 
/* 5860 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 5864 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 5869 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 5872 */         TField field = iprot.readFieldBegin();
/* 5873 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 5876 */         switch (field.id) {
/*      */           case 0:
/* 5878 */             if (field.type == 13) {
/*      */               
/* 5880 */               TMap _map5 = iprot.readMapBegin();
/* 5881 */               this.success = new HashMap<String, String>(2 * _map5.size);
/* 5882 */               for (int _i6 = 0; _i6 < _map5.size; _i6++) {
/*      */ 
/*      */ 
/*      */                 
/* 5886 */                 String _key7 = iprot.readString();
/* 5887 */                 String _val8 = iprot.readString();
/* 5888 */                 this.success.put(_key7, _val8);
/*      */               } 
/* 5890 */               iprot.readMapEnd();
/*      */               break;
/*      */             } 
/* 5893 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 5897 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 5899 */         iprot.readFieldEnd();
/*      */       } 
/* 5901 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 5904 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 5908 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 5910 */       if (isSetSuccess()) {
/* 5911 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/*      */         
/* 5913 */         oprot.writeMapBegin(new TMap((byte)11, (byte)11, this.success.size()));
/* 5914 */         for (Map.Entry<String, String> _iter9 : this.success.entrySet()) {
/*      */           
/* 5916 */           oprot.writeString(_iter9.getKey());
/* 5917 */           oprot.writeString(_iter9.getValue());
/*      */         } 
/* 5919 */         oprot.writeMapEnd();
/*      */         
/* 5921 */         oprot.writeFieldEnd();
/*      */       } 
/* 5923 */       oprot.writeFieldStop();
/* 5924 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 5929 */       StringBuilder sb = new StringBuilder("getOptions_result(");
/* 5930 */       boolean first = true;
/*      */       
/* 5932 */       sb.append("success:");
/* 5933 */       if (this.success == null) {
/* 5934 */         sb.append("null");
/*      */       } else {
/* 5936 */         sb.append(this.success);
/*      */       } 
/* 5938 */       first = false;
/* 5939 */       sb.append(")");
/* 5940 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class getCpuProfile_args
/*      */     implements TBase<getCpuProfile_args, getCpuProfile_args._Fields>, Serializable, Cloneable
/*      */   {
/* 5950 */     private static final TStruct STRUCT_DESC = new TStruct("getCpuProfile_args");
/*      */     
/* 5952 */     private static final TField PROFILE_DURATION_IN_SEC_FIELD_DESC = new TField("profileDurationInSec", (byte)8, (short)1);
/*      */     public int profileDurationInSec;
/*      */     private static final int __PROFILEDURATIONINSEC_ISSET_ID = 0;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 5958 */       PROFILE_DURATION_IN_SEC((short)1, "profileDurationInSec"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 5960 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 5963 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 5964 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 5972 */         switch (fieldId) {
/*      */           case 1:
/* 5974 */             return PROFILE_DURATION_IN_SEC;
/*      */         } 
/* 5976 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 5985 */         _Fields fields = findByThriftId(fieldId);
/* 5986 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 5987 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 5994 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 6001 */         this._thriftId = thriftId;
/* 6002 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 6006 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 6010 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6016 */     private BitSet __isset_bit_vector = new BitSet(1);
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     static {
/* 6020 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 6021 */       tmpMap.put(_Fields.PROFILE_DURATION_IN_SEC, new FieldMetaData("profileDurationInSec", (byte)3, new FieldValueMetaData((byte)8)));
/*      */       
/* 6023 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 6024 */       FieldMetaData.addStructMetaDataMap(getCpuProfile_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getCpuProfile_args(int profileDurationInSec) {
/* 6033 */       this();
/* 6034 */       this.profileDurationInSec = profileDurationInSec;
/* 6035 */       setProfileDurationInSecIsSet(true);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getCpuProfile_args(getCpuProfile_args other) {
/* 6042 */       this.__isset_bit_vector.clear();
/* 6043 */       this.__isset_bit_vector.or(other.__isset_bit_vector);
/* 6044 */       this.profileDurationInSec = other.profileDurationInSec;
/*      */     }
/*      */     
/*      */     public getCpuProfile_args deepCopy() {
/* 6048 */       return new getCpuProfile_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 6053 */       setProfileDurationInSecIsSet(false);
/* 6054 */       this.profileDurationInSec = 0;
/*      */     }
/*      */     
/*      */     public int getProfileDurationInSec() {
/* 6058 */       return this.profileDurationInSec;
/*      */     }
/*      */     
/*      */     public getCpuProfile_args setProfileDurationInSec(int profileDurationInSec) {
/* 6062 */       this.profileDurationInSec = profileDurationInSec;
/* 6063 */       setProfileDurationInSecIsSet(true);
/* 6064 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetProfileDurationInSec() {
/* 6068 */       this.__isset_bit_vector.clear(0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetProfileDurationInSec() {
/* 6073 */       return this.__isset_bit_vector.get(0);
/*      */     }
/*      */     
/*      */     public void setProfileDurationInSecIsSet(boolean value) {
/* 6077 */       this.__isset_bit_vector.set(0, value);
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 6081 */       switch (field) {
/*      */         case null:
/* 6083 */           if (value == null) {
/* 6084 */             unsetProfileDurationInSec(); break;
/*      */           } 
/* 6086 */           setProfileDurationInSec(((Integer)value).intValue());
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 6094 */       switch (field) {
/*      */         case null:
/* 6096 */           return new Integer(getProfileDurationInSec());
/*      */       } 
/*      */       
/* 6099 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 6104 */       if (field == null) {
/* 6105 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 6108 */       switch (field) {
/*      */         case null:
/* 6110 */           return isSetProfileDurationInSec();
/*      */       } 
/* 6112 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 6117 */       if (that == null)
/* 6118 */         return false; 
/* 6119 */       if (that instanceof getCpuProfile_args)
/* 6120 */         return equals((getCpuProfile_args)that); 
/* 6121 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getCpuProfile_args that) {
/* 6125 */       if (that == null) {
/* 6126 */         return false;
/*      */       }
/* 6128 */       boolean this_present_profileDurationInSec = true;
/* 6129 */       boolean that_present_profileDurationInSec = true;
/* 6130 */       if (this_present_profileDurationInSec || that_present_profileDurationInSec) {
/* 6131 */         if (!this_present_profileDurationInSec || !that_present_profileDurationInSec)
/* 6132 */           return false; 
/* 6133 */         if (this.profileDurationInSec != that.profileDurationInSec) {
/* 6134 */           return false;
/*      */         }
/*      */       } 
/* 6137 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 6142 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getCpuProfile_args other) {
/* 6146 */       if (!getClass().equals(other.getClass())) {
/* 6147 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 6150 */       int lastComparison = 0;
/* 6151 */       getCpuProfile_args typedOther = other;
/*      */       
/* 6153 */       lastComparison = Boolean.valueOf(isSetProfileDurationInSec()).compareTo(Boolean.valueOf(typedOther.isSetProfileDurationInSec()));
/* 6154 */       if (lastComparison != 0) {
/* 6155 */         return lastComparison;
/*      */       }
/* 6157 */       if (isSetProfileDurationInSec()) {
/* 6158 */         lastComparison = TBaseHelper.compareTo(this.profileDurationInSec, typedOther.profileDurationInSec);
/* 6159 */         if (lastComparison != 0) {
/* 6160 */           return lastComparison;
/*      */         }
/*      */       } 
/* 6163 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 6167 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 6172 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 6175 */         TField field = iprot.readFieldBegin();
/* 6176 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 6179 */         switch (field.id) {
/*      */           case 1:
/* 6181 */             if (field.type == 8) {
/* 6182 */               this.profileDurationInSec = iprot.readI32();
/* 6183 */               setProfileDurationInSecIsSet(true); break;
/*      */             } 
/* 6185 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 6189 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 6191 */         iprot.readFieldEnd();
/*      */       } 
/* 6193 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 6196 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 6200 */       validate();
/*      */       
/* 6202 */       oprot.writeStructBegin(STRUCT_DESC);
/* 6203 */       oprot.writeFieldBegin(PROFILE_DURATION_IN_SEC_FIELD_DESC);
/* 6204 */       oprot.writeI32(this.profileDurationInSec);
/* 6205 */       oprot.writeFieldEnd();
/* 6206 */       oprot.writeFieldStop();
/* 6207 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 6212 */       StringBuilder sb = new StringBuilder("getCpuProfile_args(");
/* 6213 */       boolean first = true;
/*      */       
/* 6215 */       sb.append("profileDurationInSec:");
/* 6216 */       sb.append(this.profileDurationInSec);
/* 6217 */       first = false;
/* 6218 */       sb.append(")");
/* 6219 */       return sb.toString();
/*      */     }
/*      */     
/*      */     public void validate() throws TException {}
/*      */     
/*      */     public getCpuProfile_args() {}
/*      */   }
/*      */   
/*      */   public static class getCpuProfile_result
/*      */     implements TBase<getCpuProfile_result, getCpuProfile_result._Fields>, Serializable, Cloneable {
/* 6229 */     private static final TStruct STRUCT_DESC = new TStruct("getCpuProfile_result");
/*      */     
/* 6231 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)11, (short)0);
/*      */     public String success;
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 6237 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 6239 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 6242 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 6243 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 6251 */         switch (fieldId) {
/*      */           case 0:
/* 6253 */             return SUCCESS;
/*      */         } 
/* 6255 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 6264 */         _Fields fields = findByThriftId(fieldId);
/* 6265 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 6266 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 6273 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 6280 */         this._thriftId = thriftId;
/* 6281 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 6285 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 6289 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 6297 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 6298 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)11)));
/*      */       
/* 6300 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 6301 */       FieldMetaData.addStructMetaDataMap(getCpuProfile_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public getCpuProfile_result() {}
/*      */ 
/*      */     
/*      */     public getCpuProfile_result(String success) {
/* 6310 */       this();
/* 6311 */       this.success = success;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public getCpuProfile_result(getCpuProfile_result other) {
/* 6318 */       if (other.isSetSuccess()) {
/* 6319 */         this.success = other.success;
/*      */       }
/*      */     }
/*      */     
/*      */     public getCpuProfile_result deepCopy() {
/* 6324 */       return new getCpuProfile_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 6329 */       this.success = null;
/*      */     }
/*      */     
/*      */     public String getSuccess() {
/* 6333 */       return this.success;
/*      */     }
/*      */     
/*      */     public getCpuProfile_result setSuccess(String success) {
/* 6337 */       this.success = success;
/* 6338 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 6342 */       this.success = null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 6347 */       return (this.success != null);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 6351 */       if (!value) {
/* 6352 */         this.success = null;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 6357 */       switch (field) {
/*      */         case null:
/* 6359 */           if (value == null) {
/* 6360 */             unsetSuccess(); break;
/*      */           } 
/* 6362 */           setSuccess((String)value);
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 6370 */       switch (field) {
/*      */         case null:
/* 6372 */           return getSuccess();
/*      */       } 
/*      */       
/* 6375 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 6380 */       if (field == null) {
/* 6381 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 6384 */       switch (field) {
/*      */         case null:
/* 6386 */           return isSetSuccess();
/*      */       } 
/* 6388 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 6393 */       if (that == null)
/* 6394 */         return false; 
/* 6395 */       if (that instanceof getCpuProfile_result)
/* 6396 */         return equals((getCpuProfile_result)that); 
/* 6397 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(getCpuProfile_result that) {
/* 6401 */       if (that == null) {
/* 6402 */         return false;
/*      */       }
/* 6404 */       boolean this_present_success = isSetSuccess();
/* 6405 */       boolean that_present_success = that.isSetSuccess();
/* 6406 */       if (this_present_success || that_present_success) {
/* 6407 */         if (!this_present_success || !that_present_success)
/* 6408 */           return false; 
/* 6409 */         if (!this.success.equals(that.success)) {
/* 6410 */           return false;
/*      */         }
/*      */       } 
/* 6413 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 6418 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(getCpuProfile_result other) {
/* 6422 */       if (!getClass().equals(other.getClass())) {
/* 6423 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 6426 */       int lastComparison = 0;
/* 6427 */       getCpuProfile_result typedOther = other;
/*      */       
/* 6429 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 6430 */       if (lastComparison != 0) {
/* 6431 */         return lastComparison;
/*      */       }
/* 6433 */       if (isSetSuccess()) {
/* 6434 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 6435 */         if (lastComparison != 0) {
/* 6436 */           return lastComparison;
/*      */         }
/*      */       } 
/* 6439 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 6443 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 6448 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 6451 */         TField field = iprot.readFieldBegin();
/* 6452 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 6455 */         switch (field.id) {
/*      */           case 0:
/* 6457 */             if (field.type == 11) {
/* 6458 */               this.success = iprot.readString(); break;
/*      */             } 
/* 6460 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 6464 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 6466 */         iprot.readFieldEnd();
/*      */       } 
/* 6468 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 6471 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 6475 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 6477 */       if (isSetSuccess()) {
/* 6478 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 6479 */         oprot.writeString(this.success);
/* 6480 */         oprot.writeFieldEnd();
/*      */       } 
/* 6482 */       oprot.writeFieldStop();
/* 6483 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 6488 */       StringBuilder sb = new StringBuilder("getCpuProfile_result(");
/* 6489 */       boolean first = true;
/*      */       
/* 6491 */       sb.append("success:");
/* 6492 */       if (this.success == null) {
/* 6493 */         sb.append("null");
/*      */       } else {
/* 6495 */         sb.append(this.success);
/*      */       } 
/* 6497 */       first = false;
/* 6498 */       sb.append(")");
/* 6499 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class aliveSince_args
/*      */     implements TBase<aliveSince_args, aliveSince_args._Fields>, Serializable, Cloneable
/*      */   {
/* 6509 */     private static final TStruct STRUCT_DESC = new TStruct("aliveSince_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 6517 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 6520 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 6521 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 6529 */         switch (fieldId) {
/*      */         
/* 6531 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 6540 */         _Fields fields = findByThriftId(fieldId);
/* 6541 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 6542 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 6549 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 6556 */         this._thriftId = thriftId;
/* 6557 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 6561 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 6565 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 6570 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 6571 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 6572 */       FieldMetaData.addStructMetaDataMap(aliveSince_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public aliveSince_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public aliveSince_args(aliveSince_args other) {}
/*      */ 
/*      */     
/*      */     public aliveSince_args deepCopy() {
/* 6585 */       return new aliveSince_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 6593 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$aliveSince_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 6598 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$aliveSince_args$_Fields[field.ordinal()];
/*      */       
/* 6600 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 6605 */       if (field == null) {
/* 6606 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 6609 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$aliveSince_args$_Fields[field.ordinal()];
/*      */       
/* 6611 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 6616 */       if (that == null)
/* 6617 */         return false; 
/* 6618 */       if (that instanceof aliveSince_args)
/* 6619 */         return equals((aliveSince_args)that); 
/* 6620 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(aliveSince_args that) {
/* 6624 */       if (that == null) {
/* 6625 */         return false;
/*      */       }
/* 6627 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 6632 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(aliveSince_args other) {
/* 6636 */       if (!getClass().equals(other.getClass())) {
/* 6637 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 6640 */       int lastComparison = 0;
/* 6641 */       aliveSince_args typedOther = other;
/*      */       
/* 6643 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 6647 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 6652 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 6655 */         TField field = iprot.readFieldBegin();
/* 6656 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 6659 */         switch (field.id) {
/*      */         
/* 6661 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 6663 */         iprot.readFieldEnd();
/*      */       } 
/* 6665 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 6668 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 6672 */       validate();
/*      */       
/* 6674 */       oprot.writeStructBegin(STRUCT_DESC);
/* 6675 */       oprot.writeFieldStop();
/* 6676 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 6681 */       StringBuilder sb = new StringBuilder("aliveSince_args(");
/* 6682 */       boolean first = true;
/*      */       
/* 6684 */       sb.append(")");
/* 6685 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class aliveSince_result
/*      */     implements TBase<aliveSince_result, aliveSince_result._Fields>, Serializable, Cloneable
/*      */   {
/* 6695 */     private static final TStruct STRUCT_DESC = new TStruct("aliveSince_result");
/*      */     
/* 6697 */     private static final TField SUCCESS_FIELD_DESC = new TField("success", (byte)10, (short)0);
/*      */     public long success;
/*      */     private static final int __SUCCESS_ISSET_ID = 0;
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum {
/* 6703 */       SUCCESS((short)0, "success"); private final String _fieldName;
/*      */       private final short _thriftId;
/* 6705 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();
/*      */       
/*      */       static {
/* 6708 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 6709 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 6717 */         switch (fieldId) {
/*      */           case 0:
/* 6719 */             return SUCCESS;
/*      */         } 
/* 6721 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 6730 */         _Fields fields = findByThriftId(fieldId);
/* 6731 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 6732 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 6739 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 6746 */         this._thriftId = thriftId;
/* 6747 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 6751 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 6755 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6761 */     private BitSet __isset_bit_vector = new BitSet(1);
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */     
/*      */     static {
/* 6765 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 6766 */       tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", (byte)3, new FieldValueMetaData((byte)10)));
/*      */       
/* 6768 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 6769 */       FieldMetaData.addStructMetaDataMap(aliveSince_result.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public aliveSince_result(long success) {
/* 6778 */       this();
/* 6779 */       this.success = success;
/* 6780 */       setSuccessIsSet(true);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public aliveSince_result(aliveSince_result other) {
/* 6787 */       this.__isset_bit_vector.clear();
/* 6788 */       this.__isset_bit_vector.or(other.__isset_bit_vector);
/* 6789 */       this.success = other.success;
/*      */     }
/*      */     
/*      */     public aliveSince_result deepCopy() {
/* 6793 */       return new aliveSince_result(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 6798 */       setSuccessIsSet(false);
/* 6799 */       this.success = 0L;
/*      */     }
/*      */     
/*      */     public long getSuccess() {
/* 6803 */       return this.success;
/*      */     }
/*      */     
/*      */     public aliveSince_result setSuccess(long success) {
/* 6807 */       this.success = success;
/* 6808 */       setSuccessIsSet(true);
/* 6809 */       return this;
/*      */     }
/*      */     
/*      */     public void unsetSuccess() {
/* 6813 */       this.__isset_bit_vector.clear(0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSetSuccess() {
/* 6818 */       return this.__isset_bit_vector.get(0);
/*      */     }
/*      */     
/*      */     public void setSuccessIsSet(boolean value) {
/* 6822 */       this.__isset_bit_vector.set(0, value);
/*      */     }
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 6826 */       switch (field) {
/*      */         case null:
/* 6828 */           if (value == null) {
/* 6829 */             unsetSuccess(); break;
/*      */           } 
/* 6831 */           setSuccess(((Long)value).longValue());
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 6839 */       switch (field) {
/*      */         case null:
/* 6841 */           return new Long(getSuccess());
/*      */       } 
/*      */       
/* 6844 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 6849 */       if (field == null) {
/* 6850 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 6853 */       switch (field) {
/*      */         case null:
/* 6855 */           return isSetSuccess();
/*      */       } 
/* 6857 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 6862 */       if (that == null)
/* 6863 */         return false; 
/* 6864 */       if (that instanceof aliveSince_result)
/* 6865 */         return equals((aliveSince_result)that); 
/* 6866 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(aliveSince_result that) {
/* 6870 */       if (that == null) {
/* 6871 */         return false;
/*      */       }
/* 6873 */       boolean this_present_success = true;
/* 6874 */       boolean that_present_success = true;
/* 6875 */       if (this_present_success || that_present_success) {
/* 6876 */         if (!this_present_success || !that_present_success)
/* 6877 */           return false; 
/* 6878 */         if (this.success != that.success) {
/* 6879 */           return false;
/*      */         }
/*      */       } 
/* 6882 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 6887 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(aliveSince_result other) {
/* 6891 */       if (!getClass().equals(other.getClass())) {
/* 6892 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 6895 */       int lastComparison = 0;
/* 6896 */       aliveSince_result typedOther = other;
/*      */       
/* 6898 */       lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(Boolean.valueOf(typedOther.isSetSuccess()));
/* 6899 */       if (lastComparison != 0) {
/* 6900 */         return lastComparison;
/*      */       }
/* 6902 */       if (isSetSuccess()) {
/* 6903 */         lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
/* 6904 */         if (lastComparison != 0) {
/* 6905 */           return lastComparison;
/*      */         }
/*      */       } 
/* 6908 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 6912 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 6917 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 6920 */         TField field = iprot.readFieldBegin();
/* 6921 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 6924 */         switch (field.id) {
/*      */           case 0:
/* 6926 */             if (field.type == 10) {
/* 6927 */               this.success = iprot.readI64();
/* 6928 */               setSuccessIsSet(true); break;
/*      */             } 
/* 6930 */             TProtocolUtil.skip(iprot, field.type);
/*      */             break;
/*      */           
/*      */           default:
/* 6934 */             TProtocolUtil.skip(iprot, field.type); break;
/*      */         } 
/* 6936 */         iprot.readFieldEnd();
/*      */       } 
/* 6938 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 6941 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 6945 */       oprot.writeStructBegin(STRUCT_DESC);
/*      */       
/* 6947 */       if (isSetSuccess()) {
/* 6948 */         oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
/* 6949 */         oprot.writeI64(this.success);
/* 6950 */         oprot.writeFieldEnd();
/*      */       } 
/* 6952 */       oprot.writeFieldStop();
/* 6953 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 6958 */       StringBuilder sb = new StringBuilder("aliveSince_result(");
/* 6959 */       boolean first = true;
/*      */       
/* 6961 */       sb.append("success:");
/* 6962 */       sb.append(this.success);
/* 6963 */       first = false;
/* 6964 */       sb.append(")");
/* 6965 */       return sb.toString();
/*      */     }
/*      */     
/*      */     public void validate() throws TException {}
/*      */     
/*      */     public aliveSince_result() {}
/*      */   }
/*      */   
/*      */   public static class reinitialize_args
/*      */     implements TBase<reinitialize_args, reinitialize_args._Fields>, Serializable, Cloneable {
/* 6975 */     private static final TStruct STRUCT_DESC = new TStruct("reinitialize_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 6983 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 6986 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 6987 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 6995 */         switch (fieldId) {
/*      */         
/* 6997 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 7006 */         _Fields fields = findByThriftId(fieldId);
/* 7007 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 7008 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 7015 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 7022 */         this._thriftId = thriftId;
/* 7023 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 7027 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 7031 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 7036 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 7037 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 7038 */       FieldMetaData.addStructMetaDataMap(reinitialize_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public reinitialize_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public reinitialize_args(reinitialize_args other) {}
/*      */ 
/*      */     
/*      */     public reinitialize_args deepCopy() {
/* 7051 */       return new reinitialize_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 7059 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$reinitialize_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 7064 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$reinitialize_args$_Fields[field.ordinal()];
/*      */       
/* 7066 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 7071 */       if (field == null) {
/* 7072 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 7075 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$reinitialize_args$_Fields[field.ordinal()];
/*      */       
/* 7077 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 7082 */       if (that == null)
/* 7083 */         return false; 
/* 7084 */       if (that instanceof reinitialize_args)
/* 7085 */         return equals((reinitialize_args)that); 
/* 7086 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(reinitialize_args that) {
/* 7090 */       if (that == null) {
/* 7091 */         return false;
/*      */       }
/* 7093 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 7098 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(reinitialize_args other) {
/* 7102 */       if (!getClass().equals(other.getClass())) {
/* 7103 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 7106 */       int lastComparison = 0;
/* 7107 */       reinitialize_args typedOther = other;
/*      */       
/* 7109 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 7113 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 7118 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 7121 */         TField field = iprot.readFieldBegin();
/* 7122 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 7125 */         switch (field.id) {
/*      */         
/* 7127 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 7129 */         iprot.readFieldEnd();
/*      */       } 
/* 7131 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 7134 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 7138 */       validate();
/*      */       
/* 7140 */       oprot.writeStructBegin(STRUCT_DESC);
/* 7141 */       oprot.writeFieldStop();
/* 7142 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 7147 */       StringBuilder sb = new StringBuilder("reinitialize_args(");
/* 7148 */       boolean first = true;
/*      */       
/* 7150 */       sb.append(")");
/* 7151 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */   
/*      */   public static class shutdown_args
/*      */     implements TBase<shutdown_args, shutdown_args._Fields>, Serializable, Cloneable
/*      */   {
/* 7161 */     private static final TStruct STRUCT_DESC = new TStruct("shutdown_args");
/*      */     
/*      */     public static final Map<_Fields, FieldMetaData> metaDataMap;
/*      */ 
/*      */     
/*      */     public enum _Fields
/*      */       implements TFieldIdEnum
/*      */     {
/* 7169 */       private static final Map<String, _Fields> byName = new HashMap<String, _Fields>(); private final short _thriftId; private final String _fieldName;
/*      */       
/*      */       static {
/* 7172 */         for (_Fields field : EnumSet.<_Fields>allOf(_Fields.class)) {
/* 7173 */           byName.put(field.getFieldName(), field);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftId(int fieldId) {
/* 7181 */         switch (fieldId) {
/*      */         
/* 7183 */         }  return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByThriftIdOrThrow(int fieldId) {
/* 7192 */         _Fields fields = findByThriftId(fieldId);
/* 7193 */         if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!"); 
/* 7194 */         return fields;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static _Fields findByName(String name) {
/* 7201 */         return byName.get(name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       _Fields(short thriftId, String fieldName) {
/* 7208 */         this._thriftId = thriftId;
/* 7209 */         this._fieldName = fieldName;
/*      */       }
/*      */       
/*      */       public short getThriftFieldId() {
/* 7213 */         return this._thriftId;
/*      */       }
/*      */       
/*      */       public String getFieldName() {
/* 7217 */         return this._fieldName;
/*      */       }
/*      */     }
/*      */     
/*      */     static {
/* 7222 */       Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
/* 7223 */       metaDataMap = Collections.unmodifiableMap(tmpMap);
/* 7224 */       FieldMetaData.addStructMetaDataMap(shutdown_args.class, metaDataMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public shutdown_args() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public shutdown_args(shutdown_args other) {}
/*      */ 
/*      */     
/*      */     public shutdown_args deepCopy() {
/* 7237 */       return new shutdown_args(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {}
/*      */ 
/*      */     
/*      */     public void setFieldValue(_Fields field, Object value) {
/* 7245 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$shutdown_args$_Fields[field.ordinal()];
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getFieldValue(_Fields field) {
/* 7250 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$shutdown_args$_Fields[field.ordinal()];
/*      */       
/* 7252 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isSet(_Fields field) {
/* 7257 */       if (field == null) {
/* 7258 */         throw new IllegalArgumentException();
/*      */       }
/*      */       
/* 7261 */       FacebookService.null.$SwitchMap$com$facebook$fb303$FacebookService$shutdown_args$_Fields[field.ordinal()];
/*      */       
/* 7263 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object that) {
/* 7268 */       if (that == null)
/* 7269 */         return false; 
/* 7270 */       if (that instanceof shutdown_args)
/* 7271 */         return equals((shutdown_args)that); 
/* 7272 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(shutdown_args that) {
/* 7276 */       if (that == null) {
/* 7277 */         return false;
/*      */       }
/* 7279 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 7284 */       return 0;
/*      */     }
/*      */     
/*      */     public int compareTo(shutdown_args other) {
/* 7288 */       if (!getClass().equals(other.getClass())) {
/* 7289 */         return getClass().getName().compareTo(other.getClass().getName());
/*      */       }
/*      */       
/* 7292 */       int lastComparison = 0;
/* 7293 */       shutdown_args typedOther = other;
/*      */       
/* 7295 */       return 0;
/*      */     }
/*      */     
/*      */     public _Fields fieldForId(int fieldId) {
/* 7299 */       return _Fields.findByThriftId(fieldId);
/*      */     }
/*      */ 
/*      */     
/*      */     public void read(TProtocol iprot) throws TException {
/* 7304 */       iprot.readStructBegin();
/*      */       
/*      */       while (true) {
/* 7307 */         TField field = iprot.readFieldBegin();
/* 7308 */         if (field.type == 0) {
/*      */           break;
/*      */         }
/* 7311 */         switch (field.id) {
/*      */         
/* 7313 */         }  TProtocolUtil.skip(iprot, field.type);
/*      */         
/* 7315 */         iprot.readFieldEnd();
/*      */       } 
/* 7317 */       iprot.readStructEnd();
/*      */ 
/*      */       
/* 7320 */       validate();
/*      */     }
/*      */     
/*      */     public void write(TProtocol oprot) throws TException {
/* 7324 */       validate();
/*      */       
/* 7326 */       oprot.writeStructBegin(STRUCT_DESC);
/* 7327 */       oprot.writeFieldStop();
/* 7328 */       oprot.writeStructEnd();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 7333 */       StringBuilder sb = new StringBuilder("shutdown_args(");
/* 7334 */       boolean first = true;
/*      */       
/* 7336 */       sb.append(")");
/* 7337 */       return sb.toString();
/*      */     }
/*      */     
/*      */     public void validate() throws TException {}
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/facebook/fb303/FacebookService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */