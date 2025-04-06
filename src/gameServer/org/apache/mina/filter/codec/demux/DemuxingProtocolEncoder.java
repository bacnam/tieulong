/*     */ package org.apache.mina.filter.codec.demux;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.UnknownMessageTypeException;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoder;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoderOutput;
/*     */ import org.apache.mina.util.CopyOnWriteMap;
/*     */ import org.apache.mina.util.IdentityHashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DemuxingProtocolEncoder
/*     */   implements ProtocolEncoder
/*     */ {
/*  50 */   private final AttributeKey STATE = new AttributeKey(getClass(), "state");
/*     */   
/*  52 */   private final Map<Class<?>, MessageEncoderFactory> type2encoderFactory = (Map<Class<?>, MessageEncoderFactory>)new CopyOnWriteMap();
/*     */ 
/*     */   
/*  55 */   private static final Class<?>[] EMPTY_PARAMS = new Class[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessageEncoder(Class<?> messageType, Class<? extends MessageEncoder> encoderClass) {
/*  63 */     if (encoderClass == null) {
/*  64 */       throw new IllegalArgumentException("encoderClass");
/*     */     }
/*     */     
/*     */     try {
/*  68 */       encoderClass.getConstructor(EMPTY_PARAMS);
/*  69 */     } catch (NoSuchMethodException e) {
/*  70 */       throw new IllegalArgumentException("The specified class doesn't have a public default constructor.");
/*     */     } 
/*     */     
/*  73 */     boolean registered = false;
/*  74 */     if (MessageEncoder.class.isAssignableFrom(encoderClass)) {
/*  75 */       addMessageEncoder(messageType, new DefaultConstructorMessageEncoderFactory(encoderClass));
/*  76 */       registered = true;
/*     */     } 
/*     */     
/*  79 */     if (!registered) {
/*  80 */       throw new IllegalArgumentException("Unregisterable type: " + encoderClass);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void addMessageEncoder(Class<T> messageType, MessageEncoder<? super T> encoder) {
/*  86 */     addMessageEncoder(messageType, new SingletonMessageEncoderFactory<T>(encoder));
/*     */   }
/*     */   
/*     */   public <T> void addMessageEncoder(Class<T> messageType, MessageEncoderFactory<? super T> factory) {
/*  90 */     if (messageType == null) {
/*  91 */       throw new IllegalArgumentException("messageType");
/*     */     }
/*     */     
/*  94 */     if (factory == null) {
/*  95 */       throw new IllegalArgumentException("factory");
/*     */     }
/*     */     
/*  98 */     synchronized (this.type2encoderFactory) {
/*  99 */       if (this.type2encoderFactory.containsKey(messageType)) {
/* 100 */         throw new IllegalStateException("The specified message type (" + messageType.getName() + ") is registered already.");
/*     */       }
/*     */ 
/*     */       
/* 104 */       this.type2encoderFactory.put(messageType, factory);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMessageEncoder(Iterable<Class<?>> messageTypes, Class<? extends MessageEncoder> encoderClass) {
/* 110 */     for (Class<?> messageType : messageTypes) {
/* 111 */       addMessageEncoder(messageType, encoderClass);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> void addMessageEncoder(Iterable<Class<? extends T>> messageTypes, MessageEncoder<? super T> encoder) {
/* 116 */     for (Class<? extends T> messageType : messageTypes) {
/* 117 */       addMessageEncoder(messageType, encoder);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void addMessageEncoder(Iterable<Class<? extends T>> messageTypes, MessageEncoderFactory<? super T> factory) {
/* 123 */     for (Class<? extends T> messageType : messageTypes) {
/* 124 */       addMessageEncoder(messageType, factory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
/* 132 */     State state = getState(session);
/* 133 */     MessageEncoder<Object> encoder = findEncoder(state, message.getClass());
/* 134 */     if (encoder != null) {
/* 135 */       encoder.encode(session, message, out);
/*     */     } else {
/* 137 */       throw new UnknownMessageTypeException("No message encoder found for message: " + message);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected MessageEncoder<Object> findEncoder(State state, Class<?> type) {
/* 142 */     return findEncoder(state, type, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MessageEncoder<Object> findEncoder(State state, Class<?> type, Set<Class<?>> triedClasses) {
/* 148 */     MessageEncoder<Object> encoder = null;
/*     */     
/* 150 */     if (triedClasses != null && triedClasses.contains(type)) {
/* 151 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     encoder = (MessageEncoder)state.findEncoderCache.get(type);
/*     */     
/* 159 */     if (encoder != null) {
/* 160 */       return encoder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     encoder = (MessageEncoder<Object>)state.type2encoder.get(type);
/*     */     
/* 168 */     if (encoder == null) {
/*     */       IdentityHashSet<Class<?>> identityHashSet;
/*     */ 
/*     */ 
/*     */       
/* 173 */       if (triedClasses == null) {
/* 174 */         identityHashSet = new IdentityHashSet();
/*     */       }
/*     */       
/* 177 */       identityHashSet.add(type);
/*     */       
/* 179 */       Class<?>[] interfaces = type.getInterfaces();
/*     */       
/* 181 */       for (Class<?> element : interfaces) {
/* 182 */         encoder = findEncoder(state, element, (Set<Class<?>>)identityHashSet);
/*     */         
/* 184 */         if (encoder != null) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 190 */     if (encoder == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       Class<?> superclass = type.getSuperclass();
/*     */       
/* 198 */       if (superclass != null) {
/* 199 */         encoder = findEncoder(state, superclass);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     if (encoder != null) {
/* 209 */       state.findEncoderCache.put(type, encoder);
/* 210 */       MessageEncoder<Object> tmpEncoder = state.findEncoderCache.putIfAbsent(type, encoder);
/*     */       
/* 212 */       if (tmpEncoder != null) {
/* 213 */         encoder = tmpEncoder;
/*     */       }
/*     */     } 
/*     */     
/* 217 */     return encoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose(IoSession session) throws Exception {
/* 224 */     session.removeAttribute(this.STATE);
/*     */   }
/*     */   
/*     */   private State getState(IoSession session) throws Exception {
/* 228 */     State state = (State)session.getAttribute(this.STATE);
/* 229 */     if (state == null) {
/* 230 */       state = new State();
/* 231 */       State oldState = (State)session.setAttributeIfAbsent(this.STATE, state);
/* 232 */       if (oldState != null) {
/* 233 */         state = oldState;
/*     */       }
/*     */     } 
/* 236 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class State
/*     */   {
/*     */     private State() throws Exception {
/* 248 */       for (Map.Entry<Class<?>, MessageEncoderFactory> e : (Iterable<Map.Entry<Class<?>, MessageEncoderFactory>>)DemuxingProtocolEncoder.this.type2encoderFactory.entrySet())
/* 249 */         this.type2encoder.put(e.getKey(), ((MessageEncoderFactory)e.getValue()).getEncoder()); 
/*     */     }
/*     */     
/*     */     private final ConcurrentHashMap<Class<?>, MessageEncoder> findEncoderCache = new ConcurrentHashMap<Class<?>, MessageEncoder>();
/*     */     private final Map<Class<?>, MessageEncoder> type2encoder = new ConcurrentHashMap<Class<?>, MessageEncoder>();
/*     */   }
/*     */   
/*     */   private static class SingletonMessageEncoderFactory<T> implements MessageEncoderFactory<T> {
/*     */     private SingletonMessageEncoderFactory(MessageEncoder<T> encoder) {
/* 258 */       if (encoder == null) {
/* 259 */         throw new IllegalArgumentException("encoder");
/*     */       }
/* 261 */       this.encoder = encoder;
/*     */     }
/*     */     
/*     */     public MessageEncoder<T> getEncoder() {
/* 265 */       return this.encoder;
/*     */     }
/*     */     
/*     */     private final MessageEncoder<T> encoder; }
/*     */   
/*     */   private static class DefaultConstructorMessageEncoderFactory<T> implements MessageEncoderFactory<T> { private final Class<MessageEncoder<T>> encoderClass;
/*     */     
/*     */     private DefaultConstructorMessageEncoderFactory(Class<MessageEncoder<T>> encoderClass) {
/* 273 */       if (encoderClass == null) {
/* 274 */         throw new IllegalArgumentException("encoderClass");
/*     */       }
/*     */       
/* 277 */       if (!MessageEncoder.class.isAssignableFrom(encoderClass)) {
/* 278 */         throw new IllegalArgumentException("encoderClass is not assignable to MessageEncoder");
/*     */       }
/* 280 */       this.encoderClass = encoderClass;
/*     */     }
/*     */     
/*     */     public MessageEncoder<T> getEncoder() throws Exception {
/* 284 */       return this.encoderClass.newInstance();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/DemuxingProtocolEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */