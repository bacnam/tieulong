/*     */ package com.mchange.v3.filecache;
/*     */ 
/*     */ import com.mchange.v1.io.InputStreamUtils;
/*     */ import com.mchange.v1.io.OutputStreamUtils;
/*     */ import com.mchange.v2.io.DirectoryDescentUtils;
/*     */ import com.mchange.v2.io.FileIterator;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public final class FileCache
/*     */ {
/*  50 */   static final MLogger logger = MLog.getLogger(FileCache.class);
/*     */   
/*     */   final File cacheDir;
/*     */   
/*     */   final int buffer_size;
/*     */   
/*     */   final boolean read_only;
/*     */   final List<URLFetcher> fetchers;
/*     */   
/*     */   private InputStream fetchURL(URL paramURL) throws IOException {
/*  60 */     LinkedList<IOException> linkedList = null;
/*  61 */     for (URLFetcher uRLFetcher : this.fetchers) {
/*     */       try {
/*  63 */         return uRLFetcher.openStream(paramURL, logger);
/*  64 */       } catch (FileNotFoundException fileNotFoundException) {
/*  65 */         throw fileNotFoundException;
/*  66 */       } catch (IOException iOException) {
/*     */         
/*  68 */         if (logger.isLoggable(MLevel.FINE))
/*  69 */           logger.log(MLevel.FINE, "URLFetcher " + uRLFetcher + " failed on Exception. Will try next fetcher, if any.", iOException); 
/*  70 */         if (linkedList == null)
/*  71 */           linkedList = new LinkedList(); 
/*  72 */         linkedList.add(iOException);
/*     */       } 
/*     */     } 
/*  75 */     if (logger.isLoggable(MLevel.WARNING)) {
/*     */       
/*  77 */       logger.log(MLevel.WARNING, "All URLFetchers failed on URL " + paramURL); byte b; int i;
/*  78 */       for (b = 0, i = linkedList.size(); b < i; b++) {
/*  79 */         logger.log(MLevel.WARNING, "URLFetcher Exception #" + (b + 1), linkedList.get(b));
/*     */       }
/*     */     } 
/*     */     
/*  83 */     throw new IOException("Failed to fetch URL '" + paramURL + "'.");
/*     */   }
/*     */ 
/*     */   
/*     */   public FileCache(File paramFile, int paramInt, boolean paramBoolean) throws IOException {
/*  88 */     this(paramFile, paramInt, paramBoolean, Collections.singletonList(URLFetchers.DEFAULT));
/*     */   }
/*     */   public FileCache(File paramFile, int paramInt, boolean paramBoolean, URLFetcher... paramVarArgs) throws IOException {
/*  91 */     this(paramFile, paramInt, paramBoolean, Arrays.asList(paramVarArgs));
/*     */   }
/*     */   
/*     */   public FileCache(File paramFile, int paramInt, boolean paramBoolean, List<URLFetcher> paramList) throws IOException {
/*  95 */     this.cacheDir = paramFile;
/*  96 */     this.buffer_size = paramInt;
/*  97 */     this.read_only = paramBoolean;
/*     */     
/*  99 */     this.fetchers = Collections.unmodifiableList(paramList);
/*     */     
/* 101 */     if (paramFile.exists()) {
/*     */       
/* 103 */       if (!paramFile.isDirectory()) {
/* 104 */         loggedIOException(MLevel.SEVERE, paramFile + "exists and is not a directory. Can't use as cacheDir.");
/* 105 */       } else if (!paramFile.canRead()) {
/* 106 */         loggedIOException(MLevel.SEVERE, paramFile + "must be readable.");
/* 107 */       } else if (!paramFile.canWrite() && !paramBoolean) {
/* 108 */         loggedIOException(MLevel.SEVERE, paramFile + "not writable, and not read only.");
/*     */       } 
/* 110 */     } else if (!paramFile.mkdir()) {
/* 111 */       loggedIOException(MLevel.SEVERE, paramFile + "does not exist and could not be created.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCached(FileCacheKey paramFileCacheKey, boolean paramBoolean) throws IOException {
/* 119 */     File file = file(paramFileCacheKey);
/*     */     
/* 121 */     if (!this.read_only) {
/*     */       
/* 123 */       if (paramBoolean || !file.exists()) {
/*     */         
/* 125 */         BufferedInputStream bufferedInputStream = null;
/* 126 */         BufferedOutputStream bufferedOutputStream = null;
/*     */ 
/*     */         
/*     */         try {
/* 130 */           if (logger.isLoggable(MLevel.FINE))
/* 131 */             logger.log(MLevel.FINE, "Caching file for " + paramFileCacheKey + " to " + file.getAbsolutePath() + "..."); 
/* 132 */           File file1 = file.getParentFile();
/* 133 */           if (!file1.exists()) file1.mkdirs();
/*     */ 
/*     */           
/* 136 */           bufferedInputStream = new BufferedInputStream(fetchURL(paramFileCacheKey.getURL()), this.buffer_size);
/* 137 */           bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), this.buffer_size); int i;
/* 138 */           for (i = bufferedInputStream.read(); i >= 0; i = bufferedInputStream.read()) {
/* 139 */             bufferedOutputStream.write(i);
/*     */           }
/* 141 */           if (logger.isLoggable(MLevel.INFO)) {
/* 142 */             logger.log(MLevel.INFO, "Cached file for " + paramFileCacheKey + ".");
/*     */           }
/* 144 */         } catch (IOException iOException) {
/*     */           
/* 146 */           logger.log(MLevel.WARNING, "An exception occurred while caching file for " + paramFileCacheKey + ". Deleting questionable cached file.", iOException);
/* 147 */           file.delete();
/* 148 */           throw iOException;
/*     */         }
/*     */         finally {
/*     */           
/* 152 */           InputStreamUtils.attemptClose(bufferedInputStream);
/* 153 */           OutputStreamUtils.attemptClose(bufferedOutputStream);
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 158 */       else if (logger.isLoggable(MLevel.FINE)) {
/* 159 */         logger.log(MLevel.FINE, "File for " + paramFileCacheKey + " already exists and force_reacquire is not set.");
/*     */       } 
/*     */     } else {
/* 162 */       if (paramBoolean) {
/*     */         
/* 164 */         String str = "force_reacquire canot be set on a read_only FileCache.";
/* 165 */         IllegalArgumentException illegalArgumentException = new IllegalArgumentException(str);
/* 166 */         logger.log(MLevel.WARNING, str, illegalArgumentException);
/* 167 */         throw illegalArgumentException;
/*     */       } 
/* 169 */       if (!file.exists()) {
/*     */         
/* 171 */         String str = "Cache is read only, and file for key '" + paramFileCacheKey + "' does not exist.";
/* 172 */         FileNotCachedException fileNotCachedException = new FileNotCachedException(str);
/* 173 */         logger.log(MLevel.FINE, str, fileNotCachedException);
/* 174 */         throw fileNotCachedException;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public InputStream fetch(FileCacheKey paramFileCacheKey, boolean paramBoolean) throws IOException {
/* 180 */     ensureCached(paramFileCacheKey, paramBoolean);
/* 181 */     return new FileInputStream(file(paramFileCacheKey));
/*     */   }
/*     */   
/*     */   public boolean isCached(FileCacheKey paramFileCacheKey) throws IOException {
/* 185 */     return file(paramFileCacheKey).exists();
/*     */   }
/* 187 */   static final FileFilter NOT_DIR_FF = new FileFilter()
/*     */     {
/*     */       public boolean accept(File param1File) {
/* 190 */         return !param1File.isDirectory();
/*     */       }
/*     */     };
/*     */   
/*     */   static class NotDirAndFileFilter implements FileFilter {
/*     */     FileFilter ff;
/*     */     
/*     */     NotDirAndFileFilter(FileFilter param1FileFilter) {
/* 198 */       this.ff = param1FileFilter;
/*     */     }
/*     */     public boolean accept(File param1File) {
/* 201 */       return (!param1File.isDirectory() && this.ff.accept(param1File));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int countCached() throws IOException {
/* 207 */     byte b = 0;
/* 208 */     for (FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(this.cacheDir, NOT_DIR_FF, false); fileIterator.hasNext(); ) {
/*     */       
/* 210 */       fileIterator.next();
/* 211 */       b++;
/*     */     } 
/* 213 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public int countCached(FileFilter paramFileFilter) throws IOException {
/* 218 */     byte b = 0;
/* 219 */     for (FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(this.cacheDir, new NotDirAndFileFilter(paramFileFilter), false); fileIterator.hasNext(); ) {
/*     */       
/* 221 */       fileIterator.next();
/* 222 */       b++;
/*     */     } 
/* 224 */     return b;
/*     */   }
/*     */   
/*     */   public File fileForKey(FileCacheKey paramFileCacheKey) {
/* 228 */     return file(paramFileCacheKey);
/*     */   }
/*     */   private File file(FileCacheKey paramFileCacheKey) {
/* 231 */     return new File(this.cacheDir, paramFileCacheKey.getCacheFilePath());
/*     */   }
/*     */   
/*     */   private void loggedIOException(MLevel paramMLevel, String paramString) throws IOException {
/* 235 */     IOException iOException = new IOException(paramString);
/* 236 */     logger.log(paramMLevel, paramString, iOException);
/* 237 */     throw iOException;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/filecache/FileCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */