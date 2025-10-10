package com.youthfi.finance.domain.user.domain.service;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.global.exception.UserException;
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

    /**
     * 사용자 생성 검증
     */
    private void validateUserCreation(String userId) {
        UserException.validateUserId(userId);
    }

    /**
     * 금액 검증
     */
    private void validateAmount(BigDecimal amount) {
        UserException.validateAmount(amount);
    }

    /**
     * 잔고 충분성 검증
     */
    private void validateSufficientBalance(User user, BigDecimal amount) {
        UserException.validateSufficientBalance(user.getBalance(), amount);
    }
}
