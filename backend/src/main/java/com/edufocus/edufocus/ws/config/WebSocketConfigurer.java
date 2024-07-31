package com.edufocus.edufocus.ws.config;

import com.edufocus.edufocus.global.properties.RabbitMQProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigurer implements WebSocketMessageBrokerConfigurer {

    private final RabbitMQProperties rabbitMQProperties;

    public WebSocketConfigurer(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher("."));

        registry.setApplicationDestinationPrefixes("/pub")
                .enableStompBrokerRelay("/topic", "/queue", "/exchange")
                .setRelayHost(rabbitMQProperties.getHost())
                .setVirtualHost("/")
                .setRelayPort(61613)
                .setClientLogin(rabbitMQProperties.getUsername())
                .setClientPasscode(rabbitMQProperties.getPassword())
                .setSystemLogin(rabbitMQProperties.getUsername())
                .setSystemPasscode(rabbitMQProperties.getPassword());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");
    }
}