/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.MalformedChunkCodingException;
/*     */ import org.apache.http.MessageConstraintException;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.TruncatedChunkException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.nio.reactor.SessionInputBuffer;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
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
/*     */ public class ChunkDecoder
/*     */   extends AbstractContentDecoder
/*     */ {
/*     */   private static final int READ_CONTENT = 0;
/*     */   private static final int READ_FOOTERS = 1;
/*     */   private static final int COMPLETED = 2;
/*     */   private int state;
/*     */   private boolean endOfChunk;
/*     */   private boolean endOfStream;
/*     */   private CharArrayBuffer lineBuf;
/*     */   private int chunkSize;
/*     */   private int pos;
/*     */   private final MessageConstraints constraints;
/*     */   private final List<CharArrayBuffer> trailerBufs;
/*     */   private Header[] footers;
/*     */   
/*     */   public ChunkDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, MessageConstraints constraints, HttpTransportMetricsImpl metrics) {
/*  84 */     super(channel, buffer, metrics);
/*  85 */     this.state = 0;
/*  86 */     this.chunkSize = -1;
/*  87 */     this.pos = 0;
/*  88 */     this.endOfChunk = false;
/*  89 */     this.endOfStream = false;
/*  90 */     this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
/*  91 */     this.trailerBufs = new ArrayList<CharArrayBuffer>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics) {
/*  98 */     this(channel, buffer, (MessageConstraints)null, metrics);
/*     */   }
/*     */   
/*     */   private void readChunkHead() throws IOException {
/* 102 */     if (this.lineBuf == null) {
/* 103 */       this.lineBuf = new CharArrayBuffer(32);
/*     */     } else {
/* 105 */       this.lineBuf.clear();
/*     */     } 
/* 107 */     if (this.endOfChunk) {
/* 108 */       if (this.buffer.readLine(this.lineBuf, this.endOfStream)) {
/* 109 */         if (!this.lineBuf.isEmpty()) {
/* 110 */           throw new MalformedChunkCodingException("CRLF expected at end of chunk");
/*     */         }
/*     */       } else {
/* 113 */         if (this.buffer.length() > 2 || this.endOfStream) {
/* 114 */           throw new MalformedChunkCodingException("CRLF expected at end of chunk");
/*     */         }
/*     */         return;
/*     */       } 
/* 118 */       this.endOfChunk = false;
/*     */     } 
/* 120 */     boolean lineComplete = this.buffer.readLine(this.lineBuf, this.endOfStream);
/* 121 */     int maxLineLen = this.constraints.getMaxLineLength();
/* 122 */     if (maxLineLen > 0 && (this.lineBuf.length() > maxLineLen || (!lineComplete && this.buffer.length() > maxLineLen)))
/*     */     {
/*     */       
/* 125 */       throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */     }
/* 127 */     if (lineComplete) {
/* 128 */       int separator = this.lineBuf.indexOf(59);
/* 129 */       if (separator < 0) {
/* 130 */         separator = this.lineBuf.length();
/*     */       }
/*     */       try {
/* 133 */         String s = this.lineBuf.substringTrimmed(0, separator);
/* 134 */         this.chunkSize = Integer.parseInt(s, 16);
/* 135 */       } catch (NumberFormatException e) {
/* 136 */         throw new MalformedChunkCodingException("Bad chunk header");
/*     */       } 
/* 138 */       this.pos = 0;
/* 139 */     } else if (this.endOfStream) {
/* 140 */       throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseHeader() throws IOException {
/* 146 */     CharArrayBuffer current = this.lineBuf;
/* 147 */     int count = this.trailerBufs.size();
/* 148 */     if ((this.lineBuf.charAt(0) == ' ' || this.lineBuf.charAt(0) == '\t') && count > 0) {
/*     */       
/* 150 */       CharArrayBuffer previous = this.trailerBufs.get(count - 1);
/* 151 */       int i = 0;
/* 152 */       while (i < current.length()) {
/* 153 */         char ch = current.charAt(i);
/* 154 */         if (ch != ' ' && ch != '\t') {
/*     */           break;
/*     */         }
/* 157 */         i++;
/*     */       } 
/* 159 */       int maxLineLen = this.constraints.getMaxLineLength();
/* 160 */       if (maxLineLen > 0 && previous.length() + 1 + current.length() - i > maxLineLen) {
/* 161 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/* 163 */       previous.append(' ');
/* 164 */       previous.append(current, i, current.length() - i);
/*     */     } else {
/* 166 */       this.trailerBufs.add(current);
/* 167 */       this.lineBuf = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processFooters() throws IOException {
/* 172 */     int count = this.trailerBufs.size();
/* 173 */     if (count > 0) {
/* 174 */       this.footers = new Header[this.trailerBufs.size()];
/* 175 */       for (int i = 0; i < this.trailerBufs.size(); i++) {
/*     */         try {
/* 177 */           this.footers[i] = (Header)new BufferedHeader(this.trailerBufs.get(i));
/* 178 */         } catch (ParseException ex) {
/* 179 */           throw new IOException(ex.getMessage());
/*     */         } 
/*     */       } 
/*     */     } 
/* 183 */     this.trailerBufs.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/* 188 */     Args.notNull(dst, "Byte buffer");
/* 189 */     if (this.state == 2) {
/* 190 */       return -1;
/*     */     }
/*     */     
/* 193 */     int totalRead = 0;
/* 194 */     while (this.state != 2) {
/*     */       int maxLen; int len;
/* 196 */       if (!this.buffer.hasData() || this.chunkSize == -1) {
/* 197 */         int bytesRead = fillBufferFromChannel();
/* 198 */         if (bytesRead == -1) {
/* 199 */           this.endOfStream = true;
/*     */         }
/*     */       } 
/*     */       
/* 203 */       switch (this.state) {
/*     */         
/*     */         case 0:
/* 206 */           if (this.chunkSize == -1) {
/* 207 */             readChunkHead();
/* 208 */             if (this.chunkSize == -1)
/*     */             {
/* 210 */               return totalRead;
/*     */             }
/* 212 */             if (this.chunkSize == 0) {
/*     */               
/* 214 */               this.chunkSize = -1;
/* 215 */               this.state = 1;
/*     */               continue;
/*     */             } 
/*     */           } 
/* 219 */           maxLen = this.chunkSize - this.pos;
/* 220 */           len = this.buffer.read(dst, maxLen);
/* 221 */           if (len > 0) {
/* 222 */             this.pos += len;
/* 223 */             totalRead += len;
/*     */           }
/* 225 */           else if (!this.buffer.hasData() && this.endOfStream) {
/* 226 */             this.state = 2;
/* 227 */             this.completed = true;
/* 228 */             throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 234 */           if (this.pos == this.chunkSize) {
/*     */             
/* 236 */             this.chunkSize = -1;
/* 237 */             this.pos = 0;
/* 238 */             this.endOfChunk = true;
/*     */             continue;
/*     */           } 
/* 241 */           return totalRead;
/*     */         case 1:
/* 243 */           if (this.lineBuf == null) {
/* 244 */             this.lineBuf = new CharArrayBuffer(32);
/*     */           } else {
/* 246 */             this.lineBuf.clear();
/*     */           } 
/* 248 */           if (!this.buffer.readLine(this.lineBuf, this.endOfStream)) {
/*     */             
/* 250 */             if (this.endOfStream) {
/* 251 */               this.state = 2;
/* 252 */               this.completed = true;
/*     */             } 
/* 254 */             return totalRead;
/*     */           } 
/* 256 */           if (this.lineBuf.length() > 0) {
/* 257 */             int maxHeaderCount = this.constraints.getMaxHeaderCount();
/* 258 */             if (maxHeaderCount > 0 && this.trailerBufs.size() >= maxHeaderCount) {
/* 259 */               throw new MessageConstraintException("Maximum header count exceeded");
/*     */             }
/* 261 */             parseHeader(); continue;
/*     */           } 
/* 263 */           this.state = 2;
/* 264 */           this.completed = true;
/* 265 */           processFooters();
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 271 */     return totalRead;
/*     */   }
/*     */   
/*     */   public Header[] getFooters() {
/* 275 */     if (this.footers != null) {
/* 276 */       return (Header[])this.footers.clone();
/*     */     }
/* 278 */     return new Header[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 284 */     StringBuilder sb = new StringBuilder();
/* 285 */     sb.append("[chunk-coded; completed: ");
/* 286 */     sb.append(this.completed);
/* 287 */     sb.append("]");
/* 288 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/ChunkDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */