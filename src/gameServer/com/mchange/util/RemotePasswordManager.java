package com.mchange.util;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePasswordManager extends PasswordManager, Remote {
  boolean validate(String paramString1, String paramString2) throws RemoteException, IOException;
  
  boolean updatePassword(String paramString1, String paramString2, String paramString3) throws RemoteException, IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/RemotePasswordManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */