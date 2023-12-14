package com.example.ProducerConsumer.config;

import com.example.ProducerConsumer.ConsumerSocketHandler;
import com.example.ProducerConsumer.ProducerSocketHandler;
import com.example.ProducerConsumer.repository.UserAuditRepository;
import com.example.ProducerConsumer.service.ConsumerService;
import com.example.ProducerConsumer.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Base64;
import java.util.List;

@Configuration
@EnableWebSocket
@EnableWebSocketSecurity
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerSocketHandler.class);

    @Autowired
    private ConsumerSocketHandler consumerSocketHandler;

    @Autowired
    private ProducerSocketHandler producerSocketHandler;

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        try {
//            registry.addEndpoint("/producer-websocket/{token}", "/consumer-websocket/{token}")
//                    .setAllowedOrigins("*")
//                    .withSockJS();
//            logger.info("WebSocketConfig.registerStompEndpoints() is successfully completed");
//        }
//        catch (Exception e) {
//            logger.error("WebSocketConfig.registerStompEndpoints() is bad. " + e.getMessage());
//        }
//    }

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        try {
//            registry.setApplicationDestinationPrefixes("/app");
//            registry.enableSimpleBroker("/topic", "/user");
//            logger.info("WebSocketConfig.configureMessageBroker() is successfully completed");
//        } catch (Exception e) {
//            logger.error("WebSocketConfig.configureMessageBroker() is bad. " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
//        registration.setMessageSizeLimit(1024 * 1024);
//    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        try {
            registry.addHandler(consumerSocketHandler, "/consumer")
                    .setAllowedOrigins("*") // Здесь можно указать разрешенные origin (для безопасности)
                    .addInterceptors(new HttpSessionHandshakeInterceptor()) // Добавляем интерсептор для проверки аутентификации
                    .withSockJS(); // Включаем поддержку SockJS, если нужно
            registry.addHandler(producerSocketHandler, "/producer")
                    .setAllowedOrigins("*")
                    .addInterceptors(new HttpSessionHandshakeInterceptor())
                    .withSockJS();
            logger.info("WebSocketConfig.registerWebSocketHandlers() is successfully completed");
        }
        catch (Exception e) {
            logger.error("WebSocketConfig.registerWebSocketHandlers() is bad. " + e.getMessage());
        }
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        try {
//            registration.interceptors(new ChannelInterceptor() {
//                @Override
//                public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
//                    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                        // Extract the token from the destination header
//                        String destination = accessor.getDestination();
//                        String token = extractTokenFromDestination(destination);
//
//                        logger.info("destination: " + destination + ", token: " + token);
//
//                        // Set the 'Authorization' header for further processing
//                        accessor.setNativeHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(("user:" + token).getBytes()));
//                    }
//
//                    return message;
//                }
//
//                private String extractTokenFromDestination(String destination) {
//                    // Extract token from destination (you need to implement your own logic)
//                    // Example: If your destination is "/producer-websocket/someToken", extract "someToken"
//                    String[] parts = destination.split("/");
//                    if (parts.length > 2) {
//                        return parts[2];
//                    } else {
//                        return "";
//                    }
//                }
//            });
//            logger.info("WebSocketConfig.configureClientInboundChannel() is successfully completed");
//        }
//        catch (Exception e) {
//            logger.error("WebSocketConfig.configureClientInboundChannel() is bad. " + e.getMessage());
//        }
//    }
}
