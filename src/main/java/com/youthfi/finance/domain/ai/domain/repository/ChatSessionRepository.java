package com.youthfi.finance.domain.ai.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.ai.domain.entity.ChatSession;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    
    Optional<ChatSession> findBySessionUuid(String sessionUuid);
    
    List<ChatSession> findByUserIdAndIsActiveTrue(String userId);
    
    @Query("SELECT cs FROM ChatSession cs WHERE cs.userId = :userId AND cs.isActive = true ORDER BY cs.lastActivityAt DESC")
    List<ChatSession> findActiveSessionsByUserIdOrderByLastActivity(@Param("userId") String userId);
    
    @Query("SELECT cs FROM ChatSession cs WHERE cs.lastActivityAt < :cutoffTime AND cs.isActive = true")
    List<ChatSession> findInactiveSessions(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    long countByUserIdAndIsActiveTrue(String userId);
}
