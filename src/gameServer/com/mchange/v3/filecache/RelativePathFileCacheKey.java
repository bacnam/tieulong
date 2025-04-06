/*    */ package com.mchange.v3.filecache;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RelativePathFileCacheKey
/*    */   implements FileCacheKey
/*    */ {
/*    */   final URL url;
/*    */   final String relPath;
/*    */   
/*    */   public RelativePathFileCacheKey(URL paramURL, String paramString) throws MalformedURLException, IllegalArgumentException {
/* 48 */     String str = paramString.trim();
/*    */     
/* 50 */     if (paramURL == null || paramString == null)
/* 51 */       throw new IllegalArgumentException("parentURL [" + paramURL + "] and relative path [" + paramString + "] must be non-null"); 
/* 52 */     if (str.length() == 0)
/* 53 */       throw new IllegalArgumentException("relative path [" + paramString + "] must not be a blank string"); 
/* 54 */     if (!str.equals(paramString))
/* 55 */       throw new IllegalArgumentException("relative path [" + paramString + "] must not begin or end with whitespace."); 
/* 56 */     if (paramString.startsWith("/")) {
/* 57 */       throw new IllegalArgumentException("Path must be relative, '" + paramString + "' begins with '/'.");
/*    */     }
/* 59 */     this.url = new URL(paramURL, paramString);
/* 60 */     this.relPath = paramString;
/*    */   }
/*    */   
/*    */   public URL getURL() {
/* 64 */     return this.url;
/*    */   }
/*    */   public String getCacheFilePath() {
/* 67 */     return this.relPath;
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 71 */     if (paramObject instanceof RelativePathFileCacheKey) {
/*    */       
/* 73 */       RelativePathFileCacheKey relativePathFileCacheKey = (RelativePathFileCacheKey)paramObject;
/* 74 */       return (this.url.equals(relativePathFileCacheKey.url) && this.relPath.equals(relativePathFileCacheKey.relPath));
/*    */     } 
/*    */     
/* 77 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 81 */     return this.url.hashCode() ^ this.relPath.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/filecache/RelativePathFileCacheKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */