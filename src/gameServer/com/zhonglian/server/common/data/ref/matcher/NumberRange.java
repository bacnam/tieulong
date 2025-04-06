/*    */ package com.zhonglian.server.common.data.ref.matcher;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.utils.CommMath;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NumberRange
/*    */ {
/*    */   int from;
/*    */   int to;
/*    */   
/*    */   public NumberRange() {
/* 20 */     this.from = 0;
/* 21 */     this.to = 0;
/*    */   }
/*    */   
/*    */   public NumberRange(int f, int t) {
/* 25 */     this.from = f;
/* 26 */     this.to = t;
/*    */   }
/*    */   
/*    */   public boolean within(int r) {
/* 30 */     return (r >= this.from && (r <= this.to || this.to == -1));
/*    */   }
/*    */   
/*    */   public static NumberRange parse(String data) {
/* 34 */     NumberRange range = new NumberRange();
/* 35 */     if (data == null || "".equals(data)) {
/* 36 */       range.from = 0;
/* 37 */       range.to = 9999;
/*    */     } else {
/* 39 */       range.parseFrom(data);
/*    */     } 
/* 41 */     return range;
/*    */   }
/*    */   
/*    */   public int getLow() {
/* 45 */     return this.from;
/*    */   }
/*    */   
/*    */   public int getTop() {
/* 49 */     return this.to;
/*    */   }
/*    */   
/*    */   public boolean isNarrow() {
/* 53 */     return (this.from == this.to);
/*    */   }
/*    */   
/*    */   public void parseFrom(String data) {
/* 57 */     if (data == null) {
/* 58 */       CommLog.error("NumberRange failed to parse string 'null'");
/*    */       return;
/*    */     } 
/* 61 */     String[] split = data.split("~");
/* 62 */     for (int i = 0; i < split.length; i++) {
/* 63 */       if (split[i].equals(">")) {
/* 64 */         split[i] = String.valueOf(2147483647);
/*    */       }
/*    */     } 
/*    */     
/*    */     try {
/* 69 */       if (split.length == 1) {
/* 70 */         this.from = this.to = Integer.parseInt(data);
/* 71 */       } else if (split.length == 2) {
/* 72 */         int a = Integer.parseInt(split[0]);
/* 73 */         int b = Integer.parseInt(split[1]);
/* 74 */         this.from = Math.min(a, b);
/* 75 */         this.to = Math.max(a, b);
/*    */       } 
/* 77 */     } catch (Exception e) {
/* 78 */       CommLog.error("NumberRange failed to parse string '" + data + "'");
/* 79 */       CommLog.error(e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public int random() {
/* 84 */     return CommMath.randomInt(this.from, this.to);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return "[ " + this.from + " ~ " + this.to + " ]";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/data/ref/matcher/NumberRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */