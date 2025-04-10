package javolution.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Realtime
{
boolean value() default true;

Limit limit() default Limit.CONSTANT;

String comment() default "";

public enum Limit
{
CONSTANT,

LOG_N,

LINEAR,

N_LOG_N,

N_SQUARE,

UNKNOWN;
}
}

