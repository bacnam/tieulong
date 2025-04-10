

package org.apache.thrift;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TIOStreamTransport;

public class TSerializer {

  private final ByteArrayOutputStream baos_ = new ByteArrayOutputStream();

  private final TIOStreamTransport transport_ = new TIOStreamTransport(baos_);

  private TProtocol protocol_;

  public TSerializer() {
    this(new TBinaryProtocol.Factory());
  }

  public TSerializer(TProtocolFactory protocolFactory) {
    protocol_ = protocolFactory.getProtocol(transport_);
  }

  public byte[] serialize(TBase base) throws TException {
    baos_.reset();
    base.write(protocol_);
    return baos_.toByteArray();
  }

  public String toString(TBase base, String charset) throws TException {
    try {
      return new String(serialize(base), charset);
    } catch (UnsupportedEncodingException uex) {
      throw new TException("JVM DOES NOT SUPPORT ENCODING: " + charset);
    }
  }

  public String toString(TBase base) throws TException {
    return new String(serialize(base));
  }
}

