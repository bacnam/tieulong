package com.mchange.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Checkable extends Remote {
  void check() throws ServiceUnavailableException, RemoteException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/rmi/Checkable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */