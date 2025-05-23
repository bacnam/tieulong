package org.apache.mina.filter.codec.demux;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.UnknownMessageTypeException;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.util.CopyOnWriteMap;
import org.apache.mina.util.IdentityHashSet;

public class DemuxingProtocolEncoder
implements ProtocolEncoder
{
private final AttributeKey STATE = new AttributeKey(getClass(), "state");

private final Map<Class<?>, MessageEncoderFactory> type2encoderFactory = (Map<Class<?>, MessageEncoderFactory>)new CopyOnWriteMap();

private static final Class<?>[] EMPTY_PARAMS = new Class[0];

public void addMessageEncoder(Class<?> messageType, Class<? extends MessageEncoder> encoderClass) {
if (encoderClass == null) {
throw new IllegalArgumentException("encoderClass");
}

try {
encoderClass.getConstructor(EMPTY_PARAMS);
} catch (NoSuchMethodException e) {
throw new IllegalArgumentException("The specified class doesn't have a public default constructor.");
} 

boolean registered = false;
if (MessageEncoder.class.isAssignableFrom(encoderClass)) {
addMessageEncoder(messageType, new DefaultConstructorMessageEncoderFactory(encoderClass));
registered = true;
} 

if (!registered) {
throw new IllegalArgumentException("Unregisterable type: " + encoderClass);
}
}

public <T> void addMessageEncoder(Class<T> messageType, MessageEncoder<? super T> encoder) {
addMessageEncoder(messageType, new SingletonMessageEncoderFactory<T>(encoder));
}

public <T> void addMessageEncoder(Class<T> messageType, MessageEncoderFactory<? super T> factory) {
if (messageType == null) {
throw new IllegalArgumentException("messageType");
}

if (factory == null) {
throw new IllegalArgumentException("factory");
}

synchronized (this.type2encoderFactory) {
if (this.type2encoderFactory.containsKey(messageType)) {
throw new IllegalStateException("The specified message type (" + messageType.getName() + ") is registered already.");
}

this.type2encoderFactory.put(messageType, factory);
} 
}

public void addMessageEncoder(Iterable<Class<?>> messageTypes, Class<? extends MessageEncoder> encoderClass) {
for (Class<?> messageType : messageTypes) {
addMessageEncoder(messageType, encoderClass);
}
}

public <T> void addMessageEncoder(Iterable<Class<? extends T>> messageTypes, MessageEncoder<? super T> encoder) {
for (Class<? extends T> messageType : messageTypes) {
addMessageEncoder(messageType, encoder);
}
}

public <T> void addMessageEncoder(Iterable<Class<? extends T>> messageTypes, MessageEncoderFactory<? super T> factory) {
for (Class<? extends T> messageType : messageTypes) {
addMessageEncoder(messageType, factory);
}
}

public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
State state = getState(session);
MessageEncoder<Object> encoder = findEncoder(state, message.getClass());
if (encoder != null) {
encoder.encode(session, message, out);
} else {
throw new UnknownMessageTypeException("No message encoder found for message: " + message);
} 
}

protected MessageEncoder<Object> findEncoder(State state, Class<?> type) {
return findEncoder(state, type, null);
}

private MessageEncoder<Object> findEncoder(State state, Class<?> type, Set<Class<?>> triedClasses) {
MessageEncoder<Object> encoder = null;

if (triedClasses != null && triedClasses.contains(type)) {
return null;
}

encoder = (MessageEncoder)state.findEncoderCache.get(type);

if (encoder != null) {
return encoder;
}

encoder = (MessageEncoder<Object>)state.type2encoder.get(type);

if (encoder == null) {
IdentityHashSet<Class<?>> identityHashSet;

if (triedClasses == null) {
identityHashSet = new IdentityHashSet();
}

identityHashSet.add(type);

Class<?>[] interfaces = type.getInterfaces();

for (Class<?> element : interfaces) {
encoder = findEncoder(state, element, (Set<Class<?>>)identityHashSet);

if (encoder != null) {
break;
}
} 
} 

if (encoder == null) {

Class<?> superclass = type.getSuperclass();

if (superclass != null) {
encoder = findEncoder(state, superclass);
}
} 

if (encoder != null) {
state.findEncoderCache.put(type, encoder);
MessageEncoder<Object> tmpEncoder = state.findEncoderCache.putIfAbsent(type, encoder);

if (tmpEncoder != null) {
encoder = tmpEncoder;
}
} 

return encoder;
}

public void dispose(IoSession session) throws Exception {
session.removeAttribute(this.STATE);
}

private State getState(IoSession session) throws Exception {
State state = (State)session.getAttribute(this.STATE);
if (state == null) {
state = new State();
State oldState = (State)session.setAttributeIfAbsent(this.STATE, state);
if (oldState != null) {
state = oldState;
}
} 
return state;
}

private class State
{
private State() throws Exception {
for (Map.Entry<Class<?>, MessageEncoderFactory> e : (Iterable<Map.Entry<Class<?>, MessageEncoderFactory>>)DemuxingProtocolEncoder.this.type2encoderFactory.entrySet())
this.type2encoder.put(e.getKey(), ((MessageEncoderFactory)e.getValue()).getEncoder()); 
}

private final ConcurrentHashMap<Class<?>, MessageEncoder> findEncoderCache = new ConcurrentHashMap<Class<?>, MessageEncoder>();
private final Map<Class<?>, MessageEncoder> type2encoder = new ConcurrentHashMap<Class<?>, MessageEncoder>();
}

private static class SingletonMessageEncoderFactory<T> implements MessageEncoderFactory<T> {
private SingletonMessageEncoderFactory(MessageEncoder<T> encoder) {
if (encoder == null) {
throw new IllegalArgumentException("encoder");
}
this.encoder = encoder;
}

public MessageEncoder<T> getEncoder() {
return this.encoder;
}

private final MessageEncoder<T> encoder; }

private static class DefaultConstructorMessageEncoderFactory<T> implements MessageEncoderFactory<T> { private final Class<MessageEncoder<T>> encoderClass;

private DefaultConstructorMessageEncoderFactory(Class<MessageEncoder<T>> encoderClass) {
if (encoderClass == null) {
throw new IllegalArgumentException("encoderClass");
}

if (!MessageEncoder.class.isAssignableFrom(encoderClass)) {
throw new IllegalArgumentException("encoderClass is not assignable to MessageEncoder");
}
this.encoderClass = encoderClass;
}

public MessageEncoder<T> getEncoder() throws Exception {
return this.encoderClass.newInstance();
} }

}

