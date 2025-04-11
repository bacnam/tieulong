package com.zhonglian.server.http.client;

import BaseCommon.CommLog;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class HttpUtil {
    public static String sendHttpGet2Web(int connTimeOutMs, int readTimeOutMs, String strGetUrl, String strPostParam, String exceptionRes) {
        String strRet = exceptionRes;

        HttpURLConnection connection = null;
        BufferedReader br = null;

        try {
            String strWholeUrl;
            if (strPostParam != null) {
                strWholeUrl = String.valueOf(strGetUrl) + "?" + strPostParam;
            } else {
                strWholeUrl = strGetUrl;
            }
            URL url = new URL(strWholeUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(connTimeOutMs);
            connection.setReadTimeout(readTimeOutMs);
            connection.setRequestMethod("GET");
            connection.connect();
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            strRet = br.readLine();

            if (strRet != null) {
                strRet = _normReturn(strRet);
            }

            br.close();
            connection.disconnect();
        } catch (Exception e) {
            CommLog.error(e.getMessage(), e);

            try {
                if (br != null)
                    br.close();
            } catch (Exception exception) {
            }

            try {
                if (connection != null)
                    connection.disconnect();
            } catch (Exception exception) {
            }
        }

        return strRet;
    }

    public static String sendHttpPost2Web(int connTimeOutMs, int readTimeOutMs, String _strPostUrl, String _strPostParam, String exceptionRes) {
        String strRet = exceptionRes;

        HttpURLConnection connection = null;
        BufferedReader br = null;
        try {
            URL url = new URL(_strPostUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(connTimeOutMs);
            connection.setReadTimeout(readTimeOutMs);
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            if (_strPostParam != null) {
                out.writeBytes(_strPostParam);
                out.flush();
            }
            out.close();

            InputStream isRet = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(isRet, "UTF-8"));

            strRet = "";
            String strRetTemp = null;
            while ((strRetTemp = br.readLine()) != null) {
                strRet = String.valueOf(strRet) + strRetTemp;
                strRetTemp = null;
            }

            br.close();
            connection.disconnect();
        } catch (SocketTimeoutException e) {
            CommLog.error("HttpPost time out for: [{} ?{}]", _strPostUrl, _strPostParam);
            try {
                if (br != null)
                    br.close();
            } catch (Exception exception) {
            }

            try {
                if (connection != null)
                    connection.disconnect();
            } catch (Exception exception) {
            }
        } catch (Exception e) {
            CommLog.error("HttpPost exception for: [{}?{}]", new Object[]{_strPostUrl, _strPostParam, e});

            try {
                if (br != null)
                    br.close();
            } catch (Exception exception) {
            }

            try {
                if (connection != null)
                    connection.disconnect();
            } catch (Exception exception) {
            }
        }

        return strRet;
    }

    private static String _normReturn(String strRetOrgi) {
        String strRet = strRetOrgi;
        int iStartPos = 0;
        for (int i = 0; i < strRetOrgi.length(); i++) {
            char ch = strRetOrgi.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
                break;
            }
            iStartPos++;
        }
        if (iStartPos != 0) {
            strRet = strRetOrgi.substring(iStartPos);
        }
        return strRet;
    }

    public static byte[] GetAll(String strGetUrl) {
        return GetAll(15000, 15000, strGetUrl, "");
    }

    public static byte[] GetAll(String strGetUrl, String strPostParam) {
        return GetAll(15000, 15000, strGetUrl, strPostParam);
    }

    public static byte[] GetAll(int connTimeOutMs, int readTimeOutMs, String strGetUrl, String strPostParam) {
        HttpURLConnection connection = null;
        InputStream br = null;

        try {
            String strWholeUrl;
            if (strPostParam != null && !strPostParam.trim().isEmpty()) {
                strWholeUrl = String.valueOf(strGetUrl) + "?" + strPostParam;
            } else {
                strWholeUrl = strGetUrl;
            }
            URL url = new URL(strWholeUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(connTimeOutMs);
            connection.setReadTimeout(readTimeOutMs);
            connection.setRequestMethod("GET");
            connection.connect();
            br = connection.getInputStream();

            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[512];
            int rc = 0;
            while ((rc = br.read(buff, 0, 512)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            br.close();

            br.close();
            connection.disconnect();
            return swapStream.toByteArray();
        } catch (Exception e) {
            CommLog.error(e.getMessage(), e);
            try {
                if (br != null)
                    br.close();
            } catch (Exception exception) {
            }

            try {
                if (connection != null)
                    connection.disconnect();
            } catch (Exception exception) {
            }

            return null;
        }
    }

    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {
                result = String.valueOf(result) + "\\u" + Integer.toHexString(chr1);
            } else {
                result = String.valueOf(result) + str.charAt(i);
            }
        }
        return result;
    }

    public static String decodeUnicode(String theString) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            char aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - 48;
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 97;
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 65;
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                    continue;
                }
                if (aChar == 't') {
                    aChar = '\t';
                } else if (aChar == 'r') {
                    aChar = '\r';
                } else if (aChar == 'n') {
                    aChar = '\n';
                } else if (aChar == 'f') {
                    aChar = '\f';
                }
                outBuffer.append(aChar);
                continue;
            }
            outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}

