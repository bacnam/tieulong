package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestor {
    public static String md5(String msg) {
        try {
            return md5(msg.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            CommLog.error(e.getMessage(), e);
            return null;
        }
    }

    public static String md5(byte[] msg) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buf = md5.digest(msg);
            return ByteUtils.toHexAscii(buf);
        } catch (NoSuchAlgorithmException ex) {
            CommLog.error("MessageDigestor.md5", ex);
            return null;
        }
    }

    public static String sha256(String msg) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");

            byte[] bufOther = msg.getBytes("UTF-8");
            byte[] bufRes = sha.digest(bufOther);
            return ByteUtils.toHexAscii(bufRes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            CommLog.error("MessageDigestor.sha256", ex);
            return null;
        }
    }
}

