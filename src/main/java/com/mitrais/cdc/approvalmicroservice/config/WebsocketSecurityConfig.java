package com.mitrais.cdc.approvalmicroservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().permitAll();
        messages.simpDestMatchers("/stomp").permitAll();
        messages.simpSubscribeDestMatchers("/topic/**").permitAll();
        messages.simpMessageDestMatchers("/topic/message").permitAll();
        //messages.simpDestPathMatcher("/stomp").
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }


}
