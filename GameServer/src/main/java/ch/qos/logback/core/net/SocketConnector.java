package ch.qos.logback.core.net;

import java.net.Socket;
import java.util.concurrent.Callable;
import javax.net.SocketFactory;

public interface SocketConnector extends Callable<Socket> {
  Socket call() throws InterruptedException;

  void setExceptionHandler(ExceptionHandler paramExceptionHandler);

  void setSocketFactory(SocketFactory paramSocketFactory);

  public static interface ExceptionHandler {
    void connectionFailed(SocketConnector param1SocketConnector, Exception param1Exception);
  }
}

