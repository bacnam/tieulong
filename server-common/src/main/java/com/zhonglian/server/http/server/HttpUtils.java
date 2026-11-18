package com.zhonglian.server.http.server;

import BaseCommon.CommLog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.secure.MD5;
import com.zhonglian.server.http.client.HttpAsyncClient;
import com.zhonglian.server.http.client.IResponseHandler;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

public class HttpUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String SIGN_KEY = "svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv";
    public static final String SIGN_KEY_RECHARGE = "cYUPYnZgCiy0zUBg9KRRXM7H4GUsRKpI";

    public static Map<String, String> abstractHttpParams(String query) {
        Map<String, String> map = new TreeMap<>();
        if (query != null && !query.trim().isEmpty()) {
            String[] arrayStr = query.split("&");

            for (String str : arrayStr) {
                if (str != null && !str.isEmpty()) {
                    int sign = str.indexOf(61);
                    if (sign >= 0) {
                        map.put(str.substring(0, sign), str.substring(sign + 1));
                    } else {
                        map.put(str, "");
                    }
                }
            }

            return map;
        } else {
            return map;
        }
    }

    public static JsonObject abstractGMParams(String query) throws Exception {
        return abstractGMParams(query, "svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv");
    }

    public static JsonObject abstractGMParams(String query, String signkey) throws Exception {
        if (query == null) {
            throw new RequestException(30001, "消息为空");
        } else {
            query = query.trim();
            if (query.isEmpty()) {
                throw new RequestException(30001, "消息为空");
            } else {
                JsonObject jsonObject = null;

                try {
                    jsonObject = new JsonParser().parse(query).getAsJsonObject();
                } catch (Exception var9) {
                    throw new RequestException(30002, "发送的post数据无法解析出相关活动参数");
                }

                TreeMap<String, JsonElement> params = new TreeMap<>();

                for (Entry<String, JsonElement> pair : jsonObject.entrySet()) {
                    params.put(pair.getKey(), pair.getValue());
                }

                String sign = params.get("sign").getAsString();
                StringBuilder signsrc = new StringBuilder();

                for (Entry<String, JsonElement> sendparam : params.entrySet()) {
                    if (!sendparam.getKey().equalsIgnoreCase("sign")) {
                        if (sendparam.getValue() == null) {
                            throw new RequestException(30002, String.format("key=%s的值为null", sendparam.getKey()));
                        }

                        String value = null;
                        if (!sendparam.getValue().isJsonObject() && !sendparam.getValue().isJsonArray()) {
                            value = sendparam.getValue().getAsString();
                        } else {
                            value = sendparam.getValue().toString();
                            value = toUnicode(value);
                        }

                        value = URLEncoder.encode(value, "utf-8");
                        signsrc.append(sendparam.getKey()).append("=").append(value).append("&");
                    }
                }

                signsrc = signsrc.append(signkey);
                String s = MD5.md5(signsrc.toString());
                if (!sign.equalsIgnoreCase(s)) {
                    CommLog.info("原串:{},预期签名:{},传参签名:{}", signsrc.toString(), s, sign);
                }

                return jsonObject;
            }
        }
    }

    private static String toUnicode(String asciicode) {
        char[] utfBytes = asciicode.toCharArray();
        StringBuilder unicodeBytes = new StringBuilder();

        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                unicodeBytes.append(utfBytes[byteIndex]);
            } else {
                unicodeBytes.append("\\u" + hexB);
            }
        }

        return unicodeBytes.toString();
    }

    public static int getTime(JsonObject json, String name) throws RequestException {
        try {
            return (int)(sdf.parse(json.get(name).getAsString()).getTime() / 1000L);
        } catch (Exception var3) {
            throw new RequestException(30002, "缺少int格式参数[%s]", name);
        }
    }

    public static int getTime(JsonObject json, String name, int def) throws RequestException {
        JsonElement value = json.get(name);
        if (value != null && !value.getAsString().equals("")) {
            try {
                return (int)(sdf.parse(value.getAsString()).getTime() / 1000L);
            } catch (Exception var5) {
                throw new RequestException(30002, "参数[%s]无法解析出int值", name);
            }
        } else {
            return def;
        }
    }

    public static <T extends Enum<T>> T getEnum(JsonObject json, String name, Class<T> class1) throws RequestException {
        try {
            return Enum.valueOf(class1, json.get(name).getAsString());
        } catch (Exception var4) {
            throw new RequestException(30002, "缺少[%s]格式参数[%s]", class1.getSimpleName(), name);
        }
    }

    public static boolean getBool(JsonObject json, String name) throws RequestException {
        try {
            return json.get(name).getAsBoolean();
        } catch (Exception var3) {
            throw new RequestException(30002, "缺少bool格式参数[%s]", name);
        }
    }

    public static boolean getBool(JsonObject json, String name, boolean def) {
        try {
            return json.get(name).getAsBoolean();
        } catch (Exception var4) {
            return def;
        }
    }

    public static JsonObject getJsonObject(JsonObject json, String name) throws RequestException {
        try {
            return json.get(name).getAsJsonObject();
        } catch (Exception var3) {
            throw new RequestException(30002, "缺少JsonObject格式参数[%s]", name);
        }
    }

    public static JsonArray getJsonArray(JsonObject json, String name) throws RequestException {
        try {
            return json.get(name).getAsJsonArray();
        } catch (Exception var3) {
            throw new RequestException(30002, "缺少JsonArray格式参数[%s]", name);
        }
    }

    public static int getInt(JsonObject json, String name) throws RequestException {
        try {
            return json.get(name).getAsInt();
        } catch (Exception var3) {
            throw new RequestException(30002, "缺少Int格式参数[%s]", name);
        }
    }

    public static long getLong(JsonObject json, String name) throws RequestException {
        try {
            return json.get(name).getAsLong();
        } catch (Exception var3) {
            throw new RequestException(30002, "缺少Long格式参数[%s]", name);
        }
    }

    public static String getString(JsonObject json, String name) throws RequestException {
        try {
            return json.get(name).getAsString();
        } catch (Exception var3) {
            throw new RequestException(30002, "缺少string格式参数[%s]", name);
        }
    }

    public static String getString(JsonObject json, String name, String def) throws RequestException {
        JsonElement value = json.get(name);
        if (value == null) {
            return def;
        } else {
            try {
                return value.getAsString();
            } catch (Exception var5) {
                throw new RequestException(30002, "参数[%s]无法解析出string值", name);
            }
        }
    }

    public static List<Integer> getIntList(JsonObject json, String name) throws RequestException {
        List<Integer> list = Lists.newArrayList();

        try {
            JsonArray jsonArray = json.get(name).getAsJsonArray();
            if (jsonArray.size() > 0) {
                jsonArray.forEach(x -> list.add(x.getAsInt()));
            }

            return list;
        } catch (Exception var4) {
            throw new RequestException(30002, "缺少Integer格式参数[%s]", name);
        }
    }

    public static List<Long> getLongList(JsonObject json, String name) throws RequestException {
        List<Long> list = Lists.newArrayList();

        try {
            JsonArray jsonArray = json.get(name).getAsJsonArray();
            if (jsonArray.size() > 0) {
                jsonArray.forEach(x -> list.add(x.getAsLong()));
            }

            return list;
        } catch (Exception var4) {
            throw new RequestException(30002, "缺少Long数组格式参数[%s]", name);
        }
    }

    public static void NotifyGM(final String url, GMParam param) {
        RequestGM(url, param, new IResponseHandler() {
            @Override
            public void compeleted(String response) {
            }

            @Override
            public void failed(Exception exception) {
                CommLog.error("GM通知[{}] 回包失败", url, exception);
            }
        });
    }

    public static void RequestGM(String url, GMParam params, IResponseHandler response) {
        try {
            int server_id = 0;
            if (params.get("server_id") != null) {
                try {
                    server_id = Integer.parseInt(params.get("server_id").toString());
                } catch (Exception var5) {
                    CommLog.error("请求GM发生异常");
                }
            }

            params.put("gameid", Config.GameID());
            params.put("platform", Config.getPlatform());
            params.put("server_id", Config.ServerID());
            if (server_id != 0) {
                params.put("server_id", server_id);
            }

            params.put("world_id", Integer.getInteger("world_sid", 0));
            HttpAsyncClient.startHttpGet(url + params.toUrlParam(), response);
        } catch (Exception var6) {
            CommLog.error("GM请求[{}] 发送失败", url, var6);
        }
    }

    public static void ZyDataCollect(final String url, GMParam param) {
        JsonObject json = new JsonObject();
        param.forEach((key, value) -> json.addProperty(key, value.toString()));
        StringEntity stringEntity = null;

        try {
            stringEntity = new StringEntity(json.toString(), "UTF-8");
            stringEntity.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
        } catch (Exception var5) {
            CommLog.error("请求[{}]封装post参数失败:{}", url, var5.toString());
        }

        if (stringEntity != null) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            httpPost.setEntity(stringEntity);
            HttpAsyncClient.startHttpPost(httpPost, new IResponseHandler() {
                @Override
                public void compeleted(String response) {
                    CommLog.warn("追踪地址[{}]通知回包成功：{}", url, response);
                }

                @Override
                public void failed(Exception exception) {
                    CommLog.warn("追踪地址[{}]通知回包失败：{}", url, exception.toString());
                }
            });
        }
    }
}
