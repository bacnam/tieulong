package org.apache.http.impl.auth;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

@Deprecated
public class NegotiateScheme
extends GGSSchemeBase
{
private final Log log = LogFactory.getLog(getClass());

private static final String SPNEGO_OID = "1.3.6.1.5.5.2";

private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";

private final SpnegoTokenGenerator spengoGenerator;

public NegotiateScheme(SpnegoTokenGenerator spengoGenerator, boolean stripPort) {
super(stripPort);
this.spengoGenerator = spengoGenerator;
}

public NegotiateScheme(SpnegoTokenGenerator spengoGenerator) {
this(spengoGenerator, false);
}

public NegotiateScheme() {
this(null, false);
}

public String getSchemeName() {
return "Negotiate";
}

public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
return authenticate(credentials, request, null);
}

public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
return super.authenticate(credentials, request, context);
}

protected byte[] generateToken(byte[] input, String authServer) throws GSSException {
return super.generateToken(input, authServer);
}

protected byte[] generateToken(byte[] input, String authServer, Credentials credentials) throws GSSException {
Oid negotiationOid = new Oid("1.3.6.1.5.5.2");

byte[] token = input;
boolean tryKerberos = false;
try {
token = generateGSSToken(token, negotiationOid, authServer, credentials);
} catch (GSSException ex) {

if (ex.getMajor() == 2) {
this.log.debug("GSSException BAD_MECH, retry with Kerberos MECH");
tryKerberos = true;
} else {
throw ex;
} 
} 

if (tryKerberos) {

this.log.debug("Using Kerberos MECH 1.2.840.113554.1.2.2");
negotiationOid = new Oid("1.2.840.113554.1.2.2");
token = generateGSSToken(token, negotiationOid, authServer, credentials);

if (token != null && this.spengoGenerator != null) {
try {
token = this.spengoGenerator.generateSpnegoDERObject(token);
} catch (IOException ex) {
this.log.error(ex.getMessage(), ex);
} 
}
} 
return token;
}

public String getParameter(String name) {
Args.notNull(name, "Parameter name");
return null;
}

public String getRealm() {
return null;
}

public boolean isConnectionBased() {
return true;
}
}

