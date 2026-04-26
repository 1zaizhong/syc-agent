package com.syc.salesAgent.repository;

import com.syc.salesAgent.entity.ChatMemoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMemoryRepository extends JpaRepository<ChatMemoryEntity, Long> {

    //查记忆
    Optional<ChatMemoryEntity> findBySessionId(String sessionId);

    //清空会话id
    void deleteBySessionId(String sessionId);
}