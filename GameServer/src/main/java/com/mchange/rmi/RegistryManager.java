package com.mchange.rmi;

import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegistryManager
{
public static Registry ensureRegistry(int paramInt) throws RemoteException {
Registry registry = findRegistry(paramInt);
if (registry == null) registry = LocateRegistry.createRegistry(paramInt); 
return registry;
}

public static Registry ensureRegistry() throws RemoteException {
return ensureRegistry(1099);
}

public static boolean registryAvailable(int paramInt) throws RemoteException, AccessException {
try {
Registry registry = LocateRegistry.getRegistry(paramInt);
registry.list();
return true;
}
catch (ConnectException connectException) {
return false;
} 
}
public static boolean registryAvailable() throws RemoteException, AccessException {
return registryAvailable(1099);
}

public static Registry findRegistry(int paramInt) throws RemoteException, AccessException {
if (!registryAvailable(paramInt)) return null; 
return LocateRegistry.getRegistry(paramInt);
}

public static Registry findRegistry() throws RemoteException, AccessException {
return findRegistry(1099);
}
}

