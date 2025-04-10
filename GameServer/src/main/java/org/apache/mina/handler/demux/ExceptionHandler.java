package org.apache.mina.handler.demux;

import org.apache.mina.core.session.IoSession;

public interface ExceptionHandler<E extends Throwable>
{
public static final ExceptionHandler<Throwable> NOOP = new ExceptionHandler<Throwable>()
{
public void exceptionCaught(IoSession session, Throwable cause) {}
};

public static final ExceptionHandler<Throwable> CLOSE = new ExceptionHandler<Throwable>() {
public void exceptionCaught(IoSession session, Throwable cause) {
session.close(true);
}
};

void exceptionCaught(IoSession paramIoSession, E paramE) throws Exception;
}

