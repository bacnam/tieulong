package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import org.apache.mina.core.buffer.IoBuffer;

public interface IoAbsoluteReader {
  int first();

  int last();

  int length();

  ByteArray slice(int paramInt1, int paramInt2);

  ByteOrder order();

  byte get(int paramInt);

  void get(int paramInt, IoBuffer paramIoBuffer);

  short getShort(int paramInt);

  int getInt(int paramInt);

  long getLong(int paramInt);

  float getFloat(int paramInt);

  double getDouble(int paramInt);

  char getChar(int paramInt);
}

