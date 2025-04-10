package org.apache.http.nio.reactor;

public interface SessionRequestCallback {
  void completed(SessionRequest paramSessionRequest);

  void failed(SessionRequest paramSessionRequest);

  void timeout(SessionRequest paramSessionRequest);

  void cancelled(SessionRequest paramSessionRequest);
}

