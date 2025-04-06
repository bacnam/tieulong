/*     */ package javolution.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectStreamException;
/*     */ import javolution.lang.Configurable;
/*     */ import javolution.lang.MathLib;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.lang.ValueType;
/*     */ import javolution.text.Cursor;
/*     */ import javolution.text.DefaultTextFormat;
/*     */ import javolution.text.TextContext;
/*     */ import javolution.text.TextFormat;
/*     */ import javolution.text.TypeFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Realtime
/*     */ @DefaultTextFormat(Index.Decimal.class)
/*     */ public final class Index
/*     */   extends Number
/*     */   implements Comparable<Index>, ValueType<Index>
/*     */ {
/*     */   public static class Decimal
/*     */     extends TextFormat<Index>
/*     */   {
/*     */     public Appendable format(Index obj, Appendable dest) throws IOException {
/*  53 */       return TypeFormat.format(obj.intValue(), dest);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Index parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
/*  59 */       return Index.valueOf(TypeFormat.parseInt(csq, cursor));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final Configurable<Integer> UNIQUE = new Configurable<Integer>()
/*     */     {
/*     */       protected Integer getDefault()
/*     */       {
/*  73 */         return Integer.valueOf(1024);
/*     */       }
/*     */ 
/*     */       
/*     */       protected Integer initialized(Integer value) {
/*  78 */         return Integer.valueOf(MathLib.min(value.intValue(), 65536));
/*     */       }
/*     */ 
/*     */       
/*     */       protected Integer reconfigured(Integer oldCount, Integer newCount) {
/*  83 */         throw new UnsupportedOperationException("Unicity reconfiguration not supported.");
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final Index ZERO = new Index(0);
/*     */   
/*     */   private static final long serialVersionUID = 1536L;
/*  94 */   private static final Index[] INSTANCES = new Index[((Integer)UNIQUE.get()).intValue()];
/*     */   static {
/*  96 */     INSTANCES[0] = ZERO;
/*  97 */     for (int i = 1; i < INSTANCES.length; i++) {
/*  98 */       INSTANCES[i] = new Index(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int value;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Index valueOf(int value) {
/* 112 */     return (value < INSTANCES.length) ? INSTANCES[value] : new Index(value);
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
/*     */   private Index(int value) {
/* 124 */     this.value = value;
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
/*     */   public int compareTo(Index that) {
/* 137 */     return this.value - that.value;
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
/*     */   public int compareTo(int value) {
/* 150 */     return this.value - value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Index copy() {
/* 158 */     return (this.value < INSTANCES.length) ? this : new Index(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 167 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 176 */     return (this.value < INSTANCES.length) ? ((this == obj)) : ((obj instanceof Index) ? ((((Index)obj).value == this.value)) : false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 187 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 195 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 204 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/* 213 */     return (this == ZERO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 222 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Index next() {
/* 229 */     return valueOf(this.value + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Index previous() {
/* 238 */     return valueOf(this.value - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object readResolve() throws ObjectStreamException {
/* 245 */     return valueOf(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 255 */     return TextContext.getFormat(Index.class).format(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Index value() {
/* 260 */     return this;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/Index.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */