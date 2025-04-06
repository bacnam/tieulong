/*    */ package com.zhonglian.server.http.client;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.concurrent.FutureCallback;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class IResponseHandler
/*    */   implements FutureCallback<HttpResponse>
/*    */ {
/*    */   public class CancelledException
/*    */     extends Exception
/*    */   {
/*    */     private static final long serialVersionUID = -421378063733917547L;
/*    */   }
/*    */   
/*    */   public void completed(HttpResponse httpResponse) {
/*    */     try {
/* 29 */       BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
/* 30 */       StringBuilder sb = new StringBuilder();
/* 31 */       String line = null;
/* 32 */       while ((line = reader.readLine()) != null) {
/* 33 */         sb.append(line);
/*    */       }
/*    */       
/* 36 */       SyncTaskManager.task(() -> compeleted(paramStringBuilder.toString()));
/* 37 */     } catch (Exception e) {
/* 38 */       CommLog.error("read httpresponse EntityString error : ", e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void cancelled() {
/* 47 */     SyncTaskManager.task(() -> failed(new CancelledException()));
/*    */   }
/*    */   
/*    */   public abstract void compeleted(String paramString);
/*    */   
/*    */   public abstract void failed(Exception paramException);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/client/IResponseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */