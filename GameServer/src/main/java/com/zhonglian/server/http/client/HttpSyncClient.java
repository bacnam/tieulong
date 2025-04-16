package com.zhonglian.server.http.client;

import BaseCommon.CommLog;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class HttpSyncClient {
    public static String sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;

        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);

            // Open connection
            URLConnection connection = realUrl.openConnection();

            // Set request headers
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            // Connect and read
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            CommLog.error("发送GET请求 [{}] 出现异常！", url, e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ignore) {}
        }

        return result.toString();
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();

        try {
            URL realUrl = new URL(url);

            // Mở kết nối
            URLConnection conn = realUrl.openConnection();

            // Thiết lập các thuộc tính request header
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            // Cho phép gửi dữ liệu POST
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Gửi dữ liệu
            out = new PrintWriter(conn.getOutputStream());
            out.print(param); // ví dụ: "name=value&age=18"
            out.flush();

            // Đọc dữ liệu phản hồi
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            CommLog.error("发送 POST请求 [{}] 出现异常！", url, e);
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ignore) {}
        }

        return result.toString();
    }

    public static String sendPost(String url, Map<String, Object> param) {
        StringBuilder sBuilder = new StringBuilder();
        for (Map.Entry<String, Object> pair : param.entrySet()) {
            try {
                sBuilder.append(pair.getKey()).append('=').append(URLEncoder.encode(pair.getValue().toString(), "utf-8")).append('&');
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
            }
        }

        if (sBuilder.length() > 0) {
            sBuilder.deleteCharAt(sBuilder.length() - 1);
        }
        return sendPost(url, sBuilder.toString());
    }
}

