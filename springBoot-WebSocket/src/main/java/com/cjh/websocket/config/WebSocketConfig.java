package com.cjh.websocket.config;

import com.cjh.websocket.socket.vo.User;
import com.cjh.websocket.util.WebsocketMapUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableSimpleBroker("/topic", "/user", "/message");
        config.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .addInterceptors(myHandshakeInterceptor())  //添加 websocket握手拦截器
                .setHandshakeHandler(myDefaultHandshakeHandler())   //添加 websocket握手处理器
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * WebSocket 握手拦截器
     * 可做一些用户认证拦截处理
     */
    private HandshakeInterceptor myHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            /**
             * websocket握手连接
             * @return 返回是否同意握手
             */
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

            }
        };
    }

    //WebSocket 握手处理器
    private DefaultHandshakeHandler myDefaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                //设置认证通过的用户到当前会话中
                return (Principal) attributes.get("user");
            }
        };
    }

    /**
     * 输入通道参数设置
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
        log.info("configureClientInboundChannel-start");
        channelRegistration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                log.info("configureClientInboundChannel-accessor.getCommand():" + accessor.getCommand());
                //判断前端socket发送过来操作
                switch (accessor.getCommand()) {
                    case CONNECT: {
                        String authorization = accessor.getNativeHeader("Authorization").get(0);
                        User user = (User) JSONObject.toBean(JSONObject.fromObject(authorization), User.class);
                        log.info("configureClientInboundChannel-CONNECT:" + "user:" + user);
                        if (user == null) {
                            return null;
                        }
                        WebsocketMapUtil.putServerChannel(user);
                        //注册当前用户
                        accessor.setUser(new MyPrincipal(user));
                        return message;
                    }
                    case ABORT:
                    case DISCONNECT: {
                        MyPrincipal myPrincipal = (MyPrincipal) accessor.getHeader("simpUser");
                        log.info("configureClientInboundChannel-DISCONNECT:myPrincipal:" + myPrincipal.toString());
                        if (myPrincipal == null) {
                            return null;
                        }
                        WebsocketMapUtil.removeServerChannel(myPrincipal.user);
                        return message;
                    }
                    case SUBSCRIBE: {
                        return message;
                    }
                    default:
                        return null;
                }
            }
        });
    }
    //重新实现Principal接口，主要重写getName方法。点对点推送就是采用这个getName方法获取用户名
    class MyPrincipal implements Principal {

        private User user;

        public MyPrincipal(User user) {
            this.user = user;
        }

        @Override
        public String getName() {
            return user.getId();
        }
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> list) {
        return false;
    }
}