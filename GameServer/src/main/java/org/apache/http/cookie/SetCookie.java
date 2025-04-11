package org.apache.http.cookie;

import org.apache.http.annotation.Obsolete;

import java.util.Date;

public interface SetCookie extends Cookie {
    void setValue(String paramString);

    @Obsolete
    void setComment(String paramString);

    void setExpiryDate(Date paramDate);

    void setDomain(String paramString);

    void setPath(String paramString);

    void setSecure(boolean paramBoolean);

    @Obsolete
    void setVersion(int paramInt);
}

