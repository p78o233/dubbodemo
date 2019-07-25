package com.example.demo.controller.websocket;/*
 * @author p78o2
 * @date 2019/7/24
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{itemStr}/{userId}")
@Component
public class WebSocketController {
    //    静态变量用于记录在线人数
    private static int onLineCount = 0;
    //    用于保存用户信息  key用户ID  value sessionID
//    private static Map<String,String> userMap = new HashMap<String,String>();
//    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<WebSocketController>();

    //    前端用户用的
    private static Map<Integer, String> userMapApi = new HashMap<Integer, String>();
    private static CopyOnWriteArraySet<WebSocketController> webSocketSetApi = new CopyOnWriteArraySet<WebSocketController>();
    //    后端用户用的
    private static Map<Integer, String> userMapAdmin = new HashMap<Integer, String>();
    private static CopyOnWriteArraySet<WebSocketController> webSocketSetAdmin = new CopyOnWriteArraySet<WebSocketController>();

    //    与某个客户端连接会话，需要通过它来发送数据
    private Session session;

    //    成功建立的调用方法
//    session参数可选
    @OnOpen
    public void onOpen(@PathParam(value = "itemStr") String itemStr, @PathParam(value = "userId") Integer userId, Session session) {
        this.session = session;
        if (itemStr.equals("api")) {
            webSocketSetApi.add(this);
            //        保存用户信息
            userMapApi.put(userId, session.getId());
//        把session的id放到对应的userId的数据库表中
        } else if (itemStr.equals("admin")) {
            webSocketSetAdmin.add(this);
            //        保存用户信息
            userMapAdmin.put(userId, session.getId());
//        把session的id放到对应的userId的数据库表中
        }
        addOnlineCount();
        System.out.println(getOnlineCount());
    }

    //    连接关闭
    @OnClose
    public void onClose(@PathParam(value = "itemStr") String itemStr) {
        if (itemStr.equals("api")) {
            webSocketSetApi.remove(this);
        } else if (itemStr.equals("admin")) {
            webSocketSetAdmin.remove(this);
        }
        sunOnlineCount();
        System.out.println(getOnlineCount());
    }

    //    收到客户端消息后调用的方法
//    msgJson客户端发送过来的参数
//    session 可选参数
    @OnMessage
    public void onMessage(String msgJson, Session session) {
        JSONObject jo = JSON.parseObject(msgJson);
        String message = jo.getString("message");
        String itemStr = jo.getString("item");
        Integer fromId = jo.getInteger("fromId");
        Integer toId = jo.getInteger("toId");
        System.out.println("来自客户端的消息" + message);
        if ("api".equals(itemStr)) {
            try {
//                发送消息的方法
                if (!message.equals("check_webSocket_connection")) {
//                if(session.getId().equals(item.session.getId())) {
                    for (WebSocketController item : webSocketSetApi) {
//                        根据fromId去数据库中获取它的seeeionid
//                        比较session.getId().equals(上一步获取的sessionId)
//                        如果人不在，就把信息保存在数据库里面
                        item.sendMessage(message);
                    }
//                }
                } else {
//                    检查自己是否还连在线上
//                    根据自己的用户id获取数据库中websocket的sessionId
//                        for (WebSocketController i : webSocketSetApi) {
//                        连接断了返回
//                            item.sendMessage("no_websocket_connect");
//                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if ("admin".equals(itemStr)) {

            try {
//                发送消息的方法
                if (!message.equals("check_webSocket_connection")) {
//                if(session.getId().equals(item.session.getId())) {
                    for (WebSocketController item : webSocketSetAdmin) {
//                        根据fromId去数据库中获取它的seeeionid
//                        比较session.getId().equals(上一步获取的sessionId)
//                        如果人不在，就把信息保存在数据库里面
                        item.sendMessage(message);
                    }
//                }
                } else {
//                    检查自己是否还连在线上
//                    根据自己的用户id获取数据库中websocket的sessionId
                    boolean flag = false;
//                        for (WebSocketController i : webSocketSetAdmin) {
//                        }
                    if (!flag) {
//                            连接断了返回
//                            item.sendMessage("no_websocket_connect");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //    增加在线人数
    public void addOnlineCount() {
        WebSocketController.onLineCount++;
    }

    //    减少在线人数
    public void sunOnlineCount() {
        WebSocketController.onLineCount--;
    }

    //    获取当前在线人数
    public int getOnlineCount() {
        return WebSocketController.onLineCount;
    }
}
