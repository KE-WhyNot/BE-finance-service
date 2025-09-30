package com.youthfi.finance.domain.portfolio.application.usecase;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.portfolio.domain.service.InvestmentProfileService;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InvestmentProfileUseCase {

    private final InvestmentProfileService investmentProfileService;
    private final UserRepository userRepository;

    /**
     * 투자성향 설문 완료 및 저장 또는 업데이트 처리 메서드.
     * 사용자 존재를 확인하고, 투자 가능 자산이 0 초과인지 검증합니다.
     * 기존 투자성향이 있을 경우 업데이트, 없으면 새로 생성합니다.
     *
     * @param userId 사용자 ID
     * @param investmentProfile 투자성향 유형
     * @param availableAssets 투자가능자산 (0 초과여야 함)
     * @param investmentGoal 투자 목표
     * @return 생성 또는 수정된 InvestmentProfile 엔터티
     * @throws RuntimeException 사용자 미존재 시
     * @throws IllegalArgumentException 투자가능자산이 0 이하일 경우
     */

    @Transactional
    public InvestmentProfile completeInvestmentProfile(Long userId, 
                                                     InvestmentProfile.InvestmentProfileType investmentProfile,
                                                     java.math.BigDecimal availableAssets, 
                                                     InvestmentProfile.InvestmentGoal investmentGoal) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        if (availableAssets.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("투자가능자산은 0보다 커야 합니다.");
        }

        Optional<InvestmentProfile> existingProfile = investmentProfileService.getInvestmentProfileByUserId(userId);
        
        if (existingProfile.isPresent()) {
            return investmentProfileService.updateInvestmentProfile(
                existingProfile.get().getProfileId(), 
                investmentProfile, 
                availableAssets, 
                investmentGoal
            );
        } else {
            return investmentProfileService.createInvestmentProfile(
                userId, 
                investmentProfile, 
                availableAssets, 
                investmentGoal
            );
        }
    }

    /**
     * 사용자의 투자성향 정보를 조회합니다.
     * 없을 경우 예외를 발생시킵니다.
     *
     * @param userId 사용자 ID
     * @return 투자성향 엔터티
     * @throws RuntimeException 투자성향 정보 미존재 시
     */

    public InvestmentProfile getMyInvestmentProfile(Long userId) {
        return investmentProfileService.getInvestmentProfileByUserId(userId)
                .orElseThrow(() -> new RuntimeException("투자성향 정보가 없습니다. 먼저 설문을 완료해주세요."));
    }

    /**
     * 사용자의 투자성향 정보를 수정합니다.
     * 사용자 존재 확인 및 투자가능자산 0 초과 조건을 검사합니다.
     *
     * @param userId 사용자 ID
     * @param investmentProfile 투자성향 유형
     * @param availableAssets 투자가능자산 (0 초과여야 함)
     * @param investmentGoal 투자 목표
     * @return 수정된 투자성향 엔터티
     * @throws RuntimeException 사용자 미존재 또는 투자성향 없을 경우
     * @throws IllegalArgumentException 투자가능자산이 0 이하일 경우
     */

    @Transactional
    public InvestmentProfile updateMyInvestmentProfile(Long userId, 
                                                     InvestmentProfile.InvestmentProfileType investmentProfile,
                                                     java.math.BigDecimal availableAssets, 
                                                     InvestmentProfile.InvestmentGoal investmentGoal) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        InvestmentProfile existingProfile = getMyInvestmentProfile(userId);

        if (availableAssets.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("투자가능자산은 0보다 커야 합니다.");
        }

        return investmentProfileService.updateInvestmentProfile(
            existingProfile.getProfileId(), 
            investmentProfile, 
            availableAssets, 
            investmentGoal
        );
    }

    /**
     * 사용자의 투자성향 정보를 삭제합니다.
     * 사용자 및 투자성향 존재 여부 확인 후 삭제합니다.
     *
     * @param userId 사용자 ID
     * @throws RuntimeException 사용자 미존재 또는 투자성향 없을 경우
     */

    @Transactional
    public void deleteMyInvestmentProfile(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        InvestmentProfile existingProfile = getMyInvestmentProfile(userId);
        investmentProfileService.deleteInvestmentProfile(existingProfile.getProfileId());
    }

    /**
     * 특정 투자성향 유형을 가진 사용자 리스트를 조회합니다.
     *
     * @param investmentProfile 투자성향 유형
     * @return 투자성향 리스트
     */

    public List<InvestmentProfile> getUsersByInvestmentProfile(InvestmentProfile.InvestmentProfileType investmentProfile) {
        return investmentProfileService.getInvestmentProfilesByType(investmentProfile);
    }

    /**
     * 특정 투자 목표를 가진 사용자 리스트를 조회합니다.
     *
     * @param investmentGoal 투자 목표
     * @return 투자성향 리스트
     */

    public List<InvestmentProfile> getUsersByInvestmentGoal(InvestmentProfile.InvestmentGoal investmentGoal) {
        return investmentProfileService.getInvestmentProfilesByGoal(investmentGoal);
    }

    /**
     * 사용자가 투자성향 설문을 완료했는지 여부를 반환합니다.
     *
     * @param userId 사용자 ID
     * @return 투자성향 완료 여부
     */
    public boolean hasCompletedInvestmentProfile(Long userId) {
        return investmentProfileService.existsInvestmentProfile(userId);
    }
}


