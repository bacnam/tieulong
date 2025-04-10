package com.zhonglian.server.common.utils.secure;

public class CSTea
{
public static String CONFIG_FILE_KEY = "789f5645f68bd5a481963ffa458fac58";

private int TEA_TIMES = 32;

public String encrypt(String strPlainText, String strKey) {
CSTeaImpl teaImpl = new CSTeaImpl();
byte[] byarrPlainText = strPlainText.getBytes();
int iPaddingCount = 8 - byarrPlainText.length % 8;
byte[] byarrEncrypt = new byte[byarrPlainText.length + iPaddingCount];
byarrEncrypt[0] = (byte)iPaddingCount;
System.arraycopy(byarrPlainText, 0, byarrEncrypt, iPaddingCount, byarrPlainText.length);
byte[] byarrCipherText = new byte[byarrEncrypt.length];
for (int offset = 0; offset < byarrCipherText.length; offset += 8) {
byte[] tempEncrpt = teaImpl.encrypt(byarrEncrypt, offset, _generateKey(strKey), this.TEA_TIMES);
System.arraycopy(tempEncrpt, 0, byarrCipherText, offset, 8);
} 
return SecureUtils.byteArray2StringHex(byarrCipherText);
}

public String decrypt(byte[] byarrCipher, String strKey) {
CSTeaImpl teaImpl = new CSTeaImpl();
byte[] byarrDecrypt = null;
byte[] byarrDecryptTmp = new byte[byarrCipher.length];
for (int offset = 0; offset < byarrCipher.length; offset += 8) {
byarrDecrypt = teaImpl.decrypt(byarrCipher, offset, _generateKey(strKey), this.TEA_TIMES);
System.arraycopy(byarrDecrypt, 0, byarrDecryptTmp, offset, 8);
} 

int iPadding = byarrDecryptTmp[0];
return new String(byarrDecryptTmp, iPadding, byarrDecrypt.length - iPadding);
}

private int[] _generateKey(String strKey) {
int[] iarrRet = new int[4];

String strKey1 = strKey.substring(0, 8);
String strKey2 = strKey.substring(8, 16);
String strKey3 = strKey.substring(16, 24);
String strKey4 = strKey.substring(24, 32);

iarrRet[0] = Long.valueOf(strKey1, 16).intValue();
iarrRet[1] = Long.valueOf(strKey2, 16).intValue();
iarrRet[2] = Long.valueOf(strKey3, 16).intValue();
iarrRet[3] = Long.valueOf(strKey4, 16).intValue();

return iarrRet;
}
}

