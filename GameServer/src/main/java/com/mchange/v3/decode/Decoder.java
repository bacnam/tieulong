package com.mchange.v3.decode;

public interface Decoder {
  Object decode(Object paramObject) throws CannotDecodeException;
}

