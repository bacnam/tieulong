/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.entity.DecompressingEntity;
/*     */ import org.apache.http.client.entity.DeflateInputStream;
/*     */ import org.apache.http.client.entity.InputStreamFactory;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Immutable
/*     */ public class ResponseContentEncoding
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   public static final String UNCOMPRESSED = "http.client.response.uncompressed";
/*     */   
/*  63 */   private static final InputStreamFactory GZIP = new InputStreamFactory()
/*     */     {
/*     */       public InputStream create(InputStream instream) throws IOException
/*     */       {
/*  67 */         return new GZIPInputStream(instream);
/*     */       }
/*     */     };
/*     */   
/*  71 */   private static final InputStreamFactory DEFLATE = new InputStreamFactory()
/*     */     {
/*     */       public InputStream create(InputStream instream) throws IOException
/*     */       {
/*  75 */         return (InputStream)new DeflateInputStream(instream);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final Lookup<InputStreamFactory> decoderRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseContentEncoding(Lookup<InputStreamFactory> decoderRegistry) {
/*  86 */     this.decoderRegistry = (decoderRegistry != null) ? decoderRegistry : (Lookup<InputStreamFactory>)RegistryBuilder.create().register("gzip", GZIP).register("x-gzip", GZIP).register("deflate", DEFLATE).build();
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
/*     */   
/*     */   public ResponseContentEncoding() {
/* 103 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 110 */     HttpEntity entity = response.getEntity();
/*     */     
/* 112 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 113 */     RequestConfig requestConfig = clientContext.getRequestConfig();
/*     */ 
/*     */     
/* 116 */     if (requestConfig.isDecompressionEnabled() && entity != null && entity.getContentLength() != 0L) {
/* 117 */       Header ceheader = entity.getContentEncoding();
/* 118 */       if (ceheader != null) {
/* 119 */         HeaderElement[] codecs = ceheader.getElements();
/* 120 */         for (HeaderElement codec : codecs) {
/* 121 */           String codecname = codec.getName().toLowerCase(Locale.ROOT);
/* 122 */           InputStreamFactory decoderFactory = (InputStreamFactory)this.decoderRegistry.lookup(codecname);
/* 123 */           if (decoderFactory != null) {
/* 124 */             response.setEntity((HttpEntity)new DecompressingEntity(response.getEntity(), decoderFactory));
/* 125 */             response.removeHeaders("Content-Length");
/* 126 */             response.removeHeaders("Content-Encoding");
/* 127 */             response.removeHeaders("Content-MD5");
/*     */           }
/* 129 */           else if (!"identity".equals(codecname)) {
/* 130 */             throw new HttpException("Unsupported Content-Coding: " + codec.getName());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/protocol/ResponseContentEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */