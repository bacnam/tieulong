package org.apache.http.client.params;

import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public final class AuthPolicy {
  public static final String NTLM = "NTLM";

  public static final String DIGEST = "Digest";

  public static final String BASIC = "Basic";

  public static final String SPNEGO = "Negotiate";

  public static final String KERBEROS = "Kerberos";
}

