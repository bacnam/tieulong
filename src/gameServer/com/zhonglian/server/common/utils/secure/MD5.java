package com.zhonglian.server.common.utils.secure;

import BaseCommon.CommLog;
import java.security.MessageDigest;

public class MD5
{
public static String md5(String inStr) {
MessageDigest md5 = null;
byte[] byteArray = null;
try {
md5 = MessageDigest.getInstance("MD5");
byteArray = inStr.getBytes("utf-8");
} catch (Exception e) {
CommLog.error("生成MD5串错误");
return "";
} 
byte[] md5Bytes = md5.digest(byteArray);
StringBuffer hexValue = new StringBuffer();
for (int i = 0; i < md5Bytes.length; i++) {
int val = md5Bytes[i] & 0xFF;
if (val < 16)
hexValue.append("0"); 
hexValue.append(Integer.toHexString(val));
} 
return hexValue.toString();
}
}

