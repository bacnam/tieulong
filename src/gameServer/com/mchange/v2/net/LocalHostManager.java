package com.mchange.v2.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class LocalHostManager
{
Set localAddresses;
Set knownGoodNames;
Set knownBadNames;

public synchronized void update() throws SocketException {
HashSet<?> hashSet = new HashSet();
Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
while (enumeration.hasMoreElements()) {

NetworkInterface networkInterface = enumeration.nextElement();
Enumeration<InetAddress> enumeration1 = networkInterface.getInetAddresses();
while (enumeration1.hasMoreElements())
hashSet.add(enumeration1.nextElement()); 
} 
this.localAddresses = Collections.unmodifiableSet(hashSet);
this.knownGoodNames = new HashSet();
this.knownBadNames = new HashSet();
}

public synchronized Set getLocalAddresses() {
return this.localAddresses;
}
public synchronized boolean isLocalAddress(InetAddress paramInetAddress) {
return this.localAddresses.contains(paramInetAddress);
}

public synchronized boolean isLocalHostName(String paramString) {
if (this.knownGoodNames.contains(paramString))
return true; 
if (this.knownGoodNames.contains(paramString)) {
return false;
}

try {
InetAddress inetAddress = InetAddress.getByName(paramString);
if (this.localAddresses.contains(inetAddress)) {

this.knownGoodNames.add(paramString);
return true;
} 

this.knownBadNames.add(paramString);
return false;

}
catch (UnknownHostException unknownHostException) {

this.knownBadNames.add(paramString);
return false;
} 
}

public LocalHostManager() throws SocketException {
update();
}

public static void main(String[] paramArrayOfString) {
try {
LocalHostManager localHostManager = new LocalHostManager();
System.out.println(localHostManager.getLocalAddresses());
}
catch (Exception exception) {
exception.printStackTrace();
} 
}
}

