package com.mchange.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallingCard {
  Remote findRemote() throws ServiceUnavailableException, RemoteException;

  boolean equals(Object paramObject);

  int hashCode();

  String toString();
}

