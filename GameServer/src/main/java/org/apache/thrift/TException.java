

package org.apache.thrift;

public class TException extends Exception {

  private static final long serialVersionUID = 1L;

  public TException() {
    super();
  }

  public TException(String message) {
    super(message);
  }

  public TException(Throwable cause) {
    super(cause);
  }

  public TException(String message, Throwable cause) {
    super(message, cause);
  }
}
