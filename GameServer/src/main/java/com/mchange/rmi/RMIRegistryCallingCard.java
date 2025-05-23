package com.mchange.rmi;

import com.mchange.io.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class RMIRegistryCallingCard
implements CallingCard, Serializable
{
transient String url;
static final long serialVersionUID = 1L;
transient Remote cached = null;

private static final short VERSION = 1;

public RMIRegistryCallingCard(String paramString1, int paramInt, String paramString2) {
this.url = "
}
public RMIRegistryCallingCard(String paramString1, String paramString2) {
this(paramString1, 1099, paramString2);
}
public boolean equals(Object paramObject) {
return (paramObject instanceof RMIRegistryCallingCard && this.url.equals(((RMIRegistryCallingCard)paramObject).url));
}
public int hashCode() {
return this.url.hashCode();
}

public Remote findRemote() throws ServiceUnavailableException, RemoteException {
if (this.cached instanceof Checkable) {

try {

((Checkable)this.cached).check();
return this.cached;
}
catch (RemoteException remoteException) {

this.cached = null;
return findRemote();
} 
}

try {
Remote remote = Naming.lookup(this.url);
if (remote instanceof Checkable)
this.cached = remote; 
return remote;
}
catch (NotBoundException notBoundException) {
throw new ServiceUnavailableException("Object Not Bound: " + this.url);
} catch (MalformedURLException malformedURLException) {
throw new ServiceUnavailableException("Uh oh. Bad url. It never will be available: " + this.url);
} 
}

public String toString() {
return super.toString() + " [" + this.url + "];";
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);

paramObjectOutputStream.writeUTF(this.url);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.url = paramObjectInputStream.readUTF();
return;
} 
throw new UnsupportedVersionException(getClass().getName() + "; Bad version: " + s);
}
}

