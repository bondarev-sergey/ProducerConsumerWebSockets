package com.example.ProducerConsumer.config;

import com.example.ProducerConsumer.ConsumerSocketHandler;
import com.example.ProducerConsumer.ProducerSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final ProducerSocketHandler producerSocketHandler;
    private final ConsumerSocketHandler consumerSocketHandler;
    private static final Logger logger = LoggerFactory.getLogger(ConsumerSocketHandler.class);

    public WebSocketConfig(ProducerSocketHandler producerSocketHandler, ConsumerSocketHandler consumerSocketHandler) {
        this.producerSocketHandler = producerSocketHandler;
        this.consumerSocketHandler = consumerSocketHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        try {
            registry.addEndpoint("/producer-websocket", "/consumer-websocket")
                    .setAllowedOrigins("*")
                    .withSockJS();
            logger.info("WebSocketConfig.registerStompEndpoints() is successfully completed");
        }
        catch (Exception e) {
            logger.error("WebSocketConfig.registerStompEndpoints() is bad. " + e.getMessage());
        }
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        try {
            registry.setApplicationDestinationPrefixes("/app");
            registry.enableSimpleBroker("/topic", "/user");
            logger.info("WebSocketConfig.configureMessageBroker() is successfully completed");
        } catch (Exception e) {
            logger.error("WebSocketConfig.configureMessageBroker() is bad. " + e.getMessage());
        }
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024 * 1024);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        try {
            registry.addHandler(producerSocketHandler, "/producer-websocket")
                    .addHandler(consumerSocketHandler, "/consumer-websocket")
                    .setAllowedOrigins("*")
                    .withSockJS();
            logger.info("WebSocketConfig.registerWebSocketHandlers() is successfully completed");
        }
        catch (Exception e) {
            logger.error("WebSocketConfig.registerWebSocketHandlers() is bad. " + e.getMessage());
        }
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        try {
            registration.interceptors(new ChannelInterceptor() {
                @Override
                public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        // Extract the 'Authorization' header from the standard headers
                        List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");

                        // Check if the header is present
                        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                            String authorizationHeader = authorizationHeaders.get(0);

                            // Set the 'Authorization' header for further processing
                            accessor.setNativeHeader("Authorization", authorizationHeader);
                        }
                    }

                    return message;
                }
            });
            logger.info("WebSocketConfig.configureClientInboundChannel() is successfully completed");
        }
        catch (Exception e) {
            logger.error("WebSocketConfig.configureClientInboundChannel() is bad. " + e.getMessage());
        }
    }
}
