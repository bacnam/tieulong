/*     */ package org.apache.mina.filter.codec;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.future.DefaultWriteFuture;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.DefaultWriteRequest;
/*     */ import org.apache.mina.core.write.NothingWrittenException;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequestWrapper;
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
/*     */ public class ProtocolCodecFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  52 */   private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolCodecFilter.class);
/*     */   
/*  54 */   private static final Class<?>[] EMPTY_PARAMS = new Class[0];
/*     */   
/*  56 */   private static final IoBuffer EMPTY_BUFFER = IoBuffer.wrap(new byte[0]);
/*     */   
/*  58 */   private static final AttributeKey ENCODER = new AttributeKey(ProtocolCodecFilter.class, "encoder");
/*     */   
/*  60 */   private static final AttributeKey DECODER = new AttributeKey(ProtocolCodecFilter.class, "decoder");
/*     */   
/*  62 */   private static final AttributeKey DECODER_OUT = new AttributeKey(ProtocolCodecFilter.class, "decoderOut");
/*     */   
/*  64 */   private static final AttributeKey ENCODER_OUT = new AttributeKey(ProtocolCodecFilter.class, "encoderOut");
/*     */   private final ProtocolCodecFactory factory;
/*     */   private final Semaphore lock;
/*     */   
/*     */   public ProtocolCodecFilter(ProtocolCodecFactory factory) {
/*  69 */     this.lock = new Semaphore(1, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (factory == null) {
/*  79 */       throw new IllegalArgumentException("factory");
/*     */     }
/*     */     
/*  82 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolCodecFilter(final ProtocolEncoder encoder, final ProtocolDecoder decoder) {
/*     */     this.lock = new Semaphore(1, true);
/*  94 */     if (encoder == null) {
/*  95 */       throw new IllegalArgumentException("encoder");
/*     */     }
/*  97 */     if (decoder == null) {
/*  98 */       throw new IllegalArgumentException("decoder");
/*     */     }
/*     */ 
/*     */     
/* 102 */     this.factory = new ProtocolCodecFactory() {
/*     */         public ProtocolEncoder getEncoder(IoSession session) {
/* 104 */           return encoder;
/*     */         }
/*     */         
/*     */         public ProtocolDecoder getDecoder(IoSession session) {
/* 108 */           return decoder;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolCodecFilter(Class<? extends ProtocolEncoder> encoderClass, Class<? extends ProtocolDecoder> decoderClass) {
/*     */     final ProtocolEncoder encoder;
/*     */     final ProtocolDecoder decoder;
/*     */     this.lock = new Semaphore(1, true);
/* 124 */     if (encoderClass == null) {
/* 125 */       throw new IllegalArgumentException("encoderClass");
/*     */     }
/* 127 */     if (decoderClass == null) {
/* 128 */       throw new IllegalArgumentException("decoderClass");
/*     */     }
/* 130 */     if (!ProtocolEncoder.class.isAssignableFrom(encoderClass)) {
/* 131 */       throw new IllegalArgumentException("encoderClass: " + encoderClass.getName());
/*     */     }
/* 133 */     if (!ProtocolDecoder.class.isAssignableFrom(decoderClass)) {
/* 134 */       throw new IllegalArgumentException("decoderClass: " + decoderClass.getName());
/*     */     }
/*     */     try {
/* 137 */       encoderClass.getConstructor(EMPTY_PARAMS);
/* 138 */     } catch (NoSuchMethodException e) {
/* 139 */       throw new IllegalArgumentException("encoderClass doesn't have a public default constructor.");
/*     */     } 
/*     */     try {
/* 142 */       decoderClass.getConstructor(EMPTY_PARAMS);
/* 143 */     } catch (NoSuchMethodException e) {
/* 144 */       throw new IllegalArgumentException("decoderClass doesn't have a public default constructor.");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 150 */       encoder = encoderClass.newInstance();
/* 151 */     } catch (Exception e) {
/* 152 */       throw new IllegalArgumentException("encoderClass cannot be initialized");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 158 */       decoder = decoderClass.newInstance();
/* 159 */     } catch (Exception e) {
/* 160 */       throw new IllegalArgumentException("decoderClass cannot be initialized");
/*     */     } 
/*     */ 
/*     */     
/* 164 */     this.factory = new ProtocolCodecFactory() {
/*     */         public ProtocolEncoder getEncoder(IoSession session) throws Exception {
/* 166 */           return encoder;
/*     */         }
/*     */         
/*     */         public ProtocolDecoder getDecoder(IoSession session) throws Exception {
/* 170 */           return decoder;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolEncoder getEncoder(IoSession session) {
/* 182 */     return (ProtocolEncoder)session.getAttribute(ENCODER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/* 187 */     if (parent.contains((IoFilter)this)) {
/* 188 */       throw new IllegalArgumentException("You can't add the same filter instance more than once.  Create another instance and add it.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPostRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/* 196 */     disposeCodec(parent.getSession());
/*     */   }
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
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/* 213 */     LOGGER.debug("Processing a MESSAGE_RECEIVED for session {}", Long.valueOf(session.getId()));
/*     */     
/* 215 */     if (!(message instanceof IoBuffer)) {
/* 216 */       nextFilter.messageReceived(session, message);
/*     */       
/*     */       return;
/*     */     } 
/* 220 */     IoBuffer in = (IoBuffer)message;
/* 221 */     ProtocolDecoder decoder = this.factory.getDecoder(session);
/* 222 */     ProtocolDecoderOutput decoderOut = getDecoderOut(session, nextFilter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     while (in.hasRemaining()) {
/* 229 */       int oldPos = in.position();
/*     */       
/* 231 */       try { this.lock.acquire();
/*     */         
/* 233 */         decoder.decode(session, in, decoderOut);
/*     */         
/* 235 */         decoderOut.flush(nextFilter, session); }
/* 236 */       catch (Exception e)
/*     */       { ProtocolDecoderException pde;
/* 238 */         if (e instanceof ProtocolDecoderException) {
/* 239 */           pde = (ProtocolDecoderException)e;
/*     */         } else {
/* 241 */           pde = new ProtocolDecoderException(e);
/*     */         } 
/* 243 */         if (pde.getHexdump() == null) {
/*     */           
/* 245 */           int curPos = in.position();
/* 246 */           in.position(oldPos);
/* 247 */           pde.setHexdump(in.getHexDump());
/* 248 */           in.position(curPos);
/*     */         } 
/*     */         
/* 251 */         decoderOut.flush(nextFilter, session);
/* 252 */         nextFilter.exceptionCaught(session, pde);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 257 */         if (!(e instanceof RecoverableProtocolDecoderException) || in.position() == oldPos)
/*     */         
/*     */         { 
/*     */           
/* 261 */           this.lock.release(); break; }  } finally { this.lock.release(); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 268 */     if (writeRequest instanceof EncodedWriteRequest) {
/*     */       return;
/*     */     }
/*     */     
/* 272 */     if (writeRequest instanceof MessageWriteRequest) {
/* 273 */       MessageWriteRequest wrappedRequest = (MessageWriteRequest)writeRequest;
/* 274 */       nextFilter.messageSent(session, wrappedRequest.getParentRequest());
/*     */     } else {
/* 276 */       nextFilter.messageSent(session, writeRequest);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 282 */     Object message = writeRequest.getMessage();
/*     */ 
/*     */ 
/*     */     
/* 286 */     if (message instanceof IoBuffer || message instanceof org.apache.mina.core.file.FileRegion) {
/* 287 */       nextFilter.filterWrite(session, writeRequest);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 292 */     ProtocolEncoder encoder = this.factory.getEncoder(session);
/*     */     
/* 294 */     ProtocolEncoderOutput encoderOut = getEncoderOut(session, nextFilter, writeRequest);
/*     */     
/* 296 */     if (encoder == null) {
/* 297 */       throw new ProtocolEncoderException("The encoder is null for the session " + session);
/*     */     }
/*     */     
/* 300 */     if (encoderOut == null) {
/* 301 */       throw new ProtocolEncoderException("The encoderOut is null for the session " + session);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 306 */       encoder.encode(session, message, encoderOut);
/*     */ 
/*     */       
/* 309 */       Queue<Object> bufferQueue = ((AbstractProtocolEncoderOutput)encoderOut).getMessageQueue();
/*     */ 
/*     */       
/* 312 */       while (!bufferQueue.isEmpty()) {
/* 313 */         Object encodedMessage = bufferQueue.poll();
/*     */         
/* 315 */         if (encodedMessage == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 320 */         if (!(encodedMessage instanceof IoBuffer) || ((IoBuffer)encodedMessage).hasRemaining()) {
/* 321 */           SocketAddress destination = writeRequest.getDestination();
/* 322 */           EncodedWriteRequest encodedWriteRequest = new EncodedWriteRequest(encodedMessage, null, destination);
/*     */           
/* 324 */           nextFilter.filterWrite(session, (WriteRequest)encodedWriteRequest);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 329 */       nextFilter.filterWrite(session, (WriteRequest)new MessageWriteRequest(writeRequest));
/* 330 */     } catch (Exception e) {
/*     */       ProtocolEncoderException pee;
/*     */ 
/*     */       
/* 334 */       if (e instanceof ProtocolEncoderException) {
/* 335 */         pee = (ProtocolEncoderException)e;
/*     */       } else {
/* 337 */         pee = new ProtocolEncoderException(e);
/*     */       } 
/*     */       
/* 340 */       throw pee;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 347 */     ProtocolDecoder decoder = this.factory.getDecoder(session);
/* 348 */     ProtocolDecoderOutput decoderOut = getDecoderOut(session, nextFilter);
/*     */     
/*     */     try {
/* 351 */       decoder.finishDecode(session, decoderOut);
/* 352 */     } catch (Exception e) {
/*     */       ProtocolDecoderException pde;
/* 354 */       if (e instanceof ProtocolDecoderException) {
/* 355 */         pde = (ProtocolDecoderException)e;
/*     */       } else {
/* 357 */         pde = new ProtocolDecoderException(e);
/*     */       } 
/* 359 */       throw pde;
/*     */     } finally {
/*     */       
/* 362 */       disposeCodec(session);
/* 363 */       decoderOut.flush(nextFilter, session);
/*     */     } 
/*     */ 
/*     */     
/* 367 */     nextFilter.sessionClosed(session);
/*     */   }
/*     */   
/*     */   private static class EncodedWriteRequest extends DefaultWriteRequest {
/*     */     public EncodedWriteRequest(Object encodedMessage, WriteFuture future, SocketAddress destination) {
/* 372 */       super(encodedMessage, future, destination);
/*     */     }
/*     */     
/*     */     public boolean isEncoded() {
/* 376 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MessageWriteRequest extends WriteRequestWrapper {
/*     */     public MessageWriteRequest(WriteRequest writeRequest) {
/* 382 */       super(writeRequest);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getMessage() {
/* 387 */       return ProtocolCodecFilter.EMPTY_BUFFER;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 392 */       return "MessageWriteRequest, parent : " + super.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ProtocolDecoderOutputImpl
/*     */     extends AbstractProtocolDecoderOutput
/*     */   {
/*     */     public void flush(IoFilter.NextFilter nextFilter, IoSession session) {
/* 402 */       Queue<Object> messageQueue = getMessageQueue();
/*     */       
/* 404 */       while (!messageQueue.isEmpty()) {
/* 405 */         nextFilter.messageReceived(session, messageQueue.poll());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ProtocolEncoderOutputImpl
/*     */     extends AbstractProtocolEncoderOutput
/*     */   {
/*     */     private final IoSession session;
/*     */     private final IoFilter.NextFilter nextFilter;
/*     */     private final SocketAddress destination;
/*     */     
/*     */     public ProtocolEncoderOutputImpl(IoSession session, IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
/* 419 */       this.session = session;
/* 420 */       this.nextFilter = nextFilter;
/*     */ 
/*     */       
/* 423 */       this.destination = writeRequest.getDestination();
/*     */     } public WriteFuture flush() {
/*     */       DefaultWriteFuture defaultWriteFuture;
/*     */       WriteFuture writeFuture1;
/* 427 */       Queue<Object> bufferQueue = getMessageQueue();
/* 428 */       WriteFuture future = null;
/*     */       
/* 430 */       while (!bufferQueue.isEmpty()) {
/* 431 */         Object encodedMessage = bufferQueue.poll();
/*     */         
/* 433 */         if (encodedMessage == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 438 */         if (!(encodedMessage instanceof IoBuffer) || ((IoBuffer)encodedMessage).hasRemaining()) {
/* 439 */           defaultWriteFuture = new DefaultWriteFuture(this.session);
/* 440 */           this.nextFilter.filterWrite(this.session, (WriteRequest)new ProtocolCodecFilter.EncodedWriteRequest(encodedMessage, (WriteFuture)defaultWriteFuture, this.destination));
/*     */         } 
/*     */       } 
/*     */       
/* 444 */       if (defaultWriteFuture == null) {
/*     */         
/* 446 */         DefaultWriteRequest defaultWriteRequest = new DefaultWriteRequest(DefaultWriteRequest.EMPTY_MESSAGE, null, this.destination);
/*     */         
/* 448 */         writeFuture1 = DefaultWriteFuture.newNotWrittenFuture(this.session, (Throwable)new NothingWrittenException((WriteRequest)defaultWriteRequest));
/*     */       } 
/*     */       
/* 451 */       return writeFuture1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void disposeCodec(IoSession session) {
/* 463 */     disposeEncoder(session);
/* 464 */     disposeDecoder(session);
/*     */ 
/*     */     
/* 467 */     disposeDecoderOut(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void disposeEncoder(IoSession session) {
/* 476 */     ProtocolEncoder encoder = (ProtocolEncoder)session.removeAttribute(ENCODER);
/* 477 */     if (encoder == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 482 */       encoder.dispose(session);
/* 483 */     } catch (Exception e) {
/* 484 */       LOGGER.warn("Failed to dispose: " + encoder.getClass().getName() + " (" + encoder + ')');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void disposeDecoder(IoSession session) {
/* 494 */     ProtocolDecoder decoder = (ProtocolDecoder)session.removeAttribute(DECODER);
/* 495 */     if (decoder == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 500 */       decoder.dispose(session);
/* 501 */     } catch (Exception e) {
/* 502 */       LOGGER.warn("Failed to dispose: " + decoder.getClass().getName() + " (" + decoder + ')');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProtocolDecoderOutput getDecoderOut(IoSession session, IoFilter.NextFilter nextFilter) {
/* 511 */     ProtocolDecoderOutput out = (ProtocolDecoderOutput)session.getAttribute(DECODER_OUT);
/*     */     
/* 513 */     if (out == null) {
/*     */       
/* 515 */       out = new ProtocolDecoderOutputImpl();
/* 516 */       session.setAttribute(DECODER_OUT, out);
/*     */     } 
/*     */     
/* 519 */     return out;
/*     */   }
/*     */   
/*     */   private ProtocolEncoderOutput getEncoderOut(IoSession session, IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
/* 523 */     ProtocolEncoderOutput out = (ProtocolEncoderOutput)session.getAttribute(ENCODER_OUT);
/*     */     
/* 525 */     if (out == null) {
/*     */       
/* 527 */       out = new ProtocolEncoderOutputImpl(session, nextFilter, writeRequest);
/* 528 */       session.setAttribute(ENCODER_OUT, out);
/*     */     } 
/*     */     
/* 531 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void disposeDecoderOut(IoSession session) {
/* 538 */     session.removeAttribute(DECODER_OUT);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolCodecFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */