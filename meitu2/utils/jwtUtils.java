package com.example.meitu2.utils;

import io.jsonwebtoken.*;

import java.util.*;

public class jwtUtils {

    public static String getJWT(String name,Integer userId){

        Map<String, Object> data = new HashMap<>();
        data.put("name",name);
        data.put("userId",userId);
        String compact = Jwts.builder()
                .signWith(SignatureAlgorithm.ES256, "52mzd")
                .addClaims(data)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setAudience("app")
                .setIssuer("heima")
                .compact();

        return compact;
    }

    public static Claims parseJwt(String token){
        Claims body = Jwts.parser()
                .setSigningKey("52mzd")
                .parseClaimsJws(token)
                .getBody();
        return body;
    }
}
