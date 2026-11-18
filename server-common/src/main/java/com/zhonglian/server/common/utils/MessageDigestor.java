package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestor {
    public static String md5(String msg) {
        try {
            return md5(msg.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException var2) {
            CommLog.error(var2.getMessage(), (Throwable)var2);
            return null;
        }
    }

    public static String md5(byte[] msg) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buf = md5.digest(msg);
            return ByteUtils.toHexAscii(buf);
        } catch (NoSuchAlgorithmException var3) {
            CommLog.error("MessageDigestor.md5", (Throwable)var3);
            return null;
        }
    }

    public static String sha256(String msg) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] bufOther = msg.getBytes("UTF-8");
            byte[] bufRes = sha.digest(bufOther);
            return ByteUtils.toHexAscii(bufRes);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException var4) {
            CommLog.error("MessageDigestor.sha256", (Throwable)var4);
            return null;
        }
    }
}
