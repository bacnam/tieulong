package org.apache.http.impl.auth;

import java.io.IOException;

@Deprecated
public interface SpnegoTokenGenerator {
  byte[] generateSpnegoDERObject(byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/auth/SpnegoTokenGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */