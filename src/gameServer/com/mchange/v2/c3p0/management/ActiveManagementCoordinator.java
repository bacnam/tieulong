package com.mchange.v2.c3p0.management;

import com.mchange.v2.c3p0.PooledDataSource;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class ActiveManagementCoordinator
implements ManagementCoordinator
{
public static final String C3P0_REGISTRY_NAME_KEY = "com.mchange.v2.c3p0.management.RegistryName";
private static final String C3P0_REGISTRY_NAME_PFX = "com.mchange.v2.c3p0:type=C3P0Registry";
static final MLogger logger = MLog.getLogger(ActiveManagementCoordinator.class);

MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
String regName = getRegistryName();

public void attemptManageC3P0Registry() {
try {
ObjectName name = new ObjectName(this.regName);
C3P0RegistryManager mbean = new C3P0RegistryManager();

if (this.mbs.isRegistered(name)) {

if (logger.isLoggable(MLevel.WARNING))
{
logger.warning("A C3P0Registry mbean is already registered. This probably means that an application using c3p0 was undeployed, but not all PooledDataSources were closed prior to undeployment. This may lead to resource leaks over time. Please take care to close all PooledDataSources.");
}

this.mbs.unregisterMBean(name);
} 
this.mbs.registerMBean(mbean, name);
}
catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Failed to set up C3P0RegistryManager mBean. [c3p0 will still function normally, but management via JMX may not be possible.]", e);
}
} 
}

public void attemptUnmanageC3P0Registry() {
try {
ObjectName name = new ObjectName(this.regName);
if (this.mbs.isRegistered(name)) {

this.mbs.unregisterMBean(name);
if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "C3P0Registry mbean unregistered.");
}
} else if (logger.isLoggable(MLevel.FINE)) {
logger.fine("The C3P0Registry mbean was not found in the registry, so could not be unregistered.");
} 
} catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "An Exception occurred while trying to unregister the C3P0RegistryManager mBean." + e);
}
} 
}

public void attemptManagePooledDataSource(PooledDataSource pds) {
String name = getPdsObjectNameStr(pds);

try {
DynamicPooledDataSourceManagerMBean mbean = new DynamicPooledDataSourceManagerMBean(pds, name, this.mbs);
}
catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Failed to set up a PooledDataSourceManager mBean. [" + name + "] " + "[c3p0 will still functioning normally, but management via JMX may not be possible.]", e);
}
} 
}

public void attemptUnmanagePooledDataSource(PooledDataSource pds) {
String nameStr = getPdsObjectNameStr(pds);

try {
ObjectName name = new ObjectName(nameStr);
if (this.mbs.isRegistered(name)) {

this.mbs.unregisterMBean(name);
if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "MBean: " + nameStr + " unregistered.");
}
}
else if (logger.isLoggable(MLevel.FINE)) {
logger.fine("The mbean " + nameStr + " was not found in the registry, so could not be unregistered.");
} 
} catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "An Exception occurred while unregistering mBean. [" + nameStr + "] " + e);
}
} 
}

static String getPdsObjectNameStr(PooledDataSource pds) {
String dataSourceName = pds.getDataSourceName();
String out = "com.mchange.v2.c3p0:type=PooledDataSource,identityToken=" + pds.getIdentityToken();
if (dataSourceName != null)
out = out + ",name=" + dataSourceName; 
return out;
}

private static String getRegistryName() {
String name = C3P0Config.getMultiPropertiesConfig().getProperty("com.mchange.v2.c3p0.management.RegistryName");
if (name == null) {
name = "com.mchange.v2.c3p0:type=C3P0Registry";
} else {
name = "com.mchange.v2.c3p0:type=C3P0Registry,name=" + name;
}  return name;
}
}

