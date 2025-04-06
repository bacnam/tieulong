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
/*     */ public class WebDecoder
/*     */   extends DemuxingProtocolDecoder
/*     */ {
/*     */   public static final byte MASK = 1;
/*     */   public static final byte HAS_EXTEND_DATA = 126;
/*     */   public static final byte HAS_EXTEND_DATA_CONTINUE = 127;
/*     */   public static final byte PAYLOADLEN = 127;
/*     */   public static final byte OPCODE = 15;
/*     */   
/*     */   public WebDecoder() {
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
/*  85 */       int ismask = head2 >> 7 & 0x1;
/*  86 */       if (ismask == 1) {
/*  87 */         if (in.remaining() < 4 + length) {
/*  88 */           return NEED_DATA;
/*     */         }
/*  90 */         return OK;
/*     */       } 
/*     */       
/*  93 */       if (in.remaining() < length) {
/*  94 */         return NEED_DATA;
/*     */       }
/*  96 */       return OK;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 102 */       in.get();
/* 103 */       byte head2 = in.get();
/* 104 */       int length = (byte)(head2 & Byte.MAX_VALUE);
/* 105 */       if (length >= 126) {
/* 106 */         if (length == 126) {
/* 107 */           byte[] extended = new byte[2];
/* 108 */           in.get(extended);
/* 109 */           int shift = 0;
/* 110 */           length = 0;
/* 111 */           for (int i = extended.length - 1; i >= 0; i--) {
/* 112 */             length += (extended[i] & 0xFF) << shift;
/* 113 */             shift += 8;
/*     */           } 
/* 115 */         } else if (length == 127) {
/* 116 */           byte[] extended = new byte[4];
/* 117 */           in.get(extended);
/* 118 */           int shift = 0;
/* 119 */           length = 0;
/* 120 */           for (int i = extended.length - 1; i >= 0; i--) {
/* 121 */             length += (extended[i] & 0xFF) << shift;
/* 122 */             shift += 8;
/*     */           } 
/*     */         } 
/*     */       }
/* 126 */       int ismask = head2 >> 7 & 0x1;
/* 127 */       byte[] data = null;
/* 128 */       if (ismask == 1) {
/*     */         
/* 130 */         byte[] mask = new byte[4];
/* 131 */         in.get(mask);
/*     */         
/* 133 */         data = new byte[Math.min(length, in.remaining())];
/* 134 */         in.get(data);
/* 135 */         for (int i = 0; i < data.length; i++)
/*     */         {
/* 137 */           data[i] = (byte)(data[i] ^ mask[i % 4]);
/*     */         }
/* 139 */         out.write(ByteBuffer.wrap(data));
/*     */       } else {
/*     */         
/* 142 */         byte[] b = new byte[in.remaining()];
/* 143 */         in.get(b);
/* 144 */         out.write(new String(b, "UTF-8"));
/*     */       } 
/* 146 */       return OK;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/codecfactory/WebDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */