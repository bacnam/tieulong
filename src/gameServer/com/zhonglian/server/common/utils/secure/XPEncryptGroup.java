/*    */ package com.zhonglian.server.common.utils.secure;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XPEncryptGroup
/*    */ {
/* 10 */   private int[] keys = new int[] { 592389130, 374625019, -215789270, -1340029375, 1484193588 };
/*    */   public static XPEncryptGroup instance;
/*    */   
/*    */   public XPEncryptGroup(int key) {
/* 14 */     this.keys[0] = key;
/*    */   }
/*    */   
/*    */   public long encode(long data) {
/* 18 */     Pair p = new Pair(data);
/* 19 */     for (int i = 0; i < this.keys.length; i++) {
/* 20 */       p.round(this.keys[i]);
/*    */     }
/* 22 */     p.exchange();
/*    */     
/* 24 */     return p.getValue();
/*    */   }
/*    */   
/*    */   public long decode(long data) {
/* 28 */     Pair p = new Pair(data);
/* 29 */     for (int i = this.keys.length - 1; i >= 0; i--) {
/* 30 */       p.round(this.keys[i]);
/*    */     }
/* 32 */     p.exchange();
/* 33 */     return p.getValue();
/*    */   }
/*    */   
/*    */   public long decode(int _a, int _b) {
/* 37 */     Pair p = new Pair(_a, _b);
/*    */     
/* 39 */     for (int i = this.keys.length - 1; i >= 0; i--) {
/* 40 */       p.round(this.keys[i]);
/*    */     }
/* 42 */     p.exchange();
/* 43 */     return p.getValue();
/*    */   }
/*    */   
/*    */   class Pair {
/*    */     int a;
/*    */     int b;
/*    */     
/*    */     public Pair(int _a, int _b) {
/* 51 */       this.a = _a;
/* 52 */       this.b = _b;
/*    */     }
/*    */     
/*    */     public Pair(long data) {
/* 56 */       int a = (int)((data & 0xFFFFFFFF00000000L) >> 32L), b = (int)(data & 0xFFFFFFFFFFFFFFFFL);
/* 57 */       this.a = a;
/* 58 */       this.b = b;
/*    */     }
/*    */     
/*    */     public void exchange() {
/* 62 */       int t = this.a;
/* 63 */       this.a = this.b;
/* 64 */       this.b = t;
/*    */     }
/*    */     
/*    */     public void round(int key) {
/* 68 */       int t = this.a;
/* 69 */       this.a = this.b ^ secretMap(key, this.a);
/* 70 */       this.b = t;
/*    */     }
/*    */     
/*    */     public int secretMap(int x, int y) {
/* 74 */       return x * y ^ 0x2BFBFBFB;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 79 */       return String.format("%08X%08X", new Object[] { Integer.valueOf(this.a), Integer.valueOf(this.b) });
/*    */     }
/*    */     
/*    */     public long getValue() {
/* 83 */       long c = this.a * 1L << 32L;
/* 84 */       long d = c | this.b * 1L & 0xFFFFFFFFL;
/* 85 */       return d;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/XPEncryptGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */