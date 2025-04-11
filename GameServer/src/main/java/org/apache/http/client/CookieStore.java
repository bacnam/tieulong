package org.apache.http.client;

import org.apache.http.cookie.Cookie;

import java.util.Date;
import java.util.List;

public interface CookieStore {
    void addCookie(Cookie paramCookie);

    List<Cookie> getCookies();

    boolean clearExpired(Date paramDate);

    void clear();
}

