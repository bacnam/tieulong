package org.apache.mina.core.service;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.Set;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.util.IdentityHashSet;

public class DefaultTransportMetadata
implements TransportMetadata
{
private final String providerName;
private final String name;
private final boolean connectionless;
private final boolean fragmentation;
private final Class<? extends SocketAddress> addressType;
private final Class<? extends IoSessionConfig> sessionConfigType;
private final Set<Class<? extends Object>> envelopeTypes;

public DefaultTransportMetadata(String providerName, String name, boolean connectionless, boolean fragmentation, Class<? extends SocketAddress> addressType, Class<? extends IoSessionConfig> sessionConfigType, Class<?>... envelopeTypes) {
if (providerName == null) {
throw new IllegalArgumentException("providerName");
}
if (name == null) {
throw new IllegalArgumentException("name");
}

providerName = providerName.trim().toLowerCase();
if (providerName.length() == 0) {
throw new IllegalArgumentException("providerName is empty.");
}
name = name.trim().toLowerCase();
if (name.length() == 0) {
throw new IllegalArgumentException("name is empty.");
}

if (addressType == null) {
throw new IllegalArgumentException("addressType");
}

if (envelopeTypes == null) {
throw new IllegalArgumentException("envelopeTypes");
}

if (envelopeTypes.length == 0) {
throw new IllegalArgumentException("envelopeTypes is empty.");
}

if (sessionConfigType == null) {
throw new IllegalArgumentException("sessionConfigType");
}

this.providerName = providerName;
this.name = name;
this.connectionless = connectionless;
this.fragmentation = fragmentation;
this.addressType = addressType;
this.sessionConfigType = sessionConfigType;

IdentityHashSet<Class<? extends Object>> identityHashSet = new IdentityHashSet();
for (Class<? extends Object> c : envelopeTypes) {
identityHashSet.add(c);
}
this.envelopeTypes = Collections.unmodifiableSet((Set<? extends Class<? extends Object>>)identityHashSet);
}

public Class<? extends SocketAddress> getAddressType() {
return this.addressType;
}

public Set<Class<? extends Object>> getEnvelopeTypes() {
return this.envelopeTypes;
}

public Class<? extends IoSessionConfig> getSessionConfigType() {
return this.sessionConfigType;
}

public String getProviderName() {
return this.providerName;
}

public String getName() {
return this.name;
}

public boolean isConnectionless() {
return this.connectionless;
}

public boolean hasFragmentation() {
return this.fragmentation;
}

public String toString() {
return this.name;
}
}

