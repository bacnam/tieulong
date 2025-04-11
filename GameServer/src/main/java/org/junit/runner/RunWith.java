package org.junit.runner;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RunWith {
    Class<? extends Runner> value();
}

