package org.junit.runners.model;

import java.lang.annotation.Annotation;

public interface Annotatable {
  Annotation[] getAnnotations();
  
  <T extends Annotation> T getAnnotation(Class<T> paramClass);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/runners/model/Annotatable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */