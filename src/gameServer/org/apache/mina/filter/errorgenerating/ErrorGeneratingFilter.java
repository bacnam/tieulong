/*     */ package org.apache.mina.filter.errorgenerating;
/*     */ 
/*     */ import java.util.Random;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.DefaultWriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class ErrorGeneratingFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  56 */   private int removeByteProbability = 0;
/*     */   
/*  58 */   private int insertByteProbability = 0;
/*     */   
/*  60 */   private int changeByteProbability = 0;
/*     */   
/*  62 */   private int removePduProbability = 0;
/*     */   
/*  64 */   private int duplicatePduProbability = 0;
/*     */   
/*  66 */   private int resendPduLasterProbability = 0;
/*     */   
/*  68 */   private int maxInsertByte = 10;
/*     */   
/*     */   private boolean manipulateWrites = false;
/*     */   
/*     */   private boolean manipulateReads = false;
/*     */   
/*  74 */   private Random rng = new Random();
/*     */   
/*  76 */   private final Logger logger = LoggerFactory.getLogger(ErrorGeneratingFilter.class);
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*     */     DefaultWriteRequest defaultWriteRequest;
/*  80 */     if (this.manipulateWrites)
/*     */     {
/*  82 */       if (writeRequest.getMessage() instanceof IoBuffer) {
/*  83 */         manipulateIoBuffer(session, (IoBuffer)writeRequest.getMessage());
/*  84 */         IoBuffer buffer = insertBytesToNewIoBuffer(session, (IoBuffer)writeRequest.getMessage());
/*  85 */         if (buffer != null) {
/*  86 */           defaultWriteRequest = new DefaultWriteRequest(buffer, writeRequest.getFuture(), writeRequest.getDestination());
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/*  91 */         if (this.duplicatePduProbability > this.rng.nextInt()) {
/*  92 */           nextFilter.filterWrite(session, (WriteRequest)defaultWriteRequest);
/*     */         }
/*     */         
/*  95 */         if (this.resendPduLasterProbability > this.rng.nextInt());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 100 */         if (this.removePduProbability > this.rng.nextInt()) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     }
/* 105 */     nextFilter.filterWrite(session, (WriteRequest)defaultWriteRequest);
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/* 110 */     if (this.manipulateReads && 
/* 111 */       message instanceof IoBuffer) {
/*     */       
/* 113 */       manipulateIoBuffer(session, (IoBuffer)message);
/* 114 */       IoBuffer buffer = insertBytesToNewIoBuffer(session, (IoBuffer)message);
/* 115 */       if (buffer != null) {
/* 116 */         message = buffer;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     nextFilter.messageReceived(session, message);
/*     */   }
/*     */   
/*     */   private IoBuffer insertBytesToNewIoBuffer(IoSession session, IoBuffer buffer) {
/* 127 */     if (this.insertByteProbability > this.rng.nextInt(1000)) {
/* 128 */       this.logger.info(buffer.getHexDump());
/*     */       
/* 130 */       int pos = this.rng.nextInt(buffer.remaining()) - 1;
/*     */ 
/*     */       
/* 133 */       int count = this.rng.nextInt(this.maxInsertByte - 1) + 1;
/*     */       
/* 135 */       IoBuffer newBuff = IoBuffer.allocate(buffer.remaining() + count); int i;
/* 136 */       for (i = 0; i < pos; i++)
/* 137 */         newBuff.put(buffer.get()); 
/* 138 */       for (i = 0; i < count; i++) {
/* 139 */         newBuff.put((byte)this.rng.nextInt(256));
/*     */       }
/* 141 */       while (buffer.remaining() > 0) {
/* 142 */         newBuff.put(buffer.get());
/*     */       }
/* 144 */       newBuff.flip();
/*     */       
/* 146 */       this.logger.info("Inserted " + count + " bytes.");
/* 147 */       this.logger.info(newBuff.getHexDump());
/* 148 */       return newBuff;
/*     */     } 
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   private void manipulateIoBuffer(IoSession session, IoBuffer buffer) {
/* 154 */     if (buffer.remaining() > 0 && this.removeByteProbability > this.rng.nextInt(1000)) {
/* 155 */       this.logger.info(buffer.getHexDump());
/*     */       
/* 157 */       int pos = this.rng.nextInt(buffer.remaining());
/*     */       
/* 159 */       int count = this.rng.nextInt(buffer.remaining() - pos) + 1;
/* 160 */       if (count == buffer.remaining()) {
/* 161 */         count = buffer.remaining() - 1;
/*     */       }
/* 163 */       IoBuffer newBuff = IoBuffer.allocate(buffer.remaining() - count);
/* 164 */       for (int i = 0; i < pos; i++) {
/* 165 */         newBuff.put(buffer.get());
/*     */       }
/* 167 */       buffer.skip(count);
/* 168 */       while (newBuff.remaining() > 0)
/* 169 */         newBuff.put(buffer.get()); 
/* 170 */       newBuff.flip();
/*     */       
/* 172 */       buffer.rewind();
/* 173 */       buffer.put(newBuff);
/* 174 */       buffer.flip();
/* 175 */       this.logger.info("Removed " + count + " bytes at position " + pos + ".");
/* 176 */       this.logger.info(buffer.getHexDump());
/*     */     } 
/* 178 */     if (buffer.remaining() > 0 && this.changeByteProbability > this.rng.nextInt(1000)) {
/* 179 */       this.logger.info(buffer.getHexDump());
/*     */       
/* 181 */       int count = this.rng.nextInt(buffer.remaining() - 1) + 1;
/*     */       
/* 183 */       byte[] values = new byte[count];
/* 184 */       this.rng.nextBytes(values);
/* 185 */       for (int i = 0; i < values.length; i++) {
/* 186 */         int pos = this.rng.nextInt(buffer.remaining());
/* 187 */         buffer.put(pos, values[i]);
/*     */       } 
/* 189 */       this.logger.info("Modified " + count + " bytes.");
/* 190 */       this.logger.info(buffer.getHexDump());
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getChangeByteProbability() {
/* 195 */     return this.changeByteProbability;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChangeByteProbability(int changeByteProbability) {
/* 205 */     this.changeByteProbability = changeByteProbability;
/*     */   }
/*     */   
/*     */   public int getDuplicatePduProbability() {
/* 209 */     return this.duplicatePduProbability;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDuplicatePduProbability(int duplicatePduProbability) {
/* 217 */     this.duplicatePduProbability = duplicatePduProbability;
/*     */   }
/*     */   
/*     */   public int getInsertByteProbability() {
/* 221 */     return this.insertByteProbability;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInsertByteProbability(int insertByteProbability) {
/* 231 */     this.insertByteProbability = insertByteProbability;
/*     */   }
/*     */   
/*     */   public boolean isManipulateReads() {
/* 235 */     return this.manipulateReads;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManipulateReads(boolean manipulateReads) {
/* 243 */     this.manipulateReads = manipulateReads;
/*     */   }
/*     */   
/*     */   public boolean isManipulateWrites() {
/* 247 */     return this.manipulateWrites;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManipulateWrites(boolean manipulateWrites) {
/* 255 */     this.manipulateWrites = manipulateWrites;
/*     */   }
/*     */   
/*     */   public int getRemoveByteProbability() {
/* 259 */     return this.removeByteProbability;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveByteProbability(int removeByteProbability) {
/* 269 */     this.removeByteProbability = removeByteProbability;
/*     */   }
/*     */   
/*     */   public int getRemovePduProbability() {
/* 273 */     return this.removePduProbability;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemovePduProbability(int removePduProbability) {
/* 281 */     this.removePduProbability = removePduProbability;
/*     */   }
/*     */   
/*     */   public int getResendPduLasterProbability() {
/* 285 */     return this.resendPduLasterProbability;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResendPduLasterProbability(int resendPduLasterProbability) {
/* 293 */     this.resendPduLasterProbability = resendPduLasterProbability;
/*     */   }
/*     */   
/*     */   public int getMaxInsertByte() {
/* 297 */     return this.maxInsertByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxInsertByte(int maxInsertByte) {
/* 306 */     this.maxInsertByte = maxInsertByte;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/errorgenerating/ErrorGeneratingFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */