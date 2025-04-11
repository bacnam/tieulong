package javolution.text;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultTextFormat {
    Class<? extends TextFormat<?>> value();
}

