package com.cjh.websocket.socket.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: chenjun
 * @Date: 2019-02-26 17:12:36
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 6001142316824875061L;

    private String id;
    private String name;
    private String message;
    private String date;
}
