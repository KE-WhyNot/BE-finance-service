package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.domain.entity.UserStock;
import com.youthfi.finance.domain.stock.domain.repository.UserStockRepository;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStockService {

    private final UserStockRepository userStockRepository;
    private final UserRepository userRepository;

    /**
     * 사용자별 보유 주식 조회 
     */
    public List<UserStock> getUserStocksByUserId(String userId) {
        return userStockRepository.findByUserUserId(userId);
    }

    /**
     * 특정 종목 보유 주식 조회 
     */
    public Optional<UserStock> getUserStockByUserIdAndStockId(String userId, String stockId) {
        return userStockRepository.findByUserUserIdAndStockStockId(userId, stockId);
    }
    
}
