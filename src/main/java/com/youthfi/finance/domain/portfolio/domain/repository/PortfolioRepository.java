package com.youthfi.finance.domain.portfolio.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserUserIdOrderByCreatedAtDesc(String userId);
    
    @Query("SELECT DISTINCT p FROM Portfolio p " +
           "LEFT JOIN FETCH p.portfolioStocks ps " +
           "LEFT JOIN FETCH ps.stock s " +
           "LEFT JOIN FETCH s.sector " +
           "WHERE p.user.userId = :userId " +
           "ORDER BY p.createdAt DESC")
    List<Portfolio> findByUserUserIdWithStocksOrderByCreatedAtDesc(@Param("userId") String userId);

}


