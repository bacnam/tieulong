/*     */ package org.apache.http.impl.nio.codecs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.MessageConstraintException;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.message.BasicLineParser;
/*     */ import org.apache.http.message.LineParser;
/*     */ import org.apache.http.nio.NHttpMessageParser;
/*     */ import org.apache.http.nio.reactor.SessionInputBuffer;
/*     */ import org.apache.http.params.HttpParamConfig;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ public abstract class AbstractMessageParser<T extends HttpMessage>
/*     */   implements NHttpMessageParser<T>
/*     */ {
/*     */   private final SessionInputBuffer sessionBuffer;
/*     */   private static final int READ_HEAD_LINE = 0;
/*     */   private static final int READ_HEADERS = 1;
/*     */   private static final int COMPLETED = 2;
/*     */   private int state;
/*     */   private boolean endOfStream;
/*     */   private T message;
/*     */   private CharArrayBuffer lineBuf;
/*     */   private final List<CharArrayBuffer> headerBufs;
/*     */   protected final LineParser lineParser;
/*     */   private final MessageConstraints constraints;
/*     */   
/*     */   @Deprecated
/*     */   public AbstractMessageParser(SessionInputBuffer buffer, LineParser lineParser, HttpParams params) {
/*  94 */     Args.notNull(buffer, "Session input buffer");
/*  95 */     Args.notNull(params, "HTTP parameters");
/*  96 */     this.sessionBuffer = buffer;
/*  97 */     this.state = 0;
/*  98 */     this.endOfStream = false;
/*  99 */     this.headerBufs = new ArrayList<CharArrayBuffer>();
/* 100 */     this.constraints = HttpParamConfig.getMessageConstraints(params);
/* 101 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)BasicLineParser.INSTANCE;
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
/*     */ 
/*     */   
/*     */   public AbstractMessageParser(SessionInputBuffer buffer, LineParser lineParser, MessageConstraints constraints) {
/* 120 */     this.sessionBuffer = (SessionInputBuffer)Args.notNull(buffer, "Session input buffer");
/* 121 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)BasicLineParser.INSTANCE;
/* 122 */     this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
/* 123 */     this.headerBufs = new ArrayList<CharArrayBuffer>();
/* 124 */     this.state = 0;
/* 125 */     this.endOfStream = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 130 */     this.state = 0;
/* 131 */     this.endOfStream = false;
/* 132 */     this.headerBufs.clear();
/* 133 */     this.message = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int fillBuffer(ReadableByteChannel channel) throws IOException {
/* 138 */     int bytesRead = this.sessionBuffer.fill(channel);
/* 139 */     if (bytesRead == -1) {
/* 140 */       this.endOfStream = true;
/*     */     }
/* 142 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T createMessage(CharArrayBuffer paramCharArrayBuffer) throws HttpException, ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseHeadLine() throws HttpException, ParseException {
/* 158 */     this.message = createMessage(this.lineBuf);
/*     */   }
/*     */   
/*     */   private void parseHeader() throws IOException {
/* 162 */     CharArrayBuffer current = this.lineBuf;
/* 163 */     int count = this.headerBufs.size();
/* 164 */     if ((this.lineBuf.charAt(0) == ' ' || this.lineBuf.charAt(0) == '\t') && count > 0) {
/*     */       
/* 166 */       CharArrayBuffer previous = this.headerBufs.get(count - 1);
/* 167 */       int i = 0;
/* 168 */       while (i < current.length()) {
/* 169 */         char ch = current.charAt(i);
/* 170 */         if (ch != ' ' && ch != '\t') {
/*     */           break;
/*     */         }
/* 173 */         i++;
/*     */       } 
/* 175 */       int maxLineLen = this.constraints.getMaxLineLength();
/* 176 */       if (maxLineLen > 0 && previous.length() + 1 + current.length() - i > maxLineLen) {
/* 177 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/* 179 */       previous.append(' ');
/* 180 */       previous.append(current, i, current.length() - i);
/*     */     } else {
/* 182 */       this.headerBufs.add(current);
/* 183 */       this.lineBuf = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public T parse() throws IOException, HttpException {
/* 189 */     while (this.state != 2) {
/* 190 */       if (this.lineBuf == null) {
/* 191 */         this.lineBuf = new CharArrayBuffer(64);
/*     */       } else {
/* 193 */         this.lineBuf.clear();
/*     */       } 
/* 195 */       boolean lineComplete = this.sessionBuffer.readLine(this.lineBuf, this.endOfStream);
/* 196 */       int maxLineLen = this.constraints.getMaxLineLength();
/* 197 */       if (maxLineLen > 0 && (this.lineBuf.length() > maxLineLen || (!lineComplete && this.sessionBuffer.length() > maxLineLen)))
/*     */       {
/*     */         
/* 200 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/* 202 */       if (!lineComplete) {
/*     */         break;
/*     */       }
/*     */       
/* 206 */       switch (this.state) {
/*     */         case 0:
/*     */           try {
/* 209 */             parseHeadLine();
/* 210 */           } catch (ParseException px) {
/* 211 */             throw new ProtocolException(px.getMessage(), px);
/*     */           } 
/* 213 */           this.state = 1;
/*     */           break;
/*     */         case 1:
/* 216 */           if (this.lineBuf.length() > 0) {
/* 217 */             int maxHeaderCount = this.constraints.getMaxHeaderCount();
/* 218 */             if (maxHeaderCount > 0 && this.headerBufs.size() >= maxHeaderCount) {
/* 219 */               throw new MessageConstraintException("Maximum header count exceeded");
/*     */             }
/*     */             
/* 222 */             parseHeader(); break;
/*     */           } 
/* 224 */           this.state = 2;
/*     */           break;
/*     */       } 
/*     */       
/* 228 */       if (this.endOfStream && !this.sessionBuffer.hasData()) {
/* 229 */         this.state = 2;
/*     */       }
/*     */     } 
/* 232 */     if (this.state == 2) {
/* 233 */       for (CharArrayBuffer buffer : this.headerBufs) {
/*     */         try {
/* 235 */           this.message.addHeader(this.lineParser.parseHeader(buffer));
/* 236 */         } catch (ParseException ex) {
/* 237 */           throw new ProtocolException(ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/* 240 */       return this.message;
/*     */     } 
/* 242 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/AbstractMessageParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */