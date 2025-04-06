/*     */ package org.apache.mina.handler.demux;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.mina.core.service.IoHandlerAdapter;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.UnknownMessageTypeException;
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
/*     */ public class DemuxingIoHandler
/*     */   extends IoHandlerAdapter
/*     */ {
/*  81 */   private final Map<Class<?>, MessageHandler<?>> receivedMessageHandlerCache = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();
/*     */   
/*  83 */   private final Map<Class<?>, MessageHandler<?>> receivedMessageHandlers = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();
/*     */   
/*  85 */   private final Map<Class<?>, MessageHandler<?>> sentMessageHandlerCache = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();
/*     */   
/*  87 */   private final Map<Class<?>, MessageHandler<?>> sentMessageHandlers = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();
/*     */   
/*  89 */   private final Map<Class<?>, ExceptionHandler<?>> exceptionHandlerCache = new ConcurrentHashMap<Class<?>, ExceptionHandler<?>>();
/*     */   
/*  91 */   private final Map<Class<?>, ExceptionHandler<?>> exceptionHandlers = new ConcurrentHashMap<Class<?>, ExceptionHandler<?>>();
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
/*     */   public <E> MessageHandler<? super E> addReceivedMessageHandler(Class<E> type, MessageHandler<? super E> handler) {
/* 109 */     this.receivedMessageHandlerCache.clear();
/* 110 */     return (MessageHandler<? super E>)this.receivedMessageHandlers.put(type, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> MessageHandler<? super E> removeReceivedMessageHandler(Class<E> type) {
/* 121 */     this.receivedMessageHandlerCache.clear();
/* 122 */     return (MessageHandler<? super E>)this.receivedMessageHandlers.remove(type);
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
/*     */   public <E> MessageHandler<? super E> addSentMessageHandler(Class<E> type, MessageHandler<? super E> handler) {
/* 134 */     this.sentMessageHandlerCache.clear();
/* 135 */     return (MessageHandler<? super E>)this.sentMessageHandlers.put(type, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> MessageHandler<? super E> removeSentMessageHandler(Class<E> type) {
/* 146 */     this.sentMessageHandlerCache.clear();
/* 147 */     return (MessageHandler<? super E>)this.sentMessageHandlers.remove(type);
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
/*     */   public <E extends Throwable> ExceptionHandler<? super E> addExceptionHandler(Class<E> type, ExceptionHandler<? super E> handler) {
/* 160 */     this.exceptionHandlerCache.clear();
/* 161 */     return (ExceptionHandler<? super E>)this.exceptionHandlers.put(type, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends Throwable> ExceptionHandler<? super E> removeExceptionHandler(Class<E> type) {
/* 172 */     this.exceptionHandlerCache.clear();
/* 173 */     return (ExceptionHandler<? super E>)this.exceptionHandlers.remove(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> MessageHandler<? super E> getMessageHandler(Class<E> type) {
/* 182 */     return (MessageHandler<? super E>)this.receivedMessageHandlers.get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Class<?>, MessageHandler<?>> getReceivedMessageHandlerMap() {
/* 190 */     return Collections.unmodifiableMap(this.receivedMessageHandlers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Class<?>, MessageHandler<?>> getSentMessageHandlerMap() {
/* 198 */     return Collections.unmodifiableMap(this.sentMessageHandlers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Class<?>, ExceptionHandler<?>> getExceptionHandlerMap() {
/* 206 */     return Collections.unmodifiableMap(this.exceptionHandlers);
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
/*     */   public void messageReceived(IoSession session, Object message) throws Exception {
/* 219 */     MessageHandler<Object> handler = findReceivedMessageHandler(message.getClass());
/* 220 */     if (handler != null) {
/* 221 */       handler.handleMessage(session, message);
/*     */     } else {
/* 223 */       throw new UnknownMessageTypeException("No message handler found for message type: " + message.getClass().getSimpleName());
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
/*     */ 
/*     */   
/*     */   public void messageSent(IoSession session, Object message) throws Exception {
/* 237 */     MessageHandler<Object> handler = findSentMessageHandler(message.getClass());
/* 238 */     if (handler != null) {
/* 239 */       handler.handleMessage(session, message);
/*     */     } else {
/* 241 */       throw new UnknownMessageTypeException("No handler found for message type: " + message.getClass().getSimpleName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
/* 257 */     ExceptionHandler<Throwable> handler = findExceptionHandler((Class)cause.getClass());
/* 258 */     if (handler != null) {
/* 259 */       handler.exceptionCaught(session, cause);
/*     */     } else {
/* 261 */       throw new UnknownMessageTypeException("No handler found for exception type: " + cause.getClass().getSimpleName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected MessageHandler<Object> findReceivedMessageHandler(Class<?> type) {
/* 267 */     return findReceivedMessageHandler(type, null);
/*     */   }
/*     */   
/*     */   protected MessageHandler<Object> findSentMessageHandler(Class<?> type) {
/* 271 */     return findSentMessageHandler(type, null);
/*     */   }
/*     */   
/*     */   protected ExceptionHandler<Throwable> findExceptionHandler(Class<? extends Throwable> type) {
/* 275 */     return findExceptionHandler(type, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MessageHandler<Object> findReceivedMessageHandler(Class type, Set<Class<?>> triedClasses) {
/* 281 */     return (MessageHandler<Object>)findHandler(this.receivedMessageHandlers, this.receivedMessageHandlerCache, type, triedClasses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MessageHandler<Object> findSentMessageHandler(Class type, Set<Class<?>> triedClasses) {
/* 288 */     return (MessageHandler<Object>)findHandler(this.sentMessageHandlers, this.sentMessageHandlerCache, type, triedClasses);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ExceptionHandler<Throwable> findExceptionHandler(Class type, Set<Class<?>> triedClasses) {
/* 294 */     return (ExceptionHandler<Throwable>)findHandler(this.exceptionHandlers, this.exceptionHandlerCache, type, triedClasses);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object findHandler(Map handlers, Map<Class<?>, Object> handlerCache, Class<?> type, Set<Class<?>> triedClasses) {
/* 300 */     Object handler = null;
/*     */     
/* 302 */     if (triedClasses != null && triedClasses.contains(type)) {
/* 303 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     handler = handlerCache.get(type);
/* 310 */     if (handler != null) {
/* 311 */       return handler;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     handler = handlers.get(type);
/*     */     
/* 319 */     if (handler == null) {
/*     */       IdentityHashSet<Class<?>> identityHashSet;
/*     */ 
/*     */ 
/*     */       
/* 324 */       if (triedClasses == null) {
/* 325 */         identityHashSet = new IdentityHashSet();
/*     */       }
/* 327 */       identityHashSet.add(type);
/*     */       
/* 329 */       Class[] interfaces = type.getInterfaces();
/* 330 */       for (Class element : interfaces) {
/* 331 */         handler = findHandler(handlers, handlerCache, element, (Set)identityHashSet);
/* 332 */         if (handler != null) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 338 */     if (handler == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 343 */       Class<?> superclass = type.getSuperclass();
/* 344 */       if (superclass != null) {
/* 345 */         handler = findHandler(handlers, handlerCache, superclass, null);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     if (handler != null) {
/* 355 */       handlerCache.put(type, handler);
/*     */     }
/*     */     
/* 358 */     return handler;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/demux/DemuxingIoHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */