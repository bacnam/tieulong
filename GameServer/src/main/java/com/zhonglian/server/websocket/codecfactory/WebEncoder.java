package com.zhonglian.server.websocket.codecfactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class WebEncoder
implements ProtocolEncoder
{
public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
byte[] _protocol = null;
if (message instanceof String) {
_protocol = ((String)message).getBytes("UTF-8");
} else if (message instanceof ByteBuffer) {
_protocol = encode(((ByteBuffer)message).array());
} 
out.write(IoBuffer.wrap(_protocol));
}

public static byte[] encode(byte[] msgByte) throws UnsupportedEncodingException {
int masking_key_startIndex = 2;

if (msgByte.length <= 125) {
masking_key_startIndex = 2;
} else if (msgByte.length > 65536) {
masking_key_startIndex = 10;
} else if (msgByte.length > 125) {
masking_key_startIndex = 4;
} 

byte[] result = new byte[msgByte.length + masking_key_startIndex];

result[0] = -126;

if (msgByte.length <= 125) {
result[1] = (byte)msgByte.length;
} else if (msgByte.length > 65536) {
result[1] = Byte.MAX_VALUE;
} else if (msgByte.length > 125) {
result[1] = 126;
result[2] = (byte)(msgByte.length >> 8);
result[3] = (byte)(msgByte.length % 256);
} 

for (int i = 0; i < msgByte.length; i++) {
result[i + masking_key_startIndex] = msgByte[i];
}

return result;
}

public void dispose(IoSession session) throws Exception {}
}

