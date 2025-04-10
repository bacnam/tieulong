package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class IntegerDecodingState
implements DecodingState
{
private int firstByte;
private int secondByte;
private int thirdByte;
private int counter;

public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
while (in.hasRemaining()) {
switch (this.counter) {
case 0:
this.firstByte = in.getUnsigned();
break;
case 1:
this.secondByte = in.getUnsigned();
break;
case 2:
this.thirdByte = in.getUnsigned();
break;
case 3:
this.counter = 0;
return finishDecode(this.firstByte << 24 | this.secondByte << 16 | this.thirdByte << 8 | in.getUnsigned(), out);
default:
throw new InternalError();
} 
this.counter++;
} 

return this;
}

public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
throw new ProtocolDecoderException("Unexpected end of session while waiting for an integer.");
}

protected abstract DecodingState finishDecode(int paramInt, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}

