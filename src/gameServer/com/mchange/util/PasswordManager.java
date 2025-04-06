package com.mchange.util;

import java.io.IOException;

public interface PasswordManager {
  boolean validate(String paramString1, String paramString2) throws IOException;
  
  boolean updatePassword(String paramString1, String paramString2, String paramString3) throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/PasswordManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */