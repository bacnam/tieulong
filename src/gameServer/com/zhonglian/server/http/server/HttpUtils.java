/*     */ package com.zhonglian.server.http.server;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.Config;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.secure.MD5;
/*     */ import com.zhonglian.server.http.client.HttpAsyncClient;
/*     */ import com.zhonglian.server.http.client.IResponseHandler;
/*     */ import java.net.URLEncoder;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.message.BasicHeader;
/*     */ 
/*     */ 
/*     */ public class HttpUtils
/*     */ {
/*  27 */   private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*     */   
/*     */   public static final String SIGN_KEY = "svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv";
/*     */   public static final String SIGN_KEY_RECHARGE = "cYUPYnZgCiy0zUBg9KRRXM7H4GUsRKpI";
/*     */   
/*     */   public static Map<String, String> abstractHttpParams(String query) {
/*  33 */     Map<String, String> map = new TreeMap<>();
/*  34 */     if (query == null || query.trim().isEmpty()) {
/*  35 */       return map;
/*     */     }
/*  37 */     String[] arrayStr = query.split("&"); byte b; int i; String[] arrayOfString1;
/*  38 */     for (i = (arrayOfString1 = arrayStr).length, b = 0; b < i; ) { String str = arrayOfString1[b];
/*  39 */       if (str != null && !str.isEmpty()) {
/*     */ 
/*     */         
/*  42 */         int sign = str.indexOf('=');
/*  43 */         if (sign >= 0) {
/*  44 */           map.put(str.substring(0, sign), str.substring(sign + 1));
/*     */         } else {
/*  46 */           map.put(str, "");
/*     */         } 
/*     */       }  b++; }
/*  49 */      return map;
/*     */   }
/*     */   
/*     */   public static JsonObject abstractGMParams(String query) throws Exception {
/*  53 */     return abstractGMParams(query, "svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv");
/*     */   }
/*     */   
/*     */   public static JsonObject abstractGMParams(String query, String signkey) throws Exception {
/*  57 */     if (query == null) {
/*  58 */       throw new RequestException(30001, "消息为空", new Object[0]);
/*     */     }
/*  60 */     query = query.trim();
/*  61 */     if (query.isEmpty()) {
/*  62 */       throw new RequestException(30001, "消息为空", new Object[0]);
/*     */     }
/*     */     
/*  65 */     JsonObject jsonObject = null;
/*     */     try {
/*  67 */       jsonObject = (new JsonParser()).parse(query).getAsJsonObject();
/*  68 */     } catch (Exception e) {
/*  69 */       throw new RequestException(30002, "发送的post数据无法解析出相关活动参数", new Object[0]);
/*     */     } 
/*     */     
/*  72 */     TreeMap<String, JsonElement> params = new TreeMap<>();
/*  73 */     for (Map.Entry<String, JsonElement> pair : (Iterable<Map.Entry<String, JsonElement>>)jsonObject.entrySet()) {
/*  74 */       params.put(pair.getKey(), pair.getValue());
/*     */     }
/*     */     
/*  77 */     String sign = ((JsonElement)params.get("sign")).getAsString();
/*     */ 
/*     */ 
/*     */     
/*  81 */     StringBuilder signsrc = new StringBuilder();
/*     */     
/*  83 */     for (Map.Entry<String, JsonElement> sendparam : params.entrySet()) {
/*  84 */       if (((String)sendparam.getKey()).equalsIgnoreCase("sign")) {
/*     */         continue;
/*     */       }
/*  87 */       if (sendparam.getValue() == null) {
/*  88 */         throw new RequestException(30002, String.format("key=%s的值为null", new Object[] { sendparam.getKey() }), new Object[0]);
/*     */       }
/*  90 */       String value = null;
/*  91 */       if (((JsonElement)sendparam.getValue()).isJsonObject() || ((JsonElement)sendparam.getValue()).isJsonArray()) {
/*  92 */         value = ((JsonElement)sendparam.getValue()).toString();
/*  93 */         value = toUnicode(value);
/*     */       } else {
/*  95 */         value = ((JsonElement)sendparam.getValue()).getAsString();
/*     */       } 
/*     */       
/*  98 */       value = URLEncoder.encode(value, "utf-8");
/*  99 */       signsrc.append(sendparam.getKey()).append("=").append(value).append("&");
/*     */     } 
/*     */     
/* 102 */     signsrc = signsrc.append(signkey);
/* 103 */     String s = MD5.md5(signsrc.toString());
/* 104 */     if (!sign.equalsIgnoreCase(s)) {
/* 105 */       CommLog.info("原串:{},预期签名:{},传参签名:{}", new Object[] { signsrc.toString(), s, sign });
/*     */     }
/*     */     
/* 108 */     return jsonObject;
/*     */   }
/*     */   
/*     */   private static String toUnicode(String asciicode) {
/* 112 */     char[] utfBytes = asciicode.toCharArray();
/* 113 */     StringBuilder unicodeBytes = new StringBuilder();
/* 114 */     for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
/* 115 */       String hexB = Integer.toHexString(utfBytes[byteIndex]);
/* 116 */       if (hexB.length() <= 2) {
/* 117 */         unicodeBytes.append(utfBytes[byteIndex]);
/*     */       } else {
/* 119 */         unicodeBytes.append("\\u" + hexB);
/*     */       } 
/*     */     } 
/* 122 */     return unicodeBytes.toString();
/*     */   }
/*     */   
/*     */   public static int getTime(JsonObject json, String name) throws RequestException {
/*     */     try {
/* 127 */       return (int)(sdf.parse(json.get(name).getAsString()).getTime() / 1000L);
/* 128 */     } catch (Exception e) {
/* 129 */       throw new RequestException(30002, "缺少int格式参数[%s]", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getTime(JsonObject json, String name, int def) throws RequestException {
/* 134 */     JsonElement value = json.get(name);
/* 135 */     if (value == null || value.getAsString().equals("")) {
/* 136 */       return def;
/*     */     }
/*     */     try {
/* 139 */       return (int)(sdf.parse(value.getAsString()).getTime() / 1000L);
/* 140 */     } catch (Exception e) {
/* 141 */       throw new RequestException(30002, "参数[%s]无法解析出int值", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T extends Enum<T>> T getEnum(JsonObject json, String name, Class<T> class1) throws RequestException {
/*     */     try {
/* 147 */       return Enum.valueOf(class1, json.get(name).getAsString());
/* 148 */     } catch (Exception e) {
/* 149 */       throw new RequestException(30002, "缺少[%s]格式参数[%s]", new Object[] { class1.getSimpleName(), name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean getBool(JsonObject json, String name) throws RequestException {
/*     */     try {
/* 155 */       return json.get(name).getAsBoolean();
/* 156 */     } catch (Exception e) {
/* 157 */       throw new RequestException(30002, "缺少bool格式参数[%s]", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean getBool(JsonObject json, String name, boolean def) {
/*     */     try {
/* 163 */       return json.get(name).getAsBoolean();
/* 164 */     } catch (Exception e) {
/* 165 */       return def;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static JsonObject getJsonObject(JsonObject json, String name) throws RequestException {
/*     */     try {
/* 171 */       return json.get(name).getAsJsonObject();
/* 172 */     } catch (Exception e) {
/* 173 */       throw new RequestException(30002, "缺少JsonObject格式参数[%s]", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static JsonArray getJsonArray(JsonObject json, String name) throws RequestException {
/*     */     try {
/* 179 */       return json.get(name).getAsJsonArray();
/* 180 */     } catch (Exception e) {
/* 181 */       throw new RequestException(30002, "缺少JsonArray格式参数[%s]", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getInt(JsonObject json, String name) throws RequestException {
/*     */     try {
/* 187 */       return json.get(name).getAsInt();
/* 188 */     } catch (Exception e) {
/* 189 */       throw new RequestException(30002, "缺少Int格式参数[%s]", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static long getLong(JsonObject json, String name) throws RequestException {
/*     */     try {
/* 195 */       return json.get(name).getAsLong();
/* 196 */     } catch (Exception e) {
/* 197 */       throw new RequestException(30002, "缺少Long格式参数[%s]", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getString(JsonObject json, String name) throws RequestException {
/*     */     try {
/* 203 */       return json.get(name).getAsString();
/* 204 */     } catch (Exception e) {
/* 205 */       throw new RequestException(30002, "缺少string格式参数[%s]", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getString(JsonObject json, String name, String def) throws RequestException {
/* 210 */     JsonElement value = json.get(name);
/* 211 */     if (value == null) {
/* 212 */       return def;
/*     */     }
/*     */     try {
/* 215 */       return value.getAsString();
/* 216 */     } catch (Exception e) {
/* 217 */       throw new RequestException(30002, "参数[%s]无法解析出string值", new Object[] { name });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<Integer> getIntList(JsonObject json, String name) throws RequestException {
/* 222 */     List<Integer> list = Lists.newArrayList();
/*     */     try {
/* 224 */       JsonArray jsonArray = json.get(name).getAsJsonArray();
/* 225 */       if (jsonArray.size() > 0) {
/* 226 */         jsonArray.forEach(x -> paramList.add(Integer.valueOf(x.getAsInt())));
/*     */       
/*     */       }
/*     */     }
/* 230 */     catch (Exception e) {
/* 231 */       throw new RequestException(30002, "缺少Integer格式参数[%s]", new Object[] { name });
/*     */     } 
/* 233 */     return list;
/*     */   }
/*     */   
/*     */   public static List<Long> getLongList(JsonObject json, String name) throws RequestException {
/* 237 */     List<Long> list = Lists.newArrayList();
/*     */     try {
/* 239 */       JsonArray jsonArray = json.get(name).getAsJsonArray();
/* 240 */       if (jsonArray.size() > 0) {
/* 241 */         jsonArray.forEach(x -> paramList.add(Long.valueOf(x.getAsLong())));
/*     */       
/*     */       }
/*     */     }
/* 245 */     catch (Exception e) {
/* 246 */       throw new RequestException(30002, "缺少Long数组格式参数[%s]", new Object[] { name });
/*     */     } 
/* 248 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void NotifyGM(final String url, GMParam param) {
/* 258 */     RequestGM(url, param, new IResponseHandler()
/*     */         {
/*     */           public void compeleted(String response) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void failed(Exception exception) {
/* 266 */             CommLog.error("GM通知[{}] 回包失败", url, exception);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void RequestGM(String url, GMParam params, IResponseHandler response) {
/*     */     try {
/* 280 */       int server_id = 0;
/* 281 */       if (params.get("server_id") != null) {
/*     */         try {
/* 283 */           server_id = Integer.parseInt(params.get("server_id").toString());
/* 284 */         } catch (Exception e) {
/* 285 */           CommLog.error("请求GM发生异常");
/*     */         } 
/*     */       }
/* 288 */       params.put("gameid", Integer.valueOf(Config.GameID()));
/* 289 */       params.put("platform", Config.getPlatform());
/* 290 */       params.put("server_id", Integer.valueOf(Config.ServerID()));
/* 291 */       if (server_id != 0) {
/* 292 */         params.put("server_id", Integer.valueOf(server_id));
/*     */       }
/* 294 */       params.put("world_id", Integer.getInteger("world_sid", 0));
/* 295 */       HttpAsyncClient.startHttpGet(String.valueOf(url) + params.toUrlParam(), response);
/* 296 */     } catch (Exception e) {
/* 297 */       CommLog.error("GM请求[{}] 发送失败", url, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void ZyDataCollect(final String url, GMParam param) {
/* 308 */     JsonObject json = new JsonObject();
/* 309 */     param.forEach((key, value) -> paramJsonObject.addProperty(key, value.toString()));
/*     */ 
/*     */     
/* 312 */     StringEntity stringEntity = null;
/*     */     try {
/* 314 */       stringEntity = new StringEntity(json.toString(), "UTF-8");
/* 315 */       stringEntity.setContentEncoding((Header)new BasicHeader("Content-Type", "application/json"));
/* 316 */     } catch (Exception e) {
/* 317 */       CommLog.error("请求[{}]封装post参数失败:{}", url, e.toString());
/*     */     } 
/* 319 */     if (stringEntity == null) {
/*     */       return;
/*     */     }
/* 322 */     HttpPost httpPost = new HttpPost(url);
/* 323 */     httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
/* 324 */     httpPost.setEntity((HttpEntity)stringEntity);
/*     */     
/* 326 */     HttpAsyncClient.startHttpPost(httpPost, new IResponseHandler()
/*     */         {
/*     */           public void compeleted(String response)
/*     */           {
/* 330 */             CommLog.warn("追踪地址[{}]通知回包成功：{}", url, response);
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception exception) {
/* 335 */             CommLog.warn("追踪地址[{}]通知回包失败：{}", url, exception.toString());
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/HttpUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */