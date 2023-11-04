package com.example.meitu2.filters;

import com.example.meitu2.utils.jwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class baseFilter implements Filter {
    //此过滤器的作用是：将所有非登录的请求拦截下来，验证其中的token，决定是否放行/路由到何处
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURL().toString();
        if(url.contains("login")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = request.getHeader("token");
        try {
            Claims claims = jwtUtils.parseJwt(token);
            String userId = (String) claims.get("userId");
            String name = (String) claims.get("name");

        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
