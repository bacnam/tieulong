package javolution.xml;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultXMLFormat {
    Class<? extends XMLFormat<?>> value();
}

