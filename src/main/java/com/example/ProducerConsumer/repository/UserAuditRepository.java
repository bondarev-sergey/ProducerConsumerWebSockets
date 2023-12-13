package com.example.ProducerConsumer.repository;

import com.example.ProducerConsumer.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAuditRepository extends JpaRepository<UserAudit, Long> {
    List<UserAudit> findByUserId(Long userId);
}
