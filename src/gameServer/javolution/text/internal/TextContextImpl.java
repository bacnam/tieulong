/*     */ package javolution.text.internal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import javolution.context.AbstractContext;
/*     */ import javolution.context.LogContext;
/*     */ import javolution.text.CharSet;
/*     */ import javolution.text.Cursor;
/*     */ import javolution.text.DefaultTextFormat;
/*     */ import javolution.text.TextContext;
/*     */ import javolution.text.TextFormat;
/*     */ import javolution.text.TypeFormat;
/*     */ import javolution.util.FastMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TextContextImpl
/*     */   extends TextContext
/*     */ {
/*  39 */   private static final TextFormat<?> OBJECT_FORMAT = new TextFormat<Object>() {
/*  40 */       ThreadLocal<Object> objToString = new ThreadLocal();
/*     */ 
/*     */       
/*     */       public Appendable format(Object obj, Appendable dest) throws IOException {
/*  44 */         if (obj == null) return dest.append("null"); 
/*  45 */         if (this.objToString.get() == obj) {
/*  46 */           return TypeFormat.format(System.identityHashCode(obj), dest.append("Object#"));
/*     */         }
/*  48 */         this.objToString.set(obj);
/*     */         try {
/*  50 */           String str = obj.toString();
/*  51 */           return dest.append(str);
/*     */         } finally {
/*  53 */           this.objToString.set(null);
/*     */         } 
/*     */       }
/*     */       
/*     */       public Object parse(CharSequence csq, Cursor cursor) {
/*  58 */         throw new UnsupportedOperationException("Generic object parsing not supported.");
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private final FastMap<Class<?>, TextFormat<?>> localFormats;
/*     */ 
/*     */   
/*     */   private final FastMap<Class<?>, TextFormat<?>> defaultFormats;
/*     */ 
/*     */ 
/*     */   
/*     */   public TextContextImpl() {
/*  71 */     this.localFormats = new FastMap();
/*  72 */     this.defaultFormats = (new FastMap()).shared();
/*  73 */     storePrimitiveTypesFormats();
/*     */   }
/*     */ 
/*     */   
/*     */   public TextContextImpl(TextContextImpl parent) {
/*  78 */     this.localFormats = (new FastMap()).putAll(parent.localFormats);
/*     */     
/*  80 */     this.defaultFormats = parent.defaultFormats;
/*     */   }
/*     */ 
/*     */   
/*     */   protected TextContext inner() {
/*  85 */     return new TextContextImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> TextFormat<T> searchFormat(Class<? extends T> type) {
/*  91 */     Class<?> cls = type;
/*  92 */     while (cls != null) {
/*     */ 
/*     */       
/*  95 */       if (this.localFormats.size() > 0) {
/*  96 */         TextFormat<?> textFormat = (TextFormat)this.localFormats.get(cls);
/*  97 */         if (textFormat != null) return (TextFormat)textFormat;
/*     */       
/*     */       } 
/* 100 */       TextFormat<?> format = (TextFormat)this.defaultFormats.get(cls);
/* 101 */       if (format != null) return (TextFormat)format;
/*     */ 
/*     */       
/* 104 */       DefaultTextFormat annotation = cls.<DefaultTextFormat>getAnnotation(DefaultTextFormat.class);
/*     */       
/* 106 */       if (annotation != null) {
/*     */         try {
/* 108 */           format = annotation.value().newInstance();
/* 109 */         } catch (Throwable error) {
/* 110 */           LogContext.warning(new Object[] { error });
/*     */         } 
/*     */         
/* 113 */         Class<?> mappedClass = type;
/*     */         while (true) {
/* 115 */           this.defaultFormats.put(mappedClass, format);
/* 116 */           if (mappedClass.equals(cls))
/* 117 */             break;  mappedClass = mappedClass.getSuperclass();
/*     */         } 
/* 119 */         return (TextFormat)format;
/*     */       } 
/*     */ 
/*     */       
/* 123 */       cls = cls.getSuperclass();
/*     */     } 
/* 125 */     throw new Error("Object default format not found !");
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void setFormat(Class<? extends T> type, TextFormat<T> format) {
/* 130 */     this.localFormats.put(type, format);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void storePrimitiveTypesFormats() {
/* 138 */     this.defaultFormats.put(Object.class, OBJECT_FORMAT);
/* 139 */     this.defaultFormats.put(Boolean.class, new TextFormat<Boolean>()
/*     */         {
/*     */           public Appendable format(Boolean obj, Appendable dest) throws IOException
/*     */           {
/* 143 */             return TypeFormat.format(obj.booleanValue(), dest);
/*     */           }
/*     */           
/*     */           public Boolean parse(CharSequence csq, Cursor cursor) {
/* 147 */             return Boolean.valueOf(TypeFormat.parseBoolean(csq, cursor));
/*     */           }
/*     */         });
/*     */     
/* 151 */     this.defaultFormats.put(Character.class, new TextFormat<Character>()
/*     */         {
/*     */           public Appendable format(Character obj, Appendable dest) throws IOException
/*     */           {
/* 155 */             return dest.append(obj.charValue());
/*     */           }
/*     */           
/*     */           public Character parse(CharSequence csq, Cursor cursor) {
/* 159 */             return Character.valueOf(cursor.nextChar(csq));
/*     */           }
/*     */         });
/*     */     
/* 163 */     this.defaultFormats.put(Byte.class, new TextFormat<Byte>()
/*     */         {
/*     */           public Appendable format(Byte obj, Appendable dest) throws IOException
/*     */           {
/* 167 */             return TypeFormat.format(obj.byteValue(), dest);
/*     */           }
/*     */           
/*     */           public Byte parse(CharSequence csq, Cursor cursor) {
/* 171 */             return Byte.valueOf(TypeFormat.parseByte(csq, 10, cursor));
/*     */           }
/*     */         });
/*     */     
/* 175 */     this.defaultFormats.put(Short.class, new TextFormat<Short>()
/*     */         {
/*     */           public Appendable format(Short obj, Appendable dest) throws IOException
/*     */           {
/* 179 */             return TypeFormat.format(obj.shortValue(), dest);
/*     */           }
/*     */           
/*     */           public Short parse(CharSequence csq, Cursor cursor) {
/* 183 */             return Short.valueOf(TypeFormat.parseShort(csq, 10, cursor));
/*     */           }
/*     */         });
/*     */     
/* 187 */     this.defaultFormats.put(Integer.class, new TextFormat<Integer>()
/*     */         {
/*     */           public Appendable format(Integer obj, Appendable dest) throws IOException
/*     */           {
/* 191 */             return TypeFormat.format(obj.intValue(), dest);
/*     */           }
/*     */           
/*     */           public Integer parse(CharSequence csq, Cursor cursor) {
/* 195 */             return Integer.valueOf(TypeFormat.parseInt(csq, 10, cursor));
/*     */           }
/*     */         });
/*     */     
/* 199 */     this.defaultFormats.put(Long.class, new TextFormat<Long>()
/*     */         {
/*     */           public Appendable format(Long obj, Appendable dest) throws IOException
/*     */           {
/* 203 */             return TypeFormat.format(obj.longValue(), dest);
/*     */           }
/*     */           
/*     */           public Long parse(CharSequence csq, Cursor cursor) {
/* 207 */             return Long.valueOf(TypeFormat.parseLong(csq, 10, cursor));
/*     */           }
/*     */         });
/*     */     
/* 211 */     this.defaultFormats.put(Float.class, new TextFormat<Float>()
/*     */         {
/*     */           public Appendable format(Float obj, Appendable dest) throws IOException
/*     */           {
/* 215 */             return TypeFormat.format(obj.floatValue(), dest);
/*     */           }
/*     */           
/*     */           public Float parse(CharSequence csq, Cursor cursor) {
/* 219 */             return new Float(TypeFormat.parseFloat(csq, cursor));
/*     */           }
/*     */         });
/*     */     
/* 223 */     this.defaultFormats.put(Double.class, new TextFormat<Double>()
/*     */         {
/*     */           public Appendable format(Double obj, Appendable dest) throws IOException
/*     */           {
/* 227 */             return TypeFormat.format(obj.doubleValue(), dest);
/*     */           }
/*     */           
/*     */           public Double parse(CharSequence csq, Cursor cursor) {
/* 231 */             return new Double(TypeFormat.parseDouble(csq, cursor));
/*     */           }
/*     */         });
/*     */     
/* 235 */     this.defaultFormats.put(String.class, new TextFormat<String>()
/*     */         {
/*     */           public Appendable format(String obj, Appendable dest) throws IOException
/*     */           {
/* 239 */             return dest.append(obj);
/*     */           }
/*     */           
/*     */           public String parse(CharSequence csq, Cursor cursor) {
/* 243 */             CharSequence tmp = csq.subSequence(cursor.getIndex(), csq.length());
/*     */             
/* 245 */             cursor.setIndex(csq.length());
/* 246 */             return tmp.toString();
/*     */           }
/*     */         });
/*     */     
/* 250 */     this.defaultFormats.put(Class.class, new TextFormat<Class<?>>()
/*     */         {
/*     */           public Appendable format(Class<?> obj, Appendable dest) throws IOException
/*     */           {
/* 254 */             return dest.append(obj.getName());
/*     */           }
/*     */           
/*     */           public Class<?> parse(CharSequence csq, Cursor cursor) {
/* 258 */             CharSequence name = cursor.nextToken(csq, CharSet.WHITESPACES);
/*     */             try {
/* 260 */               return Class.forName(name.toString());
/* 261 */             } catch (ClassNotFoundException e) {
/* 262 */               throw new IllegalArgumentException("Class " + name + " Not Found");
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 268 */     this.defaultFormats.put(Date.class, new TextFormat<Date>()
/*     */         {
/*     */           TimeZone tz;
/*     */ 
/*     */           
/*     */           DateFormat df;
/*     */ 
/*     */           
/*     */           public Appendable format(Date obj, Appendable dest) throws IOException {
/* 277 */             return dest.append(this.df.format(obj));
/*     */           }
/*     */           
/*     */           public Date parse(CharSequence csq, Cursor cursor) {
/* 281 */             CharSequence date = cursor.nextToken(csq, CharSet.WHITESPACES);
/*     */             try {
/* 283 */               return this.df.parse(date.toString());
/* 284 */             } catch (ParseException error) {
/* 285 */               throw new IllegalArgumentException(error);
/*     */             } 
/*     */           }
/*     */         });
/* 289 */     this.defaultFormats.put(BigInteger.class, new TextFormat<BigInteger>()
/*     */         {
/*     */           public Appendable format(BigInteger obj, Appendable dest) throws IOException
/*     */           {
/* 293 */             return dest.append(obj.toString());
/*     */           }
/*     */           
/*     */           public BigInteger parse(CharSequence csq, Cursor cursor) {
/* 297 */             CharSequence value = cursor.nextToken(csq, CharSet.WHITESPACES);
/* 298 */             return new BigInteger(value.toString());
/*     */           }
/*     */         });
/*     */     
/* 302 */     this.defaultFormats.put(BigDecimal.class, new TextFormat<BigDecimal>()
/*     */         {
/*     */           public Appendable format(BigDecimal obj, Appendable dest) throws IOException
/*     */           {
/* 306 */             return dest.append(obj.toString());
/*     */           }
/*     */           
/*     */           public BigDecimal parse(CharSequence csq, Cursor cursor) {
/* 310 */             CharSequence value = cursor.nextToken(csq, CharSet.WHITESPACES);
/* 311 */             return new BigDecimal(value.toString());
/*     */           }
/*     */         });
/*     */     
/* 315 */     this.defaultFormats.put(Font.class, new TextFormat<Font>()
/*     */         {
/*     */           public Appendable format(Font obj, Appendable dest) throws IOException
/*     */           {
/* 319 */             return dest.append(obj.getName());
/*     */           }
/*     */           
/*     */           public Font parse(CharSequence csq, Cursor cursor) {
/* 323 */             CharSequence name = cursor.nextToken(csq, CharSet.WHITESPACES);
/* 324 */             return Font.decode(name.toString());
/*     */           }
/*     */         });
/*     */     
/* 328 */     this.defaultFormats.put(Color.class, new TextFormat<Color>()
/*     */         {
/*     */           public Appendable format(Color obj, Appendable dest) throws IOException
/*     */           {
/* 332 */             return dest.append('#').append(Integer.toHexString(obj.getRGB()));
/*     */           }
/*     */ 
/*     */           
/*     */           public Color parse(CharSequence csq, Cursor cursor) {
/* 337 */             CharSequence name = cursor.nextToken(csq, CharSet.WHITESPACES);
/* 338 */             return Color.decode(name.toString());
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/internal/TextContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */