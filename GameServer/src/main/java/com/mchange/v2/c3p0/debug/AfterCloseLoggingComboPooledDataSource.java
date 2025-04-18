package com.mchange.v2.c3p0.debug;

import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Referenceable;

public final class AfterCloseLoggingComboPooledDataSource
extends AbstractComboPooledDataSource
implements Serializable, Referenceable
{
private static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public AfterCloseLoggingComboPooledDataSource() {}

public AfterCloseLoggingComboPooledDataSource(boolean autoregister) {
super(autoregister);
}
public AfterCloseLoggingComboPooledDataSource(String configName) {
super(configName);
}
public Connection getConnection() throws SQLException {
return AfterCloseLoggingConnectionWrapper.wrap(super.getConnection());
}
public Connection getConnection(String user, String password) throws SQLException {
return AfterCloseLoggingConnectionWrapper.wrap(super.getConnection(user, password));
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

