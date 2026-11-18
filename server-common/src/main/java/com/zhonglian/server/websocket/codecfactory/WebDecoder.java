package com.zhonglian.server.websocket.codecfactory;

import java.nio.ByteBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class WebDecoder extends DemuxingProtocolDecoder {
    public static final byte MASK = 1;
    public static final byte HAS_EXTEND_DATA = 126;
    public static final byte HAS_EXTEND_DATA_CONTINUE = 127;
    public static final byte PAYLOADLEN = 127;
    public static final byte OPCODE = 15;

    public WebDecoder() {
        this.addMessageDecoder(new WebDecoder.BaseSocketBeanDecoder());
    }

    class BaseSocketBeanDecoder extends MessageDecoderAdapter {
        @Override
        public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
            if (in.remaining() < 2) {
                return NEED_DATA;
            } else {
                byte head1 = in.get();
                int opcode = head1 & 15;
                if (opcode == 8) {
                    session.close(true);
                    return MessageDecoderResult.NOT_OK;
                } else {
                    byte head2 = in.get();
                    byte datalength = (byte)(head2 & 127);
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
                            length += (extended[i] & 255) << shift;
                            shift += 8;
                        }
                    } else if (datalength == 127) {
                        if (in.remaining() < 4) {
                            return NEED_DATA;
                        }

                        byte[] extended = new byte[4];
                        in.get(extended);
                        int shift = 0;
                        length = 0;

                        for (int i = extended.length - 1; i >= 0; i--) {
                            length += (extended[i] & 255) << shift;
                            shift += 8;
                        }
                    }

                    int ismask = head2 >> 7 & 1;
                    if (ismask == 1) {
                        return in.remaining() < 4 + length ? NEED_DATA : OK;
                    } else {
                        return in.remaining() < length ? NEED_DATA : OK;
                    }
                }
            }
        }

        @Override
        public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
            in.get();
            byte head2 = in.get();
            int length = (byte)(head2 & 127);
            if (length >= 126) {
                if (length == 126) {
                    byte[] extended = new byte[2];
                    in.get(extended);
                    int shift = 0;
                    length = 0;

                    for (int i = extended.length - 1; i >= 0; i--) {
                        length += (extended[i] & 255) << shift;
                        shift += 8;
                    }
                } else if (length == 127) {
                    byte[] extended = new byte[4];
                    in.get(extended);
                    int shift = 0;
                    length = 0;

                    for (int i = extended.length - 1; i >= 0; i--) {
                        length += (extended[i] & 255) << shift;
                        shift += 8;
                    }
                }
            }

            int ismask = head2 >> 7 & 1;
            byte[] data = null;
            if (ismask == 1) {
                byte[] mask = new byte[4];
                in.get(mask);
                data = new byte[Math.min(length, in.remaining())];
                in.get(data);

                for (int i = 0; i < data.length; i++) {
                    data[i] ^= mask[i % 4];
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
