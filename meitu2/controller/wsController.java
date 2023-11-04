package com.example.meitu2.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.meitu2.pojos.websocket;

@RestController
public class wsController {
    @Autowired
    websocket websocket;

    @GetMapping("wsTest")
    public void getTest() throws InterruptedException {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name","rom");
        jsonObject.addProperty("age",21);
        jsonObject.addProperty("text","message");

        websocket.sendOneMessage("0",jsonObject.toString());
        int a = 0;
        while (true){
            websocket.sendOneMessage("0", String.valueOf(a));
            Thread.sleep(50);
            a++;
        }
    }
}
