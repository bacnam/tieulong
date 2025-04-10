package javolution.text.internal;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javolution.context.AbstractContext;
import javolution.context.LogContext;
import javolution.text.CharSet;
import javolution.text.Cursor;
import javolution.text.DefaultTextFormat;
import javolution.text.TextContext;
import javolution.text.TextFormat;
import javolution.text.TypeFormat;
import javolution.util.FastMap;

public final class TextContextImpl
extends TextContext
{
private static final TextFormat<?> OBJECT_FORMAT = new TextFormat<Object>() {
ThreadLocal<Object> objToString = new ThreadLocal();

public Appendable format(Object obj, Appendable dest) throws IOException {
if (obj == null) return dest.append("null"); 
if (this.objToString.get() == obj) {
return TypeFormat.format(System.identityHashCode(obj), dest.append("Object#"));
}
this.objToString.set(obj);
try {
String str = obj.toString();
return dest.append(str);
} finally {
this.objToString.set(null);
} 
}

public Object parse(CharSequence csq, Cursor cursor) {
throw new UnsupportedOperationException("Generic object parsing not supported.");
}
};

private final FastMap<Class<?>, TextFormat<?>> localFormats;

private final FastMap<Class<?>, TextFormat<?>> defaultFormats;

public TextContextImpl() {
this.localFormats = new FastMap();
this.defaultFormats = (new FastMap()).shared();
storePrimitiveTypesFormats();
}

public TextContextImpl(TextContextImpl parent) {
this.localFormats = (new FastMap()).putAll(parent.localFormats);

this.defaultFormats = parent.defaultFormats;
}

protected TextContext inner() {
return new TextContextImpl(this);
}

protected <T> TextFormat<T> searchFormat(Class<? extends T> type) {
Class<?> cls = type;
while (cls != null) {

if (this.localFormats.size() > 0) {
TextFormat<?> textFormat = (TextFormat)this.localFormats.get(cls);
if (textFormat != null) return (TextFormat)textFormat;

} 
TextFormat<?> format = (TextFormat)this.defaultFormats.get(cls);
if (format != null) return (TextFormat)format;

DefaultTextFormat annotation = cls.<DefaultTextFormat>getAnnotation(DefaultTextFormat.class);

if (annotation != null) {
try {
format = annotation.value().newInstance();
} catch (Throwable error) {
LogContext.warning(new Object[] { error });
} 

Class<?> mappedClass = type;
while (true) {
this.defaultFormats.put(mappedClass, format);
if (mappedClass.equals(cls))
break;  mappedClass = mappedClass.getSuperclass();
} 
return (TextFormat)format;
} 

cls = cls.getSuperclass();
} 
throw new Error("Object default format not found !");
}

public <T> void setFormat(Class<? extends T> type, TextFormat<T> format) {
this.localFormats.put(type, format);
}

private void storePrimitiveTypesFormats() {
this.defaultFormats.put(Object.class, OBJECT_FORMAT);
this.defaultFormats.put(Boolean.class, new TextFormat<Boolean>()
{
public Appendable format(Boolean obj, Appendable dest) throws IOException
{
return TypeFormat.format(obj.booleanValue(), dest);
}

public Boolean parse(CharSequence csq, Cursor cursor) {
return Boolean.valueOf(TypeFormat.parseBoolean(csq, cursor));
}
});

this.defaultFormats.put(Character.class, new TextFormat<Character>()
{
public Appendable format(Character obj, Appendable dest) throws IOException
{
return dest.append(obj.charValue());
}

public Character parse(CharSequence csq, Cursor cursor) {
return Character.valueOf(cursor.nextChar(csq));
}
});

this.defaultFormats.put(Byte.class, new TextFormat<Byte>()
{
public Appendable format(Byte obj, Appendable dest) throws IOException
{
return TypeFormat.format(obj.byteValue(), dest);
}

public Byte parse(CharSequence csq, Cursor cursor) {
return Byte.valueOf(TypeFormat.parseByte(csq, 10, cursor));
}
});

this.defaultFormats.put(Short.class, new TextFormat<Short>()
{
public Appendable format(Short obj, Appendable dest) throws IOException
{
return TypeFormat.format(obj.shortValue(), dest);
}

public Short parse(CharSequence csq, Cursor cursor) {
return Short.valueOf(TypeFormat.parseShort(csq, 10, cursor));
}
});

this.defaultFormats.put(Integer.class, new TextFormat<Integer>()
{
public Appendable format(Integer obj, Appendable dest) throws IOException
{
return TypeFormat.format(obj.intValue(), dest);
}

public Integer parse(CharSequence csq, Cursor cursor) {
return Integer.valueOf(TypeFormat.parseInt(csq, 10, cursor));
}
});

this.defaultFormats.put(Long.class, new TextFormat<Long>()
{
public Appendable format(Long obj, Appendable dest) throws IOException
{
return TypeFormat.format(obj.longValue(), dest);
}

public Long parse(CharSequence csq, Cursor cursor) {
return Long.valueOf(TypeFormat.parseLong(csq, 10, cursor));
}
});

this.defaultFormats.put(Float.class, new TextFormat<Float>()
{
public Appendable format(Float obj, Appendable dest) throws IOException
{
return TypeFormat.format(obj.floatValue(), dest);
}

public Float parse(CharSequence csq, Cursor cursor) {
return new Float(TypeFormat.parseFloat(csq, cursor));
}
});

this.defaultFormats.put(Double.class, new TextFormat<Double>()
{
public Appendable format(Double obj, Appendable dest) throws IOException
{
return TypeFormat.format(obj.doubleValue(), dest);
}

public Double parse(CharSequence csq, Cursor cursor) {
return new Double(TypeFormat.parseDouble(csq, cursor));
}
});

this.defaultFormats.put(String.class, new TextFormat<String>()
{
public Appendable format(String obj, Appendable dest) throws IOException
{
return dest.append(obj);
}

public String parse(CharSequence csq, Cursor cursor) {
CharSequence tmp = csq.subSequence(cursor.getIndex(), csq.length());

cursor.setIndex(csq.length());
return tmp.toString();
}
});

this.defaultFormats.put(Class.class, new TextFormat<Class<?>>()
{
public Appendable format(Class<?> obj, Appendable dest) throws IOException
{
return dest.append(obj.getName());
}

public Class<?> parse(CharSequence csq, Cursor cursor) {
CharSequence name = cursor.nextToken(csq, CharSet.WHITESPACES);
try {
return Class.forName(name.toString());
} catch (ClassNotFoundException e) {
throw new IllegalArgumentException("Class " + name + " Not Found");
} 
}
});

this.defaultFormats.put(Date.class, new TextFormat<Date>()
{
TimeZone tz;

DateFormat df;

public Appendable format(Date obj, Appendable dest) throws IOException {
return dest.append(this.df.format(obj));
}

public Date parse(CharSequence csq, Cursor cursor) {
CharSequence date = cursor.nextToken(csq, CharSet.WHITESPACES);
try {
return this.df.parse(date.toString());
} catch (ParseException error) {
throw new IllegalArgumentException(error);
} 
}
});
this.defaultFormats.put(BigInteger.class, new TextFormat<BigInteger>()
{
public Appendable format(BigInteger obj, Appendable dest) throws IOException
{
return dest.append(obj.toString());
}

public BigInteger parse(CharSequence csq, Cursor cursor) {
CharSequence value = cursor.nextToken(csq, CharSet.WHITESPACES);
return new BigInteger(value.toString());
}
});

this.defaultFormats.put(BigDecimal.class, new TextFormat<BigDecimal>()
{
public Appendable format(BigDecimal obj, Appendable dest) throws IOException
{
return dest.append(obj.toString());
}

public BigDecimal parse(CharSequence csq, Cursor cursor) {
CharSequence value = cursor.nextToken(csq, CharSet.WHITESPACES);
return new BigDecimal(value.toString());
}
});

this.defaultFormats.put(Font.class, new TextFormat<Font>()
{
public Appendable format(Font obj, Appendable dest) throws IOException
{
return dest.append(obj.getName());
}

public Font parse(CharSequence csq, Cursor cursor) {
CharSequence name = cursor.nextToken(csq, CharSet.WHITESPACES);
return Font.decode(name.toString());
}
});

this.defaultFormats.put(Color.class, new TextFormat<Color>()
{
public Appendable format(Color obj, Appendable dest) throws IOException
{
return dest.append('#').append(Integer.toHexString(obj.getRGB()));
}

public Color parse(CharSequence csq, Cursor cursor) {
CharSequence name = cursor.nextToken(csq, CharSet.WHITESPACES);
return Color.decode(name.toString());
}
});
}
}

