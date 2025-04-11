package com.zhonglian.server.common.db.annotation;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBaseField {
    String type();

    int size() default 0;

    String fieldname() default "";

    String comment();

    IndexType indextype() default IndexType.None;

    public enum IndexType {
        None,
        Unique,
        Normal;
    }
}

