package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class CrLfDecodingState
implements DecodingState
{
private static final byte CR = 13;
private static final byte LF = 10;
private boolean hasCR;

public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
boolean found = false;
boolean finished = false;
while (in.hasRemaining()) {
byte b = in.get();
if (!this.hasCR) {
if (b == 13) {
this.hasCR = true; continue;
} 
if (b == 10) {
found = true;
} else {
in.position(in.position() - 1);
found = false;
} 
finished = true;

break;
} 
if (b == 10) {
found = true;
finished = true;

break;
} 
throw new ProtocolDecoderException("Expected LF after CR but was: " + (b & 0xFF));
} 

if (finished) {
this.hasCR = false;
return finishDecode(found, out);
} 

return this;
}

public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
return finishDecode(false, out);
}

protected abstract DecodingState finishDecode(boolean paramBoolean, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

