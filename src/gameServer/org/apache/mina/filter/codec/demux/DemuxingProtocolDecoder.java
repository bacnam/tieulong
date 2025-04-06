/*     */ package org.apache.mina.filter.codec.demux;
/*     */ 
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoderException;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoderOutput;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DemuxingProtocolDecoder
/*     */   extends CumulativeProtocolDecoder
/*     */ {
/*  75 */   private final AttributeKey STATE = new AttributeKey(getClass(), "state");
/*     */   
/*  77 */   private MessageDecoderFactory[] decoderFactories = new MessageDecoderFactory[0];
/*     */   
/*  79 */   private static final Class<?>[] EMPTY_PARAMS = new Class[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessageDecoder(Class<? extends MessageDecoder> decoderClass) {
/*  86 */     if (decoderClass == null) {
/*  87 */       throw new IllegalArgumentException("decoderClass");
/*     */     }
/*     */     
/*     */     try {
/*  91 */       decoderClass.getConstructor(EMPTY_PARAMS);
/*  92 */     } catch (NoSuchMethodException e) {
/*  93 */       throw new IllegalArgumentException("The specified class doesn't have a public default constructor.");
/*     */     } 
/*     */     
/*  96 */     boolean registered = false;
/*  97 */     if (MessageDecoder.class.isAssignableFrom(decoderClass)) {
/*  98 */       addMessageDecoder(new DefaultConstructorMessageDecoderFactory(decoderClass));
/*  99 */       registered = true;
/*     */     } 
/*     */     
/* 102 */     if (!registered) {
/* 103 */       throw new IllegalArgumentException("Unregisterable type: " + decoderClass);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addMessageDecoder(MessageDecoder decoder) {
/* 108 */     addMessageDecoder(new SingletonMessageDecoderFactory(decoder));
/*     */   }
/*     */   
/*     */   public void addMessageDecoder(MessageDecoderFactory factory) {
/* 112 */     if (factory == null) {
/* 113 */       throw new IllegalArgumentException("factory");
/*     */     }
/* 115 */     MessageDecoderFactory[] decoderFactories = this.decoderFactories;
/* 116 */     MessageDecoderFactory[] newDecoderFactories = new MessageDecoderFactory[decoderFactories.length + 1];
/* 117 */     System.arraycopy(decoderFactories, 0, newDecoderFactories, 0, decoderFactories.length);
/* 118 */     newDecoderFactories[decoderFactories.length] = factory;
/* 119 */     this.decoderFactories = newDecoderFactories;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 127 */     State state = getState(session);
/*     */     
/* 129 */     if (state.currentDecoder == null) {
/* 130 */       MessageDecoder[] decoders = state.decoders;
/* 131 */       int undecodables = 0;
/*     */       
/* 133 */       for (int i = decoders.length - 1; i >= 0; i--) {
/* 134 */         MessageDecoderResult result; MessageDecoder decoder = decoders[i];
/* 135 */         int limit = in.limit();
/* 136 */         int pos = in.position();
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 141 */           result = decoder.decodable(session, in);
/*     */         } finally {
/* 143 */           in.position(pos);
/* 144 */           in.limit(limit);
/*     */         } 
/*     */         
/* 147 */         if (result == MessageDecoder.OK) {
/* 148 */           state.currentDecoder = decoder; break;
/*     */         } 
/* 150 */         if (result == MessageDecoder.NOT_OK) {
/* 151 */           undecodables++;
/* 152 */         } else if (result != MessageDecoder.NEED_DATA) {
/* 153 */           throw new IllegalStateException("Unexpected decode result (see your decodable()): " + result);
/*     */         } 
/*     */       } 
/*     */       
/* 157 */       if (undecodables == decoders.length) {
/*     */         
/* 159 */         String dump = in.getHexDump();
/* 160 */         in.position(in.limit());
/* 161 */         ProtocolDecoderException e = new ProtocolDecoderException("No appropriate message decoder: " + dump);
/* 162 */         e.setHexdump(dump);
/* 163 */         throw e;
/*     */       } 
/*     */       
/* 166 */       if (state.currentDecoder == null)
/*     */       {
/* 168 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 173 */       MessageDecoderResult result = state.currentDecoder.decode(session, in, out);
/* 174 */       if (result == MessageDecoder.OK) {
/* 175 */         state.currentDecoder = null;
/* 176 */         return true;
/* 177 */       }  if (result == MessageDecoder.NEED_DATA)
/* 178 */         return false; 
/* 179 */       if (result == MessageDecoder.NOT_OK) {
/* 180 */         state.currentDecoder = null;
/* 181 */         throw new ProtocolDecoderException("Message decoder returned NOT_OK.");
/*     */       } 
/* 183 */       state.currentDecoder = null;
/* 184 */       throw new IllegalStateException("Unexpected decode result (see your decode()): " + result);
/*     */     }
/* 186 */     catch (Exception e) {
/* 187 */       state.currentDecoder = null;
/* 188 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
/* 197 */     super.finishDecode(session, out);
/* 198 */     State state = getState(session);
/* 199 */     MessageDecoder currentDecoder = state.currentDecoder;
/* 200 */     if (currentDecoder == null) {
/*     */       return;
/*     */     }
/*     */     
/* 204 */     currentDecoder.finishDecode(session, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose(IoSession session) throws Exception {
/* 212 */     super.dispose(session);
/* 213 */     session.removeAttribute(this.STATE);
/*     */   }
/*     */   
/*     */   private State getState(IoSession session) throws Exception {
/* 217 */     State state = (State)session.getAttribute(this.STATE);
/*     */     
/* 219 */     if (state == null) {
/* 220 */       state = new State();
/* 221 */       State oldState = (State)session.setAttributeIfAbsent(this.STATE, state);
/*     */       
/* 223 */       if (oldState != null) {
/* 224 */         state = oldState;
/*     */       }
/*     */     } 
/*     */     
/* 228 */     return state;
/*     */   }
/*     */   
/*     */   private class State
/*     */   {
/*     */     private final MessageDecoder[] decoders;
/*     */     private MessageDecoder currentDecoder;
/*     */     
/*     */     private State() throws Exception {
/* 237 */       MessageDecoderFactory[] decoderFactories = DemuxingProtocolDecoder.this.decoderFactories;
/* 238 */       this.decoders = new MessageDecoder[decoderFactories.length];
/* 239 */       for (int i = decoderFactories.length - 1; i >= 0; i--)
/* 240 */         this.decoders[i] = decoderFactories[i].getDecoder(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SingletonMessageDecoderFactory
/*     */     implements MessageDecoderFactory {
/*     */     private final MessageDecoder decoder;
/*     */     
/*     */     private SingletonMessageDecoderFactory(MessageDecoder decoder) {
/* 249 */       if (decoder == null) {
/* 250 */         throw new IllegalArgumentException("decoder");
/*     */       }
/* 252 */       this.decoder = decoder;
/*     */     }
/*     */     
/*     */     public MessageDecoder getDecoder() {
/* 256 */       return this.decoder;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DefaultConstructorMessageDecoderFactory implements MessageDecoderFactory {
/*     */     private final Class<?> decoderClass;
/*     */     
/*     */     private DefaultConstructorMessageDecoderFactory(Class<?> decoderClass) {
/* 264 */       if (decoderClass == null) {
/* 265 */         throw new IllegalArgumentException("decoderClass");
/*     */       }
/*     */       
/* 268 */       if (!MessageDecoder.class.isAssignableFrom(decoderClass)) {
/* 269 */         throw new IllegalArgumentException("decoderClass is not assignable to MessageDecoder");
/*     */       }
/* 271 */       this.decoderClass = decoderClass;
/*     */     }
/*     */     
/*     */     public MessageDecoder getDecoder() throws Exception {
/* 275 */       return (MessageDecoder)this.decoderClass.newInstance();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/DemuxingProtocolDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */