package ch.qos.logback.core.spi;

import ch.qos.logback.core.Appender;
import java.util.Iterator;

public interface AppenderAttachable<E> {
  void addAppender(Appender<E> paramAppender);

  Iterator<Appender<E>> iteratorForAppenders();

  Appender<E> getAppender(String paramString);

  boolean isAttached(Appender<E> paramAppender);

  void detachAndStopAllAppenders();

  boolean detachAppender(Appender<E> paramAppender);

  boolean detachAppender(String paramString);
}

