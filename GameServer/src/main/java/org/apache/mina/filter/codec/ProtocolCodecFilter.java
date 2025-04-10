package org.apache.mina.filter.codec;

import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.NothingWrittenException;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolCodecFilter
extends IoFilterAdapter
{
private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolCodecFilter.class);

private static final Class<?>[] EMPTY_PARAMS = new Class[0];

private static final IoBuffer EMPTY_BUFFER = IoBuffer.wrap(new byte[0]);

private static final AttributeKey ENCODER = new AttributeKey(ProtocolCodecFilter.class, "encoder");

private static final AttributeKey DECODER = new AttributeKey(ProtocolCodecFilter.class, "decoder");

private static final AttributeKey DECODER_OUT = new AttributeKey(ProtocolCodecFilter.class, "decoderOut");

private static final AttributeKey ENCODER_OUT = new AttributeKey(ProtocolCodecFilter.class, "encoderOut");
private final ProtocolCodecFactory factory;
private final Semaphore lock;

public ProtocolCodecFilter(ProtocolCodecFactory factory) {
this.lock = new Semaphore(1, true);

if (factory == null) {
throw new IllegalArgumentException("factory");
}

this.factory = factory;
}

public ProtocolCodecFilter(final ProtocolEncoder encoder, final ProtocolDecoder decoder) {
this.lock = new Semaphore(1, true);
if (encoder == null) {
throw new IllegalArgumentException("encoder");
}
if (decoder == null) {
throw new IllegalArgumentException("decoder");
}

this.factory = new ProtocolCodecFactory() {
public ProtocolEncoder getEncoder(IoSession session) {
return encoder;
}

public ProtocolDecoder getDecoder(IoSession session) {
return decoder;
}
};
}

public ProtocolCodecFilter(Class<? extends ProtocolEncoder> encoderClass, Class<? extends ProtocolDecoder> decoderClass) {
final ProtocolEncoder encoder;
final ProtocolDecoder decoder;
this.lock = new Semaphore(1, true);
if (encoderClass == null) {
throw new IllegalArgumentException("encoderClass");
}
if (decoderClass == null) {
throw new IllegalArgumentException("decoderClass");
}
if (!ProtocolEncoder.class.isAssignableFrom(encoderClass)) {
throw new IllegalArgumentException("encoderClass: " + encoderClass.getName());
}
if (!ProtocolDecoder.class.isAssignableFrom(decoderClass)) {
throw new IllegalArgumentException("decoderClass: " + decoderClass.getName());
}
try {
encoderClass.getConstructor(EMPTY_PARAMS);
} catch (NoSuchMethodException e) {
throw new IllegalArgumentException("encoderClass doesn't have a public default constructor.");
} 
try {
decoderClass.getConstructor(EMPTY_PARAMS);
} catch (NoSuchMethodException e) {
throw new IllegalArgumentException("decoderClass doesn't have a public default constructor.");
} 

try {
encoder = encoderClass.newInstance();
} catch (Exception e) {
throw new IllegalArgumentException("encoderClass cannot be initialized");
} 

try {
decoder = decoderClass.newInstance();
} catch (Exception e) {
throw new IllegalArgumentException("decoderClass cannot be initialized");
} 

this.factory = new ProtocolCodecFactory() {
public ProtocolEncoder getEncoder(IoSession session) throws Exception {
return encoder;
}

public ProtocolDecoder getDecoder(IoSession session) throws Exception {
return decoder;
}
};
}

public ProtocolEncoder getEncoder(IoSession session) {
return (ProtocolEncoder)session.getAttribute(ENCODER);
}

public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
if (parent.contains((IoFilter)this)) {
throw new IllegalArgumentException("You can't add the same filter instance more than once.  Create another instance and add it.");
}
}

public void onPostRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
disposeCodec(parent.getSession());
}

public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
LOGGER.debug("Processing a MESSAGE_RECEIVED for session {}", Long.valueOf(session.getId()));

if (!(message instanceof IoBuffer)) {
nextFilter.messageReceived(session, message);

return;
} 
IoBuffer in = (IoBuffer)message;
ProtocolDecoder decoder = this.factory.getDecoder(session);
ProtocolDecoderOutput decoderOut = getDecoderOut(session, nextFilter);

while (in.hasRemaining()) {
int oldPos = in.position();

try { this.lock.acquire();

decoder.decode(session, in, decoderOut);

decoderOut.flush(nextFilter, session); }
catch (Exception e)
{ ProtocolDecoderException pde;
if (e instanceof ProtocolDecoderException) {
pde = (ProtocolDecoderException)e;
} else {
pde = new ProtocolDecoderException(e);
} 
if (pde.getHexdump() == null) {

int curPos = in.position();
in.position(oldPos);
pde.setHexdump(in.getHexDump());
in.position(curPos);
} 

decoderOut.flush(nextFilter, session);
nextFilter.exceptionCaught(session, pde);

if (!(e instanceof RecoverableProtocolDecoderException) || in.position() == oldPos)

{ 

this.lock.release(); break; }  } finally { this.lock.release(); }

} 
}

public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
if (writeRequest instanceof EncodedWriteRequest) {
return;
}

if (writeRequest instanceof MessageWriteRequest) {
MessageWriteRequest wrappedRequest = (MessageWriteRequest)writeRequest;
nextFilter.messageSent(session, wrappedRequest.getParentRequest());
} else {
nextFilter.messageSent(session, writeRequest);
} 
}

public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
Object message = writeRequest.getMessage();

if (message instanceof IoBuffer || message instanceof org.apache.mina.core.file.FileRegion) {
nextFilter.filterWrite(session, writeRequest);

return;
} 

ProtocolEncoder encoder = this.factory.getEncoder(session);

ProtocolEncoderOutput encoderOut = getEncoderOut(session, nextFilter, writeRequest);

if (encoder == null) {
throw new ProtocolEncoderException("The encoder is null for the session " + session);
}

if (encoderOut == null) {
throw new ProtocolEncoderException("The encoderOut is null for the session " + session);
}

try {
encoder.encode(session, message, encoderOut);

Queue<Object> bufferQueue = ((AbstractProtocolEncoderOutput)encoderOut).getMessageQueue();

while (!bufferQueue.isEmpty()) {
Object encodedMessage = bufferQueue.poll();

if (encodedMessage == null) {
break;
}

if (!(encodedMessage instanceof IoBuffer) || ((IoBuffer)encodedMessage).hasRemaining()) {
SocketAddress destination = writeRequest.getDestination();
EncodedWriteRequest encodedWriteRequest = new EncodedWriteRequest(encodedMessage, null, destination);

nextFilter.filterWrite(session, (WriteRequest)encodedWriteRequest);
} 
} 

nextFilter.filterWrite(session, (WriteRequest)new MessageWriteRequest(writeRequest));
} catch (Exception e) {
ProtocolEncoderException pee;

if (e instanceof ProtocolEncoderException) {
pee = (ProtocolEncoderException)e;
} else {
pee = new ProtocolEncoderException(e);
} 

throw pee;
} 
}

public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
ProtocolDecoder decoder = this.factory.getDecoder(session);
ProtocolDecoderOutput decoderOut = getDecoderOut(session, nextFilter);

try {
decoder.finishDecode(session, decoderOut);
} catch (Exception e) {
ProtocolDecoderException pde;
if (e instanceof ProtocolDecoderException) {
pde = (ProtocolDecoderException)e;
} else {
pde = new ProtocolDecoderException(e);
} 
throw pde;
} finally {

disposeCodec(session);
decoderOut.flush(nextFilter, session);
} 

nextFilter.sessionClosed(session);
}

private static class EncodedWriteRequest extends DefaultWriteRequest {
public EncodedWriteRequest(Object encodedMessage, WriteFuture future, SocketAddress destination) {
super(encodedMessage, future, destination);
}

public boolean isEncoded() {
return true;
}
}

private static class MessageWriteRequest extends WriteRequestWrapper {
public MessageWriteRequest(WriteRequest writeRequest) {
super(writeRequest);
}

public Object getMessage() {
return ProtocolCodecFilter.EMPTY_BUFFER;
}

public String toString() {
return "MessageWriteRequest, parent : " + super.toString();
}
}

private static class ProtocolDecoderOutputImpl
extends AbstractProtocolDecoderOutput
{
public void flush(IoFilter.NextFilter nextFilter, IoSession session) {
Queue<Object> messageQueue = getMessageQueue();

while (!messageQueue.isEmpty()) {
nextFilter.messageReceived(session, messageQueue.poll());
}
}
}

private static class ProtocolEncoderOutputImpl
extends AbstractProtocolEncoderOutput
{
private final IoSession session;
private final IoFilter.NextFilter nextFilter;
private final SocketAddress destination;

public ProtocolEncoderOutputImpl(IoSession session, IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
this.session = session;
this.nextFilter = nextFilter;

this.destination = writeRequest.getDestination();
} public WriteFuture flush() {
DefaultWriteFuture defaultWriteFuture;
WriteFuture writeFuture1;
Queue<Object> bufferQueue = getMessageQueue();
WriteFuture future = null;

while (!bufferQueue.isEmpty()) {
Object encodedMessage = bufferQueue.poll();

if (encodedMessage == null) {
break;
}

if (!(encodedMessage instanceof IoBuffer) || ((IoBuffer)encodedMessage).hasRemaining()) {
defaultWriteFuture = new DefaultWriteFuture(this.session);
this.nextFilter.filterWrite(this.session, (WriteRequest)new ProtocolCodecFilter.EncodedWriteRequest(encodedMessage, (WriteFuture)defaultWriteFuture, this.destination));
} 
} 

if (defaultWriteFuture == null) {

DefaultWriteRequest defaultWriteRequest = new DefaultWriteRequest(DefaultWriteRequest.EMPTY_MESSAGE, null, this.destination);

writeFuture1 = DefaultWriteFuture.newNotWrittenFuture(this.session, (Throwable)new NothingWrittenException((WriteRequest)defaultWriteRequest));
} 

return writeFuture1;
}
}

private void disposeCodec(IoSession session) {
disposeEncoder(session);
disposeDecoder(session);

disposeDecoderOut(session);
}

private void disposeEncoder(IoSession session) {
ProtocolEncoder encoder = (ProtocolEncoder)session.removeAttribute(ENCODER);
if (encoder == null) {
return;
}

try {
encoder.dispose(session);
} catch (Exception e) {
LOGGER.warn("Failed to dispose: " + encoder.getClass().getName() + " (" + encoder + ')');
} 
}

private void disposeDecoder(IoSession session) {
ProtocolDecoder decoder = (ProtocolDecoder)session.removeAttribute(DECODER);
if (decoder == null) {
return;
}

try {
decoder.dispose(session);
} catch (Exception e) {
LOGGER.warn("Failed to dispose: " + decoder.getClass().getName() + " (" + decoder + ')');
} 
}

private ProtocolDecoderOutput getDecoderOut(IoSession session, IoFilter.NextFilter nextFilter) {
ProtocolDecoderOutput out = (ProtocolDecoderOutput)session.getAttribute(DECODER_OUT);

if (out == null) {

out = new ProtocolDecoderOutputImpl();
session.setAttribute(DECODER_OUT, out);
} 

return out;
}

private ProtocolEncoderOutput getEncoderOut(IoSession session, IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
ProtocolEncoderOutput out = (ProtocolEncoderOutput)session.getAttribute(ENCODER_OUT);

if (out == null) {

out = new ProtocolEncoderOutputImpl(session, nextFilter, writeRequest);
session.setAttribute(ENCODER_OUT, out);
} 

return out;
}

private void disposeDecoderOut(IoSession session) {
session.removeAttribute(DECODER_OUT);
}
}

