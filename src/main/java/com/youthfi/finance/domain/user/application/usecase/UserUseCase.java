package com.youthfi.finance.domain.user.application.usecase;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;

    /**
     * 사용자 프로필 조회 (본인만)
     */
    public User getMyProfile(Long userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 사용자 프로필 수정 (본인만)
     */
    @Transactional
    public User updateMyProfile(Long userId, String name, String profileImage) {
        // 본인 확인 로직 (추후 인증 시스템에서 처리)
        return userService.updateUser(userId, name, profileImage);
    }

    /**
     * 사용자 잔고 조회 (본인만)
     */
    public BigDecimal getMyBalance(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getBalance();
    }

    /**
     * 사용자 시드머니 조회 (본인만)
     */
    public BigDecimal getMySeedMoney(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getSeedMoney();
    }

    /**
     * 사용자 수익률 계산 (본인만)
     */
    public BigDecimal calculateMyReturnRate(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        BigDecimal seedMoney = user.getSeedMoney();
        BigDecimal currentBalance = user.getBalance();
        
        if (seedMoney.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        // 수익률 = (현재 잔고 - 시드머니) / 시드머니 * 100
        BigDecimal profit = currentBalance.subtract(seedMoney);
        return profit.divide(seedMoney, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 사용자 수익금 계산 (본인만)
     */
    public BigDecimal calculateMyProfit(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        return user.getBalance().subtract(user.getSeedMoney());
    }

    /**
     * 사용자 존재 여부 확인
     */
    public boolean existsUser(Long userId) {
        return userService.existsUser(userId);
    }
}
