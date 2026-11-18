package com.zhonglian.server.common.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBaseField {
    String type();

    int size() default 0;

    String fieldname() default "";

    String comment();

    DataBaseField.IndexType indextype() default DataBaseField.IndexType.None;

    public static enum IndexType {
        None,
        Unique,
        Normal;
    }
}
