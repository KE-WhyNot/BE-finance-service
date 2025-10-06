package com.youthfi.finance.domain.user.domain.service;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 생성 (도메인 로직) - 초기 잔고 1,000만원 고정
     */
    public User createUser(String userId) {
        // 비즈니스 규칙 검증
        validateUserCreation(userId);
        
        return User.builder()
                .userId(userId)
                .balance(new BigDecimal("10000000")) // 초기 잔고 1,000만원 고정
                .build();
    }

    /**
     * 잔고 차감 (매매 시)
     */
    public void subtractBalance(User user, BigDecimal amount) {
        // 비즈니스 규칙 검증
        validateAmount(amount);
        validateSufficientBalance(user, amount);
        
        user.subtractBalance(amount);
    }

    /**
     * 잔고 증가 (매매 시)
     */
    public void addBalance(User user, BigDecimal amount) {
        // 비즈니스 규칙 검증
        validateAmount(amount);
        
        user.addBalance(amount);
    }

    /**
     * 사용자 조회 (ID)
     */
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }


    /**
     * 사용자 존재 여부 확인
     */
    public boolean existsUser(String userId) {
        return userRepository.existsById(userId);
    }

    // ==================== 비즈니스 규칙 검증 ====================

    /**
     * 사용자 생성 검증
     */
    private void validateUserCreation(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (userId.length() > 50) {
            throw new IllegalArgumentException("사용자 ID는 50자를 초과할 수 없습니다.");
        }
    }

    /**
     * 금액 검증
     */
    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("금액은 필수입니다.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("금액은 0보다 커야 합니다.");
        }
    }

    /**
     * 잔고 충분성 검증
     */
    private void validateSufficientBalance(User user, BigDecimal amount) {
        if (user.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("잔고가 부족합니다. 현재 잔고: " + user.getBalance() + ", 요청 금액: " + amount);
        }
    }
}
