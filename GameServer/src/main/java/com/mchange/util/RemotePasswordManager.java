package com.mchange.util;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePasswordManager extends PasswordManager, Remote {
  boolean validate(String paramString1, String paramString2) throws RemoteException, IOException;

  boolean updatePassword(String paramString1, String paramString2, String paramString3) throws RemoteException, IOException;
}

