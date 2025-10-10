package com.youthfi.finance.domain.portfolio.application.usecase;

import com.youthfi.finance.domain.portfolio.application.dto.request.CompleteInvestmentProfileRequest;
import com.youthfi.finance.domain.portfolio.application.dto.request.UpdateInvestmentProfileRequest;
import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.mapper.PortfolioMapper;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.portfolio.domain.service.InvestmentProfileService;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.global.exception.PortfolioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InvestmentProfileUseCase {

    private final InvestmentProfileService investmentProfileService;
    private final UserRepository userRepository;
    private final PortfolioMapper portfolioMapper;

    /**
     * 투자성향 설문 완료 및 저장 또는 업데이트 처리 메서드
     */

    @Transactional
    public InvestmentProfileResponse completeInvestmentProfile(String userId, CompleteInvestmentProfileRequest request) {

        Optional<InvestmentProfile> existingProfile = investmentProfileService.getInvestmentProfileByUserId(userId);
        InvestmentProfile profile;

        if (existingProfile.isPresent()) {
            profile = investmentProfileService.updateInvestmentProfile(
                existingProfile.get().getProfileId(), 
                request.investmentProfile(), 
                request.availableAssets(), 
                request.investmentGoal(),
                request.interestedSectorNames()
            );
        } else {
            profile = investmentProfileService.createInvestmentProfile(
                userId, 
                request.investmentProfile(), 
                request.availableAssets(), 
                request.investmentGoal(),
                request.interestedSectorNames()
            );
        }
        return portfolioMapper.toInvestmentProfileResponse(profile);
    }

    /**
     * 사용자 투자성향 정보를 조회
     */

    public InvestmentProfileResponse getMyInvestmentProfile(String userId) {
        InvestmentProfile profile = investmentProfileService.getInvestmentProfileByUserId(userId)
                .orElseThrow(() -> PortfolioException.investmentProfileNotFound());
        
        return portfolioMapper.toInvestmentProfileResponse(profile);
    }

    /**
     * 사용자 투자성향 정보를 수정
     */

    @Transactional
    public InvestmentProfileResponse updateMyInvestmentProfile(String userId, UpdateInvestmentProfileRequest request) {
        userRepository.findById(userId)
                .orElseThrow(() -> PortfolioException.userNotFound(userId));

        InvestmentProfile existingProfile = investmentProfileService.getInvestmentProfileByUserId(userId)
                .orElseThrow(() -> PortfolioException.investmentProfileNotFound());

        PortfolioException.validateAvailableAssets(request.availableAssets());

        InvestmentProfile profile = investmentProfileService.updateInvestmentProfile(
            existingProfile.getProfileId(), 
            request.investmentProfile(), 
            request.availableAssets(), 
            request.investmentGoal(),
            request.interestedSectorNames()
        );
        
        return portfolioMapper.toInvestmentProfileResponse(profile);
    }


    /**
     * 사용자 투자성향 설문을 완료 여부 반환.
     */
    public boolean hasCompletedInvestmentProfile(String userId) {
        return investmentProfileService.existsInvestmentProfile(userId);
    }
}


