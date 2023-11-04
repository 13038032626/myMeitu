package com.example.meitu2.controller;

import com.example.meitu2.pojos.User;
import com.example.meitu2.utils.cookieUtils;
import com.example.meitu2.utils.jwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

@RestController
public class loginController {

//    HashMap<String,HashMap<String,String>> map = new HashMap<>(); //k-name , v-用户存储的必要信息
    @Autowired
    User user;
    HashSet<Integer> set = new HashSet<>();

    @GetMapping("/loginCookie")
    //逻辑：如果后台缓存中存在相同用户Id，则根据request的信息重新更新后台信息，如果没用缓存，前后端添加信息
    public void login(HttpServletResponse response, HttpServletRequest request) {
        Integer userId = Integer.parseInt(cookieUtils.getCookie(request, "userId"));
        if (!set.contains(userId)) {
            //初次登录
            String name = request.getParameter("name");
            cookieUtils.setCookie(response, "userId", String.valueOf(userId));
            cookieUtils.setCookie(response, "name", name);
            set.add(userId);
            user.setUserId(userId);
            user.setUserName(name);
        }else {
            String existedName = cookieUtils.getCookie(request,"name");
            user.setUserId(userId);
            user.setUserName(existedName);
        }
    }
//
    @GetMapping("/loginSession")
    public void login(HttpServletRequest request) {

        //login在初次登录时携带name/userId，自动跳到else配置User，后台不携带参数时其实携带了Cookie的Session，用后台保存的信息重置User

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        //前端的请求中getSession，似乎是获取SessionId，然后在后台的Sessions中寻找，如果有则有，如果没用则为新建的

        if(userId != null){
            //后台存在Session信息
            String name = (String) session.getAttribute("name");
            String bucketName = (String) session.getAttribute("bucket");
            user.setUserName(name);
            user.setUserId(Integer.parseInt(userId));
            user.setBucket(bucketName);

        }else {
            //新登录
            String name = request.getParameter("name");
            String id = request.getParameter("userId");
            String bucketName = new Date()+name;
            user.setBucket(bucketName);
            user.setUserName(name);
            user.setUserId(Integer.parseInt(id));
            session.setAttribute("bucket",bucketName);
            session.setAttribute("name",name);
            session.setAttribute("userId",id);
        }
    }

//    @GetMapping("/loginJWT")
//    public String login(HttpServletRequest request){
//        String token = request.getHeader("token");
//        if(token != null){
//            //登录过
//            Claims claims = jwtUtils.parseJwt(token);
//            String name = (String) claims.get("name");
//            Integer userId = (Integer) claims.get("userId");
//            user.setUserName(name);
//            user.setUserId(userId);
//            return "login success";
//        }else {
//            String name = request.getParameter("name");
//            String id = request.getParameter("userId");
//            user.setUserName(name);
//            user.setUserId(Integer.valueOf(id));
//            return jwtUtils.getJWT(name, Integer.valueOf(id));
//        }
//    }
}
