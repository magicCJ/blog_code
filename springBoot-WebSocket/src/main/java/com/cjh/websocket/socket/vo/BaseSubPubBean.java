package com.cjh.websocket.socket.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSubPubBean implements Serializable {

    private static final long serialVersionUID = -4971494287189403985L;

    public enum Type{
        USER,
        LOGIN
    }

    private String id;

    private Type type;

    private Object Data;
}
