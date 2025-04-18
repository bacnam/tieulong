package com.mchange.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Checkable extends Remote {
  void check() throws ServiceUnavailableException, RemoteException;
}

