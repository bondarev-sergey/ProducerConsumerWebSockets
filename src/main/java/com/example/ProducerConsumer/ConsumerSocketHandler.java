package com.example.ProducerConsumer;

import com.example.ProducerConsumer.model.UserAudit;
import com.example.ProducerConsumer.service.ConsumerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ConsumerSocketHandler extends TextWebSocketHandler {
    private final ConsumerService consumerService;

    public ConsumerSocketHandler(@Lazy ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserAudit userAudit = objectMapper.readValue(message.getPayload(), UserAudit.class);
        Authentication auth = (Authentication) session.getPrincipal();
        Long userId = Long.valueOf(auth.getName());
        consumerService.saveProductData(userId, userAudit);
    }
}
