package com.zhonglian.server.common.utils.secure;

public class CSSymmetric
{
public static String encrypt(String strPlain, String strKey) {
if (strPlain == null) {
return null;
}

if (strKey == null) {
return strPlain;
}

byte[] byarrPlain = strPlain.getBytes();
byte[] byarrKey = SecureUtils.stringHex2ByteArray(strKey);
int iPlainCount = byarrPlain.length;
int iKeyCount = byarrKey.length;
byte[] byarrCipher = new byte[iPlainCount];
for (int i = 0; i < iPlainCount; i++) {
byarrCipher[i] = (byte)(byarrPlain[i] ^ byarrKey[i % iKeyCount]);
}

return SecureUtils.byteArray2StringHex(byarrCipher);
}

public static String decrypt(String strCipher, String strKey) {
if (strCipher == null) {
return null;
}

if (strKey == null) {
return strCipher;
}

byte[] byarrCipher = SecureUtils.stringHex2ByteArray(strCipher);
byte[] byarrKey = SecureUtils.stringHex2ByteArray(strKey);
int iCipherCount = byarrCipher.length;
int iKeyCount = byarrKey.length;
byte[] byarrPlain = new byte[iCipherCount];
for (int i = 0; i < iCipherCount; i++) {
byarrPlain[i] = (byte)(byarrCipher[i] ^ byarrKey[i % iKeyCount]);
}

return new String(byarrPlain);
}
}

