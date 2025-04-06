/*     */ package core.logger.flow;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import core.server.ServerConfig;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import sun.net.www.protocol.http.HttpURLConnection;
/*     */ 
/*     */ public class TDRechargeLogger
/*     */ {
/*  20 */   private static TDRechargeLogger instance = new TDRechargeLogger();
/*     */   
/*     */   public static TDRechargeLogger getInstance() {
/*  23 */     return instance;
/*     */   }
/*     */   
/*  26 */   private String appID = System.getProperty("TDRecharge.appID");
/*     */ 
/*     */   
/*     */   public void sendRechargeLog(String msgID, String status, String OS, String accountID, String orderID, double currencyAmount, String currencyType, double virtualCurrencyAmount, long chargeTime, String iapID, int level) {
/*     */     try {
/*  31 */       JsonArray arry = new JsonArray();
/*  32 */       JsonObject param = new JsonObject();
/*  33 */       param.addProperty("msgID", msgID);
/*  34 */       param.addProperty("status", status);
/*  35 */       param.addProperty("OS", OS);
/*  36 */       param.addProperty("accountID", accountID);
/*  37 */       param.addProperty("orderID", orderID);
/*  38 */       param.addProperty("currencyAmount", Double.valueOf(currencyAmount));
/*  39 */       param.addProperty("currencyType", currencyType);
/*  40 */       param.addProperty("virtualCurrencyAmount", Double.valueOf(virtualCurrencyAmount));
/*  41 */       param.addProperty("chargeTime", Long.valueOf(chargeTime));
/*  42 */       param.addProperty("iapID", iapID);
/*  43 */       param.addProperty("gameServer", makeSign());
/*  44 */       param.addProperty("level", Integer.valueOf(level));
/*  45 */       arry.add((JsonElement)param);
/*  46 */       byte[] dataByte = gzip(arry.toString());
/*  47 */       HttpClient clinet = new HttpClient("api.talkinggame.com", "80", "/api/charge/" + this.appID);
/*  48 */       CommLog.warn("result data : " + clinet.doPost(dataByte));
/*     */     }
/*  50 */     catch (Exception e) {
/*  51 */       CommLog.warn("TD充值日志发送失败:{}", e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeSign() {
/*  57 */     String world_id = System.getProperty("world_sid", "0");
/*  58 */     String server_id = (new StringBuilder(String.valueOf(ServerConfig.ServerID()))).toString();
/*  59 */     if (world_id.equals("3001")) {
/*  60 */       server_id = "T" + ServerConfig.ServerID();
/*     */     }
/*  62 */     if (world_id.equals("60001")) {
/*  63 */       server_id = "Y" + ServerConfig.ServerID();
/*     */     }
/*  65 */     if (world_id.equals("40001")) {
/*  66 */       server_id = "Z" + ServerConfig.ServerID();
/*     */     }
/*  68 */     if (world_id.equals("10001")) {
/*  69 */       server_id = "A" + ServerConfig.ServerID();
/*     */     }
/*  71 */     if (world_id.equals("30001")) {
/*  72 */       server_id = "F" + ServerConfig.ServerID();
/*     */     }
/*  74 */     return server_id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] gzip(String content) {
/*  83 */     ByteArrayOutputStream baos = null;
/*  84 */     GZIPOutputStream out = null;
/*  85 */     byte[] ret = null;
/*     */     try {
/*  87 */       baos = new ByteArrayOutputStream();
/*  88 */       out = new GZIPOutputStream(baos);
/*  89 */       out.write(content.getBytes());
/*  90 */       out.close();
/*  91 */       baos.close();
/*  92 */       ret = baos.toByteArray();
/*  93 */     } catch (FileNotFoundException e) {
/*  94 */       e.printStackTrace();
/*  95 */     } catch (IOException e) {
/*  96 */       e.printStackTrace();
/*     */     } finally {
/*  98 */       if (out != null) {
/*     */         try {
/* 100 */           baos.close();
/* 101 */         } catch (IOException e) {
/* 102 */           e.printStackTrace();
/*     */         } 
/*     */       }
/* 105 */       if (out != null) {
/*     */         try {
/* 107 */           out.close();
/* 108 */         } catch (IOException e) {
/* 109 */           e.printStackTrace();
/*     */         } 
/*     */       }
/*     */     } 
/* 113 */     return ret;
/*     */   }
/*     */   
/*     */   public static class HttpClient {
/* 117 */     HttpURLConnection _HttpURLConnection = null;
/* 118 */     URL url = null;
/* 119 */     private String DEFAULT_PROTOCOL = "http";
/* 120 */     private String SLASH = "/";
/* 121 */     private String COLON = ":";
/* 122 */     public String DEFAULT_NET_ERROR = "NetError";
/* 123 */     public String POST = "POST";
/*     */     
/*     */     public String doPost(byte[] Message) {
/* 126 */       String result = "";
/*     */       try {
/* 128 */         this._HttpURLConnection = (HttpURLConnection)this.url.openConnection();
/* 129 */         this._HttpURLConnection.setRequestMethod(this.POST);
/* 130 */         this._HttpURLConnection.setDoOutput(true);
/* 131 */         this._HttpURLConnection.setRequestProperty("Content-Type", "application/msgpack");
/* 132 */         this._HttpURLConnection.setRequestProperty("Content-Length", String.valueOf(Message.length));
/* 133 */         DataOutputStream ds = new DataOutputStream(this._HttpURLConnection.getOutputStream());
/* 134 */         ds.write(Message);
/* 135 */         ds.flush();
/* 136 */         ds.close();
/* 137 */         result = _gzipStream2Str(this._HttpURLConnection.getInputStream());
/* 138 */         this._HttpURLConnection.disconnect();
/* 139 */       } catch (Exception e) {
/* 140 */         this._HttpURLConnection.disconnect();
/* 141 */         e.printStackTrace();
/*     */       } 
/* 143 */       return result;
/*     */     }
/*     */     
/*     */     private String _gzipStream2Str(InputStream inputStream) throws IOException {
/* 147 */       GZIPInputStream gzipinputStream = new GZIPInputStream(inputStream);
/* 148 */       byte[] buf = new byte[1024];
/* 149 */       int num = -1;
/* 150 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 151 */       while ((num = gzipinputStream.read(buf, 0, buf.length)) != -1) {
/* 152 */         baos.write(buf, 0, num);
/*     */       }
/* 154 */       return new String(baos.toByteArray(), "utf-8");
/*     */     }
/*     */     
/*     */     public HttpClient(String ServerName, String ServerPort, String QuestPath) {
/*     */       try {
/* 159 */         String ServerURL = "";
/* 160 */         ServerURL = String.valueOf(ServerURL) + this.DEFAULT_PROTOCOL;
/* 161 */         ServerURL = String.valueOf(ServerURL) + this.COLON;
/* 162 */         ServerURL = String.valueOf(ServerURL) + this.SLASH;
/* 163 */         ServerURL = String.valueOf(ServerURL) + this.SLASH;
/* 164 */         ServerURL = String.valueOf(ServerURL) + ServerName;
/* 165 */         if (ServerPort != null && ServerPort.trim().length() > 0) {
/* 166 */           ServerURL = String.valueOf(ServerURL) + this.COLON;
/* 167 */           ServerURL = String.valueOf(ServerURL) + ServerPort.trim();
/*     */         } 
/* 169 */         if (QuestPath.charAt(0) != '/') {
/* 170 */           ServerURL = String.valueOf(ServerURL) + this.SLASH;
/*     */         }
/* 172 */         ServerURL = String.valueOf(ServerURL) + QuestPath;
/* 173 */         this.url = new URL(ServerURL);
/* 174 */       } catch (Exception e) {
/* 175 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/TDRechargeLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */