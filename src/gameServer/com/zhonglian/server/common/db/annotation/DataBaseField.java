/*    */ package com.zhonglian.server.common.db.annotation;@Target({ElementType.FIELD})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ public @interface DataBaseField { String type();
/*    */   
/*    */   int size() default 0;
/*    */   
/*    */   String fieldname() default "";
/*    */   
/*    */   String comment();
/*    */   
/*    */   IndexType indextype() default IndexType.None;
/*    */   
/* 13 */   public enum IndexType { None,
/* 14 */     Unique,
/* 15 */     Normal; }
/*    */    }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/annotation/DataBaseField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */