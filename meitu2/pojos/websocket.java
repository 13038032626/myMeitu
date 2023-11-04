package com.example.meitu2.pojos;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class websocket {

    Session session;
    String userId;

    private static CopyOnWriteArraySet<websocket> webSockets =new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    private static ConcurrentHashMap<String,Session> sessionPool = new ConcurrentHashMap<String,Session>();
    @OnOpen
    public void onOpen(Session session,@PathParam(value="userId")String userId){
        this.session = session;
        this.userId = userId;
        webSockets.add(this);
        sessionPool.put(userId,session);
        log.info("onOpen");
    }

    @OnClose
    public void onclose(){
        webSockets.remove(this);
        sessionPool.remove(this.userId);
        log.info("onClose");
    }

    @OnMessage
    public void onMessage(String message){
        log.info("onMessage");
        System.out.println("message: "+message);
    }

    public void sendAllMessage(){
        log.info("sendAllMessage");
        for (websocket websocket:webSockets
             ) {
            if(websocket.session.isOpen()){
                websocket.session.getAsyncRemote().sendText("test Message");
            }
        }
    }
    public void sendOneMessage(String userId,String message){
        Session session = sessionPool.get(userId);
        if(session != null && session.isOpen()){
            log.info("sendOneMessage");
            session.getAsyncRemote().sendText(message);
        }
    }
}
