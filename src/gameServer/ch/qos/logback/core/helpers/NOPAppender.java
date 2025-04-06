package ch.qos.logback.core.helpers;

import ch.qos.logback.core.AppenderBase;

public final class NOPAppender<E> extends AppenderBase<E> {
  protected void append(E eventObject) {}
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/helpers/NOPAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */