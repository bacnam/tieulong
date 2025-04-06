/*     */ package com.zhonglian.server.common;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseServer.BaseServerInit;
/*     */ import bsh.EvalError;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.utils.CommFile;
/*     */ import com.zhonglian.server.common.utils.CommProperties;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.http.client.HttpUtil;
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IApp
/*     */ {
/*     */   public void init(String[] args) throws EvalError {
/*  33 */     String configfile = "conf";
/*  34 */     System.out.println("启动参数:" + Arrays.toString((Object[])args));
/*  35 */     if (args != null && args.length > 0) {
/*  36 */       configfile = args[0];
/*     */     }
/*     */ 
/*     */     
/*  40 */     beforeInit(configfile);
/*     */ 
/*     */     
/*  43 */     outputVersion();
/*     */ 
/*     */     
/*  46 */     BaseServerInit.initBaseServer();
/*  47 */     if (!initBase()) {
/*  48 */       CommLog.error("初始化基础服务失败, 退出服务器启动");
/*  49 */       System.exit(-1);
/*     */     } 
/*  51 */     if (!initLogic()) {
/*  52 */       CommLog.error("初始化逻辑组建失败, 退出服务器启动");
/*  53 */       System.exit(-1);
/*     */     } 
/*  55 */     afterInit();
/*     */     
/*  57 */     if (!startNet()) {
/*  58 */       CommLog.error("启动网络通信失败，退出服务器启动");
/*  59 */       System.exit(-1);
/*     */     } 
/*  61 */     CommLog.info("[服务器启动完毕]!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void beforeInit(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean initBase();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean startNet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean initLogic();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void afterInit();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void start();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadBasicConfig(String configdir, String logback) {
/* 106 */     String firstdir = String.valueOf(configdir) + "/first.properties";
/* 107 */     if (!CommProperties.load(firstdir, false)) {
/* 108 */       System.err.println("first配置文件[" + firstdir + "]读取失败，服务器退出!");
/* 109 */       System.exit(-1);
/*     */     } 
/*     */ 
/*     */     
/* 113 */     String logbackdir = String.valueOf(configdir) + File.separator + logback;
/* 114 */     System.setProperty("logback.configurationFile", logbackdir);
/* 115 */     System.setProperty("logback.configurationFile_bak", logbackdir);
/* 116 */     CommLog.initLog();
/*     */     
/* 118 */     Config.setStartTime(CommTime.nowSecond());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadRemoteConfig(String serverType, String serverId, String location, String configfile) {
/* 134 */     String url = System.getProperty("downConfUrl");
/* 135 */     if (url != null && !url.trim().isEmpty()) {
/* 136 */       url = "http://" + url + "/gm/gmNotice!loadConfig?" + serverType + "=" + serverId + "&" + "world_id" + "=" + Integer.getInteger("world_sid", 0);
/* 137 */       CommLog.info("从远端[{}]下载配置", url);
/*     */       try {
/* 139 */         JsonObject root = (new JsonParser()).parse(new String(HttpUtil.GetAll(url))).getAsJsonObject();
/* 140 */         String template = CommFile.bufferedReader(String.valueOf(location) + "/template/" + configfile);
/* 141 */         for (Map.Entry<String, String> properties : toMapValues(root).entrySet()) {
/* 142 */           template = template.replaceAll("\\{" + (String)properties.getKey() + "\\}", properties.getValue());
/*     */         }
/* 144 */         CommFile.Write(String.valueOf(location) + "/" + configfile, template);
/* 145 */         CommLog.info("download and write config ok");
/* 146 */       } catch (Exception e) {
/* 147 */         CommLog.error("写入下载来的配置文件失败 ,退出程序,url：[{}],文件:[{}] ", new Object[] { url, configfile, e });
/* 148 */         System.exit(-1);
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     CommProperties.load(String.valueOf(location) + "/" + configfile);
/*     */   }
/*     */   
/*     */   public Map<String, String> toMapValues(JsonObject root) {
/* 156 */     Map<String, String> map = new HashMap<>();
/* 157 */     for (Map.Entry<String, JsonElement> element : (Iterable<Map.Entry<String, JsonElement>>)root.entrySet()) {
/* 158 */       String name = element.getKey();
/* 159 */       JsonObject jsonObject = ((JsonElement)element.getValue()).getAsJsonObject();
/* 160 */       for (Map.Entry<String, JsonElement> property : (Iterable<Map.Entry<String, JsonElement>>)jsonObject.entrySet()) {
/* 161 */         JsonElement value = property.getValue();
/* 162 */         if (value != null && !value.isJsonNull()) {
/* 163 */           map.put(String.valueOf(name) + "\\." + (String)property.getKey(), value.getAsString());
/*     */         }
/*     */       } 
/*     */     } 
/* 167 */     return map;
/*     */   }
/*     */   
/*     */   private void outputVersion() {
/* 171 */     String version = outputVersion("kernel", BaseServerInit.class);
/* 172 */     if (!version.equals(outputVersion("Common", IApp.class))) {
/* 173 */       CommLog.error("BaseServer的包和Common的Jar包不一致! 运维注意拷贝Server的jar包的时候要拷贝Lib包的内容");
/* 174 */       System.exit(-1);
/*     */     } 
/* 176 */     if (!version.equals(outputVersion("Server", getClass()))) {
/* 177 */       CommLog.error("BaseServer的包和Server的Jar包不一致! 运维注意拷贝Server的jar包的时候要拷贝Lib包的内容");
/* 178 */       System.exit(-1);
/*     */     } 
/* 180 */     CommLog.info("----------------version.txt--------------------"); byte b; int i; String[] arrayOfString;
/* 181 */     for (i = (arrayOfString = version.split("\n")).length, b = 0; b < i; ) { String line = arrayOfString[b];
/* 182 */       CommLog.info(line); b++; }
/*     */     
/* 184 */     CommLog.info("-----------------------------------------------");
/*     */   }
/*     */   
/*     */   private String outputVersion(String jarPackage, Class<?> rootclass) {
/* 188 */     StringBuilder stringBuilder = new StringBuilder(); try {
/* 189 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 198 */     catch (Exception e) {
/* 199 */       CommLog.error("IApp.outputVersion", e);
/*     */     } 
/* 201 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/IApp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */