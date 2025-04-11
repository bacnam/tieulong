package javolution.lang;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Parallelizable {
    boolean value() default true;

    boolean mutexFree() default true;

    String comment() default "";
}

