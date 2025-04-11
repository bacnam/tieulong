package org.junit.experimental.categories;

import org.junit.validator.ValidateWith;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ValidateWith(CategoryValidator.class)
public @interface Category {
    Class<?>[] value();
}

