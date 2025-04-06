package com.mchange.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallingCard {
  Remote findRemote() throws ServiceUnavailableException, RemoteException;
  
  boolean equals(Object paramObject);
  
  int hashCode();
  
  String toString();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/rmi/CallingCard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */