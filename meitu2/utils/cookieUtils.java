package com.example.meitu2.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class cookieUtils {

    public static void setCookie(HttpServletResponse response,String k,String v){
        response.addCookie(new Cookie(k,v));
    }

    public static Cookie[] getCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        return cookies;
    }

    public static String getCookie(HttpServletRequest request, String k){
        Cookie[] cookies = getCookies(request);
        for (Cookie c :cookies
             ) {
            if(c.getName().equals(k)){
                return c.getValue();
            }
        }
        return null;
    }
}
