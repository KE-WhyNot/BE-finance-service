package com.youthfi.finance.domain.ai.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.ai.domain.entity.AiServiceLog;

@Repository
public interface AiServiceLogRepository extends JpaRepository<AiServiceLog, Long> {
    
    List<AiServiceLog> findByUserIdOrderByRequestedAtDesc(String userId);
    
    List<AiServiceLog> findBySessionIdOrderByRequestedAtDesc(String sessionId);
    
    @Query("SELECT asl FROM AiServiceLog asl WHERE asl.userId = :userId AND asl.serviceType = :serviceType ORDER BY asl.requestedAt DESC")
    List<AiServiceLog> findByUserIdAndServiceTypeOrderByRequestedAtDesc(
            @Param("userId") String userId, 
            @Param("serviceType") AiServiceLog.ServiceType serviceType);
    
    @Query("SELECT asl FROM AiServiceLog asl WHERE asl.requestedAt BETWEEN :startTime AND :endTime ORDER BY asl.requestedAt DESC")
    List<AiServiceLog> findByRequestedAtBetweenOrderByRequestedAtDesc(
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT COUNT(asl) FROM AiServiceLog asl WHERE asl.userId = :userId AND asl.isSuccess = true")
    long countSuccessfulRequestsByUserId(@Param("userId") String userId);
    
    @Query("SELECT COUNT(asl) FROM AiServiceLog asl WHERE asl.userId = :userId AND asl.isSuccess = false")
    long countFailedRequestsByUserId(@Param("userId") String userId);
    
    @Query("SELECT AVG(asl.processingTimeMs) FROM AiServiceLog asl WHERE asl.userId = :userId AND asl.isSuccess = true")
    Double getAverageProcessingTimeByUserId(@Param("userId") String userId);
    
    Page<AiServiceLog> findByUserId(String userId, Pageable pageable);
}
