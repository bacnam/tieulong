package org.apache.mina.filter.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

public abstract class CumulativeProtocolDecoder
extends ProtocolDecoderAdapter
{
private final AttributeKey BUFFER = new AttributeKey(getClass(), "buffer");

public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
if (!session.getTransportMetadata().hasFragmentation()) { do {  }
while (in.hasRemaining() && 
doDecode(session, in, out));

return; }

boolean usingSessionBuffer = true;
IoBuffer buf = (IoBuffer)session.getAttribute(this.BUFFER);

if (buf != null) {
boolean appended = false;

if (buf.isAutoExpand()) {
try {
buf.put(in);
appended = true;
} catch (IllegalStateException e) {

} catch (IndexOutOfBoundsException e) {}
}

if (appended) {
buf.flip();
}
else {

buf.flip();
IoBuffer newBuf = IoBuffer.allocate(buf.remaining() + in.remaining()).setAutoExpand(true);
newBuf.order(buf.order());
newBuf.put(buf);
newBuf.put(in);
newBuf.flip();
buf = newBuf;

session.setAttribute(this.BUFFER, buf);
} 
} else {
buf = in;
usingSessionBuffer = false;
} 

while (true) {
int oldPos = buf.position();
boolean decoded = doDecode(session, buf, out);
if (decoded) {
if (buf.position() == oldPos) {
throw new IllegalStateException("doDecode() can't return true when buffer is not consumed.");
}

if (!buf.hasRemaining()) {
break;
}

continue;
} 

break;
} 

if (buf.hasRemaining()) {
if (usingSessionBuffer && buf.isAutoExpand()) {
buf.compact();
} else {
storeRemainingInSession(buf, session);
}

} else if (usingSessionBuffer) {
removeSessionBuffer(session);
} 
}

protected abstract boolean doDecode(IoSession paramIoSession, IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;

public void dispose(IoSession session) throws Exception {
removeSessionBuffer(session);
}

private void removeSessionBuffer(IoSession session) {
session.removeAttribute(this.BUFFER);
}

private void storeRemainingInSession(IoBuffer buf, IoSession session) {
IoBuffer remainingBuf = IoBuffer.allocate(buf.capacity()).setAutoExpand(true);

remainingBuf.order(buf.order());
remainingBuf.put(buf);

session.setAttribute(this.BUFFER, remainingBuf);
}
}

