package com.example.ProducerConsumer.service;

import com.example.ProducerConsumer.model.UserAudit;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private final SimpMessagingTemplate messagingTemplate;

    public ProducerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendProductDataToConsumer(Long userId, UserAudit userAudit) {
        messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/topic/product-data", userAudit);
    }
}
