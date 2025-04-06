package com.google.common.io;

import java.io.DataInput;

public interface ByteArrayDataInput extends DataInput {
  void readFully(byte[] paramArrayOfbyte);
  
  void readFully(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  int skipBytes(int paramInt);
  
  boolean readBoolean();
  
  byte readByte();
  
  int readUnsignedByte();
  
  short readShort();
  
  int readUnsignedShort();
  
  char readChar();
  
  int readInt();
  
  long readLong();
  
  float readFloat();
  
  double readDouble();
  
  String readLine();
  
  String readUTF();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/io/ByteArrayDataInput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */