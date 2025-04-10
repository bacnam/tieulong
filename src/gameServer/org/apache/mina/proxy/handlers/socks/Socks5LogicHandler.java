package org.apache.mina.proxy.handlers.socks;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.proxy.session.ProxyIoSession;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Socks5LogicHandler
extends AbstractSocksLogicHandler
{
private static final Logger LOGGER = LoggerFactory.getLogger(Socks5LogicHandler.class);

private static final String SELECTED_AUTH_METHOD = Socks5LogicHandler.class.getName() + ".SelectedAuthMethod";

private static final String HANDSHAKE_STEP = Socks5LogicHandler.class.getName() + ".HandshakeStep";

private static final String GSS_CONTEXT = Socks5LogicHandler.class.getName() + ".GSSContext";

private static final String GSS_TOKEN = Socks5LogicHandler.class.getName() + ".GSSToken";

public Socks5LogicHandler(ProxyIoSession proxyIoSession) {
super(proxyIoSession);
getSession().setAttribute(HANDSHAKE_STEP, Integer.valueOf(0));
}

public synchronized void doHandshake(IoFilter.NextFilter nextFilter) {
LOGGER.debug(" doHandshake()");

writeRequest(nextFilter, this.request, ((Integer)getSession().getAttribute(HANDSHAKE_STEP)).intValue());
}

private IoBuffer encodeInitialGreetingPacket(SocksProxyRequest request) {
byte nbMethods = (byte)SocksProxyConstants.SUPPORTED_AUTH_METHODS.length;
IoBuffer buf = IoBuffer.allocate(2 + nbMethods);

buf.put(request.getProtocolVersion());
buf.put(nbMethods);
buf.put(SocksProxyConstants.SUPPORTED_AUTH_METHODS);

return buf;
}

private IoBuffer encodeProxyRequestPacket(SocksProxyRequest request) throws UnsupportedEncodingException {
int len = 6;
InetSocketAddress adr = request.getEndpointAddress();
byte addressType = 0;
byte[] host = null;

if (adr != null && !adr.isUnresolved()) {
if (adr.getAddress() instanceof java.net.Inet6Address) {
len += 16;
addressType = 4;
} else if (adr.getAddress() instanceof java.net.Inet4Address) {
len += 4;
addressType = 1;
} 
} else {
host = (request.getHost() != null) ? request.getHost().getBytes("ASCII") : null;

if (host != null) {
len += 1 + host.length;
addressType = 3;
} else {
throw new IllegalArgumentException("SocksProxyRequest object has no suitable endpoint information");
} 
} 

IoBuffer buf = IoBuffer.allocate(len);

buf.put(request.getProtocolVersion());
buf.put(request.getCommandCode());
buf.put((byte)0);
buf.put(addressType);

if (host == null) {
buf.put(request.getIpAddress());
} else {
buf.put((byte)host.length);
buf.put(host);
} 

buf.put(request.getPort());

return buf;
}

private IoBuffer encodeAuthenticationPacket(SocksProxyRequest request) throws UnsupportedEncodingException, GSSException {
byte[] user, pwd;
IoBuffer buf;
byte method = ((Byte)getSession().getAttribute(SELECTED_AUTH_METHOD)).byteValue();

switch (method) {

case 0:
getSession().setAttribute(HANDSHAKE_STEP, Integer.valueOf(2));
break;

case 1:
return encodeGSSAPIAuthenticationPacket(request);

case 2:
user = request.getUserName().getBytes("ASCII");
pwd = request.getPassword().getBytes("ASCII");
buf = IoBuffer.allocate(3 + user.length + pwd.length);

buf.put((byte)1);
buf.put((byte)user.length);
buf.put(user);
buf.put((byte)pwd.length);
buf.put(pwd);

return buf;
} 

return null;
}

private IoBuffer encodeGSSAPIAuthenticationPacket(SocksProxyRequest request) throws GSSException {
GSSContext ctx = (GSSContext)getSession().getAttribute(GSS_CONTEXT);
if (ctx == null) {

GSSManager manager = GSSManager.getInstance();
GSSName serverName = manager.createName(request.getServiceKerberosName(), (Oid)null);
Oid krb5OID = new Oid("1.2.840.113554.1.2.2");

if (LOGGER.isDebugEnabled()) {
LOGGER.debug("Available mechs:");
for (Oid o : manager.getMechs()) {
if (o.equals(krb5OID)) {
LOGGER.debug("Found Kerberos V OID available");
}
LOGGER.debug("{} with oid = {}", manager.getNamesForMech(o), o);
} 
} 

ctx = manager.createContext(serverName, krb5OID, null, 0);

ctx.requestMutualAuth(true);
ctx.requestConf(false);
ctx.requestInteg(false);

getSession().setAttribute(GSS_CONTEXT, ctx);
} 

byte[] token = (byte[])getSession().getAttribute(GSS_TOKEN);
if (token != null) {
LOGGER.debug("  Received Token[{}] = {}", Integer.valueOf(token.length), ByteUtilities.asHex(token));
}
IoBuffer buf = null;

if (!ctx.isEstablished()) {

if (token == null) {
token = new byte[32];
}

token = ctx.initSecContext(token, 0, token.length);

if (token != null) {
LOGGER.debug("  Sending Token[{}] = {}", Integer.valueOf(token.length), ByteUtilities.asHex(token));

getSession().setAttribute(GSS_TOKEN, token);
buf = IoBuffer.allocate(4 + token.length);
buf.put(new byte[] { 1, 1 });

buf.put(ByteUtilities.intToNetworkByteOrder(token.length, 2));
buf.put(token);
} 
} 

return buf;
}

private void writeRequest(IoFilter.NextFilter nextFilter, SocksProxyRequest request, int step) {
try {
IoBuffer buf = null;

if (step == 0) {
buf = encodeInitialGreetingPacket(request);
} else if (step == 1) {

buf = encodeAuthenticationPacket(request);

if (buf == null) {
step = 2;
}
} 

if (step == 2) {
buf = encodeProxyRequestPacket(request);
}

buf.flip();
writeData(nextFilter, buf);
}
catch (Exception ex) {
closeSession("Unable to send Socks request: ", ex);
} 
}

public synchronized void messageReceived(IoFilter.NextFilter nextFilter, IoBuffer buf) {
try {
int step = ((Integer)getSession().getAttribute(HANDSHAKE_STEP)).intValue();

if (step == 0 && buf.get(0) != 5) {
throw new IllegalStateException("Wrong socks version running on server");
}

if ((step == 0 || step == 1) && buf.remaining() >= 2) {

handleResponse(nextFilter, buf, step);
} else if (step == 2 && buf.remaining() >= 5) {
handleResponse(nextFilter, buf, step);
} 
} catch (Exception ex) {
closeSession("Proxy handshake failed: ", ex);
} 
}

protected void handleResponse(IoFilter.NextFilter nextFilter, IoBuffer buf, int step) throws Exception {
int len = 2;
if (step == 0) {

byte method = buf.get(1);

if (method == -1) {
throw new IllegalStateException("No acceptable authentication method to use with the socks proxy server");
}

getSession().setAttribute(SELECTED_AUTH_METHOD, Byte.valueOf(method));
}
else if (step == 1) {

byte method = ((Byte)getSession().getAttribute(SELECTED_AUTH_METHOD)).byteValue();

if (method == 1) {
int oldPos = buf.position();

if (buf.get(0) != 1) {
throw new IllegalStateException("Authentication failed");
}
if (buf.get(1) == 255) {
throw new IllegalStateException("Authentication failed: GSS API Security Context Failure");
}

if (buf.remaining() >= 2) {
byte[] size = new byte[2];
buf.get(size);
int s = ByteUtilities.makeIntFromByte2(size);
if (buf.remaining() >= s) {
byte[] token = new byte[s];
buf.get(token);
getSession().setAttribute(GSS_TOKEN, token);
len = 0;
} else {
return;
} 
} else {
buf.position(oldPos);
return;
} 
} else if (buf.get(1) != 0) {
throw new IllegalStateException("Authentication failed");
}

} else if (step == 2) {

byte addressType = buf.get(3);
len = 6;
if (addressType == 4) {
len += 16;
} else if (addressType == 1) {
len += 4;
} else if (addressType == 3) {
len += 1 + buf.get(4);
} else {
throw new IllegalStateException("Unknwon address type");
} 

if (buf.remaining() >= len) {

byte status = buf.get(1);
LOGGER.debug("  response status: {}", SocksProxyConstants.getReplyCodeAsString(status));

if (status == 0) {
buf.position(buf.position() + len);
setHandshakeComplete();

return;
} 
throw new Exception("Proxy handshake failed - Code: 0x" + ByteUtilities.asHex(new byte[] { status }));
} 

return;
} 

if (len > 0) {
buf.position(buf.position() + len);
}

boolean isAuthenticating = false;
if (step == 1) {
byte method = ((Byte)getSession().getAttribute(SELECTED_AUTH_METHOD)).byteValue();
if (method == 1) {
GSSContext ctx = (GSSContext)getSession().getAttribute(GSS_CONTEXT);
if (ctx == null || !ctx.isEstablished()) {
isAuthenticating = true;
}
} 
} 

if (!isAuthenticating) {
getSession().setAttribute(HANDSHAKE_STEP, Integer.valueOf(++step));
}

doHandshake(nextFilter);
}

protected void closeSession(String message) {
GSSContext ctx = (GSSContext)getSession().getAttribute(GSS_CONTEXT);
if (ctx != null) {
try {
ctx.dispose();
} catch (GSSException e) {
e.printStackTrace();
closeSession(message, e);
return;
} 
}
super.closeSession(message);
}
}

