package com.zhonglian.server.common.utils.secure;

import BaseCommon.CommLog;

import java.io.*;

public class SecureUtils {
    public static final String byteArray2StringHex(byte[] byteArray) {
        StringBuilder buf = new StringBuilder(byteArray.length * 2);

        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xFF) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((byteArray[i] & 0xFF), 16));
        }
        return buf.toString();
    }

    public static final byte[] stringHex2ByteArray(String str) {
        if (str == null) {
            return null;
        }
        int iCount = str.length() / 2;
        byte[] byarrRet = new byte[iCount];
        for (int i = 0; i < iCount; i++) {
            byarrRet[i] = (byte) (hex2Int(str.charAt(i * 2)) * 16 + hex2Int(str.charAt(i * 2 + 1)));
        }

        return byarrRet;
    }

    public static final byte[] string2ByteArray(String str) {
        if (str == null) {
            return null;
        }

        return str.getBytes();
    }

    public static final int hex2Int(char ch) {
        if (ch >= 'a' && ch <= 'f')
            return ch - 97 + 10;
        if (ch >= '0' && ch <= '9') {
            return ch - 48;
        }

        return -1;
    }

    public static final String decryptConfigFile(String strConfigFile) {
        String strConfigFileTmp = null;

        InputStream isEncrypt = null;
        try {
            isEncrypt = new FileInputStream(strConfigFile);
        } catch (FileNotFoundException e1) {
            CommLog.error(null, e1);
        }

        if (isEncrypt == null) {
            return strConfigFileTmp;
        }

        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(isEncrypt, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            CommLog.error(null, e1);
        }

        if (isr == null) {
            return strConfigFileTmp;
        }

        BufferedReader br = null;

        br = new BufferedReader(isr);

        try {
            StringBuilder sbContent = new StringBuilder();
            String strLine = br.readLine();
            while (strLine != null) {
                sbContent.append(strLine);
                strLine = br.readLine();
            }

            sbContent.deleteCharAt(0);

            CSTea tea = new CSTea();
            String strPlainText = tea.decrypt(stringHex2ByteArray(sbContent.toString()), CSTea.CONFIG_FILE_KEY);

            strConfigFileTmp = String.valueOf(strConfigFile) + ".tmp";

            File file = new File(strConfigFileTmp);
            if (file.isFile() && file.exists()) {
                file.delete();
            }

            FileOutputStream fosTemp = new FileOutputStream(strConfigFileTmp);
            fosTemp.write(strPlainText.getBytes());
            fosTemp.close();
        } catch (IOException e) {
            CommLog.error(null, e);
        }

        return strConfigFileTmp;
    }
}

