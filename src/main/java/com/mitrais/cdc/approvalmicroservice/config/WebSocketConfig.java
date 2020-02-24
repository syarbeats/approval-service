package com.mitrais.cdc.approvalmicroservice.config;

import com.mitrais.cdc.approvalmicroservice.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private JwtTokenProvider jwtTokenProvider;

    public WebSocketConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /*@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {


            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if(StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand())){

                    List<String> authorization = stompHeaderAccessor.getNativeHeader("Authorization");
                    log.info("Authorization:"+authorization.get(0));
                    String token = authorization.get(0).split(" ")[1];

                    if (token != null && jwtTokenProvider.validateToken(token)) {
                        log.info("Token is validated...");
                        Authentication auth = jwtTokenProvider.getAuthentication(token);
                        if (auth != null) {
                            stompHeaderAccessor.setUser(auth);
                        }
                    }
                }

                return message;
            }
        });
    }*/
}