package com.cjh.websocket.controller;

import com.cjh.websocket.socket.SubThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: chenjun
 * @Date: 2019-02-26 11:57:06
 */
@Component
public class CommandLine implements CommandLineRunner{

    @Autowired
    SubThread subThread;

    @Override
    public void run(String... args) {
        subThread.start();
    }
}
