/*     */ package bsh.org.objectweb.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Label
/*     */ {
/*     */   CodeWriter owner;
/*     */   boolean resolved;
/*     */   int position;
/*     */   private int referenceCount;
/*     */   private int[] srcAndRefPositions;
/*     */   int beginStackSize;
/*     */   int maxStackSize;
/*     */   Edge successors;
/*     */   Label next;
/*     */   boolean pushed;
/*     */   
/*     */   void put(CodeWriter owner, ByteVector out, int source, boolean wideOffset) {
/* 163 */     if (this.resolved) {
/* 164 */       if (wideOffset) {
/* 165 */         out.put4(this.position - source);
/*     */       } else {
/* 167 */         out.put2(this.position - source);
/*     */       }
/*     */     
/* 170 */     } else if (wideOffset) {
/* 171 */       addReference(-1 - source, out.length);
/* 172 */       out.put4(-1);
/*     */     } else {
/* 174 */       addReference(source, out.length);
/* 175 */       out.put2(-1);
/*     */     } 
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
/*     */   
/*     */   private void addReference(int sourcePosition, int referencePosition) {
/* 196 */     if (this.srcAndRefPositions == null) {
/* 197 */       this.srcAndRefPositions = new int[6];
/*     */     }
/* 199 */     if (this.referenceCount >= this.srcAndRefPositions.length) {
/* 200 */       int[] a = new int[this.srcAndRefPositions.length + 6];
/* 201 */       System.arraycopy(this.srcAndRefPositions, 0, a, 0, this.srcAndRefPositions.length);
/* 202 */       this.srcAndRefPositions = a;
/*     */     } 
/* 204 */     this.srcAndRefPositions[this.referenceCount++] = sourcePosition;
/* 205 */     this.srcAndRefPositions[this.referenceCount++] = referencePosition;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean resolve(CodeWriter owner, int position, byte[] data) {
/* 240 */     boolean needUpdate = false;
/* 241 */     this.resolved = true;
/* 242 */     this.position = position;
/* 243 */     int i = 0;
/* 244 */     while (i < this.referenceCount) {
/* 245 */       int source = this.srcAndRefPositions[i++];
/* 246 */       int reference = this.srcAndRefPositions[i++];
/*     */       
/* 248 */       if (source >= 0) {
/* 249 */         int j = position - source;
/* 250 */         if (j < -32768 || j > 32767) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 257 */           int opcode = data[reference - 1] & 0xFF;
/* 258 */           if (opcode <= 168) {
/*     */             
/* 260 */             data[reference - 1] = (byte)(opcode + 49);
/*     */           } else {
/*     */             
/* 263 */             data[reference - 1] = (byte)(opcode + 20);
/*     */           } 
/* 265 */           needUpdate = true;
/*     */         } 
/* 267 */         data[reference++] = (byte)(j >>> 8);
/* 268 */         data[reference] = (byte)j; continue;
/*     */       } 
/* 270 */       int offset = position + source + 1;
/* 271 */       data[reference++] = (byte)(offset >>> 24);
/* 272 */       data[reference++] = (byte)(offset >>> 16);
/* 273 */       data[reference++] = (byte)(offset >>> 8);
/* 274 */       data[reference] = (byte)offset;
/*     */     } 
/*     */     
/* 277 */     return needUpdate;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/org/objectweb/asm/Label.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */