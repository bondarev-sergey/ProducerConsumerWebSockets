package com.example.ProducerConsumer.config;

import com.example.ProducerConsumer.handler.ConsumerSocketHandler;
import com.example.ProducerConsumer.handler.ProducerSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@ComponentScan(basePackages = {
        "com.example.ProducerConsumer.handler",
        "com.example.ProducerConsumer.service"
})
@EnableWebSocketSecurity
@EnableWebSocketMessageBroker
public class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerSocketHandler.class);

    private final ConsumerSocketHandler consumerSocketHandler;

    private final ProducerSocketHandler producerSocketHandler;

    @Autowired
    public SocketBrokerConfig(ProducerSocketHandler producerSocketHandler, ConsumerSocketHandler consumerSocketHandler) {
        this.producerSocketHandler = producerSocketHandler;
        this.consumerSocketHandler = consumerSocketHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/producer", "/consumer")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/user");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024 * 1024);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(consumerSocketHandler, "/consumer")
                .setAllowedOrigins("*") // Здесь можно указать разрешенные origin (для безопасности)
                .addInterceptors(new HttpSessionHandshakeInterceptor()) // Добавляем интерсептор для проверки аутентификации
                .withSockJS(); // Включаем поддержку SockJS, если нужно
        registry.addHandler(producerSocketHandler, "/producer")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .withSockJS();
    }
}
