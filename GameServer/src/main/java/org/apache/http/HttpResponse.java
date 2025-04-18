package org.apache.http;

import java.util.Locale;

public interface HttpResponse extends HttpMessage {
  StatusLine getStatusLine();

  void setStatusLine(StatusLine paramStatusLine);

  void setStatusLine(ProtocolVersion paramProtocolVersion, int paramInt);

  void setStatusLine(ProtocolVersion paramProtocolVersion, int paramInt, String paramString);

  void setStatusCode(int paramInt) throws IllegalStateException;

  void setReasonPhrase(String paramString) throws IllegalStateException;

  HttpEntity getEntity();

  void setEntity(HttpEntity paramHttpEntity);

  Locale getLocale();

  void setLocale(Locale paramLocale);
}

