/*     */ package com.zhonglian.server.websocket.codecfactory;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoderOutput;
/*     */ import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
/*     */ import org.apache.mina.filter.codec.demux.MessageDecoder;
/*     */ import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
/*     */ import org.apache.mina.filter.codec.demux.MessageDecoderResult;
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
/*     */ public class ServerDecoder
/*     */   extends DemuxingProtocolDecoder
/*     */ {
/*     */   public static final byte MASK = 1;
/*     */   public static final byte HAS_EXTEND_DATA = 126;
/*     */   public static final byte HAS_EXTEND_DATA_CONTINUE = 127;
/*     */   public static final byte PAYLOADLEN = 127;
/*     */   public static final byte OPCODE = 15;
/*     */   
/*     */   public ServerDecoder() {
/*  40 */     addMessageDecoder((MessageDecoder)new BaseSocketBeanDecoder());
/*     */   }
/*     */   
/*     */   class BaseSocketBeanDecoder extends MessageDecoderAdapter {
/*     */     public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
/*  45 */       if (in.remaining() < 2) {
/*  46 */         return NEED_DATA;
/*     */       }
/*  48 */       byte head1 = in.get();
/*  49 */       int opcode = head1 & 0xF;
/*  50 */       if (opcode == 8) {
/*  51 */         session.close(true);
/*  52 */         return MessageDecoderResult.NOT_OK;
/*     */       } 
/*  54 */       byte head2 = in.get();
/*  55 */       byte datalength = (byte)(head2 & Byte.MAX_VALUE);
/*  56 */       int length = 0;
/*  57 */       if (datalength < 126) {
/*  58 */         length = datalength;
/*  59 */       } else if (datalength == 126) {
/*  60 */         if (in.remaining() < 2) {
/*  61 */           return NEED_DATA;
/*     */         }
/*  63 */         byte[] extended = new byte[2];
/*  64 */         in.get(extended);
/*  65 */         int shift = 0;
/*  66 */         length = 0;
/*  67 */         for (int i = extended.length - 1; i >= 0; i--) {
/*  68 */           length += (extended[i] & 0xFF) << shift;
/*  69 */           shift += 8;
/*     */         } 
/*  71 */       } else if (datalength == Byte.MAX_VALUE) {
/*  72 */         if (in.remaining() < 4) {
/*  73 */           return NEED_DATA;
/*     */         }
/*  75 */         byte[] extended = new byte[4];
/*  76 */         in.get(extended);
/*  77 */         int shift = 0;
/*  78 */         length = 0;
/*  79 */         for (int i = extended.length - 1; i >= 0; i--) {
/*  80 */           length += (extended[i] & 0xFF) << shift;
/*  81 */           shift += 8;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  86 */       if (in.remaining() < length) {
/*  87 */         return NEED_DATA;
/*     */       }
/*  89 */       return OK;
/*     */     }
/*     */ 
/*     */     
/*     */     public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/*  94 */       in.get();
/*  95 */       byte head2 = in.get();
/*  96 */       int length = (byte)(head2 & Byte.MAX_VALUE);
/*  97 */       if (length >= 126) {
/*  98 */         if (length == 126) {
/*  99 */           byte[] extended = new byte[2];
/* 100 */           in.get(extended);
/* 101 */           int shift = 0;
/* 102 */           length = 0;
/* 103 */           for (int i = extended.length - 1; i >= 0; i--) {
/* 104 */             length += (extended[i] & 0xFF) << shift;
/* 105 */             shift += 8;
/*     */           } 
/* 107 */         } else if (length == 127) {
/* 108 */           byte[] extended = new byte[4];
/* 109 */           in.get(extended);
/* 110 */           int shift = 0;
/* 111 */           length = 0;
/* 112 */           for (int i = extended.length - 1; i >= 0; i--) {
/* 113 */             length += (extended[i] & 0xFF) << shift;
/* 114 */             shift += 8;
/*     */           } 
/*     */         } 
/*     */       }
/* 118 */       byte[] data = new byte[Math.min(length, in.remaining())];
/* 119 */       in.get(data);
/* 120 */       out.write(ByteBuffer.wrap(data));
/* 121 */       return OK;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/codecfactory/ServerDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */