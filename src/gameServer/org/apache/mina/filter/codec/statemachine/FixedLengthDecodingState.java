package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class FixedLengthDecodingState
implements DecodingState
{
private final int length;
private IoBuffer buffer;

public FixedLengthDecodingState(int length) {
this.length = length;
}

public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
if (this.buffer == null) {
if (in.remaining() >= this.length) {
int limit = in.limit();
in.limit(in.position() + this.length);
IoBuffer product = in.slice();
in.position(in.position() + this.length);
in.limit(limit);
return finishDecode(product, out);
} 

this.buffer = IoBuffer.allocate(this.length);
this.buffer.put(in);
return this;
} 

if (in.remaining() >= this.length - this.buffer.position()) {
int limit = in.limit();
in.limit(in.position() + this.length - this.buffer.position());
this.buffer.put(in);
in.limit(limit);
IoBuffer product = this.buffer;
this.buffer = null;
return finishDecode(product.flip(), out);
} 

this.buffer.put(in);
return this;
}

public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
IoBuffer readData;
if (this.buffer == null) {
readData = IoBuffer.allocate(0);
} else {
readData = this.buffer.flip();
this.buffer = null;
} 
return finishDecode(readData, out);
}

protected abstract DecodingState finishDecode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

