package org.junit.validator;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ValidateWith {
  Class<? extends AnnotationValidator> value();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/validator/ValidateWith.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */