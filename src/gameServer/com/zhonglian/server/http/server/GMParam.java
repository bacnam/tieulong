/*    */ package com.zhonglian.server.http.server;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.utils.secure.MD5;
/*    */ import java.net.URLEncoder;
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.entity.StringEntity;
/*    */ 
/*    */ 
/*    */ public class GMParam
/*    */   extends TreeMap<String, Object>
/*    */ {
/*    */   private static final long serialVersionUID = 4770492078712306761L;
/* 16 */   private String key = null;
/*    */   
/*    */   public GMParam() {
/* 19 */     this.key = "svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv";
/*    */   }
/*    */   
/*    */   public GMParam(String key) {
/* 23 */     this.key = key;
/*    */   }
/*    */   
/*    */   public HttpEntity toEntity() throws Exception {
/* 27 */     JsonObject json = new JsonObject();
/* 28 */     StringBuilder signsrc = new StringBuilder();
/* 29 */     for (Map.Entry<String, Object> pair : entrySet()) {
/* 30 */       String value = pair.getValue().toString();
/* 31 */       json.addProperty(pair.getKey(), value);
/* 32 */       value = URLEncoder.encode(value, "utf-8");
/* 33 */       signsrc.append(pair.getKey()).append("=").append(value).append("&");
/*    */     } 
/* 35 */     signsrc = signsrc.append(this.key);
/* 36 */     json.addProperty("sign", MD5.md5(signsrc.toString()));
/* 37 */     return (HttpEntity)new StringEntity(json.toString());
/*    */   }
/*    */   
/*    */   public String toUrlParam() throws Exception {
/* 41 */     StringBuilder params = new StringBuilder("?");
/* 42 */     StringBuilder signsrc = new StringBuilder();
/* 43 */     for (Map.Entry<String, Object> pair : entrySet()) {
/* 44 */       String value = pair.getValue().toString();
/* 45 */       value = URLEncoder.encode(value, "utf-8");
/* 46 */       signsrc.append(pair.getKey()).append("=").append(value).append("&");
/* 47 */       params.append(pair.getKey()).append("=").append(value).append("&");
/*    */     } 
/* 49 */     signsrc = signsrc.append(this.key);
/* 50 */     params.append("sign").append("=").append(MD5.md5(signsrc.toString()));
/* 51 */     return params.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/GMParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */