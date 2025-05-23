package com.mchange.v2.c3p0.debug;

import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
import com.mchange.v2.log.MLevel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Referenceable;

public final class CloseLoggingComboPooledDataSource
extends AbstractComboPooledDataSource
implements Serializable, Referenceable
{
private static final long serialVersionUID = 1L;
volatile MLevel level = MLevel.INFO; private static final short VERSION = 1;
public void setCloseLogLevel(MLevel level) {
this.level = level; } public MLevel getCloseLogLevel() {
return this.level;
}

public CloseLoggingComboPooledDataSource() {}

public CloseLoggingComboPooledDataSource(boolean autoregister) {
super(autoregister);
}
public CloseLoggingComboPooledDataSource(String configName) {
super(configName);
}
public Connection getConnection() throws SQLException {
return (Connection)new CloseLoggingConnectionWrapper(super.getConnection(), this.level);
}
public Connection getConnection(String user, String password) throws SQLException {
return (Connection)new CloseLoggingConnectionWrapper(super.getConnection(user, password), this.level);
}

private void writeObject(ObjectOutputStream oos) throws IOException {
oos.writeShort(1);
}

private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
short version = ois.readShort();
switch (version) {
case 1:
return;
} 

throw new IOException("Unsupported Serialized Version: " + version);
}
}

