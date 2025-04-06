/*     */ package org.apache.http.nio.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.AbstractHttpEntity;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.ContentEncoderChannel;
/*     */ import org.apache.http.nio.FileContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.util.Args;
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
/*     */ @NotThreadSafe
/*     */ public class NFileEntity
/*     */   extends AbstractHttpEntity
/*     */   implements HttpAsyncContentProducer, ProducingNHttpEntity
/*     */ {
/*     */   private final File file;
/*     */   private RandomAccessFile accessfile;
/*     */   private FileChannel fileChannel;
/*  63 */   private long idx = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useFileChannels;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NFileEntity(File file, ContentType contentType, boolean useFileChannels) {
/*  80 */     Args.notNull(file, "File");
/*  81 */     this.file = file;
/*  82 */     this.useFileChannels = useFileChannels;
/*  83 */     if (contentType != null) {
/*  84 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NFileEntity(File file) {
/*  92 */     Args.notNull(file, "File");
/*  93 */     this.file = file;
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
/*     */   public NFileEntity(File file, ContentType contentType) {
/* 105 */     this(file, contentType, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NFileEntity(File file, String contentType, boolean useFileChannels) {
/* 113 */     Args.notNull(file, "File");
/* 114 */     this.file = file;
/* 115 */     this.useFileChannels = useFileChannels;
/* 116 */     setContentType(contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NFileEntity(File file, String contentType) {
/* 124 */     this(file, contentType, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 134 */     if (this.accessfile != null) {
/* 135 */       this.accessfile.close();
/*     */     }
/* 137 */     this.accessfile = null;
/* 138 */     this.fileChannel = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void finish() throws IOException {
/* 148 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 153 */     return this.file.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/*     */     long transferred;
/* 164 */     if (this.accessfile == null) {
/* 165 */       this.accessfile = new RandomAccessFile(this.file, "r");
/*     */     }
/* 167 */     if (this.fileChannel == null) {
/* 168 */       this.fileChannel = this.accessfile.getChannel();
/* 169 */       this.idx = 0L;
/*     */     } 
/*     */ 
/*     */     
/* 173 */     if (this.useFileChannels && encoder instanceof FileContentEncoder) {
/* 174 */       transferred = ((FileContentEncoder)encoder).transfer(this.fileChannel, this.idx, Long.MAX_VALUE);
/*     */     } else {
/*     */       
/* 177 */       transferred = this.fileChannel.transferTo(this.idx, Long.MAX_VALUE, (WritableByteChannel)new ContentEncoderChannel(encoder));
/*     */     } 
/*     */     
/* 180 */     if (transferred > 0L) {
/* 181 */       this.idx += transferred;
/*     */     }
/* 183 */     if (this.idx >= this.fileChannel.size()) {
/* 184 */       encoder.complete();
/* 185 */       close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/* 196 */     return new FileInputStream(this.file);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 201 */     Args.notNull(outstream, "Output stream");
/* 202 */     InputStream instream = new FileInputStream(this.file);
/*     */     try {
/* 204 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 206 */       while ((l = instream.read(tmp)) != -1) {
/* 207 */         outstream.write(tmp, 0, l);
/*     */       }
/* 209 */       outstream.flush();
/*     */     } finally {
/* 211 */       instream.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/NFileEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */