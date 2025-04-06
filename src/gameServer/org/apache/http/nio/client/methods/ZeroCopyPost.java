/*    */ package org.apache.http.nio.client.methods;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.client.methods.HttpPost;
/*    */ import org.apache.http.entity.ContentType;
/*    */ import org.apache.http.nio.ContentEncoder;
/*    */ import org.apache.http.nio.IOControl;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZeroCopyPost
/*    */   extends BaseZeroCopyRequestProducer
/*    */ {
/*    */   public ZeroCopyPost(URI requestURI, File content, ContentType contentType) throws FileNotFoundException {
/* 52 */     super(requestURI, content, contentType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZeroCopyPost(String requestURI, File content, ContentType contentType) throws FileNotFoundException {
/* 59 */     super(URI.create(requestURI), content, contentType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpEntityEnclosingRequest createRequest(URI requestURI, HttpEntity entity) {
/* 64 */     HttpPost httppost = new HttpPost(requestURI);
/* 65 */     httppost.setEntity(entity);
/* 66 */     return (HttpEntityEnclosingRequest)httppost;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/client/methods/ZeroCopyPost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */