package com.example.ProducerConsumer;

import com.example.ProducerConsumer.model.UserAudit;
import com.example.ProducerConsumer.service.ProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ProducerSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerSocketHandler.class);
    private final ProducerService producerService;

    public ProducerSocketHandler(@Lazy ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            logger.info("ProducerSocketHandler.handleTextMessage() is started.");
            logger.info("TextMessage message = " + message);
            ObjectMapper objectMapper = new ObjectMapper();
            UserAudit userAudit = objectMapper.readValue(message.getPayload(), UserAudit.class);
            Authentication auth = (Authentication) session.getPrincipal();
            logger.info("Authentication auth = " + auth + ". auth.getName() or userId = " + auth.getName());
            Long userId = Long.valueOf(auth.getName());
            producerService.sendProductDataToConsumer(userId, userAudit);
            logger.info("ProducerSocketHandler.handleTextMessage() is successfully completed.");
        } catch (Exception e) {
            logger.error("ProducerSocketHandler.handleTextMessage() is bad. " + e.getMessage());
        }

    }
}
