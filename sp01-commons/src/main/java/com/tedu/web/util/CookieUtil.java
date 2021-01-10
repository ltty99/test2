package com.tedu.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil
{
    public static void setCookie(final HttpServletResponse response, final String name, final String value, final String domain, final String path, final int maxAge) {
        final Cookie cookie = new Cookie(name, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    public static void setCookie(final HttpServletResponse response, final String name, final String value, final int maxAge) {
        setCookie(response, name, value, null, "/", maxAge);
    }
    
    public static void setCookie(final HttpServletResponse response, final String name, final String value) {
        setCookie(response, name, value, null, "/", 3600);
    }
    
    public static void setCookie(final HttpServletResponse response, final String name) {
        setCookie(response, name, "", null, "/", 3600);
    }
    
    public static String getCookie(final HttpServletRequest request, final String name) {
        String value = null;
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie[] array;
            for (int length = (array = cookies).length, i = 0; i < length; ++i) {
                final Cookie cookie = array[i];
                if (cookie.getName().equals(name)) {
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }
    
    public static void removeCookie(final HttpServletResponse response, final String name, final String domain, final String path) {
        setCookie(response, name, "", domain, path, 0);
    }
}
