package com.mchange.util.impl;

import com.mchange.util.PasswordManager;
import java.io.File;
import java.io.IOException;

public class PlaintextPropertiesPasswordManager
implements PasswordManager
{
private static final String PASSWORD_PROP_PFX = "password.";
private static final String HEADER = "com.mchange.util.impl.PlaintextPropertiesPasswordManager data";
SyncedProperties props;

public PlaintextPropertiesPasswordManager(File paramFile) throws IOException {
this.props = new SyncedProperties(paramFile, "com.mchange.util.impl.PlaintextPropertiesPasswordManager data");
}
public boolean validate(String paramString1, String paramString2) throws IOException {
return paramString2.equals(this.props.getProperty("password." + paramString1));
}

public boolean updatePassword(String paramString1, String paramString2, String paramString3) throws IOException {
if (!validate(paramString1, paramString2)) return false; 
this.props.put("password." + paramString1, paramString3);
return true;
}
}

