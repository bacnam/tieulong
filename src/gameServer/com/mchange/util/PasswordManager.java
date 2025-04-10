package com.mchange.util;

import java.io.IOException;

public interface PasswordManager {
  boolean validate(String paramString1, String paramString2) throws IOException;

  boolean updatePassword(String paramString1, String paramString2, String paramString3) throws IOException;
}

