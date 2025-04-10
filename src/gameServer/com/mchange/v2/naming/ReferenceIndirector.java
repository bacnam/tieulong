package com.mchange.v2.naming;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.ser.IndirectlySerialized;
import com.mchange.v2.ser.Indirector;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;

public class ReferenceIndirector
implements Indirector
{
static final MLogger logger = MLog.getLogger(ReferenceIndirector.class);

Name name;
Name contextName;
Hashtable environmentProperties;

public Name getName() {
return this.name;
}
public void setName(Name paramName) {
this.name = paramName;
}
public Name getNameContextName() {
return this.contextName;
}
public void setNameContextName(Name paramName) {
this.contextName = paramName;
}
public Hashtable getEnvironmentProperties() {
return this.environmentProperties;
}
public void setEnvironmentProperties(Hashtable paramHashtable) {
this.environmentProperties = paramHashtable;
}

public IndirectlySerialized indirectForm(Object paramObject) throws Exception {
Reference reference = ((Referenceable)paramObject).getReference();
return new ReferenceSerialized(reference, this.name, this.contextName, this.environmentProperties);
}

private static class ReferenceSerialized
implements IndirectlySerialized
{
Reference reference;

Name name;

Name contextName;
Hashtable env;

ReferenceSerialized(Reference param1Reference, Name param1Name1, Name param1Name2, Hashtable param1Hashtable) {
this.reference = param1Reference;
this.name = param1Name1;
this.contextName = param1Name2;
this.env = param1Hashtable;
}

public Object getObject() throws ClassNotFoundException, IOException {
try {
InitialContext initialContext;
if (this.env == null) {
initialContext = new InitialContext();
} else {
initialContext = new InitialContext(this.env);
} 
Context context = null;
if (this.contextName != null) {
context = (Context)initialContext.lookup(this.contextName);
}
return ReferenceableUtils.referenceToObject(this.reference, this.name, context, this.env);
}
catch (NamingException namingException) {

if (ReferenceIndirector.logger.isLoggable(MLevel.WARNING))
ReferenceIndirector.logger.log(MLevel.WARNING, "Failed to acquire the Context necessary to lookup an Object.", namingException); 
throw new InvalidObjectException("Failed to acquire the Context necessary to lookup an Object: " + namingException.toString());
} 
}
}
}

