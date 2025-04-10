package org.apache.mina.filter.codec.serialization;

import java.io.NotSerializableException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ObjectSerializationEncoder
extends ProtocolEncoderAdapter
{
private int maxObjectSize = Integer.MAX_VALUE;

public int getMaxObjectSize() {
return this.maxObjectSize;
}

public void setMaxObjectSize(int maxObjectSize) {
if (maxObjectSize <= 0) {
throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
}

this.maxObjectSize = maxObjectSize;
}

public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
if (!(message instanceof java.io.Serializable)) {
throw new NotSerializableException();
}

IoBuffer buf = IoBuffer.allocate(64);
buf.setAutoExpand(true);
buf.putObject(message);

int objectSize = buf.position() - 4;
if (objectSize > this.maxObjectSize) {
throw new IllegalArgumentException("The encoded object is too big: " + objectSize + " (> " + this.maxObjectSize + ')');
}

buf.flip();
out.write(buf);
}
}

