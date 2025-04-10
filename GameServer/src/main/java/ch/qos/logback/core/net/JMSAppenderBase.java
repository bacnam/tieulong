package ch.qos.logback.core.net;

import ch.qos.logback.core.AppenderBase;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

public abstract class JMSAppenderBase<E>
extends AppenderBase<E>
{
protected String securityPrincipalName;
protected String securityCredentials;
protected String initialContextFactoryName;
protected String urlPkgPrefixes;
protected String providerURL;
protected String userName;
protected String password;

protected Object lookup(Context ctx, String name) throws NamingException {
try {
return ctx.lookup(name);
} catch (NameNotFoundException e) {
addError("Could not find name [" + name + "].");
throw e;
} 
}

public Context buildJNDIContext() throws NamingException {
Context jndi = null;

if (this.initialContextFactoryName != null) {
Properties env = buildEnvProperties();
jndi = new InitialContext(env);
} else {
jndi = new InitialContext();
} 
return jndi;
}

public Properties buildEnvProperties() {
Properties env = new Properties();
env.put("java.naming.factory.initial", this.initialContextFactoryName);
if (this.providerURL != null) {
env.put("java.naming.provider.url", this.providerURL);
} else {
addWarn("You have set InitialContextFactoryName option but not the ProviderURL. This is likely to cause problems.");
} 

if (this.urlPkgPrefixes != null) {
env.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
}

if (this.securityPrincipalName != null) {
env.put("java.naming.security.principal", this.securityPrincipalName);
if (this.securityCredentials != null) {
env.put("java.naming.security.credentials", this.securityCredentials);
} else {
addWarn("You have set SecurityPrincipalName option but not the SecurityCredentials. This is likely to cause problems.");
} 
} 

return env;
}

public String getInitialContextFactoryName() {
return this.initialContextFactoryName;
}

public void setInitialContextFactoryName(String initialContextFactoryName) {
this.initialContextFactoryName = initialContextFactoryName;
}

public String getProviderURL() {
return this.providerURL;
}

public void setProviderURL(String providerURL) {
this.providerURL = providerURL;
}

public String getURLPkgPrefixes() {
return this.urlPkgPrefixes;
}

public void setURLPkgPrefixes(String urlPkgPrefixes) {
this.urlPkgPrefixes = urlPkgPrefixes;
}

public String getSecurityCredentials() {
return this.securityCredentials;
}

public void setSecurityCredentials(String securityCredentials) {
this.securityCredentials = securityCredentials;
}

public String getSecurityPrincipalName() {
return this.securityPrincipalName;
}

public void setSecurityPrincipalName(String securityPrincipalName) {
this.securityPrincipalName = securityPrincipalName;
}

public String getUserName() {
return this.userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getPassword() {
return this.password;
}

public void setPassword(String password) {
this.password = password;
}
}

