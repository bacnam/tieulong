package com.mchange.util;

public interface Base64Encoder {
  String encode(byte[] paramArrayOfbyte);
  
  byte[] decode(String paramString) throws Base64FormatException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/Base64Encoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */