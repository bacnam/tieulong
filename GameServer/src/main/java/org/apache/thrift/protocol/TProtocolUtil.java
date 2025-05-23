

package org.apache.thrift.protocol;

import org.apache.thrift.TException;

public class TProtocolUtil {

  private static int maxSkipDepth = Integer.MAX_VALUE;

  public static void setMaxSkipDepth(int depth) {
    maxSkipDepth = depth;
  }

  public static void skip(TProtocol prot, byte type)
    throws TException {
    skip(prot, type, maxSkipDepth);
  }

  public static void skip(TProtocol prot, byte type, int maxDepth)
  throws TException {
    if (maxDepth <= 0) {
      throw new TException("Maximum skip depth exceeded");
    }
    switch (type) {
    case TType.BOOL:
      {
        prot.readBool();
        break;
      }
    case TType.BYTE:
      {
        prot.readByte();
        break;
      }
    case TType.I16:
      {
        prot.readI16();
        break;
      }
    case TType.I32:
      {
        prot.readI32();
        break;
      }
    case TType.I64:
      {
        prot.readI64();
        break;
      }
    case TType.DOUBLE:
      {
        prot.readDouble();
        break;
      }
    case TType.STRING:
      {
        prot.readBinary();
        break;
      }
    case TType.STRUCT:
      {
        prot.readStructBegin();
        while (true) {
          TField field = prot.readFieldBegin();
          if (field.type == TType.STOP) {
            break;
          }
          skip(prot, field.type, maxDepth - 1);
          prot.readFieldEnd();
        }
        prot.readStructEnd();
        break;
      }
    case TType.MAP:
      {
        TMap map = prot.readMapBegin();
        for (int i = 0; i < map.size; i++) {
          skip(prot, map.keyType, maxDepth - 1);
          skip(prot, map.valueType, maxDepth - 1);
        }
        prot.readMapEnd();
        break;
      }
    case TType.SET:
      {
        TSet set = prot.readSetBegin();
        for (int i = 0; i < set.size; i++) {
          skip(prot, set.elemType, maxDepth - 1);
        }
        prot.readSetEnd();
        break;
      }
    case TType.LIST:
      {
        TList list = prot.readListBegin();
        for (int i = 0; i < list.size; i++) {
          skip(prot, list.elemType, maxDepth - 1);
        }
        prot.readListEnd();
        break;
      }
    default:
      break;
    }
  }
}
