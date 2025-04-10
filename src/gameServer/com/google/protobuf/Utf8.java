

package com.google.protobuf;

final class Utf8 {
  private Utf8() {}

  public static final int COMPLETE = 0;

  public static final int MALFORMED = -1;

  public static boolean isValidUtf8(byte[] bytes) {
    return isValidUtf8(bytes, 0, bytes.length);
  }

  public static boolean isValidUtf8(byte[] bytes, int index, int limit) {
    return partialIsValidUtf8(bytes, index, limit) == COMPLETE;
  }

  public static int partialIsValidUtf8(
      int state, byte[] bytes, int index, int limit) {
    if (state != COMPLETE) {

      if (index >= limit) {  
        return state;
      }
      int byte1 = (byte) state;

      if (byte1 < (byte) 0xE0) {

        if (byte1 < (byte) 0xC2 ||

            bytes[index++] > (byte) 0xBF) {
          return MALFORMED;
        }
      } else if (byte1 < (byte) 0xF0) {

        int byte2 = (byte) ~(state >> 8);
        if (byte2 == 0) {
          byte2 = bytes[index++];
          if (index >= limit) {
            return incompleteStateFor(byte1, byte2);
          }
        }
        if (byte2 > (byte) 0xBF ||

            (byte1 == (byte) 0xE0 && byte2 < (byte) 0xA0) ||

            (byte1 == (byte) 0xED && byte2 >= (byte) 0xA0) ||

            bytes[index++] > (byte) 0xBF) {
          return MALFORMED;
        }
      } else {

        int byte2 = (byte) ~(state >> 8);
        int byte3 = 0;
        if (byte2 == 0) {
          byte2 = bytes[index++];
          if (index >= limit) {
            return incompleteStateFor(byte1, byte2);
          }
        } else {
          byte3 = (byte) (state >> 16);
        }
        if (byte3 == 0) {
          byte3 = bytes[index++];
          if (index >= limit) {
            return incompleteStateFor(byte1, byte2, byte3);
          }
        }

        if (byte2 > (byte) 0xBF ||

            (((byte1 << 28) + (byte2 - (byte) 0x90)) >> 30) != 0 ||

            byte3 > (byte) 0xBF ||

             bytes[index++] > (byte) 0xBF) {
          return MALFORMED;
        }
      }
    }

    return partialIsValidUtf8(bytes, index, limit);
  }

  public static int partialIsValidUtf8(
      byte[] bytes, int index, int limit) {

    while (index < limit && bytes[index] >= 0) {
      index++;
    }

    return (index >= limit) ? COMPLETE :
        partialIsValidUtf8NonAscii(bytes, index, limit);
  }

  private static int partialIsValidUtf8NonAscii(
      byte[] bytes, int index, int limit) {
    for (;;) {
      int byte1, byte2;

      do {
        if (index >= limit) {
          return COMPLETE;
        }
      } while ((byte1 = bytes[index++]) >= 0);

      if (byte1 < (byte) 0xE0) {

        if (index >= limit) {
          return byte1;
        }

        if (byte1 < (byte) 0xC2 ||
            bytes[index++] > (byte) 0xBF) {
          return MALFORMED;
        }
      } else if (byte1 < (byte) 0xF0) {

        if (index >= limit - 1) { 
          return incompleteStateFor(bytes, index, limit);
        }
        if ((byte2 = bytes[index++]) > (byte) 0xBF ||

            (byte1 == (byte) 0xE0 && byte2 < (byte) 0xA0) ||

            (byte1 == (byte) 0xED && byte2 >= (byte) 0xA0) ||

            bytes[index++] > (byte) 0xBF) {
          return MALFORMED;
        }
      } else {

        if (index >= limit - 2) {  
          return incompleteStateFor(bytes, index, limit);
        }
        if ((byte2 = bytes[index++]) > (byte) 0xBF ||

            (((byte1 << 28) + (byte2 - (byte) 0x90)) >> 30) != 0 ||

            bytes[index++] > (byte) 0xBF ||

            bytes[index++] > (byte) 0xBF) {
          return MALFORMED;
        }
      }
    }
  }

  private static int incompleteStateFor(int byte1) {
    return (byte1 > (byte) 0xF4) ?
        MALFORMED : byte1;
  }

  private static int incompleteStateFor(int byte1, int byte2) {
    return (byte1 > (byte) 0xF4 ||
            byte2 > (byte) 0xBF) ?
        MALFORMED : byte1 ^ (byte2 << 8);
  }

  private static int incompleteStateFor(int byte1, int byte2, int byte3) {
    return (byte1 > (byte) 0xF4 ||
            byte2 > (byte) 0xBF ||
            byte3 > (byte) 0xBF) ?
        MALFORMED : byte1 ^ (byte2 << 8) ^ (byte3 << 16);
  }

  private static int incompleteStateFor(byte[] bytes, int index, int limit) {
    int byte1 = bytes[index - 1];
    switch (limit - index) {
      case 0: return incompleteStateFor(byte1);
      case 1: return incompleteStateFor(byte1, bytes[index]);
      case 2: return incompleteStateFor(byte1, bytes[index], bytes[index + 1]);
      default: throw new AssertionError();
    }
  }
}
