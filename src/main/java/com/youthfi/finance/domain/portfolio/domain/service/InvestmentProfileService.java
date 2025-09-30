package com.youthfi.finance.domain.portfolio.domain.service;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.portfolio.domain.repository.InvestmentProfileRepository;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InvestmentProfileService {

    private final InvestmentProfileRepository investmentProfileRepository;
    private final UserRepository userRepository;

    /**
     * 새로운 투자성향 프로필을 생성합니다.
     * 사용자 존재 여부를 확인한 후, 프로필을 생성 및 저장합니다.
     *
     * @param userId 사용자 ID
     * @param investmentProfile 투자성향 유형
     * @param availableAssets 투자가능 자산
     * @param investmentGoal 투자 목표
     * @return 생성된 InvestmentProfile 엔터티
     * @throws RuntimeException 사용자 미존재 시
     */

    @Transactional
    public InvestmentProfile createInvestmentProfile(Long userId, 
                                                   InvestmentProfile.InvestmentProfileType investmentProfile,
                                                   java.math.BigDecimal availableAssets, 
                                                   InvestmentProfile.InvestmentGoal investmentGoal) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        InvestmentProfile profile = InvestmentProfile.builder()
                .user(user)
                .investmentProfile(investmentProfile)
                .availableAssets(availableAssets)
                .investmentGoal(investmentGoal)
                .build();

        return investmentProfileRepository.save(profile);
    }

    /**
     * 사용자 ID로 투자성향 프로필을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return Optional<InvestmentProfile>
     */

    public Optional<InvestmentProfile> getInvestmentProfileByUserId(Long userId) {
        return investmentProfileRepository.findByUserUserId(userId);
    }

    /**
     * 프로필 ID로 투자성향 프로필을 조회합니다.
     *
     * @param profileId 투자성향 프로필 ID
     * @return Optional<InvestmentProfile>
     */

    public Optional<InvestmentProfile> getInvestmentProfileById(Long profileId) {
        return investmentProfileRepository.findById(profileId);
    }

    /**
     * 투자성향 프로필을 수정합니다.
     * 존재하지 않으면 예외를 던지며, 내부 엔터티의 updateProfile 메서드를 통해 갱신 후 저장합니다.
     *
     * @param profileId 투자성향 프로필 ID
     * @param investmentProfile 투자성향 유형
     * @param availableAssets 투자가능 자산
     * @param investmentGoal 투자 목표
     * @return 수정된 InvestmentProfile 엔터티
     * @throws RuntimeException 투자성향 프로필 미존재 시
     */

    @Transactional
    public InvestmentProfile updateInvestmentProfile(Long profileId, 
                                                   InvestmentProfile.InvestmentProfileType investmentProfile,
                                                   java.math.BigDecimal availableAssets, 
                                                   InvestmentProfile.InvestmentGoal investmentGoal) {
        InvestmentProfile profile = investmentProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("투자성향을 찾을 수 없습니다: " + profileId));

        profile.updateProfile(investmentProfile, availableAssets, investmentGoal);
        return investmentProfileRepository.save(profile);
    }

    /**
     * 투자성향 프로필을 삭제합니다.
     * 존재하지 않으면 예외를 던집니다.
     *
     * @param profileId 투자성향 프로필 ID
     * @throws RuntimeException 투자성향 프로필 미존재 시
     */

    @Transactional
    public void deleteInvestmentProfile(Long profileId) {
        InvestmentProfile profile = investmentProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("투자성향을 찾을 수 없습니다: " + profileId));

        investmentProfileRepository.delete(profile);
    }

    /**
     * 특정 투자성향 유형을 가진 프로필 리스트를 조회합니다.
     *
     * @param investmentProfile 투자성향 유형
     * @return 투자성향 리스트
     */

    public List<InvestmentProfile> getInvestmentProfilesByType(InvestmentProfile.InvestmentProfileType investmentProfile) {
        return investmentProfileRepository.findByInvestmentProfile(investmentProfile);
    }

    /**
     * 특정 투자 목표를 가진 프로필 리스트를 조회합니다.
     *
     * @param investmentGoal 투자 목표
     * @return 투자성향 리스트
     */

    public List<InvestmentProfile> getInvestmentProfilesByGoal(InvestmentProfile.InvestmentGoal investmentGoal) {
        return investmentProfileRepository.findByInvestmentGoal(investmentGoal);
    }

    /**
     * 사용자에게 투자성향 프로필이 존재하는지 여부를 반환합니다.
     *
     * @param userId 사용자 ID
     * @return 존재 여부(boolean)
     */
    
    public boolean existsInvestmentProfile(Long userId) {
        return investmentProfileRepository.findByUserUserId(userId).isPresent();
    }
}


