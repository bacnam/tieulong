/*     */ package org.apache.mina.filter.codec.statemachine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoderOutput;
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
/*     */ public abstract class DecodingStateMachine
/*     */   implements DecodingState
/*     */ {
/*  51 */   private final Logger log = LoggerFactory.getLogger(DecodingStateMachine.class);
/*     */   
/*  53 */   private final List<Object> childProducts = new ArrayList();
/*     */   
/*  55 */   private final ProtocolDecoderOutput childOutput = new ProtocolDecoderOutput()
/*     */     {
/*     */       public void flush(IoFilter.NextFilter nextFilter, IoSession session) {}
/*     */ 
/*     */       
/*     */       public void write(Object message) {
/*  61 */         DecodingStateMachine.this.childProducts.add(message);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DecodingState currentState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initialized;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/*  99 */     DecodingState state = getCurrentState();
/*     */     
/* 101 */     int limit = in.limit();
/* 102 */     int pos = in.position();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 107 */       while (pos != limit) {
/*     */ 
/*     */ 
/*     */         
/* 111 */         DecodingState oldState = state;
/* 112 */         state = state.decode(in, this.childOutput);
/*     */ 
/*     */         
/* 115 */         if (state == null) {
/* 116 */           return finishDecode(this.childProducts, out);
/*     */         }
/*     */         
/* 119 */         int newPos = in.position();
/*     */ 
/*     */         
/* 122 */         if (newPos == pos && oldState == state) {
/*     */           break;
/*     */         }
/* 125 */         pos = newPos;
/*     */       } 
/*     */       
/* 128 */       return this;
/* 129 */     } catch (Exception e) {
/* 130 */       state = null;
/* 131 */       throw e;
/*     */     } finally {
/* 133 */       this.currentState = state;
/*     */ 
/*     */       
/* 136 */       if (state == null) {
/* 137 */         cleanup();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/* 147 */     DecodingState decodingState1, state = getCurrentState(); try {
/*     */       DecodingState oldState;
/*     */       do {
/* 150 */         oldState = state;
/* 151 */         state = state.finishDecode(this.childOutput);
/* 152 */         if (state == null)
/*     */         {
/*     */           break;
/*     */         
/*     */         }
/*     */       }
/* 158 */       while (oldState != state);
/*     */ 
/*     */     
/*     */     }
/* 162 */     catch (Exception e) {
/* 163 */       state = null;
/* 164 */       this.log.debug("Ignoring the exception caused by a closed session.", e);
/*     */     } finally {
/* 166 */       this.currentState = state;
/* 167 */       decodingState1 = finishDecode(this.childProducts, out);
/* 168 */       if (state == null) {
/* 169 */         cleanup();
/*     */       }
/*     */     } 
/* 172 */     return decodingState1;
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 176 */     if (!this.initialized) {
/* 177 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 180 */     this.initialized = false;
/* 181 */     this.childProducts.clear();
/*     */     try {
/* 183 */       destroy();
/* 184 */     } catch (Exception e2) {
/* 185 */       this.log.warn("Failed to destroy a decoding state machine.", e2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private DecodingState getCurrentState() throws Exception {
/* 190 */     DecodingState state = this.currentState;
/* 191 */     if (state == null) {
/* 192 */       state = init();
/* 193 */       this.initialized = true;
/*     */     } 
/* 195 */     return state;
/*     */   }
/*     */   
/*     */   protected abstract DecodingState init() throws Exception;
/*     */   
/*     */   protected abstract DecodingState finishDecode(List<Object> paramList, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*     */   
/*     */   protected abstract void destroy() throws Exception;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/DecodingStateMachine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */