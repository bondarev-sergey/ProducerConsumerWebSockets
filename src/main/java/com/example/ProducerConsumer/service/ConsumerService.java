package com.example.ProducerConsumer.service;

import com.example.ProducerConsumer.model.UserAudit;
import com.example.ProducerConsumer.repository.UserAuditRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerService {
    private final UserAuditRepository userAuditRepository;

    public ConsumerService(UserAuditRepository userAuditRepository) {
        this.userAuditRepository = userAuditRepository;
    }

    public void saveProductData(Long userId, UserAudit productDto) {
        UserAudit userAudit = new UserAudit();
        userAudit.setUserId(userId);
        userAudit.setIsActive(productDto.getIsActive());
        userAudit.setColor(productDto.getColor());
        userAudit.setNumber(productDto.getNumber());
        userAudit.setMessage(productDto.getMessage());
        userAuditRepository.save(userAudit);
    }

    public List<UserAudit> getUserAuditData(Long userId) {
        return userAuditRepository.findByUserId(userId);
    }
}
