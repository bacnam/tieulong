package org.apache.http;

import org.apache.http.util.CharArrayBuffer;

public interface FormattedHeader extends Header {
  CharArrayBuffer getBuffer();
  
  int getValuePos();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/FormattedHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */