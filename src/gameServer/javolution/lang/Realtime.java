/*     */ package javolution.lang;
/*     */ 
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Documented
/*     */ @Inherited
/*     */ @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ public @interface Realtime
/*     */ {
/*     */   boolean value() default true;
/*     */   
/*     */   Limit limit() default Limit.CONSTANT;
/*     */   
/*     */   String comment() default "";
/*     */   
/*     */   public enum Limit
/*     */   {
/*  86 */     CONSTANT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     LOG_N,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     LINEAR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     N_LOG_N,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     N_SQUARE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     UNKNOWN;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/lang/Realtime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */