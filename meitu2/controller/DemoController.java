package com.example.meitu2.controller;

import com.example.meitu2.pojos.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    static Integer num = 0;

    @Autowired
    User user;

    static Map<Integer , User> map = new HashMap<>();

    @GetMapping("/Demo")
    public String Demo(HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();

        Integer id = Integer.parseInt(session.getId());
        if(map.containsKey(id)){
            User lastUser = map.get(id);
            user.setUserId(lastUser.getUserId());
            user.setUserName(lastUser.getUserName());
        }else {
            String name = request.getParameter("name");
            String id1 = request.getParameter("userId");
            user.setUserName(name);
            user.setUserId(Integer.valueOf(id1));
            user.setBucket("111111111111111111111111111111");
            map.put(Integer.valueOf(id1),user);
        }
        return null;
    }
}
