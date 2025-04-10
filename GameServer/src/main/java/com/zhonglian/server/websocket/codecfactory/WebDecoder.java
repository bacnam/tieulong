package com.zhonglian.server.websocket.codecfactory;

import java.nio.ByteBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class WebDecoder
extends DemuxingProtocolDecoder
{
public static final byte MASK = 1;
public static final byte HAS_EXTEND_DATA = 126;
public static final byte HAS_EXTEND_DATA_CONTINUE = 127;
public static final byte PAYLOADLEN = 127;
public static final byte OPCODE = 15;

public WebDecoder() {
addMessageDecoder((MessageDecoder)new BaseSocketBeanDecoder());
}

class BaseSocketBeanDecoder extends MessageDecoderAdapter {
public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
if (in.remaining() < 2) {
return NEED_DATA;
}
byte head1 = in.get();
int opcode = head1 & 0xF;
if (opcode == 8) {
session.close(true);
return MessageDecoderResult.NOT_OK;
} 
byte head2 = in.get();
byte datalength = (byte)(head2 & Byte.MAX_VALUE);
int length = 0;
if (datalength < 126) {
length = datalength;
} else if (datalength == 126) {
if (in.remaining() < 2) {
return NEED_DATA;
}
byte[] extended = new byte[2];
in.get(extended);
int shift = 0;
length = 0;
for (int i = extended.length - 1; i >= 0; i--) {
length += (extended[i] & 0xFF) << shift;
shift += 8;
} 
} else if (datalength == Byte.MAX_VALUE) {
if (in.remaining() < 4) {
return NEED_DATA;
}
byte[] extended = new byte[4];
in.get(extended);
int shift = 0;
length = 0;
for (int i = extended.length - 1; i >= 0; i--) {
length += (extended[i] & 0xFF) << shift;
shift += 8;
} 
} 

int ismask = head2 >> 7 & 0x1;
if (ismask == 1) {
if (in.remaining() < 4 + length) {
return NEED_DATA;
}
return OK;
} 

if (in.remaining() < length) {
return NEED_DATA;
}
return OK;
}

public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
in.get();
byte head2 = in.get();
int length = (byte)(head2 & Byte.MAX_VALUE);
if (length >= 126) {
if (length == 126) {
byte[] extended = new byte[2];
in.get(extended);
int shift = 0;
length = 0;
for (int i = extended.length - 1; i >= 0; i--) {
length += (extended[i] & 0xFF) << shift;
shift += 8;
} 
} else if (length == 127) {
byte[] extended = new byte[4];
in.get(extended);
int shift = 0;
length = 0;
for (int i = extended.length - 1; i >= 0; i--) {
length += (extended[i] & 0xFF) << shift;
shift += 8;
} 
} 
}
int ismask = head2 >> 7 & 0x1;
byte[] data = null;
if (ismask == 1) {

byte[] mask = new byte[4];
in.get(mask);

data = new byte[Math.min(length, in.remaining())];
in.get(data);
for (int i = 0; i < data.length; i++)
{
data[i] = (byte)(data[i] ^ mask[i % 4]);
}
out.write(ByteBuffer.wrap(data));
} else {

byte[] b = new byte[in.remaining()];
in.get(b);
out.write(new String(b, "UTF-8"));
} 
return OK;
}
}
}

