package com.mchange.util;

public interface Base64Encoder {
  String encode(byte[] paramArrayOfbyte);

  byte[] decode(String paramString) throws Base64FormatException;
}

