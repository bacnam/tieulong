package org.apache.mina.handler.demux;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.UnknownMessageTypeException;
import org.apache.mina.util.IdentityHashSet;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DemuxingIoHandler
        extends IoHandlerAdapter {
    private final Map<Class<?>, MessageHandler<?>> receivedMessageHandlerCache = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();

    private final Map<Class<?>, MessageHandler<?>> receivedMessageHandlers = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();

    private final Map<Class<?>, MessageHandler<?>> sentMessageHandlerCache = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();

    private final Map<Class<?>, MessageHandler<?>> sentMessageHandlers = new ConcurrentHashMap<Class<?>, MessageHandler<?>>();

    private final Map<Class<?>, ExceptionHandler<?>> exceptionHandlerCache = new ConcurrentHashMap<Class<?>, ExceptionHandler<?>>();

    private final Map<Class<?>, ExceptionHandler<?>> exceptionHandlers = new ConcurrentHashMap<Class<?>, ExceptionHandler<?>>();

    public <E> MessageHandler<? super E> addReceivedMessageHandler(Class<E> type, MessageHandler<? super E> handler) {
        this.receivedMessageHandlerCache.clear();
        return (MessageHandler<? super E>) this.receivedMessageHandlers.put(type, handler);
    }

    public <E> MessageHandler<? super E> removeReceivedMessageHandler(Class<E> type) {
        this.receivedMessageHandlerCache.clear();
        return (MessageHandler<? super E>) this.receivedMessageHandlers.remove(type);
    }

    public <E> MessageHandler<? super E> addSentMessageHandler(Class<E> type, MessageHandler<? super E> handler) {
        this.sentMessageHandlerCache.clear();
        return (MessageHandler<? super E>) this.sentMessageHandlers.put(type, handler);
    }

    public <E> MessageHandler<? super E> removeSentMessageHandler(Class<E> type) {
        this.sentMessageHandlerCache.clear();
        return (MessageHandler<? super E>) this.sentMessageHandlers.remove(type);
    }

    public <E extends Throwable> ExceptionHandler<? super E> addExceptionHandler(Class<E> type, ExceptionHandler<? super E> handler) {
        this.exceptionHandlerCache.clear();
        return (ExceptionHandler<? super E>) this.exceptionHandlers.put(type, handler);
    }

    public <E extends Throwable> ExceptionHandler<? super E> removeExceptionHandler(Class<E> type) {
        this.exceptionHandlerCache.clear();
        return (ExceptionHandler<? super E>) this.exceptionHandlers.remove(type);
    }

    public <E> MessageHandler<? super E> getMessageHandler(Class<E> type) {
        return (MessageHandler<? super E>) this.receivedMessageHandlers.get(type);
    }

    public Map<Class<?>, MessageHandler<?>> getReceivedMessageHandlerMap() {
        return Collections.unmodifiableMap(this.receivedMessageHandlers);
    }

    public Map<Class<?>, MessageHandler<?>> getSentMessageHandlerMap() {
        return Collections.unmodifiableMap(this.sentMessageHandlers);
    }

    public Map<Class<?>, ExceptionHandler<?>> getExceptionHandlerMap() {
        return Collections.unmodifiableMap(this.exceptionHandlers);
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        MessageHandler<Object> handler = findReceivedMessageHandler(message.getClass());
        if (handler != null) {
            handler.handleMessage(session, message);
        } else {
            throw new UnknownMessageTypeException("No message handler found for message type: " + message.getClass().getSimpleName());
        }
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        MessageHandler<Object> handler = findSentMessageHandler(message.getClass());
        if (handler != null) {
            handler.handleMessage(session, message);
        } else {
            throw new UnknownMessageTypeException("No handler found for message type: " + message.getClass().getSimpleName());
        }
    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        ExceptionHandler<Throwable> handler = findExceptionHandler((Class) cause.getClass());
        if (handler != null) {
            handler.exceptionCaught(session, cause);
        } else {
            throw new UnknownMessageTypeException("No handler found for exception type: " + cause.getClass().getSimpleName());
        }
    }

    protected MessageHandler<Object> findReceivedMessageHandler(Class<?> type) {
        return findReceivedMessageHandler(type, null);
    }

    protected MessageHandler<Object> findSentMessageHandler(Class<?> type) {
        return findSentMessageHandler(type, null);
    }

    protected ExceptionHandler<Throwable> findExceptionHandler(Class<? extends Throwable> type) {
        return findExceptionHandler(type, null);
    }

    private MessageHandler<Object> findReceivedMessageHandler(Class type, Set<Class<?>> triedClasses) {
        return (MessageHandler<Object>) findHandler(this.receivedMessageHandlers, this.receivedMessageHandlerCache, type, triedClasses);
    }

    private MessageHandler<Object> findSentMessageHandler(Class type, Set<Class<?>> triedClasses) {
        return (MessageHandler<Object>) findHandler(this.sentMessageHandlers, this.sentMessageHandlerCache, type, triedClasses);
    }

    private ExceptionHandler<Throwable> findExceptionHandler(Class type, Set<Class<?>> triedClasses) {
        return (ExceptionHandler<Throwable>) findHandler(this.exceptionHandlers, this.exceptionHandlerCache, type, triedClasses);
    }

    private Object findHandler(Map handlers, Map<Class<?>, Object> handlerCache, Class<?> type, Set<Class<?>> triedClasses) {
        Object handler = null;

        if (triedClasses != null && triedClasses.contains(type)) {
            return null;
        }

        handler = handlerCache.get(type);
        if (handler != null) {
            return handler;
        }

        handler = handlers.get(type);

        if (handler == null) {
            IdentityHashSet<Class<?>> identityHashSet;

            if (triedClasses == null) {
                identityHashSet = new IdentityHashSet();
            }
            identityHashSet.add(type);

            Class[] interfaces = type.getInterfaces();
            for (Class element : interfaces) {
                handler = findHandler(handlers, handlerCache, element, (Set) identityHashSet);
                if (handler != null) {
                    break;
                }
            }
        }

        if (handler == null) {

            Class<?> superclass = type.getSuperclass();
            if (superclass != null) {
                handler = findHandler(handlers, handlerCache, superclass, null);
            }
        }

        if (handler != null) {
            handlerCache.put(type, handler);
        }

        return handler;
    }
}

