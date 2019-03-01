package com.cjh.websocket.controller;

import com.cjh.websocket.socket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: chenjun
 * @Date: 2019-02-28 23:42:35
 */
@RestController
public class TestController {

    @Autowired
    private WebSocketService webSocketService;


    @RequestMapping("send")
    public String send(String id) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        webSocketService.publishData(df.format(new Date()), id);
        return id;
    }
}
